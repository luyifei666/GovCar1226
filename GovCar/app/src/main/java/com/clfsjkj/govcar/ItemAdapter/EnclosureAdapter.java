package com.clfsjkj.govcar.ItemAdapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.clfsjkj.govcar.MainApplication;
import com.clfsjkj.govcar.R;
import com.clfsjkj.govcar.bean.EnclosureBean;
import com.kevin.photo_browse.main.GlideApp;
import java.util.List;

public class EnclosureAdapter extends BaseQuickAdapter<EnclosureBean, BaseViewHolder> {

    public EnclosureAdapter(int layoutResId, @Nullable List<EnclosureBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EnclosureBean item) {
        mContext = MainApplication.getContext();
        GlideApp.with(mContext)
                .load(item.getPicUrl())
                .thumbnail(0.1f)
                .placeholder(com.kevin.photo_browse.R.drawable.img_placeholder)
                .error(com.kevin.photo_browse.R.drawable.img_error)
                .into((ImageView) helper.getView(R.id.iv_img));
    }
}
