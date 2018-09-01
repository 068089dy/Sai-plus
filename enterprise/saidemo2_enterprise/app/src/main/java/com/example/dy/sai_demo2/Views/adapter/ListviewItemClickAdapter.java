package com.example.dy.sai_demo2.Views.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.dy.sai_demo2.R;
import com.example.dy.sai_demo2.Views.AgreeItem;

import java.util.ArrayList;
import java.util.List;

public class ListviewItemClickAdapter extends BaseAdapter {
    private Context mContext;
    private List<AgreeItem> mList = new ArrayList<>();

    public ListviewItemClickAdapter(Context context, List<AgreeItem> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.agree_item, null);
            viewHolder.title_tx = (TextView) view.findViewById(R.id.title);
            viewHolder.agree_tx = (TextView) view.findViewById(R.id.agree_tx);
            viewHolder.date_tx = (TextView) view.findViewById(R.id.content);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.date_tx.setText(mList.get(i).getDate());

        if(mList.get(i).getIs_join().equals("1")){
            viewHolder.agree_tx.setTextColor(Color.BLUE);
            viewHolder.agree_tx.setText("同意");
            viewHolder.agree_tx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemDeleteListener.onDeleteClick(i);
                }
            });
        }else{
            viewHolder.agree_tx.setText("已通过");
        }


        viewHolder.title_tx.setText(mList.get(i).getTitle());
        return view;
    }

    /**
     * 删除按钮的监听接口
     */
    public interface onItemDeleteListener {
        void onDeleteClick(int i);
    }

    private onItemDeleteListener mOnItemDeleteListener;

    public void setOnItemDeleteClickListener(onItemDeleteListener mOnItemDeleteListener) {
        this.mOnItemDeleteListener = mOnItemDeleteListener;
    }

    class ViewHolder {
        TextView title_tx;
        TextView date_tx;
        TextView agree_tx;
    }

}


//    final MyAdapter adapter = new MyAdapter(MainActivity.this, mList);
//        listView.setAdapter(adapter);
//                //ListView item的点击事件
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//@Override
//public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        Toast.makeText(MainActivity.this, "Click item" + i, Toast.LENGTH_SHORT).show();
//        }
//        });
//        //ListView item 中的删除按钮的点击事件
//        adapter.setOnItemDeleteClickListener(new MyAdapter.onItemDeleteListener() {
//@Override
//public void onDeleteClick(int i) {
//        mList.remove(i);
//        adapter.notifyDataSetChanged();
//        }
//        });
