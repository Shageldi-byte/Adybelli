package com.adybelli.android.Common;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Fragment.Home;
import com.adybelli.android.Fragment.Products;
import com.adybelli.android.Fragment.Search;
import com.adybelli.android.Object.ConstantResponse;
import com.adybelli.android.Object.ConstantResponseBody;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.itextpdf.text.pdf.parser.Line;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class Utils {
    public static void hideAdd(Fragment fragment, String tagFragmentName, FragmentManager mFragmentManager, int frame) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            try {
                currentFragment.getView().setVisibility(View.GONE);
            } catch (Exception ex){
                ex.printStackTrace();
            }
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            fragmentTransaction.add(frame, fragmentTemp, tagFragmentName);
        } else {
            try {
                fragmentTemp.getView().setVisibility(View.VISIBLE);
            } catch (Exception ex){
                ex.printStackTrace();
            }
                fragmentTransaction.show(fragmentTemp);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
    }

    public static void productFragment(Fragment fragment, String tagFragmentName, FragmentManager mFragmentManager, int frame) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            try {
                currentFragment.getView().setVisibility(View.GONE);
            } catch (Exception ex){
                ex.printStackTrace();
            }
            fragmentTransaction.hide(currentFragment);
        }
        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp != null) {
            try {
                fragmentTemp.getView().setVisibility(View.VISIBLE);
            } catch (Exception ex){
                ex.printStackTrace();
            }
            MainActivity.firstFragment=new Home();
            MainActivity.secondFragment=new Search();
            fragmentTransaction.remove(fragmentTemp);
        }
        fragmentTransaction.add(frame, fragment, tagFragmentName);
        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
    }

    public static void removeShow(Fragment fragment, String tagFragmentName, FragmentManager mFragmentManager, int frame) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.remove(currentFragment);
        }

        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            fragmentTransaction.add(frame, fragmentTemp, tagFragmentName);
        } else {
            try {
                fragmentTemp.getView().setVisibility(View.VISIBLE);
            } catch (Exception ex){
                ex.printStackTrace();
            }
            fragmentTransaction.show(fragmentTemp);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
    }

    public static void reAdd(Fragment fragment, String tagFragmentName, FragmentManager mFragmentManager, int frame) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Fragment currentFragment = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (currentFragment != null) {
            fragmentTransaction.remove(currentFragment);
        }

        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            fragmentTransaction.add(frame, fragmentTemp, tagFragmentName);
        } else {
            fragmentTransaction.show(fragmentTemp);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
    }

    public static void setSharedPreference(Context context, String name, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, MODE_PRIVATE).edit();
        editor.putString(name, value);
        editor.apply();
    }

    public static String getSharedPreference(Context context, String name) {
        SharedPreferences prefs = context.getSharedPreferences(name, MODE_PRIVATE);
        String value = prefs.getString(name, "");
        return value;
    }

    public static String getLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("Settings", MODE_PRIVATE);
        String value = prefs.getString("My_Lang", "tm");
        return value;
    }


    public static String fixNumber(Double number) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(number);
    }

    public static String getStringResource(int id, Context context) {
        return context.getResources().getString(id);
    }

    public static void setLocale(String lang, Context context) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        //saved data to shared preferences
        SharedPreferences.Editor editor = context.getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    // load language saved in shared preferences
    public static void loadLocal(Context context) {
        SharedPreferences share = context.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String languages = share.getString("My_Lang", "");
        Utils.setLocale(languages, context);
    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    public static void bottomSheet(Context context,String lang){
        if (lang.isEmpty())
            lang = "tm";
        Dialog dialog = new Dialog(context);
        final Context contextThemeWrapper = new ContextThemeWrapper(context, R.style.Material);
        LayoutInflater localInflater = LayoutInflater.from(context).cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.constant_page, null, false);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView title2 = dialog.findViewById(R.id.title2);
        ImageView close = dialog.findViewById(R.id.close);
        WebView webView = dialog.findViewById(R.id.webView);
        ProgressBar progressBar = dialog.findViewById(R.id.progress);
        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<ConstantResponse> call = apiInterface.getConstantPage("privacy", lang);
        call.enqueue(new Callback<ConstantResponse>() {
            @Override
            public void onResponse(Call<ConstantResponse> call, Response<ConstantResponse> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    ConstantResponseBody body = response.body().getBody();
                    title2.setText(body.getTitle());
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
                                context.startActivity(i);
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
                progressBar.setVisibility(View.GONE);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.setCancelable(true);
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        dialog.show();

    }


    public static void showCustomToast(String textStr,int imageId,Context context,int backgroundId){
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, null, false);

        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(imageId);
        TextView text = (TextView) layout.findViewById(R.id.text);
        TextView ok = (TextView) layout.findViewById(R.id.ok);
        LinearLayout bg = (LinearLayout) layout.findViewById(R.id.bg);
        bg.setBackgroundResource(backgroundId);
        text.setText(textStr);
        text.setTypeface(AppFont.getRegularFont(context));
        ok.setTypeface(AppFont.getSemiBoldFont(context));

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.TOP|Gravity.FILL_HORIZONTAL, 20, 20);
        toast.setDuration(Toast.LENGTH_LONG);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast.cancel();
            }
        });

        toast.setView(layout);
        toast.show();


    }

    public static KProgressHUD getProgress(Context context){
        return KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setWindowColor(context.getResources().getColor(R.color.transparentBlack))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }


}
