package com.example.demo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.retrofit.ApiBanHang;
import com.example.demo.retrofit.RetrofitClient;
import com.example.demo.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txttongtien, txtsodt, txtemail;
    EditText edtdiachi;
    AppCompatButton btndathang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    long tongtien;
    int totalItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        initView();
        countItem();
        initControl();
    }

    private void countItem() {
         totalItem = 0;
        for(int i = 0 ; i< Utils.mangmuahang.size();i++){
            totalItem = totalItem+ Utils.mangmuahang.get(i).getSoluong();
        }
    }

    private void initControl() {
        setSupportActionBar(toolbar);;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien =getIntent().getLongExtra("tongtien",0);
        txttongtien.setText(decimalFormat.format(tongtien));
        txtemail.setText(Utils.user_current.getEmail());
        txtsodt.setText(Utils.user_current.getMobile());
        btndathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_diachi = edtdiachi.getText().toString().trim();
                if(TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(getApplicationContext(),"Please enter your address!",Toast.LENGTH_SHORT).show();
                }else {
                    //post data
                    String str_email = Utils.user_current.getEmail();
                    String str_sdt = Utils.user_current.getMobile();
                    int id = Utils.user_current.getId();
                    Log.d("str_email",str_email);
                    Log.d("str_sdt",str_sdt);
                    Log.d("tongtien",String.valueOf(tongtien));
                    Log.d("str_diachi",str_diachi);
                    Log.d("chitiet",new Gson().toJson(Utils.mangmuahang));
                    compositeDisposable.add(apiBanHang.createOrder(str_email, str_sdt, String.valueOf(tongtien), id, str_diachi, totalItem, new Gson().toJson(Utils.mangmuahang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    orderResponse -> {
                                        if (orderResponse.isSuccess()) {
                                            Toast.makeText(getApplicationContext(), "Order successful", Toast.LENGTH_SHORT).show();
                                            Utils.mangmuahang.clear();
                                            Utils.manggiohang.clear();
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), orderResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.d("ex", "Error: " + throwable.getMessage());
                                        // Log the raw response
                                        Log.d("ex", "Raw response: " + throwable.toString());
                                    }
                            )
                    );

                }
            }
        });
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toobar);
        txttongtien = findViewById(R.id.txttongtien);
        txtsodt = findViewById(R.id.txtsodienthoai);
        txtemail = findViewById(R.id.txtemail);
        edtdiachi = findViewById(R.id.edtdiachi);
        btndathang = findViewById(R.id.btbdathang);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}