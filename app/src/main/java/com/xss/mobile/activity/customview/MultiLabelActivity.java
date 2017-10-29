package com.xss.mobile.activity.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xss.mobile.R;
import com.xss.mobile.utils.DensityUtil;
import com.xss.mobile.utils.ViewUtil;

public class MultiLabelActivity extends AppCompatActivity {
    String[] LABELS = {"ABCD", "BCDE", "CDEF", "DEFG", "EFGH", "FGHI", "GHIJ", "HIJK"};

    private RelativeLayout rl_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multilabel);

        initView();

    }

    private void initView() {
        int screenWidth = ViewUtil.getScreenWidth(this);
        int padding = DensityUtil.dip2px(this, 5);

        rl_content = (RelativeLayout) findViewById(R.id.rl_content);

        rl_content.removeAllViews();

        for (int i = 0; i < LABELS.length; i++) {
            int itemWidth = (screenWidth - 3 * 10) / 3;

            View view = LayoutInflater.from(this).inflate(R.layout.item_view_label, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_label);
            textView.setText(LABELS[i]);

            int paddingLeft = (i % 3) * itemWidth;
            // 通过设置上间距来换行
            int paddingTop = (i / 3) * DensityUtil.dip2px(this, 35);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(paddingLeft, paddingTop, 0, 0);
//            textView.setLayoutParams(lp);

            rl_content.addView(view, lp);
        }



    }


}
