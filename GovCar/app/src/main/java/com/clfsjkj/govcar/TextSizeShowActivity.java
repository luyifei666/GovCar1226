package com.clfsjkj.govcar;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clfsjkj.govcar.alltextsize.MessageSocket;
import com.clfsjkj.govcar.alltextsize.RxBus;
import com.clfsjkj.govcar.base.BaseActivity;
import com.clfsjkj.govcar.customerview.FontSliderBar;
import com.clfsjkj.govcar.utils.DisplayUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by zsj on 2017/9/11.
 * 字体设置展示
 */

public class TextSizeShowActivity extends BaseActivity {
    @BindView(R.id.fontSliderBar)
    FontSliderBar fontSliderBar;
    @BindView(R.id.tv_chatcontent1)
    TextView tvContent1;
    @BindView(R.id.tv_chatcontent)
    TextView tvContent2;
    @BindView(R.id.tv_chatcontent3)
    TextView tvContent3;
    @BindView(R.id.iv_userhead)
    ImageView ivUserhead;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private float textsize1, textsize2, textsize3;
    private float textSizef;//缩放比例
    private int currentIndex;
    private boolean isClickable = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textsizeshow);
        ButterKnife.bind(this);
        initMyToolBar();
        setStatusBarFullTransparent();
        setFitSystemWindow(true);
        initData();
    }
    private void initMyToolBar() {
        initToolBar(mToolbar, "字体设置", R.drawable.gank_ic_back_white);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fontSliderBar.getCurrentIndex() != currentIndex) {
                    if (isClickable) {
                        isClickable = false;
                        refresh();
                    }
                } else {
                    finish();
                }
            }
        });
    }

    private void initData() {
        currentIndex = MainApplication.getMyInstance().getPreferencesHelper().getValueInt("currentIndex", 1);
        textSizef = 1 + currentIndex * 0.1f;
        textsize1 = tvContent1.getTextSize() / textSizef;
        textsize2 = tvContent2.getTextSize() / textSizef;
        textsize3 = tvContent3.getTextSize() / textSizef;
        fontSliderBar.setTickCount(6).setTickHeight(DisplayUtils.convertDip2Px(TextSizeShowActivity.this, 15)).setBarColor(Color.GRAY)
                .setTextColor(Color.BLACK).setTextPadding(DisplayUtils.convertDip2Px(TextSizeShowActivity.this, 10)).setTextSize(DisplayUtils.convertDip2Px(TextSizeShowActivity.this, 14))
                .setThumbRadius(DisplayUtils.convertDip2Px(TextSizeShowActivity.this, 10)).setThumbColorNormal(Color.GRAY).setThumbColorPressed(Color.GRAY)
                .setOnSliderBarChangeListener(new FontSliderBar.OnSliderBarChangeListener() {
                    @Override
                    public void onIndexChanged(FontSliderBar rangeBar, int index) {
                        if(index>5){
                            return;
                        }
                        index = index - 1;
                        float textSizef = 1 + index * 0.1f;
                        setTextSize(textSizef);
                    }
                }).setThumbIndex(currentIndex).withAnimation(false).applay();
    }

    private void setTextSize(float textSize) {
        //改变当前页面的字体大小
        tvContent1.setTextSize(DisplayUtils.px2sp(TextSizeShowActivity.this, textsize1 * textSize));
        tvContent2.setTextSize(DisplayUtils.px2sp(TextSizeShowActivity.this, textsize2 * textSize));
        tvContent3.setTextSize(DisplayUtils.px2sp(TextSizeShowActivity.this, textsize3 * textSize));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (currentIndex != fontSliderBar.getCurrentIndex()) {
                if (isClickable) {
                    isClickable = false;
                    refresh();
                }
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void refresh() {
        //存储标尺的下标
        MainApplication.getMyInstance().getPreferencesHelper().setValue("currentIndex", fontSliderBar.getCurrentIndex());
        //通知主页面重启
        RxBus.getInstance().post(MainActivity.class.getSimpleName(), new MessageSocket(99, null, null, null));
        //重启mainActivity
        RxBus.getInstance().post(MainActivity.class.getSimpleName(), new MessageSocket(99, null, null, null));
//        showMyDialog();
        //2s后关闭  延迟执行任务 重启完主页
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                hideMyDialog();
                finish();
            }
        }, 200);
    }


}
