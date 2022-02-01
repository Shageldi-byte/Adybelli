package com.adybelli.android.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Activity.SearchPage;
import com.adybelli.android.Adapter.FavAdapter;
import com.adybelli.android.Adapter.ProductAdapter;
import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Util;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Object.Favs;
import com.adybelli.android.Object.GetFavs;
import com.adybelli.android.Object.GetProducts;
import com.adybelli.android.Object.Product;
import com.adybelli.android.Post.ProductsPost;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen;
import com.ethanhua.skeleton.Skeleton;
import com.github.yasevich.endlessrecyclerview.EndlessRecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Favourite extends Fragment {
    private TextView title;
    private View view;
    private Context context;
    private ArrayList<Favs> products = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;
    private LinearLayout errorContainer;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button errorButton;
    private FavAdapter adapter;
    private ApiInterface apiInterface;
    private ProgressBar progress;
    public Integer count = 1;
    private boolean loading = true;
    private RecyclerViewSkeletonScreen skeletonScreen;
    private SwipeRefreshLayout refresh;
    public static boolean isChanged = false;
    private boolean isFirstTime = true;
    private boolean shouldLoadMore = true;
    private RecyclerView list;
    private RecyclerView extraRec;
    private boolean isLoading = false;

    public Favourite() {
    }

    public static Favourite newInstance() {
        Favourite fragment = new Favourite();
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
        context = getContext();
        Utils.loadLocal(context);
        view = inflater.inflate(R.layout.fragment_favourite, container, false);
        initComponents();
        setFonts();
        request(null, true);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            loading = true;
            isChanged = false;
            request(null, false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (!isFirstTime) {
//            loading = true;
//            isChanged = false;
//            request(null, false);
//        } else {
//            isFirstTime = false;
//        }

    }

    private void initComponents() {
        list = view.findViewById(R.id.rec);
        refresh = view.findViewById(R.id.refresh);
        progress = view.findViewById(R.id.progress);
        errorContainer = view.findViewById(R.id.errorContainer);
        errorTitle = view.findViewById(R.id.errorTitle);
        errorMessage = view.findViewById(R.id.errorMessage);
        errorButton = view.findViewById(R.id.errorButton);
        errorImage = view.findViewById(R.id.errorImage);
        title = view.findViewById(R.id.title);
        extraRec = view.findViewById(R.id.extraRec);
        view.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SearchPage.class);
                context.startActivity(intent);
            }
        });

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loading = true;
                request(null, true);
            }
        });

        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if (!isLoading)
                        loadMore();

                }
            }
        });


    }

    private void loadMore() {
        if (loading) {
            isLoading=true;
            try {
                progress.setVisibility(View.VISIBLE);
                count = products.get(products.size() - 1).getFav_id();
                request(count, false);
            } catch (Exception ex) {
                ex.printStackTrace();
                isLoading=false;
            }
        }
    }

    public void request(Integer page, boolean isLoading) {
        String tkn = Utils.getSharedPreference(context, "tkn");
        if (tkn.isEmpty()) {
            showErrorView(R.drawable.fav_empty, context.getResources().getString(R.string.no_login), getResources().getString(R.string.must_login), getResources().getString(R.string.continueValue),false,context,view);
            products.clear();
            adapter = new FavAdapter(context, products,view);
            list.setAdapter(adapter);
            return;
        }
        count = page;
        errorContainer.setVisibility(View.GONE);
        if (page == null && isLoading) {
            GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 2);
            adapter = new FavAdapter(context, products,view);
            extraRec.setLayoutManager(linearLayoutManager);
            skeletonScreen = Skeleton.bind(extraRec)
                    .adapter(adapter)
                    .color(R.color.skeletonColor)
                    .load(R.layout.product_skelton)
                    .count(8)
                    .show();

        }
        if (page == null || page==0) {
            products.clear();
        }

        shouldLoadMore = false;


        apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<GetFavs> call = apiInterface.getFavourites("Bearer " + Utils.getSharedPreference(context, "tkn"), page, 20);
        call.enqueue(new Callback<GetFavs>() {
            @Override
            public void onResponse(Call<GetFavs> call, Response<GetFavs> response) {
                Log.e("Code", response.message() + "");
                if (response.isSuccessful()) {
                    progress.setVisibility(View.GONE);
                    GetFavs getProducts = response.body();
                    products.addAll((ArrayList<Favs>) getProducts.getBody());
                    errorContainer.setVisibility(View.GONE);
                    if (getProducts.getBody().size() <= 0) {
                        loading = false;
//                        count--;
                    }

                    if (page == null) {
                        products();
                    } else {
                        list.getAdapter().notifyDataSetChanged();
                    }
                    if (products.size() <= 0)
                        showErrorView(R.drawable.fav_empty, context.getResources().getString(R.string.no_fav), getResources().getString(R.string.to_fav), getResources().getString(R.string.continueValue),false,context,view);

                    try {

                        skeletonScreen.hide();
                        extraRec.setVisibility(View.GONE);
                        refresh.setRefreshing(false);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    setDefault();


                } else {
                    if (products.size() == 0) {
                        refresh.setRefreshing(false);
                        progress.setVisibility(View.GONE);
                        skeletonScreen.hide();
                        extraRec.setVisibility(View.GONE);
                        showErrorView(R.drawable.fav_empty, context.getResources().getString(R.string.noInternet), getResources().getString(R.string.checkYourInternet), getResources().getString(R.string.continueValue),false,context,view);
                    }
                }


            }

            @Override
            public void onFailure(Call<GetFavs> call, Throwable t) {
                refresh.setRefreshing(false);
                progress.setVisibility(View.GONE);
                t.printStackTrace();
                skeletonScreen.hide();
                extraRec.setVisibility(View.GONE);
                if (products.size() == 0) {
                    showErrorView(R.drawable.no_connection, context.getResources().getString(R.string.noInternet), getResources().getString(R.string.checkYourInternet), getResources().getString(R.string.continueValue),true,context,view);
                }
                setDefault();
            }
        });

    }


    private void setFonts() {
        title.setTypeface(AppFont.getBoldFont(context));
    }

    private void products() {
        GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 2);
        adapter = new FavAdapter(context, products,view);
        list.setLayoutManager(linearLayoutManager);
        list.setAdapter(adapter);
    }

    public void showErrorView(int image, String title, String message, String btn,boolean isRetry,Context context,View view) {
        LinearLayout errorContainer;
        ImageView errorImage;
        TextView errorTitle, errorMessage;
        Button errorButton;

        errorContainer = view.findViewById(R.id.errorContainer);
        errorTitle = view.findViewById(R.id.errorTitle);
        errorMessage = view.findViewById(R.id.errorMessage);
        errorButton = view.findViewById(R.id.errorButton);
        errorImage = view.findViewById(R.id.errorImage);

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
                String tkn = Utils.getSharedPreference(context, "tkn");
                if (tkn.trim().isEmpty()) {
                    MainActivity.setBottomNavigationSelectedItem(getActivity(), R.id.profile);
                    return;
                }

                if(isRetry) {
                    count = null;
                    request(count, true);
                } else
                    MainActivity.setBottomNavigationSelectedItem(getActivity(), R.id.search);
            }
        });
    }


    private void setDefault() {
//        list.setRefreshing(true);
        progress.setVisibility(View.GONE);
        shouldLoadMore = true;
        isLoading=false;
    }


}