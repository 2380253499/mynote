package com.newnote.module.home.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.androidtools.PhoneUtils;
import com.github.customview.MyEditText;
import com.newnote.R;
import com.newnote.base.BaseFragment;
import com.newnote.module.home.Constant;
import com.newnote.module.joke.entity.JokeBean;

import butterknife.BindView;
import butterknife.OnClick;

public class AddJokeFragment extends BaseFragment   {

    @BindView(R.id.et_joke_remark)
    MyEditText et_joke_remark;
    @BindView(R.id.et_joke_content)
    MyEditText et_joke_content;
    @BindView(R.id.tv_joke_clear)
    TextView tv_joke_clear;
    @BindView(R.id.tv_joke_copy)
    TextView tv_joke_copy;
    @BindView(R.id.tv_joke_paste)
    TextView tv_joke_paste;
    @BindView(R.id.tv_joke_lengthprompt)
    TextView tv_joke_lengthprompt;
    @BindView(R.id.noScrollView)
    ScrollView noScrollView;

    private boolean isEdit;
    private JokeBean jokeBean;


    private boolean isPrePareSelectData;

    public static AddJokeFragment newInstance(JokeBean bean) {
        Bundle args = new Bundle();
        if (args!=null){
            args.putSerializable(Constant.IParam.editJokeBean,bean);
        }
        AddJokeFragment fragment = new AddJokeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public static AddJokeFragment newInstance() {
        return newInstance(null);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_add_joke;
    }

    @Override
    protected void initView() {
        et_joke_content.requestFocus();
        tv_joke_clear.setOnClickListener(this);
        tv_joke_copy.setOnClickListener(this);
        tv_joke_paste.setOnClickListener(this);

        et_joke_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.toString().length();
                tv_joke_lengthprompt.setText("(" + length + "/3000)");
            }
        });

        /*noScrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        noScrollView.setFocusable(true);
        noScrollView.setFocusableInTouchMode(true);
        noScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });*/
    }

    @Override
    protected void initData() {
        jokeBean = (JokeBean) getArguments().getSerializable(Constant.IParam.editJokeBean);
        if(jokeBean !=null){
            isEdit=true;
            et_joke_remark.setText(jokeBean.getDataRemark());
            et_joke_content.setText(jokeBean.getDataContent());
        }
    }


    @OnClick({R.id.tv_joke_gxdz,R.id.tv_joke_ssly,R.id.tv_joke_gxly})
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.tv_joke_gxdz:
                et_joke_remark.setText(et_joke_remark.getText());
                if(getSStr(et_joke_remark).indexOf("搞笑段子")<0){
                    et_joke_remark.setText(getSStr(et_joke_remark)+"搞笑段子");
                }
            break;
            case R.id.tv_joke_ssly:
                et_joke_remark.setText(et_joke_remark.getText());
                if(getSStr(et_joke_remark).indexOf("说说留言")<0){
                    et_joke_remark.setText(getSStr(et_joke_remark)+"说说留言");
                }
            break;
            case R.id.tv_joke_gxly:
                et_joke_remark.setText(et_joke_remark.getText());
                if(getSStr(et_joke_remark).indexOf("搞笑留言")<0){
                    et_joke_remark.setText(getSStr(et_joke_remark)+"搞笑留言");
                }
            break;
            case R.id.tv_joke_clear:
                et_joke_content.setText("");
            break;
            case R.id.tv_joke_copy:
                String jokeContent = et_joke_content.getText().toString().trim();
                if (TextUtils.isEmpty(jokeContent)) {
                    showToastS("段子内容不能为空");
                } else {
                    PhoneUtils.copyText(getActivity(), et_joke_content.getText().toString().trim());
                    showToastS("复制成功");
                }
                break;
            case R.id.tv_joke_paste:
                String content = PhoneUtils.pasteText(getActivity());
                et_joke_content.setText(et_joke_content.getText()+""+content);
            break;
        }
    }


    public boolean saveData() {
        String jokeContent = et_joke_content.getText().toString().trim();
        if (TextUtils.isEmpty(jokeContent)) {
            showToastS("段子内容不能为空");
        } else {
            String jokeRemark = et_joke_remark.getText().toString().trim();
            JokeBean bean=new JokeBean();
            bean.setDataRemark(jokeRemark);
            bean.setDataContent(jokeContent);
            bean.set_id(isEdit ? jokeBean.get_id() : -1);
            boolean b = true;//mPresenter.addJoke(bean);
            if(b){
                if(!isEdit){
                    et_joke_remark.setText(null);
                    et_joke_content.setText(null);
                }
                isPrePareSelectData=true;
            }
            return b;
        }
        return false;
    }


    public void clearData() {
        et_joke_remark.setText(null);
        et_joke_content.setText(null);

        et_joke_content.requestFocus();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

}
