package com.example.coursework;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataInterface {

    String result = "";
    String parsedXML = "";
    String urlSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    ArrayList<QuakeItem> quakeItems = null;

    public ArrayList<QuakeItem> getQuakeItems(){

        while (quakeItems == null){
            Log.e("quakeItems null", "");
        }

        return quakeItems;
    }

    public void startProgress() {
        // Run network access on a separate thread;
        new Thread(new DataInterface.Task(urlSource)).start();
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable {
        private String url;

        public Task(String aurl) {
            url = aurl;
        }

        @Override
        public void run() {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag", "in run");

            try {
                Log.e("MyTag", "in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
                //
                // Throw away the first 2 header lines before parsing
                //
                //
                //
                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
                    //Log.e("MyTag", inputLine);

                }
                in.close();
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception");
            }

            quakeItems = parseXML(result);
            parsedXML = parseXML(result).toString();
            Log.e("PARSEDXML", parsedXML);

        }
    }

    private ArrayList<QuakeItem> parseXML(String xml){
        XmlPullParserFactory pF;
        ArrayList<QuakeItem> quakes = null;

        try{
            pF = XmlPullParserFactory.newInstance();
            XmlPullParser p = pF.newPullParser();
            p.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            p.setInput(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)), null);
            quakes = startParse(p);
        }
        catch (Exception e){
            Log.e("ParseException", e.getMessage());
        }

        return quakes;
    }

    private ArrayList<QuakeItem> startParse(XmlPullParser p) throws IOException, XmlPullParserException {
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

        return quakes;
    }
}
