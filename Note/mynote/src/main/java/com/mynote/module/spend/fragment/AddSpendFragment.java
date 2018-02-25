package com.mynote.module.spend.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.github.androidtools.PhoneUtils;
import com.github.androidtools.inter.MyOnClickListener;
import com.github.customview.FlowLayout;
import com.github.customview.MyEditText;
import com.github.customview.MyTextView;
import com.mynote.IntentParam;
import com.mynote.R;
import com.mynote.base.BaseFragment;
import com.mynote.module.spend.bean.SpendBean;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/2/2.
 */

public class AddSpendFragment extends BaseFragment {
    @BindView(R.id.fl_spend)
    FlowLayout fl_spend;
    @BindView(R.id.et_spend_remark)
    MyEditText et_spend_remark;
    @BindView(R.id.et_spend_amount)
    MyEditText et_spend_amount;
    @BindView(R.id.tv_spend_date)
    TextView tv_spend_date;
    @BindView(R.id.tv_update_spend_date)
    TextView tv_update_spend_date;

    @Override
    protected int getContentView() {
        return R.layout.fragment_add_spend;
    }

    public static AddSpendFragment newInstance() {
        return newInstance(null);
    }

    public static AddSpendFragment newInstance(SpendBean bean) {
        Bundle args = new Bundle();
        if (bean != null) {
            args.putSerializable(IntentParam.editSpendBean, bean);
        }
        AddSpendFragment fragment = new AddSpendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        fl_spend.removeAllViews();
        Resources res = getResources();
        String[] spendRemark = res.getStringArray(R.array.spendRemark);
        setView(spendRemark);
    }

    private void setView(String[] spendRemark) {
        for (int i = 0; i < spendRemark.length; i++) {
            MyTextView textView = new MyTextView(getActivity());
            textView.setTextAppearance(getActivity(), R.style.add_spend_tag);
            textView.setRadius(PhoneUtils.dip2px(mContext, 14));
            textView.setBorderWidth(1);
            textView.setPressColor(ContextCompat.getColor(mContext, R.color.c_divider));
            textView.setSolidColor(ContextCompat.getColor(mContext, R.color.white));
            textView.setBorderColor(ContextCompat.getColor(mContext, R.color.c_F8F8F83));
            textView.setText(spendRemark[i]);
            textView.complete();
            textView.setOnClickListener(new MyOnClickListener() {
                @Override
                protected void onNoDoubleClick(View view) {
                    String spendRemark = et_spend_remark.getText().toString();
                    if (spendRemark.trim().length() > 30) {
                        showToastS("输入字符长度不能超过30");
                        return;
                    }
                    if (spendRemark.indexOf(textView.getText().toString()) < 0) {
                        et_spend_remark.setText(spendRemark +textView.getText().toString());
                    }
                    et_spend_amount.requestFocus();
                    PhoneUtils.showKeyBoard(getActivity(), et_spend_amount);
                }
            });
            fl_spend.addView(textView);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }

    @OnClick(R.id.tv_update_spend_date)
    public void onClick() {
    }
}
