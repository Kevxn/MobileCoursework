package com.example.coursework;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class EarthquakeListViewAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<QuakeItem> earthquakes = new ArrayList<>();

    public EarthquakeListViewAdapter(Context context, ArrayList<QuakeItem> earthquakes){
        this.mContext = context;
        this.earthquakes = earthquakes;
    }

    @Override
    public int getCount() {
        return earthquakes.size();
    }

    @Override
    public Object getItem(int position) {
        return earthquakes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.recent_listview_item, parent, false);
        }

        QuakeItem q = (QuakeItem)getItem(position);
        TextView txtLocation = (TextView)convertView.findViewById(R.id.txt_recent_Location);
        TextView txtMagnitude = (TextView)convertView.findViewById(R.id.txt_recent_magnitude);
        TextView txtDate = (TextView)convertView.findViewById(R.id.txt_recent_date);
        TextView txtLatLon = (TextView)convertView.findViewById(R.id.txt_recent_LatLon);

        txtLocation.setText(q.getLocation());
        txtMagnitude.setText(Float.toString(q.getMagnitude()));
        txtDate.setText(q.getDate().toString());
        txtLatLon.setText(q.getLat() + "°,  " + q.getLon() + "°");

        float mag = q.getMagnitude();
        setQuakeColour(txtMagnitude, mag);


        return convertView;
    }

    public void setQuakeColour(TextView txtMagnitude, float mag){
        if (mag < -0.7){
            txtMagnitude.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_steel_blue));
        }
        else if (mag >= -0.7 && mag < -0.3){
            txtMagnitude.setBackgroundColor(ContextCompat.getColor(mContext, R.color.powder_blue));
        }
        else if (mag >= -0.3 && mag < 0.2){
            txtMagnitude.setBackgroundColor(ContextCompat.getColor(mContext, R.color.aqua));
        }
        else if (mag >= 0.2 && mag < 0.5){
            txtMagnitude.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_sea_green));
        }
        else if (mag >= 0.5 && mag < 0.8){
            txtMagnitude.setBackgroundColor(ContextCompat.getColor(mContext, R.color.medium_sea_green));
        }
        else if (mag >= 0.8 && mag < 1.1){
            txtMagnitude.setBackgroundColor(ContextCompat.getColor(mContext, R.color.khaki));
        }
        else if (mag >= 1.1 && mag < 1.4){
            txtMagnitude.setBackgroundColor(ContextCompat.getColor(mContext, R.color.yellow));
        }
        else if (mag >= 1.4 && mag < 1.8){
            txtMagnitude.setBackgroundColor(ContextCompat.getColor(mContext, R.color.dark_orange));
        }
        else if (mag >= 1.8 && mag < 2.2){
            txtMagnitude.setBackgroundColor(ContextCompat.getColor(mContext, R.color.orange_red));
        }
        else if (mag >= 2.2 && mag < 2.5){
            txtMagnitude.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
        }
        else if (mag >= 2.5){
            txtMagnitude.setBackgroundColor(ContextCompat.getColor(mContext, R.color.dark_red));
        }
    }
}
