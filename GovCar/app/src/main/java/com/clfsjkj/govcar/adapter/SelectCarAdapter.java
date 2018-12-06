package com.clfsjkj.govcar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.clfsjkj.govcar.R;
import com.clfsjkj.govcar.bean.CarInfoBean;
import com.clfsjkj.govcar.index.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class SelectCarAdapter extends RecyclerView.Adapter implements Filterable {
    private List<CarInfoBean> mSourceList = new ArrayList<>();//原数组
    private List<CarInfoBean> mFilterList = new ArrayList<>();//过滤后的数组
    private Context context;

    public SelectCarAdapter(Context context) {
        this.context = context;
    }

    public void appendList(List<CarInfoBean> list) {
        mSourceList = list;
        //这里需要初始化filterList
        mFilterList = list;
    }

    //----------------------------------------------------------自己加的点击事件------------------------------------------------------------------
    private OnItemClickListener mOnItemClickLitener;
    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
//----------------------------------------------------------自己加的点击事件------------------------------------------------------------------


    @Override
    public TitleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TitleHolder(LayoutInflater.from(context).inflate(R.layout.item_select_car, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        TitleHolder holder = (TitleHolder) viewHolder;
        //这里也是过滤后的list
        holder.tv.setText(mFilterList.get(position).getCarNum());
        holder.time.setText(mFilterList.get(position).getLastTime());
        holder.status.setText(mFilterList.get(position).getStatus());
//----------------------------------------------------------自己加的点击事件------------------------------------------------------------------
        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickLitener.onItemClick(view, position);
                }
            });
        }

//----------------------------------------------------------自己加的点击事件------------------------------------------------------------------
    }

    @Override
    public int getItemCount() {
        //注意这里需要是过滤后的list
        return mFilterList.size();
    }

    //重写getFilter()方法
    @Override
    public Filter getFilter() {
        return new Filter() {
            //执行过滤操作
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    //没有过滤的内容，则使用源数据
                    mFilterList = mSourceList;
                } else {
                    List<CarInfoBean> filteredList = new ArrayList<>();
                    for (CarInfoBean str : mSourceList) {
                        //这里根据需求，添加匹配规则
                        if (str.getCarNum().contains(charString)) {
                            filteredList.add(str);
                        }
                    }
                    mFilterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterList;
                return filterResults;
            }

            //把过滤后的值返回出来
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilterList = (ArrayList<CarInfoBean>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class TitleHolder extends RecyclerView.ViewHolder {
        TextView tv,time,status;

        public TitleHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.id_tv_test);
            time = (TextView) itemView.findViewById(R.id.time);
            status = (TextView) itemView.findViewById(R.id.status);
        }
    }


}
