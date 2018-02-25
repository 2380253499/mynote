package com.mynote.module.home.activity;

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
import com.mynote.IntentParam;
import com.mynote.R;
import com.mynote.base.BaseActivity;
import com.mynote.event.ClearDataEvent;
import com.mynote.event.SaveDataEvent;
import com.mynote.module.account.bean.AccountBean;
import com.mynote.module.account.fragment.AddAccountFragment;
import com.mynote.module.home.inter.AddDataInter;
import com.mynote.module.joke.bean.JokeBean;
import com.mynote.module.joke.fragment.AddJokeFragment;
import com.mynote.module.memo.bean.MemoBean;
import com.mynote.module.memo.fragment.AddMemoFragment;
import com.mynote.module.spend.bean.SpendBean;
import com.mynote.module.spend.fragment.AddSpendFragment;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/2/2.
 */

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

    private int addDataInterIndex=0;
    private AddDataInter[]addDataInter=new AddDataInter[4];
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
        int tabIndex = getIntent().getIntExtra(IntentParam.tabIndex, 0);
        accountBean= (AccountBean) getIntent().getSerializableExtra(IntentParam.editAccount);
        memoBean= (MemoBean) getIntent().getSerializableExtra(IntentParam.editMemoBean);
        jokeBean= (JokeBean) getIntent().getSerializableExtra(IntentParam.editJokeBean);
        spendBean= (SpendBean) getIntent().getSerializableExtra(IntentParam.editSpendBean);
        addDataInterIndex=tabIndex;
        setCheckDiffTab(tabIndex);
        bt_addData_clear.setOnClickListener(this);
        bt_addData_save.setOnClickListener(this);
        rg_addData.setOnCheckedChangeListener(getChangeListener());
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
//                        addDataInter[addDataInterIndex].clearData();
                        RxBus.getInstance().post(new ClearDataEvent(addDataInterIndex));
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
                RxBus.getInstance().post(new SaveDataEvent(addDataInterIndex));
                PhoneUtils.hiddenKeyBoard(this);
                break;
        }
    }
    private void setCheckDiffTab(int tabIndex) {
        switch (tabIndex){
            case 0:
                addAccountFragment = AddAccountFragment.newInstance(accountBean);
//                addDataInter[0] = addAccountFragment;
                addFragment(R.id.fl_fragment, addAccountFragment);
                mrb_button0.setChecked(true);
                break;
            case 1:
                mrb_button1.setChecked(true);
                addMemoFragment = AddMemoFragment.newInstance(memoBean);
//                addDataInter[1] = addMemoFragment;
                addFragment(R.id.fl_fragment, addMemoFragment);
                break;
            case 2:
                mrb_button2.setChecked(true);
                addJokeFragment =   AddJokeFragment.newInstance(jokeBean);
//                addDataInter[2] = addJokeFragment;
                addFragment(R.id.fl_fragment, addJokeFragment);
                break;
            case 3:
                mrb_button3.setChecked(true);
                addSpendFragment =   AddSpendFragment.newInstance(spendBean);
//                addDataInter[3] = addSpendFragment;
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
                        addDataInterIndex=0;
                        if (addAccountFragment == null) {
                            addAccountFragment = AddAccountFragment.newInstance();
//                            addDataInter[0] = addAccountFragment;
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
                        addDataInterIndex=1;
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
                        addDataInterIndex=2;
                        if (addJokeFragment == null) {
                            addJokeFragment = AddJokeFragment.newInstance();
//                            addDataInter[2] = addJokeFragment;
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
                        addDataInterIndex=3;
                        if (addSpendFragment == null) {
                            addSpendFragment = AddSpendFragment.newInstance();
//                            addDataInter[3] = addSpendFragment;
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
}
