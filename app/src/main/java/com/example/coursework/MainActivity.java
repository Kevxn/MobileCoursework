/*
    Name                 Kevin Gray
    Student ID           S1715611
    Programme of Study   BSc Computing
*/

// Update the package name to include your Student Identifier
package com.example.coursework;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import com.example.coursework.R;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity
{
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle barToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean isInnerFragment = false;

        // below is setup code for the navigation menu
        drawerLayout = (DrawerLayout)findViewById(R.id.mainDrawer);
        barToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(barToggle);
        barToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // code below changes hamburger menu to back button
        if (isInnerFragment){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            barToggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        else {
            barToggle.setDrawerIndicatorEnabled(true);
        }

        if(savedInstanceState == null) {
            getSupportFragmentManager().
                    beginTransaction().replace(R.id.displayFragment, new HomeFragment()).commit();
        }

        NavigationView navigationView = findViewById(R.id.drawer);
                navigationView.bringToFront();
                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        Fragment fragment = null;
                        switch (menuItem.getItemId()){
                            case R.id.home:
                                Log.e("Pressed", "Home");
                                fragment = new HomeFragment();
                                break;
                            case R.id.search:
                                Log.e("Pressed", "Search");
                                fragment = new SearchFragment();
                                break;
                            case R.id.event:
                                Log.e("Pressed", "Event");
                                fragment = new EventsFragment();
                                break;
                            case R.id.recent:
                                Log.e("Pressed", "Recent");
                                fragment = new RecentFragment();
                                break;
                            case R.id.map:
                                Log.e("Pressed", "Map");
                                fragment = new MapFragment();
                                break;

                        }

                        if (fragment != null){
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.replace(R.id.displayFragment, fragment);
                            transaction.commit();
                            drawerLayout.closeDrawer(Gravity.START);
                        }

                        return true;
            }
        });
        // setup code for elements that were originally in activity_main.xml
        // have moved into home_fragment.xml

    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();



        getSupportFragmentManager().popBackStackImmediate();
        if (barToggle.onOptionsItemSelected(item))
        {
            Log.e("HOME BUTTON CLICKED", "NOW");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
}