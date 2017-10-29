package com.xss.mobile.activity.annotation;

/**
 * Created by xss on 2017/2/13.
 * desc：2、注解使用
 */

public class Apple {

    @FruitName("Apple")
    private String appleName;

    @FruitColor(fruitColor = FruitColor.Color.GREEN)
    private String appleColor;

    public void setAppleName(String appleName) {
        this.appleName = appleName;
    }

    public String getAppleName() {
        return appleName;
    }

    public void setAppleColor(String appleColor) {
        this.appleColor = appleColor;
    }

    public String getAppleColor() {
        return appleColor;
    }

    public String getDesc() {
        return "The fruit name is " + appleName + ", color = " + appleColor;
    }

}
