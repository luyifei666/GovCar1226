package com.clfsjkj.govcar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.clfsjkj.govcar.adapter.SelectCarAdapter;
import com.clfsjkj.govcar.base.BaseActivity;
import com.clfsjkj.govcar.bean.CarAndDriverBean;
import com.clfsjkj.govcar.bean.CarInfoBean;
import com.clfsjkj.govcar.index.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectCarActivity extends BaseActivity {
    //需选择车辆页面

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private Context mContext;
    private boolean isShowBtnGroup;
    private String mTitle;
    private SelectCarAdapter adapter;
    private List<CarAndDriverBean> data;
    private List<CarInfoBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_car);
        ButterKnife.bind(this);
        mContext = this;
        initMyToolBar();
        setStatusBarFullTransparent();
        setFitSystemWindow(true);
        initWidget();
//        initAdapter();
    }

    private void initMyToolBar() {
        initToolBar(mToolbar, "请选择车辆", R.drawable.gank_ic_back_white);
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
        adapter = new SelectCarAdapter(mContext);
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
//                Toast.makeText(mContext,mList.get(position).toString(),Toast.LENGTH_SHORT).show();
//                Log.e("aaa",mList.get(position).toString());
                Intent it = new Intent(SelectCarActivity.this,SelectDriverActivity.class);
                it.putExtra("drvier","driver");
                startActivity(it);
                finish();
            }
        });
    }

    private void initData() {
        CarInfoBean b;
        b = new CarInfoBean();
        b.setCarNum("云E31568");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31568");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31568");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31360");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31360");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31360");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31568");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31568");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31360");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31360");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31998");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31360");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31998");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31998");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31998");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31998");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31360");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31998");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31360");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31568");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31360");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31360");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31360");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31998");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31360");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31568");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31360");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31360");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31568");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31360");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31998");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31998");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31566");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31530");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31530");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31320");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E32008");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31530");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31566");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31320");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31572");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31310");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31566");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31572");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31566");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31310");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31566");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31320");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E32008");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31566");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31530");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E32008");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31530");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31572");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31310");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31530");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31310");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("空闲");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31572");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("出车中");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31572");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("出车中");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E32008");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("出车中");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31310");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("出车中");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31310");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("出车中");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31572");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("出车中");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E32008");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("出车中");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31530");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("出车中");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31310");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("出车中");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31566");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("出车中");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31530");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("出车中");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31572");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("出车中");
        mList.add(b);
        b = new CarInfoBean();
        b.setCarNum("云E31530");
        b.setLastTime("2018-11-30 18:30");
        b.setStatus("出车中");
        mList.add(b);
//
//        mList.add("方贤锋");
//        mList.add("陈浩");
//        mList.add("朱建涛");
//        mList.add("赵宗荣");
//        mList.add("谢家辉");
//        mList.add("韦美璟");
//        mList.add("辛凤秋");
//        mList.add("赵凯贤");
//        mList.add("刘文超");
//        mList.add("伙邦立");
//        mList.add("刘泽柱");
//        mList.add("陆国书");
//        mList.add("唐明昌");
//        mList.add("刘泽进");
//        mList.add("韦堂孝");
//        mList.add("吴国飞");
//        mList.add("杜程");
//        mList.add("庞斌");
//        mList.add("谭世勇");
//        mList.add("舒瑞");
//        mList.add("周朝祥");
//        mList.add("胡德海");
//        mList.add("苗厚祥");
//        mList.add("董家福");
//        mList.add("何应文");
//        mList.add("李勇");
//        mList.add("田锦超");
//        mList.add("胡昆伦");
//        mList.add("田云升");
//        mList.add("陆贵涛");
//        mList.add("梁昌林");
//        mList.add("袁章龙");
    }


    private void initAdapter() {
        data = new ArrayList<>();
        CarAndDriverBean bean;
        bean = new CarAndDriverBean();
        bean.setCarNum("云A00001");
        bean.setDriverName("张三");
        data.add(bean);
        bean = new CarAndDriverBean();
        bean.setCarNum("云A00002");
        bean.setDriverName("张三2");
        data.add(bean);
        bean = new CarAndDriverBean();
        bean.setCarNum("云A00003");
        bean.setDriverName("张三3");
        data.add(bean);
        bean = new CarAndDriverBean();
        bean.setCarNum("云A00004");
        bean.setDriverName("张三4");
        data.add(bean);
        bean = new CarAndDriverBean();
        bean.setCarNum("云A00005");
        bean.setDriverName("张三5");
        data.add(bean);
        bean = new CarAndDriverBean();
        bean.setCarNum("云A00006");
        bean.setDriverName("张三6");
        data.add(bean);
    }
}
