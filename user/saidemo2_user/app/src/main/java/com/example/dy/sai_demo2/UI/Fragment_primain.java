package com.example.dy.sai_demo2.UI;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dy.sai_demo2.Common.Storage;
import com.example.dy.sai_demo2.LoginActivity;
import com.example.dy.sai_demo2.MainActivity;
import com.example.dy.sai_demo2.R;
import com.example.dy.sai_demo2.UI.primain_ui.Fragment_clendar;
import com.example.dy.sai_demo2.UI.primain_ui.Fragment_joined;
import com.example.dy.sai_demo2.UI.primain_ui.Fragment_myloop;
import com.example.dy.sai_demo2.Views.LoopItem;
import com.example.dy.sai_demo2.Views.adapter.ContentAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.dy.sai_demo2.Common.Const.KEY_EMAIL;
import static com.example.dy.sai_demo2.Common.Const.KEY_SCHOOL;
import static com.example.dy.sai_demo2.Common.Const.KEY_STUDENT_ID;
import static com.example.dy.sai_demo2.Common.Const.KEY_USERNAME;
import static com.example.dy.sai_demo2.Common.Const.KEY_USER_ID;

/**
 * Created by dy on 17-12-2.
 */

public class Fragment_primain extends Fragment {
    View view;
    String[] tab = {"个人日程","已参加","我的圈子"};
    TabLayout mtablayout;
    List<Fragment> fragmentList = new ArrayList<>();
    LinearLayout primain_main_layout;
    ContentAdapter contentAdapter;
    ViewPager mviewPager;
    TextView tx_name;
    TextView tx_school;
    TextView tx_student_id;

    RequestQueue queue;
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.page_primain,container,false);
        initview();
        get_user_data();
        return view;
    }

    private void initview(){
        queue = Volley.newRequestQueue(getContext());
        tx_name = (TextView) view.findViewById(R.id.tx_name);
        tx_school = (TextView) view.findViewById(R.id.tx_school);
        tx_student_id = (TextView) view.findViewById(R.id.tx_student_id);

        primain_main_layout = (LinearLayout) view.findViewById(R.id.primain_main_layout);
        mtablayout = (TabLayout) view.findViewById(R.id.tab_primain);
        mtablayout.setTabMode(TabLayout.MODE_FIXED);
        for(int i = 0;i<tab.length;i++){
            mtablayout.addTab(mtablayout.newTab().setText(tab[i]));
        }
        mviewPager = (ViewPager) view.findViewById(R.id.primain_viewpager);
        //contentPagerAdapter = new ContentPagerAdapter(getChildFragmentManager());
        ViewCompat.setElevation(mviewPager,10);
        mtablayout.setupWithViewPager(mviewPager);

        fragmentList.clear();
        fragmentList.add(new Fragment_clendar());
        fragmentList.add(new Fragment_joined());
        fragmentList.add(new Fragment_myloop());
        mviewPager.setAdapter(contentAdapter = new ContentAdapter(getChildFragmentManager(),fragmentList,tab));

        if(Storage.getString(getContext(),KEY_USER_ID).equals("")){
            primain_main_layout.setVisibility(View.GONE);
        }else{
            primain_main_layout.setVisibility(View.VISIBLE);
        }

    }
    //重写此方法，防止碎片覆盖
    @Override
    public void setMenuVisibility(boolean menuVisibile) {
        super.setMenuVisibility(menuVisibile);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisibile ? View.VISIBLE : View.GONE);
        }
    }

    private void get_user_data() {

        String url = "http://123.206.69.183/user_load";

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("user_response", response);

                try {
                    JSONObject object = new JSONObject(response);
                    tx_school.setText(object.optString("school"));
                    tx_student_id.setText(object.optString("student_id"));
                    tx_name.setText(object.optString("name"));


                } catch (Exception e) {
                    Log.d("json error", e.toString());
                    //Toast.makeText(getContext(), "用户数据解析错误", Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "用户数据请求错误", Toast.LENGTH_SHORT).show();
            }

        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new HashMap<>();
                //mHeaders.put("cookies","");
                mHeaders.put("Cookie", "user_id=" + Storage.getString(getContext(), KEY_USER_ID));
                return mHeaders;
            }
        };
        queue.add(request);
    }

}