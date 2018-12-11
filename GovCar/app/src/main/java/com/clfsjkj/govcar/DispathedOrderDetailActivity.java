package com.clfsjkj.govcar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.clfsjkj.govcar.ItemAdapter.EnclosureAdapter;
import com.clfsjkj.govcar.ItemAdapter.SelectedCarsAdapter;
import com.clfsjkj.govcar.ItemAdapter.TimeLineAdapter;
import com.clfsjkj.govcar.base.BaseActivity;
import com.clfsjkj.govcar.bean.CarAndDriverBean;
import com.clfsjkj.govcar.bean.EnclosureBean;
import com.clfsjkj.govcar.bean.TimeLineBean;
import com.clfsjkj.govcar.imageloader.GlideImageLoader;
import com.clfsjkj.govcar.utils.ToastUtils;
import com.kevin.photo_browse.ImageBrowseIntent;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.clfsjkj.govcar.MainApplication.maxImgCount;

public class DispathedOrderDetailActivity extends BaseActivity {

    private static final int PERMISSION_CALL_PHONE_CONTACT = 0x001;
    private static final int PERMISSION_CALL_PHONE_USERTEL = 0x002;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.order_num)
    TextView orderNum;
    @BindView(R.id.order_status)
    TextView orderStatus;
    @BindView(R.id.tv_car_use_contacts_tel)
    TextView tvCarUseContactsTel;
    @BindView(R.id.tv_car_user_tel)
    TextView tvCarUserTel;
    @BindView(R.id.btn_reject)
    Button btnReject;
    @BindView(R.id.btn_pass)
    Button btnPass;
    @BindView(R.id.btn_status)
    LinearLayout btnStatus;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.recyclerView_time_line)
    RecyclerView recyclerViewTimeLine;
    @BindView(R.id.recyclerView_selected)
    RecyclerView recyclerViewSelected;
    private Context mContext;
    private ArrayList<EnclosureBean> mDataList;
    private ArrayList<TimeLineBean> mTimeLineList;
    private boolean isShowBtnGroup;
    private SelectedCarsAdapter adapter;
    private List<CarAndDriverBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatched_order_detail);
        ButterKnife.bind(this);
        mContext = this;
        isShowBtnGroup = getIntent().getBooleanExtra("isShowBtnGroup", false);
        if (isShowBtnGroup) {
            btnStatus.setVisibility(View.VISIBLE);
        }
        initMyToolBar();
        setStatusBarFullTransparent();
        setFitSystemWindow(true);
        //最好放到 Application oncreate执行
        initImagePicker();
        initWidget();
        initData();
        initAdapter();
    }

    private void initData() {
        mDataList = new ArrayList<EnclosureBean>();
        EnclosureBean enclosureBean;
        enclosureBean = new EnclosureBean();
        enclosureBean.setPicName("One");
        enclosureBean.setPicUrl("https://ws1.sinaimg.cn/large/0065oQSqgy1fxd7vcz86nj30qo0ybqc1.jpg");
        mDataList.add(enclosureBean);
        enclosureBean = new EnclosureBean();
        enclosureBean.setPicName("Two");
        enclosureBean.setPicUrl("https://ws1.sinaimg.cn/large/0065oQSqgy1fwyf0wr8hhj30ie0nhq6p.jpg");
        mDataList.add(enclosureBean);
        enclosureBean = new EnclosureBean();
        enclosureBean.setPicName("Three");
        enclosureBean.setPicUrl("https://ws1.sinaimg.cn/large/0065oQSqgy1fwgzx8n1syj30sg15h7ew.jpg");
        mDataList.add(enclosureBean);
        enclosureBean = new EnclosureBean();
        enclosureBean.setPicName("Four");
        enclosureBean.setPicUrl("https://ws1.sinaimg.cn/large/0065oQSqly1fw8wzdua6rj30sg0yc7gp.jpg");
        mDataList.add(enclosureBean);

        mTimeLineList = new ArrayList<>();
        TimeLineBean timeLineBean;
        timeLineBean = new TimeLineBean();
        timeLineBean.setTime("2018-11-26 08:30");
        timeLineBean.setTitle("申请成功");
        mTimeLineList.add(timeLineBean);
        timeLineBean = new TimeLineBean();
        timeLineBean.setTime("2018-11-26 08:31");
        timeLineBean.setTitle("审批成功");
        mTimeLineList.add(timeLineBean);
        timeLineBean = new TimeLineBean();
        timeLineBean.setTime("2018-11-26 08:35");
        timeLineBean.setTitle("调度成功");
        mTimeLineList.add(timeLineBean);
        timeLineBean = new TimeLineBean();
        timeLineBean.setTime("2018-11-26 08:40");
        timeLineBean.setTitle("派车成功");
        mTimeLineList.add(timeLineBean);
        timeLineBean = new TimeLineBean();
        timeLineBean.setTime("2018-11-26 09:13");
        timeLineBean.setTitle("驾驶员接到乘客");
        mTimeLineList.add(timeLineBean);
    }

    private void initMyToolBar() {
        initToolBar(mToolbar, "订单详情", R.drawable.gank_ic_back_white);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(false);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(false);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    private void initWidget() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
                return false;
            }
        });
        //解决数据加载不完的问题
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        //解决数据加载完成后, 没有停留在顶部的问题
//        mRecyclerView.setFocusable(false);

        recyclerViewTimeLine.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
                return false;
            }
        });

        btnStatus.setVisibility(View.VISIBLE);
        btnReject.setText("重新派车");
        btnPass.setText("取消派车");
        recyclerViewSelected.setLayoutManager(new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
                return false;
            }
        });
        //解决数据加载不完的问题
        recyclerViewSelected.setNestedScrollingEnabled(false);
        recyclerViewSelected.setHasFixedSize(true);

    }

    private void initAdapter() {
        final ArrayList<String> list = new ArrayList<>();
        for (EnclosureBean bean : mDataList) {
            list.add(bean.getPicUrl());
        }
        BaseQuickAdapter enclosureAdapter = new EnclosureAdapter(R.layout.enclosure_list_item, mDataList, DispathedOrderDetailActivity.this);
        enclosureAdapter.openLoadAnimation();
        enclosureAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ImageBrowseIntent.showUrlImageBrowse(mContext, list, position);
            }
        });

        recyclerView.setAdapter(enclosureAdapter);

        BaseQuickAdapter timeLineAdapter = new TimeLineAdapter(R.layout.item_time_line, mTimeLineList);
        enclosureAdapter.openLoadAnimation();
        recyclerViewTimeLine.setAdapter(timeLineAdapter);

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
        adapter = new SelectedCarsAdapter(mContext, data,false);//是否可删除，否
        recyclerViewSelected.setAdapter(adapter);
    }

    @OnClick({R.id.tv_car_use_contacts_tel, R.id.tv_car_user_tel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_car_use_contacts_tel:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    // 没有权限。
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                        // 用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
                        ToastUtils.showShortToast("您拒绝了打电话的权限，将无法拨打电话.");
                    } else {
                        // 申请授权。
                        ActivityCompat.requestPermissions(DispathedOrderDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CALL_PHONE_CONTACT);
                    }
                } else {
                    // 有权限了，去放肆吧。
                    callPhone(tvCarUseContactsTel.getText().toString());
                }
                break;
            case R.id.tv_car_user_tel:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    // 没有权限。
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                        // 用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
                        ToastUtils.showShortToast("您拒绝了打电话的权限，将无法拨打电话.");
                    } else {
                        // 申请授权。
                        ActivityCompat.requestPermissions(DispathedOrderDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CALL_PHONE_USERTEL);
                    }
                } else {
                    // 有权限了，去放肆吧。
                    callPhone(tvCarUserTel.getText().toString());
                }
                break;
        }
    }

    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    @SuppressLint("MissingPermission")
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CALL_PHONE_CONTACT:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意，可以去放肆了。
                    callPhone(tvCarUseContactsTel.getText().toString());
                } else {
                    // 权限被用户拒绝了，洗洗睡吧。
                    ToastUtils.showShortToast("您拒绝了打电话的权限，将无法拨打电话.");

                }
                break;
            case PERMISSION_CALL_PHONE_USERTEL:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意，可以去放肆了。
                    callPhone(tvCarUserTel.getText().toString());
                } else {
                    // 权限被用户拒绝了，洗洗睡吧。
                    ToastUtils.showShortToast("您拒绝了打电话的权限，将无法拨打电话.");
                }
                break;

        }
    }

}
