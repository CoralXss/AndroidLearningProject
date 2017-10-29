package com.xss.mobile.activity.scrollconflict;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xss.mobile.R;

import java.util.List;

/**
 * Created by xss on 2017/5/4.
 */

public class DataListAdapter extends BaseAdapter {
    private String TAG = DataListAdapter.class.getSimpleName();

    private Context context;
    private LayoutInflater inflater;
    private List<String> list;

    public DataListAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        return getViewWithoutReuse(position);

        return getViewWithReuse(convertView, position);
    }

    private View getViewWithoutReuse(int position) {
        View view = inflater.inflate(R.layout.item_view_label, null);

        ViewHolder holder = new ViewHolder(view);
        holder.tv.setText(getItem(position));

        return view;
    }

    private View getViewWithReuse(View convertView, int position) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_view_label, null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);

            Log.e(TAG, "item " + position + " is new");

        } else {
            holder = (ViewHolder) convertView.getTag();

            Log.e(TAG, "item " + position + " is reused");
        }

        holder.tv.setText(getItem(position));

        return convertView;
    }


    class ViewHolder {

        public TextView tv;

        public ViewHolder(View view) {
            tv = (TextView) view.findViewById(R.id.tv_label);
        }
    }
}
