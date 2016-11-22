package com.xss.mobile.utils.parser;

import android.provider.DocumentsContract;
import android.util.Log;

import com.xss.mobile.entity.PersonEntity;
import com.xss.mobile.handler.MySaxHandler;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by xss on 2016/10/13.
 */
public class XmlParserUtils {
    /**
     * 1、Dom解析xml优点：可以随时访问到某个节点的相邻节点，对xml文档的插入也非常方便，
     *      缺点：会将整个xml文档加载到内存中，大大占用我们的内存资源，所以在手机中很少使用Dom解析xml。
     * 2、Pull解析
     *
     * 3、SAX(Simple API for XML)
     *
     *
     *
     *
     *
     *
     */


    /**
     * Dom方式解析Xml格式数据：直接解析到对应的对象中
     * @param is
     * @param element 元素名
     * @return
     */
    public static List<PersonEntity> parseXmlByDom(InputStream is, String element) {
        List<PersonEntity> list = new ArrayList<>();
        // 1、得到一个DocumentBuilderFactory解析工厂类
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            // 2、得到一个DocumentBuilder解析类
            DocumentBuilder builder = factory.newDocumentBuilder();
            // 3、接收一个xml的字符串来解析xml，Document代表整个xml文档
            Document document = builder.parse(is);
            // 4、得到xml文档的根元素节点
            Element personsElement = document.getDocumentElement();
            // 5、得到标签为person的 Node对象集合 NodeList
            NodeList nodeList = personsElement.getElementsByTagName(element);
            for (int i = 0; i < nodeList.getLength(); i++) {
                PersonEntity person = new PersonEntity();
                // 如果该Node是一个Element
                if (nodeList.item(i).getNodeType() == Document.ELEMENT_NODE) {
                    Element personElement = (Element) nodeList.item(i);
                    // 得到id的属性值
                    String id = personElement.getAttribute("id");
                    person.id = Integer.parseInt(id);

                    // 得到person元素下的子元素
                    NodeList childNodesList = personElement.getChildNodes();
                    for (int j = 0; j < childNodesList.getLength(); j++) {
                        if (childNodesList.item(j).getNodeType() == Document.ELEMENT_NODE) {
                            String childNodeName = childNodesList.item(j).getNodeName();
                            if ("name".equals(childNodeName)) {
                                // 得到name标签文本值
                                String name = childNodesList.item(j).getFirstChild().getNodeValue();
                                person.name = name;
                            }
                            if ("age".equals(childNodeName)) {
                                // 得到age标签文本值
                                String age = childNodesList.item(j).getFirstChild().getNodeValue();
                                person.age = Integer.parseInt(age);
                            }
                        }
                    }
                }

                list.add(person);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 使用本方法解析xml数据，通过反射给属性注入值，保证以下几点：
     *      1）xml中只有一层结构；
     *      2）实体类中只支持基本数据类型及其对应的类类型；
     *      3）xml中在元素节点要有一个 id属性;
     *      4）xml中的子元素名称和个数和实体类中成员变量元素名和个数一致
     * @param is
     * @param className  对应实体类名
     * @param element    元素根节点名
     * @return   最终list中存储的为Object类型数据，但是可以强转得到相应的对象
     */
    public static List parseXmlByDom(InputStream is, String className, String element) {
        List list = new ArrayList<>();

        try {
            // 通过反射得到Class类
            Class c = Class.forName(className);

            // 1、得到一个DocumentBuilderFactory解析工厂类
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // 2、得到一个DocumentBuilder解析类
            DocumentBuilder builder = factory.newDocumentBuilder();
            // 3、接收一个xml的字符串来解析xml，Document代表整个xml文档
            Document document = builder.parse(is);
            // 4、得到xml文档的根元素节点
            Element personsElement = document.getDocumentElement();
            // 5、得到标签为person的 Node对象集合 NodeList
            NodeList nodeList = personsElement.getElementsByTagName(element);
            for (int i = 0; i < nodeList.getLength(); i++) {
                // 实例化该类
                Object obj = c.newInstance();

                // 如果该Node是一个Element
                if (nodeList.item(i).getNodeType() == Document.ELEMENT_NODE) {
                    Element personElement = (Element) nodeList.item(i);
                    // 得到id的属性值
                    String id = personElement.getAttribute("id");
                    Field idF = c.getDeclaredField("id");
                    idF.set(obj, Integer.parseInt(id));   // 给obj对象的 id赋值

                    // 得到person元素下的子元素
                    NodeList childNodesList = personElement.getChildNodes();
                    for (int j = 0; j < childNodesList.getLength(); j++) {
                        if (childNodesList.item(j).getNodeType() == Document.ELEMENT_NODE) {
                            String childNodeName = childNodesList.item(j).getNodeName();

                            // 当取 fields[j]时，此处必须保证 xml中的子元素名称个数 和 实体类中成员变量名称个数完全一致
                            Field field = c.getDeclaredField(childNodeName);
                            field.setAccessible(true);

                            if (field.getName().equals(childNodeName)) {
                                // 得到标签文本值
                                String value = childNodesList.item(j).getFirstChild().getNodeValue();
                                // 给反射获取的属性设置值
                                setValueByType(obj, field, field.getType().getSimpleName(), value);
                            }
                        }
                    }
                }

                list.add(obj);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 通过Pull方式解析xml格式数据
     * @param is
     * @param className
     * @return
     */
    public static List<PersonEntity>  parserXmlByPull(InputStream is, String className) {
        List<PersonEntity> persons = null;
        PersonEntity person = null;

        try {
            // 1、创建XmlPullParserFactory解析工厂
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            // 2、通过XmlPullParserFactory工厂实例化一个XmlPullParser解析类
            XmlPullParser parser = factory.newPullParser();
            // 3、根据指定的编码解析xml文档
            parser.setInput(is, "utf-8");

            // 4、得到当前的时间类型
            int eventType = parser.getEventType();
            // 5、只要没有解析到xml的文档结果，就一直解析
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    // 解析到文档开始时
                    case XmlPullParser.START_DOCUMENT:
                        persons = new ArrayList<>();
                        break;
                    // 解析到xml标签时
                    case XmlPullParser.START_TAG:
                        if ("person".equals(parser.getName())) {
                            person = new PersonEntity();
                            // 得到person元素的第一个属性 id
                            person.id = Integer.parseInt(parser.getAttributeValue(0));
                        } else if ("name".equals(parser.getName())) {
                            // 若是 name元素，则通过 nextText()方法得到元素的值
                            person.name = parser.nextText();
                        } else if ("age".equals(parser.getName())) {
                            // 若是 age，则通过 nextText()方法得到元素的值
                            person.age = Integer.parseInt(parser.nextText());
                        } else if ("grade".equals(parser.getName())) {
                            // 若是 age，则通过 nextText()方法得到元素的值
                            person.grade = Double.parseDouble(parser.nextText());
                        }else if ("isGirl".equals(parser.getName())) {
                            // 若是 age，则通过 nextText()方法得到元素的值
                            person.isGirl = Boolean.parseBoolean(parser.nextText());
                        }
                        break;
                    // 解析到xml标签结束时
                    case XmlPullParser.END_TAG:
                        if ("person".equals(parser.getName())) {
                            persons.add(person);
                            person = null;
                        }
                        break;
                }
                // 通过next()方法触发下一个事件
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return persons;
    }

    /**
     * 通过sax解析xml
     * @param is
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static List<PersonEntity> parserBySax(InputStream is) throws ParserConfigurationException, SAXException, IOException {
        // 1、创建一个 SAXParserFactory解析工厂
        SAXParserFactory factory = SAXParserFactory.newInstance();
        // 2、实例化一个SAXParser解析类
        SAXParser parser = factory.newSAXParser();
        // 3、实例化自定义DefaultHandler
        MySaxHandler handler = new MySaxHandler();
        // 4、根据自定义的handler来解析xml文档
        parser.parse(is, handler);

        return handler.getPersons();
    }

    /**
     * 通过Java反射给对象属性注入value值
     * @param o
     * @param f
     * @param type
     * @param value
     * @throws IllegalAccessException
     */
    private static void setValueByType(Object o, Field f, String type, String value) throws IllegalAccessException {
        if ("int".equals(type)) {
            f.setInt(o, Integer.parseInt(value));
        } else if ("long".equals(type)) {
            f.setLong(o, Long.parseLong(value));
        } else if ("short".equals(type)) {
            f.setShort(o, Short.parseShort(value));
        } else if ("float".equals(type)) {
            f.setFloat(o, Float.parseFloat(value));
        } else if ("double".equals(type)) {
            f.setDouble(o, Double.parseDouble(value));
        }else if ("char".equals(type)) {
            f.setChar(o, value.toCharArray()[0]);   // todo char只代表一个字符,取数组第一个即可
        } else if ("double".equals(type)) {
            f.setDouble(o, Double.parseDouble(value));
        } else if ("boolean".equals(type)) {
            f.setBoolean(o, Boolean.parseBoolean(value));
        } else {
            f.set(o, value);
        }
    }

}
