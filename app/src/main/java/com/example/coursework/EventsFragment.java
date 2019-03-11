package com.example.coursework;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class EventsFragment extends Fragment {

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

        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(getActivity());

        Context mContext = (MainActivity)getActivity();
        final TextView showCoords = view.findViewById(R.id.nearby_events_show_coords);

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
                    showCoords.setText(lat + ", " + lon);
                }
            }
        });

        view.findViewById(R.id.btnEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // Toast.makeText(getActivity(), "Selected Nearby Events..", Toast.LENGTH_SHORT).show();
            }
        });
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
}
