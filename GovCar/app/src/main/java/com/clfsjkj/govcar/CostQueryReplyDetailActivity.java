package com.clfsjkj.govcar;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.clfsjkj.govcar.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CostQueryReplyDetailActivity extends BaseActivity {
    //费用质疑回复详情
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_query_reply_detail);
        ButterKnife.bind(this);
        mContext = this;
        initMyToolBar();
        setStatusBarFullTransparent();
        setFitSystemWindow(true);
    }

    private void initMyToolBar() {
        initToolBar(mToolbar, "质疑回复", R.drawable.gank_ic_back_white);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });
    }
}
