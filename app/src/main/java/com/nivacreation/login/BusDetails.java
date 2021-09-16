package com.nivacreation.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BusDetails extends AppCompatActivity {

    EditText editText,editText1;
    Spinner spinner_start, spinner_end;
    TextView start,end;
    DatePickerDialog.OnDateSetListener listener;
    TimePickerDialog.OnTimeSetListener timeSetListener;

    //..........Start Location...........

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);


        editText = findViewById(R.id.Date);
        editText1= findViewById(R.id.Time);
        spinner_start = findViewById(R.id.spinner_start);
        spinner_end = findViewById(R.id.spinner_end);
        //start = findViewById(R.id.txt_start);
        //end = findViewById(R.id.txt_end);

        ArrayList<String> townList_start = new ArrayList<>();

        townList_start.add("Select Start Location");
        townList_start.add("Matara");
        townList_start.add("Weligama");
        townList_start.add("Galle");
        townList_start.add("Hikkaduw");
        townList_start.add("Ambalangoda");
        townList_start.add("Kaluthara");
        townList_start.add("Panadura");
        townList_start.add("Moratuwa");
        townList_start.add("Colombo");

        spinner_start.setAdapter(new ArrayAdapter<>(BusDetails.this, android.R.layout.simple_spinner_dropdown_item,townList_start));

        spinner_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){
                    Toast.makeText(getApplicationContext(),
                            "Select Start Location",Toast.LENGTH_SHORT).show();
                    //start.setText("");
                }else {
                    String STown = parent.getItemAtPosition(position).toString();
                    //start.setText(STown);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //.................End Location..................

        ArrayList<String> townList_end = new ArrayList<>();

        townList_end.add("Select End Location");
        townList_end.add("Matara");
        townList_end.add("Weligama");
        townList_end.add("Galle");
        townList_end.add("Hikkaduw");
        townList_end.add("Ambalangoda");
        townList_end.add("Kaluthara");
        townList_end.add("Panadura");
        townList_end.add("Moratuwa");
        townList_end.add("Colombo");


        spinner_end.setAdapter(new ArrayAdapter<>(BusDetails.this, android.R.layout.simple_spinner_dropdown_item,townList_end));

        spinner_end.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){
                    Toast.makeText(getApplicationContext(),
                            "Select End Location",Toast.LENGTH_SHORT).show();
                    //end.setText("");
                }else {
                    String ETown = parent.getItemAtPosition(position).toString();
                    //end.setText(ETown);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int min = calendar.get(Calendar.MINUTE);



        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(BusDetails.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        editText.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        editText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(BusDetails.this, android.R.style.Theme_Holo_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        int h = hourOfDay;
                        int m = minute;

                        String time = hourOfDay + ":" + minute;

                        SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                        try {
                            Date date = f24Hours.parse(time);

                            SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
                            editText1.setText(f12Hours.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                }, 12, 0, false
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(hour,min);
                timePickerDialog.show();
            }
        });
    }
}