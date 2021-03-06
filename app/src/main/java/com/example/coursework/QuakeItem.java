package com.example.coursework;

import android.util.Log;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

public class QuakeItem implements Cloneable{
    public String title;
    public String description;
    public String link;
    public String pubDate;
    public String category;
    public float lat;
    public float lon;
    public String location;
    public float magnitude;
    public float depth;
    public Date date;


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public String getDecorator() {
        return category;
    }

    public void setDecorator(String category) {
        this.category = category;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public QuakeItem(String title, String description, String link, String pubDate, String category, float lat, float lon){
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
        this.category = category;
        this.lat = lat;
        this.lon = lon;
    }

    public QuakeItem(String location, Date date, float depth, float magnitude, float lat, float lon){
        this.location = location;
        this.date = date;
        this.depth = depth;
        this.magnitude = magnitude;
        this.lat = lat;
        this.lon = lon;
    }

    public QuakeItem(){
        // empty constructor
    }

    // functions for cleaning the parsed data and sorting
    // into appropriate Java data types

    public HashMap<Object, Object> cleanParsedData(QuakeItem item){

        HashMap<String, String> map = new HashMap<String, String>();
        // String desc = "Origin date/time: Wed, 30 Jan 2019 11:53:15 ; Location: KINGSTONE,HEREFORDSHIRE ; Lat/long: 52.015,-2.861 ; Depth: 8 km ; Magnitude: 1.0";
        String desc = item.getDescription();
        String[] kvps = desc.split(";");

        for (int i=0;i<kvps.length;i++){
            String[] tmp = kvps[i].split(":");
            map.put(tmp[0].trim(), tmp[1].trim());
        }

        HashMap<Object, Object> temp = new HashMap<>();
        temp = rawDataToJavaDataType(map);
        return temp;
    }

    /*
    * The purpose of the method below is to convert the raw parsed XML
    * into its appropriate Java data type. For example, dates are converted to Date
    * objects, lat/lon's are converted to floats etc. This is to allow for
    * easier use of QuakeItem objects throughout development.
    * */
    private HashMap<Object, Object> rawDataToJavaDataType(HashMap<String, String> map){

        HashMap<Object, Object> typedMap = new HashMap<>();

        for (HashMap.Entry<String, String> entry : map.entrySet()){

            if ("Origin date/time".equals(entry.getKey())){

                String temp = entry.getValue().substring(5);
                String finalDate = temp.substring(0, temp.length() - 3);

                try{
                    DateFormat format = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                    Date date = format.parse(finalDate);

                    typedMap.put("date", date);
                    // adding formatted datatype to new typed HashMap
                }

                catch(Exception e){
                    Log.e("Error parsing Date", e.getMessage());
                }
            }

            else if ("Location".equals(entry.getKey())){
                typedMap.put("location", entry.getValue().trim());
            }

            else if ("Lat/long".equals(entry.getKey())){
                //splitting value
                try{
                    // trying to parse from String to float
                    String[] latAndLongValue = entry.getValue().split(",");
                    float latVal = Float.parseFloat(latAndLongValue[0]);
                    float longVal = Float.parseFloat(latAndLongValue[1].trim());

                    typedMap.put("lat", latVal);
                    typedMap.put("lon", longVal);
                }
                catch (Exception ex){
                    Log.e("Error parsing LATLONG", ex.getMessage());
                }
            }

            else if ("Depth".equals(entry.getKey())){
                float depth = Float.parseFloat(entry.getValue().substring(0, entry.getValue().length() - 3));
                typedMap.put("depth", depth);
            }

            else if ("Magnitude".equals(entry.getKey())){
                float magnitude = Float.parseFloat(entry.getValue());
                typedMap.put("magnitude", magnitude);
            }

        }
        Log.e("typedMap", typedMap.toString());
        return typedMap;
    }

    // this function creates a copy of the original QuakeItem by value
    // as the Java default is by reference (modifies the original)
    // which sometimes causes undesirable behaviour
     public QuakeItem clone(QuakeItem original) {
         try {
             final QuakeItem result = (QuakeItem) super.clone();

             result.setLocation(original.getLocation());
             result.setLat(original.getLat());
             result.setLon(original.getLon());
             result.setMagnitude(original.getMagnitude());
             result.setDepth(original.getDepth());
             result.setDecorator(original.getDecorator());
             result.setDate(original.getDate());

             return result;
         } catch (final CloneNotSupportedException ex) {
             throw new AssertionError();
         }
     }
}
