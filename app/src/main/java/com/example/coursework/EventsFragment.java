package com.example.coursework;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EventsFragment extends Fragment {

    Spinner spinner;
    ListView lv;
    TextView noQuakeFoundLabel;
    double lat;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    double lon;

    private FusedLocationProviderClient client;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Nearby Events");

        return inflater.inflate(R.layout.events_fragment, null);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // spinner setup code
        spinner = (Spinner)view.findViewById(R.id.nearby_radius_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.nearby_radius, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // end spinner setup code


        final DataInterface data = new DataInterface();
        data.startProgress();

        lv = (ListView) view.findViewById(R.id.recentNearbyEvents);
        noQuakeFoundLabel = (TextView) view.findViewById(R.id.nearby_event_no_quakes_found);

        final ProgressBar progressBar = view.findViewById(R.id.nearbyEventsLoading);
        final LinearLayout header = view.findViewById(R.id.nearby_event_item_list_header);
        header.setVisibility(View.GONE);
        final LinearLayout itemList = view.findViewById(R.id.nearby_event_item_list_layout);
        itemList.setVisibility(View.GONE);
        progressBar.isIndeterminate();

        // setting up ListView onClickListener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuakeItem test = (QuakeItem)parent.getAdapter().getItem(position);
                Intent i = new Intent(getActivity(), DetailedQuakeViewActivity.class);
                i.putExtra("QuakeObject", new Gson().toJson(test));
                Log.e("CLICKED: ", test.getLocation());
                getContext().startActivity(i);
            }
        });

        // start geolocation code
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED){
            return;
        }
        client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    String lat = Double.toString(location.getLatitude());
                    String lon = Double.toString(location.getLongitude());
                    // Toast.makeText(getActivity(), lat + ", " + lon, Toast.LENGTH_SHORT).show();
                    // showCoords.setText(lat + ", " + lon);
                    setLat(location.getLatitude());
                    setLon(location.getLongitude());
                }
            }
        });

        new Thread(new Runnable() {

            ArrayList<QuakeItem> items;
            public void run() {
                items=data.getQuakeItems();
                try{
                    if (items == null){
                        // show loading
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.VISIBLE);
                                noQuakeFoundLabel.setVisibility(View.GONE);
                                header.setVisibility(View.GONE);
                                itemList.setVisibility(View.GONE);
                            }
                        });

                    }
                    else{
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                noQuakeFoundLabel.setVisibility(View.GONE);
                                header.setVisibility(View.VISIBLE);
                                itemList.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
                catch(Exception ex){
                    Log.e("THREAD BROKEN: ", ex.getMessage());
                }

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        fillListView(items, Integer.parseInt(parent.getSelectedItem().toString()), getLat(), getLon());
                        // parent.getSelectedItem().toString();
                        Log.e("SELECTED: ", parent.getSelectedItem().toString());
                        Log.e("LAT FROM DEVICE: ", Double.toString(getLat()));
                        Log.e("LON FROM DEVICE: ", Double.toString(getLon()));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

        }).start();
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    // this function calculates the distance between 2 points on a 2D plane
    private double getEuclideanDistance(double x1, double x2, double y1, double y2){
        return 0f;
    }

    // this function gets the distance between 2 points taking
    // Earths curvature into account
    private double getOrthodromicDistance(double x1, double y1, double x2, double y2){

        double lon1 = Math.toRadians(x1);
        double lon2 = Math.toRadians(x2);
        double lat1 = Math.toRadians(y1);
        double lat2 = Math.toRadians(y2);

        // Haversine equation
        double distanceLon = lon2 - lon1;
        double distanceLat = lat2 - lat1;

        double a = Math.pow(Math.sin(distanceLat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(distanceLon / 2),2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double earthRadiusKM = 6371;

        return c * earthRadiusKM;
    }

    public void fillListView(ArrayList<QuakeItem> items, int searchRadius, double userLat, double userLon){

        // int selectedValue = 50;

        Collections.sort(items, new Comparator<QuakeItem>() {
            @Override
            public int compare(QuakeItem o1, QuakeItem o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });

        // items = new ArrayList<QuakeItem>(items.subList(0, selectedValue));
        ArrayList<QuakeItem> displayItems = new ArrayList<>();
        for(QuakeItem i: items){
            double distance = getOrthodromicDistance(userLat, userLon, i.getLat(), i.getLon());
            if (distance <= searchRadius){
                displayItems.add(i);
            }
        }

        EarthquakeListViewAdapter adapter = new EarthquakeListViewAdapter(getActivity(), displayItems);

        if (adapter.getCount() == 0){
            noQuakeFoundLabel.setText("Couldn't find any earthquakes in a " + Double.toString(searchRadius) + "km radius.");
            noQuakeFoundLabel.setVisibility(View.VISIBLE);
        }
        else{
            lv.setAdapter(adapter);
            noQuakeFoundLabel.setVisibility(View.GONE);
        }
    }
}
