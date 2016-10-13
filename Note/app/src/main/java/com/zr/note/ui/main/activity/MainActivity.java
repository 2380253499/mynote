package com.zr.note.ui.main.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zr.note.R;
import com.zr.note.base.BaseActivity;
import com.zr.note.inter.MyOnClickListener;
import com.zr.note.tools.PhoneUtils;
import com.zr.note.ui.main.activity.contract.MainContract;
import com.zr.note.ui.main.activity.contract.imp.MainImp;
import com.zr.note.view.MyPopupwindow;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity<MainContract.View, MainContract.Presenter> implements MainContract.View {
    @BindView(R.id.ctl_layout)
    CollapsingToolbarLayout ctl_layout;
    @BindView(R.id.drawerlayout)
    DrawerLayout drawerlayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private long exitTime;
    private TextView tv_a1, tv_a2;

    @Override
    protected int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        getToolbar().setNavigationOnClickListener(new MyOnClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                drawerlayout.openDrawer(Gravity.LEFT);
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        ctl_layout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        ctl_layout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));

    }

    @Override
    protected void setToolbarStyle() {
        setToolbarTitle("Note");
        setNavigationIcon(R.drawable.drawer_menu);
        getToolbar().setBackgroundColor(getResources().getColor(R.color.transparent));
    }

    @Override
    protected int setOptionsMenu() {
        return R.menu.menu_main;
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                STActivity(AddDataActivity.class);
                break;
        }
    }


    @Override
    protected void menuOnClick(int itemId) {
        switch (itemId) {
            case R.id.action_settings:
//                startActivity(new Intent(MainActivity.this, SActivity.class));
                MyPopupwindow popupwindow = new MyPopupwindow(this, R.layout.layout_options);
                int xoff = PhoneUtils.getPhoneWidth(this) - PhoneUtils.dip2px(this, 115);
                popupwindow.showAsDropDown(getToolbar(), xoff, 0);
                break;
        }
    }


    @Override
    protected MainImp initPresenter() {
        return new MainImp();
    }


    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 1500) {
            showToastS("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
