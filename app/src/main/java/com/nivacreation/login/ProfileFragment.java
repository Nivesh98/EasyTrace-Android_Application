package com.nivacreation.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private static final int RESULT_OK = -1;
    private Button qrBtn, logOut,findBusBtn;
    private TextView userFullNameTxt, userEmailTxt, userTypeTxt, sUserName,userUserName_Profile;

    private Uri imageUri;
    private Bitmap compressor;
    ImageView userImageP;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    String vui;
    StorageReference storageReference;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProfileFragment() {

    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userEmailTxt = view.findViewById(R.id.passenger_profile_email);
        userFullNameTxt = view.findViewById(R.id.passenger_profile_name);
        //userTypeTxt = view.findViewById(R.id.txtUserType);
        userUserName_Profile = view.findViewById(R.id.passenger_profile_usernameB2);
        sUserName = view.findViewById(R.id.userName);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userImageP = view.findViewById(R.id.imageProfile_profile);
        storageReference = FirebaseStorage.getInstance().getReference();

        ImageView userImageP = view.findViewById(R.id.imageProfile_profile);
        String userId = fAuth.getCurrentUser().getUid();

        SwipeButton swipeButton = view.findViewById(R.id.swipeBtn);

        StorageReference profileRef = storageReference.child("user profile").child(userId +".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(userImageP);
            }
        });

        userDetails();

        swipeButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.navHostFragment, new HomeFragment());
                fragmentTransaction.commit();
//                Intent signInActivity = new Intent(getActivity(), HomeFragment.class);
//                signInActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(signInActivity);
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
                        userUserName_Profile.setText("@"+value.getString("First Name").toLowerCase()+"_"+value.getString("Last Name").toLowerCase());
                        //userTypeTxt.setText(value.getString("User Type"));
                       // vui = value.getString("User Type");
                        //userTypeTxt.setText(vui);

                    }

                }
            });
        }

    }
}