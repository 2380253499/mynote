package com.zr.note.ui.main.activity;

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
import com.zr.note.ui.main.contract.MainContract;
import com.zr.note.ui.main.contract.imp.MainImp;
import com.zr.note.view.MyPopupwindow;

import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends BaseActivity<MainContract.View,MainContract.Presenter>implements MainContract.View{
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

        findViewById(R.id.tv_a1).setOnClickListener(this);
        findViewById(R.id.tv_a2).setOnClickListener(this);
        findViewById(R.id.tv_a3).setOnClickListener(this);
        findViewById(R.id.tv_a4).setOnClickListener(this);
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
    private String[] getStartAndEndTime() {
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.getTime();
        String start=dateFormater.format(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        String end=dateFormater.format(cal.getTime());
        return new String[]{start,end};
    }
    String URL;
    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()){
            case R.id.tv_a3:
                break;
            case R.id.tv_a4:
                break;
            case R.id.tv_a2:
                break;
            case R.id.tv_a1:

                break;
            case R.id.fab:
                getStartAndEndTime();
//                startActivity(new Intent(MainActivity.this,SActivity.class));
                LocalDate today = LocalDate.now();
                today.minusDays(today.getMonthOfYear());
                today.dayOfMonth();
                today.getDayOfMonth();
                today.withDayOfMonth(today.getMonthOfYear());
//                today.get(DateTimeFieldType.millisOfDay());
                LocalDate a=new LocalDate(2017,01,02);
                a.getDayOfMonth();
                a.size();
                a.getDayOfYear();
                a.withDayOfMonth(1).plusWeeks(0);
                a.withDayOfMonth(1).plusWeeks(1);
                a.withDayOfMonth(1).plusWeeks(4);
                a.withDayOfMonth(1).plusWeeks(5);
                a.withDayOfWeek(7);
                LocalDate localDate = a.withDayOfMonth(1).withDayOfWeek(7);
                localDate.getDayOfWeek();
                localDate.getDayOfMonth();
                int dayOfYear = localDate.getDayOfYear();
                new LocalDate(a.withDayOfMonth(1).plusWeeks(5)).getDayOfWeek();
                new LocalDate(a.withDayOfMonth(1).plusWeeks(5)).getDayOfMonth();
                new LocalDate(a.withDayOfMonth(1).plusWeeks(5)).getMonthOfYear();//
                new LocalDate(a.withDayOfMonth(1).plusWeeks(4).withDayOfWeek(7)).getMonthOfYear();//
                int monthOfYear = a.getMonthOfYear();
                a.getWeekOfWeekyear();
                a.getWeekyear();
                a.getMonthOfYear();//
                a.getDayOfWeek();
                showToastS("" + monthOfYear);
            break;
        }
    }



    @Override
    protected void menuOnClick(int itemId) {
        switch (itemId){
            case R.id.action_settings:
//                startActivity(new Intent(MainActivity.this, SActivity.class));
                MyPopupwindow popupwindow=new MyPopupwindow(this,R.layout.layout_options);
                int xoff= PhoneUtils.getPhoneWidth(this)-PhoneUtils.dip2px(this, 115);
                popupwindow.showAsDropDown(getToolbar(),xoff,0);
                break;
        }
    }


    @Override
    protected MainImp initPresenter() {
        return new MainImp();
    }

}
