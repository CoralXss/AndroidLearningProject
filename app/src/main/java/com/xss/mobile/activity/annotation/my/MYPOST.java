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
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface MYPOST {

    /**
     * A relative or absolute path, or full Url od the endpoint
     * @return
     */
    String value() default "";
}
