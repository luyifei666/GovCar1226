package com.clfsjkj.govcar;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.clfsjkj.govcar.base.BaseActivity;
import com.clfsjkj.govcar.customerview.SlideRightViewDragHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DriverActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private SlideRightViewDragHelper swipe_right;
    private TextView start_guide;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        ButterKnife.bind(this);
        mContext = this;
        initMyToolBar();
        setStatusBarFullTransparent();
        setFitSystemWindow(true);
        swipe_right = (SlideRightViewDragHelper) findViewById(R.id.swipe_right);
        swipe_right.setOnReleasedListener(new SlideRightViewDragHelper.OnReleasedListener() {
            @Override
            public void onReleased() {
                Log.d("右滑", "到底");
                //跳转
                Toast.makeText(DriverActivity.this,"滑动完成",Toast.LENGTH_SHORT).show();
            }
        });
        start_guide = (TextView) findViewById(R.id.start_guide);
        start_guide.setText("开始导航");
        start_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DriverActivity.this,"点击了开始导航",Toast.LENGTH_SHORT).show();
            }
        });
        mMapView = (MapView) findViewById(R.id.map);
        mBaiduMap = mMapView.getMap();
    }
    private void initMyToolBar() {
        initToolBar(mToolbar, "任务详情", R.drawable.gank_ic_back_white);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
