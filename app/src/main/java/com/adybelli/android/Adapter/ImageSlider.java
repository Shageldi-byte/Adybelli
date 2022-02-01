package com.adybelli.android.Adapter;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.adybelli.android.Common.Constant;
import com.adybelli.android.Common.Util;
import com.adybelli.android.Object.CarouselListObject;
import com.adybelli.android.Object.Slider;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class ImageSlider extends PagerAdapter {

    // Context object
    Context context;

    // Array of images
    ArrayList<Slider> carouselListObjects=new ArrayList<>();
    private ArrayList<String> images=new ArrayList<>();

    // Layout Inflater
    LayoutInflater mLayoutInflater;

    private int custom_position=0;
    ArrayList<String> largeImages=new ArrayList<>();


    // Viewpager Constructor
    public ImageSlider(Context context, ArrayList<Slider> carouselListObjects, ArrayList<String> images, ArrayList<String> largeImages) {
        this.context = context;
        this.carouselListObjects = carouselListObjects;
        this.images=images;
        this.largeImages=largeImages;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // return the number of images
        return carouselListObjects.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml

        View itemView = mLayoutInflater.inflate(R.layout.slider_image, container, false);
// we are adding fade animation





        // create Object
        Slider object=carouselListObjects.get(position);

        // referencing the image view from the item.xml file
        ImageView imageView = itemView.findViewById(R.id.imageViewMain);

        // setting the image in the imageView
        Glide.with(context)
                .load(Constant.SERVER_URL+object.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ArrayList<String> imgs=new ArrayList<>();
//                ArrayList<String> lrgimgs=new ArrayList<>();
//                imgs.add(object.getImageUrl());
//                for(String img:images){
//                    if(!img.equals(imgs.get(0))){
//                        imgs.add(img);
//                    }
//                }
                Util.showImageViewer(context,images,largeImages);
            }
        });

        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
