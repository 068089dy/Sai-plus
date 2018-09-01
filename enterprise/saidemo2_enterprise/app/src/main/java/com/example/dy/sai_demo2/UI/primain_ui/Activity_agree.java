package com.example.dy.sai_demo2.UI.primain_ui;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dy.sai_demo2.Common.Storage;
import com.example.dy.sai_demo2.PushmatchActivity;
import com.example.dy.sai_demo2.R;
import com.example.dy.sai_demo2.Views.AgreeItem;
import com.example.dy.sai_demo2.Views.MyJoinedItem;
import com.example.dy.sai_demo2.Views.adapter.ListviewItemClickAdapter;

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
import static com.example.dy.sai_demo2.Common.Const.LINK_game_user_list;
import static com.example.dy.sai_demo2.Common.Const.LINK_message_user_list;
import static com.example.dy.sai_demo2.Common.Const.LINK_my_game_list_enterprise;
import static com.example.dy.sai_demo2.Common.Const.LINK_pass_user_message;
import static com.example.dy.sai_demo2.Common.Const.SERVER_IP;

public class Activity_agree extends AppCompatActivity {
    ListView listView;
    ListviewItemClickAdapter adapter;
    List<AgreeItem> list = new ArrayList<>();
    RequestQueue queue;
    private PtrFrameLayout refresh_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initview();
        initdata();
    }
    private void initview(){
        queue = Volley.newRequestQueue(Activity_agree.this);
        listView = (ListView) findViewById(R.id.listview);
        adapter = new ListviewItemClickAdapter(this,list);
        listView.setAdapter(adapter);
        adapter.setOnItemDeleteClickListener(new ListviewItemClickAdapter.onItemDeleteListener() {
            @Override
            public void onDeleteClick(int i) {
                if(getIntent().getIntExtra("type",0) == 1) {
                    pass_user_msg(list.get(i).getId(), getIntent().getStringExtra("event_id"));
                }
                //Toast.makeText(Activity_agree.this,list.get(i).getTitle(),Toast.LENGTH_SHORT).show();
            }
        });
        refresh_layout = (PtrFrameLayout) findViewById(R.id.agree_refresh);
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

        String url = "";
        if(getIntent().getIntExtra("type",0) == 0){
            url = "http://"+SERVER_IP+LINK_game_user_list+getIntent().getStringExtra("game_id");
        }else if(getIntent().getIntExtra("type",0)==1){
            url = "http://"+SERVER_IP+LINK_message_user_list+getIntent().getStringExtra("event_id");
        }else{
            return;
        }
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.d("msg_user_list",response);
                    JSONArray array = new JSONArray(response);
                    for(int i = 0;i<array.length();i++){
                        JSONObject object = array.optJSONObject(i);
                        String name = object.optString("name");
                        String id = object.optString("id");
                        object = object.optJSONObject("pivot");
                        String state = object.optString("state");
                        list.add(new AgreeItem(id,name,id,state));
                        adapter.notifyDataSetChanged();
                    }

                }catch (Exception e){
                    Log.d("json error",e.toString());
                    Toast.makeText(Activity_agree.this,"数据解析错误", Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Activity_agree.this,"error", Toast.LENGTH_SHORT).show();
            }

        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new HashMap<>();

                mHeaders.put("Cookie","enterprise_id="+ getIntent().getStringExtra("user_id"));
                //Log.d("send_head_cookie","user_id="+ Storage.getString(getActivity(),KEY_USER_ID));
                //Log.d("send_head_cookie","circle_id="+ Fragment_loop.LoopItem_id);
                return mHeaders;
            }
        };

        queue.add(request);
        //adapter.notifyDataSetChanged();
    }


    private void pass_user_msg(final String user_id, final String event_id){
        String url = "http://"+SERVER_IP+LINK_pass_user_message;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("response", response);
                        if(response.equals("\"已通过\"")){
                            Toast.makeText(Activity_agree.this,"\"已通过\"",Toast.LENGTH_SHORT).show();
                            initdata();
                            //finish();
                        }else {
                            Toast.makeText(Activity_agree.this, "\"未通过\"", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                        Toast.makeText(Activity_agree.this,"发布error",Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("user_id",user_id);
                params.put("event_id",event_id);
                //params.put("circle[]", Fragment_loop.LoopItem_id);
                //params.put("code", "");
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new HashMap<>();

                mHeaders.put("Cookie","enterprise_id="+getIntent().getStringExtra("user_id"));
                Log.d("send_head_cookie","enterprise_id="+ getIntent().getStringExtra("user_id"));
                return mHeaders;
            }
        };
        queue.add(postRequest);
    }
}
