package com.adybelli.android.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adybelli.android.Common.Constant;
import com.adybelli.android.R;
import com.adybelli.android.TypeFace.AppFont;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zhy.base.fileprovider.FileProvider7;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Random;

public class ShareProductImage extends AppCompatActivity {
    private String imageUrl,name,trademark,cost,id,old_cost;
    private ImageView imageView;
    private ImageView share,close;
    private Context context=this;
    private RelativeLayout imageContainer;
    private TextView trademarkTV,nameTV,idTV,costTV,title,oldCost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_product_image);
        Intent intent=getIntent();
        if(intent==null){
            finish();
            return;
        }

        imageUrl=intent.getStringExtra("url");
        name=intent.getStringExtra("name");
        trademark=intent.getStringExtra("trademark");
        cost=intent.getStringExtra("cost");
        id=intent.getStringExtra("id");
        old_cost=intent.getStringExtra("old_cost");



        initComponents();
        setFonts();
        imageGenerator();
        setListener();

    }

    private void setFonts() {
        title.setTypeface(AppFont.getRegularFont(context));
    }

    private void imageGenerator() {
        Glide.with(context)
                .load(Constant.SERVER_URL+imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(imageView);

        trademarkTV.setText(trademark);
        nameTV.setText(name);
        idTV.setText("Product code: "+id);
        costTV.setText(cost+" TMT");
        if(old_cost.isEmpty()){
            oldCost.setVisibility(View.GONE);
        } else{
            oldCost.setVisibility(View.VISIBLE);
            oldCost.setText(old_cost+" TMT");
        }
        oldCost.setPaintFlags(oldCost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        costTV.setText(cost+" TMT");
    }

    private void setListener() {
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(ShareProductImage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ShareProductImage.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            11);

                } else {
                    save_toMediaStore();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
        Bitmap bitmap = loadBitmapFromView(imageContainer);
        Random generator = new Random();
        int n = 100000;
        n = generator.nextInt(n);

        try {
            saveImage(bitmap, n + "");
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String type = mime.getMimeTypeFromExtension("image/*");

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), n + ".jpg");
            Uri uri=FileProvider7.getUriForFile(context, file);
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("image/jpg");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            context.startActivity(Intent.createChooser(shareIntent, "Select"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveImage(Bitmap bitmap, @NonNull String name) throws IOException {
        OutputStream fos;
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name + ".jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(Objects.requireNonNull(uri));
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            File image = new File(imagesDir, name + ".jpg");
            fos = new FileOutputStream(image);
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        Objects.requireNonNull(fos).close();

    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    private void initComponents() {
        title=findViewById(R.id.title);
        close=findViewById(R.id.close);
        imageView=findViewById(R.id.image);
        share=findViewById(R.id.share);
        imageContainer=findViewById(R.id.imageContainer);
        trademarkTV=findViewById(R.id.trademark);
        nameTV=findViewById(R.id.name);
        idTV=findViewById(R.id.idTV);
        costTV=findViewById(R.id.costTV);
        oldCost=findViewById(R.id.oldCost);
    }
}