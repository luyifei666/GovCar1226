package com.clfsjkj.govcar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clfsjkj.govcar.R;
import com.clfsjkj.govcar.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 消息内容页
 */
public class NeedContentFragment extends BaseFragment {
    @BindView(R.id.txt_content)
    TextView tvContent;

    private String name;

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
        View view = inflater.inflate(R.layout.fragment_need_content, container, false);
        ButterKnife.bind(this, view);

        tvContent.setText(name);
        return view;
    }

}
