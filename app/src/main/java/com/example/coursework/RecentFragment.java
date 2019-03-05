package com.example.coursework;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RecentFragment extends Fragment {


    Handler threadHandler = new Handler();
    ListView lv;
    Spinner spinner;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Recent");

        return inflater.inflate(R.layout.recent_fragment, null);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // do section below on new thread

        spinner = (Spinner)view.findViewById(R.id.limit_results_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.recent_limiter, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        DataInterface data = new DataInterface();
        data.startProgress();
        final ArrayList<QuakeItem> items = data.getQuakeItems();

        //view.findViewById(R.id.loadingIcon).setVisibility(View.GONE);

        lv = (ListView) view.findViewById(R.id.recentQuakes);
        final int selectedValue = 10; // initializing in case it fails

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fillListView(items, Integer.parseInt(parent.getSelectedItem().toString()));
                // parent.getSelectedItem().toString();
                Log.e("SELECTED: ", parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //fillListView(items, selectedValue);

    }

    public void fillListView(ArrayList<QuakeItem> items, int selectedValue){

        Collections.sort(items, new Comparator<QuakeItem>() {
            @Override
            public int compare(QuakeItem o1, QuakeItem o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });

        if (selectedValue > items.size()){
            selectedValue = items.size();
        }

        items = new ArrayList<QuakeItem>(items.subList(0, selectedValue));
        EarthquakeListViewAdapter adapter = new EarthquakeListViewAdapter(getActivity(), items);
        lv.setAdapter(adapter);
    }
}
