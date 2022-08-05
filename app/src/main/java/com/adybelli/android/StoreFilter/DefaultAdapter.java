package com.adybelli.android.StoreFilter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adybelli.android.CategoryModel.Cost;
import com.adybelli.android.Fragment.Products;
import com.adybelli.android.Fragment.Store.StoreProducts;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DefaultAdapter extends RecyclerView.Adapter<DefaultAdapter.ViewHolder> {

    private ArrayList<Cost> arrayList=new ArrayList<>();
    private Context context;
    private TextView oldTitle;
    private ImageView oldCheck;
    private FragmentManager fragmentManager;
    private EditText min,max;
    public DefaultAdapter(ArrayList<Cost> arrayList, Context context,FragmentManager fragmentManager,EditText min,EditText max) {
        this.arrayList = arrayList;
        this.context = context;
        this.fragmentManager=fragmentManager;
        this.min=min;
        this.max=max;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.default_filter_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DefaultAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Cost object=arrayList.get(position);
        holder.title.setText(object.getMin()+" TMT - "+object.getMax()+" TMT");
        holder.title.setTypeface(AppFont.getSemiBoldFont(context));
        holder.setIsRecyclable(false);
        try {
            if (StoreProducts.min.equals(object.getMin()) && StoreProducts.max.equals(object.getMax())) {
                min.setText(object.getMin()+"");
                max.setText(object.getMax()+"");
                StoreProducts.min=object.getMin();
                StoreProducts.max=object.getMax();
                setActive(holder.title,holder.check);
                oldTitle=holder.title;
                oldCheck=holder.check;

            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActive(holder.title,holder.check);

                min.setText(object.getMin()+"");
                max.setText(object.getMax()+"");
                StoreProducts.min=object.getMin();
                StoreProducts.max=object.getMax();
                if(oldCheck!=null){
                    setPassive(oldTitle,oldCheck);
                }

                oldTitle=holder.title;
                oldCheck=holder.check;


            }
        });
    }

    private void setActive(TextView title,ImageView check){
        title.setTextColor(context.getResources().getColor(R.color.colorAccent));
        check.setVisibility(View.VISIBLE);
        title.setTypeface(AppFont.getBoldFont(context));
    }

    private void setPassive(TextView title,ImageView check){
        title.setTextColor(context.getResources().getColor(R.color.black));
        check.setVisibility(View.GONE);
        title.setTypeface(AppFont.getSemiBoldFont(context));
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
