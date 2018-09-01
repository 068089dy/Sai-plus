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
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dy.sai_demo2.Common.Storage;


import java.util.HashMap;
import java.util.Map;

import static com.example.dy.sai_demo2.Common.Const.KEY_EMAIL;
import static com.example.dy.sai_demo2.Common.Const.KEY_PASSWD;
import static com.example.dy.sai_demo2.Common.Const.KEY_USERNAME;
import static com.example.dy.sai_demo2.Common.Const.KEY_USER_ID;
import static com.example.dy.sai_demo2.Common.Const.LINK_USER_LOGIN;
import static com.example.dy.sai_demo2.Common.Const.SERVER_IP;

public class LoginActivity extends AppCompatActivity {
    EditText edit_email;
    EditText edit_password;
    RequestQueue queue;
    Button submit_btn;
    Button Register_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        edit_email = (EditText) findViewById(R.id.E_mail);
        edit_password = (EditText) findViewById(R.id.password);
        queue = Volley.newRequestQueue(this);
        submit_btn = (Button) findViewById(R.id.login_btn);
        Register_btn = (Button) findViewById(R.id.btn_register);
        Register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent,1);
            }
        });
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String password = edit_password.getText().toString();

                final String email = edit_email.getText().toString();
                String url = "http://"+SERVER_IP+LINK_USER_LOGIN;
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);

                                if(response.equals("\"登录成功\"")) {
                                    Storage.putString(MainActivity.th, KEY_EMAIL, email);
                                    Storage.putString(MainActivity.th, KEY_PASSWD, password);
                                    Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_SHORT).show();
                                    //数据是使用Intent返回
                                    Intent intent = new Intent();
                                    //把返回数据存入Intent
                                    intent.putExtra("result", "success");
                                    //设置返回数据
                                    LoginActivity.this.setResult(RESULT_OK, intent);
                                    //关闭Activity
                                    LoginActivity.this.finish();
                                }
                                else {
                                    Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    //把返回数据存入Intent
                                    intent.putExtra("result", "failed");
                                    //设置返回数据
                                    LoginActivity.this.setResult(RESULT_OK, intent);
                                    //关闭Activity
                                    LoginActivity.this.finish();
                                }
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("Error.Response", error.toString());
                                Intent intent = new Intent();
                                //把返回数据存入Intent
                                intent.putExtra("result", "failed");
                                //设置返回数据
                                LoginActivity.this.setResult(RESULT_OK, intent);
                                //关闭Activity
                                LoginActivity.this.finish();
                                Toast.makeText(LoginActivity.this, "login failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("password", password);
                        params.put("email", email);

                        //params.put("code", "");

                        return params;
                    }
                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response){
                        try{
                            Map<String, String> responseheader = response.headers;
                            String cookie = responseheader.get("Set-Cookie");
                            String user_id = cookie.split(";")[0].split("=")[1];
                            Log.d("登录cookie-userid",user_id);
                            Storage.putString(MainActivity.th, KEY_USER_ID, user_id);
                            String dataString = new String(response.data, "UTF-8");
                            return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                        }catch(Exception e){
                            Log.d("登录cookie","获取失败");
                            Intent intent = new Intent();
                            //把返回数据存入Intent
                            intent.putExtra("result", "failed");
                            //设置返回数据
                            LoginActivity.this.setResult(RESULT_OK, intent);
                            //关闭Activity
                            LoginActivity.this.finish();
                            return Response.error(new ParseError(e));
                        }
                    }
                };
                queue.add(postRequest);


            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
        if(result.equals("success")){
            edit_password.setText(data.getExtras().getString("passwd"));
            edit_email.setText(data.getExtras().getString("email"));
        }
        Log.i("Mainactivity", result);
    }
}
