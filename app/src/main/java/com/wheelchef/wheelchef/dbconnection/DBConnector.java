package com.wheelchef.wheelchef.dbconnection;

/**
 * Created by lyk on 10/18/2015.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class DBConnector {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public void initialize(Activity activity) throws Exception {
        try{
            if (connect == null){
                new TaskInitialize(activity).execute();
            }
        }catch (Exception e) {
            throw e;
        }
    }
    private class TaskInitialize extends AsyncTask<Void, Void, Void> {
        private Activity activity;

        public TaskInitialize(Activity activity){
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                // Setup the connection with the DB
                connect = DriverManager
                        .getConnection("jdbc:mysql://23.229.175.161:3306/wheelchefdb_test?"
                                + "user=wcDBT3stAdm1n&password=wcDBT3st@dm1n");
                Toast.makeText(activity.getApplicationContext(),
                        "Connection to database set up!", Toast.LENGTH_LONG).show();
            }catch (Exception e) {
                Log.d("mmmmmmmmmmmmmmmmmmmm","Initializing connection to db.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }


    }
}
