package com.zr.note.ui.main.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.base.customview.MyEditText;
import com.zr.note.ui.main.entity.JokeBean;
import com.zr.note.ui.main.fragment.contract.AddJokeCon;
import com.zr.note.ui.main.fragment.contract.imp.AddJokeImp;
import com.zr.note.ui.main.inter.AddDataInter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddJokeFragment extends BaseFragment<AddJokeCon.View,AddJokeCon.Presenter> implements AddDataInter,AddJokeCon.View {

    @BindView(R.id.et_joke_remark)
    MyEditText et_joke_remark;
    @BindView(R.id.et_joke_content)
    MyEditText et_joke_content;
    @BindView(R.id.tv_joke_clear)
    TextView tv_joke_clear;
    @BindView(R.id.tv_joke_copy)
    TextView tv_joke_copy;
    public static AddJokeFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AddJokeFragment fragment = new AddJokeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected AddJokeImp initPresenter() {
        return new AddJokeImp(getActivity());
    }
    
    @Override
    protected int setContentView() {
        return R.layout.fragment_add_joke;
    }

    @Override
    protected void initView() {
        tv_joke_clear.setOnClickListener(this);
        tv_joke_copy.setOnClickListener(this);
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
            case R.id.tv_joke_copy:
                String jokeContent = et_joke_content.getText().toString().trim();
                if (TextUtils.isEmpty(jokeContent)) {
                    showToastS("段子内容不能为空");
                } else {
                    showToastS("复制成功");
                }
            break;
        }
    }

    @Override
    public boolean saveData() {
        String jokeContent = et_joke_content.getText().toString().trim();
        if (TextUtils.isEmpty(jokeContent)) {
            showToastS("段子内容不能为空");
        } else {
            String jokeRemark = et_joke_remark.getText().toString().trim();
            JokeBean bean=new JokeBean();
            bean.setDataRemark(jokeRemark);
            bean.setDataContent(jokeContent);
            return mPresenter.addJoke(bean);
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate AddMemoFragment fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}