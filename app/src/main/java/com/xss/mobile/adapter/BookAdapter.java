package com.xss.mobile.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xss.mobile.R;
import com.xss.mobile.adapter.holder.BookViewHolder;
import com.xss.mobile.entity.BookEntity;

/**
 * Created by xss on 2016/11/1.
 */
public class BookAdapter extends BaseRecyclerAdapter<BookEntity> {

    public BookAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_item_book, null);

        return new BookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder baseViewHolder, int i) {
        BookViewHolder holder = (BookViewHolder) baseViewHolder;
        BookEntity entity = itemList.get(i);
        holder.tv_book_name.setText(entity.name);
    }
}
