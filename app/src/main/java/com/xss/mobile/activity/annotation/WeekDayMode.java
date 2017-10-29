package com.xss.mobile.activity.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by xss on 2017/10/26.
 *
 * 枚举：相对于int静态常量来说，枚举的最大作用是 在编译时进行检查，避免传入不合法参数，提供了类型安全；
 * 场景：如使用int表示性别时，设置性别可以输入任意 int 整形数据，会导致出现非男非女情况；这种情况使用枚举会将值限制在枚举范围内。
 *
 * 枚举缺点：在Android中枚举会增大 dex包的大小，并会增大运行时内存分配大小。
 *  使用 Integer 和 String 会好些，但是无法保证类型安全。
 *
 * Android 提供注解库，@IntDef 确保传入一个特定的值，若传入的值不是 特定组中的，则编译时会报错。
 *
 */

public class WeekDayMode {

    // 1. 定义常量
    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;

    @WeekDays
    private int currentDay = WEDNESDAY;

    private static WeekDayMode sInstance;

    public static WeekDayMode getInstance() {
        if (sInstance == null) {
            sInstance = new WeekDayMode();
        }
        return sInstance;
    }

    public void setCurrentDay(@WeekDays int currentDay) {
        this.currentDay = currentDay;
    }

    @WeekDays
    public int getWeekDay() {
        return currentDay;
    }

    public String getWeekDayString() {
        switch (currentDay) {
            case SUNDAY:
                return "Sunday";
            case MONDAY:
                return "Monday";
            case TUESDAY:
                return "Tuesday";
            case WEDNESDAY:
                return "Wednesday";
            case THURSDAY:
                return "Thursday";
            case FRIDAY:
                return "Friday";
            case SATURDAY:
                return "Saturday";
            default:
                return "Error Day";
        }
    }

    // 2. 用 @IntDef 包裹常量
    @IntDef({SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY}) // 声明构造器
    @Retention(RetentionPolicy.SOURCE)
    public @interface WeekDays {}

}

