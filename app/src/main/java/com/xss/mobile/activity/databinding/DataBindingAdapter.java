package com.xss.mobile.activity.databinding;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xss.mobile.BR;
import com.xss.mobile.databinding.AdapterDataBindingItemBinding;

import java.util.List;

/**
 * Created by xss on 2017/10/12.
 */

public class DataBindingAdapter extends RecyclerView.Adapter<DataBindingAdapter.MyViewHolder> {

    private List<UserBindEntity> list;

    private LayoutInflater inflater;

    public DataBindingAdapter(Context context, List<UserBindEntity> list) {
       this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 方式一
        AdapterDataBindingItemBinding itemBinding = AdapterDataBindingItemBinding.inflate(inflater, parent, false);
        // 方式二
//        AdapterDataBindingItemBinding itemBinding = DataBindingUtil.inflate(inflater, R.layout.adapter_data_binding_item, parent, false);

        MyViewHolder holder = new MyViewHolder(itemBinding.getRoot());
        holder.setAdapterDataBindingItemBinding(itemBinding);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final UserBindEntity item = list.get(position);
        holder.getBinding().setVariable(BR.item, item);
        holder.getBinding().executePendingBindings();

        holder.getBinding().tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean visible =  holder.getBinding().tvAge.getVisibility() == View.VISIBLE;
                holder.getBinding().tvAge.setVisibility(visible ? View.GONE : View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private AdapterDataBindingItemBinding adapterDataBindingItemBinding;

        public MyViewHolder(View itemView) {
            super(itemView);
        }

        public void setAdapterDataBindingItemBinding(AdapterDataBindingItemBinding db) {
            this.adapterDataBindingItemBinding = db;
        }

        public AdapterDataBindingItemBinding getBinding() {
            return adapterDataBindingItemBinding;
        }
    }
}
