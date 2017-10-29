package com.xss.mobile.activity.annotation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.xss.mobile.R;

/**
 * 使用 @IntDef 代替枚举
 */
public class IntDefTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_int_def_test);

        WeekDayMode weekDayMode = new WeekDayMode();
        weekDayMode.setCurrentDay(WeekDayMode.THURSDAY);

//        weekDayMode.setCurrentDay(11);  // 编译期直接提示错误

        String today = weekDayMode.getWeekDayString();
        TextView tv_content = (TextView) findViewById(R.id.tv_content);
        tv_content.setText("Today is " + today);
    }
}
