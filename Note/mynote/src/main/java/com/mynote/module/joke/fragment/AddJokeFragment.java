package com.mynote.module.joke.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.androidtools.PhoneUtils;
import com.github.customview.MyEditText;
import com.github.customview.MyTextView;
import com.github.rxbus.RxBus;
import com.mynote.IntentParam;
import com.mynote.R;
import com.mynote.base.BaseFragment;
import com.mynote.base.EventCallback;
import com.mynote.base.IOCallBack;
import com.mynote.event.ClearDataEvent;
import com.mynote.event.GetDataEvent;
import com.mynote.event.SaveDataEvent;
import com.mynote.module.joke.bean.JokeBean;
import com.mynote.module.joke.dao.imp.JokeImp;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.FlowableEmitter;

/**
 * Created by Administrator on 2018/2/2.
 */

public class AddJokeFragment extends BaseFragment<JokeImp> {
    @BindView(R.id.tv_joke_gxdz)
    MyTextView tv_joke_gxdz;
    @BindView(R.id.tv_joke_ssly)
    MyTextView tv_joke_ssly;
    @BindView(R.id.tv_joke_gxly)
    MyTextView tv_joke_gxly;
    @BindView(R.id.et_joke_remark)
    MyEditText et_joke_remark;
    @BindView(R.id.tv_joke_lengthprompt)
    TextView tv_joke_lengthprompt;
    @BindView(R.id.tv_joke_copy)
    TextView tv_joke_copy;
    @BindView(R.id.tv_joke_paste)
    TextView tv_joke_paste;
    @BindView(R.id.tv_joke_clear)
    TextView tv_joke_clear;
    @BindView(R.id.et_joke_content)
    MyEditText et_joke_content;


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
    private JokeBean jokeBean;

    @Override
    protected int getContentView() {
        return R.layout.fragment_add_joke;
    }

    public static AddJokeFragment newInstance() {
        return newInstance(null);
    }

    public static AddJokeFragment newInstance(JokeBean bean) {
        Bundle args = new Bundle();
        if (bean != null) {
            args.putSerializable(IntentParam.editJokeBean, bean);
        }
        AddJokeFragment fragment = new AddJokeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initRxBus() {
        super.initRxBus();
        getEvent(SaveDataEvent.class, new EventCallback<SaveDataEvent>() {
            @Override
            public void accept(SaveDataEvent event) {
                if(event.index== SaveDataEvent.jokeIndex){
                    String content = et_joke_content.getText().toString();
                    if (TextUtils.isEmpty(content)||content.trim().length()<=0) {
                        showToastS("内容不能为空");
                    } else {
                        String remark = et_joke_remark.getText().toString();
                        JokeBean bean ;
                        if(isEdit){
                            bean=jokeBean;
                            bean.setDataContent(content);
                            bean.setDataRemark(remark);
                            editJoke(bean);
                        }else{
                            bean=new JokeBean();
                            bean.setDataContent(content);
                            bean.setDataRemark(remark);
                            addJoke(bean);
                        }
                    }
                }
            }
        });
        getEvent(ClearDataEvent.class, new EventCallback<ClearDataEvent>() {
            @Override
            public void accept(ClearDataEvent event) {
                if(event.index== SaveDataEvent.jokeIndex){
                    et_joke_remark.setText(null);
                    et_joke_content.setText(null);
                    tv_joke_lengthprompt.setText("(0/3000)");
                }
            }
        });
    }
    private void editJoke(JokeBean bean) {
        showLoading();
        RXStart(pl_load,new IOCallBack<String>() {
            @Override
            public void call(FlowableEmitter<String> subscriber) {
                long count = mDaoImp.updateJoke(bean);
                subscriber.onNext(count>0?"修改成功":"修改失败");
                subscriber.onComplete();
            }
            @Override
            public void onMyNext(String msg) {
                addDataSuccess=true;
                showMsg(msg);
            }
        });
    }

    private void addJoke(JokeBean bean) {
        showLoading();
        RXStart(pl_load,new IOCallBack<String>() {
            @Override
            public void call(FlowableEmitter<String> subscriber) {
                long addJoke = mDaoImp.addJoke(bean);
                subscriber.onNext(addJoke>0?"添加成功":"添加失败");
                subscriber.onComplete();
            }
            @Override
            public void onMyNext(String msg) {
                showMsg(msg);
                addDataSuccess=true;
                et_joke_remark.setText(null);
                et_joke_content.setText(null);
                tv_joke_lengthprompt.setText("(0/3000)");
            }
        });
    }
    @Override
    protected void initView() {
        et_joke_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                tv_joke_lengthprompt.setText("("+length+"/3000)");
            }
        });
    }

    @Override
    protected void initData() {
        jokeBean = (JokeBean) getArguments().getSerializable(IntentParam.editJokeBean);



        if (jokeBean != null) {
            isEdit = true;
            et_joke_remark.setText(jokeBean.getDataRemark());
            et_joke_content.setText(jokeBean.getDataContent());
            tv_joke_lengthprompt.setText("("+jokeBean.getDataContent().length()+"/3000)");
        }

        setCreateTime(ll_update_time,tv_create_time, tv_update_time,isEdit,jokeBean );
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(addDataSuccess){
            RxBus.getInstance().post(new GetDataEvent(GetDataEvent.jokeIndex));
        }
    }
    @OnClick({R.id.tv_joke_gxdz, R.id.tv_joke_ssly, R.id.tv_joke_gxly, R.id.tv_joke_copy, R.id.tv_joke_paste, R.id.tv_joke_clear})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_joke_gxdz:
                et_joke_remark.setText(et_joke_remark.getText());
                if(getSStr(et_joke_remark).indexOf("搞笑段子")<0){
                    et_joke_remark.setText(getSStr(et_joke_remark)+"搞笑段子");
                }
                break;
            case R.id.tv_joke_ssly:et_joke_remark.setText(et_joke_remark.getText());
                if(getSStr(et_joke_remark).indexOf("说说留言")<0){
                    et_joke_remark.setText(getSStr(et_joke_remark)+"说说留言");
                }
                break;
            case R.id.tv_joke_gxly:et_joke_remark.setText(et_joke_remark.getText());
                if(getSStr(et_joke_remark).indexOf("搞笑留言")<0){
                    et_joke_remark.setText(getSStr(et_joke_remark)+"搞笑留言");
                }
                break;
            case R.id.tv_joke_copy:
                String content = et_joke_content.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    showToastS("请填写数据之后复制");
                } else {
                    PhoneUtils.copyText(getActivity(), content);
                    showToastS("复制成功");
                }
                break;
            case R.id.tv_joke_paste:
                et_joke_content.setText(et_joke_content.getText()+""+PhoneUtils.pasteText(getActivity()));
                break;
            case R.id.tv_joke_clear:
                et_joke_content.setText(null);
                break;
        }
    }
}
