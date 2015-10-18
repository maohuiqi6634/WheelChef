package com.wheelchef.wheelchef.main;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wheelchef.wheelchef.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ListView navList;
    private DrawerLayout drawerLayout;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    private HomeFragment homeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navList = (ListView) findViewById(R.id.navlist);
        ArrayList<String> navArray = new ArrayList<String>();
        navArray.add("Home");
        navArray.add("My Account");
        navArray.add("Current Order");
        navArray.add("Payment");
        navArray.add("History");
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
            case 5:

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
}
