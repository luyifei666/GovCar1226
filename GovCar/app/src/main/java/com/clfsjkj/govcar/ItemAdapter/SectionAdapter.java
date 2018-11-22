package com.clfsjkj.govcar.ItemAdapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.clfsjkj.govcar.MainApplication;
import com.clfsjkj.govcar.R;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class SectionAdapter extends BaseSectionQuickAdapter<MySection, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param sectionHeadResId The section head layout id for each item
     * @param layoutResId      The layout resource id of each item.
     * @param data             A new list is created out of this one to avoid mutable list
     */

    public SectionAdapter(int layoutResId, int sectionHeadResId, List data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, final MySection item) {
        helper.setText(R.id.header, item.header);
        helper.setVisible(R.id.more, item.isMore());
        helper.addOnClickListener(R.id.more);
    }


    @Override
    protected void convert(BaseViewHolder helper, MySection item) {
        mContext = MainApplication.getContext();
        Video video = (Video) item.t;
        Glide.with(mContext).
                load(video.getImg()).crossFade()
                .error(R.mipmap.m_img1)//加载错误的替换图
                .diskCacheStrategy(DiskCacheStrategy.RESULT)//缓存最终图片
                .into((ImageView) helper.getView(R.id.iv));
        Log.e("aaa", "img = " + video.getImg().toString());
//        switch (helper.getLayoutPosition() % 2) {
//            case 0:
//                helper.setImageResource(R.id.iv,R.mipmap.m_img1);
//                break;
//            case 1:
//                helper.setImageResource(R.id.iv, R.mipmap.m_img2);
//                break;
//
//        }
        helper.setText(R.id.tv, video.getName());
    }
}
