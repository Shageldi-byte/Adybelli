package com.adybelli.android.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Adapter.TabAdapter;
import com.adybelli.android.Common.Util;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.google.android.material.tabs.TabLayout;


public class Profile extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View view;
    private Context context;
    private FragmentManager fragmentManager;
    private TextView back;
    private static Profile INSTANCE;
    public Profile() { }


    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.Material);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        view=localInflater.inflate(R.layout.fragment_profile, container, false);
        context=getContext();
        initComponents();
        tabLayout();
        setListener();
        INSTANCE=this;
        return view;
    }

    public static Profile get(){
        return INSTANCE;
    }

    public TabLayout getTabLayout(){
        return tabLayout;
    }


    private void initComponents(){
        tabLayout=view.findViewById(R.id.tab_layout);
        viewPager=view.findViewById(R.id.pager);
        back=view.findViewById(R.id.back);
        viewPager.setOffscreenPageLimit(3);
    }

    private void tabLayout(){
        tabLayout.setupWithViewPager(viewPager);
        TabAdapter adapter=new TabAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new EditProfile(),context.getResources().getString(R.string.profile));
//        adapter.addFragment(new ChangePassword(),"Parol çalyşmak");
        adapter.addFragment(new ChangeNumber(),context.getResources().getString(R.string.change_mail));
        viewPager.setAdapter(adapter);

        Util.setFont(context,tabLayout, AppFont.getRegularFont(context));
        Util.setFont(context,tabLayout.getTabAt(0).view,AppFont.getBoldFont(context));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Util.setFont(context,tab.view,AppFont.getBoldFont(context));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Util.setFont(context,tab.view,AppFont.getRegularFont(context));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setListener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.removeShow(new ProfileRootFragment(), ProfileRootFragment.class.getSimpleName(), getFragmentManager(), R.id.content);
                MainActivity.fifthFragment = new ProfileRootFragment();
            }
        });
    }


}