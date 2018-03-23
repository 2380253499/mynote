package com.mynote.module.secret.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.androidtools.PhoneUtils;
import com.github.baseclass.rx.IOCallBack;
import com.github.baseclass.rx.MySubscriber;
import com.github.baseclass.rx.RxBus;
import com.github.customview.MyEditText;
import com.mynote.IntentParam;
import com.mynote.R;
import com.mynote.base.BaseFragment;
import com.mynote.event.ClearDataEvent;
import com.mynote.event.GetDataEvent;
import com.mynote.event.SaveDataEvent;
import com.mynote.module.secret.bean.SecretBean;
import com.mynote.module.secret.dao.imp.SecretImp;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by Administrator on 2018/2/2.
 */

public class AddSecretFragment extends BaseFragment<SecretImp>  {
    @BindView(R.id.et_secret_reminder)
    MyEditText et_secret_reminder;
    @BindView(R.id.et_secret_content)
    MyEditText et_secret_content;
    @BindView(R.id.tv_secret_lengthprompt)
    TextView tv_secret_lengthprompt;
    @BindView(R.id.tv_secret_copy)
    TextView tv_secret_copy;
    @BindView(R.id.tv_secret_paste)
    TextView tv_secret_paste;
    @BindView(R.id.tv_secret_clear)
    TextView tv_secret_clear;



    @BindView(R.id.ll_update_time)
    LinearLayout ll_update_time;
    @BindView(R.id.tv_create_time)
    TextView tv_create_time;
    @BindView(R.id.tv_update_time)
    TextView tv_update_time;


    //用于更新数据
    private boolean addDataSuccess;
    /**
     * 判断是否是编辑还是添加
     */
    private boolean isEdit;
    private SecretBean secretBean;

    @Override
    protected int getContentView() {
        return R.layout.fragment_add_secret;
    }

    public static AddSecretFragment newInstance() {
        return newInstance(null);
    }

    public static AddSecretFragment newInstance(SecretBean bean) {
        Bundle args = new Bundle();
        if (bean != null) {
            args.putSerializable(IntentParam.editSecretBean, bean);
        }
        AddSecretFragment fragment = new AddSecretFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void initRxBus() {
        super.initRxBus();
        getRxBusEvent(SaveDataEvent.class, new MySubscriber<SaveDataEvent>() {
            @Override
            public void onMyNext(SaveDataEvent event) {
                if(event.index== SaveDataEvent.secretIndex){
                    String content = et_secret_content.getText().toString();
                    if (TextUtils.isEmpty(content)||content.trim().length()<=0) {
                        showToastS("内容不能为空");
                    } else {
                        String remark = et_secret_reminder.getText().toString();
                        SecretBean bean ;
                        if(isEdit){
                            bean=secretBean;
                            bean.setDataContent(content);
                            bean.setDataRemark(remark);
                            editSecret(bean);
                        }else{
                            bean=new SecretBean();
                            bean.setDataContent(content);
                            bean.setDataRemark(remark);
                            addSecret(bean);
                        }
                    }
                }
            }
        });
        getRxBusEvent(ClearDataEvent.class, new MySubscriber<ClearDataEvent>() {
            @Override
            public void onMyNext(ClearDataEvent event) {
                if(event.index== SaveDataEvent.secretIndex){
                    et_secret_reminder.setText(null);
                    et_secret_content.setText(null);
                    tv_secret_lengthprompt.setText("(0/3000)");
                }
            }
        });
    }
    private void editSecret(SecretBean bean) {
        showLoading();
        RXStart(pl_load,new IOCallBack<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                long count = mDaoImp.updateSecret(bean);
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

    private void addSecret(SecretBean bean) {
        showLoading();
        RXStart(pl_load,new IOCallBack<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                long addSecret = mDaoImp.addSecret(bean);
                subscriber.onNext(addSecret>0?"添加成功":"添加失败");
                subscriber.onCompleted();
            }
            @Override
            public void onMyNext(String msg) {
                showMsg(msg);
                addDataSuccess=true;
                et_secret_reminder.setText(null);
                et_secret_content.setText(null);
                tv_secret_lengthprompt.setText("(0/3000)");
            }
        });
    }
    @Override
    protected void initView() {
        et_secret_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                tv_secret_lengthprompt.setText("("+length+"/3000)");
            }
        });
    }

    @Override
    protected void initData() {
        secretBean = (SecretBean) getArguments().getSerializable(IntentParam.editSecretBean);


        if (secretBean != null) {
            isEdit = true;
            et_secret_reminder.setText(secretBean.getDataRemark());
            et_secret_content.setText(secretBean.getDataContent());
            tv_secret_lengthprompt.setText("("+secretBean.getDataContent().length()+"/3000)");
        }
        setCreateTime(ll_update_time,tv_create_time, tv_update_time,isEdit,secretBean );
    }
    @OnClick({R.id.tv_secret_copy, R.id.tv_secret_paste, R.id.tv_secret_clear})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_secret_copy:
                String content = et_secret_content.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    showToastS("请填写数据之后复制");
                } else {
                    PhoneUtils.copyText(getActivity(), content);
                    showToastS("复制成功");
                }
                break;
            case R.id.tv_secret_paste:
                et_secret_content.setText(et_secret_content.getText()+""+PhoneUtils.pasteText(getActivity()));
                break;
            case R.id.tv_secret_clear:
                et_secret_content.setText(null);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(addDataSuccess){
            RxBus.getInstance().post(new GetDataEvent(GetDataEvent.secretIndex));
        }
    }
}
