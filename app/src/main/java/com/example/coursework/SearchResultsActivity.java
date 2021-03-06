package com.example.coursework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class SearchResultsActivity extends AppCompatActivity {

    ListView lv;
    ProgressBar progressBar;
    String searchLocation;
    Date startDate;
    Date endDate;
    Date singleDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Results (S1715611)");

        boolean usingSearchBox = false;

        lv = findViewById(R.id.searchResults);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuakeItem test = (QuakeItem)parent.getAdapter().getItem(position);
                Intent i = new Intent(SearchResultsActivity.this, DetailedQuakeViewActivity.class);
                i.putExtra("QuakeObject", new Gson().toJson(test));
                Log.e("CLICKED: ", test.getLocation());
                startActivity(i);
            }
        });

        progressBar = findViewById(R.id.search_progress_bar);
        Intent intent = getIntent();
        // try to get one day, else try 2
        try {

            Bundle options = new Gson().fromJson(intent.getSerializableExtra("SearchOptions").toString(), Bundle.class);
            if (options.get("Date") != null){

                // single day
                if(options.getString("SearchLocation") != null){
                    searchLocation = options.getString("SearchLocation");
                    usingSearchBox = true;
                }

                double test = options.getDouble("Date");
                singleDay = new Date((long)test);
                Log.e("SINGLE DAY", singleDay.toString());

                if (usingSearchBox){
                    Log.e("SEARCH BOX: ", searchLocation);
                }
            }
            else if (options.get("FirstDate") != null){

                // date range
                if(options.getString("SearchLocation") != null){
                    searchLocation = options.getString("SearchLocation");
                    usingSearchBox = true;
                }

                double fDate = options.getDouble("FirstDate");
                startDate = new Date((long)fDate);

                if (options.get("EndDate") != null){
                    double eDate = options.getDouble("EndDate");
                    endDate = new Date((long) eDate);
                    Log.e("DATE RANGE", startDate.toString() + " - " + endDate.toString());

                    if (usingSearchBox){
                        Log.e("SEARCH BOX: ", searchLocation);

                    }
                }
            }
            else {
                Log.e("Date", "Error getting options from bundle.");
            }
        }
        catch (Exception ex){
            Log.e("ERROR GETTING BUNDLE", ex.getMessage());
            ex.printStackTrace();
        }


        final LinearLayout itemList = findViewById(R.id.search_results_item_list_layout);
        final DataInterface data = new DataInterface();
        data.startProgress();
        new Thread(new Runnable() {

            ArrayList<QuakeItem> items;
            public void run() {
                items=data.getQuakeItems();
                try{
                    if (items == null){
                        // show loading
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.VISIBLE);
                                itemList.setVisibility(View.GONE);
                            }
                        });

                    }
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                itemList.setVisibility(View.VISIBLE);
                                if (singleDay != null){
                                    fillListView(items, searchLocation, singleDay, new Date(0));
                                }
                                else{
                                    fillListView(items, searchLocation, startDate, endDate);
                                }
                            }
                        });
                    }
                }
                catch(Exception ex){
                    Log.e("THREAD BROKEN: ", ex.getMessage());
                }

            }

        }).start();

    }

    public void fillListView(ArrayList<QuakeItem> items, String searchLocation, Date startDate, Date endDate){

        boolean usingDateRange = true;
        boolean searchNoDates = false;

        if (endDate.equals(new Date(0))){
            usingDateRange = false;
        }

        // this fixes a timezone bug, checks if dates are before 1971
        // as dates are initialized to 01/01/1970
        // at the moment new Date(0) returns a value in GMT+1, therefore causing problems
        if (startDate.before(new Date(31536000)) && endDate.before(new Date(31536000))){
            searchNoDates = true;
        }

        // searchLocation may be null
        if (searchLocation == null){
            searchLocation = "";
        }

        ArrayList<QuakeItem> filteredItems = new ArrayList<>();
        QuakeItem northernly, easterly, westerly, southernly, highestMag, highestDepth;
        QuakeItem currentMaxNorthernly, currentMaxEasterly, currentMaxWesterly, currentMaxSouthernly,
                currentMaxMagnitude, currentMaxDepth;


        for (QuakeItem item: items){
            if (item.getLocation().toLowerCase().contains(searchLocation) || item.getLocation().contains(searchLocation.toUpperCase()) || searchLocation == ""){
                if (usingDateRange){
                    if ((item.getDate().after(startDate) && item.getDate().before(endDate))){
                        // date range search
                        filteredItems.add(item);
                    }
                }
                else{
                    if (item.getDate().equals(startDate)){
                        // single day search
                        filteredItems.add(item);
                    }
                    if (searchNoDates){
                        filteredItems.add(item);
                    }
                }
            }
        }

        try{
            // initialize temp items
            currentMaxNorthernly = filteredItems.get(0);
            currentMaxEasterly = currentMaxNorthernly;
            currentMaxSouthernly = currentMaxEasterly;
            currentMaxWesterly = currentMaxSouthernly;
            currentMaxMagnitude = currentMaxWesterly;
            currentMaxDepth = currentMaxMagnitude;



        for (QuakeItem item: filteredItems){

                if (item.getLat() > currentMaxNorthernly.getLat()){
                    currentMaxNorthernly = item;
                }
                if (item.getLat() < currentMaxSouthernly.getLat()){
                    currentMaxSouthernly = item;
                }
                if (item.getLon() > currentMaxEasterly.getLon()){
                    currentMaxEasterly = item;
                }
                if (item.getLon() < currentMaxWesterly.getLon()){
                    currentMaxWesterly = item;
                }
                if (item.getMagnitude() > currentMaxMagnitude.getMagnitude()){
                    currentMaxMagnitude = item;
                }
                if (item.getDepth() > currentMaxDepth.getDepth()){
                    currentMaxDepth = item;
                }
        }

        /* this code isn't ideal, however it solves an issue when
           the same earthquake is for example the most northenly
           AND the most easterly */

        QuakeItem maxNorthernly, maxSouthernly, maxEasterly, maxWesterly, maxMagnitude, deepest;

        maxNorthernly = currentMaxNorthernly.clone(currentMaxNorthernly);
        maxNorthernly.setDecorator(" Most northernly");

        maxEasterly = currentMaxEasterly.clone(currentMaxEasterly);
        maxEasterly.setDecorator(" Most easterly");

        maxSouthernly = currentMaxSouthernly.clone(currentMaxSouthernly);
        maxSouthernly.setDecorator(" Most southernly");

        maxWesterly = currentMaxWesterly.clone(currentMaxWesterly);
        maxWesterly.setDecorator(" Most westerly");

        maxMagnitude = currentMaxMagnitude.clone(currentMaxMagnitude);
        maxMagnitude.setDecorator(" Highest magnitude");

        deepest = currentMaxDepth.clone(currentMaxDepth);
        deepest.setDecorator(" Deepest");

        ArrayList<QuakeItem> displayItems = new ArrayList<>();
        displayItems.addAll(Arrays.asList(maxNorthernly, maxSouthernly, maxEasterly, maxWesterly, maxMagnitude, deepest));

        //EarthquakeListViewAdapter adapter = new EarthquakeListViewAdapter(this, displayItems);
        SearchResultsListViewAdapter adapter = new SearchResultsListViewAdapter(this, displayItems);

        lv.setAdapter(adapter);

        // TODO: CHANGE SO THAT IF ONLY SEARCH IS PROVIDED, DATES ARE IGNORED.

        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "No results, please adjust search criteria.", Toast.LENGTH_LONG).show();
            Log.e("FILTERED ITEMS", "FILTERED ITEMS MAY BE EMPTY");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }


}
