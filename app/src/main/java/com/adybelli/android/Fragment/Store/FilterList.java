package com.adybelli.android.Fragment.Store;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adybelli.android.CategoryModel.Brands;
import com.adybelli.android.CategoryModel.Category;
import com.adybelli.android.CategoryModel.Color;
import com.adybelli.android.CategoryModel.FilterMain;
import com.adybelli.android.CategoryModel.Size;
import com.adybelli.android.Object.ProductOptionBody;
import com.adybelli.android.R;
import com.adybelli.android.StoreFilter.FilterMainAdapter;
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
                StoreProducts fragment = (StoreProducts) fragmentManager.findFragmentByTag(StoreProducts.class.getSimpleName());
                fragment.closeNav();
                fragment.request(1);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreProducts.brandsStr.clear();
                StoreProducts.sizesStr.clear();
                StoreProducts.colorsStr.clear();
                StoreProducts.categoriesStr.clear();
                StoreProducts.min=null;
                StoreProducts.max=null;
                StoreProducts.isDiscount=0;

                if (StoreProducts.type2 == 0 && !StoreProducts.catId2.isEmpty())
                    StoreProducts.categoriesStr.add(StoreProducts.catId2);
                else if (StoreProducts.type2 == 1 && !StoreProducts.catId2.isEmpty())
                    StoreProducts.brandsStr.add(StoreProducts.catId2);

                StoreProducts fragment = (StoreProducts) fragmentManager.findFragmentByTag(StoreProducts.class.getSimpleName());
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
            if(StoreProducts.sizesStr.contains(size.getId()+"")){
                tempSize.add(size);
            }
        }
        for(Size size:productOptionBody.getSizes()){
            if(!StoreProducts.sizesStr.contains(size.getId()+"")){
                tempSize.add(size);
            }
        }

        ArrayList<Brands> tempBrands=new ArrayList<>();
        tempBrands.clear();
        for(Brands brand:productOptionBody.getTrademarks()){
            if(StoreProducts.brandsStr.contains(brand.getTm_id()+"")){
                tempBrands.add(brand);
            }
        }
        for(Brands brand:productOptionBody.getTrademarks()){
            if(!StoreProducts.brandsStr.contains(brand.getTm_id()+"")){
                tempBrands.add(brand);
            }
        }

        ArrayList<Category> tempCategories=new ArrayList<>();
        tempCategories.clear();
        for(Category category:productOptionBody.getCategories()){
            if(StoreProducts.categoriesStr.contains(category.getCat_id()+"")){
                tempCategories.add(category);
            }
        }
        for(Category category:productOptionBody.getCategories()){
            if(!StoreProducts.categoriesStr.contains(category.getCat_id()+"")){
                tempCategories.add(category);
            }
        }

        ArrayList<Color> tempColors=new ArrayList<>();
        tempColors.clear();
        for(Color color:productOptionBody.getColors()){
            if(StoreProducts.colorsStr.contains(color.getId()+"")){
                tempColors.add(color);
            }
        }
        for(Color color:productOptionBody.getColors()){
            if(!StoreProducts.colorsStr.contains(color.getId()+"")){
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