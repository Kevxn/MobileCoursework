package com.example.coursework;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

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
        view.findViewById(R.id.homeBtnGetData).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view){
                Toast.makeText(getActivity(), "Getting data...", Toast.LENGTH_SHORT).show();
                DataInterface df = new DataInterface();
                df.startProgress();
                updateTextView(txtShowData, df.getQuakeItems());
            }

        });
    }

    private void updateTextView(TextView txt, ArrayList<QuakeItem> quakes){
        StringBuilder b = new StringBuilder();

        for (QuakeItem quake: quakes){
            b.append(quake.title).append("LAT: ")
                    .append(quake.lat).append(" LON: ")
                    .append(quake.lon).append("\n\n");
        }

        txt.setText(b.toString());
    }

}
