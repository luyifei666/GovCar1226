package com.clfsjkj.govcar.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clfsjkj.govcar.ApplyCarActivity;
import com.clfsjkj.govcar.ApplyRecordActivity;
import com.clfsjkj.govcar.DriverActivity;
import com.clfsjkj.govcar.R;
import com.clfsjkj.govcar.adapter.MsgContentFragmentAdapter;
import com.clfsjkj.govcar.base.BaseFragment;
import com.clfsjkj.govcar.index.DefaultItemCallback;
import com.clfsjkj.govcar.index.DefaultItemTouchHelper;
import com.clfsjkj.govcar.index.FunctionAdapter;
import com.clfsjkj.govcar.index.FunctionBlockAdapter;
import com.clfsjkj.govcar.index.FunctionItem;
import com.clfsjkj.govcar.index.OnItemClickListener;
import com.clfsjkj.govcar.index.SFUtils;
import com.clfsjkj.govcar.index.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 通讯录
 * <p>展示居中位置的tab页卡</p>
 */
public class IndexFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView recyclerViewExist, recyclerViewAll;

    private HorizontalScrollView horizonLScrollView;
    private RadioGroup rg_tab;

    private FunctionBlockAdapter blockAdapter;
    private FunctionAdapter functionAdapter;
    private GridLayoutManager gridManager;

    private List<String> scrollTab = new ArrayList<>();

    private int itemWidth = 0;
    private int lastRow = 0;
    private boolean isMove = false;//滑动状态
    private int scrollPosition = 0;
    private String currentTab;//当前的标签
    private int tabWidth = 0;//标签宽度


    private List<FunctionItem> allData;
    private List<FunctionItem> selData;
    private SFUtils sfUtils;
    private static final int MAX_COUNT = 14;
    private boolean isDrag = false;
    private Context mContext;
    private RelativeLayout title;
    private TextView submit;
    private boolean isEdit = false;
    private Intent it;
    private MsgContentFragmentAdapter adapter;
    private List<String> names;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container, false);
//        ButterKnife.bind(this, view);
        init(view);
        addListener(view);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (submit.getText().equals("编辑")) {
                    submit.setText("保存");
                    isEdit = true;
                    blockAdapter.setEdit(isEdit);
                    functionAdapter.setEdit(isEdit);
                } else if (submit.getText().equals("保存")) {
                    submit.setText("编辑");
                    isEdit = false;
                    blockAdapter.setEdit(isEdit);
                    functionAdapter.setEdit(isEdit);
                }
            }
        });
        return view;
    }

    public void init(View view) {
        title = (RelativeLayout) view.findViewById(R.id.title);
        submit = view.findViewById(R.id.submit);
        submit.setText("编辑");
        recyclerViewExist = (RecyclerView) view.findViewById(R.id.recyclerViewExist);
        horizonLScrollView = (HorizontalScrollView) view.findViewById(R.id.horizonLScrollView);
//        horizonLScrollView.setVisibility(View.GONE);
        rg_tab = (RadioGroup) view.findViewById(R.id.rg_tab);
        recyclerViewAll = (RecyclerView) view.findViewById(R.id.recyclerViewAll);
        sfUtils = new SFUtils(mContext);
        allData = sfUtils.getAllFunctionWithState();//修改为通过网络获取数组，而后缓存到本地
        selData = sfUtils.getSelectFunctionItem();//修改为通过网络获取数组，而后缓存到本地

        blockAdapter = new FunctionBlockAdapter(mContext, selData);
        recyclerViewExist.setLayoutManager(new GridLayoutManager(mContext, 4));
        recyclerViewExist.setAdapter(blockAdapter);
        recyclerViewExist.addItemDecoration(new SpaceItemDecoration(4, dip2px(mContext, 5)));

        DefaultItemCallback callback = new DefaultItemCallback(blockAdapter);
        DefaultItemTouchHelper helper = new DefaultItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerViewExist);

        gridManager = new GridLayoutManager(mContext, 4);
        gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                FunctionItem fi = allData.get(position);
                return fi.isTitle ? 4 : 1;
            }
        });

        functionAdapter = new FunctionAdapter(mContext, allData);
        recyclerViewAll.setLayoutManager(gridManager);
        recyclerViewAll.setAdapter(functionAdapter);
        SpaceItemDecoration spaceDecoration = new SpaceItemDecoration(4, dip2px(mContext, 5));
        recyclerViewAll.addItemDecoration(spaceDecoration);

        itemWidth = getAtyWidth(mContext) / 4 + dip2px(mContext, 2);

        resetEditHeight(selData.size());


        initTab();
    }


    public int getAtyWidth(Context context) {
        try {
            DisplayMetrics mDm = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay()
                    .getMetrics(mDm);
            return mDm.widthPixels;
        } catch (Exception e) {
            return 0;
        }
    }

    public void addListener(View view) {
        view.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sfUtils.saveSelectFunctionItem(selData);
                sfUtils.saveAllFunctionWithState(allData);
            }
        });
        functionAdapter.setOnItemAddListener(new FunctionAdapter.OnItemAddListener() {
            @Override
            public boolean add(FunctionItem item) {
                if (selData != null && selData.size() < MAX_COUNT) {   // 更新选择列表，所有列表已在内部进行更新
                    try {
                        selData.add(item);
                        resetEditHeight(selData.size());
                        blockAdapter.notifyDataSetChanged();
                        item.isSelect = true;
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                } else {
                    Toast.makeText(mContext, "选中的模块不能超过" + MAX_COUNT + "个", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });


//----------------------------------------------------------自己加的点击事件------------------------------------------------------------------
        functionAdapter.setOnItemClickLitener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position % 2) {
                    case 0:
                        Toast.makeText(mContext, "functionAdapter position = " + position, Toast.LENGTH_SHORT).show();
                        it = new Intent(mContext, ApplyCarActivity.class);
                        startActivity(it);
                        break;
                    case 1:
                        it = new Intent(mContext, ApplyRecordActivity.class);
                        startActivity(it);
                        break;
                    default:
                        break;
                }
            }
        });

        blockAdapter.setOnItemClickLitener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(mContext, "blockAdapter position = " + position, Toast.LENGTH_SHORT).show();
                it = new Intent(mContext, DriverActivity.class);
                startActivity(it);
            }
        });
//----------------------------------------------------------自己加的点击事件------------------------------------------------------------------

        blockAdapter.setOnItemRemoveListener(new FunctionBlockAdapter.OnItemRemoveListener() {
            @Override
            public void remove(FunctionItem item) {
                // 更新所有列表，选择列表已在内部进行更新
                try {
                    if (item != null && item.name != null) {
                        for (int i = 0; i < allData.size(); i++) {
                            FunctionItem data = allData.get(i);
                            if (data != null && data.name != null) {
                                if (item.name.equals(data.name)) {
                                    data.isSelect = false;
                                    break;
                                }
                            }
                        }
                        functionAdapter.notifyDataSetChanged();
                    }
                    resetEditHeight(selData.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        recyclerViewAll.addOnScrollListener(onScrollListener);

    }

    @Override
    public void onClick(View v) {
    }

    private void initTab() {
        try {
            List<FunctionItem> tabs = sfUtils.getTabNames();


            if (tabs != null && tabs.size() > 0) {
                currentTab = tabs.get(0).name;
                int padding = dip2px(mContext, 10);
                int size = tabs.size();
                for (int i = 0; i < size; i++) {
                    FunctionItem item = tabs.get(i);
                    if (item.isTitle) {
                        scrollTab.add(item.name);
                        RadioButton rb = new RadioButton(mContext);
                        rb.setPadding(padding, 0, padding, 0);
                        rb.setButtonDrawable(null);
                        rb.setGravity(Gravity.CENTER);
                        rb.setText(item.name);
                        rb.setTag(item.subItemCount);
                        Log.e("aaa", "item.subItemCount = " + item.subItemCount);
                        rb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        try {
                            rb.setTextColor(getResources().getColorStateList(R.color.bg_block_text));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        rb.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.bg_block_tab));
                        rb.setOnCheckedChangeListener(onCheckedChangeListener);
                        rg_tab.addView(rb);
                    }
                }
                ((RadioButton) rg_tab.getChildAt(0)).setChecked(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            try {
                int position = (int) buttonView.getTag();
                String text = buttonView.getText().toString();
                if (!currentTab.equals(text) && isChecked) {
                    currentTab = text;
                    moveToPosition(position);
                    Log.e("aaa", "position = " + position);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private void resetEditHeight(int size) {
        try {
            if (size == 0) {
                size = 1;
            }
            int row = size / 4 + (size % 4 > 0 ? 1 : 0);
            if (row <= 0)
                row = 1;
            if (lastRow != row) {
                lastRow = row;
                ViewGroup.LayoutParams params = recyclerViewExist.getLayoutParams();
                params.height = itemWidth * row;
                recyclerViewExist.setLayoutParams(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void moveToPosition(int position) {
        int first = gridManager.findFirstVisibleItemPosition();
        int end = gridManager.findLastVisibleItemPosition();
        if (first == -1 || end == -1)
            return;
        if (position <= first) {      //移动到前面
            gridManager.scrollToPosition(position);
            Log.e("aaa", "移动到前面 position = " + position);
        } else if (position >= end) {      //移动到后面
            isMove = true;
            scrollPosition = position;
            gridManager.smoothScrollToPosition(recyclerViewAll, null, position);
            Log.e("aaa", "移动到前面 position = " + position);
        } else {//中间部分
            int n = position - gridManager.findFirstVisibleItemPosition();
            if (n > 0 && n < allData.size()) {
                int top = gridManager.findViewByPosition(position).getTop();
                recyclerViewAll.scrollBy(0, top);
                Log.e("aaa", "移动到前面 position = " + position);
            }
        }
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.e("aaa", "279  onScrollStateChanged");
            try {
                if (isMove && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isMove = false;
                    Log.e("aaa", "newState = " + newState);
                    View view = gridManager.findViewByPosition(scrollPosition);
                    if (view != null) {
                        int top = (int) view.getTop();
                        recyclerView.scrollBy(0, top);
                    }
                }
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    isDrag = true;
                } else {
                    isDrag = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (isDrag) {  //拖动过程中
                int position = gridManager.findFirstVisibleItemPosition();
                if (position > 0) {
                    for (int i = 0; i < position + 1; i++) {
                        if (allData.get(i).isTitle) {
                            currentTab = allData.get(i).name;
                        }
                    }
                    scrollTab(currentTab);
                }
            }
        }
    };

    private void scrollTab(String newTab) {
        try {
            int position = scrollTab.indexOf(currentTab);
            int targetPosition = scrollTab.indexOf(newTab);
            currentTab = newTab;
            if (targetPosition != -1) {
                int x = (targetPosition - position) * getTabWidth();
                RadioButton radioButton = ((RadioButton) rg_tab.getChildAt(targetPosition));
                radioButton.setOnCheckedChangeListener(null);
                radioButton.setChecked(true);
                radioButton.setOnCheckedChangeListener(onCheckedChangeListener);
                horizonLScrollView.scrollBy(x, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getTabWidth() {
        if (tabWidth == 0) {
            if (rg_tab != null && rg_tab.getChildCount() != 0) {
                tabWidth = rg_tab.getWidth() / rg_tab.getChildCount();
            }
        }
        return tabWidth;
    }

    public int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
