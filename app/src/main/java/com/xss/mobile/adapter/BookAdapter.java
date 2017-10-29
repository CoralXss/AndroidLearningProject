package com.xss.mobile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xss.mobile.R;
import com.xss.mobile.adapter.holder.BookViewHolder;
import com.xss.mobile.entity.BookEntity;
import com.xss.mobile.utils.DensityUtil;
import com.xss.mobile.utils.ViewUtil;

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
        initView(v, getItemCount());
        return new BookViewHolder(v, getItemCount());
    }

    public void initView(View v, int itemCount) {
        int screenWidth = ViewUtil.getScreenWidth(context);
        int itemSpace = DensityUtil.dip2px(context, 10f);
        if (itemCount > 2) {
            itemCount = 2;
        }
        int itemWidth = (screenWidth - (itemCount + 1) * itemSpace) / itemCount;
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(itemWidth, ViewGroup.LayoutParams.MATCH_PARENT);
//        lp.setMargins(itemSpace, itemSpace, 0, itemSpace);
        v.setLayoutParams(lp);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder baseViewHolder, int i) {
        final BookViewHolder holder = (BookViewHolder) baseViewHolder;
        BookEntity entity = itemList.get(i);
        holder.tv_book_name.setText(entity.name);

        holder.tv_book_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean visible = holder.tv_book_desc.getVisibility() == View.VISIBLE;
                holder.tv_book_desc.setVisibility(!visible ? View.VISIBLE : View.GONE);
            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
