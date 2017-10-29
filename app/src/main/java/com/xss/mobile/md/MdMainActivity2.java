package com.xss.mobile.md;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.xss.mobile.R;

public class MdMainActivity2 extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_md_main_2);

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("Material Design");
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        //使用CollapsingToolbarLayout后，title需要设置到CollapsingToolbarLayout上
//        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitle("失控");
//
//        findViewById(R.id.tv_content).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MdMainActivity2.this, CoordinatorTestActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}
