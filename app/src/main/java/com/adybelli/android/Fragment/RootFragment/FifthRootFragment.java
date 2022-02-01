package com.adybelli.android.Fragment.RootFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adybelli.android.Fragment.ProfilePage;
import com.adybelli.android.Fragment.Search;
import com.adybelli.android.R;


public class FifthRootFragment extends Fragment {
    private View view;
    private Context context;
    public FifthRootFragment() {
    }


    public static FifthRootFragment newInstance() {
        FifthRootFragment fragment = new FifthRootFragment();
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
        view=inflater.inflate(R.layout.fragment_fifth_root, container, false);
        context=getContext();
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        /*
         * When this container fragment is created, we fill it with our first
         * "real" fragment
         */
        transaction.replace(R.id.fifth_root_frame, new ProfilePage());

        transaction.commit();
        return view;
    }
}