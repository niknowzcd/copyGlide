package com.architect.copy_glide;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.architect.library.Glide;
import com.architect.library.request.target.DrawableImageViewTarget;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image);

        Glide.with(MainActivity.this).load("https://cn.bing.com/sa/simg/hpb/LaDigue_EN-CA1115245085_1920x1080.jpg")
                .into(imageView);

        Button btn=findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                imageView.setImageDrawable(DrawableImageViewTarget.bitmapDrawable);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

//        Glide.with(this).load("https://cn.bing.com/sa/simg/hpb/LaDigue_EN-CA1115245085_1920x1080.jpg")
//                .into(imageView);
    }
}