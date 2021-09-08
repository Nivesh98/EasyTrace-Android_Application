package com.nivacreation.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Welcome extends AppCompatActivity {

    private ViewPager slideViewPager;
    private LinearLayout dotLayout;

    private Button skipBtn, nextButton, backButton;

    private TextView[] mDots;

    private SliderAdapter sliderAdapter;

    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        skipBtn = findViewById(R.id.skipButton);

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInActivity = new Intent(Welcome.this, SignInActivity.class);
                signInActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signInActivity);
                finish();
            }
        });

        slideViewPager =(ViewPager) findViewById(R.id.viewPage);
        dotLayout =(LinearLayout) findViewById(R.id.sideDots);

        nextButton = (Button) findViewById(R.id.nextBtn);
        backButton = (Button) findViewById(R.id.backBtn);

        sliderAdapter = new SliderAdapter(this);

        slideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);

        slideViewPager.addOnPageChangeListener(viewListener);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nextButton.getText().equals("Finish") && mCurrentPage == 2){

//                    nextButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {

                            Intent signInActivity = new Intent(Welcome.this, SignInActivity.class);
                            signInActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(signInActivity);
                            finish();
//                        }
//                    });
                }else {

                    slideViewPager.setCurrentItem(mCurrentPage + 1);


                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideViewPager.setCurrentItem(mCurrentPage -1);
            }
        });
    }

    public void addDotsIndicator(int position1){
        mDots = new TextView[3];
        dotLayout.removeAllViews();

        for (int i =0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.transparentColorWhite));

            dotLayout.addView(mDots[i]);
        }

        if (mDots.length>0){
            mDots[position1].setTextColor(getResources().getColor(R.color.white));
        }
    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndicator(position);

            mCurrentPage = position;

            if (position == 0){

                nextButton.setEnabled(true);
                backButton.setEnabled(false);
                backButton.setVisibility(View.INVISIBLE);

                nextButton.setText("Next");
                backButton.setText("");

            }else  if (position == mDots.length-1){

                nextButton.setEnabled(true);
                backButton.setEnabled(true);
                backButton.setVisibility(View.VISIBLE);

                nextButton.setText("Finish");
                backButton.setText("Back");

            }else {

                nextButton.setEnabled(true);
                backButton.setEnabled(true);
                backButton.setVisibility(View.VISIBLE);

                nextButton.setText("Next");
                backButton.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}