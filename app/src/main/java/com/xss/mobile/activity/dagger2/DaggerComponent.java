package com.xss.mobile.activity.dagger2;

import android.app.Application;

import com.xss.mobile.activity.dagger2.scope.ActivityScope;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by xss on 2017/10/11.
 */

@Singleton
//@ActivityScope
@Component(modules = DaggerModule.class)
public interface DaggerComponent {
//    void inject(Application application);

    void inject(DaggerTestActivity activity);

    void inject(DaggerTestScopeActivity activity);

//    void inject(TestDagger testDagger);
}
