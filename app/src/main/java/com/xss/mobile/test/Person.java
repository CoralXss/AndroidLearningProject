package com.xss.mobile.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    public static void main(String[] args) {
        try {
            String date = "2017-11-10";
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            Date parse = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date);
            calendar.setTime(parse);

            Calendar curCalendar = Calendar.getInstance(Locale.getDefault());

            int day = curCalendar.get(Calendar.DAY_OF_WEEK);
            int day2 = calendar.get(Calendar.DAY_OF_WEEK);
            System.out.println("week " + day +", "+ day2);
            if (curCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                    curCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                    day + 1 == day2) {
                System.out.println("明日");

            } else {
                System.out.println("非明日");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
