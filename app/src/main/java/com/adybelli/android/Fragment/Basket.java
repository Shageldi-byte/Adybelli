package com.adybelli.android.Fragment;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Activity.SearchPage;
import com.adybelli.android.Adapter.BasketAdapter;
import com.adybelli.android.Adapter.ProductAdapter;
import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Util;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Helper.RecyclerItemTouchHelper;
import com.adybelli.android.Object.AddFavResponse;
import com.adybelli.android.Object.BasketList;
import com.adybelli.android.Object.GetUserCard;
import com.adybelli.android.Object.GetUserCardBody;
import com.adybelli.android.Object.Product;
import com.adybelli.android.Object.UpdateUserCard;
import com.adybelli.android.Post.AddFavPost;
import com.adybelli.android.Post.UpdateUserCardPost;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Basket extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private View view;
    private Context context;
    private ArrayList<GetUserCardBody> arrayList = new ArrayList<>();
    private RecyclerView recyclerView, also_rec;
    private ArrayList<Product> alsoProducts = new ArrayList<>();
    private TextView also_Title, title, sumOfCost, costTitle;
    private RelativeLayout cost_dialog, bottomMenu, trbg;
    private Animation start, exit;
    private ImageView icon;
    private BasketAdapter adapter;
    private Button acceptBtn;
    private TextView aboutTitle, t1, t2, t3, t4, t5, t6, t7, t8, totalCostTitle, totalCost;
    private ApiInterface apiInterface;
    private SkeletonScreen skeletonScreen;
    private LinearProgressIndicator progress;
    private RelativeLayout disable;
    private double total = 0;
    private static Basket INSTANCE;
    public static ArrayList<String> selected_id_list = new ArrayList<>();

    private LinearLayout errorContainer;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button errorButton;
    private boolean isLoading = false;
    private LinearLayout free_container;

    public Basket() {
    }

    public static Basket newInstance() {
        Basket fragment = new Basket();
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
        context = getContext();
        Utils.loadLocal(context);
        INSTANCE = this;
        view = inflater.inflate(R.layout.fragment_basket, container, false);

        initComponents();
        setFonts();
        return view;
    }

    public static Basket get() {
        return INSTANCE;
    }

    public LinearProgressIndicator getProgress() {
        return progress;
    }

    public RelativeLayout getDisable() {
        return disable;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public TextView getSumOfCost() {
        return sumOfCost;
    }

    public TextView getT6() {
        return t6;
    }

    public TextView getT2() {
        return t2;
    }

    public TextView getT5() {
        return t5;
    }

    public TextView getT3() {
        return t3;
    }

    public TextView getT4() {
        return t4;
    }

    public LinearLayout getFree_container() {
        return free_container;
    }

    public TextView getTotalCost() {
        return totalCost;
    }

    public View getView() {
        return view;
    }

    public Context context() {
        return context;
    }

    public ArrayList<GetUserCardBody> getArrayList() {
        return arrayList;
    }

    public ArrayList<String> getSelected_id_list() {
        return selected_id_list;
    }

    private void request() {
        selected_id_list.clear();
        isLoading = true;
        errorContainer.setVisibility(View.GONE);
        String tkn = Utils.getSharedPreference(context, "tkn");
        if (tkn.isEmpty()) {
            progress.setVisibility(View.GONE);
            disable.setVisibility(View.GONE);
            showErrorView(R.drawable.basket_empty, context.getResources().getString(R.string.no_login), getResources().getString(R.string.must_login), getResources().getString(R.string.continueValue),true);
            arrayList.clear();
            adapter = new BasketAdapter(arrayList, context);
            recyclerView.setAdapter(adapter);
            bottomMenu.setVisibility(View.GONE);
            isLoading=false;
            return;
        }
        total = 0;
        progress.setVisibility(View.VISIBLE);
        disable.setVisibility(View.VISIBLE);
        apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<GetUserCard> call = apiInterface.getUserCard("Bearer " + Utils.getSharedPreference(context, "tkn"),Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
        call.enqueue(new Callback<GetUserCard>() {
            @Override
            public void onResponse(Call<GetUserCard> call, Response<GetUserCard> response) {
                if (response.isSuccessful()) {
                    arrayList = response.body().getBody();
                    setBasket();
                    also();

                    progress.setVisibility(View.GONE);
                    disable.setVisibility(View.GONE);

                    if (arrayList.size() <= 0) {
                        showErrorView(R.drawable.basket_empty, context.getResources().getString(R.string.empty_basket), getResources().getString(R.string.empty_basket_message), getResources().getString(R.string.continueValue),false);
                        bottomMenu.setVisibility(View.GONE);
                    } else {
                        bottomMenu.setVisibility(View.VISIBLE);
                        setListeners();
                    }

                    isLoading = false;

                } else {
                    showErrorView(R.drawable.no_connection, context.getResources().getString(R.string.noInternet), getResources().getString(R.string.something_went_wrong), getResources().getString(R.string.continueValue),true);
                    progress.setVisibility(View.GONE);
                    disable.setVisibility(View.GONE);
                    if(arrayList.size()<=0)
                    bottomMenu.setVisibility(View.GONE);
                    isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<GetUserCard> call, Throwable t) {
                showErrorView(R.drawable.no_connection, context.getResources().getString(R.string.noInternet), getResources().getString(R.string.checkYourInternet), getResources().getString(R.string.continueValue),true);
                progress.setVisibility(View.GONE);
                disable.setVisibility(View.GONE);
                bottomMenu.setVisibility(View.GONE);
                isLoading = false;
            }
        });
    }

    private void initComponents() {
        recyclerView = view.findViewById(R.id.rec);
        disable = view.findViewById(R.id.disable);
        also_rec = view.findViewById(R.id.also_rec);
        also_Title = view.findViewById(R.id.alsoTitle);
        cost_dialog = view.findViewById(R.id.cost_dialog);
        bottomMenu = view.findViewById(R.id.bottomMenu);
        icon = view.findViewById(R.id.icon);
        trbg = view.findViewById(R.id.trbg);
        title = view.findViewById(R.id.title);
        costTitle = view.findViewById(R.id.costTitle);
        sumOfCost = view.findViewById(R.id.sumOfCost);
        acceptBtn = view.findViewById(R.id.acceptBtn);
        progress = view.findViewById(R.id.progress);

        aboutTitle = view.findViewById(R.id.aboutTitle);
        t1 = view.findViewById(R.id.t1);
        t2 = view.findViewById(R.id.t2);
        t3 = view.findViewById(R.id.t3);
        t4 = view.findViewById(R.id.t4);
        t5 = view.findViewById(R.id.t5);
        t6 = view.findViewById(R.id.t6);
        t7 = view.findViewById(R.id.t7);
        t8 = view.findViewById(R.id.t8);
        totalCostTitle = view.findViewById(R.id.totalCostTitle);
        totalCost = view.findViewById(R.id.totalCost);
        free_container = view.findViewById(R.id.free_container);

        errorContainer = view.findViewById(R.id.errorContainer);
        errorTitle = view.findViewById(R.id.errorTitle);
        errorMessage = view.findViewById(R.id.errorMessage);
        errorButton = view.findViewById(R.id.errorButton);
        errorImage = view.findViewById(R.id.errorImage);


        start = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        exit = AnimationUtils.loadAnimation(context, R.anim.slide_down);

        view.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SearchPage.class);
                context.startActivity(intent);
            }
        });
        t3.setVisibility(View.GONE);
        t4.setVisibility(View.GONE);

        view.findViewById(R.id.deliverCostContainer).setVisibility(View.GONE);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && !isLoading) {
            request();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isLoading)
            request();
    }

    private void setBasket() {
        double discount = 0;
        double all = 0;
        if (selected_id_list.size() == 0) {
            for (GetUserCardBody item : arrayList) {

                total += (item.getSale_price() * item.getCount());
                if (item.getPrice() != null) {

                    all += item.getPrice() * item.getCount();
                    discount += (item.getPrice() - item.getSale_price()) * item.getCount();

                } else {
                    all += item.getSale_price() * item.getCount();

                }
            }
        } else {
            for (GetUserCardBody item : arrayList) {

                if (selected_id_list.contains(item.getCart_id())) {
                    total += (item.getSale_price() * item.getCount());
                    if (item.getPrice() != null) {

                        all += item.getPrice() * item.getCount();
                        discount += (item.getPrice() - item.getSale_price()) * item.getCount();

                    } else {
                        all += item.getSale_price() * item.getCount();

                    }
                }
            }
        }
        double discountPresent = 0;

        if (discount > 0) {
            discountPresent = (discount * 100) / all;
            view.findViewById(R.id.free_container).setVisibility(View.VISIBLE);
            Log.e("Formula", "(" + discount + " * 100) / " + all);
        } else {
            view.findViewById(R.id.free_container).setVisibility(View.GONE);
        }


        String sum = Utils.fixNumber(total);
        String allStr = Utils.fixNumber(all);
        String disStr = Utils.fixNumber(discount);
        String disPresentStr = Utils.fixNumber(discountPresent);
        sumOfCost.setText(sum + " TMT");
        t2.setText(allStr + " TMT");
        t6.setText("-" + disStr + " TMT");
        t5.setText(Utils.getStringResource(R.string.free_cost, context) + " (-" + disPresentStr + "%):");
        totalCost.setText(sum + " TMT");


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        adapter = new BasketAdapter(arrayList, context);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);


    }

    private void also() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        ProductAdapter adapter = new ProductAdapter(context, alsoProducts,false);
        also_rec.setLayoutManager(linearLayoutManager);
        also_rec.setAdapter(adapter);
    }

    private void setListeners() {
        bottomMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cost_dialog.getVisibility() == View.GONE) {
                    trbg.setVisibility(View.VISIBLE);
                    cost_dialog.startAnimation(start);
                    cost_dialog.setVisibility(View.VISIBLE);
                    icon.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);

                } else {
                    trbg.setVisibility(View.GONE);
                    cost_dialog.setVisibility(View.GONE);
                    cost_dialog.startAnimation(exit);
                    icon.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                }
            }
        });

        trbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trbg.setVisibility(View.GONE);
                cost_dialog.setVisibility(View.GONE);
                cost_dialog.startAnimation(exit);
                icon.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trbg.setVisibility(View.GONE);
                cost_dialog.setVisibility(View.GONE);
                icon.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);

                String strIds = Arrays.toString(selected_id_list.toArray());
                if (strIds.equals("[]") || strIds.isEmpty()) {
                    for (GetUserCardBody card : arrayList) {
                        selected_id_list.add(card.getCart_id());
                    }
                }
                strIds = Arrays.toString(selected_id_list.toArray());
                ProcceedCheckout procceedCheckout = new ProcceedCheckout(strIds.substring(1, strIds.length() - 1), selected_id_list);
                Utils.hideAdd(procceedCheckout, ProcceedCheckout.class.getSimpleName(), getFragmentManager(), R.id.content);
                MainActivity.fourthFragment = procceedCheckout;
            }
        });
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof BasketAdapter.ViewHolder) {
            // get the removed item name to display it in snack bar
            String name = arrayList.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final GetUserCardBody deletedItem = arrayList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            bottomSheet(viewHolder.getAdapterPosition());


        }
    }

    private void bottomSheet(int position) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.delete_and_add_favourite);
        TextView btn1 = bottomSheetDialog.findViewById(R.id.btn1);
        TextView btn2 = bottomSheetDialog.findViewById(R.id.btn2);
        TextView title2 = bottomSheetDialog.findViewById(R.id.title2);

        title2.setTypeface(AppFont.getBoldFont(context));
        btn1.setTypeface(AppFont.getRegularFont(context));
        btn2.setTypeface(AppFont.getRegularFont(context));


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAddFav(arrayList.get(position).getProd_id(), position);
                bottomSheetDialog.dismiss();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCard(Integer.parseInt(arrayList.get(position).getSize_id()), 0, Integer.parseInt(arrayList.get(position).getCart_id()));
                bottomSheetDialog.dismiss();


            }
        });
        bottomSheetDialog.show();
    }

    private void removeAddFav(String prod_id, int position) {
        AddFavPost post = new AddFavPost(prod_id);
        Call<AddFavResponse> call = apiInterface.addFavourites("Bearer " + Utils.getSharedPreference(context, "tkn"), post,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
        call.enqueue(new Callback<AddFavResponse>() {
            @Override
            public void onResponse(Call<AddFavResponse> call, Response<AddFavResponse> response) {
                if (response.isSuccessful()) {
                    updateCard(Integer.parseInt(arrayList.get(position).getSize_id()), 0, Integer.parseInt(arrayList.get(position).getCart_id()));
                } else {

                }
            }

            @Override
            public void onFailure(Call<AddFavResponse> call, Throwable t) {

            }
        });
    }

    private void updateCard(int size_id, int count, int cart_id) {
        UpdateUserCardPost post = new UpdateUserCardPost(size_id, count, cart_id);
        Call<UpdateUserCard> call = apiInterface.updateUserCard("Bearer " + Utils.getSharedPreference(context, "tkn"), post,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
        call.enqueue(new Callback<UpdateUserCard>() {
            @Override
            public void onResponse(Call<UpdateUserCard> call, Response<UpdateUserCard> response) {
                if (response.isSuccessful()) {
                    if (response.body().getBody().getResult().equals("SUCCESS")) {
                        request();
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateUserCard> call, Throwable t) {

            }
        });
    }

    private void setFonts() {
        acceptBtn.setTypeface(AppFont.getBoldFont(context));
        sumOfCost.setTypeface(AppFont.getBoldFont(context));
        costTitle.setTypeface(AppFont.getRegularFont(context));
        title.setTypeface(AppFont.getBoldFont(context));
        also_Title.setTypeface(AppFont.getBoldFont(context));
        aboutTitle.setTypeface(AppFont.getBoldFont(context));
        t1.setTypeface(AppFont.getRegularFont(context));
        t2.setTypeface(AppFont.getRegularFont(context));
        t3.setTypeface(AppFont.getRegularFont(context));
        t4.setTypeface(AppFont.getRegularFont(context));
        t5.setTypeface(AppFont.getRegularFont(context));
        t6.setTypeface(AppFont.getRegularFont(context));
        t7.setTypeface(AppFont.getRegularFont(context));
        t8.setTypeface(AppFont.getRegularFont(context));
        totalCostTitle.setTypeface(AppFont.getBoldFont(context));
        totalCost.setTypeface(AppFont.getBoldFont(context));

    }


    private void showErrorView(int image, String title, String message, String btn,boolean isRetry) {
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
                String tkn = Utils.getSharedPreference(context, "tkn");
                if (tkn.trim().isEmpty()) {
                    MainActivity.setBottomNavigationSelectedItem(getActivity(), R.id.profile);
                    return;
                }


                if(isRetry)
                    request();
                else
                    MainActivity.setBottomNavigationSelectedItem(getActivity(), R.id.search);

            }
        });
    }


}