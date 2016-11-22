package com.xss.mobile.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.xss.mobile.R;
import com.xss.mobile.adapter.BaseViewHolder;

/**
 * Created by xss on 2016/11/1.
 */
public class BookViewHolder extends BaseViewHolder {
    public TextView tv_book_name;

    public BookViewHolder(View itemView) {
        super(itemView);

        tv_book_name = $(R.id.tv_book_name);
    }
}
