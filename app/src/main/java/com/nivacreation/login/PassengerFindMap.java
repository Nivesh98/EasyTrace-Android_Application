package com.nivacreation.login;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.nivacreation.login.databinding.ActivityPassengerFindMapBinding;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PassengerFindMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityPassengerFindMapBinding binding;

    private ScheduledExecutorService scheduler;
    private boolean isrun = false;
    final Handler mHandler = new Handler();
    private Thread mUiThread;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    Double lat;
    Double log;

    Spinner spinner_start, spinner_end;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPassengerFindMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //selectLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        busLocation();
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void selectLocation() {
        //        editText = findViewById(R.id.Date);
//        editText1= findViewById(R.id.Time);
        spinner_start = findViewById(R.id.spinner_start);
        spinner_end = findViewById(R.id.spinner_end);
        //start = findViewById(R.id.txt_start);
        //end = findViewById(R.id.txt_end);
        submit = findViewById(R.id.btnSubmit);

        ArrayList<String> townList_start = new ArrayList<>();

        townList_start.add("Select Start Location");
        townList_start.add("Kirindiwela");
        townList_start.add("Radawana");
        townList_start.add("Henegama");
        townList_start.add("Waliweriya");
        townList_start.add("Nadungamuwa");
        townList_start.add("Miriswaththa");
        townList_start.add("Mudungoda");
        townList_start.add("Gampaha");


        spinner_start.setAdapter(new ArrayAdapter<>(PassengerFindMap.this, android.R.layout.simple_spinner_dropdown_item,townList_start));

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
        townList_end.add("Kirindiwela");
        townList_end.add("Radawana");
        townList_end.add("Henegama");
        townList_end.add("Waliweriya");
        townList_end.add("Nadungamuwa");
        townList_end.add("Miriswaththa");
        townList_end.add("Mudungoda");
        townList_end.add("Gampaha");


        spinner_end.setAdapter(new ArrayAdapter<>(PassengerFindMap.this, android.R.layout.simple_spinner_dropdown_item,townList_end));

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



//        Calendar calendar = Calendar.getInstance();
//        final int year = calendar.get(Calendar.YEAR);
//        final int month = calendar.get(Calendar.MONTH);
//        final int day = calendar.get(Calendar.DAY_OF_MONTH);
//        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        final int min = calendar.get(Calendar.MINUTE);



//        editText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(BusDetails.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//                        month = month + 1;
//                        String date = dayOfMonth + "/" + month + "/" + year;
//                        editText.setText(date);
//                    }
//                }, year, month, day);
//                datePickerDialog.show();
//            }
//        });
//
//        editText1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                TimePickerDialog timePickerDialog = new TimePickerDialog(BusDetails.this, android.R.style.Theme_Holo_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//
//                        int h = hourOfDay;
//                        int m = minute;
//
//                        String time = hourOfDay + ":" + minute;
//
//                        SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
//                        try {
//                            Date date = f24Hours.parse(time);
//
//                            SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
//                            editText1.setText(f12Hours.format(date));
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }, 12, 0, false
//                );
//                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                timePickerDialog.updateTime(hour,min);
//                timePickerDialog.show();
//            }
//        });

//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent i = new Intent(PassengerFindMap.this,AboutBus.class);
//                startActivity(i);
//            }
//        });
    }

    private void busLocation() {

        selectLocation();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDriverLocation();
                String stLo = spinner_start.getSelectedItem().toString();
                String enLo = spinner_end.getSelectedItem().toString();
                if(!stLo.isEmpty()){
                    if(!enLo.isEmpty()){
                        mMap.clear();
                        if((stLo.equals("Kirindiwela") && enLo.equals("Radawana"))||(stLo.equals("Radawana") && enLo.equals("Kirindiwela"))){
                            double kiLat = 7.0427287;
                            double kiLong = 80.1300031;
                            double raLat = 7.0312;
                            double raLong = 80.1045;

                            double comLat = (7.0427287 + 7.0312)/2;
                            double comLong = (80.1300031 + 80.1045)/2;

                            LatLng kirindiwela = new LatLng(7.0427287,80.1300031);
                            mMap.addMarker(new MarkerOptions().position(kirindiwela).title("Kirindiwela"));

                            LatLng radawana = new LatLng(7.0312,80.1045);
                            mMap.addMarker(new MarkerOptions().position(radawana).title("Radawana"));

                            LatLng sydney = new LatLng(comLat,comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,13));

                        }else if((stLo.equals("Kirindiwela") && enLo.equals("Henegama"))||(stLo.equals("Henegama") && enLo.equals("Kirindiwela"))){
                            double kiLat = 7.0427287;
                            double kiLong = 80.1300031;
                            double heLat = 7.02166;
                            double heLong = 80.05446;

                            double comLat = (kiLat + heLat)/2;
                            double comLong = (kiLong + heLong)/2;

                            LatLng kirindiwela = new LatLng(7.0427287,80.1300031);
                            mMap.addMarker(new MarkerOptions().position(kirindiwela).title("Kirindiwela"));

                            LatLng radawana = new LatLng(heLat,heLong);
                            mMap.addMarker(new MarkerOptions().position(radawana).title("Henegama"));

                            LatLng sydney = new LatLng(comLat,comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,12));

                        }else if ((stLo.equals("Kirindiwela") && enLo.equals("Waliweriya"))||(stLo.equals("Waliweriya") && enLo.equals("Kirindiwela"))){

                            double kiLat = 7.0427287;
                            double kiLong = 80.1300031;
                            double weLat = 7.03150;
                            double weLong = 80.02781;

                            double comLat = (kiLat + weLat)/2;
                            double comLong = (kiLong + weLong)/2;

                            LatLng kirindiwela = new LatLng(7.0427287,80.1300031);
                            mMap.addMarker(new MarkerOptions().position(kirindiwela).title("Kirindiwela"));

                            LatLng weliweriya = new LatLng(weLat,weLong);
                            mMap.addMarker(new MarkerOptions().position(weliweriya).title("Waliweriya"));

                            LatLng sydney = new LatLng(comLat,comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,12));

                        }else if ((stLo.equals("Kirindiwela") && enLo.equals("Nadungamuwa"))||(stLo.equals("Nadungamuwa") && enLo.equals("Kirindiwela"))){
                            double kiLat = 7.0427287;
                            double kiLong = 80.1300031;
                            double naLat = 7.05312;
                            double naLong = 80.01614;

                            double comLat = (kiLat + naLat)/2;
                            double comLong = (kiLong + naLong)/2;

                            LatLng kirindiwela = new LatLng(7.0427287,80.1300031);
                            mMap.addMarker(new MarkerOptions().position(kirindiwela).title("Kirindiwela"));

                            LatLng nadungamuwa = new LatLng(naLat,naLong);
                            mMap.addMarker(new MarkerOptions().position(nadungamuwa).title("Nadungamuwa"));

                            LatLng sydney = new LatLng(comLat,comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,11));

                        }else if ((stLo.equals("Kirindiwela") && enLo.equals("Mudungoda"))||(stLo.equals("Mudungoda") && enLo.equals("Kirindiwela"))){
                            double kiLat = 7.0427287;
                            double kiLong = 80.1300031;
                            double muLat = 7.05312;
                            double muLong = 80.01614;

                            double comLat = (kiLat + muLat)/2;
                            double comLong = (kiLong + muLong)/2;

                            LatLng kirindiwela = new LatLng(7.0427287,80.1300031);
                            mMap.addMarker(new MarkerOptions().position(kirindiwela).title("Kirindiwela"));

                            LatLng mudungoda = new LatLng(muLat,muLong);
                            mMap.addMarker(new MarkerOptions().position(mudungoda).title("Mudungoda"));

                            LatLng sydney = new LatLng(comLat,comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,11));

                        }else if ((stLo.equals("Kirindiwela") && enLo.equals("Miriswaththa"))||(stLo.equals("Miriswaththa") && enLo.equals("Kirindiwela"))) {
                            double kiLat = 7.0427287;
                            double kiLong = 80.1300031;
                            double miLat = 7.07339;
                            double miLong = 80.01610;

                            double comLat = (kiLat + miLat) / 2;
                            double comLong = (kiLong + miLong) / 2;

                            LatLng kirindiwela = new LatLng(7.0427287, 80.1300031);
                            mMap.addMarker(new MarkerOptions().position(kirindiwela).title("Kirindiwela"));

                            LatLng miriswaththa = new LatLng(miLat, miLong);
                            mMap.addMarker(new MarkerOptions().position(miriswaththa).title("Miriswaththa"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 11));

                        }else if ((stLo.equals("Kirindiwela") && enLo.equals("Gampaha"))||(stLo.equals("Gampaha") && enLo.equals("Kirindiwela"))) {
                            double kiLat = 7.0427287;
                            double kiLong = 80.1300031;
                            double gaLat = 7.09199;
                            double gaLong = 79.99321;

                            double comLat = (kiLat + gaLat) / 2;
                            double comLong = (kiLong + gaLong) / 2;

                            LatLng kirindiwela = new LatLng(7.0427287, 80.1300031);
                            mMap.addMarker(new MarkerOptions().position(kirindiwela).title("Kirindiwela"));

                            LatLng gampaha = new LatLng(gaLat, gaLong);
                            mMap.addMarker(new MarkerOptions().position(gampaha).title("Gampaha"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 11));

                        }else if ((stLo.equals("Radawana") && enLo.equals("Henegama"))||(stLo.equals("Henegama") && enLo.equals("Radawana"))) {
                            double raLat = 7.0312;
                            double raLong = 80.1045;
                            double heLat = 7.02166;
                            double heLong = 80.05446;

                            double comLat = (raLat + heLat) / 2;
                            double comLong = (raLong + heLong) / 2;

                            LatLng radawana = new LatLng(raLat, raLong);
                            mMap.addMarker(new MarkerOptions().position(radawana).title("Radawana"));

                            LatLng henegama = new LatLng(heLat, heLong);
                            mMap.addMarker(new MarkerOptions().position(henegama).title("Henegama"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

                        }else if ((stLo.equals("Radawana") && enLo.equals("Waliweriya"))||(stLo.equals("Waliweriya") && enLo.equals("Radawana"))) {
                            double raLat = 7.0312;
                            double raLong = 80.1045;
                            double waLat = 7.03150;
                            double waLong = 80.02781;

                            double comLat = (raLat + waLat) / 2;
                            double comLong = (raLong + waLong) / 2;

                            LatLng radawana = new LatLng(raLat, raLong);
                            mMap.addMarker(new MarkerOptions().position(radawana).title("Radawana"));

                            LatLng Waliweriya = new LatLng(waLat, waLong);
                            mMap.addMarker(new MarkerOptions().position(Waliweriya).title("Waliweriya"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));

                        }else if ((stLo.equals("Radawana") && enLo.equals("Nadungamuwa"))||(stLo.equals("Nadungamuwa") && enLo.equals("Radawana"))) {
                            double raLat = 7.0312;
                            double raLong = 80.1045;
                            double naLat = 7.05312;
                            double naLong = 80.01614;

                            double comLat = (raLat + naLat) / 2;
                            double comLong = (raLong + naLong) / 2;

                            LatLng radawana = new LatLng(raLat, raLong);
                            mMap.addMarker(new MarkerOptions().position(radawana).title("Radawana"));

                            LatLng Nadungamuwa = new LatLng(naLat, naLong);
                            mMap.addMarker(new MarkerOptions().position(Nadungamuwa).title("Nadungamuwa"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));

                        }else if ((stLo.equals("Radawana") && enLo.equals("Mudungoda"))||(stLo.equals("Mudungoda") && enLo.equals("Radawana"))) {
                            double raLat = 7.0312;
                            double raLong = 80.1045;
                            double muLat = 7.06615;
                            double muLong = 80.01228;

                            double comLat = (raLat + muLat) / 2;
                            double comLong = (raLong + muLong) / 2;

                            LatLng radawana = new LatLng(raLat, raLong);
                            mMap.addMarker(new MarkerOptions().position(radawana).title("Radawana"));

                            LatLng Mudungoda = new LatLng(muLat, muLong);
                            mMap.addMarker(new MarkerOptions().position(Mudungoda).title("Mudungoda"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));

                        }else if ((stLo.equals("Radawana") && enLo.equals("Miriswaththa"))||(stLo.equals("Miriswaththa") && enLo.equals("Radawana"))) {
                            double raLat = 7.0312;
                            double raLong = 80.1045;
                            double miLat = 7.07339;
                            double miLong = 80.01610;

                            double comLat = (raLat + miLat) / 2;
                            double comLong = (raLong + miLong) / 2;

                            LatLng radawana = new LatLng(raLat, raLong);
                            mMap.addMarker(new MarkerOptions().position(radawana).title("Radawana"));

                            LatLng Miriswaththa = new LatLng(miLat, miLong);
                            mMap.addMarker(new MarkerOptions().position(Miriswaththa).title("Miriswaththa"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 11));

                        }else if ((stLo.equals("Radawana") && enLo.equals("Gampaha"))||(stLo.equals("Gampaha") && enLo.equals("Radawana"))) {
                            double raLat = 7.0312;
                            double raLong = 80.1045;
                            double gaLat = 7.09199;
                            double gaLong = 79.99321;

                            double comLat = (raLat + gaLat) / 2;
                            double comLong = (raLong + gaLong) / 2;

                            LatLng radawana = new LatLng(raLat, raLong);
                            mMap.addMarker(new MarkerOptions().position(radawana).title("Radawana"));

                            LatLng Gampaha = new LatLng(gaLat, gaLong);
                            mMap.addMarker(new MarkerOptions().position(Gampaha).title("Gampaha"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 11));

                        }else if ((stLo.equals("Henegama") && enLo.equals("Waliweriya"))||(stLo.equals("Waliweriya") && enLo.equals("Henegama"))) {
                            double heLat = 7.02166;
                            double heLong = 80.05446;
                            double waLat = 7.03150;
                            double waLong = 80.02781;

                            double comLat = (heLat + waLat) / 2;
                            double comLong = (heLong + waLong) / 2;

                            LatLng Henegama = new LatLng(heLat, heLong);
                            mMap.addMarker(new MarkerOptions().position(Henegama).title("Henegama"));

                            LatLng Waliweriya = new LatLng(waLat, waLong);
                            mMap.addMarker(new MarkerOptions().position(Waliweriya).title("Waliweriya"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

                        }else if ((stLo.equals("Henegama") && enLo.equals("Nadungamuwa"))||(stLo.equals("Nadungamuwa") && enLo.equals("Henegama"))) {
                            double heLat = 7.02166;
                            double heLong = 80.05446;
                            double naLat = 7.05312;
                            double naLong = 80.01614;

                            double comLat = (heLat + naLat) / 2;
                            double comLong = (heLong + naLong) / 2;

                            LatLng Henegama = new LatLng(heLat, heLong);
                            mMap.addMarker(new MarkerOptions().position(Henegama).title("Henegama"));

                            LatLng Nadungamuwa = new LatLng(naLat, naLong);
                            mMap.addMarker(new MarkerOptions().position(Nadungamuwa).title("Nadungamuwa"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

                        }else if ((stLo.equals("Henegama") && enLo.equals("Mudungoda"))||(stLo.equals("Mudungoda") && enLo.equals("Henegama"))) {
                            double heLat = 7.02166;
                            double heLong = 80.05446;
                            double muLat = 7.06615;
                            double muLong = 80.01228;

                            double comLat = (heLat + muLat) / 2;
                            double comLong = (heLong + muLong) / 2;

                            LatLng Henegama = new LatLng(heLat, heLong);
                            mMap.addMarker(new MarkerOptions().position(Henegama).title("Henegama"));

                            LatLng Mudungoda = new LatLng(muLat, muLong);
                            mMap.addMarker(new MarkerOptions().position(Mudungoda).title("Mudungoda"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

                        }else if ((stLo.equals("Henegama") && enLo.equals("Miriswaththa"))||(stLo.equals("Miriswaththa") && enLo.equals("Henegama"))) {
                            double heLat = 7.02166;
                            double heLong = 80.05446;
                            double miLat = 7.07339;
                            double miLong = 80.01610;

                            double comLat = (heLat + miLat) / 2;
                            double comLong = (heLong + miLong) / 2;

                            LatLng Henegama = new LatLng(heLat, heLong);
                            mMap.addMarker(new MarkerOptions().position(Henegama).title("Henegama"));

                            LatLng Miriswaththa = new LatLng(miLat, miLong);
                            mMap.addMarker(new MarkerOptions().position(Miriswaththa).title("Miriswaththa"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

                        }else if ((stLo.equals("Henegama") && enLo.equals("Gampaha"))||(stLo.equals("Gampaha") && enLo.equals("Henegama"))) {
                            double heLat = 7.02166;
                            double heLong = 80.05446;
                            double gaLat = 7.09199;
                            double gaLong = 79.99321;

                            double comLat = (heLat + gaLat) / 2;
                            double comLong = (heLong + gaLong) / 2;

                            LatLng Henegama = new LatLng(heLat, heLong);
                            mMap.addMarker(new MarkerOptions().position(Henegama).title("Henegama"));

                            LatLng Gampaha = new LatLng(gaLat, gaLong);
                            mMap.addMarker(new MarkerOptions().position(Gampaha).title("Gampaha"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));

                        }else if ((stLo.equals("Waliweriya") && enLo.equals("Nadungamuwa"))||(stLo.equals("Nadungamuwa") && enLo.equals("Waliweriya"))) {
                            double waLat = 7.03150;
                            double waLong = 80.02781;
                            double naLat = 7.05312;
                            double naLong = 80.01614;

                            double comLat = (waLat + naLat) / 2;
                            double comLong = (waLong + naLong) / 2;

                            LatLng Waliweriya = new LatLng(waLat, waLong);
                            mMap.addMarker(new MarkerOptions().position(Waliweriya).title("Waliweriya"));

                            LatLng Nadungamuwa = new LatLng(naLat, naLong);
                            mMap.addMarker(new MarkerOptions().position(Nadungamuwa).title("Nadungamuwa"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

                        }else if ((stLo.equals("Waliweriya") && enLo.equals("Mudungoda"))||(stLo.equals("Mudungoda") && enLo.equals("Waliweriya"))) {
                            double waLat = 7.03150;
                            double waLong = 80.02781;
                            double muLat = 7.06615;
                            double muLong = 80.01228;

                            double comLat = (waLat + muLat) / 2;
                            double comLong = (waLong + muLong) / 2;

                            LatLng Waliweriya = new LatLng(waLat, waLong);
                            mMap.addMarker(new MarkerOptions().position(Waliweriya).title("Waliweriya"));

                            LatLng Mudungoda = new LatLng(muLat, muLong);
                            mMap.addMarker(new MarkerOptions().position(Mudungoda).title("Mudungoda"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

                        }else if ((stLo.equals("Waliweriya") && enLo.equals("Miriswaththa"))||(stLo.equals("Miriswaththa") && enLo.equals("Waliweriya"))) {
                            double waLat = 7.03150;
                            double waLong = 80.02781;
                            double miLat = 7.07339;
                            double miLong = 80.01610;

                            double comLat = (waLat + miLat) / 2;
                            double comLong = (waLong + miLong) / 2;

                            LatLng Waliweriya = new LatLng(waLat, waLong);
                            mMap.addMarker(new MarkerOptions().position(Waliweriya).title("Waliweriya"));

                            LatLng Miriswaththa = new LatLng(miLat, miLong);
                            mMap.addMarker(new MarkerOptions().position(Miriswaththa).title("Miriswaththa"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));

                        }else if ((stLo.equals("Waliweriya") && enLo.equals("Gampaha"))||(stLo.equals("Gampaha") && enLo.equals("Waliweriya"))) {
                            double waLat = 7.03150;
                            double waLong = 80.02781;
                            double gaLat = 7.09199;
                            double gaLong = 79.99321;

                            double comLat = (waLat + gaLat) / 2;
                            double comLong = (waLong + gaLong) / 2;

                            LatLng Waliweriya = new LatLng(waLat, waLong);
                            mMap.addMarker(new MarkerOptions().position(Waliweriya).title("Waliweriya"));

                            LatLng Gampaha = new LatLng(gaLat, gaLong);
                            mMap.addMarker(new MarkerOptions().position(Gampaha).title("Gampaha"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

                        }else if ((stLo.equals("Nadungamuwa") && enLo.equals("Mudungoda"))||(stLo.equals("Mudungoda") && enLo.equals("Nadungamuwa"))) {
                            double naLat = 7.05312;
                            double naLong = 80.01614;
                            double muLat = 7.06615;
                            double muLong = 80.01228;

                            double comLat = (naLat + muLat) / 2;
                            double comLong = (naLong + muLong) / 2;

                            LatLng Nadungamuwa = new LatLng(naLat, naLong);
                            mMap.addMarker(new MarkerOptions().position(Nadungamuwa).title("Nadungamuwa"));

                            LatLng Mudungoda = new LatLng(muLat, muLong);
                            mMap.addMarker(new MarkerOptions().position(Mudungoda).title("Mudungoda"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));

                        }else if ((stLo.equals("Nadungamuwa") && enLo.equals("Miriswaththa"))||(stLo.equals("Miriswaththa") && enLo.equals("Nadungamuwa"))) {
                            double naLat = 7.05312;
                            double naLong = 80.01614;
                            double miLat = 7.07339;
                            double miLong = 80.01610;

                            double comLat = (naLat + miLat) / 2;
                            double comLong = (naLong + miLong) / 2;

                            LatLng Nadungamuwa = new LatLng(naLat, naLong);
                            mMap.addMarker(new MarkerOptions().position(Nadungamuwa).title("Nadungamuwa"));

                            LatLng Miriswaththa = new LatLng(miLat, miLong);
                            mMap.addMarker(new MarkerOptions().position(Miriswaththa).title("Miriswaththa"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));

                        }else if ((stLo.equals("Nadungamuwa") && enLo.equals("Gampaha"))||(stLo.equals("Gampaha") && enLo.equals("Nadungamuwa"))) {
                            double naLat = 7.05312;
                            double naLong = 80.01614;
                            double gaLat = 7.09199;
                            double gaLong = 79.99321;

                            double comLat = (naLat + gaLat) / 2;
                            double comLong = (naLong + gaLong) / 2;

                            LatLng Nadungamuwa = new LatLng(naLat, naLong);
                            mMap.addMarker(new MarkerOptions().position(Nadungamuwa).title("Nadungamuwa"));

                            LatLng Gampaha = new LatLng(gaLat, gaLong);
                            mMap.addMarker(new MarkerOptions().position(Gampaha).title("Gampaha"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));

                        }else if ((stLo.equals("Mudungoda") && enLo.equals("Miriswaththa"))||(stLo.equals("Miriswaththa") && enLo.equals("Mudungoda"))) {
                            double muLat = 7.06615;
                            double muLong = 80.01228;
                            double miLat = 7.07339;
                            double miLong = 80.01610;

                            double comLat = (muLat + miLat) / 2;
                            double comLong = (muLong + miLong) / 2;

                            LatLng Mudungoda = new LatLng(muLat, muLong);
                            mMap.addMarker(new MarkerOptions().position(Mudungoda).title("Mudungoda"));

                            LatLng Miriswaththa = new LatLng(miLat, miLong);
                            mMap.addMarker(new MarkerOptions().position(Miriswaththa).title("Miriswaththa"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));

                        }else if ((stLo.equals("Mudungoda") && enLo.equals("Gampaha"))||(stLo.equals("Gampaha") && enLo.equals("Mudungoda"))) {
                            double muLat = 7.06615;
                            double muLong = 80.01228;
                            double gaLat = 7.09199;
                            double gaLong = 79.99321;

                            double comLat = (muLat + gaLat) / 2;
                            double comLong = (muLong + gaLong) / 2;

                            LatLng Mudungoda = new LatLng(muLat, muLong);
                            mMap.addMarker(new MarkerOptions().position(Mudungoda).title("Mudungoda"));

                            LatLng Gampaha = new LatLng(gaLat, gaLong);
                            mMap.addMarker(new MarkerOptions().position(Gampaha).title("Gampaha"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));

                        }else if ((stLo.equals("Miriswaththa") && enLo.equals("Gampaha"))||(stLo.equals("Gampaha") && enLo.equals("Miriswaththa"))) {
                            double miLat = 7.07339;
                            double miLong = 80.01610;
                            double gaLat = 7.09199;
                            double gaLong = 79.99321;

                            double comLat = (miLat + gaLat) / 2;
                            double comLong = (miLong + gaLong) / 2;

                            LatLng Miriswaththa = new LatLng(miLat, miLong);
                            mMap.addMarker(new MarkerOptions().position(Miriswaththa).title("Miriswaththa"));

                            LatLng Gampaha = new LatLng(gaLat, gaLong);
                            mMap.addMarker(new MarkerOptions().position(Gampaha).title("Gampaha"));

                            LatLng sydney = new LatLng(comLat, comLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));

                        }

                    }else{
                        Toast.makeText(PassengerFindMap.this,"Please Enter Valid Location! ",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(PassengerFindMap.this,"Please Enter Valid Location! ",Toast.LENGTH_SHORT).show();
                    //startLocation.setError("Please Select Start Location");
                }
            }

            private void getDriverLocation() {
                FirebaseUser user = fAuth.getCurrentUser();


                if (fAuth.getCurrentUser().getUid() != null){

                    userId = fAuth.getCurrentUser().getUid();


                    DocumentReference documentReference = fStore.collection("BusLocations").document("87HATPpL1MQ0hhunLRzQkzXQoDt2");
                    documentReference.addSnapshotListener( PassengerFindMap.this, new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                            if (value != null && value.exists()) {
                                lat = value.getDouble("lat");
                                log = value.getDouble("log");

                                double dLog = Double.valueOf(log);
                                double dLat = Double.valueOf(lat);

                                scheduler = Executors.newSingleThreadScheduledExecutor();
                                scheduler.scheduleAtFixedRate(new Runnable()
                                {
                                    public void run()
                                    {
                                        if(isrun){
                                            runOnUiThread(new Runnable(){
                                                @Override
                                                public void run(){
                                                    LatLng Gampaha = new LatLng(dLat, dLog);
                                                    mMap.addMarker(new MarkerOptions().position(Gampaha).title("Bus Location"));
                                                    Toast.makeText(PassengerFindMap.this,"Changed Bus Location!",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else{
                                            isrun = true;
                                        }

                                    }
                                }, 0, 30, TimeUnit.SECONDS);

                            }

                        }
                    });
                }
            }
        });
    }



}