package com.xss.mobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.ScrollView;

import com.xss.mobile.R;
import com.xss.mobile.adapter.BookAdapter;
import com.xss.mobile.entity.BookEntity;
import com.xss.mobile.widget.MyRecyclerView;

import java.util.ArrayList;

/**
 * Created by xss on 2016/11/1.
 * desc:测试 ScrollView中嵌套 水平滚动 和 竖直滚动 冲突问题
 */
public class BookListActivity extends Activity {
    private static final String TAG = "BookListActivity";
    private ScrollView scrollView;
    private RecyclerView rv_horizontal_list;
    private MyRecyclerView rv_vertical_list;
    private BookAdapter bookHorizontalAdapter, bookVerticalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        initView();
    }

    private void initView() {
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        rv_horizontal_list = (RecyclerView) findViewById(R.id.rv_horizontal_list);
        rv_vertical_list = (MyRecyclerView) findViewById(R.id.rv_vertical_list);

        bookHorizontalAdapter = new BookAdapter(this);
        bookHorizontalAdapter.setItems(getHorizontalData());

        bookVerticalAdapter = new BookAdapter(this);
        bookVerticalAdapter.setItems(getVerticalData());

        LinearLayoutManager horizontalManager = new LinearLayoutManager(this);
        horizontalManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_horizontal_list.setLayoutManager(horizontalManager);
        rv_horizontal_list.setAdapter(bookHorizontalAdapter);

        LinearLayoutManager verticalManager = new LinearLayoutManager(this);
        verticalManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_vertical_list.setLayoutManager(verticalManager);
        rv_vertical_list.setAdapter(bookVerticalAdapter);

        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                Log.e(TAG, "scrollView onScroll");
            }
        });

        rv_vertical_list.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                Log.e(TAG, "rv_vertical_list onScroll");
            }
        });
    }


    private ArrayList<BookEntity> getHorizontalData() {
        ArrayList<BookEntity> list = new ArrayList<>();
        BookEntity entity = new BookEntity();
        entity.name = "C++程序设计";
        list.add(entity);
        entity = new BookEntity();
        entity.name = "Thinking in Java";
        list.add(entity);
        entity = new BookEntity();
        entity.name = "Android程序设计";
        list.add(entity);
        entity = new BookEntity();
        entity.name = "Phony程序设计";
        list.add(entity);
        entity = new BookEntity();
        entity.name = "C#程序设计";
        list.add(entity);

        return list;
    }

    private ArrayList<BookEntity> getVerticalData() {
        ArrayList<BookEntity> list = new ArrayList<>();
        BookEntity entity = new BookEntity();
        entity.name = "计算机网络";
        list.add(entity);
        entity = new BookEntity();
        entity.name = "数据结构";
        list.add(entity);
        entity = new BookEntity();
        entity.name = "操作系统";
        list.add(entity);
        entity = new BookEntity();
        entity.name = "编译原理";
        list.add(entity);
        entity = new BookEntity();
        entity.name = "软件工程";
        list.add(entity);
        entity = new BookEntity();
        entity.name = "项目设计";
        list.add(entity);
        entity = new BookEntity();
        entity.name = "编译原理111";
        list.add(entity);
        entity = new BookEntity();
        entity.name = "软件工程222";
        list.add(entity);
        entity = new BookEntity();
        entity.name = "项目设计333";
        list.add(entity);
        entity = new BookEntity();
        entity.name = "计算机网络444";
        list.add(entity);
        entity = new BookEntity();
        entity.name = "数据结构555";
        list.add(entity);
        entity = new BookEntity();
        entity.name = "操作系统666";
        list.add(entity);
        entity = new BookEntity();
        entity.name = "操作系统7777";
        list.add(entity);

        return list;
    }
}
