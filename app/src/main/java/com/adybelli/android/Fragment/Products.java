package com.adybelli.android.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Activity.SearchPage;
import com.adybelli.android.Adapter.ProductAdapter;
import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.CategoryModel.Color;
import com.adybelli.android.Common.Util;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Fragment.NavbarFragment.FilterList;
import com.adybelli.android.LayoutManager.AutoFitGridLayoutManager;
import com.adybelli.android.Object.GetProducts;
import com.adybelli.android.Object.Product;
import com.adybelli.android.Object.ProductOption;
import com.adybelli.android.Object.ProductOptionBody;
import com.adybelli.android.Post.ProductOptionPost;
import com.adybelli.android.Post.ProductsPost;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.adybelli.android.View.TouchDetectableScrollView;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Products extends Fragment {
    private View view;
    private Context context;
    private TextView title;
    private Button sort, filter;
    private ArrayList<Product> products = new ArrayList<>();
    private BottomSheetBehavior mBehavior;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RadioButton oldRadioButton;
    private FrameLayout fragmentContainer;
    private ApiInterface apiInterface;
    private ProgressBar progress;
    public int count = 1;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private ViewTreeObserver.OnScrollChangedListener listener;
    private ProductOptionBody productOptionBody;
    private ProductAdapter adapter;
    private SkeletonScreen skeletonScreen;
    public String catId = "";
    public static String catId2 = "";
    public String catName = "";
    public Integer type = 0;
    public static Integer type2 = 0;
    private int sortVal = 0;
    private BottomSheetDialog bottomSheetDialog;
    private LinearLayout errorContainer;
    private ImageView errorImage,back;
    private TextView errorTitle, errorMessage;
    private Button errorButton;
    public static ArrayList<String> categoriesStr = new ArrayList<>();
    public static ArrayList<String> brandsStr = new ArrayList<>();
    public static ArrayList<String> sizesStr = new ArrayList<>();
    public static ArrayList<String> colorsStr = new ArrayList<>();
    public static Integer isDiscount=0;
    public static Double min, max;
    public static String where="cats";
    private RecyclerView list;
    private RecyclerView extraRec;
    private boolean isLoading = false;

    public Products() {
    }

    public static Products newInstance() {
        Products fragment = new Products();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void showBottomSheetDialog() {
        ArrayList<String> list = new ArrayList<>();
        list.add(context.getResources().getString(R.string.default_sort));
        list.add(context.getResources().getString(R.string.cost_asc));
        list.add(context.getResources().getString(R.string.cost_desc));
        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.sort_bottom_sheet);
        LinearLayout linearLayout = bottomSheetDialog.findViewById(R.id.radioButtons);

        TextView sortTitle = bottomSheetDialog.findViewById(R.id.sortTitle);
        sortTitle.setTypeface(AppFont.getBoldFont(context));
        linearLayout.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.sort_radio_button, null, false);
            RadioButton tv = view.findViewById(R.id.tv);
            int finalI = i;
            tv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (oldRadioButton != null)
                        oldRadioButton.setChecked(false);
                    oldRadioButton = tv;
                    bottomSheetDialog.dismiss();
                    if (b) {
                        sortVal = finalI;
                        count = 1;
                        request(count);

                    }
                }
            });

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog.dismiss();
                }
            });


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog.dismiss();
                    tv.setChecked(true);
                }
            });
            tv.setText(str);
            tv.setTypeface(AppFont.getRegularFont(context));

            linearLayout.addView(view);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_products, container, false);
        context = getContext();
        initComponents();
        showBottomSheetDialog();
        setFonts();
        isDiscount=0;
        if (type == 0 && !catId.isEmpty())
            categoriesStr.add(catId);
        else if (type == 1 && !catId.isEmpty())
            brandsStr.add(catId);

        catId2=catId;
        type2=type;
        Log.e("CatID",catId2);
        request(1);
        view.findViewById(R.id.onBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    public void request(int page) {
        count = page;
        errorContainer.setVisibility(View.GONE);

        if (page == 1) {
            extraRec.setVisibility(View.VISIBLE);
            GridLayoutManager linearLayoutManager = new GridLayoutManager(context, Util.calculateNoOfColumns(context, 190));
            adapter = new ProductAdapter(context, products,false);
            extraRec.setLayoutManager(linearLayoutManager);
            skeletonScreen = Skeleton.bind(extraRec)
                    .adapter(adapter)
                    .color(R.color.skeletonColor)
                    .load(R.layout.product_skelton)
                    .count(4)
                    .show();
            products.clear();
            loading=true;
            isLoading=false;
        }
        String strCat = Arrays.toString(categoriesStr.toArray());
        String strBrand = Arrays.toString(brandsStr.toArray());
        String strSize = Arrays.toString(sizesStr.toArray());
        String strColor = Arrays.toString(colorsStr.toArray());
        ProductsPost productsPost = new ProductsPost(strBrand.substring(1, strBrand.length() - 1), strCat.substring(1, strCat.length() - 1),
                strSize.substring(1, strSize.length() - 1), strColor.substring(1, strColor.length() - 1), isDiscount, page, 20, sortVal, min, max);
        apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<GetProducts> call = apiInterface.getProducts("Bearer " + Utils.getSharedPreference(context, "tkn"), productsPost,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
        call.enqueue(new Callback<GetProducts>() {
            @Override
            public void onResponse(Call<GetProducts> call, Response<GetProducts> response) {

                extraRec.setVisibility(View.GONE);
                Log.e("Code", response.code() + "");
                if (response.isSuccessful()) {
                    progress.setVisibility(View.GONE);
                    GetProducts getProducts = response.body();
                    products.addAll((ArrayList<Product>) getProducts.getBody());
                    if (getProducts.getBody().size() <= 0) {
                        loading = false;
                        count--;
                    }

                    if (page == 1) {
                        products();
                        setListeners();
                        setDrawerLayout();
                        requestFilter();


                    } else {
                        list.getAdapter().notifyDataSetChanged();
                    }
                    if (products.size() <= 0)
                        showErrorView(R.drawable.not_found, context.getResources().getString(R.string.no_data), getResources().getString(R.string.empty), getResources().getString(R.string.continueValue));
                    title.setText(catName);

                    skeletonScreen.hide();


                } else {
                    try {
                        Log.e("Error",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    showErrorView(R.drawable.not_found, context.getResources().getString(R.string.no_data), getResources().getString(R.string.empty), getResources().getString(R.string.continueValue));
                }
                isLoading=false;

            }

            @Override
            public void onFailure(Call<GetProducts> call, Throwable t) {
                count--;
                progress.setVisibility(View.GONE);
                skeletonScreen.hide();
                isLoading=false;
                extraRec.setVisibility(View.GONE);
                if (products.size() == 0) {
                    showErrorView(R.drawable.no_connection, context.getResources().getString(R.string.noInternet), getResources().getString(R.string.checkYourInternet), getResources().getString(R.string.continueValue));
                }
            }
        });

    }

    private void requestFilter() {
        String strCat = Arrays.toString(categoriesStr.toArray());
        String strBrand = Arrays.toString(brandsStr.toArray());
        String strSize = Arrays.toString(sizesStr.toArray());
        String strColor = Arrays.toString(colorsStr.toArray());
        ProductOptionPost post = new ProductOptionPost(strBrand.substring(1, strBrand.length() - 1), strCat.substring(1, strCat.length() - 1),
                strSize.substring(1, strSize.length() - 1), strColor.substring(1, strColor.length() - 1), isDiscount);
        Call<ProductOption> call = apiInterface.getProductsOptions(post,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
        call.enqueue(new Callback<ProductOption>() {
            @Override
            public void onResponse(Call<ProductOption> call, Response<ProductOption> response) {
                if (response.isSuccessful()) {
                    ProductOption productOption = response.body();
                    productOptionBody = productOption.getBody();
                    filters();

                }
            }

            @Override
            public void onFailure(Call<ProductOption> call, Throwable t) {

            }
        });
    }

    private void initComponents() {
        back = view.findViewById(R.id.back);
        sort = view.findViewById(R.id.sort);
        filter = view.findViewById(R.id.filter);
        title = view.findViewById(R.id.title);
        navigationView = (NavigationView) view.findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer);
        fragmentContainer = view.findViewById(R.id.fragmentContainer);
        progress = view.findViewById(R.id.progress);
        errorContainer = view.findViewById(R.id.errorContainer);
        errorTitle = view.findViewById(R.id.errorTitle);
        errorMessage = view.findViewById(R.id.errorMessage);
        errorButton = view.findViewById(R.id.errorButton);
        errorImage = view.findViewById(R.id.errorImage);
        list = view.findViewById(R.id.list);
        extraRec = view.findViewById(R.id.extraRec);
        view.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SearchPage.class);
                context.startActivity(intent);
            }
        });

        title.setText(catName);
    }

    private void setFonts() {
        title.setTypeface(AppFont.getBoldFont(context));
        sort.setTypeface(AppFont.getBoldFont(context));
        filter.setTypeface(AppFont.getBoldFont(context));
    }

    private void filters() {
        FilterList filterList = new FilterList(getFragmentManager());
        filterList.productOptionBody = productOptionBody;
        try {
            getChildFragmentManager().beginTransaction().replace(R.id.fragmentContainer, filterList, filterList.getClass().getSimpleName()).commit();
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }

    }

    private void products() {
        GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 2);
        adapter = new ProductAdapter(context, products,false);
        list.setLayoutManager(linearLayoutManager);
        list.setAdapter(adapter);
    }

    private void setListeners() {
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
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
            progress.setVisibility(View.VISIBLE);
            count++;
            request(count);
        }
    }


    private void setDrawerLayout() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull @NotNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull @NotNull View drawerView) {
                MainActivity.setVisiblitybottomNavigationView(getActivity(), View.GONE);
                navigationView.bringToFront();
            }

            @Override
            public void onDrawerClosed(@NonNull @NotNull View drawerView) {
                MainActivity.setVisiblitybottomNavigationView(getActivity(), View.VISIBLE);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


    }

    private void moveToBack(View myCurrentView) {
        ViewGroup myViewGroup = ((ViewGroup) myCurrentView.getParent());
        int index = myViewGroup.indexOfChild(myCurrentView);
        for (int i = 0; i < index; i++) {
            myViewGroup.bringChildToFront(myViewGroup.getChildAt(i));
        }
    }

    public static void setFragment(FragmentManager fragmentManager, Fragment fragment, String name, String type) {
        if (type.equals("back"))
            Utils.removeShow(fragment, fragment.getClass().getSimpleName(), fragmentManager, R.id.fragmentContainer);
        if (type.equals("go"))
            Utils.hideAdd(fragment, fragment.getClass().getSimpleName(), fragmentManager, R.id.fragmentContainer);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        categoriesStr.clear();
        colorsStr.clear();
        brandsStr.clear();
        sizesStr.clear();
        min=null;
        max=null;
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity.setVisiblitybottomNavigationView(getActivity(), View.VISIBLE);
    }

    private int getStatusBarHeight() {
        Rect rectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        int contentViewTop =
                window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentViewTop - statusBarHeight;

//        Log.i("*** Elenasys :: ", "StatusBar Height= " + statusBarHeight + " , TitleBar Height = " + titleBarHeight);
        return statusBarHeight;
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
                getActivity().onBackPressed();

            }
        });
    }

    public void closeNav() {
        drawerLayout.closeDrawer(GravityCompat.END);
    }

    public boolean isDrawerOpen() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END))
            return true;
        else
            return false;
    }


}