package com.adybelli.android.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adybelli.android.Activity.SearchPage;
import com.adybelli.android.Adapter.CategoryAdapter;
import com.adybelli.android.Adapter.MainCategoryAdapter;
import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Object.Brands;
import com.adybelli.android.Object.CarouselListObject;
import com.adybelli.android.Object.Catalog;
import com.adybelli.android.Object.CatalogBody;
import com.adybelli.android.Object.Category;
import com.adybelli.android.Object.Home;
import com.adybelli.android.Object.MainCategory;
import com.adybelli.android.Object.OfflineMainCategory;
import com.adybelli.android.Object.PopularCategories;
import com.adybelli.android.Object.ShopByGender;
import com.adybelli.android.Object.SubCategory;
import com.adybelli.android.Object.TopSold;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Search extends Fragment {
    private RecyclerView catRec;
    private RecyclerView rec;
    private View view;
    private Context context;
    private ArrayList<Category> categories=new ArrayList<>();
    private TextView searchBar;
    private TextView title;
    private ArrayList<MainCategory> mainCategories=new ArrayList<>();
    private ArrayList<Category> subCategories=new ArrayList<>();
    private ApiInterface apiInterface;
    private int active;
    private LinearLayout errorContainer;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button errorButton;
    private RelativeLayout container;
    private MainCategoryAdapter mainCategoryAdapter;
    private CategoryAdapter adapter;
    public static String categoryID=null;
    public static String subCategoryID=null;
    public static String oldCategoryID=null;
    public static String oldSubCategoryID=null;
    public Search() {}


    public static Search newInstance() {
        Search fragment = new Search();
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
        context=getContext();
        Utils.loadLocal(context);
        view=inflater.inflate(R.layout.fragment_search, container, false);
        initComponents();
        request();
        searchBar();
        title.setTypeface(AppFont.getBoldFont(context));
        return view;
    }

    private void request(){
        mainCategoryAdapter=new MainCategoryAdapter(mainCategories,context,rec,getFragmentManager(),active);
        catRec.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        SkeletonScreen skeletonScreen = Skeleton.bind(catRec)
                .adapter(mainCategoryAdapter)
                .shimmer(true)
                .frozen(false)
                .count(3)
                .color(R.color.skeletonColor)
                .load(R.layout.skelton_main_category)
                .show(); //default count is 10

        adapter=new CategoryAdapter(categories,context, getFragmentManager());
        rec.setLayoutManager(new GridLayoutManager(context,3));
        SkeletonScreen subCategorySkelton = Skeleton.bind(rec)
                .adapter(adapter)
                .shimmer(true)
                .angle(20)
                .count(15)
                .color(R.color.skeletonColor)
                .load(R.layout.skelton_main_category)
                .show(); //default count is 10


        apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<Catalog> call = apiInterface.getCatalog(Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
        call.enqueue(new Callback<Catalog>() {
            @Override
            public void onResponse(Call<Catalog> call, Response<Catalog> response) {
                Log.e("Code", response.code() + "");
                if (response.isSuccessful()) {
                    Catalog resource = response.body();
                    CatalogBody catalogBody =  resource.getBody();
                    mainCategories=(ArrayList<MainCategory>)catalogBody.getCategories();
                    subCategories=(ArrayList<Category>)catalogBody.getSubcategories();
                    active=catalogBody.getActive();
                    mainCategory();
                    category(subCategories);
                    errorContainer.setVisibility(View.GONE);

                } else{
                    skeletonScreen.hide();
                    subCategorySkelton.hide();
                    showErrorView(R.drawable.no_connection, context.getResources().getString(R.string.noInternet), getResources().getString(R.string.checkYourInternet), getResources().getString(R.string.continueValue));
                }
            }

            @Override
            public void onFailure(Call<Catalog> call, Throwable t) {
                call.cancel();
                Log.e("Error", t.getMessage());
                skeletonScreen.hide();
                subCategorySkelton.hide();
                showErrorView(R.drawable.no_connection, context.getResources().getString(R.string.noInternet), getResources().getString(R.string.checkYourInternet), getResources().getString(R.string.continueValue));
            }
        });
    }

    private void searchBar(){
        searchBar.setTypeface(AppFont.getRegularFont(context));
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, SearchPage.class));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            MainCategoryAdapter.offlineMainCategories.clear();
            subCategoryID=null;
            oldSubCategoryID=null;
            categoryID=null;
            oldCategoryID=null;
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            checkActive();
        }
    }

    private void checkActive() {
        if(categoryID!=null){
//            if(!categoryID.equals(oldCategoryID)){
                mainCategory();
//            }
        }
        if(subCategoryID!=null){
            if(!subCategoryID.equals(oldSubCategoryID)){
                category(subCategories);
            }
        }
    }

    private void initComponents(){
        title=view.findViewById(R.id.title);
        rec=view.findViewById(R.id.rec);
        catRec=view.findViewById(R.id.catRec);
        title=view.findViewById(R.id.title);
        searchBar=view.findViewById(R.id.searchBar);
        container=view.findViewById(R.id.container);
        errorContainer = view.findViewById(R.id.errorContainer);
        errorTitle = view.findViewById(R.id.errorTitle);
        errorMessage = view.findViewById(R.id.errorMessage);
        errorButton = view.findViewById(R.id.errorButton);
        errorImage = view.findViewById(R.id.errorImage);
    }



    private void mainCategory(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        mainCategoryAdapter=new MainCategoryAdapter(mainCategories,context,rec,getFragmentManager(),active);
        catRec.setAdapter(mainCategoryAdapter);
        catRec.setLayoutManager(linearLayoutManager);
    }

    private void category(ArrayList<Category> categories){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        adapter=new CategoryAdapter(categories,context, getFragmentManager());
        MainCategoryAdapter.offlineMainCategories.add(new OfflineMainCategory(subCategories,mainCategories.get(0).getId()));
        rec.setAdapter(adapter);
        rec.setLayoutManager(linearLayoutManager);
    }

    private void showErrorView(int image, String title, String message, String btn) {
        errorButton.setTypeface(AppFont.getRegularFont(context));
        errorMessage.setTypeface(AppFont.getRegularFont(context));
        errorTitle.setTypeface(AppFont.getSemiBoldFont(context));
        errorContainer.setVisibility(View.VISIBLE);
        errorImage.setImageResource(image);
        errorTitle.setText(title);
        errorMessage.setText(message);
        errorButton.setText(btn);

        errorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorContainer.setVisibility(View.GONE);
                request();
            }
        });
    }
}