package com.xss.mobile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc：
 * Author: xss
 * Time：2016/2/16 15:05
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    protected Context context;
    protected LayoutInflater inflater;
    protected List<T> itemList = new ArrayList<T>();

    public BaseRecyclerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 判断数据是否为空
     * @return
     */
    public boolean isEmpty() {
        return itemList.isEmpty();
    }

    /**
     * 在原有的数据上添加新数据
     * @param itemList
     */
    public void addItems(List<T> itemList) {
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    /**
     * 设置为新的数据，旧数据会被清空
     * @param itemList
     */
    public void setItems(List<T> itemList) {
        this.itemList.clear();
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    /**
     * 清空数据
     */
    public void clearItems() {
        itemList.clear();
        notifyDataSetChanged();
    }

    @Override
    public abstract BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i);

    @Override
    public abstract void onBindViewHolder(BaseViewHolder baseViewHolder, int i);

    @Override
    public int getItemCount() {
        return itemList == null ? 0: itemList.size();
    }

    protected OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * 内部接口回调方法
     */
    public interface OnItemClickListener<T> {
        void onItemClick(int position, T object);
    }
}
