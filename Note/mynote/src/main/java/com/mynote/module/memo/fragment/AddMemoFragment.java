package com.mynote.module.memo.fragment;

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
import com.mynote.module.memo.bean.MemoBean;
import com.mynote.module.memo.dao.imp.MemoImp;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by Administrator on 2018/2/2.
 */

public class AddMemoFragment extends BaseFragment<MemoImp>  {
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
    private MemoBean memoBean;

    @Override
    protected int getContentView() {
        return R.layout.fragment_add_memo;
    }

    public static AddMemoFragment newInstance() {
        return newInstance(null);
    }

    public static AddMemoFragment newInstance(MemoBean bean) {
        Bundle args = new Bundle();
        if (bean != null) {
            args.putSerializable(IntentParam.editMemoBean, bean);
        }
        AddMemoFragment fragment = new AddMemoFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void initRxBus() {
        super.initRxBus();
        getRxBusEvent(SaveDataEvent.class, new MySubscriber<SaveDataEvent>() {
            @Override
            public void onMyNext(SaveDataEvent event) {
                if(event.index== SaveDataEvent.memoIndex){
                    String content = et_memo_content.getText().toString();
                    if (TextUtils.isEmpty(content)||content.trim().length()<=0) {
                        showToastS("内容不能为空");
                    } else {
                        String remark = et_memo_reminder.getText().toString();
                        MemoBean bean ;
                        if(isEdit){
                            bean=memoBean;
                            bean.setDataContent(content);
                            bean.setDataRemark(remark);
                            editMemo(bean);
                        }else{
                            bean=new MemoBean();
                            bean.setDataContent(content);
                            bean.setDataRemark(remark);
                            addMemo(bean);
                        }
                    }
                }
            }
        });
        getRxBusEvent(ClearDataEvent.class, new MySubscriber<ClearDataEvent>() {
            @Override
            public void onMyNext(ClearDataEvent event) {
                if(event.index== SaveDataEvent.memoIndex){
                    et_memo_reminder.setText(null);
                    et_memo_content.setText(null);
                    tv_memo_lengthprompt.setText("(0/3000)");
                }
            }
        });
    }
    private void editMemo(MemoBean bean) {
        showLoading();
        RXStart(pl_load,new IOCallBack<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                long count = mDaoImp.updateMemo(bean);
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

    private void addMemo(MemoBean bean) {
        showLoading();
        RXStart(pl_load,new IOCallBack< String >() {
            @Override
            public void call(Subscriber<? super  String > subscriber) {
                long addMemo = mDaoImp.addMemo(bean);
                subscriber.onNext(addMemo>0?"添加成功":"添加失败");
                subscriber.onCompleted();
            }
            @Override
            public void onMyNext( String  msg) {
                addDataSuccess=true;
                showMsg(msg);
                et_memo_reminder.setText(null);
                et_memo_content.setText(null);
                tv_memo_lengthprompt.setText("(0/3000)");
            }
        });
    }
    @Override
    protected void initView() {
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
        memoBean = (MemoBean) getArguments().getSerializable(IntentParam.editMemoBean);


        if (memoBean != null) {
            isEdit = true;
            et_memo_reminder.setText(memoBean.getDataRemark());
            et_memo_content.setText(memoBean.getDataContent());
            tv_memo_lengthprompt.setText("("+memoBean.getDataContent().length()+"/3000)");
        }
        setCreateTime(ll_update_time,tv_create_time, tv_update_time,isEdit,memoBean );
    }
    @OnClick({R.id.tv_memo_copy, R.id.tv_memo_paste, R.id.tv_memo_clear})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_memo_copy:
                String content = et_memo_content.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    showToastS("请填写数据之后复制");
                } else {
                    PhoneUtils.copyText(getActivity(), content);
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
    public void onDestroy() {
        super.onDestroy();
        if(addDataSuccess){
            RxBus.getInstance().post(new GetDataEvent(GetDataEvent.memoIndex));
        }
    }
}
