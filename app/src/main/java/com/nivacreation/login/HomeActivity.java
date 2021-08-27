package com.nivacreation.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class HomeActivity extends AppCompatActivity {

    Button qrBtn, findBusBtn;
    public static TextView valueTxt;
    TextView userFullNameTxt, userEmailTxt, userTypeTxt;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    String vui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        qrBtn = findViewById(R.id.btnQR);
        findBusBtn = findViewById(R.id.btnFindBus);

        userEmailTxt = findViewById(R.id.txtUserEmail);
        userFullNameTxt = findViewById(R.id.txtUserFullName);
        userTypeTxt = findViewById(R.id.txtUserType);

        valueTxt = findViewById(R.id.txtValue);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

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

        qrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),qrScanner.class));
            }
        });
    }
}