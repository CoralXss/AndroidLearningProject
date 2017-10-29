package com.xss.mobile.activity.databinding;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.xss.mobile.BR;

/**
 * Created by xss on 2017/10/12.
 * 注：只读并且后期不被修改
 */

public final class UserObservableEntity extends BaseObservable {

    private String username;

    private int userAge;

    private boolean isGirl;

    private float grade;

    public UserObservableEntity(String username, int userAge, boolean isGirl, float grade) {
        this.username = username;
        this.userAge = userAge;
        this.isGirl = isGirl;
        this.grade = grade;
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    @Bindable
    public float getGrade() {
        return grade;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    public void setGrade(float grade) {
        this.grade = grade;
        notifyPropertyChanged(BR.grade);
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    /**
     * 更新所有数据（要更新的数据 get 方法添加 @Bindable 注解）
     */
    public void notifyDataChanged() {
        notifyPropertyChanged(BR._all);
    }

    public void changeData(View view) {
        this.username = "new name";
        this.userAge = 18;
        this.grade = 100.0f;
        notifyDataChanged();
    }
}
