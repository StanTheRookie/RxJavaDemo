package com.example.rxjavademo.login.core;


import com.example.rxjavademo.login.ResponseBean;
import com.example.rxjavademo.login.SuccessBean;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class CustomObserver implements Observer<ResponseBean> { //自定义Observer传入 ResponseBean， 并将此函数抽象为抽象类

    public abstract void success(SuccessBean successBean); //创建抽象函数success，当登陆成功，提取响应SuccessBean
    public abstract void error(String message); //创建抽象函数error, 等登陆失败，提取响应message

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(ResponseBean responseBean) {
        if(responseBean.getData()==null){
            error(responseBean.getMessage()+"请求失败，请检查日志！");
        }else{
            success(responseBean.getData()); //返回成功 Bean. responseBean.getData()就是返回SuccessBean
        }
    }

    @Override
    public void onError(Throwable e) {
        //如果整个链路出问题的话，打印所有的错误详情。
        error(e.getMessage()+"请检查错误详情日志！");
    }

    @Override
    public void onComplete() {

    }
}
