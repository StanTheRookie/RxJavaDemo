package com.example.rxjavademo.login.core;

import com.example.rxjavademo.login.ResponseBean;
import com.example.rxjavademo.login.SuccessBean;

import io.reactivex.Observable;

public class LoginEngine {
    //登录引擎中创建一个函数，返回值为RxJava的起点 Observable,参数为用户名和密码
    public static Observable<ResponseBean> login (String name, String pwd){

        ResponseBean responseBean = new ResponseBean(); //需要在ResponseBean class里面定义一个不带参数的构造函数

        if ("Stan_0903".equals(name) && "abc12345".equals(pwd)){ //构造一个登录成功，因为只是演示没有实际从服务器获取
            /**
             * 返回如下类型的数据
             * 	data: {XXXX,登录成功的Bean, xxxxx, successBean}
             * 	code:200
             * message: "登录成功"
             */

            //创建一个成功Bean, 完成后用于构造服务器响应 ResponseBean
            SuccessBean successBean = new SuccessBean();
            successBean.setId(1234567);
            successBean.setName("Stan_0903 登录成功");

            //以下构造一个将服务器响应返回成一个ResponseBean
            responseBean.setData(successBean);
            responseBean.setCode(200);
            responseBean.setMessage("登录成功");
        }else { //登录失败
            /**
             * 登录失败返回如下JSON数据
             * 	data：null 或者 0
             * 	code:404
             * message: "登录失败"
             */

            //创建登录失败的 ResponseBean
            responseBean.setData(null);
            responseBean.setCode(404);
            responseBean.setMessage("登录失败");
        }

        return Observable.just(responseBean); //需要返回Observable的起点。
        //这样在调用了 LoginEngine的Login方法后，程序会拿到RxJava的起点，并且起点往下一层传递的数据是 ResponseBean类型的总bean.

    }
}
