package com.clfsjkj.govcar.ItemAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clfsjkj.govcar.R;
import com.clfsjkj.govcar.bean.CarAndDriverBean;
import com.clfsjkj.govcar.index.FunctionItem;
import com.clfsjkj.govcar.index.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;


public class SelectedCarsAdapter extends RecyclerView.Adapter {

    private List<CarAndDriverBean> data = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    private Boolean isEdit = false;

    public SelectedCarsAdapter(Context context, @NonNull List<CarAndDriverBean> data,Boolean isEdit) {
        this.context = context;
        this.isEdit = isEdit;
        if (data != null) {
            this.data = data;
        }
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        holder = new SelectedViewHolder(inflater.inflate(R.layout.item_car_driver, parent, false));
        return holder;
    }

    //----------------------------------------------------------自己加的点击事件------------------------------------------------------------------
    private OnItemClickListener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void setEdit(Boolean isEdit) {
        this.isEdit = isEdit;
        notifyDataSetChanged();
    }
//----------------------------------------------------------自己加的点击事件------------------------------------------------------------------

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        final int index = position;
        SelectedViewHolder holder = (SelectedViewHolder) viewHolder;

        if (isEdit){
            holder.btn.setVisibility(View.VISIBLE);
            holder.btn.setImageResource(R.drawable.ic_block_delete);
        }else {
            holder.btn.setVisibility(View.GONE);
        }
        holder.carNum.setText(data.get(position).getCarNum());
        holder.driverName.setText(data.get(position).getDriverName());
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除这个元素在刷新
                data.remove(position);
                notifyDataSetChanged();
            }
        });


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

    public void setImage(String url, ImageView iv) {
        try {
            int rid = context.getResources().getIdentifier(url, "drawable", context.getPackageName());
            iv.setImageResource(rid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class TitleViewHolder extends RecyclerView.ViewHolder {

        private TextView text;

        public TitleViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }

    private class SelectedViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv, btn;
        private TextView carNum,driverName;

        public SelectedViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            carNum = (TextView) itemView.findViewById(R.id.carNum);
            driverName = (TextView) itemView.findViewById(R.id.driverName);
            btn = (ImageView) itemView.findViewById(R.id.btn);
        }
    }

    public interface OnItemAddListener {
        boolean add(FunctionItem item);
    }

    private OnItemAddListener listener;

    public void setOnItemAddListener(OnItemAddListener listener) {
        this.listener = listener;
    }


}
