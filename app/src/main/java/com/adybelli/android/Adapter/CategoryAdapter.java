package com.adybelli.android.Adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adybelli.android.Activity.MainActivity;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Fragment.Products;
import com.adybelli.android.Fragment.Search;
import com.adybelli.android.Object.Category;
import com.adybelli.android.Object.SubCategory;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    ArrayList<Category> categories = new ArrayList<>();
    Context context;
    Animation rotateLess, rotateMore;
    FragmentManager supportFragmentManager;
    boolean isFirstTime = true;
    RecyclerView old_rec = null;
    ImageView old_arrow = null;
    boolean isOpened = false;

    public CategoryAdapter(ArrayList<Category> categories, Context context, FragmentManager supportFragmentManager) {
        this.categories = categories;
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.accordion, parent, false);
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryAdapter.ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.setIsRecyclable(false);
        rotateLess = AnimationUtils.loadAnimation(context, R.anim.rotate_less);
        rotateMore = AnimationUtils.loadAnimation(context, R.anim.rotate_more);

        List<SubCategory> subCategories = new ArrayList<>();
        subCategories.addAll(category.getChildren());
        subCategories.add(new SubCategory(category.getId(),Utils.getStringResource(R.string.see_all_tm,context),Utils.getStringResource(R.string.see_all_ru,context),"",1));
        SubCategoryAdapter adapter = new SubCategoryAdapter(subCategories, context, supportFragmentManager);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        holder.recyclerView.setLayoutManager(gridLayoutManager);
        holder.recyclerView.setAdapter(adapter);


        if(Search.subCategoryID!=null){
            if(Search.subCategoryID.equals(category.getId()+"")){
                expand(holder.recyclerView, holder.arrow,false);
                isFirstTime = false;
                isOpened=true;
            }
        } else if (isFirstTime) {
            expand(holder.recyclerView, holder.arrow,false);
            isFirstTime = false;
        }
        if (subCategories.size() == 0) {
            holder.arrow.setVisibility(View.GONE);
        }

        holder.title.setText(category.getTitle());

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search.subCategoryID=category.getId()+"";
                Search.oldSubCategoryID=category.getId()+"";
                if (holder.recyclerView.getVisibility() == View.VISIBLE) {
                    collapse(holder.recyclerView, holder.arrow);
                } else if (holder.recyclerView.getVisibility() == View.GONE) {
                    expand(holder.recyclerView, holder.arrow,true);
                }

                if (category.getChildren().size() <= 0) {
                    Products products = new Products();
                    products.catId = category.getId() + "";
                    products.catName = category.getTitle();
                    Products.where="cats";
                    Utils.productFragment(products, Products.class.getSimpleName(), supportFragmentManager, R.id.content);
                    MainActivity.secondFragment = new Products();
                }
            }
        });




        holder.con1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search.subCategoryID=category.getId()+"";
                if (holder.recyclerView.getVisibility() == View.VISIBLE) {
                    collapse(holder.recyclerView, holder.arrow);
                } else if (holder.recyclerView.getVisibility() == View.GONE) {
                    expand(holder.recyclerView, holder.arrow,true);
                }

                if (category.getChildren().size() <= 0) {
                    Products products = new Products();
                    products.catId = category.getId() + "";
                    products.catName = category.getTitle();
                    Products.where="cats";
                    Utils.productFragment(products, Products.class.getSimpleName(), supportFragmentManager, R.id.content);
                    MainActivity.secondFragment = new Products();
                }
            }
        });

        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search.subCategoryID=category.getId()+"";
                if (holder.recyclerView.getVisibility() == View.VISIBLE) {
                    collapse(holder.recyclerView, holder.arrow);
                } else if (holder.recyclerView.getVisibility() == View.GONE) {
                    expand(holder.recyclerView, holder.arrow,true);
                }

                if (category.getChildren().size() <= 0) {
                    Products products = new Products();
                    products.catId = category.getId() + "";
                    products.catName = category.getTitle();
                    Products.where="cats";
                    Utils.productFragment(products, Products.class.getSimpleName(), supportFragmentManager, R.id.content);
                    MainActivity.secondFragment = new Products();
                }
            }
        });

        holder.title.setTypeface(AppFont.getBoldFont(context));

        if(Utils.getLanguage(context).equals("ru")){
            holder.title.setText(category.getTitle_ru());
        }
    }

    private void collapse(RecyclerView recyclerView, ImageView arrow) {
        collapseAnimation(recyclerView);
        arrow.setRotation(0);

//        arrow.setImageResource(R.drawable.more);
    }

    private void expand(RecyclerView recyclerView, ImageView arrow,boolean isAnim) {
        if(isAnim)
            expandAnimation(recyclerView);
        recyclerView.setVisibility(View.VISIBLE);

        if (old_rec != null && old_arrow.getRotation() != 0) {
            if(isAnim)
                collapseAnimation(old_rec);
            old_arrow.setRotation(0);
        }
        arrow.setRotation(90);
        old_arrow = arrow;
        old_rec = recyclerView;
//        arrow.setImageResource(R.drawable.less);
    }


    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView arrow;
        RecyclerView recyclerView;
        LinearLayout con1;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            arrow = itemView.findViewById(R.id.arrow);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            con1 = itemView.findViewById(R.id.con1);

        }
    }

    public static void expandAnimation(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration(250);
        v.startAnimation(a);
    }

    public static void collapseAnimation(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration(250);
        v.startAnimation(a);
    }
}
