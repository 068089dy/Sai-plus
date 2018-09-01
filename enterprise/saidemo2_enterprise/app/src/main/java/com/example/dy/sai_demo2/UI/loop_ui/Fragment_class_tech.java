package com.example.dy.sai_demo2.UI.loop_ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dy.sai_demo2.Common.Storage;
import com.example.dy.sai_demo2.R;
import com.example.dy.sai_demo2.UI.Fragment_loop;
import com.example.dy.sai_demo2.Views.LoopItem;
import com.example.dy.sai_demo2.Views.LooptechItem;
import com.example.dy.sai_demo2.Views.adapter.ListViewAdapter;
import com.example.dy.sai_demo2.Views.adapter.ListViewHolder;
import com.example.dy.sai_demo2.Views.adapter.LooptechItemrecycleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import static com.example.dy.sai_demo2.Common.Const.KEY_USER_ID;
import static com.example.dy.sai_demo2.Common.Const.LINK_ARTICLE_LIST_LOAD;
import static com.example.dy.sai_demo2.Common.Const.LINK_CIRCLE_LIST_LOAD;
import static com.example.dy.sai_demo2.Common.Const.SERVER_IP;

/**
 * Created by dy on 18-3-17.
 */

public class Fragment_class_tech extends Fragment {
    View view;

    ListViewAdapter adapter;
    ListView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<LooptechItem> data = new ArrayList<>();
    private PtrFrameLayout refresh_layout;

    RequestQueue queue;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.page_loop_class_tech,container,false);

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
        queue = Volley.newRequestQueue(getContext());
        recyclerView = (ListView) view.findViewById(R.id.loop_rcv_t);
        data = new ArrayList<LooptechItem>();
        mLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setAdapter(adapter = new ListViewAdapter<LooptechItem>(getContext(),data,R.layout.page_loop_class_tech_item) {
            @Override
            public void convertView(ListViewHolder holder, LooptechItem o) {
                holder.setStr(R.id.user,o.getUser())
                        .setStr(R.id.content, o.getContent())
                        .setStr(R.id.date, o.getDate())
                        .setStr(R.id.title, o.getTitle());

            }
        });
        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("article_id",data.get(position).getId());
                Intent intent = new Intent(getActivity(), Activity_class_group_particulars.class);
                intent.putExtra("article_id",data.get(position).getId());
                startActivity(intent);
            }
        });





        refresh_layout = (PtrFrameLayout) view.findViewById(R.id.class_tech_refresh);
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
        data.clear();
        String url = "http://"+SERVER_IP+LINK_ARTICLE_LIST_LOAD+Fragment_loop.LoopItem_id;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("class_tech_response", response);

                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = new JSONArray(object.optString("articles"));
                    for(int i = 0; i<array.length(); i++){
                        object = array.optJSONObject(i);
                        String id = object.optString("id");
                        String title = object.optString("title");
                        String create_time = object.optString("create_time");

                        JSONObject user_object = object.optJSONObject("user");
                        String username = "";
                        if(user_object!=null) {
                            username = user_object.optString("other_name");
                        }
                        JSONObject pivot_object = object.optJSONObject("pivot");
                        String article_id = pivot_object.optString("article_id");
                        data.add(new LooptechItem(article_id, username, create_time, title,"lalalala"));
                        adapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
                    Log.d("json error",e.toString());
                    Toast.makeText(getContext(),"数据解析错误", Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"error", Toast.LENGTH_SHORT).show();
            }

        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new HashMap<>();

                mHeaders.put("Cookie","enterprise_id="+ Storage.getString(getActivity(),KEY_USER_ID +
                    ";circle_id="+Fragment_loop.LoopItem_id));
                Log.d("send_head_cookie","user_id="+ Storage.getString(getActivity(),KEY_USER_ID));
                Log.d("send_head_cookie","circle_id="+ Fragment_loop.LoopItem_id);
                return mHeaders;
            }
        };

        queue.add(request);
    }





}