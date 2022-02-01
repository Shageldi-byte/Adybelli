package com.adybelli.android.Fragment.RootFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adybelli.android.Fragment.AgzaBol;
import com.adybelli.android.Fragment.CreatAccount;
import com.adybelli.android.Fragment.Home;
import com.adybelli.android.Fragment.Search;
import com.adybelli.android.R;


public class SecondRootFragment extends Fragment {


    private View view;
    private Context context;
    public SecondRootFragment() {
    }


    public static SecondRootFragment newInstance() {
        SecondRootFragment fragment = new SecondRootFragment();
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
        view=inflater.inflate(R.layout.fragment_second_root, container, false);
        context=getContext();
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        /*
         * When this container fragment is created, we fill it with our first
         * "real" fragment
         */
        transaction.replace(R.id.second_root, new CreatAccount());

        transaction.commit();
        return view;
    }
}