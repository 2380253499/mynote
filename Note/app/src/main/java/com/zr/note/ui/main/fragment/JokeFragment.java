package com.zr.note.ui.main.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.base.IPresenter;
import com.zr.note.base.customview.MyEditText;
import com.zr.note.ui.main.inter.AddDataInter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JokeFragment extends BaseFragment implements AddDataInter {

    @BindView(R.id.et_joke_declare)
    MyEditText et_joke_declare;
    @BindView(R.id.et_joke_content)
    MyEditText et_joke_content;
    @BindView(R.id.tv_joke_clear)
    TextView tv_joke_clear;
    public static JokeFragment newInstance() {
        
        Bundle args = new Bundle();
        
        JokeFragment fragment = new JokeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected IPresenter initPresenter() {
        return null;
    }
    
    @Override
    protected int setContentView() {
        return R.layout.fragment_joke;
    }

    @Override
    protected void initView() {
        tv_joke_clear.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()){
            case R.id.tv_joke_clear:
                et_joke_content.setText("");
            break;
        }
    }

    @Override
    public void saveData() {
        String jokeContent = et_joke_content.getText().toString().trim();
        if (TextUtils.isEmpty(jokeContent)) {
            showToastS("段子内容不能为空");
        } else {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
