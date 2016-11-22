package com.xss.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc：
 * Author: xss
 * Time：2016/1/7 10:00
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
    protected Context context;
    protected LayoutInflater inflater;
    protected List<T> itemList = new ArrayList<T>();

    public BaseListAdapter(Context context) {
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
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    public abstract View buildView(int position, View convertView, ViewGroup parent);

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = buildView(i, convertView, viewGroup);

        return view;
    }

    private View view;
    protected View setContentView(int layoutId) {
        view = inflater.inflate(layoutId, null);
        return view;
    }

    protected <T extends View> T $(int viewID) {
        return (T) view.findViewById(viewID);
    }
}
