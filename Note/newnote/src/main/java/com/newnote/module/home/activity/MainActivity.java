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
        Glide.with(this).load(R.drawable.zr5).crossFade(600).into(iv_banner);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawer_menu);
        getToolbar().setNavigationOnClickListener(new MyOnClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                drawerlayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setOptionsMenu() {
        return 0;
    }

    @Override
    protected void menuOnClick(int itemId) {

    }
}
