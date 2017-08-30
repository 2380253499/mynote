package com.newnote.module.home.activity;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.androidtools.inter.MyOnClickListener;
import com.github.baseclass.BasePresenter;
import com.github.customview.MyRadioButton;
import com.newnote.R;
import com.newnote.base.BaseActivity;
import com.newnote.module.account.fragment.AccountFragment;
import com.newnote.module.joke.fragment.JokeFragment;
import com.newnote.module.memo.fragment.MemoFragment;
import com.newnote.module.spend.fragment.SpendFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.cb_data_checkall)
    CheckBox cb_data_checkall;
    @BindView(R.id.tv_data_delete)
    TextView tv_data_delete;
    @BindView(R.id.tv_date_endselect)
    TextView tv_date_endselect;
    @BindView(R.id.ll_data_check)
    LinearLayout ll_data_check;
    @BindView(R.id.ll_bottom)
    LinearLayout ll_bottom;
    @BindView(R.id.ctl_layout)
    CollapsingToolbarLayout ctl_layout;
    @BindView(R.id.drawerlayout)
    DrawerLayout drawerlayout;
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
    @BindView(R.id.iv_banner)
    ImageView iv_banner;
//    @BindView(R.id.toolbar_main)
    Toolbar toolbar_main;
    private AccountFragment accountFragment;
    private MemoFragment memoFragment;
    private JokeFragment jokeFragment;
    private SpendFragment spendFragment;
    private int tabIndex=0;

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
    @Override
    protected int getContentView() {
        setAppTitle(getString(R.string.title_note));
        return R.layout.activity_main;
    }
    @Override
    protected void initView() {
//        tv_data_delete.setOnClickListener(this);
//        tv_date_endselect.setOnClickListener(this);
        toolbar_main= (Toolbar) findViewById(R.id.toolbar_main);
        Glide.with(this).load(R.drawable.zr5).crossFade(600).into(iv_banner);
        setSupportActionBar(toolbar_main);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawer_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_main.setNavigationOnClickListener(new MyOnClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                drawerlayout.openDrawer(Gravity.LEFT);
            }
        });
        rb_main_account.setChecked(true);
        ctl_layout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        ctl_layout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));


        accountFragment=AccountFragment.newInstance();
        addFragment(R.id.fl_fragment, accountFragment);

        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                addFragmentToView(checkedId);
            }
        });
        //批量删除数据-全选
        cb_data_checkall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                checkClick();
            }
        });
    }

    private void addFragmentToView(int checkedId) {
        switch (checkedId) {
            case R.id.rb_main_account:
                tabIndex = 0;
                showFragment(accountFragment);
                hideFragment(memoFragment);
                hideFragment(jokeFragment);
                hideFragment(spendFragment);
                break;
            case R.id.rb_main_memo:
                tabIndex = 1;
                if (memoFragment == null) {
                    memoFragment = MemoFragment.newInstance();
                    hideFragment(accountFragment);
                    addFragment(R.id.fl_fragment, memoFragment);
                    hideFragment(jokeFragment);
                    hideFragment(spendFragment);
                } else {
                    hideFragment(accountFragment);
                    showFragment(memoFragment);
                    hideFragment(jokeFragment);
                    hideFragment(spendFragment);
                }
                break;
            case R.id.rb_main_joke:
                tabIndex = 2;
                if (jokeFragment == null) {
                    jokeFragment = JokeFragment.newInstance();
                    hideFragment(accountFragment);
                    hideFragment(memoFragment);
                    addFragment(R.id.fl_fragment, jokeFragment);
                    hideFragment(spendFragment);
                } else {
                    hideFragment(accountFragment);
                    hideFragment(memoFragment);
                    showFragment(jokeFragment);
                    hideFragment(spendFragment);
                }
                break;
            case R.id.rb_main_spend:
                tabIndex = 3;
                if (spendFragment == null) {
                    spendFragment = SpendFragment.newInstance();
                    hideFragment(accountFragment);
                    hideFragment(memoFragment);
                    hideFragment(jokeFragment);
                    addFragment(R.id.fl_fragment, spendFragment);
                } else {
                    hideFragment(accountFragment);
                    hideFragment(memoFragment);
                    hideFragment(jokeFragment);
                    showFragment(spendFragment);
                }
                break;
        }

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.fab})
    public void onViewClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                break;
        }
    }

}
