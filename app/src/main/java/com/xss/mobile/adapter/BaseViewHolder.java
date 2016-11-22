package com.xss.mobile.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Desc：
 * Author: xss
 * Time：2016/2/16 15:07
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    protected <T extends View> T $(int viewID) {
        return (T) itemView.findViewById(viewID);
    }
}
