package com.adybelli.android.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.adybelli.android.Common.Utils;
import com.adybelli.android.Fragment.Products;
import com.adybelli.android.Fragment.Store.StoreProducts;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.adybelli.android.databinding.ActivityStorePageBinding;

public class StorePage extends AppCompatActivity {
    private ActivityStorePageBinding binding;
    public String storeName="Adybelli";
    public String storeId="1";
    public static Boolean isVisible=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
        isVisible=true;
        binding=ActivityStorePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setFonts();
        setListener();
        setProducts();
    }

    private void setProducts() {
        StoreProducts products = new StoreProducts();
        products.catId = storeId;
        products.catName = storeName;
        Utils.productFragment(products, StoreProducts.class.getSimpleName(), getSupportFragmentManager(), R.id.content);
    }

    private void setListener() {
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Create an ACTION_SEND Intent*/
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                /*This will be the actual content you wish you share.*/
                String shareBody = "www.adybelliandroid.com/store?store_id="+storeId;
                /*The type of the content is text, obviously.*/
                intent.setType("text/plain");
                /*Applying information Subject and Body.*/
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, storeName);
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                /*Fire!*/
                startActivity(intent);
            }
        });
    }

    private void setFonts() {
        binding.storeName.setTypeface(AppFont.getSemiBoldFont(this));
        TextView searchText = binding.searchView.findViewById(R.id.search_src_text);
        searchText.setTypeface(AppFont.getRegularFont(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isVisible=false;
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null){
            String recipeId = appLinkData.getQueryParameter("store_id");

        }
    }
}