package com.example.coursework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

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
        TextView txtLocation = (TextView)convertView.findViewById(R.id.lv_location);
        TextView txtMagnitude = (TextView)convertView.findViewById(R.id.lv_magnitiude);

        txtLocation.setText(q.getLocation());
        txtMagnitude.setText(Float.toString(q.getMagnitude()));

        return convertView;
    }
}
