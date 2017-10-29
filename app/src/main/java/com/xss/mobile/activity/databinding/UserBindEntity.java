package com.xss.mobile.activity.databinding;

import android.databinding.ObservableField;
import android.databinding.ObservableFloat;
import android.view.View;

/**
 * Created by xss on 2017/10/12.
 * 注：只读并且后期不被修改
 */

public class UserBindEntity {

    public ObservableField<String> username = new ObservableField<>();
    public ObservableFloat grade = new ObservableFloat();

    public int userAge;

    public boolean isGirl;

    public UserBindEntity(String username, int userAge, boolean isGirl, float grade) {
        this.username.set(username);
        this.grade.set(grade);
        this.userAge = userAge;
        this.isGirl = isGirl;
    }

    public void changeData(View view) {
        this.username.set("New Bind name");
        this.grade.set(100.0f);
        this.userAge = 10;
    }

    public String getUsername() {
        return username.get();
    }
}
