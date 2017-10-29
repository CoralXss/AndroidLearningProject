package com.xss.mobile.activity.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xss on 2017/2/13.
 * desc: 1、注解声明  - 水果颜色注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FruitColor {

    /**
     * 颜色枚举
     */
    public enum Color { BLUE, RED, GREEN };

    /**
     * 颜色属性
     * @return
     */
    Color fruitColor() default Color.BLUE;
}
