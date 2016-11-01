package com.example.cjeon.rxpractice;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import rx.Observable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void RxHelloWorld(View view) {
        Observable.just("Hello, World!").subscribe(this::showToast);
    }

    public void ImageDownloadRx(View view){
        Intent intent = new Intent(this, ShowImageActivity.class);
        startActivity(intent);
    }

    public void showToast(CharSequence message){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    public void ImageDownloadCacheRx(View view) {
        Intent intent = new Intent(this, ShowImageCacheActivity.class);
        startActivity(intent);
    }
}
