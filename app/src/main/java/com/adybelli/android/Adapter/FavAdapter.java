package com.adybelli.android.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Build;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Activity.ProductPage;
import com.adybelli.android.Api.APIClient;
import com.adybelli.android.Api.ApiInterface;
import com.adybelli.android.CategoryAdapter.SizeAdapter;
import com.adybelli.android.CategoryModel.Size;
import com.adybelli.android.Common.Constant;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.DataBaseHelper.FavDB;
import com.adybelli.android.DataBaseHelper.SameProductsDB;
import com.adybelli.android.Fragment.Favourite;
import com.adybelli.android.Object.AddToCardResponse;
import com.adybelli.android.Object.Favs;
import com.adybelli.android.Object.Product;
import com.adybelli.android.Object.RemoveFavResponse;
import com.adybelli.android.Post.AddToCardPost;
import com.adybelli.android.Post.DeleteFavPost;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Favs> products = new ArrayList<>();
    private ApiInterface apiInterface;
    public static Integer selectedSizeId = 0;
    private FavDB favDB;
    private View view;
    public FavAdapter(Context context, ArrayList<Favs> products,View view) {
        this.context = context;
        this.products = products;
        this.view=view;
        favDB = new FavDB(context);
        apiInterface = APIClient.getClient().create(ApiInterface.class);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fav_design, parent, false);
        return new FavAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FavAdapter.ViewHolder holder, int position) {
        Favs product = products.get(holder.getAbsoluteAdapterPosition());
        holder.cost.setText(product.getCost() + " TMT");
        holder.name.setText(product.getTrademark().getTitle());
        holder.desc.setText(product.getName());
        holder.old_cost.setText(product.getOldCost() + " TMT");
        holder.old_cost.setPaintFlags(holder.old_cost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        holder.count.setText("("+product.getCount()+")");

        Glide.with(context)
                .load(Constant.SERVER_URL + product.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.image);
//        holder.rating.setRating(product.getRating());

        holder.name.setTypeface(AppFont.getBoldFont(context));
        holder.desc.setTypeface(AppFont.getLightFont(context));
        holder.old_cost.setTypeface(AppFont.getRegularFont(context));
        holder.cost.setTypeface(AppFont.getSemiBoldFont(context));
        holder.count.setTypeface(AppFont.getSemiBoldFont(context));

        if (product.getOldCost()==null || product.getOldCost()<=0) {
            holder.old_cost.setVisibility(View.GONE);
        } else{
            holder.old_cost.setVisibility(View.VISIBLE);
        }

        holder.add_to_card.setTypeface(AppFont.getBoldFont(context));

        if (Utils.getLanguage(context).equals("ru")) {
            holder.desc.setText(product.getName_ru());
        }

        holder.add_to_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet(holder.getAbsoluteAdapterPosition(), product.getImageUrl(), holder.size, product.getCost() + "", product.getTrademark().getTitle(), product.getOldCost(), products.get(holder.getAbsoluteAdapterPosition()).getSizes(), holder.desc.getText().toString());
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // between two imageviews.
            Fade fade = new Fade();
            View decor = ((Activity) context).getWindow().getDecorView();
            // below 3 lines of code is to exclude
            // action bar,title bar and navigation
            // bar from animation.
            fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);

            // we are adding fade animation
            // for enter transition.
            ((Activity) context).getWindow().setEnterTransition(fade);
            // we are also setting fade
            // animation for exit transition.
            ((Activity) context).getWindow().setExitTransition(fade);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on image click we are opening new activity
                // and adding animation between this two activities.
                Intent intent = new Intent(context, ProductPage.class);
                intent.putExtra("id", product.getId());
                intent.putExtra("image", product.getImageUrl());
                // below method is used to make scene transition
                // and adding fade animation in it.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.image.setTransitionName("fade");
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            ((Activity) context), holder.image, ViewCompat.getTransitionName(holder.image));
                    // starting our activity with below method.
                    context.startActivity(intent, options.toBundle());
                } else {
                    context.startActivity(intent);
                }

            }
        });


        holder.size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    bottomSheet(holder.getAdapterPosition(), product.getImageUrl(), holder.size, product.getCost() + "", product.getTrademark().getTitle(), product.getOldCost(), product.getSizes(), holder.desc.getText().toString());

                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });


        Cursor cursor = favDB.getAll(products.get(holder.getAbsoluteAdapterPosition()).getFav_id() + "", products.get(holder.getAbsoluteAdapterPosition()).getId() + "");
        if (cursor.getCount() > 0) {
            String size = "";
            while (cursor.moveToNext()) {
                size = cursor.getString(4);
            }

            holder.size.setText(context.getResources().getString(R.string.size__) + ": " + size);
        } else {
            holder.size.setText(context.getResources().getString(R.string.size__));
        }
        cursor.moveToFirst();
        cursor.close();

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle(context.getResources().getString(R.string.pay_attention));
                alert.setMessage(context.getResources().getString(R.string.do_you_want_delete));
                alert.setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeFav(holder.getAbsoluteAdapterPosition());
                    }
                });

                alert.setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.setCancelable(false);
                alert.show();

            }
        });


    }

    private void removeFav(int position) {
        try {
            DeleteFavPost post = new DeleteFavPost(products.get(position).getId());
            Call<RemoveFavResponse> call = apiInterface.deleteFav("Bearer " + Utils.getSharedPreference(context, "tkn"), post,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
            call.enqueue(new Callback<RemoveFavResponse>() {
                @Override
                public void onResponse(Call<RemoveFavResponse> call, Response<RemoveFavResponse> response) {
                    if (response.isSuccessful()) {
                        products.remove(position);
                        notifyItemRemoved(position);
                        if(products.size()==0){
                            Favourite favourite=new Favourite();
                            favourite.showErrorView(R.drawable.fav_empty, context.getResources().getString(R.string.no_fav), context.getResources().getString(R.string.to_fav), context.getResources().getString(R.string.continueValue),false,context,view);
                        }
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<RemoveFavResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, fav;
        TextView name, desc, old_cost, cost, count;
        RatingBar rating;
        Button add_to_card, size;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            fav = itemView.findViewById(R.id.fav);
            desc = itemView.findViewById(R.id.desc);
            old_cost = itemView.findViewById(R.id.old_cost);
            cost = itemView.findViewById(R.id.cost);
            count = itemView.findViewById(R.id.count);
            rating = itemView.findViewById(R.id.rating);
            add_to_card = itemView.findViewById(R.id.add_to_card);
            size = itemView.findViewById(R.id.size);
        }
    }

    private void bottomSheet(int position, String imageUrl, Button textView, String costTxt, String nameTxt, Double oldCost, ArrayList<Size> sizes, String descTxt) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.size_list);
        RecyclerView rec = bottomSheetDialog.findViewById(R.id.sizeRec);
        ImageView close = bottomSheetDialog.findViewById(R.id.close);
        ImageView image = bottomSheetDialog.findViewById(R.id.image);

        Glide.with(context)
                .load(Constant.SERVER_URL + imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(image);

        TextView name = bottomSheetDialog.findViewById(R.id.name);
        TextView category = bottomSheetDialog.findViewById(R.id.category);
        TextView old_cost = bottomSheetDialog.findViewById(R.id.old_cost);
        TextView cost = bottomSheetDialog.findViewById(R.id.cost);
        TextView sizeTitle = bottomSheetDialog.findViewById(R.id.sizeTitle);
        Button acceptBtn = bottomSheetDialog.findViewById(R.id.acceptBtn);
        ProgressBar progress = bottomSheetDialog.findViewById(R.id.progress);

        name.setTypeface(AppFont.getBoldFont(context));
        category.setTypeface(AppFont.getRegularFont(context));
        old_cost.setTypeface(AppFont.getRegularFont(context));
        cost.setTypeface(AppFont.getBoldFont(context));
        sizeTitle.setTypeface(AppFont.getBoldFont(context));
        acceptBtn.setTypeface(AppFont.getBoldFont(context));

        cost.setText(costTxt + " TMT");
        name.setText(nameTxt);
        old_cost.setText(oldCost + " TMT");
        category.setText(descTxt);

        if (oldCost == null) {
            old_cost.setVisibility(View.GONE);
        }


        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedSizeId != null) {
                    if (selectedSizeId != 0)
                        addToCard(position, bottomSheetDialog, progress, acceptBtn);
                    else
                        Utils.showCustomToast(Utils.getStringResource(R.string.fill_out, context), R.drawable.ic_outline_info_24, context, R.drawable.toast_bg);
                } else {
                    Utils.showCustomToast(Utils.getStringResource(R.string.fill_out, context), R.drawable.ic_outline_info_24, context, R.drawable.toast_bg);
                }

            }
        });

        try {
            old_cost.setPaintFlags(old_cost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog.dismiss();
                }
            });
//            ArrayList<Size> sizes = products.get(position).getSizes();

            if (sizes != null) {
                if (sizes.size() > 0) {
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
                    SizeOfFit adapter = new SizeOfFit(sizes, context, textView, position, products.get(position).getFav_id() + "", products.get(position).getId() + "");
                    rec.setLayoutManager(linearLayoutManager);
                    rec.setAdapter(adapter);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        bottomSheetDialog.show();
    }

    private void addToCard(int position, BottomSheetDialog bottomSheetDialog, ProgressBar progress, Button acceptBtn) {
        acceptBtn.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        apiInterface = APIClient.getClient().create(ApiInterface.class);
        AddToCardPost post = new AddToCardPost(products.get(position).getId(), selectedSizeId, 1);
        Call<AddToCardResponse> call = apiInterface.addToCard("Bearer " + Utils.getSharedPreference(context, "tkn"), post,Utils.getLanguage(context).isEmpty()?"tm":Utils.getLanguage(context));
        call.enqueue(new Callback<AddToCardResponse>() {
            @Override
            public void onResponse(Call<AddToCardResponse> call, Response<AddToCardResponse> response) {
                if (response.isSuccessful()) {
                    SameProductsDB sameProductsDB = new SameProductsDB(context);
                    sameProductsDB.insert(products.get(position) + "");
                    MainActivity mainActivity = MainActivity.get();
                    mainActivity.getBottomNavigationView().setSelectedItemId(R.id.basket);
                    bottomSheetDialog.dismiss();


                } else {
                    acceptBtn.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                    Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong, context), R.drawable.ic_baseline_close_24, context, R.drawable.toast_bg_error);
                }
            }

            @Override
            public void onFailure(Call<AddToCardResponse> call, Throwable t) {
                acceptBtn.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                Utils.showCustomToast(Utils.getStringResource(R.string.something_went_wrong, context), R.drawable.ic_baseline_close_24, context, R.drawable.toast_bg_error);
            }
        });
    }

    class SizeOfFit extends RecyclerView.Adapter<SizeOfFit.ViewHolder> {
        private ArrayList<Size> sizes = new ArrayList<>();
        private Context context;
        private boolean isFirstTime = true;
        private Button oldButton;
        private Button textView;
        private int pos;
        private FavDB favDB;
        private String fav_id;
        private String prod_id;
        private Cursor cursor;

        public SizeOfFit(ArrayList<Size> sizes, Context context, Button textView, int pos, String fav_id, String prod_id) {
            this.sizes = sizes;
            this.context = context;
            this.textView = textView;
            this.pos = pos;
            this.fav_id = fav_id;
            this.prod_id = prod_id;
            favDB = new FavDB(context);
        }

        @NonNull
        @NotNull
        @Override
        public SizeOfFit.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.size_two, parent, false);
            return new SizeOfFit.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull SizeOfFit.ViewHolder holder, int position) {
            Size size = sizes.get(position);
            holder.sizeBtn.setTypeface(AppFont.getRegularFont(context));
            holder.sizeBtn.setText(size.getSize() + "");


            if (size.getStockQuantity() != null) {
                if (size.getStockQuantity() == 0) {
                    setDisable(holder.sizeBtn);
                } else {
                    cursor = favDB.getAll(fav_id, prod_id);
                    if (cursor.getCount() > 0) {
                        String size_id = "";
                        while (cursor.moveToNext()) {
                            size_id = cursor.getString(3);
                        }
                        if (size_id.equals(size.getId() + "")) {
                            setActive(holder.sizeBtn);
                            oldButton = holder.sizeBtn;
                            products.get(pos).setSelected(Integer.parseInt(size.getSize()));
                            FavAdapter.this.notifyItemChanged(position);
                            textView.setText(context.getResources().getString(R.string.size__) + " " + size.getSize());
                            selectedSizeId = size.getId();
                        }
                    }
                }
            } else {
                cursor = favDB.getAll(fav_id, prod_id);
                if (cursor.getCount() > 0) {
                    String size_id = "";
                    while (cursor.moveToNext()) {
                        size_id = cursor.getString(3);
                    }
                    if (size_id.equals(size.getId() + "")) {
                        setActive(holder.sizeBtn);
                        oldButton = holder.sizeBtn;
                        products.get(pos).setSelected(Integer.parseInt(size.getSize()));
                        FavAdapter.this.notifyItemChanged(position);
                        textView.setText(context.getResources().getString(R.string.size__) + " " + size.getSize());
                        selectedSizeId = size.getId();
                    }
                }
            }


            holder.sizeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (selectedSizeId == size.getId()) {
                        setPassive(holder.sizeBtn);
                        oldButton = null;
                        selectedSizeId = 0;
                        favDB.deleteData(products.get(pos).getId() + "");
                        textView.setText(context.getResources().getString(R.string.size__));
                        return;
                    }

                    if (size.getStockQuantity() != null) {
                        if (size.getStockQuantity() == 0) {
                            Toast.makeText(context, Utils.getStringResource(R.string.no_product_in_stock, context), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    textView.setText(context.getResources().getString(R.string.size__) + " " + size.getSize());

                    if (holder.sizeBtn == oldButton) {
                        return;
                    }
                    if (cursor.getCount() == 0) {
                        favDB.insert(fav_id, prod_id, size.getId() + "", size.getSize());
                    } else {
                        favDB.updateData(fav_id, prod_id, size.getId() + "", size.getSize());
                    }

                    cursor = favDB.getAll(fav_id, prod_id);

                    setActive(holder.sizeBtn);
                    if (oldButton != null) {
                        setPassive(oldButton);
                    }
                    oldButton = holder.sizeBtn;
                    selectedSizeId = size.getId();
                }
            });
        }

        private void setActive(Button button) {
            button.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            button.setTextColor(context.getResources().getColor(R.color.white));
            button.setTypeface(AppFont.getBoldFont(context));
        }

        private void setPassive(Button button) {
            button.setBackgroundColor(context.getResources().getColor(R.color.white));
            button.setBackgroundResource(R.drawable.stroke);
            button.setTextColor(context.getResources().getColor(R.color.black));
            button.setTypeface(AppFont.getRegularFont(context));
        }

        private void setDisable(Button button) {
            button.setBackgroundColor(context.getResources().getColor(R.color.white));
            button.setBackgroundResource(R.drawable.ic_group_1);
            button.setTextColor(context.getResources().getColor(R.color.inactive));
            button.setTypeface(AppFont.getRegularFont(context));
        }

        @Override
        public int getItemCount() {
            return sizes.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            Button sizeBtn;

            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                sizeBtn = itemView.findViewById(R.id.sizeBtn);
            }
        }
    }
}
