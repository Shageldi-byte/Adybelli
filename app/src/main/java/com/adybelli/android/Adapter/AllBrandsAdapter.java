package com.adybelli.android.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Common.Constant;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Fragment.Products;
import com.adybelli.android.Object.AllBrands;
import com.adybelli.android.Object.Brands;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AllBrandsAdapter extends RecyclerView.Adapter<AllBrandsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<AllBrands> arrayList=new ArrayList<>();
    private FragmentManager supportFragmentManager;
    private Activity activity;
    public AllBrandsAdapter(Context context, ArrayList<AllBrands> arrayList,FragmentManager supportFragmentManager,Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.supportFragmentManager=supportFragmentManager;
        this.activity=activity;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.all_brands_design, parent,false);
        return new AllBrandsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AllBrandsAdapter.ViewHolder holder, int position) {
        try {
            AllBrands object = arrayList.get(position);
            holder.title.setTypeface(AppFont.getBoldFont(context));

            Glide.with(context)
                    .load(Constant.SERVER_URL + object.getLogo())
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

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Products products = new Products();
                    products.catId = "";
                    products.catName = object.getTitle();
                    Products.brandsStr.add(object.getTm_id()+"");
                    Products.where="allBrands";
                    Utils.productFragment(products, Products.class.getSimpleName(), supportFragmentManager, R.id.content);
                    MainActivity.firstFragment = new Products();
//                    MainActivity.setBottomNavigationSelectedItem(activity,R.id.search);
                }
            });
        } catch (Exception ex){
            ex.printStackTrace();
        }
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
