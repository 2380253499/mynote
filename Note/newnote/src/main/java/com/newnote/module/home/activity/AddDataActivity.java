package com.newnote.module.home.activity;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.github.androidtools.PhoneUtils;
import com.github.baseclass.rx.RxBus;
import com.github.baseclass.view.MyDialog;
import com.github.customview.MyRadioButton;
import com.newnote.R;
import com.newnote.base.BaseActivity;
import com.newnote.module.account.entity.AccountBean;
import com.newnote.module.home.Constant;
import com.newnote.module.home.event.AddDataEvent;
import com.newnote.module.account.fragment.AddAccountFragment;
import com.newnote.module.home.fragment.AddJokeFragment;
import com.newnote.module.home.fragment.AddMemoFragment;
import com.newnote.module.home.fragment.AddSpendFragment;
import com.newnote.module.joke.entity.JokeBean;
import com.newnote.module.memo.entity.MemoBean;
import com.newnote.module.spend.entity.SpendBean;

import butterknife.BindView;

public class AddDataActivity extends BaseActivity {

    @BindView(R.id.rg_addData)
    RadioGroup rg_addData;
    @BindView(R.id.mrb_button0)
    MyRadioButton mrb_button0;
    @BindView(R.id.mrb_button1)
    MyRadioButton mrb_button1;
    @BindView(R.id.mrb_button2)
    MyRadioButton mrb_button2;
    @BindView(R.id.mrb_button3)
    MyRadioButton mrb_button3;
    @BindView(R.id.fl_fragment)
    FrameLayout fl_fragment;
    @BindView(R.id.bt_addData_clear)
    Button bt_addData_clear;
    @BindView(R.id.bt_addData_save)
    Button bt_addData_save;

    private int addDataIndex =0;
    private AddAccountFragment addAccountFragment;
    private AddMemoFragment addMemoFragment;
    private AddJokeFragment addJokeFragment;
    private AddSpendFragment addSpendFragment;

    private AccountBean accountBean;
    private MemoBean memoBean;
    private JokeBean jokeBean;
    private SpendBean spendBean;

    @Override
    protected int getContentView() {
        setAppTitle("信息录入");
        return R.layout.activity_add_data;
    }

    @Override
    protected void initView() {
        addDataIndex = getIntent().getIntExtra(Constant.IParam.tabIndex, 0);
        accountBean= (AccountBean) getIntent().getSerializableExtra(Constant.IParam.editAccount);
        memoBean= (MemoBean) getIntent().getSerializableExtra(Constant.IParam.editMemoBean);
        jokeBean= (JokeBean) getIntent().getSerializableExtra(Constant.IParam.editJokeBean);
        spendBean= (SpendBean) getIntent().getSerializableExtra(Constant.IParam.editSpendBean);
        setCheckDiffTab(addDataIndex);
        bt_addData_clear.setOnClickListener(this);
        bt_addData_save.setOnClickListener(this);
        rg_addData.setOnCheckedChangeListener(getChangeListener());

    }

    private void setCheckDiffTab(int tabIndex) {
        switch (tabIndex){
            case 0:
                addAccountFragment = AddAccountFragment.newInstance(accountBean);
                addFragment(R.id.fl_fragment, addAccountFragment);
                mrb_button0.setChecked(true);
            break;
            case 1:
                mrb_button1.setChecked(true);
                addMemoFragment = AddMemoFragment.newInstance(memoBean);
                addFragment(R.id.fl_fragment, addMemoFragment);
            break;
            case 2:
                mrb_button2.setChecked(true);
                addJokeFragment =   AddJokeFragment.newInstance(jokeBean);
                addFragment(R.id.fl_fragment, addJokeFragment);
            break;
            case 3:
                mrb_button3.setChecked(true);
                addSpendFragment =   AddSpendFragment.newInstance(spendBean);
                addFragment(R.id.fl_fragment, addSpendFragment);
            break;
        }
    }

    @NonNull
    private RadioGroup.OnCheckedChangeListener getChangeListener() {
        return new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.mrb_button0:
                        addDataIndex =0;
                        if (addAccountFragment == null) {
                            addAccountFragment = AddAccountFragment.newInstance();
                            addFragment(R.id.fl_fragment, addAccountFragment);
                            hideFragment(addMemoFragment);
                            hideFragment(addJokeFragment);
                            hideFragment(addSpendFragment);
                        } else {
                            showFragment(addAccountFragment);
                            hideFragment(addMemoFragment);
                            hideFragment(addJokeFragment);
                            hideFragment(addSpendFragment);
                        }
                        break;
                    case R.id.mrb_button1:
                        addDataIndex =1;
                        if (addMemoFragment == null) {
                            addMemoFragment = AddMemoFragment.newInstance();
                            hideFragment(addAccountFragment);
                            addFragment(R.id.fl_fragment, addMemoFragment);
                            hideFragment(addJokeFragment);
                            hideFragment(addSpendFragment);
                        } else {
                            hideFragment(addAccountFragment);
                            showFragment(addMemoFragment);
                            hideFragment(addJokeFragment);
                            hideFragment(addSpendFragment);
                        }
                        break;
                    case R.id.mrb_button2:
                        addDataIndex =2;
                        if (addJokeFragment == null) {
                            addJokeFragment = AddJokeFragment.newInstance();
                            hideFragment(addAccountFragment);
                            hideFragment(addMemoFragment);
                            addFragment(R.id.fl_fragment, addJokeFragment);
                            hideFragment(addSpendFragment);
                        } else {
                            hideFragment(addAccountFragment);
                            hideFragment(addMemoFragment);
                            showFragment(addJokeFragment);
                            hideFragment(addSpendFragment);
                        }
                        break;
                    case R.id.mrb_button3:
                        addDataIndex =3;
                        if (addSpendFragment == null) {
                            addSpendFragment = AddSpendFragment.newInstance();
                            hideFragment(addAccountFragment);
                            hideFragment(addMemoFragment);
                            hideFragment(addJokeFragment);
                            addFragment(R.id.fl_fragment, addSpendFragment);
                        } else {
                            hideFragment(addAccountFragment);
                            hideFragment(addMemoFragment);
                            hideFragment(addJokeFragment);
                            showFragment(addSpendFragment);
                        }
                        break;
                }
            }
        };
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.bt_addData_clear:
                mDialog=new MyDialog.Builder(this);
                mDialog.setMessage("确定清空页面已输入的数据吗?");
                mDialog.setPositiveButton(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        RxBus.getInstance().post(new AddDataEvent(addDataIndex,false));
                    }
                });
                mDialog.setNegativeButton(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mDialog.create().show();
                PhoneUtils.hiddenKeyBoard(this);
                break;
            case R.id.bt_addData_save:
                PhoneUtils.hiddenKeyBoard(this);
                RxBus.getInstance().post(new AddDataEvent(addDataIndex,true));
                break;
        }
    }
}
