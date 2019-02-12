
/*  Starter project for Mobile Platform Development in Semester B Session 2018/2019
    You should use this project as the starting point for your assignment.
    This project simply reads the data from the required URL and displays the
    raw data in a TextField
*/

//
// Name                 _________________
// Student ID           _________________
// Programme of Study   _________________
//

// Update the package name to include your Student Identifier
package com.example.coursework;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    private TextView rawDataDisplay;
    private Button startButton;
    private String result = "";
    private String url1="";
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";

    private DrawerLayout drawerLayout;
    public ActionBarDrawerToggle barToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout)findViewById(R.id.mainDrawer);

        barToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(barToggle);
        barToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        NavigationView navigationView = findViewById(R.id.drawer);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()){
                    case R.id.home:
                        Log.e("Pressed", "Home");
                        break;
                    case R.id.search:
                        Log.e("Pressed", "Search");
                        break;
                    case R.id.event:
                        Log.e("Pressed", "Event");
                        fragment = new EventsFragment();
                        break;
                    case R.id.recent:
                        Log.e("Pressed", "Recent");
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

        // Set up the raw links to the graphical components
        rawDataDisplay = (TextView)findViewById(R.id.rawDataDisplay);
        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(this);

        // More Code goes here
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (barToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View aview)
    {
        startProgress();
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
                //
                // Throw away the first 2 header lines before parsing
                //
                //
                //
                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                    Log.e("MyTag",inputLine);

                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
            }

            //
            // Now that you have the xml data you can parse it
            //


            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !

            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    try{
                        // older code in commit #78639f7d8c07eac14d4c7ab04f900b8e69aa72aa
                        parseXML(result);
                    }
                    catch (Exception e){
                        Log.e("UI Thread", e.getMessage());
                    }
                }
            });
        }

        private void parseXML(String xml){
            XmlPullParserFactory pF;

            try{
                pF = XmlPullParserFactory.newInstance();
                XmlPullParser p = pF.newPullParser();
//                InputStream iStream = xml;
                p.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                p.setInput(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)), null);
                startParse(p);
            }
            catch (Exception e){
                Log.e("ParseException", e.getMessage());
            }
        }

        private void startParse(XmlPullParser p) throws IOException, XmlPullParserException {
            ArrayList<QuakeItem> quakes = new ArrayList<>();
            int eventType = p.getEventType();
            QuakeItem quake = null;

            while (eventType != XmlPullParser.END_DOCUMENT){
                String element = null;

                switch (eventType){
                    case XmlPullParser.START_TAG:
                        element = p.getName();

                        if (element.equals("item")){
                            quake = new QuakeItem();
                            quakes.add(quake);
                        }
                        else if (quake != null){
                            if (element.equals("title")){
                                quake.title = p.nextText();
                            }
                            else if (element.equals("description")){
                                quake.description = p.nextText();
                            }
                            else if (element.equals("link")){
                                quake.link = p.nextText();
                            }
                            else if (element.equals("pubDate")){
                                quake.pubDate = p.nextText();
                            }
                            else if (element.equals("category")){
                                quake.category = p.nextText();
                            }
                            else if (element.equals("geo:lat")){
                                quake.lat = Float.parseFloat(p.nextText());
                            }
                            else if (element.equals("geo:long")){
                                quake.lon = Float.parseFloat(p.nextText());
                            }
                        }
                        break;
                }
                // end of file
                eventType = p.next();
            }

            updateTextView(quakes);
            
        }

        private void updateTextView(ArrayList<QuakeItem> quakes){
            StringBuilder b = new StringBuilder();

            for (QuakeItem quake: quakes){
                b.append(quake.title).append("LAT: ")
                        .append(quake.lat).append(" LON: ")
                        .append(quake.lon).append("\n\n");
            }

            rawDataDisplay.setText(b.toString());
        }

    }

}