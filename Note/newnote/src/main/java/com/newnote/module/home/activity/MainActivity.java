package com.newnote.module.home.activity;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.androidtools.inter.MyOnClickListener;
import com.github.customview.MyRadioButton;
import com.newnote.R;
import com.newnote.base.BaseActivity;
import com.newnote.base.BasePresenter;
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
    @BindView(R.id.iv_banner)
    ImageView iv_banner;
    //    @BindView(R.id.pfl)
//    PtrFrameLayout pfl;
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
    protected int[] getContentView() {
        return new int[]{R.layout.activity_main, R.string.title_note};
    }

    @Override
    protected void initView() {
//        tv_data_delete.setOnClickListener(this);
//        tv_date_endselect.setOnClickListener(this);
        Glide.with(this).load(R.drawable.zr5).crossFade(600).into(iv_banner);
        getToolbar().setNavigationOnClickListener(new MyOnClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                drawerlayout.openDrawer(Gravity.LEFT);
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(this);
        rb_main_account.setChecked(true);
        ctl_layout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        ctl_layout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));


//        accountFragment=AccountFragment.newInstance();
//        dataManageInters[0]=accountFragment;
        addFragment(R.id.fl_fragment, accountFragment);
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                addFragment(checkedId);
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

    @Override
    protected void initData() {

    }

    @OnClick({R.id.fab})
    public void onViewClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                STActivity(TestActivity.class);
                break;
        }
    }

    @Override
    protected int setOptionsMenu() {
        return 0;
    }

    @Override
    protected void menuOnClick(int itemId) {

    }
}
