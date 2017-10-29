package com.xss.mobile.activity.dagger2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liumeng on 16/1/3.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> {

    protected List<T> dataList;
    protected LayoutInflater inflater;
    private Context context;
    private OnItemClickListener<T> onItemClickListener;
    private OnItemLongClickListener<T> onItemLongClickListener;

    public BaseRecyclerAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataList = new ArrayList<>();
    }

    public OnItemClickListener<T> getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public T getItem(int position) {
        if (0 <= position && position < dataList.size()) {
            return dataList.get(position);
        }
        return null;
    }

    public Context getContext() {
        return context;
    }

    public int getDataSize() {
        return dataList.size();
    }

    public void addItem(T item) {
        dataList.add(item);
        notifyDataSetChanged();
    }

    public void addItems(List<T> list) {
        if (!list.isEmpty()) {
            dataList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void setItems(List<T> list) {
        if (list != null) {
            dataList.clear();
            dataList.addAll(list);
            notifyDataSetChanged();
        }
    }

    protected List<T> getDataList() {
        return dataList;
    }

    public boolean isEmpty() {
        return dataList.isEmpty();
    }

    /**
     * 返回true表示删除成功, false表示失败
     */
    public boolean removeItem(T t) {
        boolean flag = t != null && dataList.remove(t);
        notifyDataSetChanged();
        return flag;
    }

    public void clear() {
        dataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder<T> holder, final int position) {
        final T item = getItem(position);
        holder.bindData(item, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder.itemView, position, item);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(holder.itemView, position, getItemId(position), item);
                }
                return false;
            }
        });
    }

    public interface OnItemLongClickListener<T> {
        boolean onItemLongClick(View view, int position, long id, T t);
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, int position, T t);
    }
}
