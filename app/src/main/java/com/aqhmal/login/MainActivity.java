package com.aqhmal.login;

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

public class MainActivity extends AppCompatActivity {
    EditText inputEmail, inputPassword;
    Button loginBtn, registerBtn;
    String EmailHolder, PasswordHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    DBHelper sqLiteHelper;
    Cursor cursor;
    String TempPassword = "NOT FOUND";
    public static final String UserEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
        sqLiteHelper = new DBHelper(this);

        // Adding click listener to log in button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Calling EditText is empty or not
                CheckEditTextStatus();
                // Calling login method
                LoginFunction();
            }
        });

        // Addting click listener to register button
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Opening new user registration activity using intent on button click.
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    // Login function starts from here.
    public void LoginFunction() {
        if(EditTextEmptyHolder) {
            // Opening SQLite Database write permission
            sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();
            // Adding search email query to cursor
            cursor = sqLiteDatabaseObj.query(DBHelper.TABLE_NAME, null, " " + DBHelper.EMAIL + "=?", new String[] {EmailHolder}, null, null, null);
            while(cursor.moveToNext()) {
                if(cursor.isFirst()) {
                    cursor.moveToFirst();
                    // Storing Password associated with entered email
                    TempPassword = cursor.getString(cursor.getColumnIndex(DBHelper.PASSWORD));
                    // Closing cursor
                    cursor.close();
                }
            }
            // Calling method to check final result
            CheckFinalResult();
        } else {
            // If any of login EditText empty then this block will be executed
            Toast.makeText(MainActivity.this, "Please enter username or password", Toast.LENGTH_LONG).show();
        }
    }

    // Checking EditText is empty or not.
    public void CheckEditTextStatus() {
        // Getting value from all EditText and storing into String variables
        EmailHolder = inputEmail.getText().toString();
        PasswordHolder = inputPassword.getText().toString();
        // Checking EditText is empty or not
        EditTextEmptyHolder = !TextUtils.isEmpty(EmailHolder) && !TextUtils.isEmpty(PasswordHolder);
    }

    // Checking entered password from SQLite Database email associated password.
    public void CheckFinalResult() {
        if(TempPassword.equalsIgnoreCase(PasswordHolder)) {
            Toast.makeText(MainActivity.this, "Login successfully", Toast.LENGTH_LONG).show();
            // Going to Home Activity after login success message
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            // Sending Email to Home Activity Using Intent
            intent.putExtra(UserEmail, EmailHolder);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Username or Password is incorrect.", Toast.LENGTH_LONG).show();
        }
        TempPassword = "NOT_FOUND";
    }
}
