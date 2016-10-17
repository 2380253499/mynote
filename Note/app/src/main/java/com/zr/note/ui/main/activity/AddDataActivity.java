package com.zr.note.ui.main.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.zr.note.R;
import com.zr.note.base.BaseActivity;
import com.zr.note.base.customview.MyButton;
import com.zr.note.base.customview.MyRadioButton;
import com.zr.note.ui.main.activity.contract.AddDataContract;
import com.zr.note.ui.main.activity.contract.imp.AddDataImp;
import com.zr.note.ui.main.constant.IntentParam;
import com.zr.note.ui.main.constant.RequestCode;
import com.zr.note.ui.main.fragment.AddAccountFragment;
import com.zr.note.ui.main.fragment.AddDailyReminderFragment;
import com.zr.note.ui.main.fragment.AddJokeFragment;
import com.zr.note.ui.main.fragment.AddSpendFragment;
import com.zr.note.ui.main.inter.AddDataInter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddDataActivity extends BaseActivity<AddDataContract.View, AddDataContract.Presenter> implements AddDataContract.View {

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
    @BindView(R.id.bt_addData_save)
    MyButton bt_addData_save;

    private int addDataInterIndex=0;
    private AddDataInter[]addDataInter=new AddDataInter[4];
    private AddAccountFragment addAccountFragment;
    private AddDailyReminderFragment addDailyReminderFragment;
    private AddJokeFragment addJokeFragment;
    private AddSpendFragment addSpendFragment;
    private boolean saveDataIsSuccess;
    private boolean addDataFlag;

    @Override
    protected AddDataImp initPresenter() {
        return new AddDataImp();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_add_data;
    }
    @Override
    protected void setToolbarStyle() {
        setTitle("信息录入");

    }

    @Override
    protected int setOptionsMenu() {
        return 0;
    }

    @Override
    protected void initView() {
        mrb_button0.setChecked(true);
        addAccountFragment = new AddAccountFragment();
        addDataInter[0] = addAccountFragment;
        addFragment(R.id.fl_fragment, addAccountFragment);

        bt_addData_save.setOnClickListener(this);

        rg_addData.setOnCheckedChangeListener(getChangeListener());
    }

    @NonNull
    private RadioGroup.OnCheckedChangeListener getChangeListener() {
        return new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.mrb_button0:
                        addDataInterIndex=0;
                        showFragment(addAccountFragment);
                        hideFragment(addDailyReminderFragment);
                        hideFragment(addJokeFragment);
                        hideFragment(addSpendFragment);
                        break;
                    case R.id.mrb_button1:
                        addDataInterIndex=1;
                        if (addDailyReminderFragment == null) {
                            addDailyReminderFragment = AddDailyReminderFragment.newInstance();
                            addDataInter[1] = addDailyReminderFragment;
                            hideFragment(addAccountFragment);
                            addFragment(R.id.fl_fragment, addDailyReminderFragment);
                            hideFragment(addJokeFragment);
                            hideFragment(addSpendFragment);
                        } else {
                            hideFragment(addAccountFragment);
                            showFragment(addDailyReminderFragment);
                            hideFragment(addJokeFragment);
                            hideFragment(addSpendFragment);
                        }
                        break;
                    case R.id.mrb_button2:
                        addDataInterIndex=2;
                        if (addJokeFragment == null) {
                            addJokeFragment = AddJokeFragment.newInstance();
                            addDataInter[2] = addJokeFragment;
                            hideFragment(addAccountFragment);
                            hideFragment(addDailyReminderFragment);
                            addFragment(R.id.fl_fragment, addJokeFragment);
                            hideFragment(addSpendFragment);
                        } else {
                            hideFragment(addAccountFragment);
                            hideFragment(addDailyReminderFragment);
                            showFragment(addJokeFragment);
                            hideFragment(addSpendFragment);
                        }
                        break;
                    case R.id.mrb_button3:
                        addDataInterIndex=3;
                        if (addSpendFragment == null) {
                            addSpendFragment = AddSpendFragment.newInstance();
                            addDataInter[3] = addSpendFragment;
                            hideFragment(addAccountFragment);
                            hideFragment(addDailyReminderFragment);
                            hideFragment(addJokeFragment);
                            addFragment(R.id.fl_fragment, addSpendFragment);
                        } else {
                            hideFragment(addAccountFragment);
                            hideFragment(addDailyReminderFragment);
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
    protected void viewOnClick(View v) {
        switch (v.getId()){
            case R.id.bt_addData_save:
                addDataFlag = addDataInter[addDataInterIndex].saveData();
                break;
        }
    }

    @Override
    protected void menuOnClick(int itemId) {
    }

    @Override
    public void finish() {
        mIntent.putExtra(IntentParam.addDataCode,addDataFlag);
        setResult(RequestCode.addDataResultCode, mIntent);
        super.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
