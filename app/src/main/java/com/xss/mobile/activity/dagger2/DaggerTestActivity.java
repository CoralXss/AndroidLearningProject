package com.xss.mobile.activity.dagger2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xss.mobile.MyApplication;
import com.xss.mobile.R;

import javax.inject.Inject;

/**
 * 1. 组合：Activity 依赖 Presenter, 会产生耦合，当构造Presenter 需要参数时，也需要改动Activity的代码;
 *
 * 2. 依赖注入方式：
 */
public class DaggerTestActivity extends AppCompatActivity implements LoadView {
    private String TAG = DaggerTestActivity.class.getSimpleName();

//    private LoadPresenter mLoadPresenter;

    // 此处添加 @Inject 标注，会去查找 LoadPresenter 中同样带有 @Inject 标注的构造方法
//    @Inject
//    LoadPresenter mLoadPresenter;

    @Inject
    LoginManager loginManager;

    @Inject
    LoginManager loginManager1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger_test);

//        mLoadPresenter = new LoadPresenter(this);
//        mLoadPresenter.loadData();

        MyApplication.getInstance().getDaggerComponent().inject(this);

//        DaggerDaggerComponent.builder()
//                .daggerModule(new DaggerModule())
//                .build()
//                .inject(this);

//        loginManager.login(this);
        Log.e(TAG, (loginManager == loginManager1) + "" + loginManager.toString());

    }

    @Override
    public void updateView() {
        Log.e(TAG, "-- view callback success --");
    }


//    @Override
//    public DaggerAdapter constructAdapter() {
//        return new DaggerAdapter(this);
//    }



}
