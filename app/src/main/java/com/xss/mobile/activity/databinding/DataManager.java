package com.xss.mobile.activity.databinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xss on 2017/10/12.
 */

public class DataManager {

    public static List<String> getStringList() {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            list.add("第 " + i + " 个Item");
        }
        return list;
    }

    public static List<UserBindEntity> getUserList() {
        List<UserBindEntity> list = new ArrayList<>();

        UserBindEntity u = new UserBindEntity("A", 10, true, 80.0f);
        list.add(u);
        u = new UserBindEntity("A", 20, true, 70.0f);
        list.add(u);
        u = new UserBindEntity("B", 30, true, 60.0f);
        list.add(u);
        u = new UserBindEntity("C", 40, true, 80.0f);
        list.add(u);
        u = new UserBindEntity("D", 50, true, 90.0f);
        list.add(u);

        return list;
    }
}
