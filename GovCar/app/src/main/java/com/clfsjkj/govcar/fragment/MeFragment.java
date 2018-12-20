package com.clfsjkj.govcar.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clfsjkj.govcar.FeedBackActivity;
import com.clfsjkj.govcar.MainApplication;
import com.clfsjkj.govcar.R;
import com.clfsjkj.govcar.TextSizeShowActivity;
import com.clfsjkj.govcar.base.BaseFragment;
import com.clfsjkj.govcar.customerview.MySettingItemView;
import com.clfsjkj.govcar.http.UpdateAppHttpUtil;
import com.clfsjkj.govcar.utils.RxActivityTool;
import com.vector.update_app.UpdateAppManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 我
 */
public class MeFragment extends BaseFragment {

    private String mUpdateUrl1 = "https://raw.githubusercontent.com/WVector/AppUpdateDemo/master/json/json1.txt";
    Unbinder unbinder;
    @BindView(R.id.set_text_size)
    MySettingItemView setTextSize;
    @BindView(R.id.item_feedback)
    MySettingItemView itemFeedback;
    @BindView(R.id.item_app_update)
    MySettingItemView itemAppUpdate;
    private Context mContext;
    private RelativeLayout title;
    private TextView submit;

    public MeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        title = (RelativeLayout) view.findViewById(R.id.title);
        submit = view.findViewById(R.id.submit);
        submit.setVisibility(View.GONE);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        initTextSizeInfo();
        return view;
    }

    private void initTextSizeInfo() {
        float textSize = MainApplication.getMyInstance().getFontScale();
        switch (textSize + "") {
            case "1.1":
                setTextSize.setRightText("标准");
                break;
            case "1.0":
                setTextSize.setRightText("较小");
                break;
            case "1.2":
                setTextSize.setRightText("较大");
                break;
            case "1.3":
                setTextSize.setRightText("大");
                break;
            case "1.4":
                setTextSize.setRightText("很大");
                break;
            case "1.5":
                setTextSize.setRightText("超大(可能导致布局错乱)");
                break;
        }

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
                .setActivity(getActivity())
                //更新地址
                .setUpdateUrl(mUpdateUrl1)
                //实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil())
                .build()
                .update();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.set_text_size, R.id.item_feedback, R.id.item_app_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.set_text_size:
                RxActivityTool.skipActivity(mContext, TextSizeShowActivity.class);
                break;
            case R.id.item_feedback:
                RxActivityTool.skipActivity(mContext, FeedBackActivity.class);
                break;
            case R.id.item_app_update:
                checkVersion();
                break;
        }
    }
}
