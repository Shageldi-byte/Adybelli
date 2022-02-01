package com.adybelli.android.View;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adybelli.android.R;

import java.util.ArrayList;

public class Custom_Spinner extends ArrayAdapter {
    public Custom_Spinner(@NonNull Context context, ArrayList<String> customlist) {
        super(context, 0, customlist);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.custom_2,parent,false);

        }
        String item= (String) getItem(position);

        TextView spinnerTv=convertView.findViewById(R.id.tvSpinnerLayout);
        if (item!=null) {

            spinnerTv.setText(item);
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.custom_dropdown_layout,parent,false);

        }
        String item= (String) getItem(position);

        TextView dropDownTV=convertView.findViewById(R.id.tvDropDownLayout);
        if (item!=null) {

            dropDownTV.setText(item);
        }
        return convertView;
    }
}
