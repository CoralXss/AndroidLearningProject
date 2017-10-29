package com.xss.mobile.activity.scrollconflict;

import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xss.mobile.R;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class ViewPagerConflictActivity extends AppCompatActivity {
    private BadViewPager vp_bad;
    private List<View> views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_conflict);

        initView();

        initData(true);
    }

    private void initView() {
        vp_bad = (BadViewPager) findViewById(R.id.vp_bad);
        views = new ArrayList<>();

    }

    private void initData(final boolean isListView) {
        Observable.just("view1", "view2", "view3", "view4")
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        View view;
                        if (isListView) {
                            ListView lv = new ListView(ViewPagerConflictActivity.this);
                            ArrayList<String> list = new ArrayList<>();
                            for (int i = 0; i < 20; i++) {
                                list.add("ITEM " + i);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewPagerConflictActivity.this,
                                    android.R.layout.simple_list_item_1, list);
                            lv.setAdapter(adapter);

                            view = lv;
                        } else {
                            TextView tv = new TextView(ViewPagerConflictActivity.this);
                            tv.setGravity(Gravity.CENTER);
                            tv.setText(s);
                            tv.setClickable(true);

                            view = tv;
                        }

                        views.add(view);
                    }
                });

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(views);
        vp_bad.setAdapter(pagerAdapter);
    }

    class MyPagerAdapter extends PagerAdapter {
        List<View> list;

        public MyPagerAdapter(List<View> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            container.addView(list.get(position));

            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }
    }


}
