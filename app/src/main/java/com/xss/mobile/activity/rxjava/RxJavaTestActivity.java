package com.xss.mobile.activity.rxjava;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xss.mobile.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

public class RxJavaTestActivity extends AppCompatActivity {

    Button btn_click, btn_click2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_test);

        btn_click = (Button) findViewById(R.id.btn_click);

        btn_click2 = (Button) findViewById(R.id.btn_click2);

//        btn_click.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                normalCall();
//                justCall();
//                deferCall();
//                intervalCall();
//
//                mapOperator();
//                flatMapOperator();
//            }
//        });

        test();
    }

    private View.OnClickListener listener1;
    private void test() {

        listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("RxJava", "listener thread = " + Thread.currentThread().getName());

                // 生成一个新的 Observable，进行线程切换
                btn_click2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("RxJava", "listener1-1 thread = " + Thread.currentThread().getName());

                        startThread(btn_click, listener1);
                    }
                });
            }
        };

        btn_click.setOnClickListener(listener1);
    }

    /**
     * 类比 OperatorSubscriberOn
     * @param view
     * @param listener
     */
    private void startThread(final View view, final View.OnClickListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                // 创建一个新的 Subscriber
                final View.OnClickListener listener2 = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 2. 然后在 新建的 Subscriber2 中，使原始的 Subscriber 处理该事件，问：此时该事件是处于哪种线程？

                        Log.e("RxJava", "listener2 thread = " + Thread.currentThread().getName());

                        listener.onClick(v);
                    }
                };

                // 1. 在子线程中使用 原始的 Observable 关联 新建的 Subscriber2
                view.setOnClickListener(listener2);

            }
        }).start();


    }

    /**
     * 标准写法
     */
    private void normalCall() {
        // 发送者
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.e("RxJava", "Observable: begin sending...");
                // 发送者每次发送一条消息，都会调用 观察者 Subscriber 的 onNext() 方法
                subscriber.onNext("Hi, I'm Lily - normal");        // 发送数据
            }
        });

        // 接收者-观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e("RxJava", "Observer: onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("RxJava", "Observer: onError");
            }

            @Override
            public void onNext(String s) {
                Log.e("RxJava", "Observer: onReceived " + s); // 接收数据
            }
        };

        // 关联发送者和接受者
        Subscription subscription = observable.subscribe(observer);

        // 取消订阅，之后就不会收到 Observable 发送的消息
        if (subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }


        /**
         * 点击按钮，结果：
         *  RxJava: Observable: begin sending...
         *  RxJava: Observer: onReceived Hi, I'm Lily
         *
         *  异步请求：一个发送源，一个接收源
         */
    }

    /**
     * 1. Action0   Action1  使用最为广泛;
     * 2. Observer 在 subscribe() 时会被转化为 Subscriber;
     * 3. 操作符：
     */

    /**
     * 1. 使用 just() 创建 Observable, 然后自动调用 onNext() 发射数据
     */
    private void justCall() {
        Observable<String> justObservable = Observable.just("Hi, I'm Lily -just !");

        // 简化的 Subscriber 写法
        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e("RxJava", "Observer: onReceived " + s); // 接收数据
            }
        };

        Action1<String> onNextAction1 = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e("RxJava", "Observer1: onReceived " + s); // 接收数据
            }
        };

        justObservable.subscribe(onNextAction);
        justObservable.subscribe(onNextAction1);

    }

    /**
     * 2. 使用 from() 创建 Observable, 遍历 list，每次发送一个item，按顺序接收
     */
    private void fromCall() {
        List<String> list = new ArrayList<>();
        list.add("Hi, I'm Lily -from !");
        list.add("Hi, I'm Lucy !");
        list.add("Hi, I'm Nancy !");

        Observable<String> fromObservable = Observable.from(list);

        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e("RxJava", "Observer: onReceived " + s); // 接收数据
            }
        };

        fromObservable.subscribe(onNextAction);
    }

    /**
     * 3. 使用 defer() 有观察者订阅时才创建 Observable，并且为每一个观察者创建一个新的 Observable
     */
    private void deferCall() {
        Observable<String> deferObservable = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return Observable.just("deferObservable - defer");
            }
        });

        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e("RxJava", "Observer: onReceived " + s); // 接收数据
            }
        };

        deferObservable.subscribe(onNextAction);
    }

    /**
     * 4. 使用 interval() 按固定时间发送一个整数序列，可用作定时器
     */
    private Subscription subscription;
    private void intervalCall() {
        // 每个1s 发送一次
        final Observable<Long> intervalObservable = Observable.interval(1, TimeUnit.SECONDS);

        Observer<Long> observer = new Observer<Long>() {

            @Override
            public void onCompleted() {
                Log.e("RxJava", "Completed");

                // todo 此种方式，取消订阅 定时器并没有结束，为什么？如何停止定时？
                if (subscription != null && subscription.isUnsubscribed()) {
                    subscription.unsubscribe();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                Log.e("RxJava", "Observer: onReceived " + aLong); // 接收数据

                if (aLong == 10) {
                    Log.e("RxJava", "Observer: onReceived Stopped"); // 接收数据
                    onCompleted();
                }
            }
        };

        subscription = intervalObservable.subscribe(observer);
    }


    ///////////////--操作符--/////////////////////
    /**
     * 操作符：解决 Observable 对象变换的问题
     */

    /**
     * map 操作符：将一个事件转换为另一个事件（以下经过 map 操作后，将 String 转换为 Bitmap）
     */
    private void mapOperator() {
        Observable.just("images/xss.jpg")
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String filePath) {    // 参数 String
                        // 将 filePath 转成 Bitmap
                        return BitmapFactory.decodeFile(filePath);
                    }
                })
                .subscribe(new Action1<Bitmap>() {           // 参数 Bitmap
                    @Override
                    public void call(Bitmap bitmap) {
                        // showBitmap(bitmap);
                    }
                });

        // 更具说服力的例子
        Observable.just("Hello world")
                .map(new Func1<String, Integer>() {  // String -> Integer
                    @Override
                    public Integer call(String s) {
                        return s.hashCode();
                    }
                })
                .map(new Func1<Integer, String>() {  // Integer -> String
                    @Override
                    public String call(Integer integer) {
                        return integer.toString();
                    }
                })
                .subscribe(new Action1<String>() {   // 操作 String
                    @Override
                    public void call(String s) {
                       Log.e("RxJAva - map", "hashCode string = " + s);
                    }
                });
    }

    /**
     * flatMap 操作符
     */
    private void flatMapOperator() {
        Observable<List<String>> observable = query("/query/urls");
        observable.flatMap(new Func1<List<String>, Observable<String>>() {
            @Override
            public Observable<String> call(List<String> strings) {
                return Observable.from(strings);
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

                // Subscriber 接收数据
                Log.e("RxJava", "url = " + s);
            }
        });
    }

    Observable<List<String>> query(String url) {
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                List<String> list = new ArrayList<>();
                list.add("https://www.baidu.com");
                list.add("https://www.google.com");

                // 查询到数据后，发送给 Subscriber
                subscriber.onNext(list);
            }
        });
    }
}
