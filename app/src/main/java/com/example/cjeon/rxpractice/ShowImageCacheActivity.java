package com.example.cjeon.rxpractice;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class ShowImageCacheActivity extends AppCompatActivity {
    private ImageDownloader imageDownloader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_cache);
        if (imageDownloader == null) {
            imageDownloader = new ImageDownloader();
            imageDownloader.initCache();
        }
        String url = getResources().getString(R.string.url_bg_img) ;
        ImageView imageView = (ImageView) findViewById(R.id.imgView_cache);
        imageDownloader.getImage(url)
                .subscribe(imageView::setImageBitmap);
    }
}
