package com.adybelli.android.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adybelli.android.Activity.ProductPage;
import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Constant;
import com.adybelli.android.Common.Util;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Fragment.Basket;
import com.adybelli.android.Helper.RecyclerItemTouchHelper;
import com.adybelli.android.Object.AddFavResponse;
import com.adybelli.android.Object.BasketList;
import com.adybelli.android.Object.GetUserCard;
import com.adybelli.android.Object.GetUserCardBody;
import com.adybelli.android.Object.UpdateUserCard;
import com.adybelli.android.Post.AddFavPost;
import com.adybelli.android.Post.UpdateUserCardPost;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {
    private ArrayList<GetUserCardBody> arrayList=new ArrayList<>();
    private Context context;
    private ApiInterface apiInterface;
    private BasketAdapter adapter;
    public BasketAdapter(ArrayList<GetUserCardBody> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        apiInterface= APIClient.getClient().create(ApiInterface.class);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.basket_item,parent,false);
        return new BasketAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BasketAdapter.ViewHolder holder, int position) {
        GetUserCardBody basketList=arrayList.get(holder.getAdapterPosition());
        holder.old_cost.setPaintFlags(holder.old_cost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.name.setText(basketList.getName());
        holder.category.setText("");
        holder.size.setText(basketList.getSize());
        holder.date.setText(basketList.getDelivery_day()+" "+context.getResources().getString(R.string.day));
        if(basketList.getPrice()!=null){
            holder.old_cost.setText(basketList.getPrice()+" TMT");
        } else{
            holder.old_cost.setVisibility(View.GONE);
        }

        if(Basket.selected_id_list.contains(basketList.getCart_id())){
            holder.checkbox.setChecked(true);
        }

        holder.cost.setText(basketList.getSale_price()+" TMT");
        holder.sizeBtn.setText(context.getResources().getString(R.string.count)+" "+basketList.getCount());

        holder.name.setTypeface(AppFont.getBoldFont(context));
        holder.category.setTypeface(AppFont.getRegularFont(context));
        holder.sizeTitle.setTypeface(AppFont.getBoldFont(context));
        holder.size.setTypeface(AppFont.getRegularFont(context));
        holder.red.setTypeface(AppFont.getRegularFont(context));
        holder.dateTitle.setTypeface(AppFont.getBoldFont(context));
        holder.date.setTypeface(AppFont.getRegularFont(context));
        holder.sizeBtn.setTypeface(AppFont.getRegularFont(context));
        holder.old_cost.setTypeface(AppFont.getRegularFont(context));
        holder.cost.setTypeface(AppFont.getBoldFont(context));

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Basket.selected_id_list.add(basketList.getCart_id());
                } else{
                    Basket.selected_id_list.remove(basketList.getCart_id());
                }
                setCosts(arrayList);
            }
        });

        Glide.with(context)
                .load(Constant.SERVER_URL + basketList.getImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> imgs=new ArrayList<>();
                imgs.add(basketList.getImage());
                Util.showImageViewer(context,imgs,imgs);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet(holder.getAdapterPosition());
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // between two imageviews.
            Fade fade = new Fade();
            View decor = ((Activity)context).getWindow().getDecorView();

            // below 3 lines of code is to exclude
            // action bar,title bar and navigation
            // bar from animation.
            fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);

            // we are adding fade animation
            // for enter transition.
            ((Activity)context).getWindow().setEnterTransition(fade);
            // we are also setting fade
            // animation for exit transition.
            ((Activity)context).getWindow().setExitTransition(fade);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on image click we are opening new activity
                // and adding animation between this two activities.
                Intent intent = new Intent(context, ProductPage.class);
                intent.putExtra("id", Integer.parseInt(basketList.getProd_id()));
                intent.putExtra("image", basketList.getImage());
                // below method is used to make scene transition
                // and adding fade animation in it.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.image.setTransitionName("fade");
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            ((Activity)context), holder.image, ViewCompat.getTransitionName(holder.image));
                    // starting our activity with below method.
                    context.startActivity(intent, options.toBundle());
                } else{
                    context.startActivity(intent);
                }

            }
        });

        holder.sizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countSheet(position,holder.sizeBtn);
            }
        });

        if(Utils.getLanguage(context).equals("ru")){
            holder.name.setText(basketList.getName_ru());
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox;
        TextView name,category,sizeTitle,size,red,dateTitle,date,old_cost,cost;
        ImageView image,delete;
        Button sizeBtn;
        public RelativeLayout viewBackground, viewForeground;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            checkbox=itemView.findViewById(R.id.checkbox);
            name=itemView.findViewById(R.id.name);
            category=itemView.findViewById(R.id.category);
            sizeTitle=itemView.findViewById(R.id.sizeTitle);
            size=itemView.findViewById(R.id.size);
            red=itemView.findViewById(R.id.red);
            dateTitle=itemView.findViewById(R.id.dateTitle);
            date=itemView.findViewById(R.id.date);
            old_cost=itemView.findViewById(R.id.old_cost);
            cost=itemView.findViewById(R.id.cost);
            image=itemView.findViewById(R.id.image);
            delete=itemView.findViewById(R.id.delete);
            sizeBtn=itemView.findViewById(R.id.sizeBtn);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }
    }

    private void bottomSheet(int position){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.delete_and_add_favourite);
        TextView btn1=bottomSheetDialog.findViewById(R.id.btn1);
        TextView btn2=bottomSheetDialog.findViewById(R.id.btn2);
        TextView title2=bottomSheetDialog.findViewById(R.id.title2);

        bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

            }
        });

        title2.setTypeface(AppFont.getBoldFont(context));
        btn1.setTypeface(AppFont.getRegularFont(context));
        btn2.setTypeface(AppFont.getRegularFont(context));


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAddFav(arrayList.get(position).getProd_id(),position);
                bottomSheetDialog.dismiss();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCard(Integer.parseInt(arrayList.get(position).getSize_id()),0,Integer.parseInt(arrayList.get(position).getCart_id()));
                bottomSheetDialog.dismiss();


            }
        });
        bottomSheetDialog.show();
    }

    private void removeAddFav(String prod_id,int position) {
        AddFavPost post = new AddFavPost(prod_id);
        Call<AddFavResponse> call = apiInterface.addFavourites("Bearer " + Utils.getSharedPreference(context, "tkn"), post);
        call.enqueue(new Callback<AddFavResponse>() {
            @Override
            public void onResponse(Call<AddFavResponse> call, Response<AddFavResponse> response) {
                if (response.isSuccessful()) {
                    updateCard(Integer.parseInt(arrayList.get(position).getSize_id()),0,Integer.parseInt(arrayList.get(position).getCart_id()));
                } else {

                }
            }

            @Override
            public void onFailure(Call<AddFavResponse> call, Throwable t) {

            }
        });
    }


    private void countSheet(int position,Button countBtn){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.count_sheet);
        ListView listView=bottomSheetDialog.findViewById(R.id.list);
        TextView title2=bottomSheetDialog.findViewById(R.id.title2);
        title2.setTypeface(AppFont.getBoldFont(context));
        ArrayList<String> items=new ArrayList<>();
        for(int i=1;i<=20;i++){
            items.add(i+"");
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bottomSheetDialog.dismiss();
                countBtn.setText(context.getResources().getString(R.string.count)+" "+items.get(i));
                updateCard(Integer.parseInt(arrayList.get(position).getSize_id()),Integer.parseInt(items.get(i)),Integer.valueOf(arrayList.get(position).getCart_id()));

            }
        });


        listView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow NestedScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow NestedScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });


        bottomSheetDialog.show();

    }

    private void updateCard(int size_id,int count,int cart_id) {
        UpdateUserCardPost post=new UpdateUserCardPost(size_id,count,cart_id);
        Call<UpdateUserCard> call=apiInterface.updateUserCard("Bearer "+ Utils.getSharedPreference(context,"tkn"),post);
        call.enqueue(new Callback<UpdateUserCard>() {
            @Override
            public void onResponse(Call<UpdateUserCard> call, Response<UpdateUserCard> response) {
                if(response.isSuccessful()){
                    if(response.body().getBody().getResult().equals("SUCCESS")){
                        Basket basket=Basket.get();
                        request(basket.getProgress(),basket.getDisable(),basket.getRecyclerView(),basket.getSumOfCost());

                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateUserCard> call, Throwable t) {

            }
        });
    }

    public void removeItem(int position) {
        arrayList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    private void request(LinearProgressIndicator progress,RelativeLayout disable,RecyclerView recyclerView,TextView sumOfCost) {

        progress.setVisibility(View.VISIBLE);
        disable.setVisibility(View.VISIBLE);
        apiInterface= APIClient.getClient().create(ApiInterface.class);
        Call<GetUserCard> call=apiInterface.getUserCard("Bearer "+Utils.getSharedPreference(context,"tkn"));
        call.enqueue(new Callback<GetUserCard>() {
            @Override
            public void onResponse(Call<GetUserCard> call, Response<GetUserCard> response) {
                if(response.isSuccessful()){
                    ArrayList<GetUserCardBody> userCardBodies=response.body().getBody();
                    Basket.get().getArrayList().clear();
                    Basket.get().getArrayList().addAll(userCardBodies);
                    setBasket(recyclerView,sumOfCost,userCardBodies);
                    progress.setVisibility(View.GONE);
                    disable.setVisibility(View.GONE);
                    setCosts(userCardBodies);


                }
            }

            @Override
            public void onFailure(Call<GetUserCard> call, Throwable t) {

            }
        });
    }

    public void setCosts(ArrayList<GetUserCardBody> userCardBodies){
        Basket basket=Basket.get();
//        basket.getArrayList().clear();
//        basket.getArrayList().addAll(userCardBodies);

//        Toast.makeText(context, ""+userCardBodies.size(), Toast.LENGTH_SHORT).show();

        double discount = 0;
        double all = 0;
        double total=0;
        if (Basket.selected_id_list.size() == 0) {
            for (GetUserCardBody item : userCardBodies) {

                total += (item.getSale_price() * item.getCount());
                if (item.getPrice() != null) {

                    all += item.getPrice() * item.getCount();
                    discount += (item.getPrice() - item.getSale_price()) * item.getCount();

                } else {
                    all += item.getSale_price() * item.getCount();

                }
            }
        } else {
            for (GetUserCardBody item : userCardBodies) {

                if (Basket.selected_id_list.contains(item.getCart_id())) {
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
            basket.getView().findViewById(R.id.free_container).setVisibility(View.VISIBLE);
            Log.e("Formula","("+discount+" * 100) / "+all);
        } else{
            basket.getView().findViewById(R.id.free_container).setVisibility(View.GONE);
        }

        basket.getT4().setText(total+" TMT");

        String sum = Utils.fixNumber(total);
        String allStr = Utils.fixNumber(all);
        String disStr = Utils.fixNumber(discount);
        String disPresentStr = Utils.fixNumber(discountPresent);
        basket.getSumOfCost().setText(sum + " TMT");
        basket.getT2().setText(allStr + " TMT");
        basket.getT6().setText("-"+disStr + " TMT");
        basket.getT5().setText(Utils.getStringResource(R.string.free_cost,basket.getContext())+" (-"+disPresentStr + "%):");
        basket.getTotalCost().setText(sum+" TMT");
    }


    public void setBasket(RecyclerView recyclerView,TextView sumOfCost,ArrayList<GetUserCardBody> userCardBodies){
        Basket basket=Basket.get();


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        BasketAdapter adapter=new BasketAdapter(userCardBodies,context);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);



        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, new RecyclerItemTouchHelper.RecyclerItemTouchHelperListener() {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
                if (viewHolder instanceof BasketAdapter.ViewHolder) {
                    // get the removed item name to display it in snack bar
                    String name = userCardBodies.get(viewHolder.getAdapterPosition()).getName();

                    // backup of removed item for undo purpose
                    final GetUserCardBody deletedItem = userCardBodies.get(viewHolder.getAdapterPosition());
                    final int deletedIndex = viewHolder.getAdapterPosition();

                    // remove the item from recycler view
                    bottomSheet(position);



                }
            }
        });
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


}
