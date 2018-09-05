package com.aqhmal.login;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    String EmailHolder;
    TextView Email;
    Button LogOUT;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Email = findViewById(R.id.messageTxt);
        LogOUT = findViewById(R.id.btnLogout);
        Intent intent = getIntent();
        //Receiving User Email Send By MainActivity.
        EmailHolder = intent.getStringExtra(MainActivity.UserEmail);
        //Setting up received email to TextView.
        Email.setText(Email.getText().toString() + " " + EmailHolder);
        //Adding click listener to Log Out button.
        LogOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Finishing currrent Home activity on button click.
                finish();
                Toast.makeText(HomeActivity.this, "Log out Successfull", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
