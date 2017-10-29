package com.xss.mobile.activity.annotation.my;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xss on 2017/6/15.
 */

@Documented
@Target(value = ElementType.PARAMETER)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface MyQuery {

    String value() default "";

    boolean encoded() default false;
}
