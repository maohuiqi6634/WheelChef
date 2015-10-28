package com.wheelchef.wheelchef.registerlogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wheelchef.wheelchef.R;
import com.wheelchef.wheelchef.main.MainActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button bLogin, bRegisterLink;
    EditText etUsername, etPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        bRegisterLink = (Button) findViewById(R.id.bRegisterLink);

        bLogin.setOnClickListener(this);
        bRegisterLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bLogin:
                if(true){
                    startActivity(new Intent(this, MainActivity.class));
                }
                break;
            case R.id.bRegisterLink:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }
}
