package com.xss.mobile.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xss on 2017/6/7.
 */

public class MovieEntity implements Serializable {

    public int count;
    public List<Subject> subjects;

    public static class Subject implements Serializable {
        String title;
        String original_title;
        String subtype;
        String year;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("一共" + count + "条数据" + "\n");

        if (subjects != null && !subjects.isEmpty()) {
            int size = subjects.size();
            for (int i = 0; i < size; i++) {
                sb.append(subjects.get(i).title + ", " + subjects.get(i).subtype + ", " + subjects.get(i).year);
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}
