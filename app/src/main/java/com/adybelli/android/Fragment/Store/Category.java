package com.adybelli.android.Fragment.Store;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adybelli.android.Common.Utils;
import com.adybelli.android.R;
import com.adybelli.android.StoreFilter.Kategoriya;
import com.adybelli.android.TypeFace.AppFont;

import java.util.ArrayList;


public class Category extends Fragment {

    private View view;
    public ArrayList<com.adybelli.android.CategoryModel.Category> arrayList=new ArrayList<>();
    private Context context;
    private RecyclerView rec;
    private TextView title,clear;
    private Button acceptBtn;
    private String titleStr;
    public static ArrayList categories=new ArrayList<String>();
    private FragmentManager fragmentManager;
    private EditText search;
    private ImageButton searchBtn;
    ArrayList<com.adybelli.android.CategoryModel.Category> searchList = new ArrayList<>();
    public Category(FragmentManager fragmentManager) {
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
        title.setText(Utils.getStringResource(R.string.category,context));
        return view;
    }

    private void initComponents(){
        rec=view.findViewById(R.id.rec);
        title=view.findViewById(R.id.title);
        clear=view.findViewById(R.id.clear);
        acceptBtn=view.findViewById(R.id.acceptBtn);
        searchBtn=view.findViewById(R.id.searchBtn);
        search=view.findViewById(R.id.search);
    }

    private void setListeners(){
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreProducts.setFragment(getFragmentManager(),new FilterList(fragmentManager),"FilterList","back");
            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreProducts fragment = (StoreProducts) fragmentManager.findFragmentByTag(StoreProducts.class.getSimpleName());
                fragment.closeNav();
                fragment.request(1);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreProducts.categoriesStr.clear();
                StoreProducts fragment = (StoreProducts) fragmentManager.findFragmentByTag(StoreProducts.class.getSimpleName());
                fragment.closeNav();
                fragment.request(1);

            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(search.getText().toString().trim().isEmpty()){
                    setFilters();
                    searchList.clear();
                    return;
                }
                searchList.clear();
                for(com.adybelli.android.CategoryModel.Category cost:arrayList){
                    try {
                        if (cost.getTitle().toLowerCase().contains(search.getText().toString().toLowerCase()) || cost.getTitle_ru().toLowerCase().contains(search.getText().toString().toLowerCase())) {
                            searchList.add(cost);
                        }
                    } catch (Exception ex){
                        ex.printStackTrace();
                        continue;
                    }
                }
                Kategoriya adapter=new Kategoriya(searchList,context);
                rec.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setFilters(){

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        Kategoriya adapter=new Kategoriya(arrayList,context);
        rec.setLayoutManager(linearLayoutManager);
        rec.setAdapter(adapter);
    }

    private void setFonts(){
        title.setTypeface(AppFont.getBoldFont(context));
        clear.setTypeface(AppFont.getSemiBoldFont(context));
        acceptBtn.setTypeface(AppFont.getBoldFont(context));
    }


}