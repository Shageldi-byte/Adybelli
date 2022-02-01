package com.adybelli.android.CategoryAdapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.adybelli.android.CategoryModel.Color;
import com.adybelli.android.Common.Utils;
import com.adybelli.android.Fragment.Products;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {
    private ArrayList<Color> colors = new ArrayList<>();
    private Context context;

    public ColorAdapter(ArrayList<Color> colors, Context context) {
        this.colors = colors;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.color_item, parent, false);
        return new ColorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ColorAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Color color = colors.get(position);
        if (Products.colorsStr.contains(color.getId() + "")) {
            try {
                if (color.getDexColor() == null) {
                    holder.check.setColorFilter(android.graphics.Color.parseColor("#161616"), android.graphics.PorterDuff.Mode.MULTIPLY);
                } else {
                    if (color.getDexColor().equals("#multicolor")) {
                        holder.check.setColorFilter(android.graphics.Color.parseColor("#FFFFFF"), android.graphics.PorterDuff.Mode.MULTIPLY);
                    } else {
                        if (isBrightColor(android.graphics.Color.parseColor(color.getDexColor()))) {
                            holder.check.setColorFilter(android.graphics.Color.parseColor("#161616"), android.graphics.PorterDuff.Mode.MULTIPLY);
                        } else {
                            holder.check.setColorFilter(android.graphics.Color.parseColor("#FFFFFF"), android.graphics.PorterDuff.Mode.MULTIPLY);
                        }
                    }
                }
            } catch (Exception ex){
                ex.printStackTrace();
            }
            holder.check.setVisibility(View.VISIBLE);
        }

        holder.text.setTypeface(AppFont.getRegularFont(context));
        holder.text.setText(color.getName());
        try {
            if (color.getDexColor().equals("#multicolor")) {
                holder.bg.setBackgroundResource(R.drawable.gradient_cyrcle);
            } else {
                setBgColor(holder.bg, color.getDexColor());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            setBgColor(holder.bg, "#FFFFFF");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (color.getDexColor() == null) {
                        holder.check.setColorFilter(android.graphics.Color.parseColor("#161616"), android.graphics.PorterDuff.Mode.MULTIPLY);
                    } else {
                        if (color.getDexColor().equals("#multicolor")) {
                            holder.check.setColorFilter(android.graphics.Color.parseColor("#FFFFFF"), android.graphics.PorterDuff.Mode.MULTIPLY);
                        } else {
                            if (isBrightColor(android.graphics.Color.parseColor(color.getDexColor()))) {
                                holder.check.setColorFilter(android.graphics.Color.parseColor("#161616"), android.graphics.PorterDuff.Mode.MULTIPLY);
                            } else {
                                holder.check.setColorFilter(android.graphics.Color.parseColor("#FFFFFF"), android.graphics.PorterDuff.Mode.MULTIPLY);
                            }
                        }
                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                }
                if (holder.check.getVisibility() == View.GONE) {
                    holder.check.setVisibility(View.VISIBLE);
                    Products.colorsStr.add(color.getId() + "");
                } else if (holder.check.getVisibility() == View.VISIBLE) {
                    holder.check.setVisibility(View.GONE);
                    Products.colorsStr.remove(color.getId() + "");
                }
            }
        });

        if (Utils.getLanguage(context).equals("ru")) {
            holder.text.setText(color.getColor_ru());
        }

    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout bg;
        ImageView check;
        TextView text;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            bg = itemView.findViewById(R.id.bg);
            check = itemView.findViewById(R.id.check);
            text = itemView.findViewById(R.id.text);
        }
    }

    public static void setBgColor(RelativeLayout relativeLayout, String color) {
        if (color == null) {
            return;
        }
        try {
            relativeLayout.getBackground().setColorFilter(android.graphics.Color.parseColor(color), PorterDuff.Mode.SRC_ATOP);
            GradientDrawable drawable = (GradientDrawable) relativeLayout.getBackground();
            drawable.setColor(android.graphics.Color.parseColor(color));
            drawable.setStroke(1, android.graphics.Color.parseColor("#C0C0C0"));
        } catch (Exception ex) {
            ex.printStackTrace();
            setBgColor(relativeLayout, "#FFFFFF");
        }
    }

    public static boolean isBrightColor(int color) {
        if (android.R.color.transparent == color)
            return true;

        boolean rtnValue = false;

        int[] rgb = {android.graphics.Color.red(color), android.graphics.Color.green(color), android.graphics.Color.blue(color)};

        int brightness = (int) Math.sqrt(rgb[0] * rgb[0] * .241 + rgb[1]
                * rgb[1] * .691 + rgb[2] * rgb[2] * .068);

        // color is light
        if (brightness >= 200) {
            rtnValue = true;
        }

        return rtnValue;
    }

}
