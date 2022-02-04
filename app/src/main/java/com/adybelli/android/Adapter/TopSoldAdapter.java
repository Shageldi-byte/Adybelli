package com.adybelli.android.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Activity.ProductPage;
import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Constant;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Fragment.Favourite;
import com.adybelli.android.Object.AddFavResponse;
import com.adybelli.android.Object.Product;
import com.adybelli.android.Object.RemoveFavResponse;
import com.adybelli.android.Object.TopSold;
import com.adybelli.android.Post.AddFavPost;
import com.adybelli.android.Post.DeleteFavPost;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopSoldAdapter extends RecyclerView.Adapter<TopSoldAdapter.ViewHolder> {
    private Context context;
    private ArrayList<TopSold> products = new ArrayList<>();
    private ApiInterface apiInterface;
    private Boolean isFinish;
    public TopSoldAdapter(Context context, ArrayList<TopSold> products,Boolean isFinish) {
        this.context = context;
        this.products = products;
        this.isFinish=isFinish;
        apiInterface= APIClient.getClient().create(ApiInterface.class);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_design, parent, false);
        return new TopSoldAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TopSoldAdapter.ViewHolder holder, int position) {
        TopSold product = products.get(holder.getAbsoluteAdapterPosition());
        holder.cost.setText(product.getCost()+"TMT");
        holder.name.setText(product.getDesc());
        holder.old_cost.setText(product.getOldCost()+"TMT");
        holder.old_cost.setPaintFlags(holder.old_cost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.desc.setText(product.getName());

        if(products.get(position).getIs_favorite()==1){
            holder.fav.setChecked(true);
        } else{
            holder.fav.setChecked(false);
        }


        holder.fav.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if(Utils.getSharedPreference(context, "tkn").isEmpty()){
                    Utils.showCustomToast(Utils.getStringResource(R.string.no_login,context),R.drawable.ic_outline_info_24,context,R.drawable.toast_bg);
                    MainActivity mainActivity = MainActivity.get();
                    mainActivity.getBottomNavigationView().setSelectedItemId(R.id.profile);
                    holder.fav.setChecked(false);
                    return;
                }
                Favourite.isChanged=true;
                if (buttonState) {

                    products.get(position).setIs_favorite(1);
                    AddFavPost post = new AddFavPost(product.getId() + "");
                    Call<AddFavResponse> call = apiInterface.addFavourites("Bearer " + Utils.getSharedPreference(context, "tkn"), post,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
                    call.enqueue(new Callback<AddFavResponse>() {
                        @Override
                        public void onResponse(Call<AddFavResponse> call, Response<AddFavResponse> response) {
                            if (response.isSuccessful()) {
                                products.get(position).setFav_id(Integer.parseInt(response.body().getBody().getFav_id()));
                            } else {
                                holder.fav.setChecked(false);
                                products.get(position).setIs_favorite(0);
                                products.get(position).setFav_id(0);
                            }
                            notifyItemChanged(position);
                        }

                        @Override
                        public void onFailure(Call<AddFavResponse> call, Throwable t) {
                            holder.fav.setChecked(false);
                        }
                    });
                } else {
                    try {

                        products.get(position).setFav_id(0);
                        DeleteFavPost post = new DeleteFavPost(products.get(holder.getAdapterPosition()).getId());
                        Call<RemoveFavResponse> call = apiInterface.deleteFav("Bearer " + Utils.getSharedPreference(context, "tkn"), post,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
                        call.enqueue(new Callback<RemoveFavResponse>() {
                            @Override
                            public void onResponse(Call<RemoveFavResponse> call, Response<RemoveFavResponse> response) {
                                if (response.isSuccessful()) {
                                    holder.fav.setChecked(false);
                                    products.get(position).setIs_favorite(0);
                                } else {
                                    products.get(position).setIs_favorite(products.get(position).getFav_id());
                                    products.get(position).setFav_id(0);
                                    holder.fav.setChecked(true);
                                }
                                notifyItemChanged(holder.getAdapterPosition());
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


        Glide.with(context)
                .load(Constant.SERVER_URL+product.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.image);

        holder.name.setTypeface(AppFont.getBoldFont(context));
        holder.desc.setTypeface(AppFont.getLightFont(context));
        holder.old_cost.setTypeface(AppFont.getRegularFont(context));
        holder.cost.setTypeface(AppFont.getSemiBoldFont(context));
        holder.count.setTypeface(AppFont.getSemiBoldFont(context));

        if (product.getOldCost()==null || product.getOldCost()<=0) {
            holder.old_cost.setVisibility(View.GONE);
        } else{
            holder.old_cost.setVisibility(View.VISIBLE);
        }


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
                intent.putExtra("id",product.getId());
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

        if(Utils.getLanguage(context).equals("ru")){
            holder.desc.setText(product.getName_ru());
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        SparkButton fav;
        TextView name, desc, old_cost, cost, count;
        RatingBar rating;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            fav = itemView.findViewById(R.id.fav);
            desc = itemView.findViewById(R.id.desc);
            old_cost = itemView.findViewById(R.id.old_cost);
            cost = itemView.findViewById(R.id.cost);
            count = itemView.findViewById(R.id.count);
            rating = itemView.findViewById(R.id.rating);
        }
    }
}
