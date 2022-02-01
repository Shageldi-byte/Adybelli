package com.adybelli.android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Common.Constant;
import com.adybelli.android.Common.Util;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Fragment.Products;
import com.adybelli.android.Object.SubCategory;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {
    List<SubCategory> subCategories = new ArrayList<>();
    Context context;
    FragmentManager supportFragmentManager;

    public SubCategoryAdapter(List<SubCategory> subCategories, Context context, FragmentManager supportFragmentManager) {
        this.subCategories = subCategories;
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new SubCategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SubCategoryAdapter.ViewHolder holder, int position) {
        SubCategory subCategory = subCategories.get(position);
        String title = subCategory.getTitle();
        if (Utils.getLanguage(context).equals("ru"))
            title = subCategory.getTitle_ru();
        holder.textView.setText(subCategory.getTitle());
        holder.textView.setTypeface(AppFont.getRegularFont(context));
        String finalTitle = title;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Products products = new Products();
                products.catId = subCategory.getId() + "";
                products.catName = finalTitle;
                Utils.productFragment(products, Products.class.getSimpleName(), supportFragmentManager, R.id.content);
                Products.where = "cats";

                MainActivity.secondFragment = new Products();
            }
        });
        Glide.with(context)
                .load(Constant.SERVER_URL + subCategory.getImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.image);
        if (subCategory.getType() != null) {
            if (subCategory.getType() == 1) {
                Glide.with(context)
                        .load("")
                        .placeholder(R.drawable.ic_baseline_more_horiz_24)
                        .error(R.drawable.ic_baseline_more_horiz_24)
                        .into(holder.image);
                holder.image.setPadding((int)context.getResources().getDimension(R.dimen.image_padding),
                        (int)context.getResources().getDimension(R.dimen.image_padding),
                        (int)context.getResources().getDimension(R.dimen.image_padding),
                        (int)context.getResources().getDimension(R.dimen.image_padding));
            }
        }


        if (Utils.getLanguage(context).equals("ru")) {
            holder.textView.setText(subCategory.getTitle_ru());
        }
    }

    @Override
    public int getItemCount() {
        return subCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView image;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.expandedListItem);
            image = itemView.findViewById(R.id.image);
        }
    }
}
