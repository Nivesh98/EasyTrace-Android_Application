package com.nivacreation.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import org.jetbrains.annotations.NotNull;

public class layout_header extends AppCompatActivity{

    private TextView name;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    String vui;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_navigation_header_passenger);

        name = findViewById(R.id.userName);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();



//        Intent intent = getIntent();
//        String s = intent.getStringExtra("key");
//        Toast.makeText(this,"this is complete" + s,Toast.LENGTH_SHORT).show();
//        name.setText(s);

    }

}
