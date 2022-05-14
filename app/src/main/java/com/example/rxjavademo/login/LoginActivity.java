package com.example.rxjavademo.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.rxjavademo.R;
import com.example.rxjavademo.login.core.CustomObserver;
import com.example.rxjavademo.login.core.LoginEngine;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //执行登陆操作，并使用RxJava根据返回情况输出登录结果，登录成功返回 SuccessBean, 登录失败，返回message.
        LoginEngine.login("Stan_0903","abc12345") //返回Observable 起点
                .subscribe(new CustomObserver() { //订阅起点，并使用自定义的Observer来处理起点流下来的 ResponseBean
                    @Override
                    public void success(SuccessBean successBean) {
                        Log.e(TAG, "登录成功，SuccessBean: "+ successBean.toString() );
                    }

                    @Override
                    public void error(String message) {
                        Log.e(TAG, "登录失败，Message: " +message);
                    }
                });
    }
}