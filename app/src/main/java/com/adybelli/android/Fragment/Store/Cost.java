package com.adybelli.android.Fragment.Store;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adybelli.android.Common.Utils;
import com.adybelli.android.R;
import com.adybelli.android.StoreFilter.DefaultAdapter;
import com.adybelli.android.TypeFace.AppFont;

import java.util.ArrayList;


public class Cost extends Fragment {

    private View view;
    private ArrayList<com.adybelli.android.CategoryModel.Cost> arrayList=new ArrayList<>();
    private Context context;
    private RecyclerView rec;
    private TextView title,clear;
    private Button acceptBtn;
    private FragmentManager fragmentManager;
    private EditText min,max;
    public Cost(ArrayList<com.adybelli.android.CategoryModel.Cost> arrayList, FragmentManager fragmentManager) {
        this.arrayList=arrayList;
        this.fragmentManager=fragmentManager;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_cost, container, false);
        context=getContext();
        initComponents();
        setListeners();
        setFilters();
        setFonts();
        title.setText(Utils.getStringResource(R.string.cost_,context));
        try {
            if (StoreProducts.min != null && StoreProducts.max != null) {
                max.setText(""+StoreProducts.max);
                min.setText(""+StoreProducts.min);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return view;
    }

    private void initComponents(){
        rec=view.findViewById(R.id.rec);
        title=view.findViewById(R.id.title);
        clear=view.findViewById(R.id.clear);
        acceptBtn=view.findViewById(R.id.acceptBtn);
        min=view.findViewById(R.id.min);
        max=view.findViewById(R.id.max);
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
                if(min.getText().toString().isEmpty() || max.getText().toString().isEmpty()){
                    return;
                }
                StoreProducts.min=Double.valueOf(min.getText().toString());
                StoreProducts.max=Double.valueOf(max.getText().toString());
                StoreProducts fragment = (StoreProducts) fragmentManager.findFragmentByTag(StoreProducts.class.getSimpleName());
                fragment.closeNav();
                fragment.request(1);
            }
        });

       clear.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               StoreProducts.min=null;
               StoreProducts.max=null;
               StoreProducts fragment = (StoreProducts) fragmentManager.findFragmentByTag(StoreProducts.class.getSimpleName());
               fragment.closeNav();
               fragment.request(1);
           }
       });
    }

    private void setFilters(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        DefaultAdapter adapter=new DefaultAdapter(arrayList,context,fragmentManager,min,max);
        rec.setLayoutManager(linearLayoutManager);
        rec.setAdapter(adapter);
    }

    private void setFonts(){
        title.setTypeface(AppFont.getBoldFont(context));
        clear.setTypeface(AppFont.getSemiBoldFont(context));
        acceptBtn.setTypeface(AppFont.getBoldFont(context));
    }
}