package com.xss.mobile.activity.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xss on 2017/5/9.
 */

@Entity
public class User {
    @Id
    private Long id;

    private String name;

    @Transient
    private int tempUsageCount;

    @Generated(hash = 873297011)
    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    @Generated(hash = 873297011)
//    public User(Long id, String name) {
//        this.id = id;
//        this.name = name;
//    }
//
//    @Generated(hash = 586692638)
//    public User() {
//        DaoMaster m;
//    }
//
//    public Long getId() {
//        return this.id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return this.name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

}
