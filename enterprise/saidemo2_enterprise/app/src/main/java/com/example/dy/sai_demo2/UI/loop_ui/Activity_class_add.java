package com.example.dy.sai_demo2.UI.loop_ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dy.sai_demo2.Common.Storage;
import com.example.dy.sai_demo2.MainActivity;
import com.example.dy.sai_demo2.R;
import com.example.dy.sai_demo2.RegisterActivity;
import com.example.dy.sai_demo2.UI.Fragment_loop;

import java.util.HashMap;
import java.util.Map;

import static com.example.dy.sai_demo2.Common.Const.KEY_USER_ID;
import static com.example.dy.sai_demo2.Common.Const.LINK_user_article_insert;
import static com.example.dy.sai_demo2.Common.Const.SERVER_IP;

public class Activity_class_add extends AppCompatActivity {
    ImageView back;
    EditText title_edit;
    EditText content_edit;
    RequestQueue queue;
    Button submit_btn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_class_add);
        initview();
    }
    private void initview(){
        queue = Volley.newRequestQueue(this);
        back = (ImageView) findViewById(R.id.back_imgv);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_edit = (EditText) findViewById(R.id.title);
        content_edit = (EditText) findViewById(R.id.content);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });
    }
    private void post(){
        String url = "http://"+SERVER_IP+LINK_user_article_insert;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("response", response);
                        if(response.equals("\"发布成功\"")){
                            Toast.makeText(Activity_class_add.this,"发布成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(Activity_class_add.this, "发布失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                        Toast.makeText(Activity_class_add.this,"发布error",Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("title", title_edit.getText().toString());
                params.put("content", content_edit.getText().toString());
                params.put("circle[]", Fragment_loop.LoopItem_id);
                //params.put("code", "");

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new HashMap<>();

                mHeaders.put("Cookie","enterprise_id="+getIntent().getStringExtra("user_id"));
                Log.d("send_head_cookie","user_id="+ getIntent().getStringExtra("user_id"));
                return mHeaders;
            }
        };
        queue.add(postRequest);
    }
}
