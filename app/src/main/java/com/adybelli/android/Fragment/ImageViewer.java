package com.adybelli.android.Fragment;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adybelli.android.R;

import java.util.ArrayList;


public class ImageViewer extends DialogFragment {

    private View view;
    private Context context;
    private ArrayList<Image> images=new ArrayList<>();

    public ImageViewer(ArrayList<Image> images) {
        this.images=images;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_viewer, container, false);
    }
}