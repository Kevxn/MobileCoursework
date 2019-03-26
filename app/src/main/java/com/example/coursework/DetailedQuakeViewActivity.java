package com.example.coursework;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.Serializable;

public class DetailedQuakeViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private TextView location;
    private TextView date;
    private TextView latlon;
    private TextView depth;
    private TextView magnitude;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_quake_view);

        // hopefully this gives us a back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle mapViewBundle = null;
        if (savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        Intent intent = getIntent();
        Serializable s = intent.getSerializableExtra("QuakeObject");
        QuakeItem quake = new Gson().fromJson(intent.getSerializableExtra("QuakeObject").toString(), QuakeItem.class);
        getSupportActionBar().setTitle(quake.getLocation());

        location = findViewById(R.id.txt_detailed_location);
        date = findViewById(R.id.txt_detailed_date);
        latlon = findViewById(R.id.txt_detailed_latlon);
        depth= findViewById(R.id.txt_detailed_depth);
        magnitude = findViewById(R.id.txt_detailed_magnitude);

        location.setText(quake.getLocation());
        date.setText(quake.getDate().toString());
        latlon.setText("" + Float.toString(quake.getLat()) + "°, " + Float.toString(quake.getLon()) + "°");
        depth.setText(Float.toString(quake.getDepth()) + "km Depth");
        magnitude.setText(Float.toString(quake.getMagnitude()) + " Magnitude");

        final MarkerOptions marker = new MarkerOptions();
        final LatLng point = new LatLng(quake.getLat(), quake.getLon());
        marker.position(point);

        // adding point to map

        mapView = (MapView) findViewById(R.id.detailed_mapView);
        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.addMarker(marker);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 7f));
            }
        });
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
    public void onBackPressed()
    {
        super.onBackPressed();
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
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
