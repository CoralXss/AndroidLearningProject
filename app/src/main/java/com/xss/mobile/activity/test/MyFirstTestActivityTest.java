package com.xss.mobile.activity.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.xss.mobile.R;
import com.xss.mobile.activity.IntentServiceTestActivity;

/**
 * Created by xss on 2016/11/16.
 */
public class MyFirstTestActivityTest extends ActivityInstrumentationTestCase2<IntentServiceTestActivity>{
    private IntentServiceTestActivity mIntentServiceTestActivity;

    private Button mBtn;

    public MyFirstTestActivityTest(Class<IntentServiceTestActivity> activityClass) {
        super(activityClass);
    }

    /**
     * 在setUp中，初始化测试数据集的状态：
     * 1、定义保存测试数据及状态的实例变量；
     * 2、创建并保存正在测试的Activity的引用实例；
     * 3、获得想要测试的Activity中任何UI组件的引用。
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mIntentServiceTestActivity = getActivity();
        mBtn = (Button) mIntentServiceTestActivity.findViewById(R.id.btn_click);

    }

    public void testPreconditions() {
        assertNotNull("mIntentServiceTestActivity is not null", mIntentServiceTestActivity);
        assertNotNull("mBtn is not null", mBtn);
    }


}
