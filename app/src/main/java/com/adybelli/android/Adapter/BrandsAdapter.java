package com.adybelli.android.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Common.Constant;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Fragment.Products;
import com.adybelli.android.Object.Brands;
import com.adybelli.android.Object.PopularCategories;
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

public class BrandsAdapter extends RecyclerView.Adapter<BrandsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Brands> arrayList=new ArrayList<>();
    private FragmentManager supportFragmentManager;
    private Activity activity;
    public BrandsAdapter(Context context, ArrayList<Brands> arrayList,FragmentManager supportFragmentManager,Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.supportFragmentManager=supportFragmentManager;
        this.activity=activity;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.popular_categories, parent,false);
        return new BrandsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BrandsAdapter.ViewHolder holder, int position) {
        Brands object=arrayList.get(position);
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
        holder.title.setText(object.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Products products = new Products();
                products.type=1;
                products.catId = object.getTm_id() + "";
                products.catName = holder.title.getText().toString();
                Products.where="home";
                Utils.productFragment(products, Products.class.getSimpleName(), supportFragmentManager, R.id.content);
                MainActivity.firstFragment = new Products();
//                MainActivity.setBottomNavigationSelectedItem(activity,R.id.search);
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
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image);
            title=itemView.findViewById(R.id.title);
        }
    }
}
