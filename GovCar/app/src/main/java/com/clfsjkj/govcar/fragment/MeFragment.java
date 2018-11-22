package com.clfsjkj.govcar.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clfsjkj.govcar.R;
import com.clfsjkj.govcar.base.BaseFragment;

/**
 * 我
 */
public class MeFragment extends BaseFragment {
    private RelativeLayout title;
    private TextView submit;
    public MeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        title = (RelativeLayout)view.findViewById(R.id.title);
        submit = view.findViewById(R.id.submit);
        submit.setVisibility(View.GONE);
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

}
