package com.adybelli.android.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.adybelli.android.Activity.ProductPage;
import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.BaseViewHolder;
import com.adybelli.android.Common.Constant;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Object.AddFavResponse;
import com.adybelli.android.Object.Product;
import com.adybelli.android.Post.AddFavPost;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.bumptech.glide.Glide;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
public class PostRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    private ArrayList<Product> products;
    private Context context;
    private ApiInterface apiInterface;
    public PostRecyclerAdapter(ArrayList<Product>products,Context context) {
        this.products = products;
        this.context=context;
        apiInterface= APIClient.getClient().create(ApiInterface.class);
    }
    @NonNull @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.product_design, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                return null;
        }
    }
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }
    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == products.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }
    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }
    public void addItems(ArrayList<Product> postItems) {
        products.addAll(postItems);
        notifyDataSetChanged();
    }
    public void addLoading() {
        isLoaderVisible = true;
        products.add(new Product(1,"","","",0.0,0.0,false,false,2,"","",0,0));
        notifyItemInserted(products.size() - 1);
    }
    public void removeLoading() {
        isLoaderVisible = false;
        int position = products.size() - 1;
        Product item = getItem(position);
        if (item != null) {
            products.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void clear() {
        products.clear();
        notifyDataSetChanged();
    }
    Product getItem(int position) {
        return products.get(position);
    }
    public class ViewHolder extends BaseViewHolder {
        ImageView image;
        SparkButton fav;
        TextView name, desc, old_cost, cost, count;
        RatingBar rating;
        ViewHolder(View itemView) {
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
        protected void clear() {
        }
        public void onBind(int position) {
            super.onBind(position);
            Product product = products.get(position);
            cost.setText(product.getCost() + "TMT");
            name.setText(product.getTrademark());
            old_cost.setText(product.getOldCost  () + "TMT");
            desc.setText(product.getName());
            old_cost.setPaintFlags(old_cost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        holder.count.setText("("+product.getCount()+")");
//        if (product.isFav()) {
//            holder.fav.setChecked(true);
//        }
            Glide.with(context)
                    .load(Constant.SERVER_URL+product.getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(image);
//        holder.rating.setRating(product.getRating());
            name.setTypeface(AppFont.getBoldFont(context));
            desc.setTypeface(AppFont.getLightFont(context));
            old_cost.setTypeface(AppFont.getRegularFont(context));
            cost.setTypeface(AppFont.getSemiBoldFont(context));
            count.setTypeface(AppFont.getSemiBoldFont(context));

            if(products.get(position).getIs_favorite() == 1){
                old_cost.setVisibility(View.GONE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // between two imageviews.
                Fade fade = new Fade();
                View decor = ((Activity) context).getWindow().getDecorView();

                // below 3 lines of code is to exclude
                // action bar,title bar and navigation
                // bar from animation.
                fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
                fade.excludeTarget(android.R.id.statusBarBackground, true);
                fade.excludeTarget(android.R.id.navigationBarBackground, true);

                // we are adding fade animation
                // for enter transition.
                ((Activity) context).getWindow().setEnterTransition(fade);
                // we are also setting fade
                // animation for exit transition.
                ((Activity) context).getWindow().setExitTransition(fade);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // on image click we are opening new activity
                    // and adding animation between this two activities.
                    Intent intent = new Intent(context, ProductPage.class);
                    intent.putExtra("id",product.getId());
                    intent.putExtra("image",product.getImageUrl());
                    // below method is used to make scene transition
                    // and adding fade animation in it.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        image.setTransitionName("fade");
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                ((Activity) context), image, ViewCompat.getTransitionName(image));
                        // starting our activity with below method.
                        context.startActivity(intent, options.toBundle());
                    } else {
                        context.startActivity(intent);
                    }

                }
            });

            if(product.getFav_id()!=0 || product.getIs_favorite()==1){
                fav.setChecked(true);
            }

            fav.setEventListener(new SparkEventListener() {
                @Override
                public void onEvent(ImageView button, boolean buttonState) {
                    if(buttonState){
                        AddFavPost post=new AddFavPost(product.getId()+"");
                        Call<AddFavResponse> call=apiInterface.addFavourites("Bearer "+ Utils.getSharedPreference(context,"tkn"),post,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
                        call.enqueue(new Callback<AddFavResponse>() {
                            @Override
                            public void onResponse(Call<AddFavResponse> call, Response<AddFavResponse> response) {
                                if(response.isSuccessful()){
                                    products.get(position).setFav_id(Integer.parseInt(response.body().getBody().getFav_id()));
                                    products.get(position).setIs_favorite(1);
                                    notifyItemChanged(position);
                                } else{
                                    fav.setChecked(false);
                                }
                            }

                            @Override
                            public void onFailure(Call<AddFavResponse> call, Throwable t) {
                                fav.setChecked(false);
                            }
                        });
                    } else {

                    }
                }

                @Override
                public void onEventAnimationEnd(ImageView button, boolean buttonState) {

                }

                @Override
                public void onEventAnimationStart(ImageView button, boolean buttonState) {

                }
            });
        }
    }
    public class ProgressHolder extends BaseViewHolder {
        ProgressHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        @Override
        protected void clear() {
        }
    }
}
