package com.clfsjkj.govcar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.clfsjkj.govcar.adapter.SelectCarAdapter;
import com.clfsjkj.govcar.adapter.SelectDriverAdapter;
import com.clfsjkj.govcar.base.BaseActivity;
import com.clfsjkj.govcar.bean.CarAndDriverBean;
import com.clfsjkj.govcar.bean.DriverInfoBean;
import com.clfsjkj.govcar.bean.DriverInfoBean;
import com.clfsjkj.govcar.index.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectDriverActivity extends BaseActivity {
    //选择驾驶员页面

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private Context mContext;
    private boolean isShowBtnGroup;
    private String mTitle;
    private SelectDriverAdapter adapter;
    private List<CarAndDriverBean> data;
    private List<DriverInfoBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_driver);
        ButterKnife.bind(this);
        mContext = this;
        initMyToolBar();
        setStatusBarFullTransparent();
        setFitSystemWindow(true);
        initWidget();
//        initAdapter();
    }

    private void initMyToolBar() {
        initToolBar(mToolbar, "请选择驾驶员", R.drawable.gank_ic_back_white);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });
    }

    private void initWidget() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        adapter = new SelectDriverAdapter(mContext);
        initData();
        adapter.appendList(mList);
        recyclerview.setAdapter(adapter);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {
                adapter.getFilter().filter(sequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        adapter.setOnItemClickLitener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent it = new Intent(SelectDriverActivity.this,DispatchCarsActivity.class);
                //这里把选中的车和驾驶员传回派车页面
                startActivity(it);
                finish();
            }
        });
    }

    private void initData() {
        DriverInfoBean b;
        b = new DriverInfoBean();
        b.setDriverName("陆雄兵");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("张建平");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("严权");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("李昌春");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("兰进昌");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("沈绍勇");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("张廷德");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("杨建昌");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("武从安");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);

        b = new DriverInfoBean();
        b.setDriverName("张建平");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("严权");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("李昌春");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("兰进昌");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("沈绍勇");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("张廷德");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("杨建昌");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("武从安");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);

        b = new DriverInfoBean();
        b.setDriverName("张建平");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("严权");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("李昌春");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("兰进昌");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("沈绍勇");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("张廷德");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("杨建昌");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("武从安");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);

        b = new DriverInfoBean();
        b.setDriverName("张建平");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("严权");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("李昌春");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("兰进昌");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("沈绍勇");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("张廷德");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("杨建昌");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("武从安");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);

        b = new DriverInfoBean();
        b.setDriverName("张建平");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("严权");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("李昌春");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("兰进昌");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("沈绍勇");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("张廷德");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("杨建昌");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new DriverInfoBean();
        b.setDriverName("武从安");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
    }


    private void initAdapter() {
        data = new ArrayList<>();
        CarAndDriverBean bean;
        bean = new CarAndDriverBean();
        bean.setDriverName("云A00001");
        bean.setDriverName("张三");
        data.add(bean);
        bean = new CarAndDriverBean();
        bean.setDriverName("云A00002");
        bean.setDriverName("张三2");
        data.add(bean);
        bean = new CarAndDriverBean();
        bean.setDriverName("云A00003");
        bean.setDriverName("张三3");
        data.add(bean);
        bean = new CarAndDriverBean();
        bean.setDriverName("云A00004");
        bean.setDriverName("张三4");
        data.add(bean);
        bean = new CarAndDriverBean();
        bean.setDriverName("云A00005");
        bean.setDriverName("张三5");
        data.add(bean);
        bean = new CarAndDriverBean();
        bean.setDriverName("云A00006");
        bean.setDriverName("张三6");
        data.add(bean);
    }
}
