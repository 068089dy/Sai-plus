package com.example.dy.sai_demo2.UI;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dy.sai_demo2.Common.Storage;
import com.example.dy.sai_demo2.R;


import com.example.dy.sai_demo2.Views.LoopItem;
import com.example.dy.sai_demo2.Views.ResultItem;
import com.example.dy.sai_demo2.Views.adapter.ResultItemrecycleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import static com.example.dy.sai_demo2.Common.Const.KEY_USER_ID;
import static com.example.dy.sai_demo2.Common.Const.LINK_CIRCLE_LIST_LOAD;
import static com.example.dy.sai_demo2.Common.Const.SERVER_IP;

/**
 * Created by dy on 17-12-7.
 */

public class Fragment_search extends Fragment {
    View view;

    //顶栏的几项tablayout分类
    private String[] Items = {"赛事","新闻","用户","企业","圈子"};
    TabLayout mtablayout;
    RecyclerView recyclerView;
    ResultItemrecycleAdapter resultItemrecycleAdapter;
    RecyclerView.LayoutManager layoutManager;
    private PtrFrameLayout refresh_layout;

    EditText edit_search;
    //数据条目
    ArrayList<ResultItem> data = new ArrayList<>();
    String TabSelectItem = "search_event0";
    RequestQueue queue;
    Button search_btn;
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.page_search_result,container,false);

        initview();
        //initdata();
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

        edit_search = (EditText) view.findViewById(R.id.edit_search);
        resultItemrecycleAdapter = new ResultItemrecycleAdapter(data);
        recyclerView = (RecyclerView) view.findViewById(R.id.result_rcv);

        search_btn = (Button) view.findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initdata();
            }
        });

        recyclerView.setAdapter(resultItemrecycleAdapter);
        layoutManager = new LinearLayoutManager(view.getContext(), LinearLayout.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        mtablayout = (TabLayout) view.findViewById(R.id.search_result_tablayout);
        mtablayout.setTabMode(TabLayout.MODE_FIXED);
        for(int i = 0;i<Items.length;i++){
            mtablayout.addTab(mtablayout.newTab().setText(Items[i]));
        }
        mtablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        TabSelectItem = "search_event0";
                        initdata();
                        resultItemrecycleAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        TabSelectItem = "search_event1";
                        initdata();
                        resultItemrecycleAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        TabSelectItem = "search_user";
                        initdata();
                        resultItemrecycleAdapter.notifyDataSetChanged();
                        break;
                    case 3:
                        TabSelectItem = "search_enterprise";
                        initdata();
                        resultItemrecycleAdapter.notifyDataSetChanged();
                        break;
                    case 4:
                        TabSelectItem = "search_circle";
                        initdata();
                        resultItemrecycleAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        refresh_layout = (PtrFrameLayout) view.findViewById(R.id.result_refresh);
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
//        data.add(new ResultItem("df","sdfsdf"));
//        data.add(new ResultItem("df","sdfsdf"));
//        data.add(new ResultItem("df","sdfsdf"));
        if(edit_search.getText().toString().equals("")){
            Toast.makeText(getContext(),"亲输入",Toast.LENGTH_SHORT).show();
            return;
        }
        String url = "";
        if(TabSelectItem.equals("search_event0")){
            url = "http://"+SERVER_IP+"/search_event/"+edit_search.getText().toString()+"/0";
        }else if(TabSelectItem.equals("search_event1")){
            url = "http://"+SERVER_IP+"/search_event/"+edit_search.getText().toString()+"/1";
        }else {
            url = "http://"+SERVER_IP+"/"+TabSelectItem+"/"+edit_search.getText().toString();
        }

        Log.d("get_Url", url);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("search_response", response);
                if(response.equals("[]")){
                    Toast.makeText(getContext(),"未搜索到相关内容",Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    switch (TabSelectItem){
                        case "search_event0":
                            JSONObject object0 = new JSONObject(response);
                            JSONObject id_object = object0.optJSONObject("0");
                            JSONObject enter_object = object0.getJSONObject("enterprise");
                            data.add(new ResultItem(id_object.optString("title"),enter_object.optString("name")));
                            break;
                        case "search_event1":
                            JSONObject object1 = new JSONObject(response);
                            JSONObject id_object1 = object1.optJSONObject("0");
                            JSONObject enter_object1 = object1.getJSONObject("enterprise");
                            data.add(new ResultItem(id_object1.optString("title"),enter_object1.optString("name")));
                            break;
                        case "search_user":
                            JSONArray array2 = new JSONArray(response);
                            for(int i = 0;i<array2.length();i++){
                                JSONObject object2 = array2.optJSONObject(i);
                                data.add(new ResultItem(object2.optString("other_name"),object2.optString("id")));
                            }
                            break;
                        case "search_enterprise":
                            JSONArray array3 = new JSONArray(response);
                            for(int i = 0;i<array3.length();i++){
                                JSONObject object3 = array3.optJSONObject(i);
                                data.add(new ResultItem(object3.optString("name"),object3.optString("id")));
                            }
                            break;
                        case "search_circle":
                            JSONArray array4 = new JSONArray(response);
                            for(int i = 0;i<array4.length();i++){
                                JSONObject object4 = array4.optJSONObject(i);
                                data.add(new ResultItem(object4.optString("name"),object4.optString("id")));
                            }
                            break;
                    }
                    resultItemrecycleAdapter.notifyDataSetChanged();
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

                mHeaders.put("Cookie","enterprise_id="+ Storage.getString(getActivity(),KEY_USER_ID));
                Log.d("send_head_cookie","user_id="+ Storage.getString(getActivity(),KEY_USER_ID));
                return mHeaders;
            }
        };

        queue.add(request);
    }



}
