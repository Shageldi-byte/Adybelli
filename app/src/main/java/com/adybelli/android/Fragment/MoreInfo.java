package com.adybelli.android.Fragment;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Activity.ShareProductImage;
import com.adybelli.android.Adapter.MoreInfoAdapter;
import com.adybelli.android.Adapter.OrderAdapter;
import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Util;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Object.GetUserOrdersResponseBody;
import com.adybelli.android.Object.GetUserSingleOrder;
import com.adybelli.android.Object.GetUserSingleOrderProducts;
import com.adybelli.android.Object.GetUserSingleOrderResponse;
import com.adybelli.android.Object.UpdateMailResponse;
import com.adybelli.android.Post.CancelOrder;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.itextpdf.text.pdf.PdfReader;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.skydoves.powerspinner.DefaultSpinnerAdapter;
import com.skydoves.powerspinner.PowerSpinnerView;
import com.zhy.base.fileprovider.FileProvider7;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoreInfo extends Fragment {


    private View view;
    private Context context;
    private RecyclerView rec;
    private TextView faktura, orderCodeTitle, orderCode, orderDateTitle, orderDate, orderCountTitle, orderCount, date, count, address, tel, toleg, tt1, t2, t3, t4, t5, t6, t7, t8, totalCostTitle, totalCost, typeCost, back;
    private ArrayList<GetUserSingleOrderProducts> moreInfos = new ArrayList<>();
    private ApiInterface apiInterface;
    private Integer id;
    private NestedScrollView scroll;
    private ProgressBar progress;
    private LinearLayout con;
    private String customerHtml = "";
    private String addressHtml = "";
    private String productsHtml = "";
    private String pay_method = "";
    private String addressTitle, addressText, phoneText, regionText, cityText;
    private String productsHtmlProduct = "";
    private String total_price, shipping_price;
    private ImageView status;
    private TextView canceled, cancelOrder;
    private OrderAdapter.ViewHolder holder;
    private ArrayList<GetUserOrdersResponseBody> orders;
    private Integer pos;
    private static MoreInfo INSTANCE;


    public MoreInfo(Integer valueOf, OrderAdapter.ViewHolder holder, ArrayList<GetUserOrdersResponseBody> orders, Integer pos) {
        this.id = valueOf;
        this.holder = holder;
        this.orders = orders;
        this.pos = pos;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_more_info, container, false);
        context = getContext();
        initComponents();
        request();
        INSTANCE=this;
        setFonts();
        return view;
    }

    public static MoreInfo get(){
        return INSTANCE;
    }

    public TextView getCancelOrder(){
        return cancelOrder;
    }

    private void request() {

        apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<GetUserSingleOrderResponse> call = apiInterface.getSingleOrder("Bearer " + Utils.getSharedPreference(context, "tkn"), id,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
        call.enqueue(new Callback<GetUserSingleOrderResponse>() {
            @Override
            public void onResponse(Call<GetUserSingleOrderResponse> call, Response<GetUserSingleOrderResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        GetUserSingleOrderResponse order = response.body();
                        GetUserSingleOrder body = order.getBody();
                        moreInfos = body.getProducts();
                        orderCode.setText("#" + body.getOrder_id());
                        orderDate.setText(body.getCreated_at());

                        if (body.getPayment_method() == 1) {
                            pay_method = Utils.getStringResource(R.string.cash_on_door, context);
                        }
                        if (body.getPayment_method() == 2) {
                            pay_method = Utils.getStringResource(R.string.terminal_on_door, context);
                        }
                        if (body.getPayment_method() == 3) {
                            pay_method = Utils.getStringResource(R.string.online_buy, context);
                        }

                        Log.e("Status", body.getStatus());

                        if (body.getStatus().equals("pending")) {
                            status.setImageResource(R.drawable.pending_tm);
                            if (Utils.getLanguage(context).equals("ru")) {
                                status.setImageResource(R.drawable.pending_ru);
                            }


                        }
                        boolean isCancel=false;
                          for(GetUserSingleOrderProducts more:moreInfos){
                                if(more.getStatus().equals("pending")){
                                    isCancel=true;
                                    break;
                                }
                            }

                          if(isCancel){
                              cancelOrder.setVisibility(View.VISIBLE);
                          }

                        if (body.getStatus().equals("accepted") || body.getStatus().equals("in_truck") || body.getStatus().equals("stock_tr") || body.getStatus().equals("stock_tm")) {
                            status.setImageResource(R.drawable.received_tm);
                            if (Utils.getLanguage(context).equals("ru")) {
                                status.setImageResource(R.drawable.received_ru);
                            }
                        }
                        if (body.getStatus().equals("ontheway") || body.getStatus().equals("packing")) {
                            status.setImageResource(R.drawable.on_the_way_tm);
                            if (Utils.getLanguage(context).equals("ru")) {
                                status.setImageResource(R.drawable.on_the_way_ru);
                            }
                        }
                        if (body.getStatus().equals("received")) {
                            status.setImageResource(R.drawable.delivered_tm);
                            if (Utils.getLanguage(context).equals("ru")) {
                                status.setImageResource(R.drawable.delivered_ru);
                            }
                        }
                        if (body.getStatus().equals("canceled") || body.getStatus().equals("rejected") || body.getStatus().equals("refund")) {
                            status.setVisibility(View.GONE);
                            canceled.setVisibility(View.VISIBLE);
                            faktura.setVisibility(View.GONE);
                        }


                        String date = body.getCreated_at();
                        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.ss");
                        Date newDate = null;
                        try {
                            newDate = spf.parse(date);
                            spf = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
                            date = spf.format(newDate);
                            orderDate.setText(date);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        addressTitle = body.getAddressTitle();
                        addressText = body.getAddress();
                        phoneText = body.getPhone();
                        if (body.getLocation() != null)
                            regionText = body.getLocation().getText();

                        if (body.getSub_location() != null && body.getSub_location().getText() != null)
                            cityText = body.getSub_location().getText();
                        if (Utils.getLanguage(context).equals("ru")) {
                            if (body.getSub_location() != null && body.getSub_location().getText_ru() != null)
                                regionText = body.getLocation().getText_ru();
                            if (body.getSub_location() != null)
                                cityText = body.getSub_location().getText_ru();
                        }

                        orderCount.setText(moreInfos.size() + "");
                        count.setText("#" + body.getAddressTitle());
                        address.setText(body.getAddress());
                        tel.setText(body.getPhone());
                        Double total_price_temp = body.getTotal();
                        Double sum = body.getTotal();
                        shipping_price = 0 + "";
                        if (body.getShipping_price() != null) {
                            shipping_price = body.getShipping_price() + "";
                            total_price_temp = body.getTotal() + body.getShipping_price();
                            sum = body.getTotal() + body.getShipping_price();
                        }
                        total_price = total_price_temp + "";
                        t2.setText(body.getTotal() + " TMT");
                        t4.setText(shipping_price + " TMT");
                        totalCost.setText(sum + " TMT");
                        setOrders();
                        setListener();
                        progress.setVisibility(View.GONE);
                        scroll.setVisibility(View.VISIBLE);

                        setHtml();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetUserSingleOrderResponse> call, Throwable t) {

            }
        });
    }

    private void setHtml() {
        customerHtml = "<!DOCTYPE html>\n" +
                "\n" +
                "<html>\n" +
                "\n" +
                "<head>\n" +
                "\n" +
                "<meta name=\"viewport\" content=\"initial-scale=-10, user-scalable=yes, width=device-width\" />\n" +
                "\n" +
                "<style>\n" +
                "\n" +
                "table {\n" +
                "\n" +
                "  font-family: ProximaNova, sans-serif;\n" +
                "\n" +
                "  border-collapse: collapse;\n" +
                "\n" +
                "  width: 100%;\n" +
                "\n" +
                "}\n" +
                "\n" +
                "\n" +
                "\n" +
                "td, th {\n" +
                "\n" +
                "  border: 1px solid #dddddd;\n" +
                "\n" +
                "  text-align: left;\n" +
                "\n" +
                "  padding: 8px;\n" +
                "\n" +
                "}\n" +
                "\n" +
                "\n" +
                "\n" +
                "#first {\n" +
                "\n" +
                "  border: 1px solid #dddddd;\n" +
                "\n" +
                "  text-align: left;\n" +
                "\n" +
                "  padding: 8px;\n" +
                "\n" +
                "  background-color: #dddddd;\n" +
                "\n" +
                "}\n" +
                "\n" +
                "</style>\n" +
                "\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\n" +
                "<table>\n" +
                "\n" +
                "    <tr id = \"first\">\n" +
                "\n" +
                "        <th colspan=\"2\";>" + Utils.getStringResource(R.string.customer_information, context) + "</th>\n" +
                "\n" +
                "      </tr>\n" +
                "\n" +
                "        <tr>\n" +
                "\n<th>" +
                Utils.getStringResource(R.string.customer, context) +
                "</th>\n" +
                "            <td>" + Utils.getSharedPreference(context, "user_first_name") + " " + Utils.getSharedPreference(context, "user_last_name") + "</td>\n" +
                "\n" +
                "        </tr>\n" +
                "\n" +
                "        <tr>\n" +
                "\n" +
                "            <th>" + Utils.getStringResource(R.string.phone_number_, context) + "</th>\n" +
                "\n" +
                "            <td>" + Utils.getSharedPreference(context, "user_phone") + "</td>\n" +
                "\n" +
                "        </tr>\n" +
                "\n" +
                "        <tr>\n" +
                "\n" +
                "          <th>" + Utils.getStringResource(R.string.type_cost_, context) + "</th>\n" +
                "\n" +
                "          <td>" + pay_method + "</td>\n" +
                "\n" +
                "        </tr>\n" +
                "\n" +
                "      </table>\n" +
                "\n" +
                "      </body>\n" +
                "\n" +
                "      </html>";


        addressHtml = "<!DOCTYPE html>\n" +
                "\n" +
                "<html>\n" +
                "\n" +
                "<head>\n" +
                "\n" +
                "<meta name=\"viewport\" content=\"initial-scale=-10, user-scalable=yes, width=device-width\" />\n" +
                "\n" +
                "<style>\n" +
                "\n" +
                "table {\n" +
                "\n" +
                "  font-family: ProximaNova, sans-serif;\n" +
                "\n" +
                "  border-collapse: collapse;\n" +
                "\n" +
                "  width: 100%;\n" +
                "\n" +
                "}\n" +
                "\n" +
                "\n" +
                "\n" +
                "td, th {\n" +
                "\n" +
                "  border: 1px solid #dddddd;\n" +
                "\n" +
                "  text-align: left;\n" +
                "\n" +
                "  padding: 8px;\n" +
                "\n" +
                "}\n" +
                "\n" +
                "\n" +
                "\n" +
                "#first {\n" +
                "\n" +
                "  border: 1px solid #dddddd;\n" +
                "\n" +
                "  text-align: left;\n" +
                "\n" +
                "  padding: 8px;\n" +
                "\n" +
                "  background-color: #dddddd;\n" +
                "\n" +
                "}\n" +
                "\n" +
                "</style>\n" +
                "\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\n" +
                "\n" +
                "    <table>\n" +
                "\n" +
                "        <tr id = \"first\">\n" +
                "\n" +
                "          <th  colspan=\"2\";>" + Utils.getStringResource(R.string.address_information, context) + "</th>\n" +
                "\n" +
                "        </tr>\n" +
                "\n" +
                "        <tr>\n" +
                "\n" +
                "          <th>" + Utils.getStringResource(R.string.address_ady, context) + "</th>\n" +
                "\n" +
                "          <td>" + addressTitle + "</td>\n" +
                "\n" +
                "        </tr>\n" +
                "\n" +
                "        <tr>\n" +
                "\n" +
                "          <th>" + Utils.getStringResource(R.string.salgy, context) + "</th>\n" +
                "\n" +
                "          <td>" + addressText + "</td>\n" +
                "\n" +
                "        </tr>\n" +
                "\n" +
                "        <tr>\n" +
                "\n" +
                "          <th>" + Utils.getStringResource(R.string.phone_number_title, context) + "</th>\n" +
                "\n" +
                "          <td>" + phoneText + "</td>\n" +
                "\n" +
                "        </tr>\n" +
                "\n" +
                "        <tr>\n" +
                "\n" +
                "          <th>" + Utils.getStringResource(R.string.region, context) + "</th>\n" +
                "\n" +
                "          <td>" + regionText + "</td>\n" +
                "\n" +
                "        </tr>\n" +
                "\n" +
                "        <tr>\n" +
                "\n" +
                "          <th>" + Utils.getStringResource(R.string.city, context) + "</th>\n" +
                "\n" +
                "          <td>" + cityText + "</td>\n" +
                "\n" +
                "        </tr>\n" +
                "\n" +
                "      </table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "      </body>\n" +
                "\n" +
                "      </html>";


        int tb = 0;
        for (GetUserSingleOrderProducts product : moreInfos) {
            tb++;
            String name_of_product = product.getName();
            String status = "";
            if (product.getStatus().equals("pending"))
                status = Utils.getStringResource(R.string.pending, context);
            if (product.getStatus().equals("accepted") || product.getStatus().equals("in_truck") || product.getStatus().equals("stock_tr") || product.getStatus().equals("stock_tm"))
                status = Utils.getStringResource(R.string.received, context);
            if (product.getStatus().equals("ontheway") || product.getStatus().equals("packing"))
                status = Utils.getStringResource(R.string.on_the_way, context);
            if (product.getStatus().equals("received"))
                status = Utils.getStringResource(R.string.delivered, context);
            if (product.getStatus().equals("canceled") || product.getStatus().equals("rejected") || product.getStatus().equals("refund"))
                status = Utils.getStringResource(R.string.canceled, context);


            if (Utils.getLanguage(context).equals("ru"))
                name_of_product = product.getName_ru();
            Double total = product.getPrice() * product.getCount();
            ;
            productsHtmlProduct = productsHtmlProduct + ("<tr>" +
                    "" +
                    "<td>" + tb + "</td>" +
                    "" +
                    "<td>" + name_of_product + "</td>" +
                    "" +
                    "<td>" + product.getTrademark() + "</td>" +
                    "" +
                    "<td>" + product.getCount() + "</td>" +
                    "" +
                    "<td>" + product.getSize() + "</td>" +
                    "" +
                    "<td>" + status + "</td>" +
                    "" +
                    "<td>" + product.getPrice() + " TMT</td>" +
                    "" +
                    "<td>" + total + " TMT</td>" +
                    "" +
                    "</tr>");
        }


        productsHtml = "<!DOCTYPE html>\n" +
                "\n" +
                "<html>\n" +
                "\n" +
                "<head>\n" +
                "\n" +
                "<meta name=\"viewport\" content=\"initial-scale=-10, user-scalable=yes, width=device-width\" />\n" +
                "\n" +
                "<style>\n" +
                "\n" +
                "table {\n" +
                "\n" +
                "  font-family: ProximaNova, sans-serif;\n" +
                "\n" +
                "  border-collapse: collapse;\n" +
                "\n" +
                "  width: 100%;\n" +
                "\n" +
                "}\n" +
                "\n" +
                "\n" +
                "\n" +
                "td, th {\n" +
                "\n" +
                "  border: 1px solid #dddddd;\n" +
                "\n" +
                "  text-align: left;\n" +
                "\n" +
                "  padding: 8px;\n" +
                "\n" +
                "}\n" +
                "\n" +
                "\n" +
                "\n" +
                "#first {\n" +
                "\n" +
                "  border: 1px solid #dddddd;\n" +
                "\n" +
                "  text-align: left;\n" +
                "\n" +
                "  padding: 8px;\n" +
                "\n" +
                "  background-color: #dddddd;\n" +
                "\n" +
                "}\n" +
                "\n" +
                "</style>\n" +
                "\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\n" +
                "<table>\n" +
                "\n" +
                "  <tr id = \"first\">\n" +
                "\n" +
                "    <th  colspan=\"8\";>" + Utils.getStringResource(R.string.order_information, context) + "</th>\n" +
                "\n" +
                "  </tr>\n" +
                "\n" +
                "  <tr>\n" +
                "\n" +
                "    <th><em>" + Utils.getStringResource(R.string.tb, context) + "</em></th>\n" +
                "\n" +
                "    <th>" + Utils.getStringResource(R.string.product_code, context) + "</th>\n" +
                "\n" +
                "    <th>" + Utils.getStringResource(R.string.brand_of_product, context) + "</th>\n" +
                "\n" +
                "    <th>" + Utils.getStringResource(R.string.count_, context) + "</th>\n" +
                "\n" +
                "    <th>" + Utils.getStringResource(R.string.size__, context) + "</th>\n" +
                "\n" +
                "    <th>" + Utils.getStringResource(R.string.status, context) + "</th>\n" +
                "\n" +
                "    <th>" + Utils.getStringResource(R.string.cost_, context) + "</th>\n" +
                "\n" +
                "    <th>" + Utils.getStringResource(R.string.total_cost_, context) + "</th>\n" +
                "\n" +
                "  </tr>\n" +
                "\n" +
                "\n" + productsHtmlProduct + "\n" +
                "\n" +
                "\n" +
                "<tr>\n" +
                "\n" +
                "    <th colspan=\"7\"; style = \"text-align: right;\">" + Utils.getStringResource(R.string.aralyk_jem, context) + "</th>\n" +
                "\n" +
                "    <td>" + t2.getText().toString() + "</td>\n" +
                "\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "\n" +
                "    <th colspan=\"7\"; style = \"text-align: right;\">" + Utils.getStringResource(R.string.order_delivery_cost, context) + "</th>\n" +
                "\n" +
                "    <td>" + shipping_price + " TMT</td>\n" +
                "\n" +
                "  </tr>\n" +
                "\n" +
                "  <tr>\n" +
                "\n" +
                "    <th colspan=\"7\"; style = \"text-align: right;\">" + Utils.getStringResource(R.string.total_cost__, context) + "</th>\n" +
                "\n" +
                "    <td>" + total_price + " TMT</td>\n" +
                "\n" +
                "  </tr>\n" +
                "\n" +
                "  \n" +
                "\n" +
                "</table>\n" +
                "\n" +
                "</body>\n" +
                "\n" +
                "</html>";


    }

    private void initComponents() {
        rec = view.findViewById(R.id.rec);
        faktura = view.findViewById(R.id.faktura);
        orderCodeTitle = view.findViewById(R.id.orderCodeTitle);
        orderCode = view.findViewById(R.id.orderCode);
        orderDateTitle = view.findViewById(R.id.orderDateTitle);
        orderDate = view.findViewById(R.id.orderDate);
        orderCountTitle = view.findViewById(R.id.orderCountTitle);
        orderCount = view.findViewById(R.id.orderCount);
        date = view.findViewById(R.id.date);
        count = view.findViewById(R.id.count);
        tel = view.findViewById(R.id.tel);
        toleg = view.findViewById(R.id.toleg);
        tt1 = view.findViewById(R.id.tt1);
        t2 = view.findViewById(R.id.t2);
        t3 = view.findViewById(R.id.t3);
        t4 = view.findViewById(R.id.t4);
        t5 = view.findViewById(R.id.t5);
        t6 = view.findViewById(R.id.t6);
        t7 = view.findViewById(R.id.t7);
        t8 = view.findViewById(R.id.t8);
        totalCostTitle = view.findViewById(R.id.totalCostTitle);
        totalCost = view.findViewById(R.id.totalCost);
        address = view.findViewById(R.id.address);
        typeCost = view.findViewById(R.id.typeCost);
        back = view.findViewById(R.id.back);
        progress = view.findViewById(R.id.progress);
        scroll = view.findViewById(R.id.scroll);
        status = view.findViewById(R.id.status);
        canceled = view.findViewById(R.id.canceled);
        cancelOrder = view.findViewById(R.id.cancelOrder);
    }

    private void setOrders() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        MoreInfoAdapter adapter = new MoreInfoAdapter(moreInfos, context);
        rec.setLayoutManager(linearLayoutManager);
        rec.setAdapter(adapter);
    }

    private void setListener() {
        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backTo();
            }
        });

        faktura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KProgressHUD progress1 = Utils.getProgress(context);
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
                        progress1.show();
                        apiInterface = APIClient.getClient().create(ApiInterface.class);
                        CancelOrder cancelOrder = new CancelOrder(id);
                        Call<UpdateMailResponse> call = apiInterface.cancelOrder("Bearer " + Utils.getSharedPreference(context, "tkn"), cancelOrder,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
                        call.enqueue(new Callback<UpdateMailResponse>() {
                            @Override
                            public void onResponse(Call<UpdateMailResponse> call, Response<UpdateMailResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().getBody().equals("SUCCESS")) {
                                        orders.get(pos).setStatus("canceled");
                                        holder.getBindingAdapter().notifyItemChanged(pos);
                                        backTo();
                                        Utils.showCustomToast(Utils.getStringResource(R.string.successfully, context), R.drawable.ic_baseline_check_circle_outline_24, context, R.drawable.toast_bg_success);


                                    }
                                } else {
                                    Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong, context), R.drawable.ic_baseline_close_24, context, R.drawable.toast_bg_error);
                                }
                                progress1.dismiss();
                            }

                            @Override
                            public void onFailure(Call<UpdateMailResponse> call, Throwable t) {
                                progress1.dismiss();
                                Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong, context), R.drawable.ic_baseline_close_24, context, R.drawable.toast_bg_error);
                            }
                        });
                    }
                });
                alert.setCancelable(true);
                alert.show();
            }
        });
    }

    private void backTo() {
        Utils.removeShow(new Orders(), Orders.class.getSimpleName(), getFragmentManager(), R.id.content);
        MainActivity.fifthFragment = new Orders();
    }

    private void showDialog() {
        ImageView close;
        Dialog dialog = new Dialog(context);
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.Material);
        LayoutInflater localInflater = LayoutInflater.from(context).cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.hasabat_fakturasy, null, false);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;

        close = dialog.findViewById(R.id.close);

        TextView title = dialog.findViewById(R.id.title);
        TextView date = dialog.findViewById(R.id.date);
        TextView download = dialog.findViewById(R.id.download);
        WebView webView = dialog.findViewById(R.id.webView);
        WebView addressWebView = dialog.findViewById(R.id.addressWebView);
        WebView productsWebView = dialog.findViewById(R.id.productsWebView);
        con = dialog.findViewById(R.id.con);
        date.setText(orderDate.getText().toString());

        title.setTypeface(AppFont.getBoldFont(context));
        download.setTypeface(AppFont.getBoldFont(context));

        setWebViewContent(webView, customerHtml);
        setWebViewContent(addressWebView, addressHtml);
        setWebViewContent(productsWebView, productsHtml);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            11);

                } else {
                    save_toMediaStore();

                }
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.setCancelable(true);
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        dialog.show();

    }

    private void setWebViewContent(WebView webView, String content) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setLoadsImagesAutomatically(true);

        webView.getSettings().setUseWideViewPort(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadDataWithBaseURL(null, content, null, "UTF-8", null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11 && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            save_toMediaStore();
        }
    }

    private void save_toMediaStore() {
        try {
            Bitmap bitmap = Utils.loadBitmapFromView(con);
            // create a new document
            PdfDocument pdfDocument = new PdfDocument();

            PdfDocument.PageInfo pi = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();

            PdfDocument.Page page = pdfDocument.startPage(pi);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#FFFFFF"));
            canvas.drawPaint(paint);

            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
            paint.setColor(Color.BLUE);
            canvas.drawBitmap(bitmap, 0, 0, null);
            pdfDocument.finishPage(page);
            final int min = 1000;
            final int max = 100000;
            final int random = new Random().nextInt((max - min) + 1) + min;
            File mypath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), orderCode.getText().toString() + " " + random + " adybelli facture.pdf");
            pdfDocument.writeTo(new FileOutputStream(mypath));
            // close the document
            pdfDocument.close();
            gotoShare(mypath);
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void gotoShare(File file) {
        Intent sendIntent;
        sendIntent = new Intent();
        // uri =
        Uri imageUri = Uri.fromFile(file);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            imageUri = FileProvider7.getUriForFile(context, file);
        }
//                    sendBroadcast(new Intent(
//                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

        //getImageContentUri(context,new File(root + "/req_images/"+fname));
//        if (!imageUri.toString().isEmpty()) {


        Log.d("URI", imageUri.toString());

        sendIntent.setAction(Intent.ACTION_SEND);
        //   sendIntent.putExtra(Intent.EXTRA_TEXT, cars3.getName()+" / "+cars3.getModel());
        sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        sendIntent.setType("application/pdf");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        context.startActivity(shareIntent);

    }


    private void setFonts() {
        faktura.setTypeface(AppFont.getBoldFont(context));
        orderCodeTitle.setTypeface(AppFont.getRegularFont(context));
        orderCode.setTypeface(AppFont.getSemiBoldFont(context));
        orderDateTitle.setTypeface(AppFont.getRegularFont(context));
        orderDate.setTypeface(AppFont.getSemiBoldFont(context));
        orderCountTitle.setTypeface(AppFont.getRegularFont(context));
        address.setTypeface(AppFont.getRegularFont(context));
        orderCount.setTypeface(AppFont.getSemiBoldFont(context));
        date.setTypeface(AppFont.getBoldFont(context));
        count.setTypeface(AppFont.getSemiBoldFont(context));
        tel.setTypeface(AppFont.getRegularFont(context));
        toleg.setTypeface(AppFont.getBoldFont(context));
        tt1.setTypeface(AppFont.getRegularFont(context));
        t2.setTypeface(AppFont.getRegularFont(context));
        t3.setTypeface(AppFont.getRegularFont(context));
        t4.setTypeface(AppFont.getRegularFont(context));
        t5.setTypeface(AppFont.getRegularFont(context));
        t6.setTypeface(AppFont.getRegularFont(context));
        t7.setTypeface(AppFont.getRegularFont(context));
        t8.setTypeface(AppFont.getRegularFont(context));
        totalCostTitle.setTypeface(AppFont.getBoldFont(context));
        totalCost.setTypeface(AppFont.getBoldFont(context));
        typeCost.setTypeface(AppFont.getBoldFont(context));
        back.setTypeface(AppFont.getBoldFont(context));
        canceled.setTypeface(AppFont.getSemiBoldFont(context));
        cancelOrder.setTypeface(AppFont.getSemiBoldFont(context));
    }
}