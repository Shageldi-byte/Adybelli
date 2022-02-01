package com.adybelli.android.CategoryAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adybelli.android.CategoryModel.Brands;
import com.adybelli.android.CategoryModel.Default;
import com.adybelli.android.Fragment.Products;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {

    private ArrayList<Brands> arrayList=new ArrayList<>();
    private Context context;

    public BrandAdapter(ArrayList<Brands> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.default_filter_item, parent,false);
        return new BrandAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BrandAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Brands object=arrayList.get(position);
        if(Products.brandsStr.contains(object.getTm_id()+"")){
            holder.title.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.check.setVisibility(View.VISIBLE);
            holder.title.setTypeface(AppFont.getBoldFont(context));
        }
        holder.title.setText(object.getTitle());
        holder.title.setTypeface(AppFont.getSemiBoldFont(context));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.check.getVisibility()==View.GONE){
                    holder.title.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    holder.check.setVisibility(View.VISIBLE);
                    holder.title.setTypeface(AppFont.getBoldFont(context));
                    Products.brandsStr.add(object.getTm_id()+"");
                } else if(holder.check.getVisibility()==View.VISIBLE){
                    holder.title.setTextColor(context.getResources().getColor(R.color.black));
                    holder.check.setVisibility(View.GONE);
                    holder.title.setTypeface(AppFont.getSemiBoldFont(context));
                    Products.brandsStr.remove(object.getTm_id()+"");
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView check;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            check=itemView.findViewById(R.id.check);
            title=itemView.findViewById(R.id.title);
        }
    }
}
