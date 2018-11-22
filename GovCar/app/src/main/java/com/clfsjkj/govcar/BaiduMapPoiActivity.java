package com.clfsjkj.govcar;

import android.Manifest;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiDetailInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.clfsjkj.govcar.base.BaseActivity;
import com.clfsjkj.govcar.overlayutil.PoiOverlay;
import com.clfsjkj.govcar.permission.DefaultRationale;
import com.clfsjkj.govcar.permission.PermissionSetting;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaiduMapPoiActivity extends BaseActivity implements SensorEventListener, OnGetPoiSearchResultListener, OnGetSuggestionResultListener {

    @BindView(R.id.myloc)
    ImageView mMyloc;
    @BindView(R.id.btn_back)
    Button mBtnBack;
    @BindView(R.id.btn_ok)
    TextView mBtnOk;
    @BindView(R.id.titile)
    TextView mTitile;

    @BindView(R.id.searchkey)
    AutoCompleteTextView mSearchkey;
    private ArrayAdapter<String> sugAdapter = null;
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    //    @BindView(R.id.toolbar)
//    Toolbar mToolbar;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;//当前定位纬度
    private double mCurrentLon = 0.0;//当前定位经度
    private double mSearchLat = 0.0;//搜索地点纬度
    private double mSearchLon = 0.0;//搜索地点经度
    private String mProvince;//省份
    private String mCity;//城市
    private float mCurrentAccracy;
    private BitmapDescriptor myIconLocation1;//图标1，当前位置的箭头图标
    private MyOrientationListener myOrientationListener;//方向感应器类对象

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private float direction;
    private int searchType = 0;  // 搜索的类型，区域搜索3,周边搜索2,城市内搜索1
    private int loadIndex = 0;
    private LatLngBounds searchBound;
    private String titleStr;
    private int mRequestCode = 0;
    private String addr;
    private String addrDes;
    private String mAddrStr;
    public static final int REQUEST_CODE_START = 999;
    public static final int REQUEST_CODE_PATH = 888;
    public static final int REQUEST_CODE_END = 777;

    /**
     * 动态权限
     */
    private Rationale mRationale;
    private PermissionSetting mSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_map_poi);
        ButterKnife.bind(this);
        //动态权限
        mRationale = new DefaultRationale();
        mSetting = new PermissionSetting(this);
        permission();
        initMyToolBar();
        setStatusBarFullTransparent();
        setFitSystemWindow(true);
        Intent intent = getIntent();
        titleStr = intent.getStringExtra("TITLE");
        mTitile.setText(titleStr);
        mRequestCode = Integer.valueOf(intent.getStringExtra("REQUEST_CODE"));
        Log.e("aaa", "titleStr = " + titleStr + " , mRequestCode = " + mRequestCode);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 实时路况图图层
//        mBaiduMap.setTrafficEnabled(true);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        //初始化图标,BitmapDescriptorFactory是bitmap 描述信息工厂类.
        myIconLocation1 = BitmapDescriptorFactory.fromResource(R.drawable.location_marker);
        //配置定位图层显示方式,三个参数的构造器
        MyLocationConfiguration configuration
                = new MyLocationConfiguration(mCurrentMode, true, myIconLocation1);
        //设置定位图层配置信息，只有先允许定位图层后设置定位图层配置信息才会生效，参见 setMyLocationEnabled(boolean)
        mBaiduMap.setMyLocationConfigeration(configuration);
        myOrientationListener = new MyOrientationListener(mContext);
        //通过接口回调来实现实时方向的改变
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentDirection = (int) x;
            }
        });

        sugAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        mSearchkey.setAdapter(sugAdapter);
        mSearchkey.setThreshold(1);

        //单击地图的监听
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
                                            //地图单击事件回调方法
                                            @Override
                                            public void onMapClick(LatLng latLng) {
                                                Log.e("aaa", "点击到地图上了！纬度" + latLng.latitude + "经度" + latLng.longitude);
                                            }

                                            //Poi 单击事件回调方法，比如点击到地图上面的商店，公交车站，地铁站等等公共场所
                                            @Override
                                            public boolean onMapPoiClick(MapPoi mapPoi) {
                                                String POIName = mapPoi.getName();//POI点名称
                                                LatLng POIPosition = mapPoi.getPosition();//POI点坐标
                                                Log.e("aaa", "点击到地图上的POI物体了！名称：" + mapPoi.getName() + ",Uid:" + mapPoi.getUid());
                                                Log.e("aaa", "POIName：" + POIName + ",POIPosition:" + POIPosition);
                                                return true;
                                            }
                                        }

        );

        mMyloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationByLL(mCurrentLat, mCurrentLon);
            }
        });

        /* 当输入关键字变化时，动态更新建议列表 */
        mSearchkey.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length() <= 0) {
                    return;
                }

                try {
                    /* 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新 */
                    mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                            .keyword(cs.toString())
                            .city(mCity));
                    searchButtonProcess(cs.toString(), mCity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void searchButtonProcess(String keystr, String citystr) {

//        List<String> mList = new ArrayList<>();
//        mList.add("昆明市");
//        mList.add("大理白族自治州");
//        mList.add("楚雄彝族自治州");
//        mList.add("红河哈尼族彝族自治州");
//        mList.add("文山壮族苗族自治州");
//        mList.add("曲靖市");
//        mList.add("玉溪市");
//        mList.add("昭通市");
//        mList.add("保山市");
//        mList.add("丽江市");
//        mList.add("普洱市");
//        mList.add("临沧市");
//        mList.add("德宏傣族景颇族自治州");
//        mList.add("怒江傈僳族自治州");
//        mList.add("迪庆藏族自治州");
//        mList.add("西双版纳傣族自治州");
//
//        for (int i = 0; i < mList.size(); i++) {
//            searchType = 1;
//            mPoiSearch.searchInCity((new PoiCitySearchOption())
//                    .city(mList.get(i))
//                    .keyword(keystr)
//                    .pageNum(loadIndex)
//                    .scope(1));
//        }


//        searchType = 1;
//        mPoiSearch.searchInCity((new PoiCitySearchOption())
//                .city(citystr)
//                .keyword(keystr)
//                .pageNum(loadIndex)
//                .scope(1));

        LatLng southwest = new LatLng(86.290768, 18.103598);//将中国包起来
        LatLng northeast = new LatLng(136.625773, 53.545176);//将中国包起来
        searchBound = new LatLngBounds.Builder().include(southwest).include(northeast).build();
        mPoiSearch.searchInBound(new PoiBoundSearchOption()
                .bound(searchBound)
                .keyword(keystr)
                .scope(1));
    }

    /**
     * 对区域检索的范围进行绘制
     *
     * @param bounds 区域检索指定区域
     */
    public void showBound(LatLngBounds bounds) {
        BitmapDescriptor bdGround = BitmapDescriptorFactory.fromResource(R.drawable.ground_overlay);

        OverlayOptions ooGround = new GroundOverlayOptions()
                .positionFromBounds(bounds)
                .image(bdGround)
                .transparency(0.8f)
                .zIndex(1);

        mBaiduMap.addOverlay(ooGround);

        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(bounds.getCenter());
        mBaiduMap.setMapStatus(u);

        bdGround.recycle();
    }

    private void initMyToolBar() {

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (mSearchkey.getText().toString().trim().isEmpty()) {
                    intent.putExtra("lat", mCurrentLat + "");//纬度
                    intent.putExtra("lon", mCurrentLon + "");//经度
                    intent.putExtra("addr", addr);//地名
                    intent.putExtra("addrDes", addrDes);//地址
                } else {
                    intent.putExtra("lat", mSearchLat + "");//纬度
                    intent.putExtra("lon", mSearchLon + "");//经度
                    intent.putExtra("addr", addr);//地名
                    intent.putExtra("addrDes", addrDes);//地址
                }
                setResult(mRequestCode, intent);
                finish();
            }
        });


//        initToolBar(mToolbar, "POI搜索", R.drawable.gank_ic_back_white);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mSearchkey.getText().toString().isEmpty() || mRequestCode == 0) {
//                    finish();//返回
//                } else {
//                    Intent intent = new Intent();
//                    String location = mSearchkey.getText().toString().trim();
//                    intent.putExtra("location", location); //将计算的值回传回去
//                    setResult(mRequestCode);
//                }
//            }
//        });
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection)
                    .latitude(mCurrentLat)
                    .longitude(mCurrentLon)
                    .build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }

    private void requestPermission(String... permissions) {
        AndPermission.with(this)
                .permission(permissions)
                .rationale(mRationale)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
//                        Toast.makeText(BaiduMapPoiActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(BaiduMapPoiActivity.this, permissions)) {
                            mSetting.showSetting(permissions);
                        }
                    }
                })
                .start();
    }

    /**
     * 获取POI详情搜索结果，得到searchPoiDetail返回的搜索结果
     * V5.2.0版本之后，还方法废弃，使用{@link #onGetPoiDetailResult(PoiDetailSearchResult)}代替
     *
     * @param result POI详情检索结果
     */
    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BaiduMapPoiActivity.this, "抱歉，未找到结果396", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(BaiduMapPoiActivity.this,
                    "line 399 --- " + result.getName() + ": " + result.getAddress(),
                    Toast.LENGTH_SHORT).show();
            addr = result.getName();
            addrDes = result.getAddress();
            mSearchLat = result.location.latitude;
            mSearchLon = result.location.longitude;
            Log.e("aaa", "mSeachLat = " + mSearchLat + ",mSeachLon = " + mSearchLon);
            getLocationByLL(mSearchLat, mSearchLon);
            drawMarker(mSearchLat, mSearchLon);
        }
    }

    /*
     *根据经纬度前往
     */
    public void getLocationByLL(double la, double lg) {
        //地理坐标的数据结构
        LatLng latLng = new LatLng(la, lg);
        //描述地图状态将要发生的变化,通过当前经纬度来使地图显示到该位置
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(msu);
    }

    public void drawMarker(double la, double lg) {
        LatLng latLng = new LatLng(la, lg);
        // 构建Marker图标
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        // 构建MarkerOption，用于在地图上添加Marker.
        OverlayOptions options = new MarkerOptions().position(latLng).icon(bitmapDescriptor);
        // 在地图上添加Marker，并显示
        //mBaiduMap.addOverlay(options);
        Marker marker = (Marker) (mBaiduMap.addOverlay(options));

        //描述地图状态将要发生的变化,通过当前经纬度来使地图显示到该位置
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(msu);
    }

    /**
     * 获取POI搜索结果，包括searchInCity，searchNearby，searchInBound返回的搜索结果
     *
     * @param result Poi检索结果，包括城市检索，周边检索，区域检索
     */
    public void onGetPoiResult(PoiResult result) {
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(BaiduMapPoiActivity.this, "未找到结果339", Toast.LENGTH_SHORT).show();
            searchType = 3;
            return;
        }

        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mBaiduMap.clear();
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();

            switch (searchType) {
                case 3:
                    showBound(searchBound);
                    break;
                default:
                    break;
            }
            return;
        }

        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";

            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }

            strInfo += "找到结果";
            Toast.makeText(BaiduMapPoiActivity.this, strInfo, Toast.LENGTH_LONG).show();
        }
    }

    private class MyPoiOverlay extends PoiOverlay {
        MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(poi.uid));
            return true;
        }
    }

    /**
     * 获取POI详情搜索结果，得到searchPoiDetail返回的搜索结果
     * V5.2.0版本之后，还方法废弃，使用{@link #onGetPoiDetailResult(PoiDetailSearchResult)}代替
     */
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
        if (poiDetailSearchResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BaiduMapPoiActivity.this, "抱歉，未找到结果410", Toast.LENGTH_SHORT).show();
        } else {
            List<PoiDetailInfo> poiDetailInfoList = poiDetailSearchResult.getPoiDetailInfoList();
            if (null == poiDetailInfoList || poiDetailInfoList.isEmpty()) {
                Toast.makeText(BaiduMapPoiActivity.this, "抱歉，检索结果为空414", Toast.LENGTH_SHORT).show();
                return;
            }

            for (int i = 0; i < poiDetailInfoList.size(); i++) {
                PoiDetailInfo poiDetailInfo = poiDetailInfoList.get(i);
                if (null != poiDetailInfo) {
                    Toast.makeText(BaiduMapPoiActivity.this,
                            "line 484 :" + poiDetailInfo.getName() + ": " + poiDetailInfo.getAddress(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onGetSuggestionResult(final SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }

        List<String> suggest = new ArrayList<>();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null) {
                suggest.add(info.key);
            }
        }

        sugAdapter = new ArrayAdapter<>(BaiduMapPoiActivity.this, android.R.layout.simple_dropdown_item_1line,
                suggest);
        mSearchkey.setAdapter(sugAdapter);
        mSearchkey.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SuggestionResult.SuggestionInfo info = res.getAllSuggestions().get(position);
                //String uid = info.uid;
                mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())//去搜索该POI的详情->onGetPoiDetailResult(PoiDetailResult result)
                        .poiUid(info.uid));
            }
        });
        sugAdapter.notifyDataSetChanged();
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            try {
//                LocationClientOption option = new LocationClientOption();
//                option.setIsNeedAddress(true);
//                mLocClient.setLocOption(option);
                //当前设备位置所在的省
                mProvince = location.getProvince();
                //当前设备位置所在的市
                mCity = location.getCity();
                Log.e("aaa", "省 = " + mProvince + ",城市 = " + mCity);
                mAddrStr = location.getAddrStr();
                if (mRequestCode == REQUEST_CODE_START) {
                    mSearchkey.setHint(mAddrStr);
                } else if (mRequestCode == REQUEST_CODE_PATH) {
                    mSearchkey.setHint("请搜索途径地");
                } else if (mRequestCode == REQUEST_CODE_END) {
                    mSearchkey.setHint("请搜索目的地");
                }
                addr = mAddrStr;
                addrDes = location.getLocationDescribe();
                Log.e("aaa", "mAddrStr = " + mAddrStr);
                String mCountry = location.getCountry();
                Log.e("aaa", "mCountry = " + mCountry);
                String mDistrict = location.getDistrict();
                Log.e("aaa", "mDistrict = " + mDistrict);
                String mStreet = location.getStreet();
                Log.e("aaa", "mStreet = " + mStreet);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            Log.e("aaa", "mCurrentLat = " + mCurrentLat + ",mCurrentLon = " + mCurrentLon);
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
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
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        //停止定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocClient.stop();
        myOrientationListener.stop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        //开启定位，显示位置图标
//        mBaiduMap.setMyLocationEnabled(true);
//        if (!mLocClient.isStarted()) {
//            mLocClient.start();
//        }
        myOrientationListener.start();
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
