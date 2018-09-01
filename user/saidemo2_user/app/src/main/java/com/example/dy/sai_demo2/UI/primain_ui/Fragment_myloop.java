package com.example.dy.sai_demo2.UI.primain_ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.dy.sai_demo2.R;
import com.example.dy.sai_demo2.Views.LoopItem;
import com.example.dy.sai_demo2.Views.adapter.ListViewAdapter;
import com.example.dy.sai_demo2.Views.adapter.ListViewHolder;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class Fragment_myloop extends Fragment {
    View view;
    List<LoopItem> list = new ArrayList<>();
    ListView listView;
    ListViewAdapter listViewAdapter;
    PtrFrameLayout refresh_layout;
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.page_primain_myloop,container,false);

        initview();
        initdata();
        return view;
    }
    //重写此方法，防止碎片覆盖
    @Override
    public void setMenuVisibility(boolean menuVisibile) {
        super.setMenuVisibility(menuVisibile);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisibile ? View.VISIBLE : View.GONE);
        }
    }

    private void initview(){
        listView = (ListView) view.findViewById(R.id.myloop_list_lv);
        listViewAdapter = new ListViewAdapter<LoopItem>(getContext(),list,R.layout.page_loop_item) {
            @Override
            public void convertView(ListViewHolder holder, LoopItem loopItem) {
                holder.setStr(R.id.title,loopItem.getTitle())
                        .setStr(R.id.content,loopItem.getContent());
            }
        };
        listView.setAdapter(listViewAdapter);
        refresh_layout = (PtrFrameLayout) view.findViewById(R.id.myloop_list_refresh);
        refresh_layout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run(){
                        initdata();
                        refresh_layout.refreshComplete();
                    }
                }, 100);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 默认实现，根据实际情况做改动
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
    }

    private void initdata(){
        list.clear();
        //list.add(new LoopItem())
        listViewAdapter.notifyDataSetChanged();
    }
}
