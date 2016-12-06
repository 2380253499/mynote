package com.zr.note.ui.main.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.base.customview.MyEditText;
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

        et_memo_content.requestFocus();
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
