package com.clfsjkj.govcar.ItemAdapter;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.clfsjkj.govcar.MainApplication;
import com.clfsjkj.govcar.R;
import com.clfsjkj.govcar.bean.EnclosureBean;
import com.kevin.photo_browse.main.GlideApp;

import java.io.File;
import java.util.List;

public class EnclosureAdapter extends BaseQuickAdapter<EnclosureBean, BaseViewHolder> {

    public EnclosureAdapter(int layoutResId, @Nullable List<EnclosureBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EnclosureBean item) {
//        helper.setText(R.id.text, item.getTitle());
//        helper.setImageResource(R.id.icon, item.getImageResource());
        mContext = MainApplication.getContext();
//        GlideApp.with(mContext).
//                load(item.getPicUrl())
////                .crossFade()
////                .error(R.mipmap.default_image)//加载错误的替换图
////                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存最终图片
//                .into((ImageView) helper.getView(R.id.iv_img));

        GlideApp.with(mContext)
                .load(item.getPicUrl())
                .placeholder(com.kevin.photo_browse.R.drawable.img_placeholder)
                .error(com.kevin.photo_browse.R.drawable.img_error)
                .into((ImageView) helper.getView(R.id.iv_img));
    }
}
