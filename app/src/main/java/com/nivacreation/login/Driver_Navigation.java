package com.nivacreation.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

public class Driver_Navigation extends AppCompatActivity {

    // private ImageView userImage;
    private Uri imageUri;
    Bitmap compressor;
    private ProgressDialog progressDialog;
    ImageView userImage;

    StorageReference storageReference;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    String vui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_navigation);

        progressDialog = new ProgressDialog(this);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userId = fAuth.getCurrentUser().getUid();

        final DrawerLayout drawerLayoutDriver = findViewById(R.id.drawerLayout_driver);

        findViewById(R.id.image_menu_driver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayoutDriver.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView_driver);
        navigationView.setItemIconTintList(null);


        View headerView = navigationView.getHeaderView(0);

        ImageView userImage = (ImageView) headerView.findViewById(R.id.imageProfile_driver);

        StorageReference profileRef = storageReference.child("user profile Driver").child(userId +".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(userImage);
            }
        });

        userImage();

        userDetails();

        NavController navController = Navigation.findNavController(this,R.id.navHostFragment_driver);
        NavigationUI.setupWithNavController(navigationView, navController);

        final TextView textTitleDriver = findViewById(R.id.textTitle_driver);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull @NotNull NavController controller, @NonNull @NotNull NavDestination destination, @Nullable @org.jetbrains.annotations.Nullable Bundle arguments) {
                textTitleDriver.setText(destination.getLabel());
            }
        });
    }

    public void userDetails(){

        FirebaseUser user = fAuth.getCurrentUser();
        if (fAuth.getCurrentUser().getUid() != null){

            userId = fAuth.getCurrentUser().getUid();

            DocumentReference documentReference = fStore.collection("Users").document(userId);
            documentReference.addSnapshotListener( this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                    if (value != null && value.exists()) {

                        NavigationView navigationView = findViewById(R.id.navigationView_driver);
                        navigationView.setItemIconTintList(null);

                        View headerView = navigationView.getHeaderView(0);
                        TextView navUserName = (TextView) headerView.findViewById(R.id.userName_driver);

                        navUserName.setText(value.getString("First Name") + " " + value.getString("Last Name"));

                    }

                }
            });
        }
    }
    private void userImage() {
        NavigationView navigationView = findViewById(R.id.navigationView_driver);
        navigationView.setItemIconTintList(null);

        View headerView = navigationView.getHeaderView(0);

        ImageView userImage = (ImageView) headerView.findViewById(R.id.imageProfile_driver);

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if (ContextCompat.checkSelfPermission(Driver_Navigation.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(Driver_Navigation.this,"Permission Denied", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(Driver_Navigation.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }else {

                        ChoseImage();
                    }
                }else{

                    ChoseImage();
                }
            }
        });
    }

    private void ChoseImage() {
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(Driver_Navigation.this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            progressDialog.setMessage("Storing data...");

            if (resultCode==RESULT_OK){
                imageUri = result.getUri();
                //userImage.setImageURI(imageUri);
                progressDialog.show();
                uploadImageToFirebase(imageUri);
                //progressDialog.dismiss();
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
                progressDialog.dismiss();
            }
            //progressDialog.dismiss();
        }else{
            progressDialog.dismiss();
        }


    }

    private void uploadImageToFirebase(Uri imageUri) {
        NavigationView navigationView = findViewById(R.id.navigationView_driver);
        navigationView.setItemIconTintList(null);

        View headerView = navigationView.getHeaderView(0);

        ImageView userImage = (ImageView) headerView.findViewById(R.id.imageProfile_driver);

        StorageReference fileRef = storageReference.child("user profile Driver").child(userId +".jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(userImage);
                    }
                });
                Toast.makeText(Driver_Navigation.this,"Image Uploaded !",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Driver_Navigation.this,"Failed !",Toast.LENGTH_SHORT).show();
            }
        });
    }

}