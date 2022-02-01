package com.adybelli.android.Common;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.adybelli.android.Adapter.ImageViewerAdapter;
import com.adybelli.android.Adapter.ImageViewerSelectorAdapter;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Objects;

public class Util {
    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        if(noOfColumns<2){
            noOfColumns=2;
        }
        return noOfColumns;
    }

    public static void setFont(final Context context, final View v, Typeface font) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    setFont(context, child, font);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(font);
                ((TextView) v).setTextSize(14);
                ((TextView) v).setAllCaps(false);

            }
        } catch (Exception e) {
        }
    }

    public static void popBackStack(FragmentManager fragmentManager,String name){
        fragmentManager.popBackStackImmediate(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public static void setFragment(FragmentManager fragmentManager, Fragment fragment, String name, String type,int frame) {
        Fragment fragment1 = fragmentManager.findFragmentByTag(name);
        int enter = R.anim.slide_in_right;
        int exit = R.anim.slide_out_left;
        if (type.equals("back")) {
            enter = R.anim.slide_in_left;
            exit = R.anim.slide_out_right;
        }
        boolean has = false;
        if (fragment1 == null) {
            has = false;
        } else {
            has = true;
        }
        if (has) {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(enter, exit)
                    .replace(frame, fragment, name)
                    .addToBackStack(name)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(enter, exit)
                    .replace(frame, fragment, name)
                    .addToBackStack(name)
                    .commit();
        }
    }

    public static void showImageViewer(Context context, ArrayList<String> images,ArrayList<String> largeImages) {
        Dialog dialog = new Dialog(context);
        final Context contextThemeWrapper = new ContextThemeWrapper(((Activity) context), R.style.Material);
        LayoutInflater localInflater = LayoutInflater.from(context).cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.image_viewer, null, false);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
//        window.getAttributes().windowAnimations = R.style.DialogAnimation;

        ViewPager pager = dialog.findViewById(R.id.pager);
        ImageView back=dialog.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ImageViewerAdapter adapter = new ImageViewerAdapter(largeImages, context);
        pager.setAdapter(adapter);



        LinearLayout linear = dialog.findViewById(R.id.linear);
        HorizontalScrollView horizontalScrollView=dialog.findViewById(R.id.rec);

        if(images.size()<2){
            horizontalScrollView.setVisibility(View.GONE);
        }



        for (int i=0;i<images.size();i++) {
            String img = images.get(i);
            View selector = LayoutInflater.from(context).inflate(R.layout.image_selector, null, false);
            ImageView image = selector.findViewById(R.id.image);
            LinearLayout unselect = selector.findViewById(R.id.unselect);
            if(i==0){
                unselect.setVisibility(View.GONE);
            }
            Glide.with(context)
                    .load(Constant.SERVER_URL+img)
                    .placeholder(R.drawable.placeholder)
                    .into(image);
            int finalI = i;
            selector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pager.setCurrentItem(finalI);
                }
            });
            linear.addView(selector);
        }
        int[] old = {0};
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                View unselectView = linear.getChildAt(position);
                LinearLayout unselect = unselectView.findViewById(R.id.unselect);
                LinearLayout oldUnselectView = linear.getChildAt(old[0]).findViewById(R.id.unselect);
                oldUnselectView.setVisibility(View.VISIBLE);
                old[0] = position;
                unselect.setVisibility(View.GONE);
                horizontalScrollView.scrollTo(unselectView.getLeft(),unselectView.getTop());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        dialog.setCancelable(true);
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        dialog.show();
    }


    public static void hideAdd(Fragment fragment, String tagFragmentName,FragmentManager mFragmentManager) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            fragmentTransaction.add(R.id.content, fragmentTemp, tagFragmentName);
        } else {
            fragmentTransaction.show(fragmentTemp);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
    }

    public static void removeShow(Fragment fragment, String tagFragmentName,FragmentManager mFragmentManager) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.remove(currentFragment);
        }

        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            fragmentTransaction.add(R.id.content, fragmentTemp, tagFragmentName);
        } else {
            fragmentTransaction.show(fragmentTemp);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
    }


}
