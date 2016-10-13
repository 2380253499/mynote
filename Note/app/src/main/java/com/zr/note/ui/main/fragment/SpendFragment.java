package com.zr.note.ui.main.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.base.BasePresenter;
import com.zr.note.base.customview.MyEditText;
import com.zr.note.tools.EditTextUtils;
import com.zr.note.ui.main.inter.AddDataInter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpendFragment extends BaseFragment implements AddDataInter {


    @BindView(R.id.et_spend_declare)
    MyEditText et_spend_declare;
    @BindView(R.id.et_spend_amount)
    MyEditText et_spend_amount;

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    public static SpendFragment newInstance() {

        Bundle args = new Bundle();
        
        SpendFragment fragment = new SpendFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int setContentView() {
        return R.layout.fragment_spend;
    }

    @Override
    protected void initView() {
        et_spend_amount.setFilters(new InputFilter[]{EditTextUtils.getInputFilter()});
        et_spend_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int pointIndex = s.toString().trim().indexOf(".");
                if (pointIndex >= 0) {
                    String[] split = s.toString().split("\\.");
                    if (split.length > 1 && split[1].length() > 1) {
                        et_spend_amount.setText(split[0] + "." + split[1].substring(0, 1));
                        Editable etext = et_spend_amount.getText();
                        Selection.setSelection(etext, etext.length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void viewOnClick(View v) {

    }

    @Override
    public void saveData() {
        String spendContent = et_spend_amount.getText().toString().trim();
        if (TextUtils.isEmpty(spendContent)) {
            showToastS("金额不能为空");
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
