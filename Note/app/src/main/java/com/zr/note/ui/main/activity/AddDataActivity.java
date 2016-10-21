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
import com.zr.note.ui.main.fragment.AddJokeFragment;
import com.zr.note.ui.main.fragment.AddMemoFragment;
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
    private AddMemoFragment addMemoFragment;
    private AddJokeFragment addJokeFragment;
    private AddSpendFragment addSpendFragment;
    private boolean saveDataIsSuccess;
    private boolean addDataFlag;

    @Override
    protected AddDataImp initPresenter() {
        return new AddDataImp(this);
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
        int tabIndex = getIntent().getIntExtra(IntentParam.tabIndex, 0);
        addDataInterIndex=tabIndex;
        setCheckDiffTab(tabIndex);
        bt_addData_save.setOnClickListener(this);
        rg_addData.setOnCheckedChangeListener(getChangeListener());
    }

    private void setCheckDiffTab(int tabIndex) {
        switch (tabIndex){
            case 0:
                addAccountFragment = AddAccountFragment.newInstance();
                addDataInter[0] = addAccountFragment;
                addFragment(R.id.fl_fragment, addAccountFragment);
                mrb_button0.setChecked(true);
            break;
            case 1:
                mrb_button1.setChecked(true);
                addMemoFragment = AddMemoFragment.newInstance();
                addDataInter[1] = addMemoFragment;
                addFragment(R.id.fl_fragment, addMemoFragment);
            break;
            case 2:
                mrb_button2.setChecked(true);
                addJokeFragment =   AddJokeFragment.newInstance();
                addDataInter[2] = addJokeFragment;
                addFragment(R.id.fl_fragment, addJokeFragment);
            break;
            case 3:
                mrb_button3.setChecked(true);
                addSpendFragment =   AddSpendFragment.newInstance();
                addDataInter[3] = addSpendFragment;
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
                            addDataInter[0] = addAccountFragment;
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
                            addDataInter[1] = addMemoFragment;
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
                            addDataInter[2] = addJokeFragment;
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
                            addDataInter[3] = addSpendFragment;
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
        mIntent.putExtra(IntentParam.addDataIndex,addDataInterIndex);
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
