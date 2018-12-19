/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.clfsjkj.govcar;

import android.Manifest;
import android.app.Dialog;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.baidu.navisdk.adapter.IBNTTSManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.clfsjkj.govcar.base.BaseActivity;
import com.clfsjkj.govcar.customerview.SlideRightViewDragHelper;
import com.clfsjkj.govcar.overlayutil.BikingRouteOverlay;
import com.clfsjkj.govcar.overlayutil.DrivingRouteOverlay;
import com.clfsjkj.govcar.overlayutil.MassTransitRouteOverlay;
import com.clfsjkj.govcar.overlayutil.OverlayManager;
import com.clfsjkj.govcar.overlayutil.TransitRouteOverlay;
import com.clfsjkj.govcar.overlayutil.WalkingRouteOverlay;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DemoMainActivity extends BaseActivity implements BaiduMap.OnMapClickListener,
        OnGetRoutePlanResultListener {

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
    // 浏览路线节点相关
    RouteLine route = null;
    OverlayManager routeOverlay = null;
    boolean useDefaultIcon = false;

    // 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
    // 如果不处理touch事件，则无需继承，直接使用MapView即可
    MapView mMapView = null;    // 地图View
    BaiduMap mBaidumap = null;
    // 搜索相关
    RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    DrivingRouteResult nowResultdrive = null;

    int nowSearchType = -1; // 当前进行的检索，供判断浏览节点时结果使用。

    String startNodeStr = "文山壮族苗族自治州";
    String endNodeStr = "双河湾";
    boolean hasShownDialogue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.normal_demo_activity_main);
        ButterKnife.bind(this);

        mContext = this;
        initMyToolBar();
        showProgressDialog("正在加载中……");
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


        // 初始化地图
        mMapView = (MapView) findViewById(R.id.map);
        mBaidumap = mMapView.getMap();
        // 地图点击事件处理
        mBaidumap.setOnMapClickListener(this);
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        initProcess();
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

    /**
     * 发起路线规划搜索示例
     */
    public void initProcess() {
        // 重置浏览节点的路线数据
        route = null;
        mBaidumap.clear();
        // 处理搜索按钮响应
        // 设置起终点信息，对于tranist search 来说，城市名无意义
        PlanNode stNode = PlanNode.withCityNameAndPlaceName("文山", startNodeStr);
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("昆明", endNodeStr);
        mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode).to(enNode));
        nowSearchType = 1;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult result) {

    }


    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(DemoMainActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//            if (result.getRouteLines().size() > 1) {
//                nowResultdrive = result;
//                if (!hasShownDialogue) {
//                    MyTransitDlg myTransitDlg = new MyTransitDlg(DemoMainActivity.this,
//                            result.getRouteLines(),
//                            RouteLineAdapter.Type.DRIVING_ROUTE);
//                    myTransitDlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                        @Override
//                        public void onDismiss(DialogInterface dialog) {
//                            hasShownDialogue = false;
//                        }
//                    });
//                    myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
//                        public void onItemClick(int position) {
//                            route = nowResultdrive.getRouteLines().get(position);
//                            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
//                            mBaidumap.setOnMarkerClickListener(overlay);
//                            routeOverlay = overlay;
//                            overlay.setData(nowResultdrive.getRouteLines().get(position));
//                            overlay.addToMap();
//                            overlay.zoomToSpan();
//                        }
//
//                    });
//                    myTransitDlg.show();
//                    hasShownDialogue = true;
//                }
//            } else if (result.getRouteLines().size() == 1) {
            route = result.getRouteLines().get(0);
            DrivingRouteOverlay overlay = new DemoMainActivity.MyDrivingRouteOverlay(mBaidumap);
            routeOverlay = overlay;
            mBaidumap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
//            } else {
//                Log.d("route result", "结果数<0");
//                return;
//            }

        }

        dissmissProgressDialog();
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult result) {

    }

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    @Override
    public void onMapClick(LatLng point) {
        mBaidumap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi poi) {
        return false;
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mSearch != null) {
            mSearch.destroy();
        }
        mMapView.onDestroy();
        super.onDestroy();
    }

    // 响应DLg中的List item 点击
    interface OnItemInDlgClickListener {
        public void onItemClick(int position);
    }

    // 供路线选择的Dialog
    class MyTransitDlg extends Dialog {

        private List<? extends RouteLine> mtransitRouteLines;
        private ListView transitRouteList;
        private RouteLineAdapter mTransitAdapter;

        DemoMainActivity.OnItemInDlgClickListener onItemInDlgClickListener;

        public MyTransitDlg(Context context, int theme) {
            super(context, theme);
        }

        public MyTransitDlg(Context context, List<? extends RouteLine> transitRouteLines, RouteLineAdapter.Type
                type) {
            this(context, 0);
            mtransitRouteLines = transitRouteLines;
            mTransitAdapter = new RouteLineAdapter(context, mtransitRouteLines, type);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        @Override
        public void setOnDismissListener(OnDismissListener listener) {
            super.setOnDismissListener(listener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_transit_dialog);

            transitRouteList = (ListView) findViewById(R.id.transitList);
            transitRouteList.setAdapter(mTransitAdapter);

            transitRouteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onItemInDlgClickListener.onItemClick(position);
                    dismiss();
                    hasShownDialogue = false;
                }
            });
        }

        public void setOnItemInDlgClickLinster(DemoMainActivity.OnItemInDlgClickListener itemListener) {
            onItemInDlgClickListener = itemListener;
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
