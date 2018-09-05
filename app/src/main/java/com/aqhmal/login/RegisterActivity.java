package com.aqhmal.login;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends AppCompatActivity {

    EditText input_email, input_name, input_password;
    Button btnRegister, btnLogin;
    String NameHolder, EmailHolder, PasswordHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    String SQLiteDataBaseQueryHolder;
    DBHelper sqLiteHelper;
    Cursor cursor;
    String F_Result = "Not Found";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        input_email = findViewById(R.id.input_email);
        input_name = findViewById(R.id.input_name);
        input_password = findViewById(R.id.input_password);
        btnRegister = findViewById(R.id.btn_register);
        btnLogin = findViewById(R.id.btn_login);
        sqLiteHelper = new DBHelper(this);

        // Adding click listener to register button.
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating SQLite Database if not exists
                SQLiteDataBaseBuild();
                // Create SQLite Table if not exists
                SQLiteTableBuild();
                // Checking EditText is empty or not
                CheckEditTextStatus();
                // Method to check email already exists or not
                CheckingEmailAlreadyExistsOrNot();
                // Empty EditText After Done Inserting Process
                EmptyEditTextAfterDataInsert();
            }
        });

        // Open login activity
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // SQLite Database Build Method
    public void SQLiteDataBaseBuild() {
        sqLiteDatabaseObj = openOrCreateDatabase(DBHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    // SQLite Table Build Method
    public void SQLiteTableBuild() {
        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS " + DBHelper.TABLE_NAME + "(" +
                DBHelper.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                DBHelper.NAME + " VARCHAR," +
                DBHelper.EMAIL + " VARCHAR," +
                DBHelper.PASSWORD + " VARCHAR);");
    }

    // Insert data into SQLite Database Method
    public void InsertDataIntoSQLiteDatabase() {
        // If EditText is not empty then this block will be executed
        if(EditTextEmptyHolder) {
            // SQLite Query To Insert Data Into Table
            SQLiteDataBaseQueryHolder = "INSERT INTO " + DBHelper.TABLE_NAME + " (name, email, password) VALUES('" + NameHolder + "', '" + EmailHolder + "', '" + PasswordHolder +"');";
            // Executing Query
            sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);
            // Closing SQLite database Object
            sqLiteDatabaseObj.close();
            // Printing toast message after done inserting.
            Toast.makeText(RegisterActivity.this, "User Registered Successfully", Toast.LENGTH_LONG).show();
        } else {
            // Printing toast message if any of EditText is empty.
            Toast.makeText(RegisterActivity.this, "Please Fill All The Required Fields", Toast.LENGTH_LONG).show();
        }
    }

    // Empty EditText after done inserting process method
    public void EmptyEditTextAfterDataInsert() {
        input_name.getText().clear();
        input_email.getText().clear();
        input_password.getText().clear();
    }

    // Method to check EditText is empty or not
    public void CheckEditTextStatus() {
        // Getting value from all EdiText and storing into String Variables.
        NameHolder = input_name.getText().toString();
        EmailHolder = input_email.getText().toString();
        PasswordHolder = input_password.getText().toString();
        EditTextEmptyHolder = !TextUtils.isEmpty(NameHolder) && !TextUtils.isEmpty(EmailHolder) && !TextUtils.isEmpty(PasswordHolder);
    }

    // Check Email is already exists or not.
    public void CheckingEmailAlreadyExistsOrNot() {
        // Opening SQLite Database Write Permission
        sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();
        // Adding search email query to cursor
        cursor = sqLiteDatabaseObj.query(DBHelper.TABLE_NAME, null, " " + DBHelper.EMAIL + "=?", new String[] {EmailHolder}, null, null, null);
        while(cursor.moveToNext()) {
            if(cursor.isFirst()) {
                cursor.moveToFirst();
                // If Email is already exists then Result variable value set as Email Found
                F_Result = "Email Found";
                // Closing Cursor
                cursor.close();
            }
        }
        // Calling method to check final result and insert data into SQLite Database
        CheckFinalResult();
    }

    // Checking Result
    public void CheckFinalResult() {
        // Checking whether email is already exists or not.
        if(F_Result.equalsIgnoreCase("Email Found")) {
            // If email is exists then toast message will display.
            Toast.makeText(RegisterActivity.this, "Email Already Exists", Toast.LENGTH_LONG).show();
        } else {
            // If email doesn't exists then user registration details will be entered to SQLite Database
            InsertDataIntoSQLiteDatabase();
        }
        F_Result = "Not Found";
    }
}
