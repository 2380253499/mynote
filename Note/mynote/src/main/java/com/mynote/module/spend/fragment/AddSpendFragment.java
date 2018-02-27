package com.mynote.module.spend.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.github.androidtools.DateUtils;
import com.github.androidtools.PhoneUtils;
import com.github.androidtools.StringUtils;
import com.github.androidtools.inter.MyOnClickListener;
import com.github.baseclass.rx.IOCallBack;
import com.github.baseclass.rx.MySubscriber;
import com.github.baseclass.rx.RxBus;
import com.github.customview.FlowLayout;
import com.github.customview.MyEditText;
import com.github.customview.MyTextView;
import com.mynote.IntentParam;
import com.mynote.R;
import com.mynote.base.BaseFragment;
import com.mynote.event.ClearDataEvent;
import com.mynote.event.GetDataEvent;
import com.mynote.event.SaveDataEvent;
import com.mynote.module.spend.bean.SpendBean;
import com.mynote.module.spend.dao.imp.SpendImp;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by Administrator on 2018/2/2.
 */

public class AddSpendFragment extends BaseFragment<SpendImp> {
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


    @BindView(R.id.ll_update_time)
    LinearLayout ll_update_time;
    @BindView(R.id.tv_create_time)
    TextView tv_create_time;
    @BindView(R.id.tv_update_time)
    TextView tv_update_time;

    //用于更新数据
    private boolean addDataSuccess;
    private boolean isEdit;
    private SpendBean spendBean;
    private int spendYear = -1, spendMonth = -1, spendDay = -1;
    TimePickerView pvTime;

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
    protected void initRxBus() {
        super.initRxBus();
        getRxBusEvent(SaveDataEvent.class, new MySubscriber<SaveDataEvent>() {
            @Override
            public void onMyNext(SaveDataEvent event) {
                if(event.index== SaveDataEvent.spendIndex){
                    String content = et_spend_amount.getText().toString();
                    if (TextUtils.isEmpty(content)||content.trim().length()<=0) {
                        showToastS("金额不能为空");
                    } else {
                        String remark = et_spend_remark.getText().toString();
                        SpendBean bean ;
                        if(isEdit){
                            bean=spendBean;
                            bean.setLiveSpend(Double.parseDouble(content));
                            bean.setDataRemark(remark);
                            if (spendYear != -1) {
                                bean.setLocalYear(spendYear);
                                bean.setLocalMonth(spendMonth);
                                bean.setLocalDay(spendDay);
                            }else{
                                bean.setLocalYear(Integer.parseInt(DateUtils.dateToString(new Date(), "yyyy")));
                                bean.setLocalMonth(Integer.parseInt(DateUtils.dateToString(new Date(), "MM")));
                                bean.setLocalDay(Integer.parseInt(DateUtils.dateToString(new Date(), "dd")));
                            }
                            editSpend(bean);
                        }else{
                            bean=new SpendBean();
                            bean.setLiveSpend(Double.parseDouble(content));
                            bean.setDataRemark(remark);
                            if (spendYear != -1) {
                                bean.setLocalYear(spendYear);
                                bean.setLocalMonth(spendMonth);
                                bean.setLocalDay(spendDay);
                            }else{
                                bean.setLocalYear(Integer.parseInt(DateUtils.dateToString(new Date(), "yyyy")));
                                bean.setLocalMonth(Integer.parseInt(DateUtils.dateToString(new Date(), "MM")));
                                bean.setLocalDay(Integer.parseInt(DateUtils.dateToString(new Date(), "dd")));
                            }
                            addSpend(bean);
                        }
                    }
                }
            }
        });
        getRxBusEvent(ClearDataEvent.class, new MySubscriber<ClearDataEvent>() {
            @Override
            public void onMyNext(ClearDataEvent event) {
                if(event.index== SaveDataEvent.spendIndex){
                    et_spend_remark.setText(null);
                    et_spend_amount.setText(null);
                    et_spend_remark.requestFocus();
                }
            }
        });
    }
    private void editSpend(SpendBean bean) {
        showLoading();
        RXStart(pl_load,new IOCallBack<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                long count = mDaoImp.updateSpend(bean);
                subscriber.onNext(count>0?"修改成功":"修改失败");
                subscriber.onCompleted();
            }
            @Override
            public void onMyNext(String msg) {
                addDataSuccess=true;
                showMsg(msg);
            }
        });
    }

    private void addSpend(SpendBean bean) {
        showLoading();
        RXStart(pl_load,new IOCallBack<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                mDaoImp.addSpend(bean);
                subscriber.onNext(null);
                subscriber.onCompleted();
            }
            @Override
            public void onMyNext(List<String> list) {
                addDataSuccess=true;
                et_spend_remark.setText(null);
                et_spend_amount.setText(null);
            }
        });
    }
    @Override
    protected void initView() {
        fl_spend.removeAllViews();
        Resources res = getResources();
        String[] spendRemark = res.getStringArray(R.array.spendRemark);
        setView(spendRemark);

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

    private void setView(String[] spendRemark) {
        for (int i = 0; i < spendRemark.length; i++) {
            MyTextView textView = new MyTextView(getActivity());
            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,0,PhoneUtils.dip2px(mContext,15),PhoneUtils.dip2px(mContext,15));
            layoutParams.height=PhoneUtils.dip2px(mContext,28);
            if(spendRemark[i].length()>=4){
                layoutParams.width=PhoneUtils.dip2px(mContext,80);
            }else{
                layoutParams.width=PhoneUtils.dip2px(mContext,55);
            }
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
//            textView.setTextAppearance(getActivity(), R.style.add_spend_tag);
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

        spendBean = (SpendBean) getArguments().getSerializable(IntentParam.editSpendBean);

        setCreateTime(ll_update_time,tv_create_time, tv_update_time,isEdit,spendBean );

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
    @OnClick({R.id.tv_update_spend_date})
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.tv_update_spend_date:
                PhoneUtils.hiddenKeyBoard(mContext);
                pvTime.show();
            break;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(addDataSuccess){
            RxBus.getInstance().post(new GetDataEvent(GetDataEvent.spendIndex));
        }
    }

}
