package com.adybelli.android.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.adybelli.android.Activity.ProductPage;
import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Constant;
import com.adybelli.android.Common.Util;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Fragment.MoreInfo;
import com.adybelli.android.Object.CancelOrderProduct;
import com.adybelli.android.Object.GetUserSingleOrderProducts;
import com.adybelli.android.Object.UpdateMailResponse;
import com.adybelli.android.Post.CancelOrder;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.bumptech.glide.Glide;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoreInfoAdapter extends RecyclerView.Adapter<MoreInfoAdapter.ViewHolder> {
    private ArrayList<GetUserSingleOrderProducts> moreInfos = new ArrayList<>();
    private Context context;
    private ApiInterface apiInterface;
    private String status = "";

    public MoreInfoAdapter(ArrayList<GetUserSingleOrderProducts> moreInfos, Context context) {
        this.moreInfos = moreInfos;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_info_design, parent, false);
        return new MoreInfoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MoreInfoAdapter.ViewHolder holder, int position) {
        GetUserSingleOrderProducts object = moreInfos.get(position);

        holder.name.setTypeface(AppFont.getSemiBoldFont(context));
        holder.sizeTitle.setTypeface(AppFont.getRegularFont(context));
        holder.old_cost.setTypeface(AppFont.getRegularFont(context));
        holder.cost.setTypeface(AppFont.getSemiBoldFont(context));
        holder.status.setTypeface(AppFont.getSemiBoldFont(context));
        holder.old_cost.setPaintFlags(holder.old_cost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        holder.name.setText(object.getTrademark());
        String nameText = object.getName();

        if (Utils.getLanguage(context).equals("ru")) {
            nameText = object.getName_ru();
        }
        holder.sizeTitle.setText(nameText + ", " + Utils.getStringResource(R.string.size__, context) + " - " + object.getSize());
        holder.cost.setText(object.getPrice() + " TMT");
        if (object.getStatus().equals("pending")) {
            holder.status.setText(Utils.getStringResource(R.string.pending, context));
        }

        holder.count.setText(Utils.getStringResource(R.string.count, context) + " " + object.getCount());


        if (object.getStatus().equals("pending")) {
            status = Utils.getStringResource(R.string.pending, context);
            holder.statusImage.setImageResource(R.drawable.pending_icon);
            holder.cancelProduct.setVisibility(View.VISIBLE);
        }
        if (object.getStatus().equals("accepted") || object.getStatus().equals("in_truck") || object.getStatus().equals("stock_tr") || object.getStatus().equals("stock_tm")) {
            status = Utils.getStringResource(R.string.received, context);
            holder.statusImage.setImageResource(R.drawable.received_icon);
        }
        if (object.getStatus().equals("ontheway") || object.getStatus().equals("packing")) {
            status = Utils.getStringResource(R.string.on_the_way, context);
            holder.statusImage.setImageResource(R.drawable.on_the_way_icon);
        }
        if (object.getStatus().equals("received")) {
            status = Utils.getStringResource(R.string.delivered, context);
            holder.statusImage.setImageResource(R.drawable.delivered_icon);
        }
        if (object.getStatus().equals("canceled") || object.getStatus().equals("rejected") || object.getStatus().equals("refund")) {
            status = Utils.getStringResource(R.string.canceled, context);
            holder.statusImage.setImageResource(R.drawable.ic_baseline_close_24);
        }

        holder.cancelProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KProgressHUD progressHUD=Utils.getProgress(context);
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle(Utils.getStringResource(R.string.pay_attention, context));
                alert.setMessage(Utils.getStringResource(R.string.cancel_order_question, context));
                alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressHUD.show();
                        apiInterface = APIClient.getClient().create(ApiInterface.class);
                        CancelOrderProduct cancelOrder = new CancelOrderProduct(object.getOd_id());
                        Call<UpdateMailResponse> call = apiInterface.cancelOrderProduct("Bearer " + Utils.getSharedPreference(context, "tkn"), cancelOrder);
                        call.enqueue(new Callback<UpdateMailResponse>() {
                            @Override
                            public void onResponse(Call<UpdateMailResponse> call, Response<UpdateMailResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().getBody().equals("SUCCESS")) {
                                        moreInfos.get(holder.getAbsoluteAdapterPosition()).setStatus("canceled");
                                        notifyItemChanged(holder.getAbsoluteAdapterPosition());
                                        status = Utils.getStringResource(R.string.canceled, context);
                                        holder.status.setText(status);
                                        holder.statusImage.setImageResource(R.drawable.ic_baseline_close_24);
                                        holder.cancelProduct.setVisibility(View.GONE);
                                        Utils.showCustomToast(Utils.getStringResource(R.string.successfully, context), R.drawable.ic_baseline_check_circle_outline_24, context, R.drawable.toast_bg_success);
                                        boolean isCancel=false;
                                        for(GetUserSingleOrderProducts more:moreInfos){
                                            if(more.getStatus().equals("pending")){
                                                isCancel=true;
                                                break;
                                            }
                                        }

                                        if(isCancel){
                                            try{
                                                MoreInfo.get().getCancelOrder().setVisibility(View.VISIBLE);
                                            } catch (Exception ex){
                                                ex.printStackTrace();
                                            }
                                        } else{
                                            try{
                                                MoreInfo.get().getCancelOrder().setVisibility(View.GONE);
                                            } catch (Exception ex){
                                                ex.printStackTrace();
                                            }
                                        }
                                    }
                                } else {
                                    Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong, context), R.drawable.ic_baseline_close_24, context, R.drawable.toast_bg_error);
                                }
                                progressHUD.dismiss();
                            }

                            @Override
                            public void onFailure(Call<UpdateMailResponse> call, Throwable t) {
                                Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong, context), R.drawable.ic_baseline_close_24, context, R.drawable.toast_bg_error);
                                progressHUD.dismiss();
                            }
                        });
                    }
                });
                alert.setCancelable(true);
                alert.show();
            }
        });

        holder.status.setText(status);


        Glide.with(context)
                .load(Constant.SERVER_URL + object.getImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> imgs = new ArrayList<>();
                imgs.add(object.getImage());
                Util.showImageViewer(context, imgs, imgs);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moreInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, sizeTitle, old_cost, cost, status, count;
        ImageView image, statusImage;
        Button cancelProduct;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            sizeTitle = itemView.findViewById(R.id.sizeTitle);
            old_cost = itemView.findViewById(R.id.old_cost);
            cost = itemView.findViewById(R.id.cost);
            image = itemView.findViewById(R.id.image);
            status = itemView.findViewById(R.id.status);
            count = itemView.findViewById(R.id.count);
            statusImage = itemView.findViewById(R.id.statusImage);
            cancelProduct = itemView.findViewById(R.id.cancelProduct);
        }
    }
}
