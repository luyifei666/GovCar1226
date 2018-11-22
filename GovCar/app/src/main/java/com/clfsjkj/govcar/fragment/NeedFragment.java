package com.clfsjkj.govcar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clfsjkj.govcar.R;
import com.clfsjkj.govcar.adapter.MsgContentFragmentAdapter;
import com.clfsjkj.govcar.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 消息
 * <p>在这个界面中实现类似今日头条的头部tab</p>
 */
public class NeedFragment extends BaseFragment {
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private MsgContentFragmentAdapter adapter;
    private List<String> names;
    private RelativeLayout title;
    private TextView submit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_need, container, false);
        ButterKnife.bind(this, view);

        title = (RelativeLayout)view.findViewById(R.id.title);
        submit = view.findViewById(R.id.submit);
        submit.setVisibility(View.GONE);
        adapter = new MsgContentFragmentAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // 更新适配器数据
        adapter.setList(names);
        return view;
    }

    private void initData() {
        names = new ArrayList<>();
        names.add("人员审批");
        names.add("需要审批");
        names.add("需要派车");
        names.add("需要归队");
        names.add("需要变更 ");
        names.add("费用质疑");
    }
}
