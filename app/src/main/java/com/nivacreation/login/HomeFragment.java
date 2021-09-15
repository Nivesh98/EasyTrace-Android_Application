package com.nivacreation.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.concurrent.Executor;

public class HomeFragment extends Fragment {

    private static final int RESULT_OK = -1;
    private Button qrBtn, logOut;
    private TextView userFullNameTxt, userEmailTxt, userTypeTxt, sUserName;

    private Uri imageUri;
    private Bitmap compressor;
    ImageView userImageP;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    String vui;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {

    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        qrBtn = view.findViewById(R.id.btnQR);
        logOut = view.findViewById(R.id.logout);

        userEmailTxt = view.findViewById(R.id.txtUserEmail);
        userFullNameTxt = view.findViewById(R.id.txtUserFullName);
        userTypeTxt = view.findViewById(R.id.txtUserType);
        sUserName = view.findViewById(R.id.userName);

        userImageP = view.findViewById(R.id.imageProfile_home);

       // ImageView userImage = (ImageView) view.findViewById(R.id.imageProfile);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userImageMethod();
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

                    }

                }
            });
        }

    }
    public void userImageMethod(){

        userImageP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(getActivity(),"Permission Denied", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }else {
                        Toast.makeText(getActivity(),"Chose Image",Toast.LENGTH_SHORT).show();
                        ChoseImage();
                    }
                }else{
                    Toast.makeText(getActivity(),"Chose Image",Toast.LENGTH_SHORT).show();
                    ChoseImage();
                }
            }
        });
    }

    private void ChoseImage() {
        Toast.makeText(getActivity(),"Chose Image",Toast.LENGTH_SHORT).show();
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(getActivity());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode== RESULT_OK){
                imageUri = result.getUri();
                userImageP.setImageURI(imageUri);
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
    }

}