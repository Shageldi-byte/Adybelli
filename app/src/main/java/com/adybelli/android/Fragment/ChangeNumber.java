package com.adybelli.android.Fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Object.UpdateMailResponse;
import com.adybelli.android.Post.UpdateMailPost;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.inappmessaging.internal.ApiClient;
import com.kaopiz.kprogresshud.KProgressHUD;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangeNumber extends Fragment {
    private TextView title1, desc;
    private Button acceptBtn;
    private TextInputEditText number, code;
    private View view;
    private Context context;
    public static String email = "";
    public static boolean isConnected = false;
    private ApiInterface apiInterface;
    private KProgressHUD kProgressHUD = null;
    private ProgressBar progress;
    private ScrollView container;
    public ChangeNumber() {
    }

    public static ChangeNumber newInstance() {
        ChangeNumber fragment = new ChangeNumber();
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
        view = localInflater.inflate(R.layout.fragment_change_number, container, false);
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
                if (!number.getText().toString().isEmpty()) {
                    kProgressHUD.show();
                    apiInterface = APIClient.getClient().create(ApiInterface.class);
                    UpdateMailPost post = new UpdateMailPost(number.getText().toString());
                    Call<UpdateMailResponse> call = apiInterface.updateMail("Bearer " + Utils.getSharedPreference(context, "tkn"), post);
                    call.enqueue(new Callback<UpdateMailResponse>() {
                        @Override
                        public void onResponse(Call<UpdateMailResponse> call, Response<UpdateMailResponse> response) {
                            if (response.isSuccessful()) {
//                                Utils.showCustomToast(Utils.getStringResource(R.string.link_to_mail, context), R.drawable.ic_baseline_check_circle_outline_24, context, R.drawable.toast_bg_success);
                                showDialog();
                            } else {
                                Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong, context), R.drawable.ic_baseline_close_24, context, R.drawable.toast_bg_error);
                            }
                            kProgressHUD.dismiss();
                        }

                        @Override
                        public void onFailure(Call<UpdateMailResponse> call, Throwable t) {
                            Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong, context), R.drawable.ic_baseline_close_24, context, R.drawable.toast_bg_error);
                            kProgressHUD.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void initComponents() {
        container = view.findViewById(R.id.container);
        progress = view.findViewById(R.id.progress);
        title1 = view.findViewById(R.id.title1);
        desc = view.findViewById(R.id.desc);
        acceptBtn = view.findViewById(R.id.acceptBtn);
        number = view.findViewById(R.id.number);
        code = view.findViewById(R.id.code);

        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setWindowColor(getResources().getColor(R.color.transparentBlack))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }

    private void setFonts() {
        title1.setTypeface(AppFont.getBoldFont(context));
        desc.setTypeface(AppFont.getRegularFont(context));
        acceptBtn.setTypeface(AppFont.getBoldFont(context));
        number.setTypeface(AppFont.getRegularFont(context));
        code.setTypeface(AppFont.getRegularFont(context));
    }

    private void showDialog(){
        Dialog dialog = new Dialog(context);
        final Context contextThemeWrapper = new ContextThemeWrapper(((Activity) context), R.style.Material);
        LayoutInflater localInflater = LayoutInflater.from(context).cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.custom_dialog, null, false);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;

        ImageView close = dialog.findViewById(R.id.close);
        Button okbutton = dialog.findViewById(R.id.okbutton);
        TextView desc = dialog.findViewById(R.id.desc);
        TextView title2 = dialog.findViewById(R.id.title2);

        title2.setTypeface(AppFont.getBoldFont(context));
        desc.setTypeface(AppFont.getRegularFont(context));
        okbutton.setTypeface(AppFont.getSemiBoldFont(context));

        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                getActivity().onBackPressed();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        email="";
        isConnected=false;
    }

    @Override
    public void onResume() {
        super.onResume();
        number.setText(email);
        if(isConnected){
            progress.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
        } else{
            progress.setVisibility(View.VISIBLE);
            container.setVisibility(View.GONE);
        }
    }
}