package com.zr.note.ui.main.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.zr.note.R;
import com.zr.note.base.BaseActivity;
import com.zr.note.base.customview.MyRadioButton;
import com.zr.note.ui.main.contract.AddDataContract;
import com.zr.note.ui.main.contract.imp.AddDataImp;
import com.zr.note.ui.main.fragment.AccountFragment;
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
    @BindView(R.id.fl_fragment)
    FrameLayout fl_fragment;

    private AddDataInter addDataInter0,addDataInter1,addDataInter2;
    private AccountFragment accountFragment;

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
        addDataInter0= accountFragment;
        addFragment( R.id.fl_fragment, accountFragment);

        rg_addData.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.mrb_button0:
                    break;
                    case R.id.mrb_button1:
                    break;
                    case R.id.mrb_button2:
                    break;
                }
            }
        });
    }
    @Override
    protected void initData() {

    }

    @Override
    protected void viewOnClick(View v) {

    }

    @Override
    protected void menuOnClick(int itemId) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
