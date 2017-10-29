package com.xss.mobile.activity.dagger2;

import android.app.Activity;
import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by liumeng on 16/1/3.
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    protected Activity activity;

    public BaseViewHolder(View itemView) {
        super(itemView);
        activity = (Activity) itemView.getContext();
    }

    public BaseViewHolder(ViewGroup parent, int layoutId) {
        this(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
    }

    public abstract void bindData(T data, int position);

    protected Activity getContext() {
        return activity;
    }

    protected Resources getResource() {
        return activity.getResources();
    }

    protected String getString(@StringRes int id, Object... formatArgs) {
        return getResource().getString(id, formatArgs);
    }
}
