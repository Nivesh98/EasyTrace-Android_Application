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

    public int[] slide_images = {R.drawable.bus_png,R.drawable.wallet,R.drawable.pointer};
    public  String[] slide_heading = {"Slide1","Slide2","Slide3"};
    public String[] slide_descript = { "dkfjdfjkj,kdjfkdjfkdfjldjfldjfldfjdkfjkd dfjdkj fjkdj djfkd kjfd ffdkjflsjdk","dkfjdfjkj,kdjfkdjfkdfjldjfldjfldfjdkfjkd dfjdkj fjkdj djfkd kjfd ffdkjflsjdk","dkfjdfjkj,kdjfkdjfkdfjldjfldjfldfjdkfjkd dfjdkj fjkdj djfkd kjfd ffdkjflsjdk"};

    @Override
    public int getCount() {

        return slide_heading.length;
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

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slideImage);
        TextView slideHeading = (TextView) view.findViewById(R.id.slideHeading);
        TextView slideDescription = (TextView) view.findViewById(R.id.slideDescription);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_heading[position]);
        slideDescription.setText(slide_descript[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {

        container.removeView((RelativeLayout)object);
    }
}
