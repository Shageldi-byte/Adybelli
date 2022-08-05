package com.adybelli.android.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.adybelli.android.Adapter.OrderAdapter;
import com.adybelli.android.Adapter.ProductAdapter;
import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Util;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Object.GetProducts;
import com.adybelli.android.Object.GetUserOrdersResponse;
import com.adybelli.android.Object.GetUserOrdersResponseBody;
import com.adybelli.android.Object.Order;
import com.adybelli.android.Object.Product;
import com.adybelli.android.Post.ProductsPost;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.github.yasevich.endlessrecyclerview.EndlessRecyclerView;
import com.google.firebase.inappmessaging.internal.ApiClient;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Orders extends Fragment implements EndlessRecyclerView.Pager {


    private View view;
    private Context context;
    private ArrayList<GetUserOrdersResponseBody> orders=new ArrayList<>();
    private TextView back;
    private ApiInterface apiInterface;
    public int count = 1;
    private boolean loading = true;
    private LinearLayout errorContainer;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button errorButton;
    private SkeletonScreen skeletonScreen;
    private ProgressBar progress;
    private OrderAdapter adapter;
    private EndlessRecyclerView list;
    private boolean shouldLoadMore=true;
    private RecyclerView extraRec;
    public Orders() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_orders, container, false);
        context=getContext();
        initComponents();
        setListener();
        request(1);

        return view;
    }



    public void request(int page) {
        count = page;
        errorContainer.setVisibility(View.GONE);
        if (page == 1) {
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
            adapter = new OrderAdapter(orders,context,getParentFragmentManager());
            extraRec.setLayoutManager(linearLayoutManager);
            skeletonScreen = Skeleton.bind(extraRec)
                    .adapter(adapter)
                    .color(R.color.skeletonColor)
                    .load(R.layout.skeleton_order)
                    .count(2)
                    .show();
            orders.clear();
        }

        shouldLoadMore=false;
        apiInterface= APIClient.getClient().create(ApiInterface.class);
        Call<GetUserOrdersResponse> call=apiInterface.getUserOrders("Bearer "+Utils.getSharedPreference(context,"tkn"),count,20,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
        call.enqueue(new Callback<GetUserOrdersResponse>() {
            @Override
            public void onResponse(Call<GetUserOrdersResponse> call, Response<GetUserOrdersResponse> response) {
                Log.e("Code", response.code() + "");
                if (response.isSuccessful()) {
                    progress.setVisibility(View.GONE);
                    GetUserOrdersResponse getProducts = response.body();
                    orders.addAll(getProducts.getBody());
                    if (getProducts.getBody().size() <= 0) {
                        loading = false;
                        count--;
                    }

                    if (count == 1) {
                        setOrders();



                    } else {
                        try{
                            list.getAdapter().notifyDataSetChanged();
                        } catch (Exception ex){
                            ex.printStackTrace();
                            setOrders();
                        }

                    }
                    if (orders.size() <= 0)
                        showErrorView(R.drawable.not_found, context.getResources().getString(R.string.no_data), getResources().getString(R.string.no_orders), getResources().getString(R.string.continueValue));


                    skeletonScreen.hide();
                    extraRec.setVisibility(View.GONE);
                    setDefault();


                }

            }

            @Override
            public void onFailure(Call<GetUserOrdersResponse> call, Throwable t) {
                count--;
                progress.setVisibility(View.GONE);
                skeletonScreen.hide();
                extraRec.setVisibility(View.GONE);
                setDefault();
                if (orders.size() == 0)
                showErrorView(R.drawable.no_connection, context.getResources().getString(R.string.noInternet), getResources().getString(R.string.checkYourInternet), getResources().getString(R.string.continueValue));
            }
        });

    }

    private void initComponents(){
        list=view.findViewById(R.id.list);
        back=view.findViewById(R.id.back);
        progress = view.findViewById(R.id.progress);
        errorContainer = view.findViewById(R.id.errorContainer);
        errorTitle = view.findViewById(R.id.errorTitle);
        errorMessage = view.findViewById(R.id.errorMessage);
        errorButton = view.findViewById(R.id.errorButton);
        errorImage = view.findViewById(R.id.errorImage);
        extraRec = view.findViewById(R.id.extraRec);
        back.setTypeface(AppFont.getBoldFont(context));
    }

    private void setOrders(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        adapter=new OrderAdapter(orders,context,getParentFragmentManager());
        list.setLayoutManager(linearLayoutManager);
        list.setAdapter(adapter);
        list.setPager(this);
    }

    private void setListener(){
        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.removeShow(new ProfileRootFragment(), ProfileRootFragment.class.getSimpleName(), getFragmentManager(), R.id.content);
                MainActivity.fifthFragment = new ProfileRootFragment();
            }
        });


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
                count = 1;
                request(count);

            }
        });
    }

    private void setDefault() {
        list.setRefreshing(false);
        progress.setVisibility(View.GONE);
        shouldLoadMore=true;
    }

    @Override
    public boolean shouldLoad() {
        return shouldLoadMore;
    }

    @Override
    public void loadNextPage() {
        loadMore();
    }

    private void loadMore() {
        if (loading) {
            progress.setVisibility(View.VISIBLE);
            count++;
            request(count);
        }
    }
}