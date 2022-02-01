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
import android.widget.Toast;

import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Object.SignInPost;
import com.adybelli.android.Object.SignInResponse;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CodeVerification extends Fragment {
    private TextView login_desc,kod_gelmedi,t5,motiv_title,motiv_desc,term_1;
    private Button acceptBtn;
    private TextInputEditText code_input;
    private View view;
    private Context context;
    private String phoneNumber;


    public CodeVerification() {
    }


    public static CodeVerification newInstance(String param1, String param2) {
        CodeVerification fragment = new CodeVerification();
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

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        view=localInflater.inflate(R.layout.fragment_code_verification, container, false);
        context=getContext();
        initComponents();
        setFonts();
        setListener();
        return view;
    }

    private void setListener() {
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiInterface apiInterfac= APIClient.getClient().create(ApiInterface.class);

            }
        });
    }

    private void initComponents(){
        code_input=view.findViewById(R.id.code_input);
        login_desc=view.findViewById(R.id.login_desc);
        kod_gelmedi=view.findViewById(R.id.kod_gelmedi);
        t5=view.findViewById(R.id.t5);
        motiv_title=view.findViewById(R.id.motiv_title);
        motiv_desc=view.findViewById(R.id.motiv_desc);
        term_1=view.findViewById(R.id.term_1);
        acceptBtn=view.findViewById(R.id.acceptBtn);
    }

    private void setFonts(){
        login_desc.setTypeface(AppFont.getRegularFont(context));
        kod_gelmedi.setTypeface(AppFont.getRegularFont(context));
        t5.setTypeface(AppFont.getRegularFont(context));
        motiv_title.setTypeface(AppFont.getBoldFont(context));
        motiv_desc.setTypeface(AppFont.getRegularFont(context));
        term_1.setTypeface(AppFont.getRegularFont(context));
        acceptBtn.setTypeface(AppFont.getBoldFont(context));
        code_input.setTypeface(AppFont.getRegularFont(context));
    }
}