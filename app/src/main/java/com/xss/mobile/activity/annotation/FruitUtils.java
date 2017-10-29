package com.xss.mobile.activity.annotation;

import android.nfc.Tag;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Created by xss on 2017/2/13.
 * desc：3、注解处理器
 */

public class FruitUtils {
    static String TAG = FruitUtils.class.getSimpleName();

    public static String getFruitInfo(Class<?> clazz) {
        String fruitName = "水果名称：";
        String fruitColor = "水果颜色：";

        Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields) {
            // 判断该 程序元素上是否包含 指定类型的注解
            if (field.isAnnotationPresent(FruitName.class)) {
                // 返回该 程序元素上 指定类型的注解，
                FruitName fruitNameObj = field.getAnnotation(FruitName.class);
                fruitName = fruitNameObj.value();
                Log.e(TAG, "fruitName = " + fruitName);
            } else if (field.isAnnotationPresent(FruitColor.class)) {
                FruitColor fruitColorObj = field.getAnnotation(FruitColor.class);
                fruitColor = fruitColorObj.fruitColor().toString();
                Log.e(TAG, "fruitColor = " + fruitColor);
            }
        }

        return "This is a/an " + fruitColor + "的" + fruitName;
    }
}
