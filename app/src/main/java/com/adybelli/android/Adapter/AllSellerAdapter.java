package com.adybelli.android.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Activity.StorePage;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Fragment.Products;
import com.adybelli.android.Object.Store;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AllSellerAdapter extends RecyclerView.Adapter<AllSellerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Store> arrayList=new ArrayList<>();
    private FragmentManager supportFragmentManager;
    private Activity activity;
    public AllSellerAdapter(Context context, ArrayList<Store> arrayList, FragmentManager supportFragmentManager, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.supportFragmentManager=supportFragmentManager;
        this.activity=activity;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.seller_design, parent,false);
        return new AllSellerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AllSellerAdapter.ViewHolder holder, int position) {
        Store object=arrayList.get(position);
        holder.title.setTypeface(AppFont.getBoldFont(context));
        holder.title.setText(object.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, StorePage.class);
                context.startActivity(intent);
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
