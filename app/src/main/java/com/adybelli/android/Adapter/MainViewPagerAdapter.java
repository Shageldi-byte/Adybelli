package com.adybelli.android.Adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.adybelli.android.Fragment.Basket;
import com.adybelli.android.Fragment.Favourite;
import com.adybelli.android.Fragment.Home;
import com.adybelli.android.Fragment.ProfilePage;
import com.adybelli.android.Fragment.RootFragment.FifthRootFragment;
import com.adybelli.android.Fragment.RootFragment.FirstRootFragment;
import com.adybelli.android.Fragment.RootFragment.FourthRootFragment;
import com.adybelli.android.Fragment.RootFragment.SecondRootFragment;
import com.adybelli.android.Fragment.RootFragment.ThirdRootFragment;
import com.adybelli.android.Fragment.Search;

import org.jetbrains.annotations.NotNull;

public class MainViewPagerAdapter extends FragmentStatePagerAdapter {


    public MainViewPagerAdapter(@NonNull @NotNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
               return FirstRootFragment.newInstance();
            case 1:
                return SecondRootFragment.newInstance();
            case 2:
                return ThirdRootFragment.newInstance();
            case 3:
                return FourthRootFragment.newInstance();
            case 4:
                return FifthRootFragment.newInstance();
            default:
                return FirstRootFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
