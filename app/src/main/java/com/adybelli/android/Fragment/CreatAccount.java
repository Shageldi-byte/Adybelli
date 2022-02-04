package com.adybelli.android.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Object.SignInPost;
import com.adybelli.android.Object.UserVerificationBody;
import com.adybelli.android.Post.CreateAccountPost;
import com.adybelli.android.Post.CreateAccountResponse;
import com.adybelli.android.Post.CreateAccountResponseBody;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.google.android.material.textfield.TextInputEditText;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreatAccount extends DialogFragment {

    private RadioButton man, women;
    private View view;
    private Button acceptBtn;
    private TextView userTitle, desc, genderTitle;
    private TextInputEditText name_surname, surname,phone_input;
    private Context context;
    private int gender = 2;
    private ApiInterface apiInterface;
    private KProgressHUD kProgressHUD = null;
    private CheckBox acceptCheck;
    private TextView t4,t5;
    public CreatAccount() {
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
        view = localInflater.inflate(R.layout.fragment_creat_account, container, false);
        context = getContext();
        initComponents();
        setListeners();
        setFonts();
        if(!Utils.getSharedPreference(context,"gender").isEmpty()){
            try{
                gender=Integer.parseInt(Utils.getSharedPreference(context,"gender"));
                if(gender==1){
                    women.setChecked(true);
                    man.setChecked(false);
                } else if(gender==2){
                    women.setChecked(false);
                    man.setChecked(true);
                }
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return view;
    }

    private void initComponents() {
        man = view.findViewById(R.id.man);
        women = view.findViewById(R.id.women);
        phone_input = view.findViewById(R.id.phone_input);
        acceptBtn = view.findViewById(R.id.acceptBtn);
        userTitle = view.findViewById(R.id.userTitle);
        desc = view.findViewById(R.id.desc);
        genderTitle = view.findViewById(R.id.genderTitle);
        name_surname = view.findViewById(R.id.name_surname);
        surname = view.findViewById(R.id.surname);
        acceptCheck = view.findViewById(R.id.acceptCheck);
        t4 = view.findViewById(R.id.t4);
        t5 = view.findViewById(R.id.t5);
        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setWindowColor(getResources().getColor(R.color.transparentBlack))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }

    private void setListeners() {
        man.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    women.setChecked(false);
                }
            }
        });

        women.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    man.setChecked(false);
                }
            }
        });

        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender=2;
            }
        });

        women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender=1;
            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(name_surname.getText().toString().trim().isEmpty() || surname.getText().toString().trim().isEmpty() || phone_input.getText().toString().length()<7){
                    Utils.showCustomToast(Utils.getStringResource(R.string.fill_out, context),R.drawable.ic_outline_info_24,context,R.drawable.toast_bg);
                    return;
                }
                String ph="+9936"+phone_input.getText().toString();
                if (!acceptCheck.isChecked()) {
                    Toast.makeText(context, Utils.getStringResource(R.string.check_terms_of_use, context), Toast.LENGTH_SHORT).show();
                    return;
                }
                kProgressHUD.show();
                apiInterface = APIClient.getClient().create(ApiInterface.class);
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
                                sendCode();
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

        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.bottomSheet(context, Utils.getLanguage(context));
            }
        });
    }

    private void sendCode() {
        apiInterface= APIClient.getClient().create(ApiInterface.class);
        SignInPost post=new SignInPost("+9936"+phone_input.getText().toString(),"",0);
        Call<UserVerificationBody> call=apiInterface.userVerification(post,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
        call.enqueue(new Callback<UserVerificationBody>() {
            @Override
            public void onResponse(Call<UserVerificationBody> call, Response<UserVerificationBody> response) {
                if(response.isSuccessful()){
                    if(!response.body().isError() && response.body().getBody().equals("not_exists")){
                        FragmentTransaction transaction = getFragmentManager()
                                .beginTransaction();

                        CodeVerificationSignUp dialogFragment = new CodeVerificationSignUp("+9936"+phone_input.getText().toString(),name_surname.getText().toString(),
                                surname.getText().toString(),gender);
                        dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                        dialogFragment.show(transaction, CodeVerificationLogin.class.getSimpleName());
                    } else {
                        kProgressHUD.dismiss();
                        Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong,context),R.drawable.ic_baseline_close_24,context,R.drawable.toast_bg_error);
                        call.cancel();
                    }
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

    private void setFonts() {
        man.setTypeface(AppFont.getRegularFont(context));
        women.setTypeface(AppFont.getRegularFont(context));
        acceptBtn.setTypeface(AppFont.getBoldFont(context));
        userTitle.setTypeface(AppFont.getBoldFont(context));
        desc.setTypeface(AppFont.getRegularFont(context));
        genderTitle.setTypeface(AppFont.getBoldFont(context));
        name_surname.setTypeface(AppFont.getRegularFont(context));
        surname.setTypeface(AppFont.getRegularFont(context));
        t4.setTypeface(AppFont.getRegularFont(context));
        t5.setTypeface(AppFont.getRegularFont(context));
    }
}