package com.adybelli.android.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Common.Constant;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Fragment.Products;
import com.adybelli.android.Fragment.Search;
import com.adybelli.android.Object.ShopByGender;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ShopByGenderAdapter extends RecyclerView.Adapter<ShopByGenderAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ShopByGender> arrayList=new ArrayList<>();
    private SkeletonScreen skeletonScreen;
    private FragmentManager supportFragmentManager;
    private Activity activity;
    public ShopByGenderAdapter(Context context, ArrayList<ShopByGender> arrayList,FragmentManager supportFragmentManager,Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.supportFragmentManager=supportFragmentManager;
        this.activity=activity;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.shop_by_gender, parent,false);
        return new ShopByGenderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ShopByGenderAdapter.ViewHolder holder, int position) {
        ShopByGender object=arrayList.get(position);
        holder.title.setTypeface(AppFont.getBoldFont(context));



        Glide.with(context)
                .load(Constant.SERVER_URL+object.getImageUrl())
                .placeholder(R.drawable.placeholder)
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
                .into(holder.imageView);
        holder.title.setText(object.getTitle());
        if(Utils.getLanguage(context).equals("ru")){
            holder.title.setText(object.getTitle_ru());
        }
        Animation left_to_right=AnimationUtils.loadAnimation(context, R.anim.left_to_right);
        Animation right_to_left=AnimationUtils.loadAnimation(context, R.anim.right_to_left);
        if(position%2==0){
            holder.container.startAnimation(left_to_right);
        } else if(position%2==1){
            holder.container.startAnimation(right_to_left);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search search=new Search();
                if(object.getCategories()!=null){
                    String[] split=object.getCategories().split(",");
                    if(split.length==0){
                        return;
                    }
                    Search.categoryID=split[0];
                    Search.oldCategoryID="-1123abc";
                    if(split.length<2){
                        Search.subCategoryID=null;
                        Search.oldSubCategoryID=null;
                    }
                    if(split.length>=2){
                        Search.subCategoryID=split[1];
                        Search.oldSubCategoryID="-123abc4";
                    }
                }
//                Utils.hideAdd(search, Search.class.getSimpleName(), supportFragmentManager, R.id.content);
                MainActivity.secondFragment = search;
                MainActivity.setBottomNavigationSelectedItem(activity,R.id.search);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        LinearLayout container;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image);
            title=itemView.findViewById(R.id.title);
            container=itemView.findViewById(R.id.container);

        }
    }
}
