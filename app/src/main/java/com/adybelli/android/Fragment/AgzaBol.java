package com.adybelli.android.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Object.SignInPost;
import com.adybelli.android.Object.UserVerificationBody;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.google.android.material.textfield.TextInputEditText;
import com.kaopiz.kprogresshud.KProgressHUD;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AgzaBol extends Fragment {

    private TextView login_desc, showPassword, kod_gelmedi, t4, t5, t3, motiv_title, motiv_desc, term_1;
    private Button acceptBtn;
    private TextInputEditText code_input;
    private View view;
    private Context context;
    private CheckBox acceptCheck;
    private ApiInterface apiInterface;
    private KProgressHUD kProgressHUD = null;

    public AgzaBol() {
    }

    public static AgzaBol newInstance() {
        AgzaBol fragment = new AgzaBol();
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
        view = localInflater.inflate(R.layout.fragment_agza_bol, container, false);
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
//                if (!acceptCheck.isChecked()) {
//                    Toast.makeText(context, Utils.getStringResource(R.string.check_terms_of_use, context), Toast.LENGTH_SHORT).show();
//                    return;
//                }
                kProgressHUD.show();
                apiInterface = APIClient.getClient().create(ApiInterface.class);
                SignInPost post = new SignInPost("+9936"+code_input.getText().toString(), "",1);
                Call<UserVerificationBody> call = apiInterface.userVerification(post);
                call.enqueue(new Callback<UserVerificationBody>() {
                    @Override
                    public void onResponse(Call<UserVerificationBody> call, Response<UserVerificationBody> response) {
                        kProgressHUD.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body().getBody().equals("success")) {
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                CodeVerificationLogin dialogFragment = new CodeVerificationLogin("+9936"+code_input.getText().toString(), 2);
                                dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                                dialogFragment.show(transaction, CodeVerificationLogin.class.getSimpleName());
                            } else {
                                Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong,context),R.drawable.ic_baseline_close_24,context,R.drawable.toast_bg_error);
                                kProgressHUD.dismiss();
                            }
                        } else {
                            kProgressHUD.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserVerificationBody> call, Throwable t) {
                        Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong,context),R.drawable.ic_baseline_close_24,context,R.drawable.toast_bg_error);
                        kProgressHUD.dismiss();
                        t.printStackTrace();
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

    private void initComponents() {
        code_input = view.findViewById(R.id.code_input);
        login_desc = view.findViewById(R.id.login_desc);
        kod_gelmedi = view.findViewById(R.id.kod_gelmedi);
        t4 = view.findViewById(R.id.t4);
        t5 = view.findViewById(R.id.t5);
        motiv_title = view.findViewById(R.id.motiv_title);
        motiv_desc = view.findViewById(R.id.motiv_desc);
        term_1 = view.findViewById(R.id.term_1);
        acceptBtn = view.findViewById(R.id.acceptBtn);
        acceptCheck = view.findViewById(R.id.acceptCheck);
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
        t4.setTypeface(AppFont.getRegularFont(context));
        t5.setTypeface(AppFont.getRegularFont(context));
        motiv_title.setTypeface(AppFont.getBoldFont(context));
        motiv_desc.setTypeface(AppFont.getRegularFont(context));
        term_1.setTypeface(AppFont.getRegularFont(context));
        acceptBtn.setTypeface(AppFont.getBoldFont(context));
        code_input.setTypeface(AppFont.getRegularFont(context));
    }
}