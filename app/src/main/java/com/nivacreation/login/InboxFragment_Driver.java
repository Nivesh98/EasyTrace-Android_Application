package com.nivacreation.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public class InboxFragment_Driver extends Fragment implements OnMapReadyCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
     GoogleMap mMap;

    public InboxFragment_Driver() {
    }

    public static InboxFragment_Driver newInstance(String param1, String param2) {
        InboxFragment_Driver fragment = new InboxFragment_Driver();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox__driver, container, false);

         //Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        getFragmentManager().beginTransaction().add(R.id.framePasssenger,new InboxFragment_Driver()).commit();
//        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager()
//                .findFragmentById(R.id.mapDriver);
//        mapFragment.getMapAsync(this);
        return view;
    }

    private void getSupportFragmentManager() {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}