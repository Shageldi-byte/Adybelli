package com.adybelli.android.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Object.ConstantResponse;
import com.adybelli.android.Object.ConstantResponseBody;
import com.adybelli.android.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ConstantPage extends DialogFragment {

    private Context context;
    private View view;
    private ApiInterface apiInterface;
    private String page,lang="tm";
    private TextView back;
    private WebView webView;
    private ProgressBar progress;
    public ConstantPage(String page) {
        this.page=page;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_constant_page, container, false);
        context=getContext();
        initComponents();
        lang=Utils.getLanguage(context);
        if(lang.isEmpty()){
            lang="tm";
        }
        request();

        return view;
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

                    webView.setWebViewClient(new WebViewClient(){
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            if(url!=null) {
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                            return true;
                        }
                    });

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
        back=view.findViewById(R.id.back);
        webView=view.findViewById(R.id.webView);
        progress=view.findViewById(R.id.progress);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.removeShow(new ProfileRootFragment(), ProfileRootFragment.class.getSimpleName(), getFragmentManager(), R.id.content);
                MainActivity.fifthFragment = new ProfileRootFragment();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}