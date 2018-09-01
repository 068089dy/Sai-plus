package com.example.dy.sai_demo2.UI.primain_ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dy.sai_demo2.Common.Storage;
import com.example.dy.sai_demo2.MainActivity;
import com.example.dy.sai_demo2.R;
import com.example.dy.sai_demo2.Views.LoopItem;
import com.example.dy.sai_demo2.Views.Primain_Cal_Item;
import com.example.dy.sai_demo2.Views.adapter.ListViewAdapter;
import com.example.dy.sai_demo2.Views.adapter.ListViewHolder;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import static com.example.dy.sai_demo2.Common.Const.KEY_USER_ID;
import static com.example.dy.sai_demo2.Common.Const.LINK_CIRCLE_LIST_LOAD;
import static com.example.dy.sai_demo2.Common.Const.LINK_Schedule_list_load_user;
import static com.example.dy.sai_demo2.Common.Const.SERVER_IP;

/**
 * Created by dy on 17-12-3.
 */

public class Fragment_clendar extends Fragment {
    View view;

    ImageView add_img;
    MaterialCalendarView calendarView;
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    String sel_date;
    ListView listView;
    ListViewAdapter<Primain_Cal_Item> adapter;
    List<Primain_Cal_Item> list = new ArrayList<>();
    RequestQueue queue;
    PtrFrameLayout refresh_layout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.page_primain_clender,container,false);

        initview();
        initdata();
        return view;
    }
    //重写此方法，防止碎片覆盖
    @Override
    public void setMenuVisibility(boolean menuVisibile) {
        super.setMenuVisibility(menuVisibile);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisibile ? View.VISIBLE : View.GONE);
        }
    }

    private void initview(){
        queue = Volley.newRequestQueue(getContext());
        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String d = date.toString();
                d = d.substring(12,d.length()-1);

                String year = d.split("-")[0];
                String month = (Integer.parseInt(d.split("-")[1])+1)+"";
                if(month.length() == 1) month = "0"+month;
                String day = d.split("-")[2];
                if(day.length() == 1) day = "0"+day;
                sel_date = year+"-"+month+"-"+day;
                Toast.makeText(getContext(),sel_date,Toast.LENGTH_SHORT).show();
            }
        });
        add_img = (ImageView) view.findViewById(R.id.add_img);
        add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
            }
        });
        listView = (ListView) view.findViewById(R.id.primain_calender_lv);
        listView.setAdapter(adapter = new ListViewAdapter<Primain_Cal_Item>(getContext(),list,R.layout.page_primain_clender_item) {
            @Override
            public void convertView(ListViewHolder holder, Primain_Cal_Item primain_cal_item) {
                holder.setStr(R.id.date, primain_cal_item.getDate().substring(5,10))
                        .setStr(R.id.title, primain_cal_item.getTitle());
            }
        });
        refresh_layout = (PtrFrameLayout) view.findViewById(R.id.primain_cal_refresh);
        refresh_layout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run(){
                        initdata();
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
        //setListViewHeightBasedOnChildren(listView);
    }

    private void showdialog(){
        DialogFragment_clender_add dm = new DialogFragment_clender_add();
        dm.setdate(sel_date+" 11:11:11");
        dm.show(getActivity().getFragmentManager(),"hello");
    }

    private void initdata(){
        list.clear();
        String url = "http://"+SERVER_IP+LINK_Schedule_list_load_user;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("loop_response", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        String id = jsonObject.optString("id");
                        String date = jsonObject.optString("create_time");
                        String title = jsonObject.optString("title");
                        String content = jsonObject.optString("content");
                        list.add(new Primain_Cal_Item(id,title,content,date));
                    }
                    adapter.notifyDataSetChanged();
                    //setListViewHeightBasedOnChildren(listView);
                }catch (Exception e){
                    Log.d("json error",e.toString());
                    //Toast.makeText(getContext(),"数据解析错误", Toast.LENGTH_LONG).show();
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

                mHeaders.put("Cookie","user_id="+ Storage.getString(getActivity(),KEY_USER_ID));
                Log.d("send_head_cookie","user_id="+ Storage.getString(getActivity(),KEY_USER_ID));
                return mHeaders;
            }
        };

        queue.add(request);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
