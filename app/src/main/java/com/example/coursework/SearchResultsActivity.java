package com.example.coursework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SearchResultsActivity extends AppCompatActivity {

    ListView lv;
    ProgressBar progressBar;
    Date startDate;
    Date endDate;
    Date singleDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        lv = findViewById(R.id.searchResults);
        progressBar = findViewById(R.id.search_progress_bar);

        Intent intent = getIntent();
        // try to get one day, else try 2
        try {

            Bundle options = new Gson().fromJson(intent.getSerializableExtra("SearchOptions").toString(), Bundle.class);
            if (options.get("Date") != null){

                // single day
                double test = options.getDouble("Date");
                singleDay = new Date((long)test);
                Log.e("SINGLE DAY", singleDay.toString());
            }
            else if (options.get("FirstDate") != null){

                // date range
                double fDate = options.getDouble("FirstDate");
                startDate = new Date((long)fDate);

                if (options.get("EndDate") != null){
                    double eDate = options.getDouble("EndDate");
                    endDate = new Date((long) eDate);
                    Log.e("DATE RANGE", startDate.toString() + " - " + endDate.toString());
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
                            }
                        });
                    }
                }
                catch(Exception ex){
                    Log.e("THREAD BROKEN: ", ex.getMessage());
                }

//                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////                    @Override
////                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                        fillListView(items, Integer.parseInt(parent.getSelectedItem().toString()));
////                        // parent.getSelectedItem().toString();
////                        Log.e("SELECTED: ", parent.getSelectedItem().toString());
////                    }
////
////                    @Override
////                    public void onNothingSelected(AdapterView<?> parent) {
////
////                    }
////                });
            }

        }).start();

    }

    public void fillListView(){
        ArrayList<QuakeItem> quakes = new ArrayList<>();
        ArrayAdapter<QuakeItem> adapter = new ArrayAdapter<QuakeItem>(this, R.layout.search_results_listview_item, quakes);

        lv.setAdapter(adapter);
    }

}
