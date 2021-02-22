package com.sunfb.loadingview58;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoadingView loadingView =findViewById(R.id.view_loading_view);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingView.setVisibility(View.GONE);
            }
        },30000);
    }
}