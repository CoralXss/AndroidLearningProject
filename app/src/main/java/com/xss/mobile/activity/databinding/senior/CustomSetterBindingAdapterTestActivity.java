package com.xss.mobile.activity.databinding.senior;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.xss.mobile.R;
import com.xss.mobile.databinding.ActivityCustomSetterBindingAdapterTestBinding;

public class CustomSetterBindingAdapterTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCustomSetterBindingAdapterTestBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_custom_setter_binding_adapter_test);
        binding.itemView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomSetterBindingAdapterTestActivity.this, "Click Parent", Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * 在executeBindings() 中调用 BindAdapter.setPermissions(itemView1, Permissions.P_ADD);
         * 给其他view设置app:permissions属性，则调用 BindAdapter.setPermissions(tvMsg, Permissions.P_ADD);
         */

        binding.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomSetterBindingAdapterTestActivity.this, "Click TV Name", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
