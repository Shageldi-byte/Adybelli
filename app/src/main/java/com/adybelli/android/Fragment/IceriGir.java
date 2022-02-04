package com.adybelli.android.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Object.SignInPost;
import com.adybelli.android.Object.UserVerificationBody;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class IceriGir extends Fragment {

    private TextView login_desc,t1,t2,t3,motiv_title,motiv_desc,term_1;
    private Button acceptBtn;
        private TextInputEditText phone_input;
    private View view;
    private Context context;
    private ApiInterface apiInterface;
    private KProgressHUD kProgressHUD=null;
    private TextInputLayout phone;

    public IceriGir() {
    }


    public static IceriGir newInstance() {
        IceriGir fragment = new IceriGir();
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
        view=localInflater.inflate(R.layout.fragment_iceri_gir, container, false);
        context=getContext();
        initComponents();
        setFonts();
        setListeners();
        return view;
    }
    private void initComponents(){
        login_desc=view.findViewById(R.id.login_desc);
        t1=view.findViewById(R.id.t1);
        t2=view.findViewById(R.id.t2);
        t3=view.findViewById(R.id.t3);
        motiv_title=view.findViewById(R.id.motiv_title);
        motiv_desc=view.findViewById(R.id.motiv_desc);
        term_1=view.findViewById(R.id.term_1);
        acceptBtn=view.findViewById(R.id.acceptBtn);
        phone_input=view.findViewById(R.id.phone_input);
        phone=view.findViewById(R.id.phone);

        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setWindowColor(getResources().getColor(R.color.transparentBlack))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }

    private void setFonts(){
        login_desc.setTypeface(AppFont.getRegularFont(context));
        t1.setTypeface(AppFont.getRegularFont(context));
        t2.setTypeface(AppFont.getRegularFont(context));
        t3.setTypeface(AppFont.getRegularFont(context));
        motiv_title.setTypeface(AppFont.getBoldFont(context));
        motiv_desc.setTypeface(AppFont.getRegularFont(context));
        term_1.setTypeface(AppFont.getRegularFont(context));
        acceptBtn.setTypeface(AppFont.getBoldFont(context));
        phone_input.setTypeface(AppFont.getRegularFont(context));
        phone.setTypeface(AppFont.getRegularFont(context));
    }

    private void setListeners(){
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kProgressHUD.show();
                apiInterface= APIClient.getClient().create(ApiInterface.class);
                SignInPost post=new SignInPost("+9936"+phone_input.getText().toString(),"",1);
                Call<UserVerificationBody> call=apiInterface.userVerification(post,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
                call.enqueue(new Callback<UserVerificationBody>() {
                    @Override
                    public void onResponse(Call<UserVerificationBody> call, Response<UserVerificationBody> response) {
                        kProgressHUD.dismiss();
                        if(response.isSuccessful()){

                            if(response.body().getBody().equals("exists")){
                                kProgressHUD.dismiss();
                                FragmentTransaction transaction = getFragmentManager()
                                        .beginTransaction();
                                CodeVerificationLogin dialogFragment = new CodeVerificationLogin("+9936"+phone_input.getText().toString(),1);
                                dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                                dialogFragment.show(transaction, CodeVerificationLogin.class.getSimpleName());
                            } else if(response.body().getBody().equals("not_exists")) {
                                showCreateAccount();
                                kProgressHUD.dismiss();
                            }
                        } else {
                            Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong,context),R.drawable.ic_baseline_close_24,context,R.drawable.toast_bg_error);
                            kProgressHUD.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserVerificationBody> call, Throwable t) {
                        kProgressHUD.dismiss();
                        t.printStackTrace();
                        Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong,context),R.drawable.ic_baseline_close_24,context,R.drawable.toast_bg_error);
                        call.cancel();
                    }
                });
            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.bottomSheet(context,Utils.getLanguage(context));
            }
        });
    }



    private void showCreateAccount() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(context.getResources().getString(R.string.not_found_account));
        alert.setMessage(context.getResources().getString(R.string.do_you_want_sign_up));
        alert.setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Login.get().getTabLayout().selectTab(Login.get().getTabLayout().getTabAt(1));
            }
        });
        alert.show();
    }

    private void setUnderineText(TextView view){
        view.setPaintFlags(view.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }


}