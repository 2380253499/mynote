package com.mynote.module.home.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import com.github.androidtools.PhoneUtils;
import com.github.androidtools.inter.MyOnClickListener;
import com.github.baseclass.view.MyPopupwindow;
import com.github.customview.MyRadioButton;
import com.github.rxbus.RxBus;
import com.mynote.IntentParam;
import com.mynote.R;
import com.mynote.base.BaseActivity;
import com.mynote.base.EventCallback;
import com.mynote.event.OptionEvent;
import com.mynote.module.account.fragment.AccountFragment;
import com.mynote.module.joke.fragment.JokeFragment;
import com.mynote.module.memo.fragment.MemoFragment;
import com.mynote.module.spend.fragment.SpendFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


//    @BindView(R.id.ctl_layout)
//    CollapsingToolbarLayout ctl_layout;
    @BindView(R.id.fl_content)
    FrameLayout fl_content;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.rb_main_account)
    MyRadioButton rb_main_account;
    @BindView(R.id.rb_main_memo)
    MyRadioButton rb_main_memo;
    @BindView(R.id.rb_main_joke)
    MyRadioButton rb_main_joke;
    @BindView(R.id.rb_main_spend)
    MyRadioButton rb_main_spend;
    @BindView(R.id.rg_main)
    RadioGroup rg_main;
    @BindView(R.id.view_backgroud)
    View view_backgroud;

    @BindView(R.id.ll_home_operation)
    LinearLayout ll_home_operation;

    private MyRadioButton selectView;

    @BindView(R.id.drawerlayout)
    DrawerLayout drawerlayout;

    private int tabIndex=0;

    AccountFragment accountFragment;
    MemoFragment    memoFragment;
    JokeFragment    jokeFragment;
    SpendFragment   spendFragment;
    private MyPopupwindow mPopupwindow;
    private String[]dataCountStr=new String[4];
    @Override
    protected int getContentView() {
        setAppRightImg(R.drawable.main_more);
        return R.layout.activity_main;
    }

    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_MOVE:
                fab.setVisibility(View.GONE);
            break;
            case MotionEvent.ACTION_UP:
                fab.setVisibility(View.VISIBLE);
            break;
        }
        return super.dispatchTouchEvent(ev);
    }*/

    @Override
    protected void initView() {
        setBackIcon(R.drawable.drawer_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneUtils.hiddenKeyBoard(mContext);
                drawerlayout.openDrawer(Gravity.LEFT);
            }
        });
        getSupportActionBar().setTitle("Note");
        toolbar.setTitleTextColor(ContextCompat.getColor(mContext,R.color.white));
//        ctl_layout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
//        ctl_layout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));

        selectView=rb_main_account;
        accountFragment=AccountFragment.newInstance();
        addFragment(R.id.fl_content,accountFragment);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initRxBus() {
        super.initRxBus();
        getEvent(OptionEvent.class, new EventCallback<OptionEvent>() {
            @Override
            public void accept(OptionEvent event) {
                //获取数据量
                if(event.flag==OptionEvent.flag_get_data_count){
                    int dataCount=0;
                    switch (event.index){
                        case 0:
                            dataCount=accountFragment.getDataCount();
                            break;
                        case 1:
                            dataCount=memoFragment.getDataCount();
                            break;
                        case 2:
                            dataCount=jokeFragment.getDataCount();
                            break;
                        case 3:
                            dataCount=spendFragment.getDataCount();
                            break;
                    }
                    dataCountStr[event.index]=dataCount+"";
                    getSupportActionBar().setTitle("Note("+dataCount+")");
                }
            }
        });
    }

    private void showSeting() {
        View view = inflateView(R.layout.popu_options, null);
        mPopupwindow = new MyPopupwindow(this,view);
        view.findViewById(R.id.tv_orderBy_create).setOnClickListener(getSetListener(OptionEvent.flag_0));
        view.findViewById(R.id.tv_orderBy_update).setOnClickListener(getSetListener(OptionEvent.flag_1));
        view.findViewById(R.id.tv_batchDelete).setOnClickListener(getSetListener(OptionEvent.flag_prepare_delete));

        int xoff = PhoneUtils.getPhoneWidth(this) - PhoneUtils.dip2px(this, 125);
        mPopupwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
                alphaAnimation.setDuration(300);//持续时间
                view_backgroud.startAnimation(alphaAnimation);
                view_backgroud.setVisibility(View.GONE);
            }
        });
        mPopupwindow.setBackground(R.color.transparent);
        mPopupwindow.showAsDropDown(toolbar, xoff,0);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(300);//持续时间
        view_backgroud.startAnimation(alphaAnimation);
        view_backgroud.setVisibility(View.VISIBLE);
    }

    @NonNull
    private MyOnClickListener getSetListener(final int flag) {
        return new MyOnClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                mPopupwindow.dismiss();
                if(flag==OptionEvent.flag_prepare_delete){
                    showOperation(true);
                }
                RxBus.getInstance().post(new OptionEvent(flag,tabIndex));
            }
        };
    }
    public void showOperation(boolean isShow){

        rg_main.setVisibility(isShow?View.GONE:View.VISIBLE);
        ll_home_operation.setVisibility(isShow?View.VISIBLE:View.GONE);
        fab.setVisibility(isShow?View.GONE:View.VISIBLE);
    }
    @OnClick({R.id.rb_main_account,
            R.id.rb_main_memo,
            R.id.rb_main_joke,
            R.id.fab,
            R.id.rb_main_spend,
            R.id.app_right_iv,
            R.id.tv_home_operation_delete,
            R.id.tv_home_operation_complete
    })
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.tv_home_operation_delete:
                //批量删除-删除
                RxBus.getInstance().post(new OptionEvent(OptionEvent.flag_start_delete,tabIndex));
                break;
            case R.id.tv_home_operation_complete:
                //批量删除-完成
                showOperation(false);
                RxBus.getInstance().post(new OptionEvent(OptionEvent.flag_cancel_delete,tabIndex));
                break;
            case R.id.app_right_iv:
                showSeting();
                break;
            case R.id.fab:
                /*requestPermission(Manifest.permission.ACCESS_WIFI_STATE, new PermissionCallback() {
                    @Override
                    public void onGranted() {
                        showMsg("成功");
                    }
                    @Override
                    public void onDenied(String s) {
                        showMsg("失败"+s);
                    }
                });*/
                Intent intent=new Intent();
                intent.putExtra(IntentParam.tabIndex, tabIndex);
                STActivity(intent,AddDataActivity.class);
                break;
            case R.id.rb_main_account:
                showMenu(true);
                tabIndex=0;
                if (accountFragment == null) {
                    accountFragment=AccountFragment.newInstance();
                }
                selectFragment(rb_main_account,accountFragment);
            break;
            case R.id.rb_main_memo:
                showMenu(true);
                tabIndex=1;
                if (memoFragment == null) {
                    memoFragment=MemoFragment.newInstance();
                    addFragment(R.id.fl_content,memoFragment);
                }
                selectFragment(rb_main_memo,memoFragment);
            break;
            case R.id.rb_main_joke:
                showMenu(true);
                tabIndex=2;
                if (jokeFragment == null) {
                    jokeFragment=JokeFragment.newInstance();
                    addFragment(R.id.fl_content,jokeFragment);
                }
                selectFragment(rb_main_joke,jokeFragment);
            break;
            case R.id.rb_main_spend:
                showMenu(false);
                tabIndex=3;
                if (spendFragment == null) {
                    spendFragment=SpendFragment.newInstance();
                    addFragment(R.id.fl_content,spendFragment);
                }
                selectFragment(rb_main_spend,spendFragment);
            break;
        }
    }

    private void showMenu(boolean flag){
        app_right_iv.setVisibility(flag?View.VISIBLE:View.GONE);
    }
    public void selectFragment(MyRadioButton myRadioButton, Fragment fragment){
        if(selectView==myRadioButton){
            return;
        }
        selectView = myRadioButton;
        showFragment(fragment);
        if(fragment instanceof AccountFragment){
            hideFragment(memoFragment);
            hideFragment(jokeFragment);
            hideFragment(spendFragment);
        }else if(fragment instanceof MemoFragment){
            hideFragment(accountFragment);
            hideFragment(jokeFragment);
            hideFragment(spendFragment);
        }else if(fragment instanceof JokeFragment){
            hideFragment(accountFragment);
            hideFragment(memoFragment);
            hideFragment(spendFragment);
        }else if(fragment instanceof SpendFragment){
            hideFragment(accountFragment);
            hideFragment(memoFragment);
            hideFragment(jokeFragment);
        }

        if(tabIndex!=3&&!TextUtils.isEmpty(dataCountStr[tabIndex])){
            getSupportActionBar().setTitle("Note("+dataCountStr[tabIndex]+")");
        }else{
            getSupportActionBar().setTitle("Note");
        }
    }
    @Override
    public void onBackPressed() {
        if(drawerlayout.isDrawerOpen(GravityCompat.START)) {
            drawerlayout.closeDrawer(GravityCompat.START);
        }else{
            if ((System.currentTimeMillis() - mExitTime) > 1500) {
                showToastS("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }
}
