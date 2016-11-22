package com.xss.mobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xss.mobile.R;
import com.xss.mobile.entity.BookEntity;
import com.xss.mobile.entity.PersonEntity;
import com.xss.mobile.utils.parser.XmlParserUtils;
import com.xss.mobile.widget.MyLinearLayout;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by xss on 2016/10/13.
 */
public class XmlParserActivity extends Activity {
    private String TAG = "XmlParserActivity";

    private TextView tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml_parser);
        Log.e(TAG, "onCreate");

        tv_content = (TextView) findViewById(R.id.tv_content);

        final MyLinearLayout myLinearLayout = (MyLinearLayout) findViewById(R.id.myLinearLayout);


        findViewById(R.id.btn_parser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try {
//
////                    parserByDom();
////                    XmlParserActivity.this.parserBySax();
////                    test();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (ParserConfigurationException e) {
//                e.printStackTrace();
//            } catch (SAXException e) {
//                e.printStackTrace();
//            }

                myLinearLayout.invalidate();
            }
        });
    }


    private void parserBySax() throws IOException, ParserConfigurationException, SAXException {
        Log.e(TAG, " /***** SAX *****/ ");
        InputStream is = getAssets().open("persons.xml");
        List<PersonEntity> list = XmlParserUtils.parserBySax(is);
        Log.e(TAG, "list size " + list.size());
        for (PersonEntity p: list) {
            Log.e(TAG, "Person " + p.id + ", " + p.name + "," + p.age);
        }
    }

    private void parserByPull() throws IOException {
        Log.e(TAG, " /***** PULL *****/ ");
        InputStream is = getAssets().open("persons.xml");
        List<PersonEntity> list = XmlParserUtils.parserXmlByPull(is, "");
        Log.e(TAG, "list size " + list.size());
        for (PersonEntity p: list) {
            Log.e(TAG, "Person " + p.id + ", " + p.name + "," + p.age);
        }
    }

    private void parserByDom() throws IOException {
        InputStream is = getAssets().open("persons.xml");
        List<PersonEntity> list = XmlParserUtils.parseXmlByDom(is, "person");
        Log.e(TAG, "list size " + list.size());
        for (PersonEntity p: list) {
            Log.e(TAG, "Person " + p.id + ", " + p.name + "," + p.age);
        }

        Log.e(TAG, "通过反射得到");
        InputStream iss = getAssets().open("persons.xml");
        String nodes[] = {"name", "age"};
        List objs = XmlParserUtils.parseXmlByDom(iss, "com.xss.mobile.entity.PersonEntity", "person");
        Log.e(TAG, "objs size " + list.size());
        for (Object o: objs) {
            PersonEntity p = (PersonEntity) o;
            Log.e(TAG, "Person " + p.id + ", " + p.name + "," + p.age + "," + p.grade + "," + (p.isGirl ? "a girl" : "a boy"));
        }

        Log.e(TAG, "///////////////////////");
        InputStream is_book = getAssets().open("books.xml");
        List books = XmlParserUtils.parseXmlByDom(is_book, "com.xss.mobile.entity.BookEntity", "book");
        Log.e(TAG, "books size " + books.size());
        for (Object o: books) {
            BookEntity b = (BookEntity) o;
            Log.e(TAG, "Book " + b.id + ", " + b.name + "," + b.publisher + "," + b.publishTime);
        }
    }

    private void test() {
        try {
            Class c = Class.forName("com.xss.mobile.entity.PersonEntity");

            StringBuilder sb = new StringBuilder();
            sb.append(Modifier.toString(c.getModifiers()) + " class " + c.getSimpleName() + "\n");

            // 属性
            Field[] fields = c.getDeclaredFields();
            for (Field f: fields) {
                sb.append("\t")
                        .append(Modifier.toString(f.getModifiers()) + " ")   // 属性修饰符
                        .append(f.getType().getSimpleName() + " ")      // 属性类型名
                        .append(f.getName() + ";\n");                 // 属性名
            }

            /** 打印结果：
             public class PersonEntity
             public String name;
             public int id;
             public int age;
             */
            Log.e(TAG, "reflect: " + sb.toString());


            // 给属性注入值
            for (Field f: fields) {

            }
            Field nameF = c.getDeclaredField("name");
            // 实例化这个类赋给o
            Object o = c.newInstance();
            nameF.setAccessible(true);
            nameF.set(o, "Hahaha");

            Log.e(TAG, "nameF: " + nameF.get(o));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
