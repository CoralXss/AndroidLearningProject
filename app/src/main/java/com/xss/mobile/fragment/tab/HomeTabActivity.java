package com.xss.mobile.fragment.tab;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.xss.mobile.R;

public class HomeTabActivity extends AppCompatActivity {

    private TabLayout tab_layout;

    private TabFragmentManager tabFragmentManager;

    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tab);

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);

        tabFragmentManager = new TabFragmentManager(this, 0, tab_layout, R.id.tab_content);
        tabFragmentManager.selectTab(0);

        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();
                tabFragmentManager.selectTab(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

}
