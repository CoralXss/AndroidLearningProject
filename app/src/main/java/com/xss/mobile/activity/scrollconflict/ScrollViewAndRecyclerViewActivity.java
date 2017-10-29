package com.xss.mobile.activity.scrollconflict;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xss.mobile.R;

import java.util.ArrayList;
import java.util.List;

public class ScrollViewAndRecyclerViewActivity extends AppCompatActivity {
    private RecyclerView rv_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view_and_recycler_view);

        initView();
    }

    private void initView() {
        rv_list = (RecyclerView) findViewById(R.id.rv_list);

        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(this);
        rv_list.setLayoutManager(linearLayoutManager);

        MyRecyclerAdapter adapter = new MyRecyclerAdapter(getListData());
        rv_list.setAdapter(adapter);

        findViewById(R.id.tv_click1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScrollViewAndRecyclerViewActivity.this, ScrollConflictActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.tv_click2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScrollViewAndRecyclerViewActivity.this, ViewPagerConflictActivity.class);
                startActivity(intent);
            }
        });
    }

    // 向listView 中加载100条数据，造成卡顿时如何优化
    private List<String> getListData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("Will it be 卡顿 ?");
        }
        return list;
    }
}
