package com.example.dy.sai_demo2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dy.sai_demo2.Common.Storage;
import com.example.dy.sai_demo2.UI.Fragment_company;
import com.example.dy.sai_demo2.UI.Fragment_loop;
import com.example.dy.sai_demo2.UI.Fragment_news;
import com.example.dy.sai_demo2.UI.Fragment_primain;
import com.example.dy.sai_demo2.UI.Fragment_search;
import com.example.dy.sai_demo2.Views.Drawer;
import com.example.dy.sai_demo2.Views.LoopItem;
import com.example.dy.sai_demo2.Views.adapter.ContentAdapter;
import com.example.dy.sai_demo2.Views.adapter.DrawerlistAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.example.dy.sai_demo2.Common.Const.KEY_USERNAME;
import static com.example.dy.sai_demo2.Common.Const.KEY_USER_ID;

public class MainActivity extends AppCompatActivity {
    static MainActivity th;
    RequestQueue queue;
    //低栏五项
    TabLayout bottom_bar;
    private String[] bottom_bar_tag = {"新闻","圈子","搜索","企业","个人"};
    private ContentAdapter contentAdapter;
    //主界面
    private ViewPager mviewPager;

    //侧边栏listview定义
    private ListView drawer_listview;
    private List<Drawer> drawerList = new LinkedList<>();
    private DrawerlistAdapter adapter;

    //drawer布局
    public static DrawerLayout drawerLayout;

    //侧边栏
    public static LinearLayout drawer_menu;

    //侧边栏退出键
    private ImageView img_quit;

    //关闭侧边栏按键
    private ImageView quit_menu_img;

    //侧边栏个人信息布局
    LinearLayout me_layout;

    //侧边栏个人信息
    TextView tx_username;
    TextView tx_stu_num;
    TextView tx_school;

    //侧边栏未登录按钮
    TextView tx_login;

    private Fragment_news fragment_news;
    private Fragment_loop fragment_loop;
    private Fragment_search fragment_search;
    private Fragment_company fragment_company;
    private Fragment_primain fragment_primain;
    private List<Fragment> fragmentList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
        initdata();
    }

    private void initview(){
        //沉浸式，从郭霖那炒的，我也懒得去看。
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        th = MainActivity.this;
        queue = Volley.newRequestQueue(this);
        //低栏
        bottom_bar = (TabLayout)findViewById(R.id.bottom_bar1);
        bottom_bar.setTabMode(TabLayout.MODE_FIXED);

        //主界面iewpagew初始化
        mviewPager = (ViewPager) findViewById(R.id.main_viewpager);
        //初始化fragment
        fragment_news = new Fragment_news();
        fragment_loop = new Fragment_loop();
        fragment_search = new Fragment_search();
        fragment_company = new Fragment_company();
        fragment_primain = new Fragment_primain();
        fragmentList.clear();
        fragmentList.add(fragment_news);
        fragmentList.add(fragment_loop);
        fragmentList.add(fragment_search);
        fragmentList.add(fragment_company);
        fragmentList.add(fragment_primain);
        contentAdapter = new ContentAdapter(getSupportFragmentManager(),fragmentList,bottom_bar_tag);

        ViewCompat.setElevation(mviewPager,10);
        bottom_bar.setupWithViewPager(mviewPager);
        mviewPager.setAdapter(contentAdapter);

        //测边栏listview初始化
        drawer_listview = (ListView) findViewById(R.id.drawer_listview);

        adapter = new DrawerlistAdapter(MainActivity.this,drawerList);
        drawer_listview.setAdapter(adapter);
        drawer_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Drawer drawer = drawerList.get(position);
                switch(drawer.getName()){
                    case "日程提醒":
                        mviewPager.setCurrentItem(4);
                        drawerLayout.closeDrawer(drawer_menu);
                        break;
                    case "留言提醒":
                        mviewPager.setCurrentItem(4);
                        drawerLayout.closeDrawer(drawer_menu);
                        break;
                    case "我的比赛":
                        mviewPager.setCurrentItem(4);
                        drawerLayout.closeDrawer(drawer_menu);
                        break;
                    case "我的圈子":
                        mviewPager.setCurrentItem(4);
                        drawerLayout.closeDrawer(drawer_menu);
                        break;
                    case "个人中心":
                        mviewPager.setCurrentItem(4);
                        drawerLayout.closeDrawer(drawer_menu);
                        break;
                    case "设置":
                        mviewPager.setCurrentItem(4);
                        drawerLayout.closeDrawer(drawer_menu);
                        break;
                    default:
                        Toast.makeText(MainActivity.this,"错误",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

        //侧边栏退出键初始化
        img_quit = (ImageView) findViewById(R.id.img_quit);
        img_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Storage.cleardata(MainActivity.this);
                tx_login.setVisibility(View.VISIBLE);
                me_layout.setVisibility(View.GONE);
                initdata();
                initview();
            }
        });

        //未登录按钮
        tx_login = (TextView) findViewById(R.id.tx_nologin);

        //侧边栏个人信息布局
        me_layout = (LinearLayout) findViewById(R.id.me_layout);
        me_layout.setVisibility(View.GONE);
        tx_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tx_login.setVisibility(View.GONE);
                //me_layout.setVisibility(View.VISIBLE);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent,1);
            }
        });
        tx_school = (TextView) findViewById(R.id.school_tx);
        tx_stu_num = (TextView) findViewById(R.id.stu_num);
        tx_username = (TextView) findViewById(R.id.username);
        get_user_data();

        //drawer布局
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        //侧边栏
        drawer_menu = (LinearLayout) findViewById(R.id.drawer_menu);

        //侧边栏头像右边的">"按钮
        quit_menu_img = (ImageView) findViewById(R.id.quit_menu_img);
        quit_menu_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(drawer_menu);
            }
        });


    }
    //侧边栏listview初始化数据
    private void initdata(){
        drawerList.clear();
        Drawer dra_cld = new Drawer("日程提醒",R.drawable.drawer_cld);
        dra_cld.setMsgNum(12);
        Drawer dra_msg = new Drawer("留言提醒",R.drawable.drawer_msg);
        dra_msg.setMsgNum(12);
        drawerList.add(dra_cld);
        drawerList.add(dra_msg);
        drawerList.add(new Drawer("我的比赛",R.drawable.drawer_mymatch));
        drawerList.add(new Drawer("我的圈子",R.drawable.drawer_myloop));
        drawerList.add(new Drawer("个人中心",R.drawable.drawer_me));
        drawerList.add(new Drawer("设置",R.drawable.drawer_set));


    }

    private void get_user_data() {
        //Toast.makeText(this,Storage.getString(MainActivity.this,KEY_USERNAME),Toast.LENGTH_SHORT).show();
        if(Storage.getString(MainActivity.this,KEY_USER_ID).equals("")) {
            //Toast.makeText(this,"未登录",Toast.LENGTH_SHORT).show();
            return;
        }
        String url = "http://123.206.69.183/user_load";
        //Toast.makeText(this, Storage.getString(MainActivity.this, KEY_USER_ID), Toast.LENGTH_SHORT).show();
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("user_response", response);

                try {
                    JSONObject object = new JSONObject(response);
                    tx_school.setText(object.optString("school"));
                    tx_stu_num.setText(object.optString("student_id"));
                    tx_username.setText(object.optString("name"));

                    tx_login.setVisibility(View.GONE);
                    me_layout.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Log.d("json error", e.toString());
                    Toast.makeText(MainActivity.this, "用户数据解析错误", Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "用户数据请求错误", Toast.LENGTH_SHORT).show();
            }

        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new HashMap<>();
                //mHeaders.put("cookies","");
                mHeaders.put("Cookie", "user_id=" + Storage.getString(MainActivity.this, KEY_USER_ID));
                return mHeaders;
            }
        };
        queue.add(request);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
        if(result.equals("success")){
            tx_login.setVisibility(View.GONE);
            me_layout.setVisibility(View.VISIBLE);

            initview();
            initdata();
        }else{
            tx_login.setVisibility(View.VISIBLE);
            me_layout.setVisibility(View.GONE);
        }
        Log.i("Mainactivity", result);
    }
}
