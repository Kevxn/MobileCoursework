package com.example.coursework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {

    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        lv = findViewById(R.id.searchResults);

        Intent intent = getIntent();
        Serializable s = intent.getSerializableExtra("QuakeObject");
        QuakeItem quake = new Gson().fromJson(intent.getSerializableExtra("QuakeObject").toString(), QuakeItem.class);


    }

    public void fillListView(){
        ArrayList<QuakeItem> quakes = new ArrayList<>();
        ArrayAdapter<QuakeItem> adapter = new ArrayAdapter<QuakeItem>(this, R.layout.search_results_listview_item, quakes);

        lv.setAdapter(adapter);
    }

}
