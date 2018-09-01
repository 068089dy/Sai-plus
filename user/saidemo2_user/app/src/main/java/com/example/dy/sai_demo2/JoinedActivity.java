package com.example.dy.sai_demo2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dy.sai_demo2.UI.company_ui.Activity_particulars;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.dy.sai_demo2.Common.Const.LINK_game_list_load;
import static com.example.dy.sai_demo2.Common.Const.LINK_game_load_user;
import static com.example.dy.sai_demo2.Common.Const.LINK_join_game;
import static com.example.dy.sai_demo2.Common.Const.LINK_message_load_user;
import static com.example.dy.sai_demo2.Common.Const.SERVER_IP;

public class JoinedActivity extends AppCompatActivity {
    TextView title_tx;
    TextView content_tx;
    TextView create_time_tx;
    TextView start_time_tx;
    TextView end_time_tx;
    TextView enterprise_name_tx;
    TextView join_btn;
    ImageView back;
    RequestQueue queue;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined);
        //沉浸式，从郭霖那炒的，我也懒得去看。
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
        intent = getIntent();
        queue = Volley.newRequestQueue(this);
        title_tx = (TextView) findViewById(R.id.title);
        content_tx = (TextView) findViewById(R.id.content);
        create_time_tx = (TextView) findViewById(R.id.create_time);
        start_time_tx = (TextView) findViewById(R.id.start_time);
        end_time_tx = (TextView) findViewById(R.id.end_time);
        enterprise_name_tx = (TextView) findViewById(R.id.enterprise_name);
        join_btn = (TextView) findViewById(R.id.join);
        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join();
            }
        });
        back = (ImageView) findViewById(R.id.back_imgv);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initdata(){
        String url = "http://"+SERVER_IP+LINK_game_load_user+getIntent().getStringExtra("game_id");
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("particulars_response", response);

                try {
                    Log.d("joined_response",response);
                    JSONObject object = new JSONObject(response);
                    String title = object.optString("title");
                    String content = object.optString("content");
                    String create_time = object.optString("create_time");
                    String Start_time = object.getString("start_time");
                    String end_time = object.optString("end_time");
                    String is_join = object.optString("is_join");
                    if(is_join.equals("未参加")){
                        join_btn.setText("我要报名");
                    }else{
                        join_btn.setText("已报名");
                        join_btn.setEnabled(false);
                    }
                    object = object.optJSONObject("enterprise");
                    if(object == null){

                    }else {
                        String enterprise_name = object.optString("name");
                        enterprise_name_tx.setText(enterprise_name);
                    }
                    title_tx.setText(title);
                    content_tx.setText(content);
                    create_time_tx.setText(create_time);
                    start_time_tx.setText(Start_time);
                    end_time_tx.setText(end_time);

                }catch (Exception e){
                    Log.d("json error",e.toString());
                    Toast.makeText(JoinedActivity.this,"数据解析错误", Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(JoinedActivity.this,"error", Toast.LENGTH_SHORT).show();
            }

        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new HashMap<>();

                mHeaders.put("Cookie","user_id="+intent.getStringExtra("user_id"));
                //Log.d("send_head_cookie","user_id="+ Storage.getString(getActivity(),KEY_USER_ID));
                //Log.d("send_head_cookie","circle_id="+ Fragment_loop.LoopItem_id);
                return mHeaders;
            }
        };

        queue.add(request);
    }

    private void join(){
        String url = "http://"+SERVER_IP+LINK_join_game+getIntent().getStringExtra("game_id");
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("particu_join_response", response);
                if(response.equals("\"已加入\"")) {
                    join_btn.setText("已报名");
                    join_btn.setEnabled(false);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(JoinedActivity.this,"error", Toast.LENGTH_SHORT).show();
            }

        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new HashMap<>();

                mHeaders.put("Cookie","user_id="+intent.getStringExtra("user_id"));
                //Log.d("send_head_cookie","user_id="+ Storage.getString(getActivity(),KEY_USER_ID));
                //Log.d("send_head_cookie","circle_id="+ Fragment_loop.LoopItem_id);
                return mHeaders;
            }
        };

        queue.add(request);
    }
}
