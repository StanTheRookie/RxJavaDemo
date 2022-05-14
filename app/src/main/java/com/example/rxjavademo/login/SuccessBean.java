package com.example.rxjavademo.login;

//登录成功后，服务器响应的总Bean里面嵌套的登录成功Bean
//成功Bean设计两个变量，一个code, 一个name
public class SuccessBean {

    private int id;
    private String name;

    public SuccessBean() {
    }

    public SuccessBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SuccessBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
