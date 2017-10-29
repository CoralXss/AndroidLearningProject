package com.xss.mobile.activity.dagger2;

import android.content.Context;
import android.view.ViewGroup;

/**
 * Created by xss on 2017/10/8.
 */

public class DaggerAdapter extends BaseRecyclerAdapter<MessageEntity> {

    public DaggerAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<MessageEntity> onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }
}
