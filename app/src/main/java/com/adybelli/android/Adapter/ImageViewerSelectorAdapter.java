package com.adybelli.android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.adybelli.android.R;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ImageViewerSelectorAdapter extends RecyclerView.Adapter<ImageViewerSelectorAdapter.ViewHolder> {
    private ArrayList<String> images=new ArrayList<>();
    private Context context;
    private ViewPager pager;
    private LinearLayout oldUnselect=null;
    public ImageViewerSelectorAdapter(ArrayList<String> images, Context context, ViewPager pager) {
        this.images = images;
        this.context = context;
        this.pager = pager;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.image_selector,parent,false);
        return new ImageViewerSelectorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ImageViewerSelectorAdapter.ViewHolder holder, int position) {
        String img=images.get(position);
        Glide.with(context)
                .load(img)
                .into(holder.imageView);
        if(position==0 && oldUnselect==null){
            oldUnselect=holder.unselect;
            holder.unselect.setVisibility(View.GONE);
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldUnselect.setVisibility(View.VISIBLE);
                oldUnselect=holder.unselect;
                holder.unselect.setVisibility(View.GONE);
                pager.setCurrentItem(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        LinearLayout unselect;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image);
            unselect=itemView.findViewById(R.id.unselect);
        }
    }


}
