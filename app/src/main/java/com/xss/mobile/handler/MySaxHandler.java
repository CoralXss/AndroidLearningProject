package com.xss.mobile.handler;

import android.text.TextUtils;

import com.xss.mobile.entity.PersonEntity;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xss on 2016/10/13.
 */
public class MySaxHandler extends DefaultHandler {
    private List<PersonEntity> persons;
    private PersonEntity person;

    // 存放当前解析到的标签名
    private String curretnTag;
    // 存放当前解析到的标签的文本值
    private String currentValue;

    public List<PersonEntity> getPersons() {
        return persons;
    }

    // 当解析到文档开始时的回调方法
    @Override
    public void startDocument() throws SAXException {
        persons = new ArrayList<>();
    }

    // 当解析到 xml 标签时的回调方法
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("person".equals(qName)) {
            person = new PersonEntity();
            // 得到元素的属性值
            for (int i = 0; i < attributes.getLength(); i++) {
                if ("id".equals(attributes.getQName(i))) {
                    person.id = Integer.parseInt(attributes.getValue(i));
                }
            }
        }

        // 设置当前的标签名
        curretnTag = qName;
    }

    // 当解析到 xml 文本内容时的回调方法
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // 得到当前的文本内容
        currentValue = new String(ch, start, length);
        // 当currentValue不为 null, "" 以及换行时
        if (!TextUtils.isEmpty(currentValue) && !"\n".equals(currentValue)) {
            // 判断当前的currentTag是哪个标签
            if ("name".equals(curretnTag)) {
                // 若是 name元素，则通过 nextText()方法得到元素的值
                person.name = currentValue;
            } else if ("age".equals(curretnTag)) {
                // 若是 age，则通过 nextText()方法得到元素的值
                person.age = Integer.parseInt(currentValue);
            } else if ("grade".equals(curretnTag)) {
                // 若是 age，则通过 nextText()方法得到元素的值
                person.grade = Double.parseDouble(currentValue);
            }else if ("isGirl".equals(curretnTag)) {
                // 若是 age，则通过 nextText()方法得到元素的值
                person.isGirl = Boolean.parseBoolean(currentValue);
            }
        }

        // 清空currentTag 和 currentValue
        currentValue = null;
        curretnTag = null;
    }

    // 当解析到标签结束时的回调方法
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("person".equals(qName)) {
            persons.add(person);
            person = null;
        }
    }
}
