package com.clfsjkj.govcar.ItemAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.clfsjkj.govcar.CostQueryActivity;
import com.clfsjkj.govcar.R;

public class MyExtendableListViewAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    public String[] groupString = {"2018年11月", "2018年10月", "2018年9月", "2018年8月", "2018年7月", "2018年6月", "2018年5月", "2018年4月", "2018年3月", "2018年2月", "2018年1月", "2017年12月", "20178年11月", "2017年10月", "2017年91月", "2017年8月"};
    public String[][] childString = {
            {"孙尚香", "后羿", "马可波罗", "狄仁杰", "后羿", "马可波罗", "狄仁杰", "后羿", "马可波罗", "狄仁杰", "后羿", "马可波罗", "狄仁杰", "后羿", "马可波罗", "狄仁杰", "后羿", "马可波罗", "狄仁杰"},
            {"孙膑", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环"},
            {"张飞", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽"},
            {"诸葛亮", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将"},
            {"孙膑", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环"},
            {"张飞", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽"},
            {"诸葛亮", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将"},
            {"孙膑", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环"},
            {"张飞", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽"},
            {"诸葛亮", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将"},
            {"孙膑", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环"},
            {"张飞", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽"},
            {"诸葛亮", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将"},
            {"孙膑", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环"},
            {"张飞", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽"},
            {"诸葛亮", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将"},
            {"孙膑", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环", "蔡文姬", "鬼谷子", "杨玉环"},
            {"张飞", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽", "廉颇", "牛魔", "项羽"},
            {"诸葛亮", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将", "王昭君", "安琪拉", "干将"}

    };

    public MyExtendableListViewAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    // 获取分组的个数
    public int getGroupCount() {
        return groupString.length;
    }

    //获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return childString[groupPosition].length;
    }

    //获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return groupString[groupPosition];
    }

    //获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childString[groupPosition][childPosition];
    }

    //获取指定分组的ID, 这个ID必须是唯一的
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //获取子选项的ID, 这个ID必须是唯一的
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 获取显示指定组的视图对象
     *
     * @param groupPosition 组位置
     * @param isExpanded    该组是展开状态还是伸缩状态
     * @param convertView   重用已有的视图对象
     * @param parent        返回的视图对象始终依附于的视图组
     */
// 获取显示指定分组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand_parent, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvTitle.setText(groupString[groupPosition]);
        return convertView;
    }

    /**
     * 获取一个视图对象，显示指定组中的指定子元素数据。
     *
     * @param groupPosition 组位置
     * @param childPosition 子元素位置
     * @param isLastChild   子元素是否处于组中的最后一个
     * @param convertView   重用已有的视图(View)对象
     * @param parent        返回的视图(View)对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
     * android.view.ViewGroup)
     */

    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand_child, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.question = (TextView) convertView.findViewById(R.id.question);
            convertView.setTag(childViewHolder);

        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext,CostQueryActivity.class);
                mContext.startActivity(it);
            }
        });
        return convertView;
    }

    //指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        TextView tvTitle;
    }

    static class ChildViewHolder {
        TextView question;

    }
}
