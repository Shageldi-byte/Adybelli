package com.adybelli.android.Fragment;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Adapter.AddressAdapter;
import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Util;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Object.AddAddressResponse;
import com.adybelli.android.Object.AddAddressResponseBody;
import com.adybelli.android.Object.AddressList;
import com.adybelli.android.Object.GetAddressResponse;
import com.adybelli.android.Object.GetLocations;
import com.adybelli.android.Object.GetLocationsBody;
import com.adybelli.android.Object.LocationChildren;
import com.adybelli.android.Post.AddAddressPost;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.adybelli.android.View.Custom_Spinner;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.android.material.textfield.TextInputEditText;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.skydoves.powerspinner.DefaultSpinnerAdapter;
import com.skydoves.powerspinner.IconSpinnerAdapter;
import com.skydoves.powerspinner.IconSpinnerItem;
import com.skydoves.powerspinner.PowerSpinnerView;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Addres extends Fragment {

    private View view;
    private Context context;
    private RecyclerView rec;
    private ArrayList<AddAddressResponseBody> addressLists = new ArrayList<>();
    private TextView back, newAddress;
    private LinearLayout addAddress;
    private Spinner dr1;
    private Spinner dr2;
    private ArrayList<String> welayatlar = new ArrayList<>();
    private ArrayList<GetLocationsBody> welayatlarOrginal = new ArrayList<>();
    private ArrayList<String> etraplar = new ArrayList<>();
    private ArrayList<LocationChildren> etraplarOrginal = new ArrayList<>();
    private Dialog dialog;
    private ApiInterface apiInterface;
    private KProgressHUD kProgressHUD=null;
    private AddressAdapter addressAdapter;
    private SkeletonScreen skeletonScreen;
    private Integer from=1;
    public Addres(Integer from) {
        if(from!=null)
            this.from=from;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        Utils.loadLocal(context);
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.Material);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        view = localInflater.inflate(R.layout.fragment_addres, container, false);
        initComponents();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        addressAdapter = new AddressAdapter(addressLists, context,welayatlarOrginal);
        rec.setLayoutManager(linearLayoutManager);
        skeletonScreen = Skeleton.bind(rec)
                .adapter(addressAdapter)
                .shimmer(true)
                .angle(20)
                .frozen(false)
                .duration(1200)
                .count(3)
                .color(R.color.skeletonColor)
                .load(R.layout.skelton_sub_category)
                .show(); //default count is 10
        setListener();
        showDialog();
        return view;
    }

    private void initComponents() {
        rec = view.findViewById(R.id.rec);
        back = view.findViewById(R.id.back);
        newAddress = view.findViewById(R.id.newAddress);
        addAddress = view.findViewById(R.id.addAddress);

        newAddress.setTypeface(AppFont.getSemiBoldFont(context));
        back.setTypeface(AppFont.getBoldFont(context));
        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setWindowColor(getResources().getColor(R.color.transparentBlack))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        if(from==2){
            back.setVisibility(View.GONE);
        }
    }

    private void setAddress() {

        apiInterface=APIClient.getClient().create(ApiInterface.class);
        Call<GetAddressResponse> call=apiInterface.getAddress("Bearer "+Utils.getSharedPreference(context,"tkn"));
        call.enqueue(new Callback<GetAddressResponse>() {
            @Override
            public void onResponse(Call<GetAddressResponse> call, Response<GetAddressResponse> response) {
                if(response.isSuccessful()){
                    addressLists=response.body().getBody();
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    addressAdapter = new AddressAdapter(addressLists, context,welayatlarOrginal);
                    rec.setLayoutManager(linearLayoutManager);
                    rec.setAdapter(addressAdapter);
                    rec.setNestedScrollingEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<GetAddressResponse> call, Throwable t) {
                call.cancel();
            }
        });

    }

    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.removeShow(new ProfileRootFragment(), ProfileRootFragment.class.getSimpleName(), getFragmentManager(), R.id.content);
                MainActivity.fifthFragment = new ProfileRootFragment();

            }
        });

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }

    private void showDialog() {
        apiInterface= APIClient.getClient().create(ApiInterface.class);
        Call<GetLocations> call=apiInterface.getLocations();
        call.enqueue(new Callback<GetLocations>() {
            @Override
            public void onResponse(Call<GetLocations> call, Response<GetLocations> response) {
                if(response.isSuccessful()){

                    welayatlar.clear();
                    etraplar.clear();
                    welayatlarOrginal.clear();
                    welayatlarOrginal=response.body().getBody();
                    welayatlar.add(context.getResources().getString(R.string.regions));
                    etraplar.add(context.getResources().getString(R.string.sub_locations));
                    for(int i=0;i<welayatlarOrginal.size();i++){
                        String txt=welayatlarOrginal.get(i).getText();
                        if(Utils.getLanguage(context).equals("ru")){
                            txt=welayatlarOrginal.get(i).getText_ru();
                        }
                        welayatlar.add(txt);

                    }

                    Custom_Spinner welayatAdapter = new Custom_Spinner(context, welayatlar);
                    dr1.setAdapter(welayatAdapter);

                    dr1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long d) {
                            if(dr1.getSelectedItemPosition()<=0){
                                etraplar.clear();
                                etraplar.add(context.getResources().getString(R.string.sub_locations));
                                dr2.setSelection(0);
                                Custom_Spinner etraplarAdapter = new Custom_Spinner(context, etraplar);
                                dr2.setAdapter(etraplarAdapter);
                                return;
                            }
                            etraplarOrginal=welayatlarOrginal.get(dr1.getSelectedItemPosition()-1).getChildren();
                            etraplar.clear();
                            etraplar.add(context.getResources().getString(R.string.sub_locations));
                            for(int l=0;l<etraplarOrginal.size();l++){
                                String txt=etraplarOrginal.get(l).getText();
                                if(Utils.getLanguage(context).equals("ru")){
                                    txt=etraplarOrginal.get(l).getText_ru();
                                }
                                etraplar.add(txt);
                            }
                            dr2.setSelection(0);
                            Custom_Spinner etraplarAdapter = new Custom_Spinner(context, etraplar);
                            dr2.setAdapter(etraplarAdapter);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                }
                setAddress();
            }

            @Override
            public void onFailure(Call<GetLocations> call, Throwable t) {
                setAddress();
            }
        });
        ImageView close;
        dialog = new Dialog(context);
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.Material);
        LayoutInflater localInflater = LayoutInflater.from(context).cloneInContext(contextThemeWrapper);
        View view2 = localInflater.inflate(R.layout.add_address_dialog, null, false);
        dialog.setContentView(view2);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;

        close = dialog.findViewById(R.id.close);
        dr1 = dialog.findViewById(R.id.dr1);
        dr2 = dialog.findViewById(R.id.dr2);

        TextView title = dialog.findViewById(R.id.title);
        TextInputEditText name = dialog.findViewById(R.id.name);
        TextInputEditText tel = dialog.findViewById(R.id.tel);
        TextInputEditText address = dialog.findViewById(R.id.address);
        Button acceptBtn2 = dialog.findViewById(R.id.acceptBtn2);

        title.setTypeface(AppFont.getBoldFont(context));
        name.setTypeface(AppFont.getRegularFont(context));
        tel.setTypeface(AppFont.getRegularFont(context));
        address.setTypeface(AppFont.getRegularFont(context));
        acceptBtn2.setTypeface(AppFont.getBoldFont(context));

        if(!Utils.getSharedPreference(context,"user_phone").isEmpty())
            tel.setText(Utils.getSharedPreference(context,"user_phone").substring(5));






        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        acceptBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tel.getText().toString().trim().isEmpty() || address.getText().toString().trim().isEmpty()){
                    Utils.showCustomToast(Utils.getStringResource(R.string.fill_out,context),R.drawable.ic_outline_info_24,context,R.drawable.toast_bg);
                    return;
                }
                if(dr1.getSelectedItemPosition()<=0 || dr2.getSelectedItemPosition()<=0 || etraplarOrginal.size()<=0 || welayatlarOrginal.size()<=0){
                    Utils.showCustomToast(Utils.getStringResource(R.string.fill_out,context),R.drawable.ic_outline_info_24,context,R.drawable.toast_bg);
                    return;
                }

                kProgressHUD.show();
                AddAddressPost post=new AddAddressPost("",name.getText().toString(),address.getText().toString(),"+9936"+tel.getText().toString(),welayatlarOrginal.get(dr1.getSelectedItemPosition()-1).getLoc_id(),etraplarOrginal.get(dr2.getSelectedItemPosition()-1).getLoc_id()+"");
                Call<AddAddressResponse> call1=apiInterface.addAddress("Bearer "+Utils.getSharedPreference(context,"tkn"),post);
                call1.enqueue(new Callback<AddAddressResponse>() {
                    @Override
                    public void onResponse(Call<AddAddressResponse> call, Response<AddAddressResponse> response) {
                        if(response.isSuccessful()){

                            addressLists.add(response.body().getBody());
                            addressAdapter.notifyItemInserted(addressLists.size()-1);
                            name.setText("");
                            address.setText("");
                            tel.setText("");
                            dr1.setSelection(0);
                            dr1.setSelection(0);
                            dialog.dismiss();
                        } else{
                            Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong,context),R.drawable.ic_baseline_close_24,context,R.drawable.toast_bg_error);
                        }
                        kProgressHUD.dismiss();
                    }

                    @Override
                    public void onFailure(Call<AddAddressResponse> call, Throwable t) {
                        kProgressHUD.dismiss();
                        Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong,context),R.drawable.ic_baseline_close_24,context,R.drawable.toast_bg_error);
                        call1.cancel();
                    }
                });
            }
        });




        dialog.setCancelable(true);
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);


    }
}