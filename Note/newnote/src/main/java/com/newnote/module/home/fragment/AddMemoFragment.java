package com.newnote.module.home.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.github.androidtools.PhoneUtils;
import com.github.customview.MyEditText;
import com.newnote.R;
import com.newnote.base.BaseFragment;
import com.newnote.module.home.Constant;
import com.newnote.module.memo.entity.MemoBean;

import butterknife.BindView;

public class AddMemoFragment extends BaseFragment {

    @BindView(R.id.et_memo_reminder)
    MyEditText et_memo_reminder;
    @BindView(R.id.et_memo_content)
    MyEditText et_memo_content;
    @BindView(R.id.tv_memo_lengthprompt)
    TextView tv_memo_lengthprompt;
    @BindView(R.id.tv_memo_copy)
    TextView tv_memo_copy;
    @BindView(R.id.tv_memo_paste)
    TextView tv_memo_paste;
    @BindView(R.id.tv_memo_clear)
    TextView tv_memo_clear;
    private boolean isEdit;
    private MemoBean memoBean;

    private boolean isPrePareSelectData;

    public static AddMemoFragment newInstance(MemoBean bean) {
        Bundle args = new Bundle();
        if (args!=null){
            args.putSerializable(Constant.IParam.editMemoBean,bean);
        }
        AddMemoFragment fragment = new AddMemoFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public static AddMemoFragment newInstance() {
        return newInstance(null);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_add_memo;
    }

    @Override
    protected void initView() {
        tv_memo_copy.setOnClickListener(this);
        tv_memo_paste.setOnClickListener(this);
        tv_memo_clear.setOnClickListener(this);
        et_memo_content.requestFocus();
        et_memo_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                tv_memo_lengthprompt.setText("("+length+"/3000)");
            }
        });
    }

    @Override
    protected void initData() {
        memoBean = (MemoBean) getArguments().getSerializable(Constant.IParam.editMemoBean);
        if(memoBean !=null){
            isEdit=true;
            et_memo_reminder.setText(memoBean.getDataRemark());
            et_memo_content.setText(memoBean.getDataContent());
        }
    }


    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.tv_memo_copy:
                String memoContent = et_memo_content.getText().toString().trim();
                if (TextUtils.isEmpty(memoContent)) {
                    showToastS("备忘内容不能为空");
                } else {
                    PhoneUtils.copyText(getActivity(), et_memo_content.getText().toString().trim());
                    showToastS("复制成功");
                }
            break;
            case R.id.tv_memo_paste:
                et_memo_content.setText(et_memo_content.getText()+""+ PhoneUtils.pasteText(getActivity()));
            break;
            case R.id.tv_memo_clear:
                et_memo_content.setText(null);
            break;
        }
    }


    public boolean saveData() {
        String memoContent=et_memo_content.getText().toString().trim();
        if (TextUtils.isEmpty(memoContent)) {
            showToastS("备忘内容不能为空");
        } else {
            String reminder = et_memo_reminder.getText().toString();
            MemoBean reminderBean=new MemoBean();
            reminderBean.setDataRemark(reminder);
            reminderBean.setDataContent(memoContent);
            reminderBean.set_id(isEdit ? memoBean.get_id() : -1);
            boolean b = false;//mPresenter.addMemo(reminderBean);
            if(b){
                if(!isEdit){
                    et_memo_content.setText(null);
                    et_memo_reminder.setText(null);
                }
                isPrePareSelectData=true;
            }
            return b;
        }
        return false;

    }


    public void clearData() {
        et_memo_reminder.setText(null);
        et_memo_content.setText(null);
        et_memo_content.requestFocus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(isPrePareSelectData){

        }

    }

}
