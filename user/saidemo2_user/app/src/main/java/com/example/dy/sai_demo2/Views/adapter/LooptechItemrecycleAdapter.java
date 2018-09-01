package com.example.dy.sai_demo2.Views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dy.sai_demo2.R;
import com.example.dy.sai_demo2.Views.LoopItem;
import com.example.dy.sai_demo2.Views.LooptechItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dy on 18-3-19.
 */

public class LooptechItemrecycleAdapter extends RecyclerView.Adapter<LooptechItemrecycleAdapter.ViewHolder> implements View.OnClickListener{

    private ArrayList<LooptechItem> mData;
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , int position);
    }
    //private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private interface  OnRecyclerViewItemLongClickListener{}
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private OnRecyclerViewItemLongClickListener mOnItemLongClickListener = null;


    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener = listener;
    }
    public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener listener){
        this.mOnItemLongClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup,  int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.page_loop_class_tech_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return vh;
    }

    public LooptechItemrecycleAdapter(ArrayList<LooptechItem> data) {
        this.mData = data;
    }

    public void updateData(ArrayList<LooptechItem> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        // 实例化展示的view
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_loop_class_tech_item, parent, false);
//        // 实例化viewholder
//        ViewHolder viewHolder = new ViewHolder(v);
//        return viewHolder;
//    }
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 绑定数据
        holder.user_tv.setText(mData.get(position).getUser());

        holder.title_tv.setText(mData.get(position).getTitle());
        holder.content_tv.setText(mData.get(position).getContent());
        //holder.date_tv.setText(mData.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
//    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
//        this.mOnItemClickListener = listener;
//    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView user_tv;
        public TextView title_tv;
        public TextView content_tv;
        public TextView date_tv;
        OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
        public ViewHolder(View itemView) {
            super(itemView);
            user_tv = (TextView) itemView.findViewById(R.id.user);
            date_tv = (TextView) itemView.findViewById(R.id.date);
            title_tv = (TextView) itemView.findViewById(R.id.title);
            content_tv = (TextView) itemView.findViewById(R.id.content);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onRecyclerViewItemClickListener.
//                }
//            });
        }

//        @Override
//        public void onClick(View v) {
//            if (onRecyclerViewItemClickListener != null){
//
//            }
//        }
    }
}

