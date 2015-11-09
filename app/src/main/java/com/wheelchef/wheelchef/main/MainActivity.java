package com.wheelchef.wheelchef.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wheelchef.wheelchef.R;
import com.wheelchef.wheelchef.utils.ConnectionParams;
import com.wheelchef.wheelchef.utils.PrefUtil;
import com.wheelchef.wheelchef.utils.JSONParser;
import com.wheelchef.wheelchef.registerlogin.LoginActivity;
import com.wheelchef.wheelchef.registerlogin.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ListView navList;
    private DrawerLayout drawerLayout;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private static final String TAG = "LoginActivity";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MSG = "message";
    private HomeFragment homeFragment;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if(intent.getBooleanExtra(LoginActivity.NEEDVERIFY,false)){
            String username = PrefUtil.getStringPreference(SessionManager.USERNAME,this);
            String password = PrefUtil.getStringPreference(SessionManager.PASSWORD,this);
            new VerifyTask(username,password).execute();
        }

        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navList = (ListView) findViewById(R.id.navlist);
        ArrayList<String> navArray = new ArrayList<String>();
        navArray.add("Home");
        navArray.add("My Account");
        navArray.add("Current Order");
        navArray.add("Payment");
        navArray.add("History");
        navArray.add("Log out");
        navList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, navArray);
        navList.setOnItemClickListener(this);
        navList.setAdapter(adapter);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.opendrawer,R.string.closedrawer);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        fragmentManager = getSupportFragmentManager();


        homeFragment = new HomeFragment();


        loadSelection(0);
    }


    private void loadSelection(int i){
        navList.setItemChecked(i,true);
        switch (i){
            case 0:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentholder, homeFragment);
                fragmentTransaction.commit();
                break;
            case 1:
                MyAccountFragment myAccountFragment = new MyAccountFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentholder, myAccountFragment);
                fragmentTransaction.commit();
                break;
            case 2:
                CurrentOrderFragment currentOrderFragment = new CurrentOrderFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentholder, currentOrderFragment);
                fragmentTransaction.commit();
                break;
            case 3:

                break;
            case 4:

                break;
            case 5:// log out
                SessionManager.logout(this);
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
        }

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == android.R.id.home){
            if(drawerLayout.isDrawerOpen(navList)){
                drawerLayout.closeDrawer(navList);
            }else{
                drawerLayout.openDrawer(navList);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        loadSelection(position);

        drawerLayout.closeDrawer(navList);
    }


    private class VerifyTask extends AsyncTask<String, String, String> {
        int success = 0;
        String msg = "";
        String username;
        String password;

        VerifyTask(String username,String password){
            this.username = username;
            this.password = password;
        }
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(ConnectionParams.URL_CUSTOMER_LOGIN, "POST", params);
            Log.d(TAG, "with the username: " + username);
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
            if (success == 1) {
                Log.d(TAG, "login succeed!");
            } else {
                SessionManager.logout(MainActivity.this);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                Log.d(TAG, "login failed!");
                finish();
            }
        }

    }
}
