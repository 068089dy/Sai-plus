package com.example.dy.sai_demo2.UI;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.example.dy.sai_demo2.UI.loop_ui.Fragment_class;

import com.example.dy.sai_demo2.Views.LoopItem;
import com.example.dy.sai_demo2.Views.Trace;
import com.example.dy.sai_demo2.Views.adapter.LoopItemlistAdapter;

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
import static com.example.dy.sai_demo2.Common.Const.LINK_CIRCLE_LIST_LOAD;
import static com.example.dy.sai_demo2.Common.Const.SERVER_IP;

/**
 * Created by dy on 17-12-2.
 */

public class Fragment_loop extends Fragment {
    View view;

    ListView loop_lv;
    private List<LoopItem> loop_item_list = new ArrayList<>();
    private LoopItemlistAdapter loopItemlistAdapter;
    private PtrFrameLayout refresh_layout;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    public static String LoopItem_id = "";
    RequestQueue queue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.page_loop,container,false);

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
        loop_lv = (ListView) view.findViewById(R.id.loop_list_lv);
        loopItemlistAdapter = new LoopItemlistAdapter(view.getContext(), loop_item_list);
        loop_lv.setAdapter(loopItemlistAdapter);
        loop_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(view.getContext(),loop_item_list.get(position).getTitle(),Toast.LENGTH_LONG).show();
                //title_name = loop_item_list.get(position).getTitle();
                LoopItem_id = loop_item_list.get(position).getId();
                replacefragment(new Fragment_class());
            }
        });
        refresh_layout = (PtrFrameLayout) view.findViewById(R.id.loop_list_refresh);
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
        loop_item_list.clear();
        String url = "http://"+SERVER_IP+LINK_CIRCLE_LIST_LOAD;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("loop_response", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        loop_item_list.add(new LoopItem(jsonObject.optString("id"),
                                jsonObject.optString("create_time"),
                                jsonObject.optString("name"),
                                jsonObject.optString("describe"),
                                jsonObject.optString("image"),
                                jsonObject.optString("is_json")
                        ));
                        loopItemlistAdapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
                    Log.d("json error",e.toString());
                    //Toast.makeText(getContext(),"数据解析错误", Toast.LENGTH_LONG).show();
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

                mHeaders.put("Cookie","user_id="+ Storage.getString(getActivity(),KEY_USER_ID));
                Log.d("send_head_cookie","user_id="+ Storage.getString(getActivity(),KEY_USER_ID));
                return mHeaders;
            }
        };

        queue.add(request);
    }
    private void replacefragment(Fragment fragment){
        this.setUserVisibleHint(false);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.loop_layout,fragment);
        transaction.commit();

    }
}

