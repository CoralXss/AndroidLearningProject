package com.xss.mobile.activity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by xss on 2017/2/13.
 */

@Target(ElementType.TYPE)
public @interface Table {

    /**
     * 数据表名称 注解，默认值为 类名称
     * @return
     */
    public String tableName() default "className";

}
