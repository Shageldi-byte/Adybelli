package com.adybelli.android.Fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Adapter.AddressAdapter;
import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Object.AddressList;
import com.adybelli.android.Object.CreateOrderResponse;
import com.adybelli.android.Object.GetUserCardBody;
import com.adybelli.android.Object.GetUserCardInfo;
import com.adybelli.android.Post.CreateOrderPost;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProcceedCheckout extends Fragment {

    private View view;
    private Context context;
    private RecyclerView rec;
    private ArrayList<AddressList> addressLists = new ArrayList<>();
    private TextView back, available, deliveryTitle, deliverTime, workTitle, workTime, addressTitle, type, acceptBtn, costTitle, sumOfCost;
    private PowerSpinnerView dr1;
    private PowerSpinnerView dr2;
    private ArrayList<String> welayatlar = new ArrayList<>();
    private ArrayList<String> etraplar = new ArrayList<>();
    private RelativeLayout cost_dialog, bottomMenu, trbg;
    private ImageView icon;
    private Animation start, exit;
    private RadioButton nagtToleg, terminal, online;
    private TextView aboutTitle, t1, t2, t3, t4, t5, t6, t7, t8, totalCostTitle, totalCost;
    private String selected_list_ids;
    private int payment_method = 1;
    private ApiInterface apiInterface;
    private KProgressHUD kProgressHUD;
    private double total = 0;
    private static ProcceedCheckout INSTANCE;
    private ArrayList<String> selected_list=new ArrayList<>();
    private String base64="";
    public ProcceedCheckout(String selected_list_ids,ArrayList<String> selected_list) {
        this.selected_list_ids = selected_list_ids;
        this.selected_list=selected_list;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.Material);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        view = localInflater.inflate(R.layout.fragment_procceed_checkout, container, false);
        context = getContext();
        INSTANCE = this;

        initComponents();
        setListener();
        setCosts(0);
        return view;
    }

    private void initComponents() {
        rec = view.findViewById(R.id.rec);
        back = view.findViewById(R.id.back);
        bottomMenu = view.findViewById(R.id.bottomMenu);
        icon = view.findViewById(R.id.icon);
        trbg = view.findViewById(R.id.trbg);
        cost_dialog = view.findViewById(R.id.cost_dialog);
        nagtToleg = view.findViewById(R.id.nagtToleg);
        terminal = view.findViewById(R.id.terminal);
        online = view.findViewById(R.id.online);
        start = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        exit = AnimationUtils.loadAnimation(context, R.anim.slide_down);

        available = view.findViewById(R.id.available);
        deliveryTitle = view.findViewById(R.id.deliveryTitle);
        deliverTime = view.findViewById(R.id.deliverTime);
        workTitle = view.findViewById(R.id.workTitle);
        workTime = view.findViewById(R.id.workTime);
        addressTitle = view.findViewById(R.id.addressTitle);
        type = view.findViewById(R.id.type);
        acceptBtn = view.findViewById(R.id.acceptBtn);
        costTitle = view.findViewById(R.id.costTitle);
        sumOfCost = view.findViewById(R.id.sumOfCost);

        aboutTitle = view.findViewById(R.id.aboutTitle);
        t1 = view.findViewById(R.id.t1);
        t2 = view.findViewById(R.id.t2);
        t3 = view.findViewById(R.id.t3);
        t4 = view.findViewById(R.id.t4);
        t5 = view.findViewById(R.id.t5);
        t6 = view.findViewById(R.id.t6);
        t7 = view.findViewById(R.id.t7);
        t8 = view.findViewById(R.id.t8);
        totalCostTitle = view.findViewById(R.id.totalCostTitle);
        totalCost = view.findViewById(R.id.totalCost);

        back.setTypeface(AppFont.getBoldFont(context));
        nagtToleg.setTypeface(AppFont.getSemiBoldFont(context));
        terminal.setTypeface(AppFont.getSemiBoldFont(context));
        online.setTypeface(AppFont.getSemiBoldFont(context));
        available.setTypeface(AppFont.getRegularFont(context));
        deliveryTitle.setTypeface(AppFont.getBoldFont(context));
        deliverTime.setTypeface(AppFont.getRegularFont(context));
        workTitle.setTypeface(AppFont.getBoldFont(context));
        workTime.setTypeface(AppFont.getRegularFont(context));
        addressTitle.setTypeface(AppFont.getBoldFont(context));
        type.setTypeface(AppFont.getBoldFont(context));
        acceptBtn.setTypeface(AppFont.getBoldFont(context));
        costTitle.setTypeface(AppFont.getRegularFont(context));
        sumOfCost.setTypeface(AppFont.getBoldFont(context));

        aboutTitle.setTypeface(AppFont.getBoldFont(context));
        t1.setTypeface(AppFont.getRegularFont(context));
        t2.setTypeface(AppFont.getRegularFont(context));
        t3.setTypeface(AppFont.getRegularFont(context));
        t4.setTypeface(AppFont.getRegularFont(context));
        t5.setTypeface(AppFont.getRegularFont(context));
        t6.setTypeface(AppFont.getRegularFont(context));
        t7.setTypeface(AppFont.getRegularFont(context));
        t8.setTypeface(AppFont.getRegularFont(context));
        totalCostTitle.setTypeface(AppFont.getBoldFont(context));
        totalCost.setTypeface(AppFont.getBoldFont(context));

        getFragmentManager().beginTransaction().replace(R.id.addressFragment, new AddresInCards(2,selected_list_ids)).commit();

        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setWindowColor(getResources().getColor(R.color.transparentBlack))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);


    }


    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.removeShow(new Basket(), Basket.class.getSimpleName(), getFragmentManager(), R.id.content);
                MainActivity.fourthFragment = new Basket();
            }
        });


        bottomMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cost_dialog.getVisibility() == View.GONE) {
                    trbg.setVisibility(View.VISIBLE);
                    cost_dialog.startAnimation(start);
                    cost_dialog.setVisibility(View.VISIBLE);
                    icon.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);

                } else {
                    trbg.setVisibility(View.GONE);
                    cost_dialog.setVisibility(View.GONE);
                    cost_dialog.startAnimation(exit);
                    icon.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                }
            }
        });

        trbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trbg.setVisibility(View.GONE);
                cost_dialog.setVisibility(View.GONE);
                cost_dialog.startAnimation(exit);
                icon.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
            }
        });

        nagtToleg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    terminal.setChecked(false);
                    online.setChecked(false);
                    payment_method = 1;
                }
            }
        });

        terminal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    nagtToleg.setChecked(false);
                    online.setChecked(false);
                    payment_method = 2;
                }

            }
        });

        online.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    nagtToleg.setChecked(false);
                    terminal.setChecked(false);
                    payment_method = 3;
                }

            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(AddressAdapter.addressId==null || AddressAdapter.addressId==0 || selected_list_ids.isEmpty()){
                    Utils.showCustomToast(Utils.getStringResource(R.string.fill_out, context),R.drawable.ic_outline_info_24,context,R.drawable.toast_bg);
                    return;
                }
                showDialog();

            }
        });


    }


    private void showDialog(){
        if(AddresInCards.bodyResponse==null){
            Utils.showCustomToast(Utils.getStringResource(R.string.fill_out, context),R.drawable.ic_outline_info_24,context,R.drawable.toast_bg);
            return;
        }
        if(AddresInCards.bodyResponse.getCaptcha()==null){
            Utils.showCustomToast(Utils.getStringResource(R.string.fill_out, context),R.drawable.ic_outline_info_24,context,R.drawable.toast_bg);
            return;
        }
        Dialog dialog = new Dialog(context);
        final Context contextThemeWrapper = new ContextThemeWrapper(((Activity) context), R.style.Material);
        LayoutInflater localInflater = LayoutInflater.from(context).cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.captcha, null, false);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;

        ImageView close = dialog.findViewById(R.id.close);
        ImageView captchaImage = dialog.findViewById(R.id.captchaImage);
        Button acceptBtn = dialog.findViewById(R.id.acceptBtn);
        Button refresh = dialog.findViewById(R.id.refresh);
        EditText editText = dialog.findViewById(R.id.editText);
        TextView title2 = dialog.findViewById(R.id.title2);

        title2.setTypeface(AppFont.getBoldFont(context));
        editText.setTypeface(AppFont.getBoldFont(context));
        acceptBtn.setTypeface(AppFont.getSemiBoldFont(context));
        refresh.setTypeface(AppFont.getSemiBoldFont(context));
        try {
            base64 = AddresInCards.bodyResponse.getCaptcha();

            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            captchaImage.setImageBitmap(decodedByte);
        } catch (Exception ex){
            ex.printStackTrace();
            Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong, context),R.drawable.ic_baseline_close_24,context,R.drawable.toast_bg_error);
            return;
        }


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFromServerBase64(captchaImage);
            }
        });


        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editText.getText().toString().trim().isEmpty()){
                    Utils.showCustomToast(Utils.getStringResource(R.string.fill_out, context),R.drawable.ic_outline_info_24,context,R.drawable.toast_bg);
                    return;
                }

                dialog.dismiss();


                kProgressHUD.show();
                apiInterface = APIClient.getClient().create(ApiInterface.class);
                CreateOrderPost post = new CreateOrderPost(1, AddressAdapter.addressId, payment_method, selected_list_ids,editText.getText().toString());
                Call<CreateOrderResponse> call = apiInterface.createOrder("Bearer " + Utils.getSharedPreference(context, "tkn"), post,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
                call.enqueue(new Callback<CreateOrderResponse>() {
                    @Override
                    public void onResponse(Call<CreateOrderResponse> call, Response<CreateOrderResponse> response) {
                        if (response.isSuccessful()) {
                            kProgressHUD.dismiss();
                            AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
                            alertDialog.setMessage(Utils.getStringResource(R.string.success_order, context));
                            alertDialog.setPositiveButton(Utils.getStringResource(R.string.ok, context), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Utils.removeShow(new Basket(), Basket.class.getSimpleName(), getFragmentManager(), R.id.content);
                                    MainActivity.fourthFragment = new Basket();
                                }
                            });
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                            Utils.showCustomToast(Utils.getStringResource(R.string.success_order, context),R.drawable.ic_baseline_check_circle_outline_24,context,R.drawable.toast_bg_success);
                        } else{
                            String lang=Utils.getLanguage(context);
                            String message=Utils.getStringResource(R.string.something_went_wrong, context);
                            try {
                                if (response.body().getMessage() != null) {
                                    message = response.body().getMessage().getText();
                                    if (lang.equals("ru"))
                                        message = response.body().getMessage().getText_ru();
                                }
                            } catch (Exception ex){
                                ex.printStackTrace();
                            }
                            kProgressHUD.dismiss();
                            Utils.showCustomToast(message,R.drawable.ic_baseline_close_24,context,R.drawable.toast_bg_error);
                        }

                    }

                    @Override
                    public void onFailure(Call<CreateOrderResponse> call, Throwable t) {
                        kProgressHUD.dismiss();
                        Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong, context),R.drawable.ic_baseline_close_24,context,R.drawable.toast_bg_error);
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

        dialog.setCancelable(true);
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void getFromServerBase64(ImageView captchaImage) {
        kProgressHUD.show();
        apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<GetUserCardInfo> call = apiInterface.getUserCartInfo("Bearer " + Utils.getSharedPreference(context, "tkn"), selected_list_ids,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
        call.enqueue(new Callback<GetUserCardInfo>() {
            @Override
            public void onResponse(Call<GetUserCardInfo> call, Response<GetUserCardInfo> response) {
                if (response.isSuccessful()) {
                    try {
                        AddresInCards.bodyResponse=response.body().getBody();
                        base64=response.body().getBody().getCaptcha();
                        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        captchaImage.setImageBitmap(decodedByte);
                    } catch (Exception ex){
                        ex.printStackTrace();
                        Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong, context),R.drawable.ic_baseline_close_24,context,R.drawable.toast_bg_error);
                        return;
                    }
                }
                kProgressHUD.dismiss();
            }

            @Override
            public void onFailure(Call<GetUserCardInfo> call, Throwable t) {
                call.cancel();
                kProgressHUD.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static void setCosts(double deliveryCost) {
        try {
            ProcceedCheckout basket = ProcceedCheckout.get();
            Basket basket2 = Basket.get();
            double discount = 0;
            double all = 0;
            double total = 0;
            for (GetUserCardBody item : basket2.getArrayList()) {
                if(basket2.getSelected_id_list().contains(item.getCart_id())) {
                    total += (item.getSale_price() * item.getCount());
                    if (item.getPrice() != null) {
                        all += item.getPrice() * item.getCount();
                        discount += (item.getPrice()-item.getSale_price()) * item.getCount();
                    } else {
                        all += item.getSale_price() * item.getCount();
                    }
                }
            }
            double discountPresent = 0;
            if (discount > 0) {
                discountPresent = (discount * 100) / all;
                Log.e("Formula","("+discount+" * 100) / "+all);
            } else {
                basket.getView().findViewById(R.id.free_container).setVisibility(View.GONE);
            }
            double total2 = total;
            total += deliveryCost;

            basket.getT4().setText(deliveryCost + " TMT");


            String sum = Utils.fixNumber(total);
            String sum2 = Utils.fixNumber(total2);
            String allStr = Utils.fixNumber(all);
            String disStr = Utils.fixNumber(discount);
            String disPresentStr = Utils.fixNumber(discountPresent);
            basket.getSumOfCost().setText(sum + " TMT");
            basket.getT2().setText(allStr + " TMT");
            basket.getT6().setText("-" + disStr + " TMT");
            basket.getT5().setText(Utils.getStringResource(R.string.free_cost, basket.getContext()) + " (-" + disPresentStr + "%):");
            basket.getTotalCost().setText(sum + " TMT");
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static ProcceedCheckout get() {
        return INSTANCE;
    }


    public TextView getSumOfCost() {
        return sumOfCost;
    }

    public TextView getT6() {
        return t6;
    }

    public TextView getT2() {
        return t2;
    }

    public TextView getT5() {
        return t5;
    }

    public TextView getT3() {
        return t3;
    }

    public TextView getT4() {
        return t4;
    }

    public TextView getTotalCost() {
        return totalCost;
    }

    public View getView() {
        return view;
    }

    public Context context() {
        return context;
    }


}