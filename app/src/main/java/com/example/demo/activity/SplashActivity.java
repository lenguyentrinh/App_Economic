package com.example.demo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.demo.R;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
        Paper.init(this);
        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(5000);
                }catch (Exception ex){

                }finally {
                    if(Paper.book().read("user") == null){
                        Intent intent = new Intent(getApplicationContext(),DangnhapActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        };
        thread.start();
    }
}