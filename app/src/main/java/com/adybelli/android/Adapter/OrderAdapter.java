package com.adybelli.android.Adapter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Common.Constant;
import com.adybelli.android.Common.Util;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Fragment.Addres;
import com.adybelli.android.Fragment.ImageViewer;
import com.adybelli.android.Fragment.MoreInfo;
import com.adybelli.android.Object.GetUserOrderProducts;
import com.adybelli.android.Object.GetUserOrdersResponseBody;
import com.adybelli.android.Object.Order;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.bumptech.glide.Glide;
import com.skydoves.powerspinner.DefaultSpinnerAdapter;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private ArrayList<GetUserOrdersResponseBody> orders = new ArrayList<>();
    private Context context;
    private FragmentManager fragmentManager;

    public OrderAdapter(ArrayList<GetUserOrdersResponseBody> orders, Context context, FragmentManager fragmentManager) {
        this.orders = orders;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_design, parent, false);
        return new OrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrderAdapter.ViewHolder holder, int position) {
        try {
            GetUserOrdersResponseBody order = orders.get(position);
            if (order.getTotal() != null) {
                holder.cost.setText(order.getTotal() + " TMT");
            } else {
                holder.cost.setText("0 TMT");
            }

            if (order.getProducts() != null) {
                holder.count.setText(context.getResources().getString(R.string.count_of_products)+" "+order.getProducts().size());

                holder.colorVariantsContainer.removeAllViews();
                ArrayList<String> colorVariants = new ArrayList<>();
                for (GetUserOrderProducts product : order.getProducts()) {
                    colorVariants.add(product.getImage());
                }
                for (String image : colorVariants) {
                    ImageView imageView = new ImageView(context);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)context.getResources().getDimension(R.dimen.order_product_width), (int)context.getResources().getDimension(R.dimen.order_product_height));
                    layoutParams.setMargins(30, 0, 0, 0);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setPadding(2, 2, 2, 2);
                    imageView.setBackgroundResource(android.R.color.transparent);
                    Glide.with(context)
                            .load(Constant.SERVER_URL + image)
                            .placeholder(R.drawable.placeholder)
                            .into(imageView);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ArrayList<String> imgs = new ArrayList<>();
                            imgs.add(image);
                            Util.showImageViewer(context, imgs, imgs);
                        }
                    });

                    holder.colorVariantsContainer.addView(imageView);

                }
            }


            if(order.getCreated_at()!=null){
                holder.date.setText(order.getCreated_at());
                String date=order.getCreated_at();
                SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.ss");
                Date newDate= null;
                try {
                    newDate = spf.parse(date);
                    spf= new SimpleDateFormat("dd.MM.yyyy, HH:mm");
                    date = spf.format(newDate);
                    holder.date.setText(date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else{
                holder.date.setText("");
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MoreInfo moreInfo=new MoreInfo(Integer.valueOf(order.getOrder_id()),holder,orders,position);
                    Utils.hideAdd(moreInfo, MoreInfo.class.getSimpleName(), fragmentManager, R.id.content);
                    MainActivity.fifthFragment = moreInfo;
                }
            });

            String status = "";
            if (order.getStatus().equals("pending")) {
                status = Utils.getStringResource(R.string.pending, context);
                holder.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pending_icon, 0, 0, 0);
            }
            if (order.getStatus().equals("accepted") || order.getStatus().equals("in_truck") || order.getStatus().equals("stock_tr") || order.getStatus().equals("stock_tm")) {
                status = Utils.getStringResource(R.string.received, context);
                holder.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.received_icon, 0, 0, 0);
            }
            if (order.getStatus().equals("ontheway") || order.getStatus().equals("packing")) {
                status = Utils.getStringResource(R.string.on_the_way, context);
                holder.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.on_the_way_icon, 0, 0, 0);
            }
            if (order.getStatus().equals("received")) {
                status = Utils.getStringResource(R.string.delivered, context);
                holder.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.delivered_icon, 0, 0, 0);
            }
            if (order.getStatus().equals("canceled") || order.getStatus().equals("rejected") || order.getStatus().equals("refund")) {
                status = Utils.getStringResource(R.string.canceled, context);
                holder.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_close_24, 0, 0, 0);
            }

            holder.status.setText(status);

            holder.moreInfo.setTypeface(AppFont.getBoldFont(context));
            holder.cost.setTypeface(AppFont.getSemiBoldFont(context));
            holder.count.setTypeface(AppFont.getSemiBoldFont(context));
            holder.date.setTypeface(AppFont.getSemiBoldFont(context));
            holder.costTitle.setTypeface(AppFont.getRegularFont(context));
            holder.status.setTypeface(AppFont.getSemiBoldFont(context));




        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, cost, status, moreInfo, count, costTitle;
        LinearLayout colorVariantsContainer;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            colorVariantsContainer = itemView.findViewById(R.id.colorVariantsContainer);
            date = itemView.findViewById(R.id.date);
            cost = itemView.findViewById(R.id.cost);
            status = itemView.findViewById(R.id.status);
            moreInfo = itemView.findViewById(R.id.moreInfo);
            count = itemView.findViewById(R.id.count);
            costTitle = itemView.findViewById(R.id.costTitle);
        }
    }


}
