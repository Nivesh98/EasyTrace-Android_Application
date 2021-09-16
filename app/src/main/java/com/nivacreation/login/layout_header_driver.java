package com.nivacreation.login;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class layout_header_driver extends AppCompatActivity {
    private TextView name;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    String vui;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_navigation_header_driver);

        name = findViewById(R.id.userName);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();



//        Intent intent = getIntent();
//        String s = intent.getStringExtra("key");
//        Toast.makeText(this,"this is complete" + s,Toast.LENGTH_SHORT).show();
//        name.setText(s);

    }
}
