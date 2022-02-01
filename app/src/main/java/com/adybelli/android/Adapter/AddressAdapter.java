package com.adybelli.android.Adapter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.RecyclerView;

import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Fragment.ProcceedCheckout;
import com.adybelli.android.Object.AddAddressResponse;
import com.adybelli.android.Object.AddAddressResponseBody;
import com.adybelli.android.Object.AddressList;
import com.adybelli.android.Object.DeleteAddressBody;
import com.adybelli.android.Object.DeleteAddressResponse;
import com.adybelli.android.Object.GetLocationsBody;
import com.adybelli.android.Object.LocationChildren;
import com.adybelli.android.Post.AddAddressPost;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.adybelli.android.View.Custom_Spinner;
import com.alexandrius.accordionswipelayout.library.SwipeLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.skydoves.powerspinner.DefaultSpinnerAdapter;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private ArrayList<AddAddressResponseBody> addressLists = new ArrayList<>();
    private Context context;
    private CheckBox oldRadioButton;
    private ApiInterface apiInterface;
    private ArrayList<GetLocationsBody> welayatlarOrginal;
    private ArrayList<LocationChildren> etraplarOrginal = new ArrayList<>();
    private int s1 = -1, s2 = -1;
    private boolean isFirstTime = true;
    public static Integer addressId = 0;
    public static double deliveryCost = 0;

    public AddressAdapter(ArrayList<AddAddressResponseBody> addressLists, Context context, ArrayList<GetLocationsBody> welayatlarOrginal) {
        this.addressLists = addressLists;
        this.context = context;
        this.welayatlarOrginal = welayatlarOrginal;
        apiInterface = APIClient.getClient().create(ApiInterface.class);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.address_design, parent, false);
        return new AddressAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AddressAdapter.ViewHolder holder, int position) {
        AddAddressResponseBody object = addressLists.get(position);
        holder.name.setText(object.getTitle());
        holder.number.setText(object.getPhone());
        holder.address.setText(object.getAddress());

        holder.address.setTypeface(AppFont.getSemiBoldFont(context));
        holder.name.setTypeface(AppFont.getSemiBoldFont(context));
        holder.number.setTypeface(AppFont.getRegularFont(context));

        if (position == 0) {
            try {
                oldRadioButton = holder.radioButton;
                holder.radioButton.setChecked(true);
                addressId = Integer.parseInt(object.getUa_id());
                if (object.getSub_location() != null)
                    deliveryCost = object.getSub_location().getCargo_price();
                else
                    deliveryCost = 0;
                ProcceedCheckout.setCosts(deliveryCost);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        holder.setIsRecyclable(false);

        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    try {
                        if(oldRadioButton!=null)
                            oldRadioButton.setChecked(false);
                        oldRadioButton = holder.radioButton;
                        addressId = Integer.parseInt(object.getUa_id());
                        if (object.getSub_location() != null)
                            deliveryCost = object.getSub_location().getCargo_price();
                        else
                            deliveryCost = 0;
                        ProcceedCheckout.setCosts(deliveryCost);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else{
                    deliveryCost = 0;
                    ProcceedCheckout.setCosts(deliveryCost);
                    addressId=0;
                    oldRadioButton=null;
                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.radioButton.setChecked(!holder.radioButton.isChecked());
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(holder.getAdapterPosition());
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelete(holder.getAdapterPosition());
            }
        });
        holder.swipeLayout.setItemState(SwipeLayout.ITEM_STATE_COLLAPSED, false);
        holder.swipeLayout.setOnSwipeItemClickListener(new SwipeLayout.OnSwipeItemClickListener() {
            @Override
            public void onSwipeItemClick(boolean left, int index) {
                if (left) {
                    switch (index) {
                        case 0:
                            break;
                    }
                } else {
                    switch (index) {
                        case 0:
                            showDialog(holder.getAdapterPosition());
                            break;
                        case 1:
                            showDelete(holder.getAdapterPosition());
                    }
                }
            }
        });
    }

    private void showDelete(int position) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(context.getResources().getString(R.string.remove_address));
        alert.setCancelable(true);
        alert.setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                KProgressHUD kProgressHUD = KProgressHUD.create(context)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setWindowColor(context.getResources().getColor(R.color.transparentBlack))
                        .setCancellable(false)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f);
                kProgressHUD.show();
                Call<DeleteAddressResponse> call = apiInterface.deleteAddress("Bearer " + Utils.getSharedPreference(context, "tkn"), new DeleteAddressBody(Integer.valueOf(addressLists.get(position).getUa_id())));
                call.enqueue(new Callback<DeleteAddressResponse>() {
                    @Override
                    public void onResponse(Call<DeleteAddressResponse> call, Response<DeleteAddressResponse> response) {
                        if (response.isSuccessful()) {
                            addressLists.remove(position);
                            notifyItemRemoved(position);
                            ProcceedCheckout.setCosts(0);
                        }
                        Utils.showCustomToast(Utils.getStringResource(R.string.successfully,context),R.drawable.ic_baseline_check_circle_outline_24,context,R.drawable.toast_bg_success);
                        kProgressHUD.dismiss();
                    }

                    @Override
                    public void onFailure(Call<DeleteAddressResponse> call, Throwable t) {
                        call.cancel();
                        Utils.showCustomToast(Utils.getStringResource(R.string.cant_delete,context),R.drawable.ic_baseline_clear_24,context,R.drawable.toast_bg_error);
                        kProgressHUD.dismiss();
                    }
                });

            }
        });
        alert.setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }

    @Override
    public int getItemCount() {
        return addressLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, number, address;
        CheckBox radioButton;
        ImageView edit, delete;
        SwipeLayout swipeLayout;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            address = itemView.findViewById(R.id.address);
            radioButton = itemView.findViewById(R.id.radioButton);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            swipeLayout = itemView.findViewById(R.id.swipe_layout);
        }
    }

    private void showDialog(int position) {
        isFirstTime = true;
        AddAddressResponseBody object = addressLists.get(position);
        ImageView close;
        KProgressHUD kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setWindowColor(context.getResources().getColor(R.color.transparentBlack))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        Spinner dr1;
        Spinner dr2;
        ArrayList<String> welayatlar = new ArrayList<>();
        ArrayList<String> etraplar = new ArrayList<>();
        Dialog dialog = new Dialog(context);
        final Context contextThemeWrapper = new ContextThemeWrapper(((Activity) context), R.style.Material);
        LayoutInflater localInflater = LayoutInflater.from(context).cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.add_address_dialog, null, false);
        dialog.setContentView(view);
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

        title.setText(context.getResources().getString(R.string.chnage_address));
        acceptBtn2.setText(context.getResources().getString(R.string.accept));

        name.setText(object.getTitle());
        tel.setText(object.getPhone().substring(5));
        address.setText(object.getAddress());

        welayatlar.clear();
        etraplar.clear();
        for (int i = 0; i < welayatlarOrginal.size(); i++) {
            if (welayatlarOrginal.get(i).getLoc_id().equals(object.getLocation().getLoc_id())) {
                s1 = i;
            }
            String txt = welayatlarOrginal.get(i).getText();
            if (Utils.getLanguage(context).equals("ru")) {
                txt = welayatlarOrginal.get(i).getText_ru();
            }
            welayatlar.add(txt);

        }

        Custom_Spinner welayatAdapter = new Custom_Spinner(context, welayatlar);
        dr1.setAdapter(welayatAdapter);

        etraplarOrginal = welayatlarOrginal.get(s1).getChildren();
        etraplar.clear();
        for (int l = 0; l < etraplarOrginal.size(); l++) {
            if (etraplarOrginal.get(l).getLoc_id() == Integer.parseInt(object.getSub_location().getLoc_id())) {
                s2 = l;
            }
            String txt = etraplarOrginal.get(l).getText();
            if (Utils.getLanguage(context).equals("ru")) {
                txt = etraplarOrginal.get(l).getText_ru();
            }
            etraplar.add(txt);
        }


        Custom_Spinner etraplarAdapter = new Custom_Spinner(context, etraplar);
        dr2.setAdapter(etraplarAdapter);

        dr1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long d) {
                if (!isFirstTime) {
                    etraplarOrginal = welayatlarOrginal.get(dr1.getSelectedItemPosition()).getChildren();
                    etraplar.clear();
                    for (int l = 0; l < etraplarOrginal.size(); l++) {
                        String txt = etraplarOrginal.get(l).getText();
                        if (Utils.getLanguage(context).equals("ru")) {
                            txt = etraplarOrginal.get(l).getText_ru();
                        }
                        etraplar.add(txt);
                    }
                    Custom_Spinner etraplarAdapter = new Custom_Spinner(context, etraplar);
                    dr2.setAdapter(etraplarAdapter);

                } else {
                    dr2.setSelection(s2);
                    isFirstTime = false;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        acceptBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (tel.toString().trim().isEmpty() || address.getText().toString().trim().isEmpty()) {
                    Utils.showCustomToast(Utils.getStringResource(R.string.fill_out,context),R.drawable.ic_outline_info_24,context,R.drawable.toast_bg);
                    return;
                }

                if (etraplarOrginal.size() <= 0 || welayatlarOrginal.size() <= 0) {
                    Utils.showCustomToast(Utils.getStringResource(R.string.fill_out,context),R.drawable.ic_outline_info_24,context,R.drawable.toast_bg);
                    return;
                }

                s2 = etraplarOrginal.get(dr2.getSelectedItemPosition()).getLoc_id();
                s1 = Integer.parseInt(welayatlarOrginal.get(dr1.getSelectedItemPosition()).getLoc_id());
                if (s1 <= -1 || s2 <= -1) {
                    Utils.showCustomToast(Utils.getStringResource(R.string.fill_out,context),R.drawable.ic_outline_info_24,context,R.drawable.toast_bg);
                    return;
                }
                kProgressHUD.show();
                //Toast.makeText(context, welayatlarOrginal.get(dr1.getSelectedIndex()).getLoc_id()+" / "+etraplarOrginal.get(dr2.getSelectedIndex()).getLoc_id(), Toast.LENGTH_SHORT).show();
                AddAddressPost post = new AddAddressPost(object.getUa_id(), name.getText().toString(), address.getText().toString(), "+9936"+tel.getText().toString(), s1 + "", s2 + "");
                Call<AddAddressResponse> call1 = apiInterface.updateAddress("Bearer " + Utils.getSharedPreference(context, "tkn"), post);
                call1.enqueue(new Callback<AddAddressResponse>() {
                    @Override
                    public void onResponse(Call<AddAddressResponse> call, Response<AddAddressResponse> response) {
                        if (response.isSuccessful()) {
                            Utils.showCustomToast(Utils.getStringResource(R.string.successfully,context),R.drawable.ic_baseline_check_circle_outline_24,context,R.drawable.toast_bg_success);
                            kProgressHUD.dismiss();
//                            addressLists.remove(position);
//                            notifyItemRemoved(position);
                            addressLists.set(position, response.body().getBody());

                            notifyDataSetChanged();
                            name.setText("");
                            address.setText("");
                            tel.setText("");
                            dr1.setSelection(0);
                            dr2.setSelection(0);
                            dialog.dismiss();
                        } else{
                            Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong,context),R.drawable.ic_baseline_close_24,context,R.drawable.toast_bg_error);
                        }
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


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
//        Log.e("S2", s2 + "");
        dr1.setSelection(s1);


        dialog.setCancelable(true);
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        dialog.show();

    }
}
