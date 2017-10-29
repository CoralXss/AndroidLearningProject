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
public class DaggerTestScopeActivity extends AppCompatActivity{
    private String TAG = DaggerTestScopeActivity.class.getSimpleName();

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

        // 添加 @Singleton，Component只初始化一次，实现整个应用维持一个实例
        MyApplication.getInstance().getDaggerComponent().inject(this);

//        DaggerDaggerComponent.builder()
//                .daggerModule(new DaggerModule())
//                .build()
//                .inject(this);

        loginManager.login(this);
        Log.e(TAG, loginManager.toString() + ", " + loginManager1.toString() + ", " + LoginManager.getInstance().toString());

    }


//    @Module
//    public class DaggerSingletonModule {
//        @Singleton
//        @Provides
//        LoginManager providerLoginManager() {
//            return new LoginManager();
//        }
//    }
//
//    @Singleton
//    @Component(modules = DaggerModule.class)
//    public interface DaggerSingletonComponent {
//        void inject(DaggerTestScopeActivity activity);
//    }


}
