package com.twinkle.htwinkle.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.loopj.android.image.SmartImageView;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
       ((SmartImageView) imageView).setImageUrl(path.toString());
    }

    @Override
    public ImageView createImageView(Context context) {
        return new SmartImageView(context);
    }
}
