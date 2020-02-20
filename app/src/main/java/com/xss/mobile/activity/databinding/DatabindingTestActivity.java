package com.xss.mobile.activity.databinding;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.xss.mobile.R;
import com.xss.mobile.databinding.ActivityDatabindingTestBinding;

import java.util.Timer;
import java.util.TimerTask;

public class DatabindingTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDatabindingTestBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_databinding_test);
        final UserBindEntity entity = new UserBindEntity("Coralline", 24, true, 85.5f);
        dataBinding.setUser(entity);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        dataBinding.rvList.setLayoutManager(layoutManager);
        dataBinding.rvList.setAdapter(new DataBindingAdapter(this, DataManager.getUserList()));

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                index++;
                entity.username.set("Xss " + index);
                Log.e("test", index + "");
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    private int index = 0;


}
