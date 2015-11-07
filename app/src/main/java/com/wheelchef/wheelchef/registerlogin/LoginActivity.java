package com.wheelchef.wheelchef.registerlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wheelchef.wheelchef.R;
import com.wheelchef.wheelchef.jsonutils.JSONParser;
import com.wheelchef.wheelchef.main.MainActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button bLogin, bRegisterLink;
    private EditText etUsername, etPassword;
    private String username, password;

    // url to get all products list
    private static final String URL_CUSTOMER_LOGIN = "http://10.27.53.225/wheelchef/db_customer_login.php";
    private static final String TAG = "LoginActivity";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MSG = "message";
    // Progress Dialog
    private ProgressDialog pDialog;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();


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
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                new LoginTask().execute();
                break;
            case R.id.bRegisterLink:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    private class LoginTask extends AsyncTask<String, String, String> {
        int success = 0;
        String msg = "";
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging in...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(URL_CUSTOMER_LOGIN, "POST", params);
            Log.d(TAG,"with the username: "+username);
            Log.d(TAG,"with the password: "+password);
            Log.d(TAG,"json received is: "+json.toString());

            try {
                // Checking for SUCCESS TAG
                success = json.getInt(TAG_SUCCESS);
                msg = json.getString(TAG_MSG);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            if (success == 1) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                Log.d(TAG, "login succeed!");
                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG,"login failed!");
                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        }

    }


}
