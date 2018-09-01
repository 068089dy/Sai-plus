package com.example.dy.sai_demo2.UI.company_ui;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dy.sai_demo2.R;
import com.example.dy.sai_demo2.UI.Fragment_loop;
import com.example.dy.sai_demo2.UI.loop_ui.Activity_class_add;

import java.util.HashMap;
import java.util.Map;

import static com.example.dy.sai_demo2.Common.Const.LINK_message_insert;
import static com.example.dy.sai_demo2.Common.Const.LINK_user_article_insert;
import static com.example.dy.sai_demo2.Common.Const.SERVER_IP;

public class Activity_recruite extends AppCompatActivity {
    String user_id;
    RequestQueue queue;
    EditText edit_work;
    EditText edit_population;
    EditText edit_require;
    Button submit_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruite);
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
        user_id = getIntent().getStringExtra("user_id");
        edit_population = (EditText) findViewById(R.id.edit_population);
        edit_require = (EditText) findViewById(R.id.edit_require);
        edit_work = (EditText) findViewById(R.id.edit_work);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_population.getText().toString().equals("")||
                        edit_work.getText().toString().equals("")||
                        edit_require.getText().toString().equals("")) {
                    Toast.makeText(Activity_recruite.this,"请检查输入信息",Toast.LENGTH_SHORT).show();
                }else {
                    post();
                }
            }
        });
    }

    private void post(){
        String url = "http://"+SERVER_IP+LINK_message_insert;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("response", response);
                        if(response.equals("\"发布成功\"")){
                            Toast.makeText(Activity_recruite.this,"发布成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(Activity_recruite.this, "发布失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                        Toast.makeText(Activity_recruite.this,"发布error",Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("title", edit_work.getText().toString());
                params.put("content", edit_require.getText().toString());
                params.put("url", edit_population.getText().toString());
                //params.put("circle[]", Fragment_loop.LoopItem_id);
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
