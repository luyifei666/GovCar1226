package com.clfsjkj.govcar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.clfsjkj.govcar.ItemAdapter.DataServer;
import com.clfsjkj.govcar.adapter.CustomLoadMoreView;
import com.clfsjkj.govcar.adapter.PullToRefreshAdapter;
import com.clfsjkj.govcar.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ApplyRecordActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.right_btn)
    Button mRightBtn;
    @BindView(R.id.right_text)
    TextView mRightText;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_list)
    RecyclerView mRvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    private Context mContext;
    private PullToRefreshAdapter pullToRefreshAdapter;
    private static final int TOTAL_COUNTER = 18;
    private static final int PAGE_SIZE = 6;
    private int delayMillis = 1000;
    private int mCurrentCounter = 0;
    private boolean isErr;
    private boolean mLoadMoreEndGone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_record);
        ButterKnife.bind(this);
        mContext = this;
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRvList.setLayoutManager(new LinearLayoutManager(this));
//        setTitle("Pull TO Refresh Use");
        initMyToolBar();
        setStatusBarFullTransparent();
        setFitSystemWindow(true);
        initAdapter();
//        addHeadView();
    }

    private void initMyToolBar() {
        initToolBar(mToolbar, "申请记录", R.drawable.gank_ic_back_white);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });
    }

    private void addHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.head_view, (ViewGroup) mRvList.getParent(), false);
        headView.findViewById(R.id.iv).setVisibility(View.GONE);
        ((TextView) headView.findViewById(R.id.tv)).setText("change load view");
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadMoreEndGone = true;
                pullToRefreshAdapter.setLoadMoreView(new CustomLoadMoreView());
                mRvList.setAdapter(pullToRefreshAdapter);
                Toast.makeText(ApplyRecordActivity.this, "change complete", Toast.LENGTH_LONG).show();
            }
        });
        pullToRefreshAdapter.addHeaderView(headView);
    }

    @Override
    public void onLoadMoreRequested() {
        mSwipeLayout.setEnabled(false);
        if (pullToRefreshAdapter.getData().size() < PAGE_SIZE) {
            pullToRefreshAdapter.loadMoreEnd(true);
        } else {
            if (mCurrentCounter >= TOTAL_COUNTER) {
//                    pullToRefreshAdapter.loadMoreEnd();//default visible
                pullToRefreshAdapter.loadMoreEnd(mLoadMoreEndGone);//true is gone,false is visible
            } else {
                if (isErr) {
                    pullToRefreshAdapter.addData(DataServer.getSampleData(PAGE_SIZE));
                    mCurrentCounter = pullToRefreshAdapter.getData().size();
                    pullToRefreshAdapter.loadMoreComplete();
                } else {
                    isErr = true;
                    Toast.makeText(ApplyRecordActivity.this, R.string.network_err, Toast.LENGTH_LONG).show();
                    pullToRefreshAdapter.loadMoreFail();

                }
            }
            mSwipeLayout.setEnabled(true);
        }
    }

    @Override
    public void onRefresh() {
        pullToRefreshAdapter.setEnableLoadMore(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshAdapter.setNewData(DataServer.getSampleData(PAGE_SIZE));
                isErr = false;
                mCurrentCounter = PAGE_SIZE;
                mSwipeLayout.setRefreshing(false);
                pullToRefreshAdapter.setEnableLoadMore(true);
            }
        }, delayMillis);
    }

    private void initAdapter() {
        pullToRefreshAdapter = new PullToRefreshAdapter();
        pullToRefreshAdapter.setOnLoadMoreListener(this, mRvList);
        pullToRefreshAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//        pullToRefreshAdapter.setPreLoadNumber(3);
        mRvList.setAdapter(pullToRefreshAdapter);
        mCurrentCounter = pullToRefreshAdapter.getData().size();

        mRvList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
                Toast.makeText(ApplyRecordActivity.this, Integer.toString(position), Toast.LENGTH_SHORT).show();
                //跳转到申请详情，当状态为XX时可修改，否则不可修改
                Intent it;
                if (position%2==1){
                    it = new Intent(ApplyRecordActivity.this,ApplyCarActivity.class);
                    startActivity(it);
                }else {
                    it = new Intent(ApplyRecordActivity.this,ApplayOrderDetailActivity.class);
                    startActivity(it);
                }
            }
        });
    }
}
