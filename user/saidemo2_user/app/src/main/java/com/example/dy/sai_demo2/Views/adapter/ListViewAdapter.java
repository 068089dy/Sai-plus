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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

//作者：zhangxiao
//链接：https://www.jianshu.com/p/2bc7edde983f
//來源：简书
//著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
public abstract class ListViewAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<T> mList;
    protected int mLayoutId;

    public ListViewAdapter(Context context, List<T> list, int layoutId) {
        mContext=context;
        mList=list;
        mLayoutId=layoutId;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 根据OO思想,第一行和最后一行不存在变化,所以封装起来,中间适配内容的部分通过convertView抽象方法交给调用者去实现
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewHolder holder = ListViewHolder.getHolder(mContext, mLayoutId, convertView, parent);
        convertView(holder,mList.get(position));
        return holder.getConvertView();
    }

    /**
     * 真正内容适配的方法
     * @param holder
     * @param t
     */
    public abstract void convertView(ListViewHolder holder,T t);
}
