package com.zr.note.ui.main.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.base.BasePresenter;
import com.zr.note.base.customview.MyEditText;
import com.zr.note.ui.main.inter.AddDataInter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddDailyReminderFragment extends BaseFragment implements AddDataInter {

    @BindView(R.id.et_memo_reminder)
    MyEditText et_memo_reminder;
    @BindView(R.id.et_memo_content)
    MyEditText et_memo_content;

    public static AddDailyReminderFragment newInstance() {
        Bundle args = new Bundle();
        AddDailyReminderFragment fragment = new AddDailyReminderFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_add_daily_reminder;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

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
        }
        return false;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
