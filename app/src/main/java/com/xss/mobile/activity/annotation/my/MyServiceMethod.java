package com.xss.mobile.activity.annotation.my;

import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xss on 2017/6/15.
 */

public class MyServiceMethod<T> {
    private static final String TAG = MyServiceMethod.class.getSimpleName();

    /** 服务方法 */
    private Method method;
    /** 方法注解 */
    private Annotation[] methodAnnotations;
    /** 方法参数注解 */
    private Annotation[][] parameterAnnotationArrays;
    /** 方法参数类型 */
    private Type[] parameterTypes;

    /** 请求相对路径 */
    private String relativeUrl;
    /** images/{id}/{name} 则为 {id, name}  */
    private Set<String> relativeUrlParamsName;

    private Object[] args;

    public MyServiceMethod(Method method, Object[] args) {
        this.method = method;
        this.args = args;

        methodAnnotations = method.getAnnotations();
        parameterAnnotationArrays = method.getParameterAnnotations();
        parameterTypes = method.getGenericParameterTypes();
    }

    public void parseAnnotations() {
        parseMethodAnnotation();
        parseMethodParametersAnnotation();
    }

    /**
     * 解析方法注解  MYGET(value=image/{id}/{name})
     */
    public void parseMethodAnnotation() {
        for (Annotation annotation: methodAnnotations) {
            if (annotation instanceof MYGET) {
                parseMethodAndPath("MYGET", ((MYGET) annotation).value(), false);
            } else if (annotation instanceof MYPOST) {
                parseMethodAndPath("MYPOST", ((MYPOST) annotation).value(), true);
            }
        }
    }

    static final String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";
    static final Pattern PARAM_URL_REGEX = Pattern.compile("\\{(" + PARAM + ")\\}");
    private void parseMethodAndPath(String httpMethod, String value, boolean hasBody) {

        // 一、解析方法注解
        // 1. 解析得到：relativeUrl = "images/{id}/{name}"
        relativeUrl = value;
        // httpMethod = MYGET, relativeUrl = image/{id}/{name}, hasBody = false
        String msg = "httpMethod = " + httpMethod + ", relativeUrl = " + value + ", hasBody = " + hasBody;
//        Log.e(TAG, msg);
        System.out.println(msg);

        // 2. 还要将 {id} {name} 给解析出来, relativeUrlParamsName = {"id", "name"}
        Matcher m = PARAM_URL_REGEX.matcher(relativeUrl);
        relativeUrlParamsName = new LinkedHashSet<>();
        while (m.find()) {
            relativeUrlParamsName.add(m.group(1));
        }
    }

    /**
     * 解析方法参数注解
     *   parameterAnnotationArrays[0] = MyQuery(value=page, encoded=false)
     *   parameterAnnotationArrays[1] = MyPath(value=id, encoded=false)
     *   parameterAnnotationArrays[2] = MyPath(value=name, encoded=false)
     */
    private void parseMethodParametersAnnotation() {
        int parameterCount = parameterAnnotationArrays.length;
        for (int i = 0; i < parameterCount; i++) {
            Type parameterType = parameterTypes[i];

            Annotation[] parameterAnnotations = parameterAnnotationArrays[i];
            for (Annotation annotation: parameterAnnotations) {
                if (annotation instanceof MyPath) {
                    MyPath myPath = (MyPath) annotation;
                    String name = myPath.value();
//                    Log.e(TAG, "MyPath value = " + name);
                    // MyPath value = id
                    // MyPath value = name
                    System.out.println("MyPath value = " + name);

                    relativeUrl = relativeUrl.replace("{" + name + "}", args[i].toString());

                } else if (annotation instanceof MyQuery) {
                    MyQuery myQuery = (MyQuery) annotation;
                    String name = myQuery.value();
                    boolean encoded = myQuery.encoded();
//                    Log.e(TAG, "MyQuery value = " + name + ", encoded = " + encoded);

                    // MyQuery value = page, encoded = false
                    System.out.println("MyQuery value = " + name + ", encoded = " + encoded);

                    if (relativeUrl.indexOf("?") < 0) {
                        relativeUrl += "?" + name + "=" + args[i];
                    } else {
                        relativeUrl += "&" + name + "=" + args[i];
                    }
                }
            }
            // 转化参数为对应的类型 Converter

        }
    }

    public String getRelativeUrl() {
        return relativeUrl;
    }
}
