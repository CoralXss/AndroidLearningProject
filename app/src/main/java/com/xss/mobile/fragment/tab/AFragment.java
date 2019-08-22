package com.xss.mobile.fragment.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xss.mobile.R;

/**
 * Created by xss on 2019/8/7.
 */
public class AFragment extends BaseTabFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.fragment_home_tab, container, false);

        TextView tv_msg = (TextView) contentView.findViewById(R.id.tv_msg);
        tv_msg.setText("Fragment A");

        return contentView;
    }
}
