package com.adybelli.android.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Activity.ProductPage;
import com.adybelli.android.Common.Constant;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Fragment.Products;
import com.adybelli.android.Object.CarouselListObject;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CarouselAdapter extends PagerAdapter {
    // Context object
    private Context context;
    // Array of images
    private ArrayList<CarouselListObject> carouselListObjects = new ArrayList<>();
    // Layout Inflater
    private LayoutInflater mLayoutInflater;
    private int custom_position = 0;
    private FragmentManager fragmentManager;
    private Activity activity;
    private ViewPager viewPager;

    // Viewpager Constructor
    public CarouselAdapter(Context context, ArrayList<CarouselListObject> carouselListObjects, FragmentManager fragmentManager, Activity activity, ViewPager viewPager) {
        this.context = context;
        this.carouselListObjects = carouselListObjects;
        this.fragmentManager = fragmentManager;
        this.activity = activity;
        this.viewPager = viewPager;
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
        View itemView = mLayoutInflater.inflate(R.layout.crousel_design, container, false);


        // create Object
        CarouselListObject object = carouselListObjects.get(position);

        // referencing the image view from the item.xml file
        ImageView imageView = itemView.findViewById(R.id.imageViewMain);
        TextView title = itemView.findViewById(R.id.title);
        TextView desc = itemView.findViewById(R.id.desc);
        Button action = itemView.findViewById(R.id.action);
        View transparent = itemView.findViewById(R.id.transparent);

        title.setText(object.getTitle());
        desc.setText(object.getDesc());
        action.setText(object.getLabel());

        // set Font family
        title.setTypeface(AppFont.getBlackFont(context));
        desc.setTypeface(AppFont.getRegularFont(context));
        action.setTypeface(AppFont.getBoldFont(context));
        String image = object.getImageUrl();


        if (Utils.getLanguage(context).equals("ru")) {
            title.setText(object.getTitle_ru());
            desc.setText(object.getText_ru());
            action.setText(object.getLabel_ru());
            image = object.getImg_mobile_ru();
        }

        if (object.getLabel().isEmpty() || object.getLabel_ru().isEmpty()
                || object.getSrc().isEmpty() || object.getSrc().equals("/"))
            action.setVisibility(View.GONE);

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(object.getSrc());
                String page = "";
                String tm_id_list = uri.getQueryParameter("trademarks");
                String cat_id_list = uri.getQueryParameter("categories");
                String color_id_list = uri.getQueryParameter("colors");
                String size_id_list = uri.getQueryParameter("sizes");
                Double min = null;
                Double max = null;
                if (uri.getQueryParameter("min") != null)
                    min = Double.valueOf(uri.getQueryParameter("min"));
                if (uri.getQueryParameter("max") != null)
                    max = Double.valueOf(uri.getQueryParameter("max"));
                int isDiscount = 0;
                if (uri.getQueryParameter("discount") != null)
                    isDiscount = Integer.parseInt(uri.getQueryParameter("discount"));
                Products products = new Products();
                Products.brandsStr.clear();
                Products.categoriesStr.clear();
                Products.colorsStr.clear();
                Products.sizesStr.clear();
                Products.isDiscount = isDiscount;

                if (cat_id_list != null && cat_id_list.length() > 0) {
                    String[] separated = cat_id_list.split(",");
                    Products.categoriesStr.addAll(Arrays.asList(separated));
                }

                if (tm_id_list != null && tm_id_list.length() > 0) {
                    String[] separated = tm_id_list.split(",");
                    Products.brandsStr.addAll(Arrays.asList(separated));
                }

                if (color_id_list != null && color_id_list.length() > 0) {
                    String[] separated = color_id_list.split(",");
                    Products.colorsStr.addAll(Arrays.asList(separated));
                }

                if (size_id_list != null && size_id_list.length() > 0) {
                    String[] separated = size_id_list.split(",");
                    Products.sizesStr.addAll(Arrays.asList(separated));
                }
                if (min != null)
                    Products.min = min;
                if (max != null)
                    Products.max = max;
                String id = "";
                if (uri.getPathSegments().size() > 1)
                    page = uri.getPathSegments().get(0);
                if (uri.getPathSegments().size() >= 2) {
                    if (page.equals("products")) {
                        id = uri.getPathSegments().get(1);
                    }
                    if (!id.isEmpty())
                        Products.categoriesStr.add(id);
                }

                products.catName = title.getText().toString();
                products.type = 3;
                Products.where = "home";
                Utils.productFragment(products, Products.class.getSimpleName(), fragmentManager, R.id.content);
                MainActivity.firstFragment = products;
//                    MainActivity.setBottomNavigationSelectedItem(activity, R.id.search);

            }
        });


//        transparent.setVisibility(View.GONE);
        // setting the image in the imageView
        Glide.with(context)
                .load(Constant.SERVER_URL + image)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        skeletonScreen.hide();
//                        transparent.setVisibility(View.GONE);
                        return false;
                    }


                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        skeletonScreen.hide();
//                        transparent.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(imageView);


        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        if (position == carouselListObjects.size() - 2) {
            viewPager.post(runnable);
        }

        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            carouselListObjects.addAll(carouselListObjects);
            notifyDataSetChanged();
        }
    };


}
