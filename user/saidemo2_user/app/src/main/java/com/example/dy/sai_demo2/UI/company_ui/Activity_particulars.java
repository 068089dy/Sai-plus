package com.example.dy.sai_demo2.UI.company_ui;

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
import com.example.dy.sai_demo2.Common.Storage;
import com.example.dy.sai_demo2.JoinedActivity;
import com.example.dy.sai_demo2.R;
import com.example.dy.sai_demo2.UI.Fragment_company;
import com.example.dy.sai_demo2.UI.Fragment_loop;
import com.example.dy.sai_demo2.Views.LooptechItem;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import static com.example.dy.sai_demo2.Common.Const.KEY_USER_ID;
import static com.example.dy.sai_demo2.Common.Const.LINK_join_game;
import static com.example.dy.sai_demo2.Common.Const.LINK_join_message;
import static com.example.dy.sai_demo2.Common.Const.LINK_message_list_load;
import static com.example.dy.sai_demo2.Common.Const.LINK_message_load_user;
import static com.example.dy.sai_demo2.Common.Const.SERVER_IP;

public class Activity_particulars extends AppCompatActivity {
    ImageView back_img;
    RequestQueue queue;
    TextView title_tx;
    TextView content_tx;
    TextView author_tx;
    TextView date_tx;
    TextView join_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式，从郭霖那炒的，我也懒得去看。
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_particulars);
        initview();
        initdata();
    }
    private void initview(){
        queue = Volley.newRequestQueue(this);
        title_tx = (TextView) findViewById(R.id.title);
        content_tx = (TextView) findViewById(R.id.content);
        date_tx = (TextView) findViewById(R.id.date);
        author_tx = (TextView) findViewById(R.id.author);
        join_btn = (TextView) findViewById(R.id.join);
        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join();
            }
        });
        back_img = (ImageView) findViewById(R.id.back_imgv);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initdata(){

        String url = "http://"+SERVER_IP+LINK_message_load_user+getIntent().getStringExtra("message_id");
        Log.d("url",url);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("particulars_response", response);

                try {
                    JSONObject object = new JSONObject(response);
                    String title = object.optString("title");
                    String content = object.optString("content");
                    String date = object.optString("create_time");
                    String is_join = object.optString("is_join");
                    if(is_join.equals("未参加")){
                        join_btn.setText("我要参加");
                    }else{
                        join_btn.setText("已参加");
                        join_btn.setEnabled(false);
                    }
                    object = new JSONObject(object.optString("enterprise"));
                    String name = object.optString("name");
                    title_tx.setText(title);
                    author_tx.setText(name);
                    content_tx.setText(content);
                    date_tx.setText(date);
                }catch (Exception e){
                    Log.d("json error",e.toString());
                    Toast.makeText(Activity_particulars.this,"数据解析错误", Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Activity_particulars.this,"error", Toast.LENGTH_SHORT).show();
            }

        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new HashMap<>();

                mHeaders.put("Cookie","user_id="+getIntent().getStringExtra("user_id"));
                //Log.d("send_head_cookie","user_id="+ Storage.getString(getActivity(),KEY_USER_ID));
                //Log.d("send_head_cookie","circle_id="+ Fragment_loop.LoopItem_id);
                return mHeaders;
            }
        };

        queue.add(request);
    }

    private void join(){
        String url = "http://"+SERVER_IP+LINK_join_message+getIntent().getStringExtra("message_id");
        Log.d("url",url);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("particu_join_response", response);
                if(response.equals("\"已加入\"")) {
                    join_btn.setText("已加入");
                    join_btn.setEnabled(false);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Activity_particulars.this,"error", Toast.LENGTH_SHORT).show();
            }

        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new HashMap<>();

                mHeaders.put("Cookie","user_id="+getIntent().getStringExtra("user_id"));
                //Log.d("send_head_cookie","user_id="+ Storage.getString(getActivity(),KEY_USER_ID));
                //Log.d("send_head_cookie","circle_id="+ Fragment_loop.LoopItem_id);
                return mHeaders;
            }
        };

        queue.add(request);
    }

}
