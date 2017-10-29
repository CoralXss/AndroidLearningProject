package com.xss.mobile.activity.annotation.my;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by xss on 2017/6/13.
 */

public class MyRetrofit {

    /**
     * 创建 MyService 的代理
     * @param service
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {

        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        System.out.println(method.toGenericString() + ", " + args.toString());

                        MyServiceMethod serviceMethod = new MyServiceMethod(method, args);
                        serviceMethod.parseAnnotations();

                        String relativeUrl = serviceMethod.getRelativeUrl();

                        // image/101/xss?page=1
                        System.out.println("relativeUrl = " + relativeUrl);

                        return null;
                    }
                });
    }

    private void parseMethodAnnotation(Method method, Object[] args) {

    }

    public static void main(String[] args) {
        MyService service = new MyRetrofit().create(MyService.class);
        service.getImageById(1, 101, "xss");
    }

}
