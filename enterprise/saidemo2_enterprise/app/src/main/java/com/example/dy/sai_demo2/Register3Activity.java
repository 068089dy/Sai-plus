package com.example.dy.sai_demo2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import cz.msebera.android.httpclient.Header;
import uk.me.hardill.volley.multipart.MultipartRequest;

import static com.example.dy.sai_demo2.Common.Const.LINK_enterprise_insert;
import static com.example.dy.sai_demo2.Common.Const.SERVER_IP;

public class Register3Activity extends AppCompatActivity {
    EditText edit_business_scope;
    EditText edit_enterprise_size;
    EditText edit_money;
    EditText edit_describe;
    Button submit_btn;
    String enterprise_name;
    String num;
    String postcode;
    String location;

    String IDcard_image_path;
    String legal_IDcard;
    String legal_name;
    String email;
    String tel;
    String code;
    String password;

    String business_scope;
    String enterprise_size;
    String money;
    String describe;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);
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
        //Log.d("initview","initview");
        queue = Volley.newRequestQueue(Register3Activity.this);

        enterprise_name = getIntent().getStringExtra("enterprise_name");
        num = getIntent().getStringExtra("num");
        postcode = getIntent().getStringExtra("postcode");
        location = getIntent().getStringExtra("location");

        IDcard_image_path = getIntent().getStringExtra("IDcard_image");
        legal_IDcard = getIntent().getStringExtra("legal_IDcard");
        legal_name = getIntent().getStringExtra("legal_name");
        email = getIntent().getStringExtra("email");
        tel = getIntent().getStringExtra("tel");
        code = getIntent().getStringExtra("code");
        password = getIntent().getStringExtra("password");

        edit_business_scope = (EditText) findViewById(R.id.business_scope);
        edit_enterprise_size = (EditText) findViewById(R.id.enterprise_size);
        edit_money = (EditText) findViewById(R.id.money);
        edit_describe = (EditText) findViewById(R.id.describe);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_business_scope.getText().toString().equals("") ||
                        edit_enterprise_size.getText().toString().equals("") ||
                        edit_money.getText().toString().equals("") ||
                        edit_describe.getText().toString().equals("")) {
                    Toast.makeText(Register3Activity.this,"请检查信息是否输入完整",Toast.LENGTH_SHORT).show();
                }else{

                    business_scope = edit_business_scope.getText().toString();
                    enterprise_size = edit_enterprise_size.getText().toString();
                    money = edit_money.getText().toString();
                    describe = edit_describe.getText().toString();
                    //Toast.makeText(Register3Activity.this,"发送请求",Toast.LENGTH_SHORT).show();
                    try {
                        post();
                    }catch (Exception e){

                    }


                }
            }
        });
    }
    private void post() throws Exception{
        MultipartRequest request = new MultipartRequest("http://123.206.69.183/enterprise_insert", null,
                new com.android.volley.Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        //
                        try {
                            String srt2 = new String(response.data.clone(), "UTF-8");
                            Log.d("response",srt2);
                            //Log.d("response",response.headers.get("Cookie"));

                            if(srt2.equals("\"successful\"")) {

                                Toast.makeText(Register3Activity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                LoginActivity.me.finish();
                                Register2Activity.me.finish();
                                RegisterActivity.me.finish();
                                Intent intent = new Intent(Register3Activity.this, LoginActivity.class);
                                intent.putExtra("email", email);
                                intent.putExtra("password", password);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(Register3Activity.this,"注册失败",Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Log.d("encode error",e.toString());
                            Toast.makeText(Register3Activity.this,"注册失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error","error");
                        Toast.makeText(Register3Activity.this,"注册失败",Toast.LENGTH_SHORT).show();
                    }
                });
        try {
            File file = new File(IDcard_image_path);
            Log.d("file_path",IDcard_image_path);
            byte[] bytes = new byte[(int) file.length()];
            FileInputStream stream = new FileInputStream(file);
            stream.read(bytes);
            stream.close();
            request.addPart(new MultipartRequest.FilePart("IDcard_image","image/jpg",file.getName(),bytes));
            request.addPart(new MultipartRequest.FormPart("enterprise_name", enterprise_name));
            request.addPart(new MultipartRequest.FormPart("registre_code", num));
            request.addPart(new MultipartRequest.FormPart("postcode", postcode));
            request.addPart(new MultipartRequest.FormPart("location", location));

            request.addPart(new MultipartRequest.FormPart("legal_IDcard", legal_IDcard));
            request.addPart(new MultipartRequest.FormPart("legal_name", legal_name));
            request.addPart(new MultipartRequest.FormPart("email", email));
            request.addPart(new MultipartRequest.FormPart("tel", tel));
            //request.addPart(new MultipartRequest.FormPart("code", "5789"));
            request.addPart(new MultipartRequest.FormPart("business_scope", business_scope));
            request.addPart(new MultipartRequest.FormPart("enterprise_size", enterprise_name));
            request.addPart(new MultipartRequest.FormPart("money", money));
            request.addPart(new MultipartRequest.FormPart("describe", describe));
            request.addPart(new MultipartRequest.FormPart("password",password));
            queue.add(request);
        }catch(Exception e){
            Log.d("request error",e.toString());
            Toast.makeText(Register3Activity.this,"数据读取失败",Toast.LENGTH_SHORT).show();
        }

    }
//    private void sendImage(Bitmap bm) {
//        String url = "http://"+SERVER_IP+LINK_enterprise_insert;
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.PNG, 60, stream);
//        byte[] bytes = stream.toByteArray();
//        String img = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
//        AsyncHttpClient client = new AsyncHttpClient();
//        RequestParams params = new RequestParams();
//        params.add("IDcard_image", img);
//        params.put("enterprise_name;", enterprise_name);
//                params.put("registre_code", num);
//                params.put("postcode", postcode);
//                params.put("location", location);
//                //params.put("IDcard_image", IDcard_image);
//                params.put("legal_IDcard", legal_IDcard);
//                params.put("legal_name", legal_name);
//                params.put("email", email);
//                params.put("tel", tel);
//                params.put("code", code);
//                params.put("business_scope", business_scope);
//                params.put("enterprise_size", enterprise_size);
//                params.put("money", money);
//                params.put("describe", describe);
//                params.put("password",password);
//        client.post(url, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                Toast.makeText(Register3Activity.this, "Upload Success!", Toast.LENGTH_LONG).show();
//
//            }
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                Toast.makeText(Register3Activity.this, "Upload Fail!", Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//    private void post(){
//        String url = "http://"+SERVER_IP+LINK_enterprise_insert;
//        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>()
//                {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        Log.d("register_response",response);
//                        if(response.equals("\"注册成功\"")){
//                            Intent intent = new Intent(Register3Activity.this, LoginActivity.class);
//                            intent.putExtra("password",password);
//                            intent.putExtra("email",email);
//                            startActivity(intent);
//                        }
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // error
//                        Log.d("Error.Response", error.toString());
//
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("enterprise_name;", enterprise_name);
//                params.put("registre_code", num);
//                params.put("postcode", postcode);
//                params.put("location", location);
//                params.put("IDcard_image", IDcard_image);
//                params.put("legal_IDcard", legal_IDcard);
//                params.put("legal_name", legal_name);
//                params.put("email", email);
//                params.put("tel", tel);
//                params.put("code", code);
//                params.put("business_scope", business_scope);
//                params.put("enterprise_size", enterprise_size);
//                params.put("money", money);
//                params.put("describe", describe);
//                params.put("password",password);
//
//                return params;
//            }
//
//        };
//        queue.add(postRequest);
//    }

}
