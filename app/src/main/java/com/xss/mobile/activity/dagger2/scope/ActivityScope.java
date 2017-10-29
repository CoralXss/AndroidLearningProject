package com.xss.mobile.activity.dagger2.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by xss on 2017/10/11.
 */
@Scope
@Documented
@Retention(RUNTIME)
public @interface ActivityScope {
}
