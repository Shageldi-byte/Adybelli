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
import android.widget.Toast;

import com.adybelli.android.CategoryAdapter.FilterMainAdapter;
import com.adybelli.android.CategoryModel.Brands;
import com.adybelli.android.CategoryModel.Category;
import com.adybelli.android.CategoryModel.Color;
import com.adybelli.android.CategoryModel.FilterMain;
import com.adybelli.android.CategoryModel.Size;
import com.adybelli.android.Fragment.Products;
import com.adybelli.android.Object.ProductOptionBody;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;

import java.util.ArrayList;

public class FilterList extends Fragment {

    private View view;
    private ArrayList<FilterMain> arrayList=new ArrayList<>();
    private TextView title,clear;
    private Button acceptBtn;
    private RecyclerView rec;
    private Context context;
    public ProductOptionBody productOptionBody;
    private FragmentManager fragmentManager;
    public FilterList(FragmentManager fragmentManager) {
        this.fragmentManager=fragmentManager;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_filter_list, container, false);
        context=getContext();
        initComponents();
        setFonts();
        setListener();
        addItems();
        return view;
    }

    private void setListener() {
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Products fragment = (Products) fragmentManager.findFragmentByTag(Products.class.getSimpleName());
                fragment.closeNav();
                fragment.request(1);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Products.brandsStr.clear();
                Products.sizesStr.clear();
                Products.colorsStr.clear();
                Products.categoriesStr.clear();
                Products.min=null;
                Products.max=null;
                Products.isDiscount=0;

                if (Products.type2 == 0 && !Products.catId2.isEmpty())
                    Products.categoriesStr.add(Products.catId2);
                else if (Products.type2 == 1 && !Products.catId2.isEmpty())
                    Products.brandsStr.add(Products.catId2);

                Products fragment = (Products) fragmentManager.findFragmentByTag(Products.class.getSimpleName());
                fragment.closeNav();
                fragment.request(1);

            }
        });
    }

    private void initComponents(){
        title=view.findViewById(R.id.title);
        clear=view.findViewById(R.id.clear);
        acceptBtn=view.findViewById(R.id.acceptBtn);
        rec=view.findViewById(R.id.rec);
    }

    private void addItems(){
        sortItems();
        arrayList.clear();
        arrayList.add(new FilterMain(1,context.getResources().getString(R.string.category),"Category"));
        arrayList.add(new FilterMain(1,context.getResources().getString(R.string.brands),"Brand"));
        arrayList.add(new FilterMain(1,context.getResources().getString(R.string.cost_),"Cost"));
        arrayList.add(new FilterMain(1,context.getResources().getString(R.string.size__),"Size"));
        arrayList.add(new FilterMain(1,context.getResources().getString(R.string.colour),"Color"));
        arrayList.add(new FilterMain(1,context.getResources().getString(R.string.discount_products),"discount"));
//        arrayList.add(new FilterMain(1,"Kargo görnüşi","Default"));
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        FilterMainAdapter mainAdapter=new FilterMainAdapter(arrayList,context,getFragmentManager(),productOptionBody,fragmentManager);
        rec.setLayoutManager(linearLayoutManager);
        rec.setAdapter(mainAdapter);

    }

    private void sortItems() {
        ArrayList<Size> tempSize=new ArrayList<>();
        tempSize.clear();
        for(Size size:productOptionBody.getSizes()){
            if(Products.sizesStr.contains(size.getId()+"")){
                tempSize.add(size);
            }
        }
        for(Size size:productOptionBody.getSizes()){
            if(!Products.sizesStr.contains(size.getId()+"")){
                tempSize.add(size);
            }
        }

        ArrayList<Brands> tempBrands=new ArrayList<>();
        tempBrands.clear();
        for(Brands brand:productOptionBody.getTrademarks()){
            if(Products.brandsStr.contains(brand.getTm_id()+"")){
                tempBrands.add(brand);
            }
        }
        for(Brands brand:productOptionBody.getTrademarks()){
            if(!Products.brandsStr.contains(brand.getTm_id()+"")){
                tempBrands.add(brand);
            }
        }

        ArrayList<Category> tempCategories=new ArrayList<>();
        tempCategories.clear();
        for(Category category:productOptionBody.getCategories()){
            if(Products.categoriesStr.contains(category.getCat_id()+"")){
                tempCategories.add(category);
            }
        }
        for(Category category:productOptionBody.getCategories()){
            if(!Products.categoriesStr.contains(category.getCat_id()+"")){
                tempCategories.add(category);
            }
        }

        ArrayList<Color> tempColors=new ArrayList<>();
        tempColors.clear();
        for(Color color:productOptionBody.getColors()){
            if(Products.colorsStr.contains(color.getId()+"")){
                tempColors.add(color);
            }
        }
        for(Color color:productOptionBody.getColors()){
            if(!Products.colorsStr.contains(color.getId()+"")){
                tempColors.add(color);
            }
        }

        productOptionBody=new ProductOptionBody(tempSize,tempBrands,tempCategories,tempColors);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        addItems();
    }

    @Override
    public void onResume() {
        super.onResume();
        addItems();
    }

    private void setFonts(){
        title.setTypeface(AppFont.getBoldFont(context));
        clear.setTypeface(AppFont.getSemiBoldFont(context));
        acceptBtn.setTypeface(AppFont.getBoldFont(context));
    }
}