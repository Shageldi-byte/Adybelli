package com.adybelli.android.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adybelli.android.Adapter.AllBrandsAdapter;
import com.adybelli.android.Adapter.ProductAdapter;
import com.adybelli.android.Adapter.SearchHistoryAdapter;
import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Util;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.DataBaseHelper.SearchDB;
import com.adybelli.android.Object.AllBrandsResponse;
import com.adybelli.android.Object.GetProducts;
import com.adybelli.android.Object.Product;
import com.adybelli.android.Post.SearchPost;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPage extends AppCompatActivity {
    private ApiInterface apiInterface;
    private ImageView back;
    private EditText autotext;
    private ImageButton searchBtn;
    private LinearLayout historyContainer, resultCon;
    private TextView recent, clear;
    private RecyclerView searchHistoryRec, resultsRec;
    private SearchDB searchDB;
    private Context context = this;
    private ArrayList<String> searchList = new ArrayList<>();
    private ArrayList<Product> products = new ArrayList<>();
    private LinearProgressIndicator progress;
    private ProgressBar loadingP;

    public int count = 1;
    private boolean loading = true;
    private LinearLayout errorContainer;
    private ImageView errorImage,clearAndClose;
    private TextView errorTitle, errorMessage;
    private Button errorButton;
    private ProductAdapter adapter;
    private boolean isLoading=false;
    private Integer oldLength=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        initComponents();
        autotext.requestFocus();
        searchDB = new SearchDB(context);
        getSearchHistory();
        setListener();
    }

    private void setListener() {
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSearch();

            }
        });

        clearAndClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autotext.clearFocus();
                autotext.setText("");
                clearAndClose.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(autotext.getWindowToken(), 0);
            }
        });

        autotext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAndClose.setVisibility(View.VISIBLE);
            }
        });

        autotext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    clearAndClose.setVisibility(View.VISIBLE);
                    errorContainer.setVisibility(View.GONE);
                } else{
                    clearAndClose.setVisibility(View.GONE);
                }
            }
        });

        autotext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
        autotext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(String.valueOf(charSequence).length()>oldLength+1){
//                    performSearch();
//                }
//                oldLength=String.valueOf(charSequence).length();
                getSearchHistory();
                if(!String.valueOf(charSequence).isEmpty()){
                    clearAndClose.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDB.truncate();
                getSearchHistory();
            }
        });

        resultsRec.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void loadMore(){
        if (loading) {
            isLoading=true;
            loadingP.setVisibility(View.VISIBLE);
            count++;
            request(count);
        }
    }

    private void performSearch() {
        if(autotext.getText().toString().trim().isEmpty()){
            return;
        }
        autotext.clearFocus();
        clearAndClose.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(autotext.getWindowToken(), 0);
        isLoading=false;
        loading=true;
        Cursor cursor = searchDB.getSelect(autotext.getText().toString());
        if (cursor.getCount() == 0) {
            searchDB.insert(autotext.getText().toString());
        }
        searchHistoryRec.setVisibility(View.GONE);
        recent.setVisibility(View.GONE);
        clear.setVisibility(View.GONE);
        resultsRec.setVisibility(View.VISIBLE);
        request(1);
    }

    private void getSearchHistory() {
        searchList.clear();
        searchHistoryRec.setVisibility(View.VISIBLE);
        recent.setVisibility(View.VISIBLE);
        clear.setVisibility(View.VISIBLE);
        resultsRec.setVisibility(View.GONE);
        Cursor cursor;
        if (autotext.getText().toString().trim().isEmpty()) {
            cursor = searchDB.getAll();
        } else {
            cursor = searchDB.getSelect(autotext.getText().toString());
        }

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                searchList.add(cursor.getString(1));
            }
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        SearchHistoryAdapter adapter = new SearchHistoryAdapter(searchList, context, autotext);
        searchHistoryRec.setLayoutManager(linearLayoutManager);
        searchHistoryRec.setAdapter(adapter);

    }

    private void initComponents() {
        back = findViewById(R.id.back);
        autotext = findViewById(R.id.autotext);
        searchBtn = findViewById(R.id.searchBtn);
        historyContainer = findViewById(R.id.historyContainer);
        resultCon = findViewById(R.id.resultCon);
        recent = findViewById(R.id.recent);
        clear = findViewById(R.id.clear);
        searchHistoryRec = findViewById(R.id.searchHistoryRec);
        resultsRec = findViewById(R.id.resultsRec);
        progress = findViewById(R.id.progress);
        errorContainer = findViewById(R.id.errorContainer);
        errorTitle = findViewById(R.id.errorTitle);
        errorMessage = findViewById(R.id.errorMessage);
        errorButton = findViewById(R.id.errorButton);
        errorImage = findViewById(R.id.errorImage);
        loadingP = findViewById(R.id.loading);
        clearAndClose = findViewById(R.id.clearAndClose);

        autotext.setTypeface(AppFont.getRegularFont(context));
    }

    public void request(int page) {
        count = page;
        errorContainer.setVisibility(View.GONE);
        if (page == 1) {
            loadingP.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
            products.clear();

        }

        apiInterface = APIClient.getClient().create(ApiInterface.class);
        SearchPost post = new SearchPost(page, 20, autotext.getText().toString());
        Call<GetProducts> call = apiInterface.getSearch("Bearer " + Utils.getSharedPreference(context, "tkn"), post);
        call.enqueue(new Callback<GetProducts>() {
            @Override
            public void onResponse(Call<GetProducts> call, Response<GetProducts> response) {
                Log.e("Code", response.code() + "");
                if (response.isSuccessful()) {
                    loadingP.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                    GetProducts getProducts = response.body();
                    products.addAll(getProducts.getBody());
                    if (getProducts.getBody().size() <= 0) {
                        loading = false;
                        count--;
                    }

                    if (page == 1) {
                        results();
                    } else {
                        resultsRec.getAdapter().notifyDataSetChanged();
                    }
                    if (products.size() <= 0)
                        showErrorView(R.drawable.not_found, context.getResources().getString(R.string.no_data), getResources().getString(R.string.empty), getResources().getString(R.string.continueValue));

                }
                progress.setVisibility(View.GONE);
                loadingP.setVisibility(View.GONE);
                isLoading=false;

            }

            @Override
            public void onFailure(Call<GetProducts> call, Throwable t) {
                count--;
                loadingP.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                isLoading=false;
                if (products.size() == 0)
                    showErrorView(R.drawable.no_connection, context.getResources().getString(R.string.noInternet), getResources().getString(R.string.checkYourInternet), getResources().getString(R.string.continueValue));
            }
        });

    }

    private void results() {
        GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 2);
        adapter = new ProductAdapter(context, products,false);
        resultsRec.setLayoutManager(linearLayoutManager);
        resultsRec.setAdapter(adapter);
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
                autotext.requestFocus();
                autotext.setText("");
                errorContainer.setVisibility(View.GONE);

            }
        });
    }
}