package com.xss.mobile.test;

/**
 * Created by xss on 2017/3/23.
 */

public class Person {

    private final int    id;
    private final String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
