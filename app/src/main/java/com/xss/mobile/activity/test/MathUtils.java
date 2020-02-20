//package com.xss.mobile.activity.test;
//
//import java.util.ArrayList;
//
///**
// * Created by xss on 2017/2/23.
// */
//
//public class MathUtils {
//
//    public static void testRemove14List(ArrayList<Integer> src) {
//
//        for (int i = 0; i < src.size(); i++) {
//            for (int j = i; j < src.size(); j++) {
//                if ((src.get(i) + src.get(j)) == 14) {
//                    src.remove(i);
//                    src.remove(j);
//                }
//            }
//        }
//
//        if (!src.isEmpty()) {
//            for (int i = 0; i < src.size(); i++) {
//                System.out.println(src.get(i) + ", ");
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        Integer[] values = new Integer[] { 0, 12, 2, 3, 11, 12, 3, 8, 6, 7 };
//        ArrayList<Integer> list = new ArrayList<>();
//        for (Integer val: values) {
//            list.add(val);
//        }
//        MathUtils.testRemove14List(list);
//
//    }
//}
