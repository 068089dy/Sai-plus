package com.example.dy.sai_demo2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dy.sai_demo2.Views.LoopItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText edit_school;
    EditText edit_phone;
    EditText edit_num;
    EditText edit_password;
    EditText edit_email;
    Button submit_btn;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //沉浸式，从郭霖那炒的，我也懒得去看。
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
        edit_num = (EditText) findViewById(R.id.num);
        edit_password = (EditText) findViewById(R.id.password);
        edit_phone = (EditText) findViewById(R.id.phone_num);
        edit_school = (EditText) findViewById(R.id.shcool);
        edit_email = (EditText) findViewById(R.id.email);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        queue = Volley.newRequestQueue(this);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String num = edit_num.getText().toString();
                final String password = edit_password.getText().toString();
                final String phone = edit_phone.getText().toString();
                final String school = edit_school.getText().toString();
                final String email = edit_email.getText().toString();
                String url = "http://123.206.69.183/user_insert";
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
                                if(response.equals("\"注册成功\"")) {
                                    Toast.makeText(RegisterActivity.this, "success", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    //把返回数据存入Intent
                                    intent.putExtra("result", "success");
                                    intent.putExtra("email", edit_email.getText().toString());
                                    intent.putExtra("passwd", edit_password.getText().toString());
                                    //设置返回数据
                                    RegisterActivity.this.setResult(RESULT_OK, intent);
                                    //关闭Activity
                                    RegisterActivity.this.finish();
                                }
                                else{
                                    Toast.makeText(RegisterActivity.this, "failed",Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Toast.makeText(RegisterActivity.this, "failed",Toast.LENGTH_SHORT).show();

                                Log.d("Error.Response", error.toString());
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("password", password);
                        params.put("email", email);
                        params.put("school", school);
                        params.put("department", "lalla");
                        params.put("phone", phone);
                        params.put("student_id", num);
                        params.put("name", email);
                        params.put("other_name", email);
                        //params.put("code", "");

                        return params;
                    }
                };
                queue.add(postRequest);
            }
        });
    }
}
