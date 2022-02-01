package com.adybelli.android.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Fragment.ProfileRootFragment;
import com.adybelli.android.Object.ConstantResponse;
import com.adybelli.android.Object.ConstantResponseBody;
import com.adybelli.android.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebViewPage extends AppCompatActivity {
    private Context context=this;
    private View view;
    private ApiInterface apiInterface;
    private String page,lang="tm";
    private TextView back;
    private WebView webView;
    private ProgressBar progress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_page);
        page=getIntent().getStringExtra("page");
        initComponents();
        lang=Utils.getLanguage(context);
        if(lang.isEmpty()){
            lang="tm";
        }
        request();
    }

    private void request() {
        apiInterface= APIClient.getClient().create(ApiInterface.class);
        Call<ConstantResponse> call=apiInterface.getConstantPage(page,lang);
        call.enqueue(new Callback<ConstantResponse>() {
            @Override
            public void onResponse(Call<ConstantResponse> call, Response<ConstantResponse> response) {
                if(response.isSuccessful()){
                    progress.setVisibility(View.GONE);
                    ConstantResponseBody body=response.body().getBody();
                    back.setText(body.getTitle());
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                    webView.getSettings().setBuiltInZoomControls(false);
                    webView.getSettings().setLoadWithOverviewMode(true);
                    webView.getSettings().setLoadsImagesAutomatically(true);

                    webView.getSettings().setUseWideViewPort(true);
                    webView.setWebChromeClient(new WebChromeClient());
                    webView.loadDataWithBaseURL(null, body.getContent(), null, "UTF-8", null);
                }
            }

            @Override
            public void onFailure(Call<ConstantResponse> call, Throwable t) {

            }
        });
    }

    private void initComponents() {
        back=findViewById(R.id.back);
        webView=findViewById(R.id.webView);
        progress=findViewById(R.id.progress);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}