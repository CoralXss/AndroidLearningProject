package com.xss.mobile.activity.memorytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.xss.mobile.R;
import com.xss.mobile.activity.scrollconflict.DataListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListViewTestActivity extends AppCompatActivity {
    private ListView lv_list;

    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_test);

        initData();

        initView();
    }

    private void initView() {
        lv_list = (ListView) findViewById(R.id.lv_list);
        DataListAdapter adapter = new DataListAdapter(this, list);
        lv_list.setAdapter(adapter);
    }

    // 向listView 中加载100条数据，造成卡顿时如何优化
    private void initData() {
        list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("Will it be 卡顿 ?");
        }
    }
}
