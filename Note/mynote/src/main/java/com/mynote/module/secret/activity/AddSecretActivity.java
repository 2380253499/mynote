package com.mynote.module.secret.activity;

import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.androidtools.PhoneUtils;
import com.github.baseclass.view.MyDialog;
import com.github.customview.MyEditText;
import com.mynote.IntentParam;
import com.mynote.R;
import com.mynote.base.BaseActivity;
import com.mynote.base.IOCallBack;
import com.mynote.module.secret.bean.SecretBean;
import com.mynote.module.secret.dao.imp.SecretImp;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.FlowableEmitter;

/**
 * Created by Administrator on 2017/2/6.
 */
public class AddSecretActivity extends BaseActivity<SecretImp> {

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
        setAppTitle("添加数据");
        return R.layout.activity_add_secret;
    }
    private void editSecret(SecretBean bean) {
        showLoading();
        RXStart(pl_load,new IOCallBack<String>() {
            @Override
            public void call(FlowableEmitter<String> subscriber) {
                long count = mDaoImp.updateSecret(bean);
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

    private void addSecret(SecretBean bean) {
        showLoading();
        RXStart(pl_load,new IOCallBack<String>() {
            @Override
            public void call(FlowableEmitter<String> subscriber) {
                long addSecret = mDaoImp.addSecret(bean);
                subscriber.onNext(addSecret>0?"添加成功":"添加失败");
                subscriber.onComplete();
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
        secretBean= (SecretBean) getIntent().getSerializableExtra(IntentParam.editSecretBean);

        setCreateTime(ll_update_time,tv_create_time, tv_update_time,isEdit,secretBean );

        if (secretBean != null) {
            isEdit = true;
            et_secret_reminder.setText(secretBean.getDataRemark());
            et_secret_content.setText(secretBean.getDataContent());
            tv_secret_lengthprompt.setText("("+secretBean.getDataContent().length()+"/3000)");
        }
    }
    @OnClick({R.id.tv_secret_copy, R.id.tv_secret_paste, R.id.tv_secret_clear,R.id.bt_addData_clear,R.id.bt_addData_save})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.bt_addData_clear:
                mDialog=new MyDialog.Builder(this);
                mDialog.setMessage("确定清空页面已输入的数据吗?");
                mDialog.setPositiveButton(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        et_secret_reminder.setText(null);
                        et_secret_content.setText(null);
                    }
                });
                mDialog.setNegativeButton(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mDialog.create().show();
                break;
            case R.id.bt_addData_save:
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
                break;
            case R.id.tv_secret_copy:
                String jokeContent = et_secret_content.getText().toString().trim();
                if (TextUtils.isEmpty(jokeContent)) {
                    showToastS("数据内容不能为空");
                } else {
                    PhoneUtils.copyText(this, et_secret_content.getText().toString().trim());
                    showToastS("复制成功");
                }
                break;
            case R.id.tv_secret_paste:
                et_secret_content.setText(et_secret_content.getText()+""+PhoneUtils.pasteText(this));
                break;
            case R.id.tv_secret_clear:
                et_secret_content.setText(null);
                break;
        }
    }


    @Override
    public void finish() {
        if(addDataSuccess){
            setResult(RESULT_OK);
        }
        super.finish();
    }
}
