package com.clfsjkj.govcar.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.clfsjkj.govcar.FeedBackActivity;
import com.clfsjkj.govcar.MainApplication;
import com.clfsjkj.govcar.R;
import com.clfsjkj.govcar.TextSizeShowActivity;
import com.clfsjkj.govcar.base.BaseFragment;
import com.clfsjkj.govcar.customerview.MySettingItemView;
import com.clfsjkj.govcar.http.UpdateAppHttpUtil;
import com.clfsjkj.govcar.utils.RxActivityTool;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.vector.update_app.UpdateAppManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import q.rorbin.badgeview.QBadgeView;

import static android.app.Activity.RESULT_OK;

/**
 * 我
 */
public class MeFragment extends BaseFragment {

    private String mUpdateUrl1 = "https://raw.githubusercontent.com/WVector/AppUpdateDemo/master/json/json1.txt";
    Unbinder unbinder;
    @BindView(R.id.avatar)
    ImageView mAvatar;
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

    private void choosePicture() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_QQ_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                .isCamera(true)// 是否显示拍照按钮
                .enableCrop(true)// 是否裁剪
                .compress(true)// 是否压缩
                .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                .circleDimmedLayer(false)// 是否圆形裁剪
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .cropCompressQuality(90)// 裁剪压缩质量 默认100
                .rotateEnabled(true) // 裁剪是否可旋转图片
                .scaleEnabled(true)// 裁剪是否可放大缩小图片
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    //获取路径
                    if (localMedias != null && localMedias.size() > 0) {
                        Log.e("aaa", "onActivityResult:" + localMedias.toString());
                        LocalMedia localMedia = localMedias.get(0);
                        String compressPath = localMedia.getCompressPath();

                        //更新数据
//                        mUserCache.setAvatarLocal(compressPath);
                        //将地址保存至服务器

                        RequestOptions options = new RequestOptions();
                        options.placeholder(R.drawable.ic_launcher_background);
                        options.error(R.drawable.ic_launcher_background);
                        Glide.with(mContext).load(compressPath).apply(options).into(mAvatar);
                    }
                    break;
            }
        }
    }

    @OnClick({R.id.set_text_size, R.id.item_feedback, R.id.item_app_update, R.id.rl_item_avatar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_item_avatar:
                //跳转选择图片
                choosePicture();
                break;
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
