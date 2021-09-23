package com.nivacreation.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutBus extends AppCompatActivity {
    Button busA, busB, busC, busD, busE, busF, busG, busH, busI, busJ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_bus);


        busA = findViewById(R.id.bus_A);
        busB = findViewById(R.id.bus_B);
        busC = findViewById(R.id.bus_C);
        busD = findViewById(R.id.bus_D);
        busE = findViewById(R.id.bus_E);
        busF = findViewById(R.id.bus_F);
        busG = findViewById(R.id.bus_G);
        busH = findViewById(R.id.bus_H);
        busI = findViewById(R.id.bus_I);
        busJ = findViewById(R.id.bus_J);

        busA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(AboutBus.this,BusInsideDetails.class);
                startActivity(i);
            }
        });

        busB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(AboutBus.this,BusInsideDetails.class);
                startActivity(i);
            }
        });

        busC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(AboutBus.this,BusInsideDetails.class);
                startActivity(i);
            }
        });

        busD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(AboutBus.this,BusInsideDetails.class);
                startActivity(i);
            }
        });

        busE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(AboutBus.this,BusInsideDetails.class);
                startActivity(i);
            }
        });

        busF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(AboutBus.this,BusInsideDetails.class);
                startActivity(i);
            }
        });

        busG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(AboutBus.this,BusInsideDetails.class);
                startActivity(i);
            }
        });

        busH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(AboutBus.this,BusInsideDetails.class);
                startActivity(i);
            }
        });

        busI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(AboutBus.this,BusInsideDetails.class);
                startActivity(i);
            }
        });

        busJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(AboutBus.this,BusInsideDetails.class);
                startActivity(i);
            }
        });
    }
}