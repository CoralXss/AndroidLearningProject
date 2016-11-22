package com.xss.mobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.xss.mobile.R;
import com.xss.mobile.widget.ComposeLabelEditView;

/**
 * Created by xss on 2016/11/2.
 */
public class ComposeViewTestActivity extends Activity {
    private ComposeLabelEditView composeLabelEditView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_view_test);

        composeLabelEditView = (ComposeLabelEditView) findViewById(R.id.composeLabelEditView);

        LinearLayout ll_parent = (LinearLayout) findViewById(R.id.ll_parent);
        LayoutInflater.from(this).inflate(R.layout.layout_compose_label_edit_view, ll_parent, true);
    }
}
