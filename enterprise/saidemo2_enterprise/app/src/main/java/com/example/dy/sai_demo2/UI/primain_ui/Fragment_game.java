package com.example.dy.sai_demo2.UI.primain_ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.dy.sai_demo2.JoinedActivity;
import com.example.dy.sai_demo2.R;
import com.example.dy.sai_demo2.Views.MyJoinedItem;
import com.example.dy.sai_demo2.Views.adapter.ListViewAdapter;
import com.example.dy.sai_demo2.Views.adapter.ListViewHolder;

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
import static com.example.dy.sai_demo2.Common.Const.LINK_my_event_list_user;
import static com.example.dy.sai_demo2.Common.Const.LINK_my_game_list_enterprise;
import static com.example.dy.sai_demo2.Common.Const.SERVER_IP;

public class Fragment_game extends Fragment {
    View view;
    List<MyJoinedItem> list = new ArrayList<>();
    ListView listView;
    ListViewAdapter adapter;
    RequestQueue queue;
    private PtrFrameLayout refresh_layout;
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.page_primain_joined, container, false);
        }
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
    @Override
    public void onDestroyView(){
        super.onDestroyView();

        //移除mRootView，防止添加多个View
        if(null != view){
            ((ViewGroup)view.getParent()).removeView(view);
        }
    }

    private void initview(){
        queue = Volley.newRequestQueue(getContext());
        listView = (ListView) view.findViewById(R.id.myjoined_list_lv);
        listView.setAdapter(adapter = new ListViewAdapter<MyJoinedItem>(getContext(), list,R.layout.page_primain_joined_item) {
            @Override
            public void convertView(ListViewHolder holder, MyJoinedItem o) {
                holder.setStr(R.id.title, o.getTitle())
                        .setStr(R.id.content, o.getContent())
                        .setStr(R.id.date, o.getDate());
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(list.get(position).getType().equals("0")){
                    Intent intent = new Intent(getActivity(), Activity_agree.class);
                    intent.putExtra("type",0);
                    intent.putExtra("game_id",list.get(position).getId());
                    intent.putExtra("user_id", Storage.getString(getActivity(),KEY_USER_ID));
                    startActivity(intent);
                }
            }
        });
        refresh_layout = (PtrFrameLayout) view.findViewById(R.id.myjoined_list_refresh);
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
        String url = "http://"+SERVER_IP+LINK_my_game_list_enterprise;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("particulars_response", response);

                try {
                    Log.d("my_joined_response",response);
                    JSONArray array = new JSONArray(response);
                    for(int i = 0;i<array.length();i++){
                        JSONObject object = array.optJSONObject(i);
                        String title = object.optString("title");
                        //String type = object.optString("type");
                        String id = object.optString("id");
                        String[] create_time = object.optString("create_time").split(" ")[0].split("-");
                        String date = create_time[1]+"-"+create_time[2];
//                        if(type.equals("0")){
//                            list.add(new MyJoinedItem(id, type, date, "比赛："+title, title));
//                        }else if(type.equals("1")){
//                            list.add(new MyJoinedItem(id, type, date, "招聘："+title, title));
//                        }
                        list.add(new MyJoinedItem(id, "0", date, "比赛："+title, title));
                        adapter.notifyDataSetChanged();
                    }

                }catch (Exception e){
                    Log.d("json error",e.toString());
                    Toast.makeText(getActivity(),"数据解析错误", Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"error", Toast.LENGTH_SHORT).show();
            }

        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new HashMap<>();

                mHeaders.put("Cookie","enterprise_id="+ Storage.getString(getContext(),KEY_USER_ID));
                //Log.d("send_head_cookie","user_id="+ Storage.getString(getActivity(),KEY_USER_ID));
                //Log.d("send_head_cookie","circle_id="+ Fragment_loop.LoopItem_id);
                return mHeaders;
            }
        };

        queue.add(request);
        //adapter.notifyDataSetChanged();
    }
}

