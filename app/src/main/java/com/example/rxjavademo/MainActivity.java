package com.example.rxjavademo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    //定义一个网络图片的下载地址
    private final static String PATH ="https://t7.baidu.com/it/u=1244220659,2745338456&fm=193&f=GIF";
    //弹出加载框（正在加载中...）
    private ProgressDialog progressDialog;

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.image);
    }

    /**
     * LoadImage
     * @param view
     */
    public void loadImageAction(View view) {
//        传统方式加载图片，那么每位开发者的思想可能都是不一样的有如下思想，不论哪种思想，对于后续的开发者维护来说很痛苦，因为各自的思维可能不同。
//        线程池
//        new Thread + Handler
//        A singel touch
//        使用古老的方式

        /**
         * 使用RX思维编程
         * 第一步，定义好起点和终点
         * 起点就是 被观察者， Observable    --------------------------> 终点就是观察者 Observer
         * 在RxJava中所有的函数都被称作操作符, 是因为RxJava的线程是通过操作符来操作流向的。因为我们的函数是要去操作从起点流向终点
         */
        
        //编写起点：
        //just操作符的主要作用是将数据从一个节点发送出去，使之流向终点
        //整个流程采用观察者设计模式
        //TODO RxJava 程序流程第二步，进入起点
        Observable.just(PATH)
                //在从起点拿到PATH String类型的数据之后，这一层要做的工作是根据PATH String 请求服务器，得到服务器对PATH的响应并对应生成图像的 Bitmap 传到下一层，给到终点进行图像展示

                //TODO 流程第三步，进行图片下载
                .map(new Function<String, Bitmap>() { //在起点和终点之间新增一层，用于做 String PATH 到图片 Bitmap的转换， 所以这个new Function的两个参数，系统知道我们的上层是 String,不知道我们的下层是什么，需要手动改成 Bitmap
                    @Override
                    public Bitmap apply(String s) throws Exception {  //在指定好下层数据类型的之后，重写 apply 构造函数。
                        try{  //try 向服务器进行异步操作，请求图片并保存为 Bitmap数据。所以需要在第二层分配异步线程，然后在终点 Observer那边再分配Android UI主线程让其显示图片。
                            //涉及到网络请求，需要开启网络权限，去AndroidManifest.xml 文件中开启

                            Thread.sleep(2000); //设置程序运行停止两秒钟，为了让加载框显示更久，纯粹为了演示RxJava的执行流程。
                            URL url = new URL(s); //根据从起点传入的PATH参数创建一个url
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(); //使用url打开和服务器的链接，并获得一个HttpURL连接
                            httpURLConnection.setConnectTimeout(5000); //设置请求超时时常，如果超过5秒即提示下载失败
                            int responseCode = httpURLConnection.getResponseCode();//获取请求的响应状态码， 从这一步才开始发送给服务器request
                            if(responseCode==HttpURLConnection.HTTP_OK){
                                //此时服务器会给回响应，我们用数据流来接收响应
                                InputStream inputStream = httpURLConnection.getInputStream(); //从服务器获取输入流，临时保存在 inputStream中
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);//将输入流传入Bitmap 解码器进行解码并获得 bitmap
                                return bitmap;   //这句话的深层含义是将 获取到的Bitmap对象传入下一层
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        return null;
                    }
                })

                //给上面的 map 第二层分配异步线程，让其进行图片下载操作。
                .subscribeOn(Schedulers.io())//分配异步线程

                //在子线程下载图片之后，还得给终点 Observer分配为Android主线程。
                .observeOn(AndroidSchedulers.mainThread())

        //TODO 点击按钮后，找到Subscribe 的终点订阅者，才是真正程序开始执行的入口
        //设置好起点之后，接下来需要关联终点观察者
        .subscribe( //关联到起点的 Observable, Subscribe　这边就理解成关联的意思


                new Observer<Bitmap>() {  //新建一个观察者，并关联到起点的 Observable
            //为什么一开始Observer的泛型类型是 String, 那是因为Observer是从上一个节点获取入参，而上一层（起点 Observable）传下来的数据PATH的数据类型就是String.
            //但是因为加了中间层map之后，我们指定到终点 Observer的数据类型已经改变成为 Bitmap, 所以也需要手动将Observer 和里面的函数参数类型都改成Bitmap


            //onSubscribe 函数代表起点和终点关联成功，即订阅成功
            //TODO 程序执行第一步
            @Override
            public void onSubscribe(Disposable d) {
                //程序刚开始执行的时候，显示加载框
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("RxJava Image Downloading");
                progressDialog.show();
            }

            //onNext 函数是拿到上层（流向中上一个节点）的响应
            //TODO 第四步，进行图片显示
            @Override
            public void onNext(Bitmap s) { //这边的参数类型也把String改成Bitmap, 因为onNext 是拿到上层传下来的数据 Bitmap
                //从上层拿到的Bitmap 会在这边出发 onNext 函数启动，这边根据上层传入的Bitmap 做出响应即可。即将图片设为ImageLayout的显示图片
                image.setImageBitmap(s); //将图片显示到控件上
                //在UI显示图片的时候必须在主线程，如果在异步线程尝试显示图片将会报错，这是Android开发的规范
            }

            //onError 流程链条发生异常
            @Override
            public void onError(Throwable e) {

            }

            //代表整个链条全部结束
            //TODO 第五步，整个RxJava流程走完，取消加载框显示
            @Override
            public void onComplete() {
                //取消显示加载框
                if(progressDialog !=null)
                    progressDialog.dismiss();
            }
        });

        
    }
}