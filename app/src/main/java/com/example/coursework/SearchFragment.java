package com.example.coursework;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class SearchFragment extends Fragment {

    Context fragmentContext;
    Switch dateRangeToggle;
    TextView lblSecondDay;
    Button btnChooseSecondDay;
    Button btnChooseFirstDay;
    Button btnHomeSearch;
    TextView lblHomeHeader;
    TextView lblChosenDate1;
    TextView lblChosenDate2;
    EditText txtLocationSearch;
    DatePickerDialog.OnDateSetListener dateSetListener1;
    DatePickerDialog.OnDateSetListener dateSetListener2;
    Spinner greaterOrLessThanSpinner;
    Spinner magnitudeSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Search");

        return inflater.inflate(R.layout.search_fragment, null);
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
        txtLocationSearch = view.findViewById(R.id.txtSearchLocation);
        // make second button hidden by default
        lblChosenDate1.setVisibility(View.GONE);
        lblChosenDate2.setVisibility(View.GONE);
        lblSecondDay.setVisibility(View.GONE);
        btnChooseSecondDay.setVisibility(View.GONE);
        dateRangeToggle.setText("Select dates");

        dateRangeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    // show second date button
                    lblSecondDay.setVisibility(View.VISIBLE);
                    btnChooseSecondDay.setVisibility(View.VISIBLE);
                    lblHomeHeader.setText("From:");
                    lblSecondDay.setText("To:");
                    dateRangeToggle.setText("Select day");
                }
                else{
                    // show only first button
                    lblSecondDay.setVisibility(View.GONE);
                    lblChosenDate2.setVisibility(View.GONE);
                    btnChooseSecondDay.setVisibility(View.GONE);
                    lblHomeHeader.setText("On:");
                    dateRangeToggle.setText("Select dates");
                    // should fix bug where user does search and then re searchers after toggling to one day search
                    lblChosenDate2.setText("01/01/1970");
                    lblChosenDate2.setVisibility(View.GONE);
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
                String searchLocation = "";

                boolean useEndDate = false;
                boolean useSeachBox = false;

                if (txtLocationSearch.getText().toString() != null && !txtLocationSearch.getText().toString().equals("")){
                    useSeachBox = true;
                    searchLocation = txtLocationSearch.getText().toString();
                }
                else{
                    useSeachBox = false;
                }

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

                    Bundle options = new Bundle();
                    // options.putSerializable("FirstDate", start);
                    if (useSeachBox){
                        options.putString("SearchLocation", searchLocation);
                    }
                    options.putLong("FirstDate", start.getTime());
                    options.putLong("EndDate", end.getTime());

                    Intent i = new Intent(getActivity(), SearchResultsActivity.class);

                    i.putExtra("SearchOptions", new Gson().toJson(options));
                    Log.e("CLICKED: ", "Putting into bundle");
                    getContext().startActivity(i);
                }
                else{
                    // send first date only to next activity
                    Bundle options = new Bundle();
                    if (useSeachBox){
                        options.putString("SearchLocation", searchLocation);
                    }
                    options.putLong("Date", start.getTime());
                    Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                    intent.putExtra("SearchOptions", new Gson().toJson(options));
                    Log.e("FirstDate: ", start.toString());
                    getContext().startActivity(intent);
                }
            }
        });
    }
}
