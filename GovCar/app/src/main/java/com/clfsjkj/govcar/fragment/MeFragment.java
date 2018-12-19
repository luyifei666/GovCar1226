package com.clfsjkj.govcar.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clfsjkj.govcar.R;
import com.clfsjkj.govcar.TextSizeShowActivity;
import com.clfsjkj.govcar.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 我
 */
public class MeFragment extends BaseFragment {
    @BindView(R.id.btn)
    Button btn;
    Unbinder unbinder;
    private Context mContext;
    private RelativeLayout title;
    private TextView submit;

    public MeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        title = (RelativeLayout) view.findViewById(R.id.title);
        submit = view.findViewById(R.id.submit);
        submit.setVisibility(View.GONE);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn)
    public void onViewClicked() {
        Intent it  = new Intent(mContext,TextSizeShowActivity.class);
        startActivity(it);
    }
}
