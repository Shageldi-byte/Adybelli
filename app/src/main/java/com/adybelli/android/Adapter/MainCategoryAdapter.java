package com.adybelli.android.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.Common.Constant;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Fragment.Search;
import com.adybelli.android.Object.Catalog;
import com.adybelli.android.Object.CatalogBody;
import com.adybelli.android.Object.Category;
import com.adybelli.android.Object.GetCategory;
import com.adybelli.android.Object.MainCategory;
import com.adybelli.android.Object.OfflineMainCategory;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.bumptech.glide.Glide;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ViewHolder> {
    private ArrayList<MainCategory> mainCategories = new ArrayList<>();
    private Context context;
    private RecyclerView recyclerView;
    private FragmentManager fragmentManager;
    private TextView oldTv;
    private MaterialCardView oldCard;
    private int active;
    boolean isFirstTime = true;
    private ApiInterface apiInterface;
    public static ArrayList<OfflineMainCategory> offlineMainCategories=new ArrayList<>();


    public MainCategoryAdapter(ArrayList<MainCategory> mainCategories, Context context, RecyclerView recyclerView, FragmentManager fragmentManager, int active) {
        this.mainCategories = mainCategories;
        this.context = context;
        this.recyclerView = recyclerView;
        this.fragmentManager = fragmentManager;
        this.active = active;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(((Activity) context), R.style.Material);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = LayoutInflater.from(context).cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.main_category_design, parent, false);
        return new MainCategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MainCategoryAdapter.ViewHolder holder, int position) {
        MainCategory mainCategory = mainCategories.get(position);
        holder.title.setText(mainCategory.getName());
        holder.title.setTypeface(AppFont.getRegularFont(context));
        Glide.with(context)
                .load(Constant.SERVER_URL+mainCategory.getImage())
                .placeholder(R.drawable.placeholder).centerCrop()
                .error(R.drawable.placeholder).centerCrop()
                .into(holder.image);

        if(Search.categoryID!=null){
            if(Search.categoryID.equals(mainCategory.getId()+"")){
                if (oldCard != null) {
                    unSelect(oldCard, oldTv);
                }
                oldCard = holder.card;
                oldTv = holder.title;
                select(holder.card, holder.title);
                isFirstTime = false;
                category(recyclerView, mainCategory.getId(), holder.card, holder.title);
            }
        } else if (isFirstTime) {
            if (oldCard != null) {
                unSelect(oldCard, oldTv);
            }
            oldCard = holder.card;
            oldTv = holder.title;
            select(holder.card, holder.title);
            isFirstTime = false;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search.subCategoryID=null;
                Search.oldSubCategoryID=null;
                category(recyclerView, mainCategory.getId(), holder.card, holder.title);

            }
        });

        if(Utils.getLanguage(context).equals("ru")){
            holder.title.setText(mainCategory.getTitle_ru());
        }
    }

    @Override
    public int getItemCount() {
        return mainCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        MaterialCardView card;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            card = itemView.findViewById(R.id.card);
        }
    }

    private void category(RecyclerView rec, int id, MaterialCardView cardView, TextView tv) {
        Search.categoryID=id+"";
        Search.oldCategoryID=id+"";

        for(OfflineMainCategory offlineMainCategory:offlineMainCategories){
            if(offlineMainCategory.getId()==id){
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                CategoryAdapter adapter = new CategoryAdapter(offlineMainCategory.getMainCategories(), context, fragmentManager);
                rec.setAdapter(adapter);
                rec.setLayoutManager(linearLayoutManager);
                if (oldCard != null) {
                    unSelect(oldCard, oldTv);
                }
                oldCard = cardView;
                oldTv = tv;
                select(cardView, tv);
                return;
            }
        }
        CategoryAdapter adapter2=new CategoryAdapter(new ArrayList<Category>(),context, fragmentManager);
        rec.setLayoutManager(new GridLayoutManager(context,3));
        SkeletonScreen subCategorySkelton = Skeleton.bind(rec)
                .adapter(adapter2)
                .shimmer(true)
                .angle(20)
                .count(15)
                .color(R.color.skeletonColor)
                .load(R.layout.skelton_main_category)
                .show(); //default count is 10
        apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<GetCategory> call = apiInterface.getCategories(id,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
        call.enqueue(new Callback<GetCategory>() {
            @Override
            public void onResponse(Call<GetCategory> call, Response<GetCategory> response) {
                Log.e("Code", response.code() + "");
                if (response.isSuccessful()) {
                    subCategorySkelton.hide();
                    GetCategory resource = response.body();
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    CategoryAdapter adapter = new CategoryAdapter((ArrayList<Category>) resource.getBody(), context, fragmentManager);
                    rec.setAdapter(adapter);
                    rec.setLayoutManager(linearLayoutManager);
                    if (oldCard != null) {
                        unSelect(oldCard, oldTv);
                    }
                    oldCard = cardView;
                    oldTv = tv;
                    select(cardView, tv);
                    offlineMainCategories.add(new OfflineMainCategory((ArrayList<Category>) resource.getBody(),id));
                }
            }

            @Override
            public void onFailure(Call<GetCategory> call, Throwable t) {
                call.cancel();
                Log.e("Error", t.getMessage());
                Snackbar.make(rec, context.getResources().getString(R.string.noInternet), Snackbar.LENGTH_LONG).setAction(context.getResources().getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        category(rec, id, cardView, tv);
                    }
                }).show();
            }
        });

    }

    private void select(MaterialCardView card, TextView textView) {
        card.setStrokeColor(context.getResources().getColor(R.color.colorAccent));
        card.setStrokeWidth(6);
        textView.setTextColor(context.getResources().getColor(R.color.colorAccent));
        textView.setTypeface(AppFont.getSemiBoldFont(context));
    }

    private void unSelect(MaterialCardView card, TextView textView) {
        card.setStrokeColor(context.getResources().getColor(android.R.color.transparent));
        card.setStrokeWidth(0);
        textView.setTextColor(context.getResources().getColor(R.color.black));
        textView.setTypeface(AppFont.getRegularFont(context));
    }
}
