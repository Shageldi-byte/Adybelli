package com.adybelli.android.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Adapter.TabAdapter;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Fragment.RootFragment.FirstRootFragment;
import com.adybelli.android.Fragment.RootFragment.SecondRootFragment;
import com.adybelli.android.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;


public class Login extends Fragment {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View view;
    private Context context;
    private FragmentManager fragmentManager;
    private TextView changeLanguage;
    private static Login INSTANCE;
    public Login() {}


    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager=getChildFragmentManager();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentManager=null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.Material);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        view=localInflater.inflate(R.layout.fragment_login, container, false);
        context=getContext();
        INSTANCE=this;
        initComponents();
        tabLayout();
        setListener();
        // inflate the layout using the cloned inflater, not default inflater
        return view;
    }

    public static Login get(){
        return INSTANCE;
    }

    public TabLayout getTabLayout(){
        return tabLayout;
    }



    private void setListener() {
        changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    private void initComponents(){
        tabLayout=view.findViewById(R.id.tab_layout);
        viewPager=view.findViewById(R.id.pager);
        changeLanguage=view.findViewById(R.id.changeLanguage);
        viewPager.setOffscreenPageLimit(2);
    }

    private void tabLayout(){
        tabLayout.setupWithViewPager(viewPager);
        TabAdapter adapter=new TabAdapter(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new FirstRootFragment(),context.getResources().getString(R.string.login));
        adapter.addFragment(new SecondRootFragment(),context.getResources().getString(R.string.create_account));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void showDialog() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.change_language);
        TextView first=bottomSheetDialog.findViewById(R.id.first);
        TextView second=bottomSheetDialog.findViewById(R.id.second);

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Utils.setLocale("tm",context);
                MainActivity.firstFragment = new Home();
                MainActivity.secondFragment = new Search();
                MainActivity.thirdFragment = new Favourite();
                MainActivity.fourthFragment = new Basket();
                MainActivity.fifthFragment = new ProfileRootFragment();
                MainActivity.setBottomNavigationSelectedItem(getActivity(),R.id.main);
                restart();
            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Utils.setLocale("ru",context);
                MainActivity.firstFragment = new Home();
                MainActivity.secondFragment = new Search();
                MainActivity.thirdFragment = new Favourite();
                MainActivity.fourthFragment = new Basket();
                MainActivity.fifthFragment = new ProfileRootFragment();
                MainActivity.setBottomNavigationSelectedItem(getActivity(),R.id.main);
                restart();

            }
        });
        bottomSheetDialog.show();





    }

    private void restart() {
        Intent i = context.getPackageManager().
                getLaunchIntentForPackage(context.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        getActivity().finish();
    }


}