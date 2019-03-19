package com.example.coursework;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    Context fragmentContext;
    Switch dateRangeToggle;
    TextView lblSecondDay;
    Button btnChooseSecondDay;
    Button btnChooseFirstDay;
    Button btnHomeSearch;
    TextView lblHomeHeader;
    TextView lblChosenDate1;
    TextView lblChosenDate2;
    DatePickerDialog.OnDateSetListener dateSetListener1;
    DatePickerDialog.OnDateSetListener dateSetListener2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Home");

        return inflater.inflate(R.layout.home_fragment, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        dateRangeToggle = view.findViewById(R.id.toggleDateRange);
        btnChooseSecondDay = view.findViewById(R.id.btnChooseSecondDay);
        btnChooseFirstDay = view.findViewById(R.id.btnChooseFirstDay);
        btnHomeSearch = view.findViewById(R.id.btnHomeSearch);
        lblHomeHeader = view.findViewById(R.id.lblHomeHeader);
        lblChosenDate1 = view.findViewById(R.id.lblChosenDate1);
        lblChosenDate2 = view.findViewById(R.id.lblChosenDate2);
        lblSecondDay = view.findViewById(R.id.lblSecondDay);

        // make second button hidden by default
        lblChosenDate1.setVisibility(View.GONE);
        lblChosenDate2.setVisibility(View.GONE);
        lblSecondDay.setVisibility(View.GONE);
        btnChooseSecondDay.setVisibility(View.GONE);

        dateRangeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    // show second date button
                    lblSecondDay.setVisibility(View.VISIBLE);
                    btnChooseSecondDay.setVisibility(View.VISIBLE);
                    lblHomeHeader.setText("Show earthquakes between...");

                }
                else{
                    // show only first button
                    lblSecondDay.setVisibility(View.GONE);
                    lblChosenDate2.setVisibility(View.GONE);
                    btnChooseSecondDay.setVisibility(View.GONE);
                    lblHomeHeader.setText("Show earthquakes on...");
                }
            }
        });

        // date picker
        btnChooseFirstDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen, dateSetListener1, year, month, day);
                datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePicker.show();
            }
        });

        btnChooseSecondDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen, dateSetListener2, year, month, day);
                datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePicker.show();
            }
        });

        dateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String buildDate = Integer.toString(dayOfMonth) + "/" + Integer.toString(month) + "/" + Integer.toString(year);
                lblChosenDate1.setVisibility(View.VISIBLE);
                lblChosenDate1.setText(buildDate);
            }
        };

        dateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String buildDate = Integer.toString(dayOfMonth) + "/" + Integer.toString(month) + "/" + Integer.toString(year);
                lblChosenDate2.setVisibility(View.VISIBLE);
                lblChosenDate2.setText(buildDate);
            }
        };

        btnHomeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date start = new Date(0);
                Date end = new Date(0);
                boolean useEndDate = false;

                try{
                    start = new SimpleDateFormat("dd/MM/yyyy").parse((String)lblChosenDate1.getText());

                    if (lblChosenDate2.getVisibility() == View.VISIBLE){
                        useEndDate = true;
                        end = new SimpleDateFormat("dd/MM/yyyy").parse((String)lblChosenDate2.getText());

                    }
                }
                catch (ParseException pEx){
                    Log.e("ParseException", pEx.getMessage());
                }

                if (useEndDate){
                    // send both dates to next activity
                    Log.e("FirstDate, SecondDate: ", start.toString() + ", " + end.toString());
                }
                else{
                    // send first date only to next activity
                    Log.e("FirstDate: ", start.toString());
                }
            }
        });

//        final TextView txtShowData = view.findViewById(R.id.txtDisplayData);
////        final LinearLayout layout = view.findViewById(R.id.home_card_holder);
//        view.findViewById(R.id.homeBtnGetData).setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View view){
//
//                Toast.makeText(getActivity(), "Getting data...", Toast.LENGTH_SHORT).show();
//                DataInterface data = new DataInterface();
//                data.startProgress();
//                ArrayList<QuakeItem> items = data.getQuakeItems();
//
//                updateTextView(txtShowData, items);
//            }
//
//        });
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
