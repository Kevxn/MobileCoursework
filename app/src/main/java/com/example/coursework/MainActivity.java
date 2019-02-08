
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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import com.example.coursework.R;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    private TextView rawDataDisplay;
    private Button startButton;
    private String result = "";
    private String url1="";
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the raw links to the graphical components
        rawDataDisplay = (TextView)findViewById(R.id.rawDataDisplay);
        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(this);

        // More Code goes here
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
                        List<QuakeItem> quakeItems = new ArrayList<QuakeItem>();
                        NodeList items = loadXMLFromString(result).getElementsByTagName("item");
                        Log.e("SizeofItems", Integer.toString(items.getLength()));

                        for (int i=0; i< items.getLength(); i++){

//                            Log.e("Items", items.item(i).getChildNodes().item(0).gette());
                            Log.e("LoopDebug", items.item(i).getFirstChild().getTextContent() + "");
                            Log.e("Iteration", Integer.toString(i));

                            Node title = items.item(i).getFirstChild();
                            Node description = title.getNextSibling();
                            Node link = description.getNextSibling();
                            Node pubDate = link.getNextSibling();
                            Node category = pubDate.getNextSibling();
                            Node lat = category.getNextSibling();
                            Node lon = lat.getNextSibling();
                            // horrible, will likely change later

                            QuakeItem temp = new QuakeItem(title.getTextContent(), description.getTextContent(), link.getTextContent(), pubDate.getTextContent(), category.getTextContent(), Float.parseFloat(lat.getTextContent()), Float.parseFloat(lon.getTextContent()));
                            quakeItems.add(temp);
                            // meaty constructor
                        }

                        Log.e("QuakeItemSize", Integer.toString(quakeItems.size()));
                        Log.e("ItemObject", items.toString());
                        NodeList titles = loadXMLFromString(result).getElementsByTagName("title");

                        String display = "";
                        for (int i=0; i<quakeItems.size(); i++){
                            display+= "\n" + quakeItems.get(i).title;
                        }

//                        String title = titles.item(0).getTextContent();
                        rawDataDisplay.setText(display);
                    }
                    catch (Exception e){
                        Log.e("UI Thread", e.getMessage());
                    }
                }
            });
        }

        public Document loadXMLFromString(String xml) throws Exception
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            return builder.parse(new ByteArrayInputStream(xml.getBytes()));
            // adapted from shsteimer on StackOverflow,
            // https://stackoverflow.com/questions/562160/in-java-how-do-i-parse-xml-as-a-string-instead-of-a-file
        }

    }

}