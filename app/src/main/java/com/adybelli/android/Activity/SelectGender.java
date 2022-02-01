package com.adybelli.android.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adybelli.android.Common.Utils;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;

public class SelectGender extends AppCompatActivity {
    private Button save;
    private TextView select_gender_title,manTitle,womenTitle;
    private RelativeLayout manCon,womenCon;
    private int gender=2;
    private Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_gender);
        initComponents();
        setListener();
        setFonts();
    }

    private void setFonts() {
        save.setTypeface(AppFont.getSemiBoldFont(context));
        select_gender_title.setTypeface(AppFont.getSemiBoldFont(context));
        manTitle.setTypeface(AppFont.getRegularFont(context));
        womenTitle.setTypeface(AppFont.getRegularFont(context));
    }

    private void setListener() {
        manCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manCon.setBackgroundResource(R.drawable.select_gender_active);
                womenCon.setBackgroundResource(R.drawable.select_gender_passive);
                manTitle.setTextColor(getResources().getColor(R.color.colorAccent));
                womenTitle.setTextColor(getResources().getColor(R.color.description));
                gender=2;
            }
        });

        womenCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manCon.setBackgroundResource(R.drawable.select_gender_passive);
                womenCon.setBackgroundResource(R.drawable.select_gender_active);
                manTitle.setTextColor(getResources().getColor(R.color.description));
                womenTitle.setTextColor(getResources().getColor(R.color.colorAccent));
                gender=1;
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.setSharedPreference(context,"gender",""+gender);
                finish();
            }
        });
    }

    private void initComponents() {
        save=findViewById(R.id.save);
        select_gender_title=findViewById(R.id.select_gender_title);
        manTitle=findViewById(R.id.manTitle);
        womenTitle=findViewById(R.id.womenTitle);
        manCon=findViewById(R.id.manCon);
        womenCon=findViewById(R.id.womenCon);
    }
}