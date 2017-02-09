package com.zr.note.ui.main.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.customview.MyEditText;
import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.tools.PhoneUtils;
import com.zr.note.ui.constant.IntentParam;
import com.zr.note.ui.main.broadcast.BroFilter;
import com.zr.note.ui.main.entity.MemoBean;
import com.zr.note.ui.main.fragment.contract.AddMemoCon;
import com.zr.note.ui.main.fragment.contract.imp.AddMemoImp;
import com.zr.note.ui.main.inter.AddDataInter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddMemoFragment extends BaseFragment<AddMemoCon.View,AddMemoCon.Presenter> implements AddMemoCon.View, AddDataInter {

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
            args.putSerializable(IntentParam.editMemoBean,bean);
        }
        AddMemoFragment fragment = new AddMemoFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public static AddMemoFragment newInstance() {
        return newInstance(null);
    }
    @Override
    protected AddMemoImp initPresenter() {
        return new AddMemoImp(getActivity());
    }

    @Override
    protected int setContentView() {
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
                tv_memo_lengthprompt.setText("("+length+"/1000)");
            }
        });
    }

    @Override
    protected void initData() {
        memoBean = (MemoBean) getArguments().getSerializable(IntentParam.editMemoBean);
        if(memoBean !=null){
            isEdit=true;
            et_memo_reminder.setText(memoBean.getDataRemark());
            et_memo_content.setText(memoBean.getDataContent());
        }
    }

    @Override
    protected void viewOnClick(View v) {
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
                et_memo_content.setText(et_memo_content.getText()+""+PhoneUtils.pasteText(getActivity()));
            break;
            case R.id.tv_memo_clear:
                et_memo_content.setText(null);
            break;
        }
    }

    @Override
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
            boolean b = mPresenter.addMemo(reminderBean);
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

    @Override
    public void clearData() {
        et_memo_reminder.setText(null);
        et_memo_content.setText(null);
        et_memo_content.requestFocus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(isPrePareSelectData){
            mIntent.setAction(BroFilter.addData_memo);
            mIntent.putExtra(BroFilter.isAddData,true);
            mIntent.putExtra(BroFilter.isAddData_index,BroFilter.index_1);
            getActivity().sendBroadcast(mIntent);
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
