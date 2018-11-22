package com.clfsjkj.govcar.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.clfsjkj.govcar.fragment.NeedContentFragment;
import com.clfsjkj.govcar.fragment.NeedOneFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息内容子页面适配器
 */
public class MsgContentFragmentAdapter extends FragmentPagerAdapter {
    private List<String> names;

    public MsgContentFragmentAdapter(FragmentManager fm) {
        super(fm);
        this.names = new ArrayList<>();
    }

    /**
     * 数据列表
     *
     * @param datas
     */
    public void setList(List<String> datas) {
        this.names.clear();
        this.names.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
//        NeedContentFragment fragment = new NeedContentFragment();
        Bundle bundle = new Bundle();
//        bundle.putString("name", names.get(position));
//        fragment.setArguments(bundle);
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new NeedOneFragment();
                bundle.putString("name", names.get(position));
                fragment.setArguments(bundle);
                break;
            case 1:
                fragment = new NeedOneFragment();
                bundle.putString("name", names.get(position));
                fragment.setArguments(bundle);
                break;
            case 2:
                fragment = new NeedOneFragment();
                bundle.putString("name", names.get(position));
                fragment.setArguments(bundle);
                break;
            case 3:
                fragment = new NeedOneFragment();
                bundle.putString("name", names.get(position));
                fragment.setArguments(bundle);
                break;
            default:
                fragment = new NeedOneFragment();
                bundle.putString("name", names.get(position));
                fragment.setArguments(bundle);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String plateName = names.get(position);
        if (plateName == null) {
            plateName = "";
        } else if (plateName.length() > 15) {
            plateName = plateName.substring(0, 15) + "...";
        }
        return plateName;
    }
}
