package com.adybelli.android.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.google.android.material.textfield.TextInputEditText;


public class ChangePassword extends Fragment {

    private TextView title1,desc;
    private Button acceptBtn;
    private TextInputEditText currentPassword,newPassword,acceptPassword;
    private View view;
    private Context context;
    public ChangePassword() {
    }


    public static ChangePassword newInstance() {
        ChangePassword fragment = new ChangePassword();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.Material);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        view=localInflater.inflate(R.layout.fragment_change_password, container, false);
        context=getContext();
        initComponents();
        setFonts();
        return view;
    }

    private void initComponents(){
        title1=view.findViewById(R.id.title1);
        desc=view.findViewById(R.id.desc);
        acceptBtn=view.findViewById(R.id.acceptBtn);
        currentPassword=view.findViewById(R.id.currentPassword);
        newPassword=view.findViewById(R.id.newPassword);
        acceptPassword=view.findViewById(R.id.acceptPassword);
    }

    private void setFonts(){
        title1.setTypeface(AppFont.getBoldFont(context));
        desc.setTypeface(AppFont.getRegularFont(context));
        acceptBtn.setTypeface(AppFont.getBoldFont(context));
        currentPassword.setTypeface(AppFont.getRegularFont(context));
        newPassword.setTypeface(AppFont.getRegularFont(context));
        acceptPassword.setTypeface(AppFont.getRegularFont(context));
    }
}