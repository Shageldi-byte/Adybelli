package com.adybelli.android.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Adapter.AllBrandsAdapter;
import com.adybelli.android.Adapter.AllSellerAdapter;
import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Object.AllBrandsResponse;
import com.adybelli.android.Object.Store;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.github.yasevich.endlessrecyclerview.EndlessRecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllStores extends Fragment implements EndlessRecyclerView.Pager {

    private EditText editText;
    private Context context;
    private View view;
    public int count = 1;
    private boolean loading = true;
    private LinearLayout errorContainer;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button errorButton;
    private AllSellerAdapter adapter;
    private ArrayList<Store> arrayList = new ArrayList<>();
    private EndlessRecyclerView list;
    private ApiInterface apiInterface;
    private ProgressBar progress,firstProgress;
    private boolean shouldLoadMore = true;
    private TextView title;

    public AllStores() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_stores, container, false);
        context = getContext();
        initComponents();
        setListener();
        setBrands();
        request(1);

        return view;
    }

    private void initComponents() {
        editText = view.findViewById(R.id.searchBar);
        progress = view.findViewById(R.id.progress);
        firstProgress = view.findViewById(R.id.firstProgress);
        errorContainer = view.findViewById(R.id.errorContainer);
        errorTitle = view.findViewById(R.id.errorTitle);
        errorMessage = view.findViewById(R.id.errorMessage);
        errorButton = view.findViewById(R.id.errorButton);
        errorImage = view.findViewById(R.id.errorImage);
        list = view.findViewById(R.id.rec);
        title = view.findViewById(R.id.title);
    }

    private void setListener() {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    return true;
                }
                return false;
            }
        });

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.removeShow(new Home(), Home.class.getSimpleName(), getFragmentManager(), R.id.content);
                MainActivity.firstFragment = new Home();

            }
        });


    }

    public void request(int page) {
//        count = page;
//        errorContainer.setVisibility(View.GONE);
//        LayoutParams lm= (LayoutParams) progress.getLayoutParams();
//        progress.setVisibility(View.VISIBLE);
//        firstProgress.setVisibility(View.GONE);
//        if (page == 1) {
//            GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 4);
//            adapter = new AllBrandsAdapter(context, allBrands, getFragmentManager(), getActivity());
//            list.setLayoutManager(linearLayoutManager);
//            firstProgress.setVisibility(View.VISIBLE);
//            progress.setVisibility(View.GONE);
//        }
//        shouldLoadMore = false;

//        apiInterface = APIClient.getClient().create(ApiInterface.class);
//        Call<AllBrandsResponse> call = apiInterface.getAllBrands(page, 60,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
//        call.enqueue(new Callback<AllBrandsResponse>() {
//            @Override
//            public void onResponse(Call<AllBrandsResponse> call, Response<AllBrandsResponse> response) {
//                Log.e("Code", response.code() + "");
//                if (response.isSuccessful()) {
//                    progress.setVisibility(View.GONE);
//                    firstProgress.setVisibility(View.GONE);
//                    AllBrandsResponse getProducts = response.body();
//                    allBrands.addAll(getProducts.getBody());
//                    if (getProducts.getBody().size() <= 0) {
//                        loading = false;
//                        count--;
//                    }
//
//                    if (page == 1) {
//                        setBrands();
//                    } else {
//                        list.getAdapter().notifyDataSetChanged();
//                    }
//                    if (allBrands.size() <= 0)
//                        showErrorView(R.drawable.no_connection, context.getResources().getString(R.string.no_data), getResources().getString(R.string.empty), getResources().getString(R.string.continueValue));
//
//                }
//                setDefault();
//
//            }
//
//            @Override
//            public void onFailure(Call<AllBrandsResponse> call, Throwable t) {
//                try {
//                    count--;
//                    progress.setVisibility(View.GONE);
//                    firstProgress.setVisibility(View.GONE);
//                    setDefault();
//                    showErrorView(R.drawable.no_connection, context.getResources().getString(R.string.noInternet), getResources().getString(R.string.checkYourInternet), getResources().getString(R.string.continueValue));
//                } catch (Exception ex){
//                    ex.printStackTrace();
//                }
//            }
//        });

    }

    private void setBrands() {
        arrayList.clear();
        arrayList.add(new Store("Adybelli","1"));
        arrayList.add(new Store("Store-1","2"));
        arrayList.add(new Store("Store-2","3"));
        arrayList.add(new Store("Store-3","4"));
        arrayList.add(new Store("Store-4","5"));
        arrayList.add(new Store("Store-5","6"));
        arrayList.add(new Store("Store-6","7"));
        arrayList.add(new Store("Store-7","8"));
        arrayList.add(new Store("Store-8","9"));
        GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 3);
        adapter = new AllSellerAdapter(context, arrayList, getFragmentManager(), getActivity());
        list.setLayoutManager(linearLayoutManager);
        list.setAdapter(adapter);
        list.setPager(this);
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
        shouldLoadMore = true;
    }

    @Override
    public boolean shouldLoad() {
        return shouldLoadMore;
    }

    @Override
    public void loadNextPage() {
        if (loading) {
            progress.setVisibility(View.VISIBLE);
            count++;
            request(count);
        }
    }
}