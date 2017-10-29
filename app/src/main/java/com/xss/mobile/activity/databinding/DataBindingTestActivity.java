package com.xss.mobile.activity.databinding;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xss.mobile.R;
import com.xss.mobile.databinding.ActivityDataBindingTestBinding;

public class DataBindingTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_data_binding_test);

        ActivityDataBindingTestBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding_test);
        User user = new User("xss", "24");
        binding.setUser(user);
    }
}
