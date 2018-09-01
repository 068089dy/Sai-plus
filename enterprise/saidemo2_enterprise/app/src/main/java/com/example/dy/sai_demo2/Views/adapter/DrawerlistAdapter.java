package com.example.dy.sai_demo2.Views.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dy.sai_demo2.R;
import com.example.dy.sai_demo2.Views.Drawer;
import com.example.dy.sai_demo2.Views.Trace;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dy on 17-12-5.
 */

public class DrawerlistAdapter extends BaseAdapter {
    private Context context;
    private List<Drawer> List = new ArrayList<>(1);
    public DrawerlistAdapter(Context context, List<Drawer> List) {
        this.context = context;
        this.List = List;
    }
    @Override
    public int getCount() {
        return List.size();
    }

    @Override
    public Drawer getItem(int position) {
        return List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DViewHolder holder = new DViewHolder();
        final Drawer drawer = getItem(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.drawer_items, parent, false);

        holder.content = (TextView) convertView.findViewById(R.id.content);
        holder.image = (ImageView) convertView.findViewById(R.id.image);
        holder.msgnum = (TextView) convertView.findViewById(R.id.msgnum);
        holder.image.setImageResource(drawer.getImageId());
        if(drawer.getMsgNum()>0) {
            Drawable image = ContextCompat.getDrawable(convertView.getContext(),R.drawable.msgnum_bg);
            holder.msgnum.setBackground(image);
            holder.msgnum.setText(drawer.getMsgNum() + "");
        }
        holder.content.setText(drawer.getName());
        return convertView;
    }
    static class DViewHolder {
        public ImageView image;
        public TextView content;
        public TextView msgnum;
    }
}
