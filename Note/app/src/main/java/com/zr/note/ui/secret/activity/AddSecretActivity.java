package com.zr.note.ui.secret.activity;

import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zr.note.R;
import com.zr.note.base.BaseActivity;
import com.zr.note.tools.MyDialog;
import com.zr.note.tools.PhoneUtils;
import com.zr.note.ui.main.entity.MemoBean;
import com.zr.note.ui.secret.activity.contract.AddSecretContract;
import com.zr.note.ui.secret.activity.contract.imp.AddSecretImp;
import com.zr.note.ui.secret.constant.IntentParam;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/2/6.
 */
public class AddSecretActivity extends BaseActivity<AddSecretContract.View,AddSecretContract.Presenter> implements AddSecretContract.View {


    @BindView(R.id.et_secret_reminder)
    EditText et_secret_reminder;

    @BindView(R.id.tv_secret_lengthprompt)
    TextView tv_secret_lengthprompt;

    @BindView(R.id.et_secret_content)
    EditText et_secret_content;
    @BindView(R.id.tv_secret_copy)
    TextView tv_secret_copy;
    @BindView(R.id.tv_secret_paste)
    TextView tv_secret_paste;

    @BindView(R.id.bt_addSecret_clear)
    Button bt_addSecret_clear;
    @BindView(R.id.bt_addSecret_save)
    Button bt_addSecret_save;
    boolean saveFlag;
    boolean isEditBean;
    private MemoBean editBean;
    @Override
    protected AddSecretImp initPresenter() {
        return new AddSecretImp(this);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_add_secret;
    }

    @Override
    protected void setToolbarStyle() {
        setTitle("添加数据");
    }

    @Override
    protected int setOptionsMenu() {
        return 0;
    }

    @Override
    protected void initView() {
        bt_addSecret_clear.setOnClickListener(this);
        bt_addSecret_save.setOnClickListener(this);
        tv_secret_copy.setOnClickListener(this);
        tv_secret_paste.setOnClickListener(this);
        et_secret_content.requestFocus();
        et_secret_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                tv_secret_lengthprompt.setText("("+s.length()+"/5000)");
            }
        });
    }

    @Override
    protected void initData() {
        editBean= (MemoBean) getIntent().getSerializableExtra(IntentParam.editData);
        if(editBean!=null){
            isEditBean=true;
            et_secret_reminder.setText(editBean.getDataRemark());
            et_secret_content.setText(editBean.getDataContent());
        }
    }

    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()){
            case R.id.bt_addSecret_clear:
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
            case R.id.bt_addSecret_save:
                String secretReminder = et_secret_reminder.getText().toString();
                String secretContent = et_secret_content.getText().toString().trim();
                if(TextUtils.isEmpty(secretContent)){
                    showMsg("数据内容不能为空");
                    return;
                }
                MemoBean memoBean=new MemoBean();
                memoBean.setDataRemark(secretReminder);
                memoBean.setDataContent(secretContent);
                if(isEditBean){
                    memoBean.set_id(editBean.get_id());
                    mPresenter.editData(memoBean);
                }else{
                    mPresenter.addData(memoBean);
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
                String content = PhoneUtils.pasteText(this);
                et_secret_content.setText(et_secret_content.getText()+""+content);
                break;
        }
    }
    @Override
    protected void menuOnClick(int itemId) {

    }

    @Override
    public void addDataResult(boolean isEdit,boolean isSuccess) {
        if(isEdit){
            if(isSuccess){
                showMsg("修改成功");
                saveFlag=true;
            }else{
                showMsg("修改失败");
            }
        }else{
            if(isSuccess){
                et_secret_reminder.setText(null);
                et_secret_content.setText(null);
                showMsg("保存成功");
                saveFlag=true;
            }else{
                showMsg("保存失败");
            }
        }
    }

    @Override
    public void finish() {
        if(saveFlag){
            setResult(RESULT_OK);
        }
        super.finish();
    }
}
