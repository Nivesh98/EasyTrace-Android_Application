package com.nivacreation.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import org.jetbrains.annotations.NotNull;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){

        this.context = context;
    }

    public int[] slide_images = {R.drawable.wallet_background,R.drawable.bus_background,R.drawable.pointer_background};
//    public  int[] slide_heading = {R.drawable.bus_heaser,R.drawable.wallet_header,R.drawable.pointer_header};
//    public int[] slide_descript = { R.drawable.bus_description,R.drawable.wallet_description,R.drawable.pointer_description};
//    public int[] slide_background = {R.drawable.background_first,R.drawable.background_second,R.drawable.background_third};


    @Override
    public int getCount() {

        return slide_images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == (RelativeLayout)object;
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);

//        RelativeLayout slideBackground = (RelativeLayout) view.findViewById(R.id.slideBackground);
        ImageView slideImageView = (ImageView) view.findViewById(R.id.slideImage);
//        ImageView slideHeading = (ImageView) view.findViewById(R.id.slideHeading);
//        ImageView slideDescription = (ImageView) view.findViewById(R.id.slideDescription);

//        slideBackground.setBackground(slide_background[position]);
        slideImageView.setImageResource(slide_images[position]);
//        slideHeading.setImageResource(slide_heading[position]);
//        slideDescription.setImageResource(slide_descript[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {

        container.removeView((RelativeLayout)object);
    }
}
