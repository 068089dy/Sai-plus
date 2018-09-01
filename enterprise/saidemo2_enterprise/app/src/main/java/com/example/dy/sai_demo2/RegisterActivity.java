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
    EditText edit_enterprise_name;
    EditText edit_num;
    EditText edit_postcode;
    EditText edit_location;
    Button submit_btn;
    RequestQueue queue;

    static RegisterActivity me;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        me = this;
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
        edit_enterprise_name = (EditText) findViewById(R.id.enterprise_name);
        edit_postcode = (EditText) findViewById(R.id.postcode);
        edit_location = (EditText) findViewById(R.id.location);
        //edit_email = (EditText) findViewById(R.id.email);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        //queue = Volley.newRequestQueue(this);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_location.getText().toString().equals("") ||
                        edit_enterprise_name.getText().toString().equals("") ||
                        edit_num.getText().toString().equals("") ||
                        edit_postcode.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this,"请检查信息是否输入完整",Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(RegisterActivity.this,"请检查信息是否输入完整",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, Register2Activity.class);
                    intent.putExtra("location", edit_location.getText().toString());
                    intent.putExtra("enterprise_name", edit_enterprise_name.getText().toString());
                    intent.putExtra("num", edit_num.getText().toString());
                    intent.putExtra("postcode", edit_postcode.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
