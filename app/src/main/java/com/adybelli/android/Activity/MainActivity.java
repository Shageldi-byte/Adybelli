package com.adybelli.android.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;


import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adybelli.android.Adapter.MainViewPagerAdapter;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Fragment.Addres;
import com.adybelli.android.Fragment.AllBrands;
import com.adybelli.android.Fragment.AllStores;
import com.adybelli.android.Fragment.Basket;
import com.adybelli.android.Fragment.ConstantPage;
import com.adybelli.android.Fragment.Favourite;
import com.adybelli.android.Fragment.Home;
import com.adybelli.android.Fragment.Login;
import com.adybelli.android.Fragment.MoreInfo;
import com.adybelli.android.Fragment.Orders;
import com.adybelli.android.Fragment.ProcceedCheckout;
import com.adybelli.android.Fragment.Products;
import com.adybelli.android.Fragment.Profile;
import com.adybelli.android.Fragment.ProfilePage;
import com.adybelli.android.Fragment.ProfileRootFragment;
import com.adybelli.android.Fragment.RootFragment.FifthRootFragment;
import com.adybelli.android.Fragment.RootFragment.FirstRootFragment;
import com.adybelli.android.Fragment.RootFragment.FourthRootFragment;
import com.adybelli.android.Fragment.RootFragment.SecondRootFragment;
import com.adybelli.android.Fragment.RootFragment.ThirdRootFragment;
import com.adybelli.android.Fragment.Search;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Context context = this;
    public static Fragment firstFragment = new Home();
    public static Fragment secondFragment = new Search();
    public static Fragment thirdFragment = new Favourite();
    public static Fragment fourthFragment = new Basket();
    public static Fragment fifthFragment = new ProfileRootFragment();
    private static MainActivity INSTANCE;
    FirebaseFirestore db;
    private ImageView splash_image;
    private Handler handler;
    private int splash_count = 0;
    private int old_item_id=R.id.main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.loadLocal(context);
        FirebaseApp.initializeApp(context);
//        db=FirebaseFirestore.getInstance();
        INSTANCE = this;
        int topic = 0;
        if (!Utils.getSharedPreference(context, "userId").isEmpty()) {
            topic = Integer.parseInt(Utils.getSharedPreference(context, "userId")) / 1000;
            if(topic==0){
                FirebaseMessaging.getInstance().subscribeToTopic("com.adybelli.project");
            }
            FirebaseMessaging.getInstance().subscribeToTopic("com.adybelli.project" + topic);
        } else {
            FirebaseMessaging.getInstance().subscribeToTopic("com.adybelli.project0");
        }

        Log.e("Topic",topic+"");


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //bundle must contain all info sent in "data" field of the notification
            if (bundle.get("typed") != null) {
                if (bundle.get("typed").equals("products")) {

                    Products products = new Products();
                    Products.brandsStr.clear();
                    Products.categoriesStr.clear();
                    Products.colorsStr.clear();
                    Products.sizesStr.clear();

                    Object is_discount = bundle.get("is_discount");
                    if (is_discount == null)
                        is_discount = 0;
                    else
                        is_discount = 1;

                    Products.isDiscount = (Integer) is_discount;
                    Object tm_id_list = bundle.get("tm_id_list");
                    if (tm_id_list != null) {
                        String[] separated = tm_id_list.toString().split(",");
                        Products.brandsStr.addAll(Arrays.asList(separated));
                    }
                    Object cat_id_list = bundle.get("cat_id_list");
                    if (cat_id_list != null) {
                        String[] separated = cat_id_list.toString().split(",");
                        Products.categoriesStr.addAll(Arrays.asList(separated));
                    }
                    Object size_id_list = bundle.get("size_id_list");
                    if (size_id_list != null) {
                        String[] separated = size_id_list.toString().split(",");
                        Products.sizesStr.addAll(Arrays.asList(separated));
                    }
                    Object pc_id_list = bundle.get("pc_id_list");
                    if (pc_id_list != null) {
                        String[] separated = pc_id_list.toString().split(",");
                        Products.colorsStr.addAll(Arrays.asList(separated));
                    }

                    Object min = bundle.get("min");
                    if (min != null) {
                        Products.min = (Double) min;
                    }
                    Object max = bundle.get("max");
                    if (max != null) {
                        Products.max = (Double) max;
                    }

                    Utils.reAdd(products, Products.class.getSimpleName(), getSupportFragmentManager(), R.id.content);
                    secondFragment = products;
                    setBottomNavigationSelectedItem(this, R.id.search);

                }

                if (bundle.get("typed").equals("favorites")) {
                    Utils.reAdd(new Favourite(), Favourite.class.getSimpleName(), getSupportFragmentManager(), R.id.content);
                    thirdFragment = new Favourite();
                    setBottomNavigationSelectedItem(this, R.id.favourite);
                }

                if (bundle.get("typed").equals("cart")) {
                    Utils.reAdd(new Basket(), Basket.class.getSimpleName(), getSupportFragmentManager(), R.id.content);
                    fourthFragment = new Basket();
                    setBottomNavigationSelectedItem(this, R.id.basket);
                }

                if (bundle.get("typed").equals("order")) {
                    Integer order_id = (Integer) bundle.get("order_id");
                    if (order_id != null) {
                        MoreInfo moreInfo = new MoreInfo(order_id, null, null, null);
                        Utils.reAdd(moreInfo, MoreInfo.class.getSimpleName(), getSupportFragmentManager(), R.id.content);
                        fifthFragment = moreInfo;
                        setBottomNavigationSelectedItem(this, R.id.profile);
                    }

                }
            }
        }


        initComponents();
        setListeners();
        startSplash();
    }

    private void startSplash() {
        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                handler.postDelayed(this, 500);
                AlphaAnimation animation1 = new AlphaAnimation(1.0f, 1.0f);
                animation1.setDuration(1000);
                animation1.setStartOffset(1000);
                animation1.setFillAfter(true);
                splash_image.startAnimation(animation1);
                splash_count++;
                if (splash_count == 1)
                    splash_image.setImageResource(R.drawable.splash_second);
                if (splash_count == 2) {
                    splash_image.setImageResource(R.drawable.splash_third);
                    splash_count = 0;
                }


            }
        };

        handler.postDelayed(r, 500);
    }


    private void initComponents() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        splash_image = findViewById(R.id.splash_image);
        setBottomNavigationFont(context, bottomNavigationView);

        ViewGroup layout = (ViewGroup) findViewById(R.id.yourLayout);
        LayoutTransition layoutTransition = layout.getLayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);

    }

    public static MainActivity get() {
        return INSTANCE;
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }


    private void setListeners() {
        // Bottom navigation listener
        changeFragment(firstFragment, firstFragment.getClass().getSimpleName());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.main:
                        Home home = (Home) getSupportFragmentManager().findFragmentByTag(Home.class.getSimpleName());
                        if (home != null && !home.isVisible() && item.getItemId()==old_item_id){
                            firstFragment=home;
                            Utils.removeShow(firstFragment, firstFragment.getClass().getSimpleName(),getSupportFragmentManager(),R.id.content);
                        } else{
                            changeFragment(firstFragment, firstFragment.getClass().getSimpleName());
                        }
                        old_item_id=item.getItemId();
                        break;

                    case R.id.search:
                        Search category = (Search) getSupportFragmentManager().findFragmentByTag(Search.class.getSimpleName());
                        if(category != null && !category.isVisible() && item.getItemId()==old_item_id){
                            secondFragment=category;
                            Utils.removeShow(secondFragment, secondFragment.getClass().getSimpleName(),getSupportFragmentManager(),R.id.content);
                        } else{
                            changeFragment(secondFragment, secondFragment.getClass().getSimpleName());
                        }
                        old_item_id=item.getItemId();
                        break;

                    case R.id.favourite:
                        Favourite favourite = (Favourite) getSupportFragmentManager().findFragmentByTag(Favourite.class.getSimpleName());
                        if(favourite!=null && !favourite.isVisible() && item.getItemId()==old_item_id){
                            thirdFragment=favourite;
                            Utils.removeShow(thirdFragment, thirdFragment.getClass().getSimpleName(),getSupportFragmentManager(),R.id.content);
                        } else{
                            changeFragment(thirdFragment, thirdFragment.getClass().getSimpleName());
                        }
                        old_item_id=item.getItemId();
                        break;

                    case R.id.basket:
                        Basket basket = (Basket) getSupportFragmentManager().findFragmentByTag(Basket.class.getSimpleName());
                        if(basket!=null && !basket.isVisible() && item.getItemId()==old_item_id){
                            fourthFragment=basket;
                            Utils.removeShow(fourthFragment, fourthFragment.getClass().getSimpleName(),getSupportFragmentManager(),R.id.content);
                        } else{
                            changeFragment(fourthFragment, fourthFragment.getClass().getSimpleName());
                        }
                        old_item_id=item.getItemId();
                        break;

                    case R.id.profile:
                        ProfileRootFragment profilePage = (ProfileRootFragment) getSupportFragmentManager().findFragmentByTag(ProfileRootFragment.class.getSimpleName());
                        if(profilePage!=null && !profilePage.isVisible() && item.getItemId()==old_item_id){
                            fifthFragment=profilePage;
                            Utils.removeShow(fifthFragment, fifthFragment.getClass().getSimpleName(),getSupportFragmentManager(),R.id.content);
                        } else{
                            changeFragment(fifthFragment, fifthFragment.getClass().getSimpleName());
                        }
                        old_item_id=item.getItemId();
                        break;

                }

                return true;


            }
        });


    }

    public static void setBottomNavigationFont(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    setBottomNavigationFont(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(AppFont.getRegularFont(context));
                ((TextView) v).setTextSize(10);
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportFragmentManager().getFragments().clear();
        firstFragment.onDetach();
        secondFragment.onDetach();
        thirdFragment.onDetach();
        fourthFragment.onDetach();
        fifthFragment.onDetach();
        firstFragment = new Home();
        secondFragment = new Search();
        thirdFragment = new Favourite();
        fourthFragment = new Basket();
        fifthFragment = new ProfileRootFragment();
    }

    @Override
    public void onBackPressed() {
        Home home = (Home) getSupportFragmentManager().findFragmentByTag(Home.class.getSimpleName());
        Search category = (Search) getSupportFragmentManager().findFragmentByTag(Search.class.getSimpleName());
        Favourite favourite = (Favourite) getSupportFragmentManager().findFragmentByTag(Favourite.class.getSimpleName());
        Basket basket = (Basket) getSupportFragmentManager().findFragmentByTag(Basket.class.getSimpleName());
        ProfileRootFragment profilePage = (ProfileRootFragment) getSupportFragmentManager().findFragmentByTag(ProfileRootFragment.class.getSimpleName());


        Products popular = (Products) getSupportFragmentManager().findFragmentByTag(Products.class.getSimpleName());
        if (popular != null && popular.isVisible()) {
            if (popular.isDrawerOpen()) {
                popular.closeNav();
            } else {
                if (Products.where == null || Products.where.equals("cats")) {
                    Utils.removeShow(new Search(), Search.class.getSimpleName(), getSupportFragmentManager(), R.id.content);
                    secondFragment = new Search();
                } else if (Products.where.equals("home")) {
                    Utils.removeShow(new Home(), Home.class.getSimpleName(), getSupportFragmentManager(), R.id.content);
                    firstFragment = new Home();
                } else if (Products.where.equals("allBrands")) {
                    Utils.removeShow(new AllBrands(), AllBrands.class.getSimpleName(), getSupportFragmentManager(), R.id.content);
                    firstFragment = new AllBrands();
                    return;
                }
            }

        } else {
            if ((home != null && home.isVisible())) {
                finish();
            }
            if((category != null && category.isVisible()) || (favourite != null && favourite.isVisible()) || (basket != null && basket.isVisible()) || (profilePage != null && profilePage.isVisible())){
                setBottomNavigationSelectedItem(this,R.id.main);
            }
        }

        AllBrands allBrands = (AllBrands) getSupportFragmentManager().findFragmentByTag(AllBrands.class.getSimpleName());
        if (allBrands != null && allBrands.isVisible()) {
            Utils.removeShow(new Home(), Home.class.getSimpleName(), getSupportFragmentManager(), R.id.content);
            firstFragment = new Home();
        }


        AllStores allStores = (AllStores) getSupportFragmentManager().findFragmentByTag(AllStores.class.getSimpleName());
        if (allStores != null && allStores.isVisible()) {
            Utils.removeShow(new Home(), Home.class.getSimpleName(), getSupportFragmentManager(), R.id.content);
            firstFragment = new Home();
        }


        ProcceedCheckout procceedCheckout = (ProcceedCheckout) getSupportFragmentManager().findFragmentByTag(ProcceedCheckout.class.getSimpleName());
        if (procceedCheckout != null && procceedCheckout.isVisible()) {
            Utils.removeShow(new Basket(), Basket.class.getSimpleName(), getSupportFragmentManager(), R.id.content);
            fourthFragment = new Basket();
        }


        Addres addres = (Addres) getSupportFragmentManager().findFragmentByTag(Addres.class.getSimpleName());
        if (addres != null && addres.isVisible()) {
            Utils.removeShow(new ProfileRootFragment(), ProfileRootFragment.class.getSimpleName(), getSupportFragmentManager(), R.id.content);
            fifthFragment = new ProfileRootFragment();
        }


        Orders orders = (Orders) getSupportFragmentManager().findFragmentByTag(Orders.class.getSimpleName());
        if (orders != null && orders.isVisible()) {
            Utils.removeShow(new ProfileRootFragment(), ProfileRootFragment.class.getSimpleName(), getSupportFragmentManager(), R.id.content);
            fifthFragment = new ProfileRootFragment();
        }

        Profile profile = (Profile) getSupportFragmentManager().findFragmentByTag(Profile.class.getSimpleName());
        if (profile != null && profile.isVisible()) {
            Utils.removeShow(new ProfileRootFragment(), ProfileRootFragment.class.getSimpleName(), getSupportFragmentManager(), R.id.content);
            fifthFragment = new ProfileRootFragment();
        }

        ConstantPage constantPage = (ConstantPage) getSupportFragmentManager().findFragmentByTag(ConstantPage.class.getSimpleName());
        if (constantPage != null && constantPage.isVisible()) {
            Utils.removeShow(new ProfileRootFragment(), ProfileRootFragment.class.getSimpleName(), getSupportFragmentManager(), R.id.content);
            fifthFragment = new ProfileRootFragment();
        }


        MoreInfo moreInfo = (MoreInfo) getSupportFragmentManager().findFragmentByTag(MoreInfo.class.getSimpleName());
        if (moreInfo != null && moreInfo.isVisible()) {
            Utils.removeShow(new Orders(), Orders.class.getSimpleName(), getSupportFragmentManager(), R.id.content);
            fifthFragment = new Orders();
        }




    }

    public static void setVisiblitybottomNavigationView(Activity activity, int visibilty) {
        try {
            BottomNavigationView bn = activity.findViewById(R.id.bottomNavigationView);
            bn.setVisibility(visibilty);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void setBottomNavigationSelectedItem(Activity activity, int itemId) {
        try {
            BottomNavigationView bn = activity.findViewById(R.id.bottomNavigationView);
            bn.setSelectedItemId(itemId);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void changeFragment(Fragment fragment, String tagFragmentName) {

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            try {
                currentFragment.getView().setVisibility(View.GONE);
            } catch (Exception ex){
                ex.printStackTrace();
            }
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            fragmentTransaction.add(R.id.content, fragmentTemp, tagFragmentName);
        } else {
            try {
                fragmentTemp.getView().setVisibility(View.VISIBLE);
            } catch (Exception ex){
                ex.printStackTrace();
            }
            fragmentTransaction.show(fragmentTemp);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
    }


}