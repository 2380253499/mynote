package com.zr.note.ui.main.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.zr.note.R;
import com.zr.note.base.BaseActivity;
import com.zr.note.base.customview.MyRadioButton;
import com.zr.note.ui.main.contract.AddDataContract;
import com.zr.note.ui.main.contract.imp.AddDataImp;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddDataActivity extends BaseActivity<AddDataContract.View, AddDataContract.Presenter> implements AddDataContract.View {

//    @BindView(R.id.rg_addData)
//    RadioGroup rg_addData;
    @BindView(R.id.mrb_button0)
    MyRadioButton mrb_button0;
    @BindView(R.id.mrb_button1)
    MyRadioButton mrb_button1;
    @BindView(R.id.mrb_button2)
    MyRadioButton mrb_button2;
    @BindView(R.id.fl_fragment)
    FrameLayout fl_fragment;

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
//        setNavigationIcon( R.drawable.ic_menu_back);
    }

    @Override
    protected int setOptionsMenu() {
        return 0;
    }

    @Override
    protected void initView() {
        mrb_button0.setChecked(true);

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
