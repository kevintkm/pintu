package com.imooc.xpuzzle.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;

public class ImageLoaderUtil {

    public static void loadImage(Context context, String url , ImageView imageView){
        Glide.with(context).load(url).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    public static void loadImage(Context context, String url  , SimpleTarget<Bitmap> target){
        Glide.with(context).load(url).asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(target);
    }
}
