package com.adybelli.android.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Object.SignInBody;
import com.adybelli.android.Object.SignInPost;
import com.adybelli.android.Object.SignInResponse;
import com.adybelli.android.Object.UserVerificationBody;
import com.adybelli.android.Post.VerifyUserResponse;
import com.adybelli.android.Post.VerifyUserResponseBody;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.google.android.material.textfield.TextInputEditText;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CodeVerificationLogin extends DialogFragment {


    private TextView login_desc, kod_gelmedi, t5, motiv_title, motiv_desc, term_1;
    private Button acceptBtn;
    private TextInputEditText code_input;
    private View view;
    private Context context;
    private String phoneNumber;
    private int which;
    private ApiInterface apiInterface;
    private KProgressHUD kProgressHUD = null;
    private CountDownTimer countDownTimer;
    private ImageView close;

    public CodeVerificationLogin(String phoneNumber, int which) {
        this.phoneNumber = phoneNumber;
        this.which = which;
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
        view = localInflater.inflate(R.layout.fragment_code_verification_login, container, false);
        context = getContext();
        initComponents();
        setFonts();
        setListener();
        return view;
    }

    private void setListener() {
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (code_input.getText().toString().length() < 4) {
                    Utils.showCustomToast(getResources().getString(R.string.fill_out), R.drawable.ic_outline_info_24, context, R.drawable.toast_bg);
                    return;
                }
                kProgressHUD.show();
                ApiInterface apiInterfac = APIClient.getClient().create(ApiInterface.class);
                SignInPost post = new SignInPost(phoneNumber, code_input.getText().toString(), 0);
                Call<SignInResponse> call = apiInterfac.signIn(post,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
                call.enqueue(new Callback<SignInResponse>() {
                    @Override
                    public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                        kProgressHUD.dismiss();
                        if (response.isSuccessful()) {
                            SignInBody res = response.body().getBody();
                            if (!response.body().isError() && res != null) {
                                Utils.setSharedPreference(context, "tkn", res.getToken());
                                Utils.setSharedPreference(context, "userId", res.getUser_id());
                                Utils.setSharedPreference(context, "user_phone", res.getPhone());
                                Utils.setSharedPreference(context, "user_first_name", res.getName());
                                Utils.setSharedPreference(context, "user_last_name", res.getSurname());
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.layout, new ProfilePage());
                                transaction.commit();
                                getDialog().dismiss();
                            }
                        } else {
                            Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong, context), R.drawable.ic_baseline_close_24, context, R.drawable.toast_bg_error);
                            getDialog().dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<SignInResponse> call, Throwable t) {
                        kProgressHUD.dismiss();
                        t.printStackTrace();
                        call.cancel();
                    }
                });
            }
        });

        countDownTimer = new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                String time = String.format("%02d:%02d", ((millisUntilFinished / 1000) % 3600) / 60, ((millisUntilFinished / 1000) % 60));
                t5.setText(time);
            }

            public void onFinish() {
                t5.setText(Utils.getStringResource(R.string.kod_gelmedi, context));
                t5.setPaintFlags(t5.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                t5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userVerification();
                    }
                });
            }
        }.start();


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
    }

    private void initComponents() {
        code_input = view.findViewById(R.id.code_input);
        login_desc = view.findViewById(R.id.login_desc);
        kod_gelmedi = view.findViewById(R.id.kod_gelmedi);
        t5 = view.findViewById(R.id.t5);
        motiv_title = view.findViewById(R.id.motiv_title);
        motiv_desc = view.findViewById(R.id.motiv_desc);
        term_1 = view.findViewById(R.id.term_1);
        acceptBtn = view.findViewById(R.id.acceptBtn);
        close = view.findViewById(R.id.close);
        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setWindowColor(getResources().getColor(R.color.transparentBlack))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }

    private void setFonts() {
        login_desc.setTypeface(AppFont.getRegularFont(context));
        kod_gelmedi.setTypeface(AppFont.getRegularFont(context));
        t5.setTypeface(AppFont.getRegularFont(context));
        motiv_title.setTypeface(AppFont.getBoldFont(context));
        motiv_desc.setTypeface(AppFont.getRegularFont(context));
        term_1.setTypeface(AppFont.getRegularFont(context));
        acceptBtn.setTypeface(AppFont.getBoldFont(context));
        code_input.setTypeface(AppFont.getRegularFont(context));
    }

    private void userVerification() {
        kProgressHUD.show();
        apiInterface = APIClient.getClient().create(ApiInterface.class);
        SignInPost post = new SignInPost(phoneNumber, "",0);
        Call<UserVerificationBody> call = apiInterface.userVerification(post,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
        call.enqueue(new Callback<UserVerificationBody>() {
            @Override
            public void onResponse(Call<UserVerificationBody> call, Response<UserVerificationBody> response) {
                kProgressHUD.dismiss();
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        countDownTimer.start();
                        t5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                    } else{
                        Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong, context), R.drawable.ic_baseline_close_24, context, R.drawable.toast_bg_error);
                        kProgressHUD.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserVerificationBody> call, Throwable t) {
                kProgressHUD.dismiss();
                t.printStackTrace();
                call.cancel();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}