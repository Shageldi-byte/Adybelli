package com.adybelli.android.TypeFace;

import android.content.Context;
import android.graphics.Typeface;

public class AppFont {

    public static Typeface getRegularFont(Context context){
        return Typeface.createFromAsset(context.getAssets(),"fonts/Proxima Nova Regular.otf");
    }

    public static Typeface getLightFont(Context context){
        return Typeface.createFromAsset(context.getAssets(),"fonts/Proxima Nova Light.otf");
    }

    public static Typeface getBoldFont(Context context){
        return Typeface.createFromAsset(context.getAssets(),"fonts/Proxima Nova Bold.otf");
    }

    public static Typeface getSemiBoldFont(Context context){
        return Typeface.createFromAsset(context.getAssets(),"fonts/Proxima Nova Semibold.otf");
    }

    public static Typeface getExtraBoldFont(Context context){
        return Typeface.createFromAsset(context.getAssets(),"fonts/Proxima Nova Extrabold.otf");
    }

    public static Typeface getThinFont(Context context){
        return Typeface.createFromAsset(context.getAssets(),"fonts/Proxima Nova Thin.otf");
    }

    public static Typeface getBlackFont(Context context){
        return Typeface.createFromAsset(context.getAssets(),"fonts/Proxima Nova Black.otf");
    }
}
