package com.xss.mobile.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xss.mobile.R;
import com.xss.mobile.adapter.BaseViewHolder;
import com.xss.mobile.utils.DensityUtil;
import com.xss.mobile.utils.ViewUtil;

/**
 * Created by xss on 2016/11/1.
 */
public class BookViewHolder extends BaseViewHolder {
    public TextView tv_book_name, tv_book_desc;
    public LinearLayout ll_content;

    public BookViewHolder(View itemView, int itemCount) {
        super(itemView);

        tv_book_name = $(R.id.tv_book_name);
        tv_book_desc = $(R.id.tv_book_desc);
        ll_content = $(R.id.ll_content);
    }

    public Context getContext() {
        return ll_content.getContext();
    }

    public void initView(int itemCount) {
        int screenWidth = ViewUtil.getScreenWidth(getContext());
        int itemSpace = DensityUtil.dip2px(getContext(), 10f);
        if (itemCount > 2) {
            itemCount = 2;
        }
        int itemWidth = (screenWidth - (itemCount + 1) * itemSpace) / itemCount;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tv_book_name.getLayoutParams();
        lp.width = itemWidth;
    }
}
