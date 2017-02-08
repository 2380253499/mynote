package com.zr.note.ui.secret.activity;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zr.note.R;
import com.zr.note.base.BaseActivity;
import com.zr.note.tools.MyDialog;
import com.zr.note.ui.main.entity.MemoBean;
import com.zr.note.ui.secret.activity.contract.AddSecretContract;
import com.zr.note.ui.secret.activity.contract.imp.AddSecretImp;

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

    @BindView(R.id.bt_addSecret_clear)
    Button bt_addSecret_clear;
    @BindView(R.id.bt_addSecret_save)
    Button bt_addSecret_save;
    boolean saveFlag;
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
        et_secret_content.requestFocus();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void viewOnClick(View v) {

    }
    @Override
    protected void menuOnClick(int itemId) {
        switch (itemId){
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
                String secretContent = et_secret_content.getText().toString();

                MemoBean memoBean=new MemoBean();
                memoBean.setDataRemark(secretReminder);
                memoBean.setDataContent(secretContent);
                mPresenter.addData(memoBean);
                break;
        }
    }

    @Override
    public void addDataResult(boolean isSuccess) {
        if(isSuccess){
            et_secret_reminder.setText(null);
            et_secret_content.setText(null);
            showMsg("保存成功");
            saveFlag=true;
        }else{
            showMsg("保存失败");
        }
    }

    @Override
    public void finishActivity(int requestCode) {
        super.finishActivity(requestCode);
        if(saveFlag){
            setResult(RESULT_OK);
        }
    }
}
