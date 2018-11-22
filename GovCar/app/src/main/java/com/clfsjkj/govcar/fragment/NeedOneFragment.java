package com.clfsjkj.govcar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.clfsjkj.govcar.ItemAdapter.AnimationAdapter;
import com.clfsjkj.govcar.ItemAdapter.DataServer;
import com.clfsjkj.govcar.ItemAdapter.Status;
import com.clfsjkj.govcar.R;
import com.clfsjkj.govcar.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 消息内容页
 */
public class NeedOneFragment extends BaseFragment {
//    @BindView(R.id.txt_content)
//    TextView tvContent;
//
    private String name;

    private RecyclerView mRecyclerView;
    private AnimationAdapter mAnimationAdapter;
    private int mFirstPageItemCount = 3;
    private int count = 10;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        name = bundle.getString("name");
        if (name == null) {
            name = "参数非法";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_need_one, container, false);
//        ButterKnife.bind(this, view);
//
//        tvContent.setText(name);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        initAdapter();
        return view;
    }

    private void initAdapter() {
        mAnimationAdapter = new AnimationAdapter();
        mAnimationAdapter.openLoadAnimation();
        mAnimationAdapter.setNotDoAnimationCount(mFirstPageItemCount);
        mAnimationAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String content = null;
                Status status = (Status) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.img:
                        content = "img:" + status.getUserAvatar();
                        Toast.makeText(getContext(), content, Toast.LENGTH_LONG).show();
                        break;
                    case R.id.tweetName:
                        content = "name:" + status.getUserName();
                        Toast.makeText(getContext(), content, Toast.LENGTH_LONG).show();
                        break;
                    case R.id.tweetText:
                        content = "tweetText:" + status.getUserName();
                        Toast.makeText(getContext(), content, Toast.LENGTH_LONG).show();
                        // you have set clickspan .so there should not solve any click event ,just empty
                        break;

                }
            }
        });
        mRecyclerView.setAdapter(mAnimationAdapter);

//        -------------------------------------上拉加载---------------------------------------------------------
//        mAnimationAdapter.setUpFetchEnable(true);
//        mAnimationAdapter.setUpFetchListener(new BaseQuickAdapter.UpFetchListener() {
//            @Override
//            public void onUpFetch() {
//                startUpFetch();
//            }
//        });

//        -------------------------------------上拉加载---------------------------------------------------------
    }

    private void startUpFetch() {
        count++;
        /**
         * set fetching on when start network request.
         */
        mAnimationAdapter.setUpFetching(true);
        /**
         * get data from internet.
         */
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAnimationAdapter.addData(0, DataServer.getSampleData(3));//获取数据
                /**
                 * set fetching off when network request ends.
                 */
                mAnimationAdapter.setUpFetching(false);
                /**
                 * set fetch enable false when you don't need anymore.
                 */
                if (count > 5) {
                    mAnimationAdapter.setUpFetchEnable(false);
                }
            }
        }, 300);
        mAnimationAdapter.setStartUpFetchPosition(2);
    }

}
