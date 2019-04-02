package com.example.coursework;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SearchResultsListViewAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<QuakeItem> earthquakes = new ArrayList<>();

    public SearchResultsListViewAdapter(Context context, ArrayList<QuakeItem> earthquakes){
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.search_results_listview_item, parent, false);
        }

        QuakeItem q = (QuakeItem)getItem(position);
        TextView lblDecorator = (TextView)convertView.findViewById(R.id.lblDecorator);
        TextView txtLocation = (TextView)convertView.findViewById(R.id.txt_nearby_event_Location);
        TextView txtMagnitude = (TextView)convertView.findViewById(R.id.txt_nearby_event_magnitude);
        TextView txtDate = (TextView)convertView.findViewById(R.id.txt_nearby_event_date);
        TextView txtLatLon = (TextView)convertView.findViewById(R.id.txt_nearby_event_LatLon);

        DateFormat formattedDate = new SimpleDateFormat("dd/MM/yyyy");

        lblDecorator.setText(q.getDecorator());
        txtLocation.setText(q.getLocation());
        txtMagnitude.setText(Float.toString(q.getMagnitude()));
        txtDate.setText(formattedDate.format(q.getDate()));
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
