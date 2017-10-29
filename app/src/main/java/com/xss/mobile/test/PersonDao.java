package com.xss.mobile.test;

/**
 * Created by xss on 2017/3/23.
 */

public interface PersonDao {

    Person getPerson(int id);

    boolean update(Person person);
}
