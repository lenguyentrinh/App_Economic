package com.example.demo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.retrofit.ApiBanHang;
import com.example.demo.retrofit.RetrofitClient;
import com.example.demo.utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangnhapActivity extends AppCompatActivity {
    TextView txtdangki,txtresetpass;
    EditText email, pass;
    AppCompatButton btndangnhap;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);
        initView();
        initControl();
    }

    private void initControl() {
        txtdangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DangKiActivity.class);
                startActivity(intent);
            }
        });
        txtresetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ResetPassActivity.class);
                startActivity(intent);
            }
        });
        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_email = email.getText().toString().trim();
                String str_pass = pass.getText().toString().trim();
                if(TextUtils.isEmpty(str_email)){
                    Toast.makeText(getApplicationContext(),"Please enter your email!",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str_pass)){
                    Toast.makeText(getApplicationContext(),"Please enter your password!",Toast.LENGTH_SHORT).show();
                }else {
                    //save user
                    Paper.book().write("email",str_email);
                    Paper.book().write("pass",str_pass);
                    dangNhap(str_email, str_pass);
                }
            }
        });

    }

    private void initView() {
        Paper.init(this);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        txtdangki = findViewById(R.id.txtdangki);
        txtresetpass = findViewById(R.id.txtresetpass);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        btndangnhap = findViewById(R.id.btndangnhap);

        // Read data
        if (Paper.book().read("email") != null && Paper.book().read("pass") != null) {
            email.setText(Paper.book().read("email"));
            pass.setText(Paper.book().read("pass"));
            Object isLoginObject = Paper.book().read("isLogin");
            if (isLoginObject instanceof Boolean) {
                boolean isLogin = (boolean) isLoginObject;
                if (isLogin) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            dangNhap(Paper.book().read("email"), Paper.book().read("pass"));
                        }
                    }, 1000);
                }
            }
        }
    }

    private void dangNhap(String email, String pass) {
        compositeDisposable.add(apiBanHang.dangnhap(email, pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                boolean isLogin = true;
                                Paper.book().write("isLogin", isLogin);
                                Utils.user_current = userModel.getResult().get(0);
                                //luu thong tin gnuwoi dung
                                Paper.book().write("user",userModel.getResult().get(0));
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                                finish();

                            }else {
                                Toast.makeText(getApplicationContext(),"Please enter your password!",Toast.LENGTH_SHORT).show();
                            }
                        },throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.user_current.getEmail() != null && Utils.user_current.getPass() !=null){
            email.setText(Utils.user_current.getEmail());
            pass.setText(Utils.user_current.getPass());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}








//package com.example.demo.activity;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.AppCompatButton;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.demo.R;
//import com.example.demo.retrofit.ApiBanHang;
//import com.example.demo.retrofit.RetrofitClient;
//import com.example.demo.utils.Utils;
//
//import io.paperdb.Paper;
//import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
//import io.reactivex.rxjava3.disposables.CompositeDisposable;
//import io.reactivex.rxjava3.schedulers.Schedulers;
//
//public class DangnhapActivity extends AppCompatActivity {
//    TextView txtdangki,txtresetpass;
//    EditText email, pass;
//    AppCompatButton btndangnhap;
//    ApiBanHang apiBanHang;
//    CompositeDisposable compositeDisposable = new CompositeDisposable();
//    boolean isLogin = false;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dangnhap);
//        initView();
//        initControl();
//    }
//
//    private void initControl() {
//        txtdangki.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(),DangKiActivity.class);
//                startActivity(intent);
//            }
//        });
//        btndangnhap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String str_email = email.getText().toString().trim();
//                String str_pass = pass.getText().toString().trim();
//                if(TextUtils.isEmpty(str_email)){
//                    Toast.makeText(getApplicationContext(),"Please enter your email!",Toast.LENGTH_SHORT).show();
//                }else if(TextUtils.isEmpty(str_pass)){
//                    Toast.makeText(getApplicationContext(),"Please enter your password!",Toast.LENGTH_SHORT).show();
//                }else {
//                    //save user
//                    Paper.book().write("email",str_email);
//                    Paper.book().write("pass",str_pass);
//                    compositeDisposable.add(apiBanHang.dangnhap(str_email, str_pass)
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(
//                                    userModel -> {
//                                        if(userModel.isSuccess()){
//
//                                            Paper.book().write("isLogin",str_pass);
//                                            Utils.user_current = userModel.getResult().get(0);
//                                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                                            startActivity(intent);
//                                            finish();
//
//                                        }else {
//                                            Toast.makeText(getApplicationContext(),"Please enter your password!",Toast.LENGTH_SHORT).show();
//                                        }
//                                    },throwable -> {
//                                        Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
//                                    }
//                            ));
//                }
//            }
//        });
//        txtresetpass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(),ResetPassActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    private void initView() {
//        Paper.init(this);
//        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
//        txtdangki = findViewById(R.id.txtdangki);
//        txtresetpass = findViewById(R.id.txtresetpass);
//        email = findViewById(R.id.email);
//        pass = findViewById(R.id.pass);
//        btndangnhap = findViewById(R.id.btndangnhap);
//
//        //read data
//        if(Paper.book().read("email")!= null && Paper.book().read("pass") !=null){
//            email.setText(Paper.book().read("email"));
//            pass.setText(Paper.book().read("pass"));
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(Utils.user_current.getEmail() != null && Utils.user_current.getPass() !=null){
//            email.setText(Utils.user_current.getEmail());
//            pass.setText(Utils.user_current.getPass());
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        compositeDisposable.clear();
//        super.onDestroy();
//    }
//}


