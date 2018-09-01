package com.example.dy.sai_demo2.UI.primain_ui;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.example.dy.sai_demo2.LoginActivity;
import com.example.dy.sai_demo2.MainActivity;
import com.example.dy.sai_demo2.R;

import java.util.HashMap;
import java.util.Map;

import static com.example.dy.sai_demo2.Common.Const.KEY_EMAIL;
import static com.example.dy.sai_demo2.Common.Const.KEY_PASSWD;
import static com.example.dy.sai_demo2.Common.Const.KEY_USER_ID;
import static com.example.dy.sai_demo2.Common.Const.LINK_SCHEDULE_USER_INSERT;
import static com.example.dy.sai_demo2.Common.Const.LINK_USER_LOGIN;
import static com.example.dy.sai_demo2.Common.Const.SERVER_IP;

/**
 * Created by dy on 18-3-18.
 */

public class DialogFragment_clender_add extends DialogFragment {
    View view;
    EditText edit_title;
    EditText edit_content;
    Button post_btn;
    //String date = "2018-06-02 11:11:11";
    RequestQueue queue;
    String sel_date;
    public DialogFragment_clender_add(){

    }

    public void setdate(String sel_date){
        this.sel_date = sel_date;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstancestate){
        view = inflater.inflate(R.layout.page_primain_add_dialog,container);
        initview();
        return view;
    }

    private void initview(){
        queue = Volley.newRequestQueue(getActivity());
        edit_content = (EditText) view.findViewById(R.id.edit_content);
        edit_title = (EditText) view.findViewById(R.id.edit_title);
        post_btn = (Button) view.findViewById(R.id.post_btn);
        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_content.getText().toString().equals("") || edit_title.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "请输入", Toast.LENGTH_SHORT).show();
                }else{
                    post();
                    dismiss();
                }

            }
        });
    }

    private void post(){
        String url = "http://"+SERVER_IP+LINK_SCHEDULE_USER_INSERT;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("schedule_response", response);
                        if(response.equals("\"successful\"")){
                            Toast.makeText(view.getContext(), "已添加",Toast.LENGTH_SHORT).show();
                        }else{
                            //
                            Toast.makeText(view.getContext(), "添加失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(view.getContext(), "post failed",Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("title", edit_title.getText().toString());
                params.put("content", edit_content.getText().toString());
                params.put("create_time", sel_date);
                //params.put("code", "");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new HashMap<>();

                mHeaders.put("Cookie","user_id="+ Storage.getString(getActivity(),KEY_USER_ID));
                Log.d("send_head_cookie","user_id="+ Storage.getString(getActivity(),KEY_USER_ID));
                return mHeaders;
            }

        };
        queue.add(postRequest);
    }
}
