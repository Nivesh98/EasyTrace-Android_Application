package com.nivacreation.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class HomeFragment_Driver extends Fragment {

    private Button qrBtn, logOut;
    private TextView userFullNameTxt, userEmailTxt, userTypeTxt, sUserName;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    String vui;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment_Driver() {
    }

    public static HomeFragment_Driver newInstance(String param1, String param2) {
        HomeFragment_Driver fragment = new HomeFragment_Driver();
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

        View view = inflater.inflate(R.layout.fragment_home__driver, container, false);

        qrBtn = view.findViewById(R.id.btnQR);
        logOut = view.findViewById(R.id.logout);

        userEmailTxt = view.findViewById(R.id.txtUserEmail);
        userFullNameTxt = view.findViewById(R.id.txtUserFullName);
        userTypeTxt = view.findViewById(R.id.txtUserType);
        sUserName = view.findViewById(R.id.userName);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userDetails();

        qrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),qrScanner.class));
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                Intent signInActivity = new Intent(getActivity(), SignInActivity.class);
                signInActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signInActivity);
            }
        });
        return view;
    }

    public void userDetails(){

        FirebaseUser user = fAuth.getCurrentUser();
        if (fAuth.getCurrentUser().getUid() != null){

            userId = fAuth.getCurrentUser().getUid();

            DocumentReference documentReference = fStore.collection("Users").document(userId);
            documentReference.addSnapshotListener( getActivity(), new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                    if (value != null && value.exists()) {

                        userEmailTxt.setText(value.getString("email"));
                        userFullNameTxt.setText(value.getString("First Name") + " " + value.getString("Last Name"));
                        //userTypeTxt.setText(value.getString("User Type"));
                        vui = value.getString("User Type");
                        userTypeTxt.setText(vui);

//                        String name = value.getString("First Name") + " " + value.getString("Last Name");
//                        Intent intent = new Intent(getActivity(),layout_header.class);
//                        intent.putExtra("key",name);
//                        Toast.makeText(getActivity(),"this is complete"  + name,Toast.LENGTH_LONG).show();
//                        startActivity(intent);

                    }

                }
            });
        }
    }
}