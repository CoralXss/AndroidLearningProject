package com.xss.mobile.activity.databinding.dbinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xss.mobile.R;
import com.xss.mobile.activity.databinding.UserBindEntity;
import com.xss.mobile.activity.databinding.UserObservableEntity;
import com.xss.mobile.custom.CustomBinding;

public class CustomBindingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CustomBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_custom_binding);
        UserObservableEntity entity = new UserObservableEntity("xss", 23, true, 90.0f);
        binding.setUser(entity);

        UserBindEntity bindEntity = new UserBindEntity("Bind Coral", 23, true, 59.9f);
        binding.setUserBind(bindEntity);

    }

}
