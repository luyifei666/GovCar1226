/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.clfsjkj.govcar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.baidu.navisdk.adapter.IBNTTSManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.clfsjkj.govcar.base.BaseActivity;
import com.clfsjkj.govcar.customerview.SlideRightViewDragHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DemoMainActivity extends BaseActivity {

    private static final String APP_FOLDER_NAME = "GovCar";

    static final String ROUTE_PLAN_NODE = "routePlanNode";

    private static final String[] authBaseArr = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    @BindView(R.id.right_btn)
    Button rightBtn;

    //    private Button mWgsNaviBtn = null;
    //    private Button mGcjNaviBtn = null;
//    private Button mBdmcNaviBtn = null;
//    private Button mDb06ll = null;
    //    private Button mGotoSettingsBtn = null;
    private String mSDCardPath = null;

    private static final int authBaseRequestCode = 1;

    private boolean hasInitSuccess = false;

    private BNRoutePlanNode mStartNode = null;
    private Context mContext;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private SlideRightViewDragHelper swipe_right;
    private TextView start_guide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.normal_demo_activity_main);
        ButterKnife.bind(this);

        mContext = this;
        initMyToolBar();
        setStatusBarFullTransparent();
        setFitSystemWindow(true);
//        mWgsNaviBtn = findViewById(R.id.wgsNaviBtn);
//        mGcjNaviBtn = findViewById(R.id.gcjNaviBtn);
//        mBdmcNaviBtn = findViewById(R.id.bdmcNaviBtn);
//        mDb06ll = findViewById(R.id.mDb06llNaviBtn);
//        mGotoSettingsBtn = findViewById(R.id.mGotoSettingsBtn);
        swipe_right = (SlideRightViewDragHelper) findViewById(R.id.swipe_right);
        swipe_right.setOnReleasedListener(new SlideRightViewDragHelper.OnReleasedListener() {
            @Override
            public void onReleased() {
                Log.d("右滑", "到底");
                //跳转
                Toast.makeText(DemoMainActivity.this, "滑动完成", Toast.LENGTH_SHORT).show();
                if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
                    routeplanToNavi(CoordinateType.BD09LL);
                }
            }
        });
        start_guide = (TextView) findViewById(R.id.start_guide);
        start_guide.setText("开始导航");
        start_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DemoMainActivity.this, "点击了开始导航", Toast.LENGTH_SHORT).show();

            }
        });
//        initListener();
        if (initDirs()) {
            initNavi();
        }
    }

    private void initMyToolBar() {
        initToolBar(mToolbar, "任务详情", R.drawable.gank_ic_back_white);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setBackgroundResource(R.drawable.ic_settings);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initListener() {
//        if (mWgsNaviBtn != null) {
//            mWgsNaviBtn.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//                    if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
//                        routeplanToNavi(CoordinateType.WGS84);
//                    }
//                }
//
//            });
//        }
//        if (mGcjNaviBtn != null) {
//            mGcjNaviBtn.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//                    if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
//                        routeplanToNavi(CoordinateType.GCJ02);
//                    }
//                }
//
//            });
//        }
//        if (mBdmcNaviBtn != null) {
//            mBdmcNaviBtn.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//
//                    if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
//                        routeplanToNavi(CoordinateType.BD09_MC);
//                    }
//                }
//            });
//        }

//        if (mDb06ll != null) {
//            mDb06ll.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View arg0) {
//                    if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
//                        routeplanToNavi(CoordinateType.BD09LL);
//                    }
//                }
//            });
//        }

//        if (mGotoSettingsBtn != null) {
//            mGotoSettingsBtn.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
//                        NormalUtils.gotoSettings(DemoMainActivity.this);
//                    }
//                }
//            });
//        }
    }

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private boolean hasBasePhoneAuth() {
        PackageManager pm = this.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void initNavi() {
        // 申请权限
        if (Build.VERSION.SDK_INT >= 23) {
            if (!hasBasePhoneAuth()) {
                this.requestPermissions(authBaseArr, authBaseRequestCode);
                return;
            }
        }

        BaiduNaviManagerFactory.getBaiduNaviManager().init(this,
                mSDCardPath, APP_FOLDER_NAME, new IBaiduNaviManager.INaviInitListener() {

                    @Override
                    public void onAuthResult(int status, String msg) {
                        String result;
                        if (0 == status) {
                            result = "key校验成功!";
                        } else {
                            result = "key校验失败, " + msg;
                        }
                        Toast.makeText(DemoMainActivity.this, result, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void initStart() {
                        Toast.makeText(DemoMainActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void initSuccess() {
                        Toast.makeText(DemoMainActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                        hasInitSuccess = true;
                        // 初始化tts
                        initTTS();
                    }

                    @Override
                    public void initFailed() {
                        Toast.makeText(DemoMainActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    // 外置tts时需要实现的接口回调
    private IBNTTSManager.IBNOuterTTSPlayerCallback mTTSCallback = new IBNTTSManager.IBNOuterTTSPlayerCallback() {

        @Override
        public int getTTSState() {
//            /** 播放器空闲 */
//            int PLAYER_STATE_IDLE = 1;
//            /** 播放器正在播报 */
//            int PLAYER_STATE_PLAYING = 2;
            return PLAYER_STATE_IDLE;
        }

        @Override
        public int playTTSText(String text, String s1, int i, String s2) {
            Log.e("aaa", "playTTSText:" + text);
            return 0;
        }

        @Override
        public void stopTTS() {
            Log.e("aaa", "stopTTS");
        }
    };

    private void initTTS() {
        // 使用内置TTS
        BaiduNaviManagerFactory.getTTSManager().initTTS(getApplicationContext(),
                getSdcardDir(), APP_FOLDER_NAME, NormalUtils.getTTSAppID());

        // 不使用内置TTS
//         BaiduNaviManagerFactory.getTTSManager().initTTS(mTTSCallback);

        // 注册同步内置tts状态回调
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedListener(
                new IBNTTSManager.IOnTTSPlayStateChangedListener() {
                    @Override
                    public void onPlayStart() {
                        Log.e("aaa", "ttsCallback.onPlayStart");
                    }

                    @Override
                    public void onPlayEnd(String speechId) {
                        Log.e("aaa", "ttsCallback.onPlayEnd");
                    }

                    @Override
                    public void onPlayError(int code, String message) {
                        Log.e("aaa", "ttsCallback.onPlayError");
                    }
                }
        );

        // 注册内置tts 异步状态消息
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedHandler(
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        Log.e("aaa", "ttsHandler.msg.what=" + msg.what);
                    }
                }
        );
    }

    private void routeplanToNavi(final int coType) {
        if (!hasInitSuccess) {
            Toast.makeText(DemoMainActivity.this, "还未初始化!", Toast.LENGTH_SHORT).show();
        }

        BNRoutePlanNode sNode = new BNRoutePlanNode(102.705808, 25.002344, "彩立方数据科技有限公司", "彩立方数据科技有限公司", coType);
        BNRoutePlanNode eNode = new BNRoutePlanNode(102.70988916706, 24.979739, "双河湾", "双河湾", coType);
        switch (coType) {
            case CoordinateType.GCJ02: {
                sNode = new BNRoutePlanNode(102.705808, 25.002344, "彩立方数据科技有限公司", "彩立方数据科技有限公司", coType);
                eNode = new BNRoutePlanNode(102.70988916706, 24.979739, "双河湾", "双河湾", coType);
                break;
            }
            case CoordinateType.WGS84: {
                sNode = new BNRoutePlanNode(102.705808, 25.002344, "彩立方数据科技有限公司", "彩立方数据科技有限公司", coType);
                eNode = new BNRoutePlanNode(102.70988916706, 24.979739, "双河湾", "双河湾", coType);
                break;
            }
            case CoordinateType.BD09_MC: {
                sNode = new BNRoutePlanNode(102.705808, 25.002344, "彩立方数据科技有限公司", "彩立方数据科技有限公司", coType);
                eNode = new BNRoutePlanNode(102.70988916706, 24.979739, "双河湾", "双河湾", coType);
                break;
            }
            case CoordinateType.BD09LL: {
                sNode = new BNRoutePlanNode(102.70988916706, 24.979739, "双河湾", "双河湾", coType);
                eNode = new BNRoutePlanNode(102.705808, 25.002344, "彩立方数据科技有限公司", "彩立方数据科技有限公司", coType);
                break;
            }
            default:
                break;
        }

        mStartNode = sNode;

        List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
        list.add(sNode);
        list.add(eNode);

        BaiduNaviManagerFactory.getRoutePlanManager().routeplanToNavi(
                list,
                IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_DEFAULT,
                null,
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START:
                                Toast.makeText(DemoMainActivity.this, "算路开始", Toast.LENGTH_SHORT)
                                        .show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS:
                                Toast.makeText(DemoMainActivity.this, "算路成功", Toast.LENGTH_SHORT)
                                        .show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_FAILED:
                                Toast.makeText(DemoMainActivity.this, "算路失败", Toast.LENGTH_SHORT)
                                        .show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_TO_NAVI:
                                Toast.makeText(DemoMainActivity.this, "算路成功准备进入导航", Toast.LENGTH_SHORT)
                                        .show();
                                Intent intent = new Intent(DemoMainActivity.this,
                                        DemoGuideActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(ROUTE_PLAN_NODE, mStartNode);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                            default:
                                // nothing
                                break;
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == authBaseRequestCode) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                } else {
                    Toast.makeText(DemoMainActivity.this, "缺少导航基本的权限!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            initNavi();
        }
    }

    @OnClick({R.id.right_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_btn:
                if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
                    NormalUtils.gotoSettings(DemoMainActivity.this);
                }
                break;
        }
    }
}
