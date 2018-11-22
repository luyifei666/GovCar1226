package com.clfsjkj.govcar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clfsjkj.govcar.adapter.MainFragmentAdapter;
import com.clfsjkj.govcar.base.BaseActivity;
import com.clfsjkj.govcar.http.UpdateAppHttpUtil;
import com.clfsjkj.govcar.keepingappalive.receiver.ScreenReceiverUtil;
import com.clfsjkj.govcar.keepingappalive.service.DaemonService;
import com.clfsjkj.govcar.keepingappalive.service.PlayerMusicService;
import com.clfsjkj.govcar.keepingappalive.utils.Contants;
import com.clfsjkj.govcar.keepingappalive.utils.JobSchedulerManager;
import com.clfsjkj.govcar.keepingappalive.utils.ScreenManager;
import com.clfsjkj.govcar.permission.DefaultRationale;
import com.clfsjkj.govcar.permission.PermissionSetting;
import com.clfsjkj.govcar.utils.NotifyUtil;
import com.vector.update_app.UpdateAppManager;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主框架
 */
public class MainActivity extends BaseActivity {

    //    @BindView(R.id.toolbar)
//    Toolbar mToolbar;
    private String mUpdateUrl1 = "https://raw.githubusercontent.com/WVector/AppUpdateDemo/master/json/json1.txt";
    private Context context;
    private NotifyUtil notifyUtils;
    /**
     * 菜单标题
     */
    private final int[] TAB_TITLES = new int[]{R.string.menu_msg, R.string.menu_contact, R.string.menu_find, R.string.menu_me};
    /**
     * 菜单图标
     */
    private final int[] TAB_IMGS = new int[]{R.drawable.tab_main_msg_selector, R.drawable.tab_main_contact_selector, R.drawable.tab_main_find_selector
            , R.drawable.tab_main_me_selector};

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    /**
     * 页卡适配器
     */
    private PagerAdapter adapter;
    /**
     * 退出时间
     */
    private long exitTime;
    /**
     * 动态权限
     */
    private Rationale mRationale;
    private PermissionSetting mSetting;

    /**
     * keepingappalive
     */
    // 动态注册锁屏等广播
    private ScreenReceiverUtil mScreenListener;
    // 1像素Activity管理类
    private ScreenManager mScreenManager;
    // JobService，执行系统任务
    private JobSchedulerManager mJobManager;
    // 华为推送管理类
//    private HwPushManager mHwPushManager;
    private ScreenReceiverUtil.SreenStateListener mScreenListenerer = new ScreenReceiverUtil.SreenStateListener() {
        @Override
        public void onSreenOn() {
            // 亮屏，移除"1像素"
            mScreenManager.finishActivity();
        }

        @Override
        public void onSreenOff() {
            // 接到锁屏广播，将MainActivity切换到可见模式
            // "咕咚"、"乐动力"、"悦动圈"就是这么做滴
//            Intent intent = new Intent(MainActivity.this,MainActivity.class);
//            startActivity(intent);
            // 如果你觉得，直接跳出SportActivity很不爽
            // 那么，我们就制造个"1像素"惨案
            mScreenManager.startActivity();
        }

        @Override
        public void onUserPresent() {
            // 解锁，暂不用，保留
        }
    };
    private String TAG = "GovCar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        context = this;
        initMyToolBar();
        setStatusBarFullTransparent();
        setFitSystemWindow(true);
        // 初始化页卡
        initPager();
        setTabs(tabLayout, getLayoutInflater(), TAB_TITLES, TAB_IMGS);
        //动态权限demo
        mRationale = new DefaultRationale();
        mSetting = new PermissionSetting(this);
//        permission();
        //keepaingappaliva
        if (Contants.DEBUG)
            Log.d(TAG, "--->onCreate");
        // 1. 注册锁屏广播监听器
        mScreenListener = new ScreenReceiverUtil(this);
        mScreenManager = ScreenManager.getScreenManagerInstance(this);
        mScreenListener.setScreenReceiverListener(mScreenListenerer);
        // 2. 启动系统任务
        mJobManager = JobSchedulerManager.getJobSchedulerInstance(this);
        mJobManager.startJobScheduler();
        // 3. 启动前台Service
        startDaemonService();
        // 4. 启动播放音乐Service
//        startPlayMusicService();
        //APP版本更新检查
//        checkVersion();
    }

    private void initMyToolBar() {
//        initToolBar(mToolbar, "GovCar");
//        Button rightBtnSearch = (Button) mToolbar.findViewById(R.id.right_btn);
//        rightBtnSearch.setVisibility(View.VISIBLE);
//        rightBtnSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                startActivity(new Intent(MainActivity.this, SearchActivity.class));
//            }
//        });
    }

    private void checkVersion() {
//        1,有新版本
//        {
//            "update": "Yes",//有新版本
//                "new_version": "0.8.3",//新版本号
//                "apk_file_url": "https://raw.githubusercontent.com/WVector/AppUpdateDemo/master/apk/app-debug.apk", //apk下载地址
//                "update_log": "1，添加删除信用卡接口\r\n2，添加vip认证\r\n3，区分自定义消费，一个小时不限制。\r\n4，添加放弃任务接口，小时内不生成。\r\n5，消费任务手动生成。",//更新内容
//                "target_size": "5M",//apk大小
//                "new_md5":"A818AD325EACC199BC62C552A32C35F2",
//                "constraint": false//是否强制更新
//        }

//        2,没有新版本
//        {
//            "update": "No",//没有新版本
//        }


        new UpdateAppManager
                .Builder()
                //当前Activity
                .setActivity(this)
                //更新地址
                .setUpdateUrl(mUpdateUrl1)
                //实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil())
                .build()
                .update();

    }

    private void stopPlayMusicService() {
        Intent intent = new Intent(MainActivity.this, PlayerMusicService.class);
        stopService(intent);
    }

    private void startPlayMusicService() {
        Intent intent = new Intent(MainActivity.this, PlayerMusicService.class);
        startService(intent);
    }

    private void startDaemonService() {
        Intent intent = new Intent(MainActivity.this, DaemonService.class);
        startService(intent);
    }

    private void stopDaemonService() {
        Intent intent = new Intent(MainActivity.this, DaemonService.class);
        stopService(intent);
    }

    public void permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
//            AndPermission.with(this)
//                    .permission(Manifest.permission.RECORD_AUDIO)
//                    .rationale(mRationale)
//                    .onGranted(new Action() {
//                        @Override
//                        public void onAction(List<String> permissions) {
//                            Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .onDenied(new Action() {
//                        @Override
//                        public void onAction(@NonNull List<String> permissions) {
//                            Toast.makeText(MainActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
//                            if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
//                                mSetting.showSetting(permissions);
//                            }
//                        }
//                    })
//                    .start();

        }


    }

    private void requestPermission(String... permissions) {
        AndPermission.with(this)
                .permission(permissions)
                .rationale(mRationale)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
//                        Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
//                        Toast.makeText(MainActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
                        if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
                            mSetting.showSetting(permissions);
                        }
                    }
                })
                .start();
    }

    /**
     * 设置页卡显示效果
     *
     * @param tabLayout
     * @param inflater
     * @param tabTitlees
     * @param tabImgs
     */
    private void setTabs(TabLayout tabLayout, LayoutInflater inflater, int[] tabTitlees, int[] tabImgs) {
        for (int i = 0; i < tabImgs.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View view = inflater.inflate(R.layout.item_main_menu, null);
            // 使用自定义视图，目的是为了便于修改，也可使用自带的视图
            tab.setCustomView(view);

            TextView tvTitle = (TextView) view.findViewById(R.id.txt_tab);
            tvTitle.setText(tabTitlees[i]);
            ImageView imgTab = (ImageView) view.findViewById(R.id.img_tab);
            imgTab.setImageResource(tabImgs[i]);
            tabLayout.addTab(tab);
        }
    }

    private void initPager() {
        adapter = new MainFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // 关联切换
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 取消平滑切换
                viewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            // 重写键盘事件分发，onKeyDown方法某些情况下捕获不到，只能在这里写
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Snackbar snackbar = Snackbar.make(viewPager, "再按一次退出程序", Snackbar.LENGTH_SHORT);
                snackbar.getView().setBackgroundResource(R.color.colorPrimary);
                snackbar.show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


}
