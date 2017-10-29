package com.xss.mobile.activity.dagger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xss.mobile.R;

import javax.inject.Inject;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

/**
 * 1. 组合：Activity 依赖 Presenter, 会产生耦合，当构造Presenter 需要参数时，也需要改动Activity的代码;
 *
 * 2. 依赖注入方式：
 */
public class DaggerTestActivity extends AppCompatActivity implements LoadView {
    private String TAG = DaggerTestActivity.class.getSimpleName();

//    private LoadPresenter mLoadPresenter;

    // 1. 此处添加 @Inject 标注，会去查找 LoadPresenter 中同样带有 @Inject 标注的构造方法，自动初始化该类，从而完成依赖注入。
    // @Inject 表示 LoadPresenter 是需要注入到本Activity 中的，即本Activity 是依赖 LoadPresenter 的
    // 注：@Inject 注解的是不能用 private 修饰
    @Inject
    LoadPresenter mLoadPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger_test);

        // rebuild 项目后生成一个以Dagger为前缀的 Component 类
        DaggerDaggerTestActivity_DaggerComponent.builder()
                .daggerModule(new DaggerModule(this))
                .build()
                .inject(this);

        mLoadPresenter.loadData();

    }

    @Override
    public void updateView() {
        Log.e(TAG, "-- view callback success --");
    }

    @Module
    public class DaggerModule {
        private final LoadView mLoadView;

        public DaggerModule(LoadView loadView) {
            this.mLoadView = loadView;
        }

        @Provides
        LoadView providerLoadView() {
            return mLoadView;
        }
    }

    @Component(modules = DaggerModule.class)
    public interface DaggerComponent {
        void inject(DaggerTestActivity activity);
    }

}
