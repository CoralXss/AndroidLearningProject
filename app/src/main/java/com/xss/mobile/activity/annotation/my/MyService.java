package com.xss.mobile.activity.annotation.my;

/**
 * Created by xss on 2017/6/13.
 */

public interface MyService {

    @MYGET("image/{id}/{name}")
    String getImageById(@MyQuery("page") int page, @MyPath("id") int id, @MyPath("name") String name);

}
