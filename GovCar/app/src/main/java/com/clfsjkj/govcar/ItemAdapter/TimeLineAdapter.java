package com.clfsjkj.govcar.ItemAdapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.clfsjkj.govcar.MainApplication;
import com.clfsjkj.govcar.R;
import com.clfsjkj.govcar.bean.EnclosureBean;
import com.clfsjkj.govcar.bean.TimeLineBean;
import com.clfsjkj.govcar.utils.ToastUtils;
import com.kevin.photo_browse.main.GlideApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TimeLineAdapter extends BaseQuickAdapter<TimeLineBean, BaseViewHolder> {
    ArrayList<TimeLineBean> mTimeLineList;
    public TimeLineAdapter(int layoutResId, @Nullable ArrayList<TimeLineBean> mTimeLineList) {
        super(layoutResId, mTimeLineList);
        this.mTimeLineList = mTimeLineList;
    }

    @Override
    protected void convert(BaseViewHolder helper, TimeLineBean bean) {
        helper.setText(R.id.title, bean.getTitle());
        helper.setText(R.id.tv_time, bean.getTime());
        if (helper.getLayoutPosition() == (mTimeLineList.size() - 1)){
            //如果是最后一项，则隐藏timeline的线条
            helper.setVisible(R.id.view_2, false);
        }
    }

}
