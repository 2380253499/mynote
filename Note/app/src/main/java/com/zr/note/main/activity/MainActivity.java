package com.zr.note.main.activity;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zr.note.R;
import com.zr.note.base.BaseActivity;
import com.zr.note.inter.MyOnClickListener;
import com.zr.note.main.contract.imp.MainImp;
import com.zr.note.main.view.MainView;

public class MainActivity extends BaseActivity<MainView,MainImp>implements MainView{
    private DrawerLayout drawerLayout;
    private CollapsingToolbarLayout ctl_layout;
    private TextView tv_a1,tv_a2;
    private FloatingActionButton fab;

    @Override
    protected int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        getToolbar().setNavigationOnClickListener(new MyOnClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        drawerLayout= (DrawerLayout) findViewById(R.id.drawerlayout);
        ctl_layout= (CollapsingToolbarLayout) findViewById(R.id.ctl_layout);
        ctl_layout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        ctl_layout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
    }
    @Override
    protected void setToolbarStyle() {
        setToolbarTitle("Note");
        setNavigationIcon(R.drawable.drawer_menu);
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
        switch (v.getId()){
            case R.id.fab:
                startActivity(new Intent(MainActivity.this,SActivity.class));
            break;
        }
    }

    @Override
    protected void menuOnClick(int itemId) {
        switch (itemId){
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SActivity.class));
                break;
        }
    }


    @Override
    protected MainImp initPresenter() {
        return new MainImp();
    }

    @Override
    public void show() {

    }
}
