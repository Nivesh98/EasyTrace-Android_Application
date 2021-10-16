package com.nivacreation.login;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class InboxFragment_Driver extends Fragment implements OnMapReadyCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    //firebase
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    String vui;
    StorageReference storageReference;

    final Handler mHandler = new Handler();
    private Thread mUiThread;
    private GoogleMap mMap;

    //map change live location
    private ScheduledExecutorService scheduler;
    private boolean isrun = false;


    private static final int DEFAULT_ZOOM = 11;

    MarkerOptions marker;
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    LatLng centerlocation;
    private Location lastKnownLocation;
    private CameraPosition cameraPosition;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    Vector<MarkerOptions> markerOptions;

    private boolean locationPermissionGranted;
    private FusedLocationProviderClient fusedLocationProviderClient;


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
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapDriver);
        mapFragment.getMapAsync(this);

        //firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        Switch busStatus = (Switch) view.findViewById(R.id.busStatusSwitch);
        Button checkBtn = (Button) view.findViewById(R.id.busStatusBtn);

//        if (busStatus.isChecked() == true){
//            checkBtn.setText("STOP");
//            checkBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(getActivity(),"Now Clickable",Toast.LENGTH_SHORT).show();
//                }
//            });


        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                centerlocation = new LatLng(7.0,80);
                if (savedInstanceState != null) {
                    lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
                    cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
                }
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                Vector<MarkerOptions> markerOptions = new Vector<>();

                markerOptions.add(new MarkerOptions().title("Kirindiwela")
                        .position(new LatLng(7.0427287,80.1300031))
                        .snippet("Open during MCO : 8am - 6pm").icon(BitmapDescriptorFactory.fromResource(R.drawable.pngwing))
                );
                markerOptions.add(new MarkerOptions().title("Radawana")
                        .position(new LatLng(7.0312, 80.1045))
                        .snippet("Open during MCO : 8am - 6pm").icon(BitmapDescriptorFactory.fromResource(R.drawable.pngwing))
                );
                markerOptions.add(new MarkerOptions().title("Henegama")
                        .position(new LatLng(7.02166,80.05446))
                        .snippet("Open during MCO : 8am - 6pm").icon(BitmapDescriptorFactory.fromResource(R.drawable.pngwing))
                );
                markerOptions.add(new MarkerOptions().title("Waliweriya")
                        .position(new LatLng(7.03150,80.02781))
                        .snippet("Open during MCO : 8am - 6pm").icon(BitmapDescriptorFactory.fromResource(R.drawable.pngwing))
                );
                markerOptions.add(new MarkerOptions().title("Nadungamuwa")
                        .position(new LatLng(7.05312,80.01614))
                        .snippet("Open during MCO : 8am - 6pm").icon(BitmapDescriptorFactory.fromResource(R.drawable.pngwing))
                );
                markerOptions.add(new MarkerOptions().title("Mudungoda")
                        .position(new LatLng(7.06615,80.01228))
                        .snippet("Open during MCO : 8am - 6pm").icon(BitmapDescriptorFactory.fromResource(R.drawable.pngwing))
                );
                markerOptions.add(new MarkerOptions().title("Miriswaththa")
                        .position(new LatLng(7.07339,80.01610))
                        .snippet("Open during MCO : 8am - 6pm").icon(BitmapDescriptorFactory.fromResource(R.drawable.pngwing))
                );
                markerOptions.add(new MarkerOptions().title("Gampaha")
                        .position(new LatLng(7.09199,79.99321))
                        .snippet("Open during MCO : 8am - 6pm").icon(BitmapDescriptorFactory.fromResource(R.drawable.pngwing))
                );

                mMap = googleMap;
                // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

                for (MarkerOptions mark : markerOptions){
                    mMap.addMarker(mark);
                }
//                Vector<MarkerOptions> markerOptions = new Vector<>();
//                markerOptions.add(new MarkerOptions().title("Gampaha")
//                        .position(new LatLng(7.09199,79.99321))
//                        .snippet("Open during MCO : 8am - 6pm")
//                );
//                for (MarkerOptions mark : markerOptions){
//                    mMap.addMarker(mark);
//                }

                getLocationPermission();
                updateLocationUI();
                //TaskRunInCycle();
                checkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (busStatus.isChecked() == true){
                            checkBtn.setText("STOP");

                            //checkBtn.isClickable() = false;
                            Toast.makeText(getActivity(),"Bus Started!",Toast.LENGTH_SHORT).show();
                            // checkBtn.isClickable();
                            scheduler = Executors.newSingleThreadScheduledExecutor();
                            scheduler.scheduleAtFixedRate(new Runnable()
                            {
                                public void run()
                                {
                                    if(isrun){
                                        runOnUiThread(new Runnable(){
                                            @Override
                                            public void run(){
                                                getDeviceLocation();
                                                enableMyLocation();
                                                Toast.makeText(getActivity(), "change location",Toast.LENGTH_SHORT).show();
                                                mMap.clear();
                                                for (MarkerOptions mark : markerOptions){
                                                    mMap.addMarker(mark);
                                                }
                                            }
                                        });
                                    }else{
                                        isrun = true;
                                    }

                                }
                            }, 0, 30, TimeUnit.SECONDS);
                        }else if (busStatus.isChecked() == false) {
                            checkBtn.setText("RUN");
                            mMap.clear();
                            for (MarkerOptions mark : markerOptions){
                                mMap.addMarker(mark);
                            }
                            Toast.makeText(getActivity(),"Bus Stopped!",Toast.LENGTH_SHORT).show();
                            // checkBtn.isClickable();
                        }
                    }
                });
                // [END_EXCLUDE]

                // Turn on the My Location layer and the related control on the map.


                // Get the current location of the device and set the position of the map.
                getDeviceLocation();

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerlocation,10));

            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            String perms[] = {"android.permission.ACCESS_FINE_LOCATION"};
            // Permission to access the location is missing. Show rationale and request permission
            ActivityCompat.requestPermissions(getActivity(), perms,200);
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */

        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                LatLng currentlocation = new LatLng(lastKnownLocation.getLatitude(),
                                        lastKnownLocation.getLongitude());
                                mMap.addMarker(new
                                        MarkerOptions().position(currentlocation).title("current location"));
                                Toast.makeText(getActivity(), "latitude:" + lastKnownLocation.getLatitude() + " longitude:" +lastKnownLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                                drawCircle(new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude()),2);
                                Toast.makeText(getActivity(), "circle is here", Toast.LENGTH_SHORT).show();
                                String userID ;
                                userID = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("BusLocations").document(userID);
                                Map<String,Object> user = new HashMap<>();
                                user.put("lat",lastKnownLocation.getLatitude());
                                user.put("log",lastKnownLocation.getLongitude());
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG,"onSuccess: Success Updated LatLong! "+ userID);
                                    }
                                });
                                Toast.makeText(getActivity(), "Success Updated LatLong!", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }



    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    //map change live location
    private void TaskRunInCycle() {

    }
    public final void runOnUiThread(Runnable action) {
        if (Thread.currentThread() != mUiThread) {
            mHandler.post(action);
        } else {
            action.run();
        }
    }
    private void drawCircle(LatLng point, double radius_km){
        CircleOptions circleOptions = new CircleOptions();
        //Set center of circle
        circleOptions.center(point);
        //Set radius of circle
        circleOptions.radius(radius_km * 1000);
        //Set border color of circle
        circleOptions.strokeColor(Color.BLUE);
        //Set border width of circle
        circleOptions.strokeWidth(2);
        circleOptions.fillColor(Color.argb(75,94,165,197));
        //Adding circle to map
        Circle mapCircle = mMap.addCircle(circleOptions);
        //We can remove above circle with code bellow
        //mapCircle.remove();

    }
}