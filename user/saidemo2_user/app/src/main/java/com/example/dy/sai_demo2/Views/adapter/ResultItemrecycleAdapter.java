package com.example.dy.sai_demo2.Views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dy.sai_demo2.R;
import com.example.dy.sai_demo2.Views.ResultItem;

import java.util.ArrayList;

/**
 * Created by dy on 18-3-19.
 */


public class ResultItemrecycleAdapter extends RecyclerView.Adapter<ResultItemrecycleAdapter.ViewHolder>{

    private ArrayList<ResultItem> mData;

    public ResultItemrecycleAdapter(ArrayList<ResultItem> data) {
        this.mData = data;
    }

    public void updateData(ArrayList<ResultItem> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ResultItemrecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_search_result_item, parent, false);
        // 实例化viewholder
        ResultItemrecycleAdapter.ViewHolder viewHolder = new ResultItemrecycleAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ResultItemrecycleAdapter.ViewHolder holder, int position) {
        // 绑定数据
        //holder.user_tv.setText(mData.get(position).getUser());

        holder.title_tv.setText(mData.get(position).getTitle());
        holder.content_tv.setText(mData.get(position).getContent());
        //holder.date_tv.setText(mData.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title_tv;
        public TextView content_tv;

        public ViewHolder(View itemView) {
            super(itemView);

            title_tv = (TextView) itemView.findViewById(R.id.title);
            content_tv = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
