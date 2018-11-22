package com.clfsjkj.govcar.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.clfsjkj.govcar.fragment.IndexFragment;
import com.clfsjkj.govcar.fragment.MsgFragment;
import com.clfsjkj.govcar.fragment.MeFragment;
import com.clfsjkj.govcar.fragment.NeedFragment;

/**
 * 主界面底部菜单适配器
 */
public class MainFragmentAdapter extends FragmentPagerAdapter {
    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        switch (i) {
            case 0:
                //首页
                fragment = new IndexFragment();
                break;
            case 1:
                //待办
                fragment = new NeedFragment();
                break;
            case 2:
                //消息
                fragment = new MsgFragment();
                break;
            case 3:
                //我的
                fragment = new MeFragment();
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

}
