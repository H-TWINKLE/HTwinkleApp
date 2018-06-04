package com.twinkle.htwinkle.Adapter;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoader;

import org.xutils.image.ImageOptions;
import org.xutils.x;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        x.image().bind(imageView,(String)path,new ImageOptions.Builder().setIgnoreGif(false).build());
    }
}
