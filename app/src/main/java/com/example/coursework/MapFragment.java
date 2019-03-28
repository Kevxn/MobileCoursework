package com.example.coursework;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class MapFragment extends Fragment implements OnMapReadyCallback{

    MapView map;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Map");

        boolean isInnerFragment = false;

        // code below changes hamburger menu to back button
        if (isInnerFragment){
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((MainActivity) getActivity()).barToggle.setDrawerIndicatorEnabled(false);
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((MainActivity)getActivity()).drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        else {
            ((MainActivity)getActivity()).barToggle.setDrawerIndicatorEnabled(true);
        }

        return inflater.inflate(R.layout.map_fragment, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        final DataInterface data = new DataInterface();
        data.startProgress();

        map = view.findViewById(R.id.mapFragmentMapView);
        map.onCreate(mapViewBundle);


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
//                                progressBar.setVisibility(View.VISIBLE);
//                                header.setVisibility(View.GONE);
//                                itemList.setVisibility(View.GONE);
                            }
                        });
                    }
                    else{
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                progressBar.setVisibility(View.GONE);
//                                header.setVisibility(View.VISIBLE);
//                                itemList.setVisibility(View.VISIBLE);
                                for (final QuakeItem item: items) {
                                    final float lat = item.getLat();
                                    float lon = item.getLon();

                                    final LatLng point = new LatLng(lat, lon);
                                    //MarkerOptions markerOptions = new MarkerOptions();


                                    map.getMapAsync(new OnMapReadyCallback() {
                                        @Override
                                        public void onMapReady(final GoogleMap googleMap) {

                                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                                                    != PackageManager.PERMISSION_GRANTED
                                                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                                                    != PackageManager.PERMISSION_GRANTED) {
                                                return;
                                            }

                                            if (map != null){
                                                googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                                    @Override
                                                    public View getInfoWindow(Marker marker) {
                                                        return null;
                                                    }

                                                    @Override
                                                    public View getInfoContents(Marker marker) {

                                                        final QuakeItem quake = (QuakeItem) marker.getTag();

                                                        View view = getLayoutInflater().inflate(R.layout.maps_popup, null);
                                                        TextView location = view.findViewById(R.id.txt_map_Location);
                                                        TextView datetime = view.findViewById(R.id.txt_map_date);
                                                        TextView latlon = view.findViewById(R.id.txt_map_LatLon);
                                                        TextView magnitude = view.findViewById(R.id.txt_map_magnitude);
                                                        // Button btnViewEarthquake = view.findViewById(R.id.btn_map_view_earthquake);

                                                        setQuakeColour(magnitude, quake.getMagnitude());
                                                        DateFormat formattedDate = new SimpleDateFormat("dd/MM/yyyy");



                                                        location.setText(quake.getLocation());
                                                        datetime.setText(formattedDate.format(quake.getDate()));
                                                        latlon.setText("" + Float.toString(quake.getLat()) + "°, " + Float.toString(quake.getLon()) + "°");
                                                        magnitude.setText(Float.toString(quake.getMagnitude()));

                                                        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                                            @Override
                                                            public void onInfoWindowClick(Marker marker) {
                                                                Intent i = new Intent(getActivity(), DetailedQuakeViewActivity.class);
                                                                i.putExtra("QuakeObject", new Gson().toJson(quake));
                                                                getContext().startActivity(i);
                                                            }
                                                        });
                                                        return view;
                                                    }
                                                });
                                            }

                                            googleMap.setMyLocationEnabled(true);

                                            Marker marker = googleMap.addMarker(new MarkerOptions().position(point));
                                            marker.setTag(item);

                                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 6f));
                                        }
                                    });
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        map.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onMapReady(GoogleMap map){

    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        map.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        map.onStop();
    }

    @Override
    public void onPause() {
        map.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        map.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }

    public void setQuakeColour(TextView txtMagnitude, float mag){
        if (mag < -0.7){
            txtMagnitude.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_steel_blue));
        }
        else if (mag >= -0.7 && mag < -0.3){
            txtMagnitude.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.powder_blue));
        }
        else if (mag >= -0.3 && mag < 0.2){
            txtMagnitude.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.aqua));
        }
        else if (mag >= 0.2 && mag < 0.5){
            txtMagnitude.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_sea_green));
        }
        else if (mag >= 0.5 && mag < 0.8){
            txtMagnitude.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.medium_sea_green));
        }
        else if (mag >= 0.8 && mag < 1.1){
            txtMagnitude.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.khaki));
        }
        else if (mag >= 1.1 && mag < 1.4){
            txtMagnitude.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.yellow));
        }
        else if (mag >= 1.4 && mag < 1.8){
            txtMagnitude.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dark_orange));
        }
        else if (mag >= 1.8 && mag < 2.2){
            txtMagnitude.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange_red));
        }
        else if (mag >= 2.2 && mag < 2.5){
            txtMagnitude.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
        }
        else if (mag >= 2.5){
            txtMagnitude.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dark_red));
        }
    }
}
