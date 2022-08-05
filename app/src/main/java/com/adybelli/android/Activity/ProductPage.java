package com.adybelli.android.Activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.adybelli.android.Adapter.CarouselAdapter;
import com.adybelli.android.Adapter.ImageSlider;
import com.adybelli.android.Adapter.ProductAdapter;
import com.adybelli.android.Adapter.TopSoldAdapter;
import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.CategoryModel.Size;
import com.adybelli.android.Common.Constant;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.DataBaseHelper.RecentlyDB;
import com.adybelli.android.DataBaseHelper.SameProductsDB;
import com.adybelli.android.Fragment.Favourite;
import com.adybelli.android.Object.AddFavResponse;
import com.adybelli.android.Object.AddToCardResponse;
import com.adybelli.android.Object.Brands;
import com.adybelli.android.Object.ColorVariants;
import com.adybelli.android.Object.GetProducts;
import com.adybelli.android.Object.GetProducts2;
import com.adybelli.android.Object.Product;
import com.adybelli.android.Object.RemoveFavResponse;
import com.adybelli.android.Object.SingleProduct;
import com.adybelli.android.Object.SingleProductBody;
import com.adybelli.android.Object.SingleProductDesc;
import com.adybelli.android.Object.SingleProductImages;
import com.adybelli.android.Object.SingleProductSize;
import com.adybelli.android.Object.Slider;
import com.adybelli.android.Object.TopSold;
import com.adybelli.android.Post.AddFavPost;
import com.adybelli.android.Post.AddToCardPost;
import com.adybelli.android.Post.DeleteFavPost;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.bumptech.glide.Glide;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductPage extends AppCompatActivity {
    private Context context = this;
    // creating object of ViewPager
    private ViewPager mViewPager;
    private Handler sliderHandler = new Handler();
    private ImageSlider mViewPagerAdapter;
    private LinearLayout indicator, sizeCon;
    private Button oldActiveIndicator;
    private ArrayList<Slider> sliders = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();
    private int lastPageIndex = 0;
    private ScrollView scroll;
    private LinearLayout colorVariantsContainer, descCon;
    private ArrayList<ColorVariants> colorVariants = new ArrayList<>();
    private ArrayList<SingleProductSize> sizes = new ArrayList<>();
    private RecyclerView also_rec, recently_rec, combo_rec;
    private Button add_to_card;
    private ArrayList<Product> alsoProducts = new ArrayList<>();
    private ArrayList<TopSold> recentlyProducts = new ArrayList<>();
    private ArrayList<Product> comboProducts = new ArrayList<>();
    private TextView category, countStatus, name, kargo, skidkaStatus, cost, oldCost, desc, colorTitle, color;
    private TextView sizeTitle, size, size_fit_guide, details, alsoTitle, recentlyTitle, comboTitle, time;
    private RelativeLayout toolbar;
    private ImageView back, shareProductImage;
    private ApiInterface apiInterface;
    private int productId = 0;
    private SparkButton fav;
    private ImageView oldActiveImage = null;
    private ArrayList<SingleProductImages> imagesArray = new ArrayList<>();
    private ArrayList<String> largeImages = new ArrayList<>();
    private LinearLayout parentContainer;
    private SkeletonScreen productSkelton;
    private BottomSheetDialog bottomSheetDialog;
    private String tempImage;
    private String nameText = "", descText = "", oldCostText = "", costText = "";
    public static Integer selectedSizeId = 0;
    private RecentlyDB recentlyDB;
    private Cursor cursor;
    private SameProductsDB sameProductsDB;
    private SingleProductBody body;
    private SkeletonScreen recentlySkeleton;
    private ImageView shareTextTV;
    private TextView size_select,seller,sellerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // set an exit transition
            // here we are initializing
            // fade animation.
            Fade fade = new Fade();
            View decor = getWindow().getDecorView();

            // here also we are excluding status bar,
            // action bar and navigation bar from animation.
            fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);

            // below methods are used for adding
            // enter and exit transition.
            getWindow().setEnterTransition(fade);
            getWindow().setExitTransition(fade);
        }
        setContentView(R.layout.activity_product_page);

        Intent getIntent = getIntent();
        productId = getIntent.getIntExtra("id", 0);
        tempImage = getIntent.getStringExtra("image");
        recentlyDB = new RecentlyDB(context);
        initComponents();
        firstTime();

        defaultListener();



        setFonts();
        toolbar();

        requestProduct();
    }

    private void firstTime(){
        imagesArray.clear();
        imagesArray.add(new SingleProductImages(tempImage, tempImage, tempImage));

        slider();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        selectedSizeId = 0;
    }

    private void shareText() {
        /*Create an ACTION_SEND Intent*/
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        /*This will be the actual content you wish you share.*/
        String shareBody = "https://www.adybelli.com/products?id=12";
        /*The type of the content is text, obviously.*/
        intent.setType("text/plain");
        /*Applying information Subject and Body.*/
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Adybelli");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        /*Fire!*/
        startActivity(Intent.createChooser(intent, "Paýlaşyljak app-y saýlaň"));
    }

    private void requestProduct() {
        productSkelton.show();
        apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<SingleProduct> cal = apiInterface.getSingleProduct("Bearer " + Utils.getSharedPreference(context, "tkn"), productId,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
        cal.enqueue(new Callback<SingleProduct>() {
            @Override
            public void onResponse(Call<SingleProduct> call, Response<SingleProduct> response) {
                if (response.isSuccessful()) {
                    imagesArray.clear();

                    try {
                        body = response.body().getBody();
                        Brands brands = body.getTrademarks();
                        nameText = brands.getName();
                        descText = body.getName();
                        oldCostText = body.getPrice() + "";
                        costText = body.getSale_price() + "";
                        name.setText(brands.getName());
                        desc.setText(body.getName());

                        cost.setText(body.getSale_price() + " TMT");
                        if (body.getPrice()!=null) {
                            oldCost.setText(body.getPrice() + " TMT");
                            skidkaStatus.setVisibility(View.VISIBLE);
                            Double discPres=((body.getSale_price()*100)/body.getPrice()-100);
                            skidkaStatus.setText(Utils.fixNumber(discPres)+"%");
                        } else {
                            oldCost.setVisibility(View.GONE);
                            skidkaStatus.setVisibility(View.INVISIBLE);
                        }

                        colorVariants = body.getColors();

                        fav.setChecked(body.isIs_favorite());

                        imagesArray = body.getImages();

                        color.setText(body.getColor().getName());

                        sizes = body.getSizes();

                        if(body.getDelivery_day()!=null)
                            time.setText(Utils.getStringResource(R.string.delivery_day, context) + body.getDelivery_day()+" "+Utils.getStringResource(R.string.day,context));

                        productSkelton.hide();


                        setSizesText();

                        slider();
                        setColorVariants(colorVariants);

                        also();
                        recently();
                        combos();
                        setListener();
                        setDescriptions(body.getDescriptions());


                        if(images!=null){
                            if(images.size()>0){
                                tempImage=images.get(0);
                            }
                        }

                        bottomSheet(tempImage);


                        if (Utils.getLanguage(context).equals("ru")) {
                            desc.setText(body.getName_ru());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<SingleProduct> call, Throwable t) {
                call.cancel();
                t.printStackTrace();
//                productSkelton.hide();
            }
        });
    }

    private void setSizesText() {
        String ss = " ( ";
        if(sizes==null){
            return;
        }
        if(sizes.size()==1){
            ss+=sizes.get(0).getLabel();
        } else {
            for (int i = 0; i < sizes.size(); i++) {
                if (i < sizes.size() - 1)
                    ss += sizes.get(i).getLabel() + ", ";
                else
                    ss += " & " + sizes.get(i).getLabel();
            }
        }
        size_select.setText(Utils.getStringResource(R.string.size__, context) + ss + " )");
    }

    private void setDescriptions(ArrayList<SingleProductDesc> descriptions) {
        descCon.removeAllViews();
        for (SingleProductDesc desc : descriptions) {
            View descView = LayoutInflater.from(context).inflate(R.layout.detail_tv, null, false);
            TextView detailTv = descView.findViewById(R.id.d1);
            detailTv.setText(desc.getText());
            if (Utils.getLanguage(context).equals("ru")) {
                detailTv.setText(desc.getText_ru());
            }
            detailTv.setTypeface(AppFont.getRegularFont(context));
            descCon.addView(descView);
        }
    }

    private void initComponents() {
        seller = findViewById(R.id.seller);
        sellerTitle = findViewById(R.id.sellerTitle);
        mViewPager = findViewById(R.id.viewPagerMain);
        indicator = findViewById(R.id.indicator);
        scroll = findViewById(R.id.scroll);
        colorVariantsContainer = findViewById(R.id.colorVariantsContainer);
        sizeCon = findViewById(R.id.sizeCon);
        add_to_card = findViewById(R.id.add_to_card);
        also_rec = findViewById(R.id.also_rec);
        recently_rec = findViewById(R.id.recently_rec);
        combo_rec = findViewById(R.id.combo_rec);
        category = findViewById(R.id.category);
        countStatus = findViewById(R.id.countStatus);
        name = findViewById(R.id.name);
        kargo = findViewById(R.id.kargo);
        skidkaStatus = findViewById(R.id.skidkaStatus);
        cost = findViewById(R.id.cost);
        oldCost = findViewById(R.id.oldCost);
        desc = findViewById(R.id.desc);
        colorTitle = findViewById(R.id.colorTitle);
        color = findViewById(R.id.color);
        sizeTitle = findViewById(R.id.sizeTitle);
        size = findViewById(R.id.size);
        size_fit_guide = findViewById(R.id.size_fit_guide);
        details = findViewById(R.id.details);
        alsoTitle = findViewById(R.id.alsoTitle);
        recentlyTitle = findViewById(R.id.recentlyTitle);
        comboTitle = findViewById(R.id.comboTitle);
        time = findViewById(R.id.time);
        toolbar = findViewById(R.id.toolbar);
        back = findViewById(R.id.back);
        shareProductImage = findViewById(R.id.shareProductImage);
        fav = findViewById(R.id.fav);
        descCon = findViewById(R.id.descCon);
        parentContainer = findViewById(R.id.parentContainer);
        shareTextTV = findViewById(R.id.shareText);
        size_select = findViewById(R.id.size_select);
        productSkelton = Skeleton.bind(parentContainer)
                .load(R.layout.skelton_product_page)
                .color(R.color.skeletonColor)
                .show();
        sameProductsDB = new SameProductsDB(this);

        recentlySkeleton = Skeleton.bind(recentlyTitle)
                .load(R.layout.skelton_sub_category)
                .color(R.color.skeletonColor)
                .show();


    }

    private void slider() {
        sliders.clear();
        images.clear();
        largeImages.clear();
        for (SingleProductImages img : imagesArray) {
            sliders.add(new Slider(0, img.getMedium()));
            images.add(img.getSmall());
            largeImages.add(img.getLarge());
        }

        Log.e("Sizes", largeImages.size() + " / " + images.size());

        lastPageIndex = sliders.size();
        indicator.removeAllViews();
        // create indicator
        for (int i = 0; i < sliders.size(); i++) {
            Button indicatorButton = new Button(context);
            int backgroundId = R.drawable.slider_inactive;
            LinearLayout.LayoutParams params = getInActiveIndicatorParams();
            int position = i;
            if (i == 0) {
                backgroundId = R.drawable.slider_inactive;
                oldActiveIndicator = indicatorButton;
                params = getActiveIndicatorParams();
            }
            indicatorButton.setBackgroundResource(backgroundId);
            indicatorButton.setVisibility(View.VISIBLE);

            indicatorButton.setLayoutParams(params);

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
                oldActiveIndicator = (Button) indicator.getChildAt(position);
                Button indicatorButton = (Button) indicator.getChildAt(position);
                indicatorButton.setLayoutParams(getActiveIndicatorParams());
                indicatorButton.setBackgroundResource(R.drawable.slider_inactive);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Initializing the ViewPagerAdapter
        mViewPagerAdapter = new ImageSlider(context, sliders, images, largeImages);
        mViewPager.setAdapter(mViewPagerAdapter);

    }

    private LinearLayout.LayoutParams getActiveIndicatorParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins((int)getResources().getDimension(R.dimen.padd_10), 0, 0, 0);
        params.width = (int)getResources().getDimension(R.dimen.indicator_width);
        params.height = (int)getResources().getDimension(R.dimen.indicator_active_height);
        return params;
    }

    private LinearLayout.LayoutParams getInActiveIndicatorParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins((int)getResources().getDimension(R.dimen.padd_10), 0, 0, 0);
        params.width = (int)getResources().getDimension(R.dimen.indicator_width);
        params.height = (int)getResources().getDimension(R.dimen.indicator_passive_height);
        return params;
    }

    private void setListener() {
        shareProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Slider slider = sliders.get(mViewPager.getCurrentItem());
                Intent intent = new Intent(context, ShareProductImage.class);
                intent.putExtra("url", slider.getImageUrl());
                if (Utils.getLanguage(context).equals("ru"))
                    intent.putExtra("name", body.getName_ru());
                else
                    intent.putExtra("name", body.getName());
                intent.putExtra("trademark", body.getTrademarks().getName());
                intent.putExtra("cost", body.getSale_price() + "");
                if(body.getPrice()==null)
                    intent.putExtra("old_cost", "");
                else
                    intent.putExtra("old_cost", body.getPrice() + "");
                intent.putExtra("id", body.getProd_id() + "");
                startActivity(intent);
            }
        });


        add_to_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });

        size_fit_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, WebViewPage.class).putExtra("page", "size_table"));
            }
        });


        sizeCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });

        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,StorePage.class);
                startActivity(intent);
            }
        });

        fav.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if(Utils.getSharedPreference(context, "tkn").isEmpty()){
                    Utils.showCustomToast(Utils.getStringResource(R.string.no_login,context),R.drawable.ic_outline_info_24,context,R.drawable.toast_bg);
                    finish();
                    MainActivity mainActivity = MainActivity.get();
                    mainActivity.getBottomNavigationView().setSelectedItemId(R.id.profile);
                    return;
                }
                Favourite.isChanged = true;
                if (buttonState) {
                    AddFavPost post = new AddFavPost(productId + "");
                    Call<AddFavResponse> call = apiInterface.addFavourites("Bearer " + Utils.getSharedPreference(context, "tkn"), post,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
                    call.enqueue(new Callback<AddFavResponse>() {
                        @Override
                        public void onResponse(Call<AddFavResponse> call, Response<AddFavResponse> response) {
                            if (response.isSuccessful()) {

                            } else {

                            }
                        }

                        @Override
                        public void onFailure(Call<AddFavResponse> call, Throwable t) {

                        }
                    });
                } else {
                    try {
                        DeleteFavPost post = new DeleteFavPost(productId);
                        Call<RemoveFavResponse> call = apiInterface.deleteFav("Bearer " + Utils.getSharedPreference(context, "tkn"), post,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
                        call.enqueue(new Callback<RemoveFavResponse>() {
                            @Override
                            public void onResponse(Call<RemoveFavResponse> call, Response<RemoveFavResponse> response) {
                                if (response.isSuccessful()) {

                                } else {

                                }
                            }

                            @Override
                            public void onFailure(Call<RemoveFavResponse> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });

        shareTextTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareText();
            }
        });


    }

    private void defaultListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void setColorVariants(ArrayList<ColorVariants> variants) {
        colorVariantsContainer.removeAllViews();
        if(variants==null){
            return;
        }
        for (int i = 0; i < variants.size(); i++) {
            ColorVariants image = variants.get(i);
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)context.getResources().getDimension(R.dimen.thumbnail), (int)context.getResources().getDimension(R.dimen.thumbnail));
            layoutParams.setMargins(30, 0, 0, 0);
            imageView.setLayoutParams(layoutParams);
            imageView.setPadding(2, 2, 2, 2);
            imageView.setBackgroundResource(android.R.color.transparent);
            if(productId==image.getProd_id()){
                imageView.setBackgroundResource(R.drawable.orrange_stroke);
            }

            Glide.with(context)
                    .load(Constant.SERVER_URL + image.getImage())
                    .placeholder(R.drawable.placeholder).fitCenter()
                    .into(imageView);
            int finalI = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (variants.size()>1 && image.getProd_id()!=productId) {
                        productId = image.getProd_id();
                        tempImage=image.getImage();
                        firstTime();
                        requestProduct();

                        if (oldActiveImage != null && oldActiveImage != imageView) {
                            oldActiveImage.setBackgroundResource(android.R.color.transparent);
                        }
                        oldActiveImage = imageView;
                    }
                }
            });

            colorVariantsContainer.addView(imageView);
        }
    }


    class SizeOfFit extends RecyclerView.Adapter<SizeOfFit.ViewHolder> {
        private ArrayList<Size> sizes = new ArrayList<>();
        private Context context;

        public SizeOfFit(ArrayList<Size> sizes, Context context) {
            this.sizes = sizes;
            this.context = context;
        }

        @NonNull
        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.size_item, parent, false);
            return new SizeOfFit.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ProductPage.SizeOfFit.ViewHolder holder, int position) {
            Size size = sizes.get(position);
            holder.sizeBtn.setTypeface(AppFont.getRegularFont(context));
            holder.sizeBtn.setText(size.getSize() + "");
            holder.sizeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.sizeBtn.getTypeface().equals(AppFont.getBoldFont(context))) {
                        holder.sizeBtn.setBackgroundColor(context.getResources().getColor(R.color.white));
                        holder.sizeBtn.setBackgroundResource(R.drawable.stroke);
                        holder.sizeBtn.setTextColor(context.getResources().getColor(R.color.black));
                        holder.sizeBtn.setTypeface(AppFont.getRegularFont(context));
                    } else {
                        holder.sizeBtn.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                        holder.sizeBtn.setTextColor(context.getResources().getColor(R.color.white));
                        holder.sizeBtn.setTypeface(AppFont.getBoldFont(context));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return sizes.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            Button sizeBtn;

            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                sizeBtn = itemView.findViewById(R.id.sizeBtn);
            }
        }
    }


    private void bottomSheet(String imageUrl) {
        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.add_to_basket);
        RecyclerView rec = bottomSheetDialog.findViewById(R.id.sizeRec);
        ImageView close = bottomSheetDialog.findViewById(R.id.close);
        ImageView image = bottomSheetDialog.findViewById(R.id.image);

        Glide.with(context)
                .load(Constant.SERVER_URL+imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(image);

        TextView name = bottomSheetDialog.findViewById(R.id.name);
        TextView category = bottomSheetDialog.findViewById(R.id.category);
        TextView old_cost = bottomSheetDialog.findViewById(R.id.old_cost);
        TextView cost = bottomSheetDialog.findViewById(R.id.cost);
        TextView sizeTitle = bottomSheetDialog.findViewById(R.id.sizeTitle);
        TextView continueSopping = bottomSheetDialog.findViewById(R.id.continueSopping);
        Button acceptBtn = bottomSheetDialog.findViewById(R.id.acceptBtn);
        ProgressBar progressBar = bottomSheetDialog.findViewById(R.id.progressBar);

        name.setTypeface(AppFont.getBoldFont(context));
        category.setTypeface(AppFont.getRegularFont(context));
        old_cost.setTypeface(AppFont.getRegularFont(context));
        cost.setTypeface(AppFont.getBoldFont(context));
        sizeTitle.setTypeface(AppFont.getBoldFont(context));
        acceptBtn.setTypeface(AppFont.getBoldFont(context));
        continueSopping.setTypeface(AppFont.getBoldFont(context));

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.getSharedPreference(context, "tkn").isEmpty()){
                    Utils.showCustomToast(Utils.getStringResource(R.string.no_login,context),R.drawable.ic_outline_info_24,context,R.drawable.toast_bg);
                    bottomSheetDialog.dismiss();
                    finish();
                    MainActivity mainActivity = MainActivity.get();
                    mainActivity.getBottomNavigationView().setSelectedItemId(R.id.profile);
                    progressBar.setVisibility(View.GONE);
                    acceptBtn.setVisibility(View.VISIBLE);
                    return;
                }
                if(selectedSizeId==0){
                    Utils.showCustomToast(Utils.getStringResource(R.string.fill_out,context),R.drawable.ic_outline_info_24,context,R.drawable.toast_bg);
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                acceptBtn.setVisibility(View.GONE);
                apiInterface = APIClient.getClient().create(ApiInterface.class);
                AddToCardPost post = new AddToCardPost(productId, selectedSizeId, 1);
                Call<AddToCardResponse> call = apiInterface.addToCard("Bearer " + Utils.getSharedPreference(context, "tkn"), post,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
                call.enqueue(new Callback<AddToCardResponse>() {
                    @Override
                    public void onResponse(Call<AddToCardResponse> call, Response<AddToCardResponse> response) {
                        if (response.isSuccessful()) {
                            sameProductsDB.insert(productId + "");
                            finish();
                            MainActivity mainActivity = MainActivity.get();
                            mainActivity.getBottomNavigationView().setSelectedItemId(R.id.basket);
                            progressBar.setVisibility(View.GONE);
                            acceptBtn.setVisibility(View.VISIBLE);
                        } else{
                            Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong,context),R.drawable.ic_baseline_close_24,context,R.drawable.toast_bg_error);
                            progressBar.setVisibility(View.GONE);
                            acceptBtn.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<AddToCardResponse> call, Throwable t) {
                        Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong,context),R.drawable.ic_baseline_close_24,context,R.drawable.toast_bg_error);
                        progressBar.setVisibility(View.GONE);
                        acceptBtn.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        name.setText(nameText);
        category.setText(descText);
        old_cost.setText(oldCostText+" TMT");
        if (oldCostText.isEmpty() || oldCostText.equals("null")) {
            old_cost.setVisibility(View.GONE);
        }
        cost.setText(costText+" TMT");

        continueSopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        old_cost.setPaintFlags(old_cost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        SizeAd adapter = new SizeAd(sizes, context);
        rec.setLayoutManager(linearLayoutManager);
        rec.setAdapter(adapter);
    }

    private void setFonts() {
        time.setTypeface(AppFont.getRegularFont(context));
        category.setTypeface(AppFont.getRegularFont(context));
        countStatus.setTypeface(AppFont.getRegularFont(context));
        name.setTypeface(AppFont.getBoldFont(context));
        kargo.setTypeface(AppFont.getRegularFont(context));
        skidkaStatus.setTypeface(AppFont.getRegularFont(context));
        cost.setTypeface(AppFont.getBoldFont(context));
        oldCost.setTypeface(AppFont.getRegularFont(context));
        desc.setTypeface(AppFont.getRegularFont(context));
        colorTitle.setTypeface(AppFont.getSemiBoldFont(context));
        color.setTypeface(AppFont.getRegularFont(context));
        sizeTitle.setTypeface(AppFont.getSemiBoldFont(context));
        size.setTypeface(AppFont.getRegularFont(context));
        size_fit_guide.setTypeface(AppFont.getSemiBoldFont(context));
        details.setTypeface(AppFont.getBoldFont(context));
        alsoTitle.setTypeface(AppFont.getBoldFont(context));
        recentlyTitle.setTypeface(AppFont.getBoldFont(context));
        comboTitle.setTypeface(AppFont.getBoldFont(context));
        add_to_card.setTypeface(AppFont.getBoldFont(context));
        size_select.setTypeface(AppFont.getRegularFont(context));
        sellerTitle.setTypeface(AppFont.getRegularFont(context));
        seller.setTypeface(AppFont.getSemiBoldFont(context));
        oldCost.setPaintFlags(oldCost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    private void also() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        ProductAdapter adapter = new ProductAdapter(context, alsoProducts,true);
        also_rec.setLayoutManager(linearLayoutManager);
        also_rec.setAdapter(adapter);
    }

    private void recently() {
        cursor = recentlyDB.getAll();
        TopSoldAdapter adapter4 = new TopSoldAdapter(context, recentlyProducts,true);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        recently_rec.setLayoutManager(linearLayoutManager2);
        recently_rec.setAdapter(adapter4);
        String ids = "";

        if (cursor.getCount() == 0) {
            recentlyTitle.setVisibility(View.GONE);
            recently_rec.setVisibility(View.GONE);
            Cursor cursor1 = recentlyDB.getSelect(productId + "");

            if (cursor1.getCount() == 0) {
                recentlyDB.insert(productId + "");
            }
            recentlyTitle.setVisibility(View.GONE);
            recently_rec.setVisibility(View.GONE);
            recentlySkeleton.hide();
        } else {
            while (cursor.moveToNext()) {
                if(!String.valueOf(productId).equals(cursor.getString(1))) {
                    ids += cursor.getString(1) + ",";
                }
            }
            ids = ids.substring(0, ids.length() - 1);
            Cursor cursor1 = recentlyDB.getSelect(productId + "");

            if (cursor1.getCount() == 0) {
                recentlyDB.insert(productId + "");
            }
            apiInterface = APIClient.getClient().create(ApiInterface.class);
            Call<GetProducts2> call = apiInterface.getRecently("Bearer " + Utils.getSharedPreference(context, "tkn"), ids,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
            call.enqueue(new Callback<GetProducts2>() {
                @Override
                public void onResponse(Call<GetProducts2> call, Response<GetProducts2> response) {
                    if (response.isSuccessful()) {
                        recentlyTitle.setVisibility(View.VISIBLE);
                        recently_rec.setVisibility(View.VISIBLE);
                        recentlyProducts = (ArrayList<TopSold>) response.body().getBody();
                        TopSoldAdapter adapter3 = new TopSoldAdapter(context, recentlyProducts,true);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
                        recently_rec.setLayoutManager(linearLayoutManager);
                        recently_rec.setAdapter(adapter3);

                        if(recentlyProducts.size()<=0){
                            recentlyTitle.setVisibility(View.GONE);
                            recently_rec.setVisibility(View.GONE);
                        }

                    } else {
                        recentlyTitle.setVisibility(View.GONE);
                        recently_rec.setVisibility(View.GONE);
                    }
                    recentlySkeleton.hide();


                }

                @Override
                public void onFailure(Call<GetProducts2> call, Throwable t) {
                    recentlyTitle.setVisibility(View.GONE);
                    recently_rec.setVisibility(View.GONE);
                    recentlySkeleton.hide();
                }
            });
        }


    }

    private void combos() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        ProductAdapter adapter = new ProductAdapter(context, comboProducts,true);
        combo_rec.setLayoutManager(linearLayoutManager);
        combo_rec.setAdapter(adapter);
    }

    private void toolbar() {
        scroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (scroll.getScrollY() >= (indicator.getY() - 70)) {
                    toolbar.setBackgroundColor(getResources().getColor(R.color.white));
                    ColorStateList csl = AppCompatResources.getColorStateList(context, R.color.black);
                    ImageViewCompat.setImageTintList(back, csl);
                } else {
                    toolbar.setBackgroundResource(R.drawable.gradient);
                    ColorStateList csl = AppCompatResources.getColorStateList(context, R.color.white);
                    ImageViewCompat.setImageTintList(back, csl);
                }
//                Log.e("EQUAL", scroll.getScrollY() + " / " + indicator.getY() + " / " + indicator.getScrollY() + " / " + indicator.getTop());
            }
        });


    }


    class SizeAd extends RecyclerView.Adapter<SizeAd.ViewHolder> {
        private ArrayList<SingleProductSize> sizes = new ArrayList<>();
        private Context context;
        private Button oldButton;
        private boolean isFirstTime = true;

        public SizeAd(ArrayList<SingleProductSize> sizes, Context context) {
            this.sizes = sizes;
            this.context = context;
        }

        @NonNull
        @NotNull
        @Override
        public ProductPage.SizeAd.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.size_two, parent, false);
            return new ProductPage.SizeAd.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ProductPage.SizeAd.ViewHolder holder, int position) {
            SingleProductSize size = sizes.get(position);
            holder.sizeBtn.setTypeface(AppFont.getRegularFont(context));
            holder.sizeBtn.setText(size.getLabel());


            TextView textView = ((Activity) context).findViewById(R.id.size);
//            if (position == 0 && isFirstTime) {
//                setActive(holder.sizeBtn);
//                oldButton = holder.sizeBtn;
//                selectedSizeId = size.getS_id();
//                textView.setText(size.getLabel());
//                isFirstTime = false;
//            }
            if(size.getStockQuantity()!=null){
                if(size.getStockQuantity()==0){
                    setDisable(holder.sizeBtn);
                }
            }
            
            
            
            holder.sizeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(selectedSizeId==size.getS_id()){
                        setPassive(holder.sizeBtn);
                        oldButton=null;
                        selectedSizeId=0;
                        textView.setText("");
                        return;
                    }
                    if(size.getStockQuantity()!=null){
                        if(size.getStockQuantity()==0){
                            Toast.makeText(context, Utils.getStringResource(R.string.no_product_in_stock,context), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    selectedSizeId = size.getS_id();
                    textView.setText(size.getLabel());
                    if (holder.sizeBtn == oldButton) {
                        return;
                    }
                    setActive(holder.sizeBtn);
                    if (oldButton != null) {
                        setPassive(oldButton);
                    }
                    oldButton = holder.sizeBtn;
                }
            });
        }

        private void setActive(Button button) {
            button.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            button.setTextColor(context.getResources().getColor(R.color.white));
            button.setTypeface(AppFont.getBoldFont(context));
        }

        private void setPassive(Button button) {
            button.setBackgroundColor(context.getResources().getColor(R.color.white));
            button.setBackgroundResource(R.drawable.stroke);
            button.setTextColor(context.getResources().getColor(R.color.black));
            button.setTypeface(AppFont.getRegularFont(context));
        }

        private void setDisable(Button button) {
            button.setBackgroundColor(context.getResources().getColor(R.color.white));
            button.setBackgroundResource(R.drawable.ic_group_1);
            button.setTextColor(context.getResources().getColor(R.color.inactive));
            button.setTypeface(AppFont.getRegularFont(context));
        }


        @Override
        public int getItemCount() {
            return sizes.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            Button sizeBtn;

            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                sizeBtn = itemView.findViewById(R.id.sizeBtn);
            }
        }
    }


}