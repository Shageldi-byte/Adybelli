package com.adybelli.android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adybelli.android.Object.Category;
import com.adybelli.android.Object.SubCategory;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;

import java.util.ArrayList;
import java.util.List;

public class CategoryBaseAdapter extends BaseAdapter {
    ArrayList<Category> categories=new ArrayList<>();
    Context context;
    Animation rotateLess,rotateMore;
    FragmentManager supportFragmentManager;


    public CategoryBaseAdapter(ArrayList<Category> categories, Context context, FragmentManager supportFragmentManager) {
        this.categories = categories;
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View convertView= LayoutInflater.from(context).inflate(R.layout.accordion,viewGroup,false);
        TextView title;
        ImageView arrow;
        RecyclerView recyclerView;
        LinearLayout con1;
        title=convertView.findViewById(R.id.title);
        arrow=convertView.findViewById(R.id.arrow);
        recyclerView=convertView.findViewById(R.id.recyclerView);
        con1=convertView.findViewById(R.id.con1);


        Category category= categories.get(i);
        rotateLess= AnimationUtils.loadAnimation(context,R.anim.rotate_less);
        rotateMore= AnimationUtils.loadAnimation(context,R.anim.rotate_more);

        title.setText(category.getTitle());

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recyclerView.getVisibility()==View.VISIBLE){
                    collapse(recyclerView,arrow);
                } else if(recyclerView.getVisibility()==View.GONE){
                    expand(recyclerView,arrow);
                }


            }
        });



        con1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recyclerView.getVisibility()==View.VISIBLE){
                    collapse(recyclerView,arrow);
                } else if(recyclerView.getVisibility()==View.GONE){
                    expand(recyclerView,arrow);
                }
            }
        });

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recyclerView.getVisibility()==View.VISIBLE){
                    collapse(recyclerView,arrow);
                } else if(recyclerView.getVisibility()==View.GONE){
                    expand(recyclerView,arrow);
                }
            }
        });

        title.setTypeface(AppFont.getBoldFont(context));




        return convertView;
    }


    private void collapse(RecyclerView recyclerView, ImageView arrow) {
        collapseAnimation(recyclerView);
        arrow.setRotation(0);
//        arrow.setImageResource(R.drawable.more);
    }

    private void expand(RecyclerView recyclerView, ImageView arrow) {
        expandAnimation(recyclerView);
        recyclerView.setVisibility(View.VISIBLE);
        arrow.setRotation(90);
//        arrow.setImageResource(R.drawable.less);
    }




    public static void expandAnimation(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapseAnimation(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
