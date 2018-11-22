package com.clfsjkj.govcar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.clfsjkj.govcar.ItemAdapter.DataServer;
import com.clfsjkj.govcar.ItemAdapter.MySection;
import com.clfsjkj.govcar.ItemAdapter.SectionAdapter;
import com.clfsjkj.govcar.R;
import com.clfsjkj.govcar.adapter.MsgContentFragmentAdapter;
import com.clfsjkj.govcar.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 发现
 * <p>展示平均分配位置的tab页卡</p>
 */
public class MsgFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private List<MySection> mData;
    private RelativeLayout title;
    private TextView submit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg, container, false);
        ButterKnife.bind(this, view);

        title = (RelativeLayout)view.findViewById(R.id.title);
        submit = view.findViewById(R.id.submit);
        submit.setVisibility(View.GONE);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mData = DataServer.getSampleData();
        SectionAdapter sectionAdapter = new SectionAdapter(R.layout.item_section_content, R.layout.def_section_head, mData);

        sectionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MySection mySection = mData.get(position);
                if (mySection.isHeader)
                    Toast.makeText(getContext(), mySection.header, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getContext(), mySection.t.getName(), Toast.LENGTH_LONG).show();
            }
        });
        sectionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(getContext(), "onItemChildClick" + position, Toast.LENGTH_LONG).show();
            }
        });
        mRecyclerView.setAdapter(sectionAdapter);
        return view;
    }


}
