package com.example.coursework;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.w3c.dom.Text;

public class DetailedQuakeViewFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private TextView location;
    private TextView date;
    private TextView latlon;
    private TextView depth;
    private TextView magnitude;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        boolean isInnerFragment = true;

        // code below changes hamburger menu to back button
        if (isInnerFragment){
            //((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((MainActivity) getActivity()).barToggle.setDrawerIndicatorEnabled(false);
            ((MainActivity)getActivity()).drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        else {
            ((MainActivity)getActivity()).barToggle.setDrawerIndicatorEnabled(true);
        }

        QuakeItem quake = getQuakeFromBundle();

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(quake.getLocation());

        return inflater.inflate(R.layout.detailed_quake_view, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // code below is for map

        Bundle mapViewBundle = null;
        if (savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        QuakeItem quake = getQuakeFromBundle();

        location = view.findViewById(R.id.txt_detailed_location);
        date = view.findViewById(R.id.txt_detailed_date);
        latlon = view.findViewById(R.id.txt_detailed_latlon);
        depth= view.findViewById(R.id.txt_detailed_depth);
        magnitude = view.findViewById(R.id.txt_detailed_magnitude);

        location.setText(quake.getLocation());
        date.setText(quake.getDate().toString());
        latlon.setText("" + Float.toString(quake.getLat()) + "°, " + Float.toString(quake.getLon()) + "°");
        depth.setText(Float.toString(quake.getDepth()) + "km Depth");
        magnitude.setText(Float.toString(quake.getMagnitude()) + "Magnitude");

        final MarkerOptions marker = new MarkerOptions();
        final LatLng point = new LatLng(quake.getLat(), quake.getLon());
        marker.position(point);


        // adding point to map

        mapView = (MapView) view.findViewById(R.id.detailed_mapView);
        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.addMarker(marker);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 7f));
            }
        });

        // mapView.getMapAsync(this);

        super.onViewCreated(view, savedInstanceState);
    }

    public QuakeItem getQuakeFromBundle(){
        String fromJson;
        Bundle quakeItem = getArguments();
        fromJson = quakeItem.getString("QuakeObject");
        QuakeItem quake = new Gson().fromJson(fromJson, QuakeItem.class);

        return quake;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
