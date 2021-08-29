package com.nivacreation.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class splashScreen extends AppCompatActivity {

    private static  int SPLASH_SCREEN = 4000;
    Animation topAnim, bottomAnim;
    ImageView logoImg, textImg;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_splash_screen);

        topAnim = AnimationUtils.loadAnimation(this,R.animator.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.animator.bottom_animation);

        logoImg = findViewById(R.id.imageLogo);
        textImg = findViewById(R.id.imageText);

        logoImg.setAnimation(topAnim);
        textImg.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent goSplash = new Intent(splashScreen.this,SignInActivity.class);
                startActivity(goSplash);
                finish();
            }
        },SPLASH_SCREEN);
    }
}