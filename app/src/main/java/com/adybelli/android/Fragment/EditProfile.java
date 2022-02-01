package com.adybelli.android.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Object.GetUserById;
import com.adybelli.android.Object.GetUserByIdBody;
import com.adybelli.android.Object.UpdateUserInfoPost;
import com.adybelli.android.Object.UpdateUserResponse;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.google.android.material.textfield.TextInputEditText;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditProfile extends Fragment {

    private RadioButton man, women;
    private View view;
    private Button acceptBtn;
    private TextView userTitle, desc, genderTitle, birthdayDesc, dateOfBirth;
    private TextInputEditText name_surname, username, phone, mail;
    private LinearLayout birthday;
    private Context context;
    private ApiInterface apiInterface;
    private KProgressHUD kProgressHUD;
    private String dateBirthday = "";
    private int gender = 2;
    private Date newDate= null;
    int day =0;
    int month = 0;
    int year =0;
    private LinearLayout container;
    private ProgressBar progress;
    private LinearLayout errorContainer;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button errorButton;
    public EditProfile() {
    }


    public static EditProfile newInstance() {
        EditProfile fragment = new EditProfile();
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
        view = localInflater.inflate(R.layout.fragment_edit_profile, container, false);
        context = getContext();
        initComponents();
        getUserData();
        setListeners();
        setFonts();
        return view;
    }

    private void getUserData() {
        errorContainer.setVisibility(View.GONE);
        Profile.get().getTabLayout().setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<GetUserById> call = apiInterface.getUserById("Bearer " + Utils.getSharedPreference(context, "tkn"));
        call.enqueue(new Callback<GetUserById>() {
            @Override
            public void onResponse(Call<GetUserById> call, Response<GetUserById> response) {
                if (response.isSuccessful()) {
                    Profile.get().getTabLayout().setVisibility(View.VISIBLE);
                    container.setVisibility(View.VISIBLE);
                    GetUserByIdBody body = response.body().getBody();
                    name_surname.setText(body.getName());
                    username.setText(body.getSurname());
                    phone.setText(body.getPhone());
                    if (body.getGender().equals("1")) {
                        women.setChecked(true);
                    } else if (body.getGender().equals("2")) {
                        man.setChecked(true);
                    }
                    mail.setText(body.getEmail());
                    if (body.getDate_birth() != null) {
                        String date=body.getDate_birth();
                        SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.ss");
                        try {
                            newDate = spf.parse(date);
                            spf= new SimpleDateFormat("dd.MM.yyyy");
                            date = spf.format(newDate);
                            dateOfBirth.setText(date);
                            dateBirthday = date;

                            spf= new SimpleDateFormat("dd");
                            date = spf.format(newDate);
                            day=Integer.valueOf(date);
                            spf= new SimpleDateFormat("MM");
                            date = spf.format(newDate);
                            month=Integer.valueOf(date);
                            spf= new SimpleDateFormat("yyyy");
                            date = spf.format(newDate);
                            year=Integer.valueOf(date);
                            dateBirthday = year + "/" + month + "/" + day;
                            dateOfBirth.setText(dateBirthday);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Calendar c = Calendar.getInstance();
                        day=c.get(Calendar.DAY_OF_MONTH);
                        month=c.get(Calendar.MONTH)+1;
                        year=c.get(Calendar.YEAR);
                    }
                    ChangeNumber.email = body.getEmail();
                    ChangeNumber.isConnected=true;

                } else{
                    showErrorView(R.drawable.no_connection, context.getResources().getString(R.string.noInternet), getResources().getString(R.string.checkYourInternet), getResources().getString(R.string.continueValue));
                    ChangeNumber.isConnected=false;
                    ChangeNumber.email = "";
                }
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GetUserById> call, Throwable t) {
                ChangeNumber.isConnected=false;
                ChangeNumber.email = "";
                showErrorView(R.drawable.no_connection, context.getResources().getString(R.string.noInternet), getResources().getString(R.string.checkYourInternet), getResources().getString(R.string.continueValue));
//                Utils.removeShow(new ProfileRootFragment(), ProfileRootFragment.class.getSimpleName(), getParentFragmentManager(), R.id.content);
//                MainActivity.fifthFragment = new ProfileRootFragment();
            }
        });
    }

    private void initComponents() {
        errorContainer = view.findViewById(R.id.errorContainer);
        errorTitle = view.findViewById(R.id.errorTitle);
        errorMessage = view.findViewById(R.id.errorMessage);
        errorButton = view.findViewById(R.id.errorButton);
        errorImage = view.findViewById(R.id.errorImage);
        container = view.findViewById(R.id.container);
        progress = view.findViewById(R.id.progress);
        man = view.findViewById(R.id.man);
        women = view.findViewById(R.id.women);
        acceptBtn = view.findViewById(R.id.acceptBtn);
        userTitle = view.findViewById(R.id.userTitle);
        desc = view.findViewById(R.id.desc);
        genderTitle = view.findViewById(R.id.genderTitle);
        birthdayDesc = view.findViewById(R.id.birthdayDesc);
        name_surname = view.findViewById(R.id.name_surname);
        username = view.findViewById(R.id.username);
        phone = view.findViewById(R.id.phone);
        mail = view.findViewById(R.id.mail);
        birthday = view.findViewById(R.id.birthday);
        dateOfBirth = view.findViewById(R.id.dateOfBirth);

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
                    gender = 2;
                }
            }
        });

        women.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    man.setChecked(false);
                    gender = 1;
                }
            }
        });

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                    Calendar c = Calendar.getInstance();
                    if (newDate == null) {
                        day=c.get(Calendar.DAY_OF_MONTH);
                        month=c.get(Calendar.MONTH)+1;
                        year=c.get(Calendar.YEAR);
                    }


                    DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(context, new DatePickerPopWin.OnDatePickedListener() {
                        @Override
                        public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                            dateBirthday = year + "/" + month + "/" + day;
                            dateOfBirth.setText(dateBirthday);
                        }
                    }).textConfirm(context.getResources().getString(R.string.accept)) //text of confirm button
                            .textCancel(context.getResources().getString(R.string.cancel)) //text of cancel button
                            .btnTextSize(16) // button text size
                            .viewTextSize(25) // pick view text size
                            .colorCancel(Color.GRAY) //color of cancel button
                            .colorConfirm(getResources().getColor(R.color.colorAccent))//color of confirm button
                            .minYear(1900) //min year in loop
                            .maxYear(c.get(Calendar.YEAR) + 1) // max year in loop
                            .showDayMonthYear(true) // shows like dd mm yyyy (default is false)
                            .dateChose(year + "-" + month + "-" + day) // date chose when init popwindow
                            .build();

                    if(newDate!=null){
                        pickerPopWin.setSelectedDate(year + "-" + month + "-" + day);
                    }
                    pickerPopWin.showPopWin(getActivity());
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ady = name_surname.getText().toString();
                String fam = username.getText().toString();
                String tel = phone.getText().toString();
                if (ady.trim().isEmpty() || fam.trim().isEmpty() || tel.trim().isEmpty()) {
                    Toast.makeText(context, context.getResources().getString(R.string.fill_out), Toast.LENGTH_SHORT).show();
                    return;
                }
                kProgressHUD.show();
                apiInterface = APIClient.getClient().create(ApiInterface.class);
                UpdateUserInfoPost post = new UpdateUserInfoPost(ady, fam, tel, gender + "", dateBirthday);
                Call<UpdateUserResponse> call = apiInterface.updateUser("Bearer " + Utils.getSharedPreference(context, "tkn"), post);
                call.enqueue(new Callback<UpdateUserResponse>() {
                    @Override
                    public void onResponse(Call<UpdateUserResponse> call, Response<UpdateUserResponse> response) {
                        if (response.isSuccessful()) {
                            Utils.showCustomToast(Utils.getStringResource(R.string.successfully,context),R.drawable.ic_baseline_check_circle_outline_24,context,R.drawable.toast_bg_success);
                        } else{
                            Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong,context),R.drawable.ic_baseline_close_24,context,R.drawable.toast_bg_error);
                        }
                        kProgressHUD.dismiss();
                    }

                    @Override
                    public void onFailure(Call<UpdateUserResponse> call, Throwable t) {
                        kProgressHUD.dismiss();
                        Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong,context),R.drawable.ic_baseline_close_24,context,R.drawable.toast_bg_error);

                    }
                });
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
        birthdayDesc.setTypeface(AppFont.getRegularFont(context));
        name_surname.setTypeface(AppFont.getRegularFont(context));
        username.setTypeface(AppFont.getRegularFont(context));
        phone.setTypeface(AppFont.getRegularFont(context));
        mail.setTypeface(AppFont.getRegularFont(context));
        dateOfBirth.setTypeface(AppFont.getRegularFont(context));
    }

    private void showErrorView(int image, String title, String message, String btn) {
        progress.setVisibility(View.GONE);
        errorButton.setTypeface(AppFont.getRegularFont(context));
        errorMessage.setTypeface(AppFont.getRegularFont(context));
        errorTitle.setTypeface(AppFont.getSemiBoldFont(context));
        errorContainer.setVisibility(View.VISIBLE);
        errorImage.setImageResource(image);
        errorTitle.setText(title);
        errorMessage.setText(message);
        errorButton.setText(btn);

        errorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserData();
            }
        });
    }
}