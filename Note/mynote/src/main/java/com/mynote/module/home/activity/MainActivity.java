package com.mynote.module.home.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.github.customview.MyRadioButton;
import com.mynote.IntentParam;
import com.mynote.R;
import com.mynote.base.BaseActivity;
import com.mynote.module.account.fragment.AccountFragment;
import com.mynote.module.account.fragment.JokeFragment;
import com.mynote.module.account.fragment.MemoFragment;
import com.mynote.module.account.fragment.SpendFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


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
    private MyRadioButton selectView;

    @BindView(R.id.drawerlayout)
    DrawerLayout drawerlayout;

    private int tabIndex=0;

    AccountFragment accountFragment;
    MemoFragment    memoFragment;
    JokeFragment    jokeFragment;
    SpendFragment   spendFragment;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        selectView=rb_main_account;
        accountFragment=AccountFragment.newInstance();
        addFragment(R.id.fl_content,accountFragment);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.rb_main_account,
            R.id.rb_main_memo,
            R.id.rb_main_joke,
            R.id.fab,
            R.id.rb_main_spend})
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                Intent intent=new Intent();
                intent.putExtra(IntentParam.tabIndex, tabIndex);
                STActivity(intent,AddDataActivity.class);
            break;
            case R.id.rb_main_account:
                tabIndex=0;
                selectFragment(rb_main_account,accountFragment);
            break;
            case R.id.rb_main_memo:
                tabIndex=1;
                selectFragment(rb_main_memo,memoFragment);
            break;
            case R.id.rb_main_joke:
                tabIndex=2;
                selectFragment(rb_main_joke,jokeFragment);
            break;
            case R.id.rb_main_spend:
                tabIndex=3;
                selectFragment(rb_main_spend,spendFragment);
            break;
        }
    }
    public void selectFragment(MyRadioButton myRadioButton, Fragment fragment){
        if(selectView==myRadioButton){
            return;
        }
        selectView = myRadioButton;
        if (fragment == null) {
            if(fragment instanceof AccountFragment){
                fragment=AccountFragment.newInstance();
            }else if(fragment instanceof MemoFragment){
                fragment=MemoFragment.newInstance();
            }else if(fragment instanceof JokeFragment){
                fragment=JokeFragment.newInstance();
            }else if(fragment instanceof SpendFragment){
                fragment=SpendFragment.newInstance();
            }
            addFragment(R.id.fl_content, fragment);
        }
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
    }
}
