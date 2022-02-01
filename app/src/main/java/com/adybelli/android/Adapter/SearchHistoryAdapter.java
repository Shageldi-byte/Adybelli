package com.adybelli.android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adybelli.android.DataBaseHelper.SearchDB;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {
    private ArrayList<String> arrayList=new ArrayList<>();
    private Context context;
    private SearchDB searchDB;
    private EditText editText;
    public SearchHistoryAdapter(ArrayList<String> arrayList, Context context,EditText editText) {
        this.arrayList = arrayList;
        this.context = context;
        this.editText=editText;
        searchDB=new SearchDB(context);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.search_history_design,parent,false);
        return new SearchHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchHistoryAdapter.ViewHolder holder, int position) {
        try {
            holder.query.setText(arrayList.get(holder.getAdapterPosition()));
            holder.query.setTypeface(AppFont.getRegularFont(context));
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    searchDB.deleteData(arrayList.get(holder.getAdapterPosition()));
                    arrayList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                }
            });
            holder.query.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editText.setText(arrayList.get(holder.getAdapterPosition()));
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
        TextView query;
        ImageView delete;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            query=itemView.findViewById(R.id.query);
            delete=itemView.findViewById(R.id.delete);
        }
    }
}
