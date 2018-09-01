package com.example.dy.sai_demo2;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.example.dy.sai_demo2.UI.company_ui.Activity_recruite;

import java.util.HashMap;
import java.util.Map;

import static com.example.dy.sai_demo2.Common.Const.LINK_game_insert;
import static com.example.dy.sai_demo2.Common.Const.LINK_message_insert;
import static com.example.dy.sai_demo2.Common.Const.SERVER_IP;

public class PushmatchActivity extends AppCompatActivity {
    ImageView back;
    EditText edit_start_time;
    EditText edit_end_time;
    EditText edit_content;
    EditText edit_title;
    EditText edit_url;
    Button submit_btn;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_push_match);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        initview();
    }

    private void initview(){
        queue = Volley.newRequestQueue(this);
        edit_content = (EditText) findViewById(R.id.edit_content);
        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_start_time = (EditText) findViewById(R.id.edit_start_time);
        edit_end_time = (EditText) findViewById(R.id.edit_end_time);
        edit_url = (EditText) findViewById(R.id.edit_url);
        back = (ImageView) findViewById(R.id.back_imgv);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        submit_btn = (Button) findViewById(R.id.btn_submit);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_start_time.getText().toString().equals("")||
                        edit_end_time.getText().toString().equals("")||
                        edit_title.getText().toString().equals("")||
                        edit_content.getText().toString().equals("")){
                    Toast.makeText(PushmatchActivity.this,"请检查输入信息",Toast.LENGTH_SHORT).show();
                }else{
                    post();
                }

            }
        });

    }

    private void post(){
        String url = "http://"+SERVER_IP+LINK_game_insert;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("response", response);
                        if(response.equals("\"发布成功\"")){
                            Toast.makeText(PushmatchActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(PushmatchActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                        Toast.makeText(PushmatchActivity.this,"发布error",Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("title", edit_title.getText().toString());
                params.put("content", edit_content.getText().toString());
                params.put("url", edit_url.getText().toString());
                params.put("end_time", edit_end_time.getText().toString()+" 11:11:11");
                params.put("start_time", edit_start_time.getText().toString()+" 11:11:11");
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
