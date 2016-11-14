package com.zr.note.ui.main.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.base.customview.MyEditText;
import com.zr.note.ui.constant.IntentParam;
import com.zr.note.ui.main.broadcast.BroFilter;
import com.zr.note.ui.main.entity.SpendBean;
import com.zr.note.ui.main.fragment.contract.AddSpendCon;
import com.zr.note.ui.main.fragment.contract.imp.AddSpendImp;
import com.zr.note.ui.main.inter.AddDataInter;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddSpendFragment extends BaseFragment<AddSpendCon.View,AddSpendCon.Presenter> implements AddDataInter,AddSpendCon.View {


    @BindView(R.id.et_spend_remark)
    MyEditText et_spend_remark;
    @BindView(R.id.et_spend_amount)
    MyEditText et_spend_amount;
    private boolean isEdit;

    @Override
    protected AddSpendImp initPresenter() {
        return new AddSpendImp(getActivity());
    }

    public static AddSpendFragment newInstance(SpendBean bean) {
        Bundle args = new Bundle();
        if (args!=null){
            args.putSerializable(IntentParam.editSpendBean,bean);
        }
        AddSpendFragment fragment = new AddSpendFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public static AddSpendFragment newInstance() {
        return newInstance(null);
    }
    @Override
    protected int setContentView() {
        return R.layout.fragment_add_spend;
    }

    @Override
    protected void initView() {
//        et_spend_amount.setFilters(new InputFilter[]{EditTextUtils.getInputFilter()});
//        et_spend_amount.setMaxLines(9);
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
        SpendBean spendBean = (SpendBean) getArguments().getSerializable(IntentParam.editSpendBean);
        if(spendBean!=null){
            isEdit =true;
            et_spend_remark.setText(spendBean.getDataRemark());
            et_spend_amount.setText(new BigDecimal(spendBean.getLiveSpend())+"");
        }
    }

    @Override
    protected void viewOnClick(View v) {

    }

    @Override
    public boolean saveData() {
        String spendContent = et_spend_amount.getText().toString().trim();
        if (TextUtils.isEmpty(spendContent)) {
            showToastS("金额不能为空");
        } else {
            String spendRemark = et_spend_remark.getText().toString().trim();
            SpendBean bean=new SpendBean();
            bean.setLiveSpend(Double.parseDouble(spendContent));
            bean.setDataRemark(spendRemark);
            boolean b = mPresenter.addSpend(bean);
            if(b){
                mIntent.setAction(BroFilter.addData_spend);
                mIntent.putExtra(BroFilter.isAddData, true);
                mIntent.putExtra(BroFilter.isAddData_index,BroFilter.index_2);
                getActivity().sendBroadcast(mIntent);
            }
            return b;
        }
        return false;
    }

    @Override
    public void clearData() {
        et_spend_remark.setText(null);
        et_spend_amount.setText(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate AddMemoFragment fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
