package com.example.rxjavademo.login;

//服务端的响应结果 Bean类，称之为总Bean,
//分别定义三个参数， data, code,和message, 并填充构造函数，get, set 和toString方法
public class ResponseBean {

    private SuccessBean data;
    private int code;
    private String message;

    public ResponseBean() {
    }

    public ResponseBean(SuccessBean data, int code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public SuccessBean getData() {
        return data;
    }

    public void setData(SuccessBean data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "data=" + data +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
