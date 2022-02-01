package com.adybelli.android.Fragment.NavbarFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.adybelli.android.CategoryAdapter.DefaultAdapter;
import com.adybelli.android.CategoryModel.Default;
import com.adybelli.android.Fragment.Products;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;

import java.util.ArrayList;


public class DefaultFilter extends Fragment {

    private View view;
    private ArrayList<Default> arrayList=new ArrayList<>();
    private Context context;
    private RecyclerView rec;
    private TextView title,clear;
    private Button acceptBtn;
    private String titleStr;
    private FragmentManager fragmentManager;
    public DefaultFilter(ArrayList<Default> arrayList,String titleStr,FragmentManager fragmentManager) {
        this.arrayList=arrayList;
        this.titleStr=titleStr;
        this.fragmentManager=fragmentManager;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_default_filter, container, false);
        context=getContext();
        initComponents();
        setListeners();
        setFilters();
        setFonts();
        title.setText(titleStr);
        return view;
    }

    private void initComponents(){
        rec=view.findViewById(R.id.rec);
        title=view.findViewById(R.id.title);
        clear=view.findViewById(R.id.clear);
        acceptBtn=view.findViewById(R.id.acceptBtn);
    }

    private void setListeners(){
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Products.setFragment(getFragmentManager(),new FilterList(fragmentManager),"FilterList","back");
            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Products fragment = (Products) fragmentManager.findFragmentByTag(Products.class.getSimpleName());
                fragment.closeNav();
                fragment.request(1);
            }
        });
    }

    private void setFilters(){
        
//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
//        DefaultAdapter adapter=new DefaultAdapter(arrayList,context);
//        rec.setLayoutManager(linearLayoutManager);
//        rec.setAdapter(adapter);
    }

    private void setFonts(){
        title.setTypeface(AppFont.getBoldFont(context));
        clear.setTypeface(AppFont.getSemiBoldFont(context));
        acceptBtn.setTypeface(AppFont.getBoldFont(context));
    }


}