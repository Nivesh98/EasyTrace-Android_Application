package com.nivacreation.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class HomeActivity extends AppCompatActivity {

    Button qrBtn, findBusBtn, logOutBtn;
    public static TextView valueTxt;
    TextView userFullNameTxt, userEmailTxt, userTypeTxt;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    String vui;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        qrBtn = findViewById(R.id.btnQR);
        findBusBtn = findViewById(R.id.btnFindBus);
        logOutBtn = findViewById(R.id.logout);

        userEmailTxt = findViewById(R.id.txtUserEmail);
        userFullNameTxt = findViewById(R.id.txtUserFullName);
        userTypeTxt = findViewById(R.id.txtUserType);

        toolbar = findViewById(R.id.toolBar_logout);

        valueTxt = findViewById(R.id.txtValue);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        setSupportActionBar(toolbar);

       userDetails();

       qrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),qrScanner.class));
            }
        });
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                Intent signInActivity = new Intent(HomeActivity.this, SignInActivity.class);
                signInActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signInActivity);
                finish();
            }
        });

    }
    public void userDetails(){

        FirebaseUser user = fAuth.getCurrentUser();
        if (fAuth.getCurrentUser().getUid() != null){
            if (user.isEmailVerified()){

                userId = fAuth.getCurrentUser().getUid();

                DocumentReference documentReference = fStore.collection("Users").document(userId);
                documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        userEmailTxt.setText(value.getString("email"));
                        userFullNameTxt.setText(value.getString("First Name")+" "+value.getString("Last Name"));
                        //userTypeTxt.setText(value.getString("User Type"));
                        vui = value.getString("User Type");
                        userTypeTxt.setText(vui);

                    }
                });
            }

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return  super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,SignInActivity.class));
        }
        return true;
    }
    //    public void logOutOnClick(View view) {
//        fAuth.signOut();
//        startActivity(new Intent(getApplicationContext(),SignInActivity.class));
//        finish();
//    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
}