package com.example.aggarwal.ridealong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Aggarwal on 30-06-2016.
 */
public class SplashJava extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashlayout);
        int sec=3;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashJava.this,FirstActivity.class));
                finish();
            }
        },sec * 800);
    }
}
