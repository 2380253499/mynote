package com.zr.note.ui.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.base.customview.MyEditText;
import com.zr.note.base.customview.MyTextView;
import com.zr.note.tools.DateUtils;
import com.zr.note.tools.StringUtils;
import com.zr.note.ui.constant.IntentParam;
import com.zr.note.ui.main.broadcast.BroFilter;
import com.zr.note.ui.main.entity.SpendBean;
import com.zr.note.ui.main.fragment.contract.AddSpendCon;
import com.zr.note.ui.main.fragment.contract.imp.AddSpendImp;
import com.zr.note.ui.main.inter.AddDataInter;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddSpendFragment extends BaseFragment<AddSpendCon.View, AddSpendCon.Presenter> implements AddDataInter, AddSpendCon.View {


    @BindView(R.id.tv_spend_date)
    TextView tv_spend_date;
    @BindView(R.id.tv_update_spend_date)
    TextView tv_update_spend_date;
    @BindView(R.id.et_spend_remark)
    MyEditText et_spend_remark;
    @BindView(R.id.et_spend_amount)
    MyEditText et_spend_amount;
    @BindView(R.id.tv_spend_zaocan)
    MyTextView tvSpendZaocan;
    @BindView(R.id.tv_spend_wucan)
    MyTextView tvSpendWucan;
    @BindView(R.id.tv_spend_wancan)
    MyTextView tvSpendWancan;
    @BindView(R.id.tv_spend_lingshi)
    MyTextView tvSpendLingshi;
    @BindView(R.id.tv_spend_gouwu)
    MyTextView tvSpendGouwu;
    @BindView(R.id.tv_spend_jiaotong)
    MyTextView tvSpendJiaotong;
    @BindView(R.id.tv_spend_kanbingmaiyao)
    MyTextView tvSpendKanbingmaiyao;
    @BindView(R.id.tv_spend_maishuiguo)
    MyTextView tvSpendMaishuiguo;
    @BindView(R.id.tv_spend_fangzu)
    MyTextView tvSpendFangzu;
    @BindView(R.id.tv_spend_wanggou)
    MyTextView tvSpendWanggou;
    @BindView(R.id.tv_spend_shuidian)
    MyTextView tvSpendShuidian;
    @BindView(R.id.tv_spend_chuxingyouwan)
    MyTextView tvSpendChuxingyouwan;
    @BindView(R.id.tv_spend_xiuxianyule)
    MyTextView tvSpendXiuxianyule;
    @BindView(R.id.tv_spend_ther)
    MyTextView tvSpendTher;
    private boolean isEdit;
    private SpendBean spendBean;
    private int spendYear = -1, spendMonth = -1, spendDay = -1;
    TimePickerView pvTime;

    private boolean isPrePareSelectData;
    @Override
    protected AddSpendImp initPresenter() {
        return new AddSpendImp(getActivity());
    }

    public static AddSpendFragment newInstance(SpendBean bean) {
        Bundle args = new Bundle();
        if (args != null) {
            args.putSerializable(IntentParam.editSpendBean, bean);
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
        tvSpendZaocan.setOnClickListener(this);
        tvSpendWucan.setOnClickListener(this);
        tvSpendWancan.setOnClickListener(this);
        tvSpendLingshi.setOnClickListener(this);
        tvSpendGouwu.setOnClickListener(this);
        tvSpendJiaotong.setOnClickListener(this);
        tvSpendKanbingmaiyao.setOnClickListener(this);
        tvSpendMaishuiguo.setOnClickListener(this);
        tvSpendFangzu.setOnClickListener(this);
        tvSpendWanggou.setOnClickListener(this);
        tvSpendShuidian.setOnClickListener(this);
        tvSpendChuxingyouwan.setOnClickListener(this);
        tvSpendXiuxianyule.setOnClickListener(this);
        tvSpendTher.setOnClickListener(this);

        tv_update_spend_date.setOnClickListener(this);
        et_spend_remark.requestFocus();
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

        initPickTime();
    }

    private void initPickTime() {
        //时间选择器
        pvTime = new TimePickerView(getActivity(), TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 5, calendar.get(Calendar.YEAR));//要在setTime 之前才有效果哦
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                spendYear = Integer.parseInt(DateUtils.dateToString(date, "yyyy"));
                spendMonth = Integer.parseInt(DateUtils.dateToString(date, "MM"));
                spendDay = Integer.parseInt(DateUtils.dateToString(date, "dd"));
                tv_spend_date.setText(DateUtils.dateToString(date));
            }
        });

    }

    @Override
    protected void initData() {
        spendBean = (SpendBean) getArguments().getSerializable(IntentParam.editSpendBean);
        if (spendBean != null) {
            tv_update_spend_date.setVisibility(View.GONE);
            isEdit = true;
            et_spend_remark.setText(spendBean.getDataRemark());

            String divide = StringUtils.roundForBigDecimal(spendBean.getLiveSpend())+"";
            String[] split = divide.split("\\.");
            if (Integer.parseInt(split[1]) > 0) {
                et_spend_amount.setText(spendBean.getLiveSpend() + "");
            } else {
                et_spend_amount.setText(split[0]);
            }
            tv_spend_date.setText(spendBean.getLocalYear() + "-" + (spendBean.getLocalMonth() < 10 ? "0" + spendBean.getLocalMonth() : spendBean.getLocalMonth()) + "-" + (spendBean.getLocalDay() < 10 ? "0" + spendBean.getLocalDay() : spendBean.getLocalDay()));
        } else {
            tv_spend_date.setText(DateUtils.dateToString(new Date()));
        }
    }

    @Override
    protected void viewOnClick(View v) {
        String spendRemark = et_spend_remark.getText().toString();
        switch (v.getId()) {
            case R.id.tv_update_spend_date:
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                pvTime.show();
                break;
            case R.id.tv_spend_zaocan:
                if(spendRemark.trim().length()>28){
                    showToastS("输入字符长度不能超过30");
                    return;
                }
                if(spendRemark.indexOf("早餐")<0){
                    et_spend_remark.setText(spendRemark+"早餐");
                }
                break;
            case R.id.tv_spend_wucan:
                if(spendRemark.trim().length()>28){
                    showToastS("输入字符长度不能超过30");
                    return;
                }
                if(spendRemark.indexOf("午餐")<0){
                    et_spend_remark.setText(spendRemark+"午餐");
                }
            break;
            case R.id.tv_spend_wancan:
                if(spendRemark.trim().length()>28){
                    showToastS("输入字符长度不能超过30");
                    return;
                }
                if(spendRemark.indexOf("晚餐")<0){
                    et_spend_remark.setText(spendRemark+"晚餐");
                }
            break;
            case R.id.tv_spend_lingshi:
                if(spendRemark.trim().length()>28){
                    showToastS("输入字符长度不能超过30");
                    return;
                }
                if(spendRemark.indexOf("零食")<0){
                    et_spend_remark.setText(spendRemark+"零食");
                }
            break;
            case R.id.tv_spend_gouwu:
                if(spendRemark.trim().length()>28){
                    showToastS("输入字符长度不能超过30");
                    return;
                }
                if(spendRemark.indexOf("购物")<0){
                    et_spend_remark.setText(spendRemark+"购物");
                }
            break;
            case R.id.tv_spend_jiaotong:
                if(spendRemark.trim().length()>28){
                    showToastS("输入字符长度不能超过30");
                    return;
                }
                if(spendRemark.indexOf("交通")<0){
                    et_spend_remark.setText(spendRemark+"交通");
                }
            break;
            case R.id.tv_spend_kanbingmaiyao:
                if(spendRemark.trim().length()>26){
                    showToastS("输入字符长度不能超过30");
                    return;
                }
                if(spendRemark.indexOf("看病买药")<0){
                    et_spend_remark.setText(spendRemark+"看病买药");
                }
            break;
            case R.id.tv_spend_maishuiguo:
                if(spendRemark.trim().length()>27){
                    showToastS("输入字符长度不能超过30");
                    return;
                }
                if(spendRemark.indexOf("买水果")<0){
                    et_spend_remark.setText(spendRemark+"买水果");
                }
            break;
            case R.id.tv_spend_fangzu:
                if(spendRemark.trim().length()>28){
                    showToastS("输入字符长度不能超过30");
                    return;
                }
                if(spendRemark.indexOf("房租")<0){
                    et_spend_remark.setText(spendRemark+"房租");
                }
            break;
            case R.id.tv_spend_wanggou:
                if(spendRemark.trim().length()>28){
                    showToastS("输入字符长度不能超过30");
                    return;
                }
                if(spendRemark.indexOf("网购")<0){
                    et_spend_remark.setText(spendRemark+"网购");
                }
            break;
            case R.id.tv_spend_shuidian:
                if(spendRemark.trim().length()>28){
                    showToastS("输入字符长度不能超过30");
                    return;
                }
                if(spendRemark.indexOf("水电")<0){
                    et_spend_remark.setText(spendRemark+"水电");
                }
            break;
            case R.id.tv_spend_chuxingyouwan:
                if(spendRemark.trim().length()>26){
                    showToastS("输入字符长度不能超过30");
                    return;
                }
                if(spendRemark.indexOf("出行游玩")<0){
                    et_spend_remark.setText(spendRemark+"出行游玩");
                }
            break;
            case R.id.tv_spend_xiuxianyule:
                if(spendRemark.trim().length()>26){
                    showToastS("输入字符长度不能超过30");
                    return;
                }
                if(spendRemark.indexOf("休闲娱乐")<0){
                    et_spend_remark.setText(spendRemark+"休闲娱乐");
                }
            break;
            case R.id.tv_spend_ther:
                if(spendRemark.trim().length()>28){
                    showToastS("输入字符长度不能超过30");
                    return;
                }
                if(spendRemark.indexOf("其他")<0){
                    et_spend_remark.setText(spendRemark+"其他");
                }
            break;
        }
    }

    @Override
    public boolean saveData() {
        String spendContent = et_spend_amount.getText().toString().trim();
        if (TextUtils.isEmpty(spendContent)) {
            showToastS("金额不能为空");
        } else {
            String spendRemark = et_spend_remark.getText().toString().trim();
            SpendBean bean = new SpendBean();
            bean.setLiveSpend(Double.parseDouble(spendContent));
            bean.setDataRemark(spendRemark);
            if (spendYear != -1) {
                bean.setLocalYear(spendYear);
                bean.setLocalMonth(spendMonth);
                bean.setLocalDay(spendDay);
            }
            bean.set_id(isEdit ? spendBean.get_id() : -1);
            boolean b = mPresenter.addSpend(bean);
            if (b) {
                isPrePareSelectData=true;
            }
            return b;
        }
        return false;
    }

    @Override
    public void clearData() {
        et_spend_remark.setText(null);
        et_spend_amount.setText(null);
        et_spend_remark.requestFocus();
    }
    public void prePareSelectData(){
        mIntent.setAction(BroFilter.addData_spend);
        mIntent.putExtra(BroFilter.isAddData, true);
        mIntent.putExtra(BroFilter.isAddData_index, BroFilter.index_2);
        getActivity().sendBroadcast(mIntent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(isPrePareSelectData){
            prePareSelectData();
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
