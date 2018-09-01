package com.example.dy.sai_demo2.UI;

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
import com.example.dy.sai_demo2.R;
import com.example.dy.sai_demo2.UI.company_ui.Activity_particulars;
import com.example.dy.sai_demo2.Views.CompanyItem;
import com.example.dy.sai_demo2.Views.LoopItem;
import com.example.dy.sai_demo2.Views.adapter.CompanyItemlistAdapter;
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
import static com.example.dy.sai_demo2.Common.Const.LINK_CIRCLE_LIST_LOAD;
import static com.example.dy.sai_demo2.Common.Const.LINK_game_list_load;
import static com.example.dy.sai_demo2.Common.Const.LINK_message_list_load;
import static com.example.dy.sai_demo2.Common.Const.SERVER_IP;

/**
 * Created by dy on 17-12-7.
 */

public class Fragment_company extends Fragment {
    View view;

    ListView company_lv;
    List<CompanyItem> company_list = new ArrayList<>();
    CompanyItemlistAdapter companyItemlistAdapter;
    ListViewAdapter adapter;
    private PtrFrameLayout refresh_layout;
    RequestQueue queue;
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.page_company_list,container,false);

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
        company_lv = (ListView) view.findViewById(R.id.company_lv);
        //companyItemlistAdapter = new CompanyItemlistAdapter(view.getContext(), company_list);
        adapter = new ListViewAdapter<CompanyItem>(view.getContext(),company_list,R.layout.page_company_list_item) {
            @Override
            public void convertView(ListViewHolder holder, CompanyItem o) {
                holder.setStr(R.id.date,o.getDate())
                        .setStr(R.id.title,o.getTitle())
                        .setStr(R.id.content,o.getContent());
            }
        };
        company_lv.setAdapter(adapter);
        company_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),Activity_particulars.class);
                intent.putExtra("message_id",company_list.get(position).getId());
                intent.putExtra("user_id",Storage.getString(getActivity(),KEY_USER_ID));
                startActivity(intent);
                //startActivity(new Intent(getContext(), Activity_particulars.class));
            }
        });
        refresh_layout = (PtrFrameLayout) view.findViewById(R.id.company_refresh);
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
        company_list.clear();
        String url = "http://"+SERVER_IP+LINK_message_list_load;

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("company_response", response);

                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = new JSONArray(object.optString("message_list"));
                    for (int i = 0;i<array.length();i++){
                        object = array.optJSONObject(i);
                        String id = object.optString("id");
                        String title = object.optString("title");
                        String content = object.optString("id");
                        String date = object.optString("create_time");
                        company_list.add(new CompanyItem(id, title, content, date));

                    }
                    adapter.notifyDataSetChanged();
                }catch (Exception e){
                    Log.d("json error",e.toString());
                    Toast.makeText(getContext(),"企业数据解析错误", Toast.LENGTH_LONG).show();
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
}
