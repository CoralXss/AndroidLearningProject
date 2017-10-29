package com.xss.mobile.activity.annotation;

import com.google.gson.Gson;

/**
 * Created by xss on 2017/10/26.
 */

public class EnumerationTest {

    public static class Person {
        public String name;

        public Gender gender;

        public Person(String name, Gender gender) {
            this.name = name;
            this.gender = gender;
        }

        @Override
        public String toString() {

            return "person = { name = + " + name + "" + " + gender + " + gender + "\n" + Gender.BOY.name() + Gender.BOY.ordinal() + "}";
        }
    }

    public enum Gender {
        BOY, GIRL
    }

    public static void main(String[] args) {
        Person person = new Person("Coral", Gender.GIRL);
        Gson gson = new Gson();
        String json = gson.toJson(person);
        System.out.println(json);

        Person newPerson = gson.fromJson(json, Person.class);
        System.out.println(newPerson.toString());
    }
}
