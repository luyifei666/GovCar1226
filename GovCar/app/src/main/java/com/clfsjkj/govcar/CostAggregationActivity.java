package com.clfsjkj.govcar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.clfsjkj.govcar.ItemAdapter.MyExtendableListViewAdapter;
import com.clfsjkj.govcar.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CostAggregationActivity extends BaseActivity {
    //费用汇总
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.expand_list_view)
    ExpandableListView expandListView;
    private Context mContext;
    private MyExtendableListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_aggregation);
        ButterKnife.bind(this);
        mContext = this;
        initMyToolBar();
        setStatusBarFullTransparent();
        setFitSystemWindow(true);
        initView();
    }

    private void initMyToolBar() {
        initToolBar(mToolbar, "费用汇总", R.drawable.gank_ic_back_white);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });
    }

    private void initView() {
        adapter = new MyExtendableListViewAdapter(mContext);
        expandListView.setAdapter(adapter);
        //设置分组的监听
        expandListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                Toast.makeText(getApplicationContext(), groupString[groupPosition], Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //设置子项布局监听
        expandListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                Toast.makeText(getApplicationContext(), childString[groupPosition][childPosition], Toast.LENGTH_SHORT).show();
                Intent it  = new Intent(CostAggregationActivity.this,ApplayOrderDetailActivity.class);
                it.putExtra("isShowBtnGroup",false);
                startActivity(it);
                return true;

            }
        });
        //控制他只能打开一个组
        expandListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int count = adapter.getGroupCount();
                for(int i = 0;i < count;i++){
                    if (i!=groupPosition){
                        expandListView.collapseGroup(i);
                    }
                }
            }
        });
    }

}
