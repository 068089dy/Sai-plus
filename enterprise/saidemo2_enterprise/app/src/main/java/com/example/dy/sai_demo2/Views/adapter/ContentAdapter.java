package com.example.dy.sai_demo2.Views.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ContentAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList = new ArrayList<>();
    String[] list_tag;

    public ContentAdapter(FragmentManager fm,List<Fragment> fragmentList,String[] list_tag) {
        super(fm);
        this.fragmentList = fragmentList;
        this.list_tag = list_tag;
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list_tag[position];
    }

}

//class ContentPagerAdapter extends FragmentPagerAdapter {
//
//    public ContentPagerAdapter(FragmentManager fm) {
//        super(fm);
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//
//
//    }
//
//    @Override
//    public int getCount() {
//        return 5;
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return bottom_bar_tag[position];
//    }
//
//}