package com.example.dy.sai_demo2.Views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dy.sai_demo2.R;
import com.example.dy.sai_demo2.Views.LoopItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dy on 18-3-13.
 */

public class LoopItemlistAdapter extends BaseAdapter{
    Context context;
    List<LoopItem> list = new ArrayList<>();

    public LoopItemlistAdapter(Context context, List<LoopItem> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public LoopItem getItem(int position){
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder = new ViewHolder();

        LoopItem loopItem = getItem(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.page_loop_item, parent, false);

        viewHolder.content_tv = (TextView) convertView.findViewById(R.id.content);
        viewHolder.content_tv.setText(loopItem.getContent());
        viewHolder.title_tv = (TextView) convertView.findViewById(R.id.title);
        viewHolder.title_tv.setText(loopItem.getTitle());

        return convertView;
    }
    class ViewHolder{
        public TextView title_tv;
        public TextView content_tv;
    }
}
