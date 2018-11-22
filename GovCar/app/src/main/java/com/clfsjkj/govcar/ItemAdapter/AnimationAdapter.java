package com.clfsjkj.govcar.ItemAdapter;

import android.support.annotation.Nullable;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.clfsjkj.govcar.MainApplication;
import com.clfsjkj.govcar.R;

import java.util.List;

public class AnimationAdapter extends BaseQuickAdapter<Status, BaseViewHolder> {

    public AnimationAdapter() {
        super( R.layout.layout_animation, DataServer.getSampleData(100));
    }

    @Override
    protected void convert(BaseViewHolder helper, Status item) {
        helper.addOnClickListener(R.id.img).addOnClickListener(R.id.tweetText).addOnClickListener(R.id.tweetName);
        mContext = MainApplication.getContext();
        Glide.with(mContext).
                load("https://ws1.sinaimg.cn/large/0065oQSqly1fw8wzdua6rj30sg0yc7gp.jpg").crossFade()
                .error(R.mipmap.m_img1)//加载错误的替换图
                .diskCacheStrategy(DiskCacheStrategy.RESULT)//缓存最终图片
                .into((ImageView) helper.getView(R.id.img));

//        switch (helper.getLayoutPosition()% 3){
//            case 0:
//                helper.setImageResource(R.id.img,R.mipmap.animation_img1);
//                break;
//            case 1:
//                helper.setImageResource(R.id.img,R.mipmap.animation_img1);
//                break;
//            case 2:
//                helper.setImageResource(R.id.img,R.mipmap.animation_img1);
//                break;
//        }
//        helper.setText(R.id.tweetName,"Hoteis in Rio de Janeiro");
        helper.setText(R.id.tweetName,"敬老万寿宴：昆明市新的社会……");
        String msg="\"He was one of Australia's most of distinguished artistes, renowned for his portraits\"";
//        ( (TextView)helper.getView(R.id.tweetText)).setText(SpannableStringUtils.getBuilder(msg).append("landscapes and nedes").setClickSpan(clickableSpan).create());
//        ( (TextView)helper.getView(R.id.tweetText)).setMovementMethod(LinkMovementMethod.getInstance());
    }
}
