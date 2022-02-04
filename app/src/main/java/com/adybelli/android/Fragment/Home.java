package com.adybelli.android.Fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Activity.ProductPage;
import com.adybelli.android.Activity.SearchPage;
import com.adybelli.android.Activity.SelectGender;
import com.adybelli.android.Adapter.BrandsAdapter;
import com.adybelli.android.Adapter.CarouselAdapter;
import com.adybelli.android.Adapter.PopularCategoriesAdapter;
import com.adybelli.android.Adapter.ProductAdapter;
import com.adybelli.android.Adapter.ShopByGenderAdapter;
import com.adybelli.android.Adapter.TopSoldAdapter;
import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.BuildConfig;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Object.Brands;
import com.adybelli.android.Object.CarouselListObject;
import com.adybelli.android.Object.Constants;
import com.adybelli.android.Object.GetUserById;
import com.adybelli.android.Object.PopularCategories;
import com.adybelli.android.Object.Product;
import com.adybelli.android.Object.ShopByGender;
import com.adybelli.android.Object.TopSold;
import com.adybelli.android.Object.UpdateUserTokenResponse;
import com.adybelli.android.Post.UserUpdateTokenPost;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.bumptech.glide.Glide;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home extends Fragment {
    // creating object of ViewPager
    private ViewPager mViewPager;
    private Handler sliderHandler = new Handler();
    // images array
    private ArrayList<CarouselListObject> carouselListObjects = new ArrayList<>();
    // Creating Object of ViewPagerAdapter
    // Shop by gender
    private TextView shop_by_gender;
    private RecyclerView shop_by_gender_rec;
    private ArrayList<ShopByGender> shopByGenderArrayList = new ArrayList<>();

    private CarouselAdapter mViewPagerAdapter;
    private View view;
    private Context context;
    private LinearLayout indicator;
    private Button oldActiveIndicator;

    //Popular categories
    private RecyclerView popular_categories_rec;
    private TextView popular_categories_title;
    private ArrayList<PopularCategories> popularCategoriesArrayList = new ArrayList<>();

    // Top sellers
    private RecyclerView top_sellers_rec;
    private TextView top_sellers_title;
    private ArrayList<TopSold> topSellersArrayList = new ArrayList<>();


    // Brands
    private TextView brands_title, see_all;
    private RecyclerView brands_rec;
    private ArrayList<Brands> brands = new ArrayList<>();


    // New Arrivals
    private TextView newArrivalsTitle;
    private RecyclerView newArrivalsRec;
    private ArrayList<TopSold> newArrivalsArrayList = new ArrayList<>();
    private boolean isFirstTime = true;
    private int mCurrentPosition = 0, lastPageIndex;


    private int sliderPosition = 0;
    private int SLIDER_INTERVAL = 3000;

    private SkeletonScreen bannerSkeleton, genderSkeleton;

    private ApiInterface apiInterface;

    private LinearLayout errorContainer;
    private ImageView errorImage, firstImg, secondImg;
    private TextView errorTitle, errorMessage;
    private Button errorButton;

    private ScrollView scrollContainer;
    private KProgressHUD kProgressHUD = null;
    private WebView webView1, webView2;
    private String langugae = "tm";
    private ArrayList<Constants> constants = new ArrayList<>();
    private Integer gender = 1;

    public Home() {
    }

    public static Home newInstance() {
        Home fragment = new Home();
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

        view = inflater.inflate(R.layout.fragment_home, container, false);
        context = getContext();
        Utils.loadLocal(context);
        langugae = Utils.getLanguage(context);
        initComponents();
        requestVersion();

        if (Utils.getSharedPreference(context, "gender").isEmpty()) {
            gender = null;
        } else {
            try {
                gender = Integer.parseInt(Utils.getSharedPreference(context, "gender"));
            } catch (Exception ex) {
                ex.printStackTrace();
                gender = null;
            }
        }

        Log.e("Token", Utils.getSharedPreference(context, "tkn"));


        return view;
    }

    private void requestVersion() {
        ApiInterface api = APIClient.getClient().create(ApiInterface.class);
        Call<GetUserById> call = api.getVersionUser("Bearer " + Utils.getSharedPreference(context, "tkn"), "android",Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
        call.enqueue(new Callback<GetUserById>() {
            @Override
            public void onResponse(Call<GetUserById> call, Response<GetUserById> response) {
                if (response.isSuccessful()) {
                    GetUserById ver = response.body();
                    if (ver.isError() || ver.getBody() == null) {
                        logout();
                    }
                } else {
                    logout();
                }
                if (Utils.getSharedPreference(context, "isFirst").isEmpty()) {
                    showGenderDialog();
                    Utils.setSharedPreference(context, "isFirst", "ok");
                } else {
                    requestHome();
                }
            }

            @Override
            public void onFailure(Call<GetUserById> call, Throwable t) {
                hideSplash();
                scrollContainer.setVisibility(View.GONE);
                showErrorView(R.drawable.no_connection, context.getResources().getString(R.string.noInternet), getResources().getString(R.string.checkYourInternet), getResources().getString(R.string.continueValue));
            }
        });
    }

    private void logout() {
        Utils.setSharedPreference(context, "tkn", "");
        Utils.setSharedPreference(context, "userId", "");
    }

    private void sendNotificationToken(){
        if (!Utils.getSharedPreference(context, "tkn").isEmpty() || !Utils.getSharedPreference(context, "userId").isEmpty()) {
            ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);
            UserUpdateTokenPost post = new UserUpdateTokenPost(Utils.getSharedPreference(context, "notif_token"));
            Call<UpdateUserTokenResponse> call = apiInterface.updateUserToken("Bearer " + Utils.getSharedPreference(context, "tkn"), post,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
            call.enqueue(new Callback<UpdateUserTokenResponse>() {
                @Override
                public void onResponse(Call<UpdateUserTokenResponse> call, Response<UpdateUserTokenResponse> response) {

                }

                @Override
                public void onFailure(Call<UpdateUserTokenResponse> call, Throwable t) {

                }
            });
        }
    }

    private void showUpdateDialog() {
        Dialog dialog = new Dialog(context);
        final Context contextThemeWrapper = new ContextThemeWrapper(((Activity) context), R.style.Material);
        LayoutInflater localInflater = LayoutInflater.from(context).cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.update_dialog, null, false);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;

        Button updateButton = dialog.findViewById(R.id.updateButton);
        TextView updateTitle = dialog.findViewById(R.id.updateTitle);

        updateTitle.setTypeface(AppFont.getRegularFont(context));
        updateButton.setTypeface(AppFont.getSemiBoldFont(context));
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        dialog.setCancelable(true);
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


    private void requestHome() {
        apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<com.adybelli.android.Object.Home> call = apiInterface.getHome("Bearer " + Utils.getSharedPreference(context, "tkn"), gender, "android",Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
        call.enqueue(new Callback<com.adybelli.android.Object.Home>() {
            @Override
            public void onResponse(Call<com.adybelli.android.Object.Home> call, Response<com.adybelli.android.Object.Home> response) {
                Log.e("Code", response.code() + "");
                if (response.isSuccessful()) {

                    String displayResponse = "";
                    com.adybelli.android.Object.Home resource = response.body();
                    if (resource.getBody().getRequired_version() != null) {
                        if (!resource.getBody().getRequired_version().equals(BuildConfig.VERSION_NAME)) {
                            showUpdateDialog();
                            return;
                        }
                    }
                    carouselListObjects = (ArrayList<CarouselListObject>) resource.getBody().getBanners();
                    shopByGenderArrayList = (ArrayList<ShopByGender>) resource.getBody().getGenders();
                    popularCategoriesArrayList = (ArrayList<PopularCategories>) resource.getBody().getPopular_categories();
                    topSellersArrayList = (ArrayList<TopSold>) resource.getBody().getTop_sold();
                    brands = (ArrayList<Brands>) resource.getBody().getBrands();
                    newArrivalsArrayList = (ArrayList<TopSold>) resource.getBody().getTop_sold();
                    constants = resource.getBody().getConstants();


                    carousel();
                    shopByGender();
                    popularCategories();
                    topSellers();
                    Accessorie();
                    brands();
                    coupon();
                    newArrivals();

                    errorContainer.setVisibility(View.GONE);

                    if (kProgressHUD != null) {
                        kProgressHUD.dismiss();
                    }

                    hideSplash();
                    brands_title.setText(Utils.getStringResource(R.string.brands, context));
                    see_all.setText(Utils.getStringResource(R.string.all_brands, context));
                    newArrivalsTitle.setText(Utils.getStringResource(R.string.new_arrivals, context));


                } else {
                    if (kProgressHUD != null) {
                        kProgressHUD.dismiss();
                    }
                    hideSplash();
                    scrollContainer.setVisibility(View.GONE);
                    showErrorView(R.drawable.no_connection, context.getResources().getString(R.string.noInternet), getResources().getString(R.string.checkYourInternet), getResources().getString(R.string.continueValue));
                }

                sendNotificationToken();


            }

            @Override
            public void onFailure(Call<com.adybelli.android.Object.Home> call, Throwable t) {
                call.cancel();
                Log.e("Error", t.getMessage());
                if (kProgressHUD != null) {
                    kProgressHUD.dismiss();
                }
                hideSplash();
                scrollContainer.setVisibility(View.GONE);
                showErrorView(R.drawable.no_connection, context.getResources().getString(R.string.noInternet), getResources().getString(R.string.checkYourInternet), getResources().getString(R.string.continueValue));
            }
        });
    }

    private void hideSplash() {
        try {
            RelativeLayout splash = getActivity().findViewById(R.id.splash);
            FrameLayout content = getActivity().findViewById(R.id.content);
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
            splash.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
            bottomNavigationView.setVisibility(View.VISIBLE);
            scrollContainer.setVisibility(View.VISIBLE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//            MainActivity.setBottomNavigationSelectedItem(getActivity(), R.id.search);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showGenderDialog() {
        Dialog dialog = new Dialog(context);
        final Context contextThemeWrapper = new ContextThemeWrapper(((Activity) context), R.style.Material);
        LayoutInflater localInflater = LayoutInflater.from(context).cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.select_gender, null, false);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;

        ImageView close = dialog.findViewById(R.id.close);
        LinearLayout man = dialog.findViewById(R.id.manCon);
        LinearLayout women = dialog.findViewById(R.id.women);
        TextView manTitle = dialog.findViewById(R.id.manTitle);
        TextView womenTitle = dialog.findViewById(R.id.womenTitle);
        TextView title2 = dialog.findViewById(R.id.title2);

        title2.setTypeface(AppFont.getBoldFont(context));
        manTitle.setTypeface(AppFont.getSemiBoldFont(context));
        womenTitle.setTypeface(AppFont.getSemiBoldFont(context));

        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.setSharedPreference(context, "gender", "" + 2);
                gender = 2;
                dialog.dismiss();
            }
        });

        women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.setSharedPreference(context, "gender", "" + 1);
                gender = 1;
                dialog.dismiss();
            }
        });


        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                requestHome();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.setSharedPreference(context, "gender", "" + 2);
                gender = 2;
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void shopByGender() {
        shop_by_gender.setTypeface(AppFont.getBoldFont(context));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        ShopByGenderAdapter adapter = new ShopByGenderAdapter(context, shopByGenderArrayList, getFragmentManager(), getActivity());

        shop_by_gender_rec.setLayoutManager(gridLayoutManager);
        shop_by_gender_rec.setAdapter(adapter);
        shop_by_gender_rec.setNestedScrollingEnabled(false);

        genderSkeleton = Skeleton.bind(shop_by_gender_rec)
                .adapter(adapter)
                .duration(1000)
                .shimmer(true)
                .angle(20)
                .load(R.layout.gender_skeleton)
                .show();

        genderSkeleton.hide();
    }


    private void carousel() {
        lastPageIndex = carouselListObjects.size();
        int pos = 0;
        for (CarouselListObject slider : carouselListObjects) {
            slider.setPosition(pos);
            carouselListObjects.set(pos, slider);
            pos++;
        }
        // create indicator
        for (int i = 0; i < carouselListObjects.size(); i++) {
            Button indicatorButton = new Button(context);
            int backgroundId = R.drawable.slider_inactive;
            LayoutParams params = getInActiveIndicatorParams();
            int position = i;
            if (i == 0) {
                backgroundId = R.drawable.slider_active;
                oldActiveIndicator = indicatorButton;
                params = getActiveIndicatorParams();
            }
            indicatorButton.setBackgroundResource(backgroundId);
            indicatorButton.setVisibility(View.VISIBLE);

            indicatorButton.setLayoutParams(params);
            indicatorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setViewPagerPosition(position);
                }
            });
            indicator.addView(indicatorButton);
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                oldActiveIndicator.setLayoutParams(getInActiveIndicatorParams());
                oldActiveIndicator.setBackgroundResource(R.drawable.slider_inactive);
                oldActiveIndicator = (Button) indicator.getChildAt(carouselListObjects.get(position).getPosition());
                Button indicatorButton = (Button) indicator.getChildAt(carouselListObjects.get(position).getPosition());
                indicatorButton.setLayoutParams(getActiveIndicatorParams());
                indicatorButton.setBackgroundResource(R.drawable.slider_active);
                mCurrentPosition = position;


                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, SLIDER_INTERVAL);
                sliderPosition = position - 1;
                if (position == carouselListObjects.size() - 1)
                    sliderPosition = carouselListObjects.size() - 1;

            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                if (mCurrentPosition == 0)
//                    mViewPager.setCurrentItem(lastPageIndex - 1, true);
//
//                if (mCurrentPosition == lastPageIndex)
//                    mViewPager.setCurrentItem(0, true);
            }
        });


        // Initializing the ViewPagerAdapter
        mViewPagerAdapter = new CarouselAdapter(context, carouselListObjects, getFragmentManager(), getActivity(), mViewPager);
        // Adding the Adapter to the ViewPager
        mViewPager.setAdapter(mViewPagerAdapter);
        sliderHandler.postDelayed(sliderRunnable, SLIDER_INTERVAL);


    }

    private void setViewPagerPosition(int position) {
        mViewPager.setCurrentItem(position, true);
    }

    private LinearLayout.LayoutParams getActiveIndicatorParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins((int) getResources().getDimension(R.dimen.padd_10), 0, 0, 0);
        params.width = (int) getResources().getDimension(R.dimen.indicator_width);
        params.height = (int) getResources().getDimension(R.dimen.indicator_active_height);
        return params;
    }

    private LinearLayout.LayoutParams getInActiveIndicatorParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins((int) getResources().getDimension(R.dimen.padd_10), 0, 0, 0);
        params.width = (int) getResources().getDimension(R.dimen.indicator_width);
        params.height = (int) getResources().getDimension(R.dimen.indicator_passive_height);
        return params;
    }

    @Override
    public void onDestroy() {
        mViewPager.clearOnPageChangeListeners();
        super.onDestroy();

    }

    private void initComponents() {
        mViewPager = view.findViewById(R.id.viewPagerSlider);
        indicator = view.findViewById(R.id.indicator);
        shop_by_gender = view.findViewById(R.id.shop_by_gender);
        shop_by_gender_rec = view.findViewById(R.id.shop_by_gender_rec);
        popular_categories_title = view.findViewById(R.id.popular_categories_title);
        popular_categories_rec = view.findViewById(R.id.popular_categories_rec);
        top_sellers_rec = view.findViewById(R.id.top_sellers_rec);
        top_sellers_title = view.findViewById(R.id.top_sellers_title);
        brands_title = view.findViewById(R.id.brands_title);
        brands_rec = view.findViewById(R.id.brands_rec);
        newArrivalsRec = view.findViewById(R.id.newArrivalsRec);
        newArrivalsTitle = view.findViewById(R.id.newArrivalsTitle);
        errorContainer = view.findViewById(R.id.errorContainer);
        errorTitle = view.findViewById(R.id.errorTitle);
        errorMessage = view.findViewById(R.id.errorMessage);
        errorButton = view.findViewById(R.id.errorButton);
        errorImage = view.findViewById(R.id.errorImage);
        scrollContainer = view.findViewById(R.id.scrollContainer);
//        scrollContainer.fullScroll(View.FOCUS_UP);
        webView1 = view.findViewById(R.id.webView1);
        webView2 = view.findViewById(R.id.webView2);
        see_all = view.findViewById(R.id.see_all);
        firstImg = view.findViewById(R.id.firstImg);
        secondImg = view.findViewById(R.id.secondImg);


        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setWindowColor(getResources().getColor(R.color.transparentBlack))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        view.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SearchPage.class);
                context.startActivity(intent);
            }
        });

        see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllBrands allBrands = new AllBrands();

                Utils.hideAdd(allBrands, AllBrands.class.getSimpleName(), getFragmentManager(), R.id.content);
                MainActivity.firstFragment = allBrands;
            }
        });
    }

    private void popularCategories() {
        popular_categories_title.setTypeface(AppFont.getBoldFont(context));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        PopularCategoriesAdapter adapter = new PopularCategoriesAdapter(context, popularCategoriesArrayList, getFragmentManager(), getActivity());
        popular_categories_rec.setAdapter(adapter);
        popular_categories_rec.setLayoutManager(linearLayoutManager);

    }

    private void topSellers() {
        top_sellers_title.setTypeface(AppFont.getBoldFont(context));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        TopSoldAdapter adapter = new TopSoldAdapter(context, topSellersArrayList, false);
        top_sellers_rec.setLayoutManager(linearLayoutManager);
        top_sellers_rec.setAdapter(adapter);
    }

    private void Accessorie() {

        if (langugae.equals("tm") || langugae.isEmpty()) {
            loadHtml(firstImg, constants.get(0).getImg(), constants.get(0).getLink(), constants.get(0).getPage());
        }
        if (langugae.equals("ru")) {
            loadHtml(firstImg, constants.get(1).getImg(), constants.get(1).getLink(), constants.get(1).getPage());
        }
    }

    private void brands() {
        brands_title.setTypeface(AppFont.getBoldFont(context));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        brands_rec.setLayoutManager(linearLayoutManager);
        BrandsAdapter brandsAdapter = new BrandsAdapter(context, brands, getFragmentManager(), getActivity());
        brands_rec.setAdapter(brandsAdapter);
    }

    private void coupon() {
        if (langugae.equals("tm") || langugae.isEmpty()) {
            loadHtml(secondImg, constants.get(2).getImg(), constants.get(2).getLink(), constants.get(2).getPage());
        }
        if (langugae.equals("ru")) {
            loadHtml(secondImg, constants.get(3).getImg(), constants.get(3).getLink(), constants.get(3).getPage());
        }
    }


    private void loadHtml(ImageView iv, String img, String link, String pageTitle) {


        Glide.with(context)
                .load(img)
                .placeholder(R.drawable.placeholder)
                .into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse(link);
                String page = "";
                String tm_id_list = uri.getQueryParameter("trademarks");
                String cat_id_list = uri.getQueryParameter("categories");
                String color_id_list = uri.getQueryParameter("colors");
                String size_id_list = uri.getQueryParameter("sizes");
                Double min = null;
                Double max = null;
                if (uri.getQueryParameter("min") != null)
                    min = Double.valueOf(uri.getQueryParameter("min"));
                if (uri.getQueryParameter("max") != null)
                    max = Double.valueOf(uri.getQueryParameter("max"));
                int isDiscount = 0;
                if (uri.getQueryParameter("discount") != null)
                    isDiscount = Integer.parseInt(uri.getQueryParameter("discount"));
                Products products = new Products();
                Products.brandsStr.clear();
                Products.categoriesStr.clear();
                Products.colorsStr.clear();
                Products.sizesStr.clear();

                Products.isDiscount = isDiscount;

                if (cat_id_list != null && cat_id_list.length() > 0) {
                    String[] separated = cat_id_list.split(",");
                    Products.categoriesStr.addAll(Arrays.asList(separated));
                }

                if (tm_id_list != null && tm_id_list.length() > 0) {
                    String[] separated = tm_id_list.split(",");
                    Products.brandsStr.addAll(Arrays.asList(separated));
                }

                if (color_id_list != null && color_id_list.length() > 0) {
                    String[] separated = color_id_list.split(",");
                    Products.colorsStr.addAll(Arrays.asList(separated));
                }

                if (size_id_list != null && size_id_list.length() > 0) {
                    String[] separated = size_id_list.split(",");
                    Products.sizesStr.addAll(Arrays.asList(separated));
                }
                if (min != null)
                    Products.min = min;
                if (max != null)
                    Products.max = max;
                String id = "";
                if (uri.getPathSegments().size() > 1)
                    page = uri.getPathSegments().get(0);
                if (uri.getPathSegments().size() >= 2) {
                    if (page.equals("products")) {
                        id = uri.getPathSegments().get(1);
                    }
                    if (!id.isEmpty())
                        Products.categoriesStr.add(id);
                }

                products.catName = pageTitle;
                products.type = 3;
                Products.where = "home";
                Utils.productFragment(products, Products.class.getSimpleName(), getFragmentManager(), R.id.content);
                MainActivity.firstFragment = products;
            }
        });

    }


    private void loadHtml(WebView wv, String htmlCode) {
        wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        wv.getSettings().setBuiltInZoomControls(false);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setLoadsImagesAutomatically(true);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
                return true;
            }
        });
        wv.getSettings().setUseWideViewPort(true);
        wv.setWebChromeClient(new WebChromeClient());
        wv.loadDataWithBaseURL(null, htmlCode, null, "UTF-8", null);
    }

    private void newArrivals() {
        newArrivalsTitle.setTypeface(AppFont.getBoldFont(context));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        TopSoldAdapter adapter = new TopSoldAdapter(context, newArrivalsArrayList, false);
        newArrivalsRec.setAdapter(adapter);
        newArrivalsRec.setLayoutManager(linearLayoutManager);

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
                kProgressHUD.show();
                requestVersion();
            }
        });
    }


    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
//            if (mViewPager.getCurrentItem() == carouselListObjects.size() - 1) {
//                mViewPager.setCurrentItem(0, true);
//                sliderPosition = 0;
//            } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            sliderPosition++;
//            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, SLIDER_INTERVAL);
        webView2.onResume();
        webView1.onResume();

    }
}