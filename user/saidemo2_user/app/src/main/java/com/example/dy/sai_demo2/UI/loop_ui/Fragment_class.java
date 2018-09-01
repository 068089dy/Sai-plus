package com.example.dy.sai_demo2.UI.loop_ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dy.sai_demo2.Common.Storage;
import com.example.dy.sai_demo2.R;
import com.example.dy.sai_demo2.UI.Fragment_loop;

import static com.example.dy.sai_demo2.Common.Const.KEY_USER_ID;


/**
 * Created by dy on 18-3-17.
 */

public class Fragment_class extends Fragment {
    View view;

    ImageView back_imgv;
    public static ViewPager loop_class_viewPager;
    ContentPagerAdapter adapter;
    TabLayout tabLayout;
    String[] bottom_bar_tag = {"技术交流","团队组建"};

    FloatingActionButton add_btn;
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.page_loop_class,container,false);
        initview();


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

        back_imgv = (ImageView) view.findViewById(R.id.back_imgv);
        back_imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fragment_loop.loop_viewPager.setCurrentItem(0);
                //Fragment_class.this.onDestroy();
                //getActivity().onBackPressed();//销毁自己
                replacefragment(new Fragment_loop());
            }
        });
        loop_class_viewPager = (ViewPager) view.findViewById(R.id.class_vp);
        adapter = new ContentPagerAdapter(getChildFragmentManager());
        loop_class_viewPager.setAdapter(adapter);
        loop_class_viewPager.setCurrentItem(0);
        add_btn = (FloatingActionButton) view.findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loop_class_viewPager.setCurrentItem(2);
                Intent intent = new Intent(getActivity(), Activity_class_add.class);
                intent.putExtra("user_id", Storage.getString(getActivity(),KEY_USER_ID));
                startActivity(intent);
            }
        });
        tabLayout = (TabLayout) view.findViewById(R.id.loop_class_tab);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.print(String.valueOf(tab.getPosition()));
                switch (tab.getPosition()){
                    case 0:
                        loop_class_viewPager.setCurrentItem(0);
                        break;
                    case 1:
                        loop_class_viewPager.setCurrentItem(1);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:

                    Fragment fragment = new Fragment_class_tech();
//                    fragment.setUserVisibleHint(false);
                    return fragment;
                case 1:
                    return new Fragment_class_group();

                default:
                    return null;

            }

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return bottom_bar_tag[position];
        }

    }

    private void replacefragment(Fragment fragment){
        this.setUserVisibleHint(false);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.loop_layout,fragment);
        transaction.commit();

    }
}
