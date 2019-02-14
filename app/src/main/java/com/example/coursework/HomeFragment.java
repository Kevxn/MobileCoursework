package com.example.coursework;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    Context fragmentContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Home");
        return inflater.inflate(R.layout.home_fragment, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextView txtShowData = view.findViewById(R.id.txtDisplayData);
//        final LinearLayout layout = view.findViewById(R.id.home_card_holder);
        view.findViewById(R.id.homeBtnGetData).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view){

                Toast.makeText(getActivity(), "Getting data...", Toast.LENGTH_SHORT).show();
                DataInterface data = new DataInterface();
                data.startProgress();
                ArrayList<QuakeItem> items = data.getQuakeItems();

                updateTextView(txtShowData, items);
            }

        });
    }

    private void updateTextView(TextView txt, ArrayList<QuakeItem> quakes){
        StringBuilder b = new StringBuilder();

        for (QuakeItem quake: quakes){
            b.append("Location: ").append(quake.getLocation()).append("\nLAT: ")
                    .append(quake.getLat()).append("\nLON: ")
                    .append(quake.getLon()).append("\nDATE: ")
                    .append(quake.getDate()).append("\nDEPTH: ")
                    .append(quake.getDepth()).append("\nMAGNITUDE: ")
                    .append(quake.getMagnitude()).append("\n\n");
        }
        txt.setText(b.toString());
    }

}
