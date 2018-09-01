package com.example.dy.sai_demo2.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dy.sai_demo2.Common.Storage;
import com.example.dy.sai_demo2.JoinedActivity;
import com.example.dy.sai_demo2.MainActivity;
import com.example.dy.sai_demo2.PushmatchActivity;
import com.example.dy.sai_demo2.R;
import com.example.dy.sai_demo2.Views.CompanyItem;
import com.example.dy.sai_demo2.Views.Trace;
import com.example.dy.sai_demo2.Views.adapter.TraceListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

import static com.example.dy.sai_demo2.Common.Const.KEY_USER_ID;
import static com.example.dy.sai_demo2.Common.Const.LINK_game_list_load;
import static com.example.dy.sai_demo2.Common.Const.LINK_message_list_load;
import static com.example.dy.sai_demo2.Common.Const.SERVER_IP;


/**
 * Created by dy on 17-11-23.
 */

public class Fragment_news extends Fragment {
    View view;

    ListView timeline;
    private List<Trace> traceList = new ArrayList<>(10);
    private TraceListAdapter adapter;
    private PtrFrameLayout refresh_layout;
    ImageView head_img;

    TextView tx_match;
    RequestQueue queue;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.page_news,container,false);
        initview();
        initdata();
        return view;
    }
    private void initview(){
        queue = Volley.newRequestQueue(getContext());
        timeline = (ListView) view.findViewById(R.id.time_line);

        head_img = (ImageView) view.findViewById(R.id.head);
        head_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.drawerLayout.openDrawer(MainActivity.drawer_menu);
            }
        });
        tx_match = (TextView) view.findViewById(R.id.tx_match);
        tx_match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PushmatchActivity.class);
                intent.putExtra("user_id",Storage.getString(getActivity(),KEY_USER_ID));
                startActivity(intent);
            }
        });
        adapter = new TraceListAdapter(view.getContext(), traceList);
        timeline.setAdapter(adapter);
        timeline.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), JoinedActivity.class);
                intent.putExtra("game_id",traceList.get(position).getId());
                intent.putExtra("user_id",Storage.getString(getActivity(),KEY_USER_ID));
                startActivity(intent);
            }
        });
        refresh_layout = (PtrFrameLayout) view.findViewById(R.id.news_list_refresh);
        refresh_layout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run(){
                        initdata();
                        //adapter.notifyDataSetChanged();
                        refresh_layout.refreshComplete();
                    }
                }, 100);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 默认实现，根据实际情况做改动
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
    }

    private void initdata(){
        // 模拟一些假的数据
        traceList.clear();

        String url = "http://"+SERVER_IP+LINK_game_list_load;

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("company_response", response);

                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = new JSONArray(object.optString("game_list"));
                    for (int i = 0;i<array.length();i++){
                        object = array.optJSONObject(i);
                        String[] date = object.optString("create_time").split(" ")[0].split("-");
                        String d = date[1]+"-"+date[2];
                        String id = object.optString("id");
                        String title = object.optString("title");
                        traceList.add(new Trace(id, d, title));
                        //company_list.add(new CompanyItem(object.optString("id"),object.optString("title"), object.optString("id"),object.optString("create_time")));

                    }
                    adapter.notifyDataSetChanged();
                }catch (Exception e){
                    Log.d("json error",e.toString());
                    Toast.makeText(getContext(),"企业数据解析错误", Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"error", Toast.LENGTH_SHORT).show();
            }

        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new HashMap<>();

                mHeaders.put("Cookie","enterprise_id="+ Storage.getString(getActivity(),KEY_USER_ID));
                Log.d("send_head_cookie","user_id="+ Storage.getString(getActivity(),KEY_USER_ID));
                return mHeaders;
            }
        };

        queue.add(request);
//        traceList.add(new Trace("17日", "“数据杯”大数据分析应用大赛接近尾声，经过一轮大众投票后，各参赛团队取得了一定的成绩，3天后的闭幕暨答辩大会将会为我们揭晓最终的胜者，让我们拭目以待。"));
//        traceList.add(new Trace("7日", "“数据杯”大数据分析应用大赛第一轮投票开始，请为你欣赏的团队投上宝贵的一票。"));
//        traceList.add(new Trace("27日", "“数据杯”大数据分析应用大赛作品提交时间截止，30支参赛团队通过初选顺利提交作品，十天后将进行第一轮网上投票，请各团队做好准备，投票过程中严禁作弊刷票行为，一经查出，取消参赛资格。"));
//        traceList.add(new Trace("17日", "“实验杯”软件设计大赛开始报名。"));
//        traceList.add(new Trace("17日", "“数据杯”大数据分析应用大赛作品提交时间截止，30支参赛团队通过初选顺利提交作品，十天后将进行第一轮网上投票，请各团队做好准备，投票过程中严禁作弊刷票行为，一经查出，取消参赛资格。"));
//        traceList.add(new Trace("17日", "“数据杯”大数据分析应用大赛作品提交时间截止，30支参赛团队通过初选顺利提交作品，十天后将进行第一轮网上投票，请各团队做好准备，投票过程中严禁作弊刷票行为，一经查出，取消参赛资格。"));
//        traceList.add(new Trace("17日", "“数据杯”大数据分析应用大赛作品提交时间截止，30支参赛团队通过初选顺利提交作品，十天后将进行第一轮网上投票，请各团队做好准备，投票过程中严禁作弊刷票行为，一经查出，取消参赛资格。]"));
//        traceList.add(new Trace("17日", "“实验杯”软件设计大赛开始报名。"));
//        traceList.add(new Trace("17日", "“实验杯”软件设计大赛开始报名。"));
        //adapter.notifyDataSetChanged();
    }



    //重写此方法，防止碎片覆盖
    @Override
    public void setMenuVisibility(boolean menuVisibile) {
        super.setMenuVisibility(menuVisibile);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisibile ? View.VISIBLE : View.GONE);
        }
    }



}
