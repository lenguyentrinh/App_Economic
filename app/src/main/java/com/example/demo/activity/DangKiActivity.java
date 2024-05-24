package com.example.demo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.retrofit.ApiBanHang;
import com.example.demo.retrofit.RetrofitClient;
import com.example.demo.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKiActivity extends AppCompatActivity {
    EditText email, pass, repass ,mobile, username;
    AppCompatButton button;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);
        initView();
        initControl();
    }

    private void initControl() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangki();
            }
        });
    }

    private void dangki() {
        String str_email = email.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();
        String str_repass = repass.getText().toString().trim();
        String str_mobile = mobile.getText().toString().trim();
        String str_username = username.getText().toString().trim();

        if(TextUtils.isEmpty(str_email)){
            Toast.makeText(getApplicationContext(),"Please enter your email!",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(str_pass)){
            Toast.makeText(getApplicationContext(),"Please enter your password!",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(str_repass)){
            Toast.makeText(getApplicationContext(),"Please enter confirm password!",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(str_mobile)){
            Toast.makeText(getApplicationContext(),"Please enter your mobile!",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(str_username)){
            Toast.makeText(getApplicationContext(),"Please enter your username!",Toast.LENGTH_SHORT).show();
        }else {
            if(str_pass.equals(str_repass)){
                //post data
                compositeDisposable.add(apiBanHang.dangki(str_email,str_pass,str_username,str_mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                     userModel -> {
                         if(userModel.isSuccess()){
                             Utils.user_current.setEmail(str_email);
                             Utils.user_current.setPass(str_pass);
                             Intent intent = new Intent(getApplicationContext(),DangnhapActivity.class);
                             startActivity(intent);
                             Toast.makeText(getApplicationContext(), "Signup is successfully!",Toast.LENGTH_SHORT).show();
                             finish();
                         }else {
                             Toast.makeText(getApplicationContext(), userModel.getMessage(),Toast.LENGTH_SHORT).show();
                         }

                     }   ,throwable -> {
                         Toast.makeText(getApplicationContext(), throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                ));
            }else {
                Toast.makeText(getApplicationContext(),"Password and confirm password not equal!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        repass = findViewById(R.id.repass);
        button = findViewById(R.id.btndangki);
        mobile = findViewById(R.id.mobile);
        username = findViewById(R.id.username);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}