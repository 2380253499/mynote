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
import com.zr.note.ui.main.fragment.AccountFragment;
import com.zr.note.ui.main.fragment.DailyReminderFragment;
import com.zr.note.ui.main.fragment.JokeFragment;
import com.zr.note.ui.main.fragment.SpendFragment;
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
    private AccountFragment accountFragment;
    private DailyReminderFragment dailyReminderFragment;
    private JokeFragment jokeFragment;
    private SpendFragment spendFragment;
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
        accountFragment = new AccountFragment();
        addDataInter[0] = accountFragment;
        addFragment(R.id.fl_fragment, accountFragment);

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
                        showFragment(accountFragment);
                        hideFragment(dailyReminderFragment);
                        hideFragment(jokeFragment);
                        hideFragment(spendFragment);
                        break;
                    case R.id.mrb_button1:
                        addDataInterIndex=1;
                        if (dailyReminderFragment == null) {
                            dailyReminderFragment= DailyReminderFragment.newInstance();
                            addDataInter[1] = dailyReminderFragment;
                            hideFragment(accountFragment);
                            addFragment(R.id.fl_fragment, dailyReminderFragment);
                            hideFragment(jokeFragment);
                            hideFragment(spendFragment);
                        } else {
                            hideFragment(accountFragment);
                            showFragment(dailyReminderFragment);
                            hideFragment(jokeFragment);
                            hideFragment(spendFragment);
                        }
                        break;
                    case R.id.mrb_button2:
                        addDataInterIndex=2;
                        if (jokeFragment == null) {
                            jokeFragment= JokeFragment.newInstance();
                            addDataInter[2] = jokeFragment;
                            hideFragment(accountFragment);
                            hideFragment(dailyReminderFragment);
                            addFragment(R.id.fl_fragment, jokeFragment);
                            hideFragment(spendFragment);
                        } else {
                            hideFragment(accountFragment);
                            hideFragment(dailyReminderFragment);
                            showFragment(jokeFragment);
                            hideFragment(spendFragment);
                        }
                        break;
                    case R.id.mrb_button3:
                        addDataInterIndex=3;
                        if (spendFragment == null) {
                            spendFragment= SpendFragment.newInstance();
                            addDataInter[3] = spendFragment;
                            hideFragment(accountFragment);
                            hideFragment(dailyReminderFragment);
                            hideFragment(jokeFragment);
                            addFragment(R.id.fl_fragment, spendFragment);
                        } else {
                            hideFragment(accountFragment);
                            hideFragment(dailyReminderFragment);
                            hideFragment(jokeFragment);
                            showFragment(spendFragment);
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
                addDataInter[addDataInterIndex].saveData();
            break;
        }
    }

    @Override
    protected void menuOnClick(int itemId) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
