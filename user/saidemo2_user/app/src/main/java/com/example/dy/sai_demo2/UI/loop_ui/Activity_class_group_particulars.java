package com.example.dy.sai_demo2.UI.loop_ui;

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
import com.example.dy.sai_demo2.R;
import com.example.dy.sai_demo2.UI.Fragment_loop;
import com.example.dy.sai_demo2.Views.LooptechItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.dy.sai_demo2.Common.Const.KEY_USER_ID;
import static com.example.dy.sai_demo2.Common.Const.LINK_ARTICLE_LIST_LOAD;
import static com.example.dy.sai_demo2.Common.Const.LINK_article_load;
import static com.example.dy.sai_demo2.Common.Const.SERVER_IP;

public class Activity_class_group_particulars extends AppCompatActivity {
    TextView title_tx;
    TextView content_tx;
    TextView date_tx;
    TextView author_tx;
    RequestQueue queue;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_class_group_particulars);
        initview();
        Intent intent = getIntent();
        String article_id = intent.getStringExtra("article_id");
        get_article_data(article_id);
    }
    private void initview(){
        back = (ImageView) findViewById(R.id.back_imgv);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        queue = Volley.newRequestQueue(this);
        title_tx = (TextView) findViewById(R.id.title);
        content_tx = (TextView) findViewById(R.id.content);
        date_tx = (TextView) findViewById(R.id.date);
        author_tx = (TextView) findViewById(R.id.author);
    }

    private void get_article_data(String article_id){

        //String url = "http://"+SERVER_IP+LINK_article_load+article_id;
        String url = getIntent().getStringExtra("url");
        final StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("class_tech_response", response);

                try {
                    JSONObject object = new JSONObject(response);
                    String title = object.optString("name");
                    String content = object.optString("describe");
                    String Create_time = object.optString("create_time");
                    object = object.optJSONObject("create_user");
                    String other_name = object.optString("other_name");
                    title_tx.setText(title);
                    content_tx.setText(content);
                    date_tx.setText(Create_time);
                    author_tx.setText(other_name);
                }catch (Exception e){
                    Log.d("json error",e.toString());
                    //Toast.makeText(Activity_class_group_particulars.this,"数据解析错误", Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Activity_class_group_particulars.this,"error", Toast.LENGTH_SHORT).show();
            }

        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new HashMap<>();

                mHeaders.put("Cookie","user_id="+ Storage.getString(Activity_class_group_particulars.this,KEY_USER_ID));
                Log.d("send_head_cookie","user_id="+ Storage.getString(Activity_class_group_particulars.this,KEY_USER_ID));
                //Log.d("send_head_cookie","circle_id="+ Fragment_loop.LoopItem_id);
                return mHeaders;
            }
        };

        queue.add(request);
    }
}
