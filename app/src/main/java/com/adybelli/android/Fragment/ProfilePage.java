package com.adybelli.android.Fragment;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Activity.SearchPage;
import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Util;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Object.AddAddressResponse;
import com.adybelli.android.Object.GetLocations;
import com.adybelli.android.Post.AddAddressPost;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.adybelli.android.View.Custom_Spinner;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfilePage extends Fragment {

    private View view;
    private Context context;
    private LinearLayout orders,profile,address,help,logout,language,termsOfUse,about;
    private TextView title,ordersTv,profileTv,addressTv,helpTv,logoutTv,languageTv,termsTv,aboutTv;
    private FragmentManager fragmentManager;
    private BottomSheetDialog bottomSheetDialog;
    public ProfilePage() {
    }


    public static ProfilePage newInstance() {
        Bundle args = new Bundle();
        ProfilePage fragment = new ProfilePage();
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
        context = getContext();
        Utils.loadLocal(context);
        view=inflater.inflate(R.layout.fragment_profile_page, container, false);
        initComponents();
        setListeners();

        return view;
    }

    private void initComponents(){
        orders=view.findViewById(R.id.orders);
        profile=view.findViewById(R.id.profile);
        address=view.findViewById(R.id.address);
        help=view.findViewById(R.id.help);
        ordersTv=view.findViewById(R.id.ordersTv);
        profileTv=view.findViewById(R.id.profileTv);
        addressTv=view.findViewById(R.id.addressTv);
        helpTv=view.findViewById(R.id.helpTv);
        title=view.findViewById(R.id.title);
        logoutTv=view.findViewById(R.id.logoutTv);
        logout=view.findViewById(R.id.logout);
        languageTv=view.findViewById(R.id.languageTv);
        language=view.findViewById(R.id.language);
        termsOfUse=view.findViewById(R.id.termsOfUse);
        about=view.findViewById(R.id.about);
        termsTv=view.findViewById(R.id.termsTv);
        aboutTv=view.findViewById(R.id.aboutTv);

        title.setTypeface(AppFont.getBoldFont(context));
        ordersTv.setTypeface(AppFont.getRegularFont(context));
        profileTv.setTypeface(AppFont.getRegularFont(context));
        addressTv.setTypeface(AppFont.getRegularFont(context));
        helpTv.setTypeface(AppFont.getRegularFont(context));
        logoutTv.setTypeface(AppFont.getRegularFont(context));
        languageTv.setTypeface(AppFont.getRegularFont(context));
        aboutTv.setTypeface(AppFont.getRegularFont(context));
        termsTv.setTypeface(AppFont.getRegularFont(context));

        view.findViewById(R.id.search).setVisibility(View.GONE);
    }

    private void setListeners(){
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideAdd(new Orders(), Orders.class.getSimpleName(),getFragmentManager(),R.id.content);
                MainActivity.fifthFragment=new Orders();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantPage constantPage=new ConstantPage("help");
                Utils.hideAdd(constantPage, ConstantPage.class.getSimpleName(),getFragmentManager(),R.id.content);
                MainActivity.fifthFragment=constantPage;
            }
        });

        termsOfUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantPage constantPage=new ConstantPage("privacy");
                Utils.hideAdd(constantPage, ConstantPage.class.getSimpleName(),getFragmentManager(),R.id.content);
                MainActivity.fifthFragment=constantPage;
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantPage constantPage=new ConstantPage("about");
                Utils.hideAdd(constantPage, ConstantPage.class.getSimpleName(),getFragmentManager(),R.id.content);
                MainActivity.fifthFragment=constantPage;
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideAdd(new Profile(), Profile.class.getSimpleName(),getFragmentManager(),R.id.content);
                MainActivity.fifthFragment=new Profile();
            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideAdd(new Addres(1), Addres.class.getSimpleName(),getFragmentManager(),R.id.content);
                MainActivity.fifthFragment=new Addres(1);
            }
        });

        view.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, SearchPage.class);
                context.startActivity(intent);
            }
        });

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle(context.getResources().getString(R.string.logout_));
                alert.setMessage(context.getResources().getString(R.string.do_you_want_logout));
                alert.setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Utils.setSharedPreference(context,"tkn", "");
                        Utils.setSharedPreference(context,"userId","");
                        MainActivity.firstFragment = new Home();
                        MainActivity.secondFragment = new Search();
                        MainActivity.thirdFragment = new Favourite();
                        MainActivity.fourthFragment = new Basket();
                        MainActivity.fifthFragment = new ProfileRootFragment();
                        MainActivity.setBottomNavigationSelectedItem(getActivity(),R.id.main);
                        restart();
                    }
                });
                alert.show();

            }
        });

        
    }


    private void showDialog() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.change_language);
        TextView first=bottomSheetDialog.findViewById(R.id.first);
        TextView second=bottomSheetDialog.findViewById(R.id.second);

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Utils.setLocale("tm",context);
                MainActivity.firstFragment = new Home();
                MainActivity.secondFragment = new Search();
                MainActivity.thirdFragment = new Favourite();
                MainActivity.fourthFragment = new Basket();
                MainActivity.fifthFragment = new ProfileRootFragment();
                MainActivity.setBottomNavigationSelectedItem(getActivity(),R.id.main);
                restart();
            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Utils.setLocale("ru",context);
                MainActivity.firstFragment = new Home();
                MainActivity.secondFragment = new Search();
                MainActivity.thirdFragment = new Favourite();
                MainActivity.fourthFragment = new Basket();
                MainActivity.fifthFragment = new ProfileRootFragment();
                MainActivity.setBottomNavigationSelectedItem(getActivity(),R.id.main);
                restart();

            }
        });
        bottomSheetDialog.show();





    }

    private void restart() {
        Intent i = context.getPackageManager().
                getLaunchIntentForPackage(context.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        getActivity().finish();
    }
}