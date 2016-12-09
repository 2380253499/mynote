package com.zr.note.ui.main.activity;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.zr.customview.MyRadioButton;
import com.zr.note.R;
import com.zr.note.base.BaseActivity;
import com.zr.note.inter.MyOnClickListener;
import com.zr.note.tools.PhoneUtils;
import com.zr.note.ui.constant.IntentParam;
import com.zr.note.ui.constant.RxTag;
import com.zr.note.ui.main.activity.contract.MainContract;
import com.zr.note.ui.main.activity.contract.imp.MainImp;
import com.zr.note.ui.main.fragment.AccountFragment;
import com.zr.note.ui.main.fragment.JokeFragment;
import com.zr.note.ui.main.fragment.MemoFragment;
import com.zr.note.ui.main.fragment.SpendFragment;
import com.zr.note.ui.main.inter.DateInter;
import com.zr.note.view.MyPopupwindow;

import butterknife.BindView;

public class MainActivity extends BaseActivity<MainContract.View, MainContract.Presenter> implements MainContract.View {
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
    private AccountFragment accountFragment;
    private MemoFragment memoFragment;
    private JokeFragment jokeFragment;
    private SpendFragment spendFragment;
    private int tabIndex=0;
    private DateInter.dataManageInter[] dataManageInters =new DateInter.dataManageInter[4];
    @Override
    protected int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        tv_data_delete.setOnClickListener(this);
        tv_date_endselect.setOnClickListener(this);
        Glide.with(this).load(R.drawable.zr5).crossFade(600).into(iv_banner);
        getToolbar().setNavigationOnClickListener(new MyOnClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                drawerlayout.openDrawer(Gravity.LEFT);
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        rb_main_account.setChecked(true);
        ctl_layout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        ctl_layout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));


        accountFragment=AccountFragment.newInstance();
        dataManageInters[0]=accountFragment;
        addFragment(R.id.fl_fragment, accountFragment);
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                addFragment(checkedId);
            }
        });
        //批量删除数据-全选
        cb_data_checkall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkClick();
            }
        });
    }

    private void checkClick() {
        boolean isChecked = cb_data_checkall.isChecked();
        switch (tabIndex){
            case 0:
                RxBus.get().post(RxTag.dataCheckAll_account,isChecked);
                break;
            case 1:
                RxBus.get().post(RxTag.dataCheckAll_memo,isChecked);
                break;
            case 2:
                RxBus.get().post(RxTag.dataCheckAll_joke,isChecked);
                break;
            case 3:
                RxBus.get().post(RxTag.dataCheckAll_3,isChecked);
                break;
        }
    }

    private void addFragment(int checkedId) {
        switch (checkedId) {
            case R.id.rb_main_account:
                tabIndex = 0;
                showFragment(accountFragment);
                hideFragment(memoFragment);
                hideFragment(jokeFragment);
                hideFragment(spendFragment);
                setMenuHidden();
                break;
            case R.id.rb_main_memo:
                tabIndex = 1;
                if (memoFragment == null) {
                    memoFragment = MemoFragment.newInstance();
                    dataManageInters[1] = memoFragment;
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
                setMenuHidden();
                break;
            case R.id.rb_main_joke:
                tabIndex = 2;
                if (jokeFragment == null) {
                    jokeFragment = JokeFragment.newInstance();
                    dataManageInters[2] = jokeFragment;
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
                setMenuHidden();
                break;
            case R.id.rb_main_spend:
                tabIndex = 3;
                if (spendFragment == null) {
                    spendFragment = SpendFragment.newInstance();
                    dataManageInters[3] = spendFragment;
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
                setMenuHidden();
                break;
        }
    }

    private void setMenuHidden() {
        if(tabIndex==3){
            setMenuVisible(0,false);
        }else{
            setMenuVisible(0);
        }
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
            //删除
            case R.id.tv_data_delete:
                switch (tabIndex){
                    case 0:
                        RxBus.get().post(RxTag.deleteAll_account,0);
                        break;
                    case 1:
                        RxBus.get().post(RxTag.deleteAll_memo,1);
                        break;
                    case 2:
                        RxBus.get().post(RxTag.deleteAll_joke,2);
                        break;
                    case 3:
                        RxBus.get().post(RxTag.deleteAll_3,3);
                        break;
                }
                break;
            //取消删除
            case R.id.tv_date_endselect:
                setMenuVisible(0);
                rg_main.setVisibility(View.VISIBLE);
                ll_data_check.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                cb_data_checkall.setChecked(false);
                switch (tabIndex){
                    case 0:
                        RxBus.get().post(RxTag.endDataBatchSelect_account,0);
                    break;
                    case 1:
                        RxBus.get().post(RxTag.endDataBatchSelect_memo,1);
                    break;
                    case 2:
                        RxBus.get().post(RxTag.endDataBatchSelect_joke,2);
                    break;
                    case 3:
                        RxBus.get().post(RxTag.endDataBatchSelect_3,3);
                    break;
                }
                break;
            case R.id.fab:
                mIntent.putExtra(IntentParam.tabIndex, tabIndex);
                STActivity(mIntent, AddDataActivity.class);
//                SparseArray<SparseArray<SparseArray<List<SpendBean>>>> sparseArray = DBManager.getNewInstance(this).selectSpendForTree();
//                Log.i("===","======"+sparseArray.size());
                break;
            case R.id.tv_orderBy_create:
                dataManageInters[tabIndex].orderByCreateTime(true);
                mPopupwindow.dismiss();
                break;
            case R.id.tv_orderBy_update:
                dataManageInters[tabIndex].orderByCreateTime(false);
                mPopupwindow.dismiss();
                break;
            //批量删除
            case R.id.tv_batchDelete:
                mPopupwindow.dismiss();
                if(fab.getVisibility()!=View.GONE){
                    switch (tabIndex){
                        case 0:
                            RxBus.get().post(RxTag.dataBatchSelect_account,tabIndex);
                        break;
                        case 1:
                            RxBus.get().post(RxTag.dataBatchSelect_memo,tabIndex);
                        break;
                        case 2:
                            RxBus.get().post(RxTag.dataBatchSelect_joke,tabIndex);
                        break;
                        case 3:
                            RxBus.get().post(RxTag.dataBatchSelect_3,tabIndex);
                        break;
                    }
                }
                break;
        }
    }
    //批量删除之前判断是否为空数据
    @Subscribe(tags = @Tag(RxTag.notEmpty))
    public void dataBatchSelect(Boolean notEmpty){
        if(notEmpty){
            startDataBatchSelect();
        }
    }
    public void startDataBatchSelect(){
        setMenuVisible(0, false);
        fab.setVisibility(View.GONE);
        rg_main.setVisibility(View.GONE);
        ll_data_check.setVisibility(View.VISIBLE);
    }
    public void endDataBatchSelect(){
        setMenuVisible(0);
        rg_main.setVisibility(View.VISIBLE);
        ll_data_check.setVisibility(View.GONE);
        fab.setVisibility(View.VISIBLE);
        cb_data_checkall.setChecked(false);
    }
    @Override
    protected void menuOnClick(int itemId) {
        switch (itemId) {
            case R.id.action_settings:
                showSeting();
                break;
        }
    }

    private void showSeting() {
//        tv_orderBy_create
        View view = inflateView(R.layout.popu_options, null);
        TextView tv_orderBy_create = (TextView) view.findViewById(R.id.tv_orderBy_create);
        TextView tv_orderBy_update = (TextView) view.findViewById(R.id.tv_orderBy_update);
        TextView tv_batchDelete = (TextView) view.findViewById(R.id.tv_batchDelete);
        tv_orderBy_create.setOnClickListener(this);
        tv_orderBy_update.setOnClickListener(this);
        tv_batchDelete.setOnClickListener(this);
        mPopupwindow = new MyPopupwindow(this,view);
        int xoff = PhoneUtils.getPhoneWidth(this) - PhoneUtils.dip2px(this, 125);
        view_backgroud.setVisibility(View.VISIBLE);
        mPopupwindow.showAsDropDown(getToolbar(), xoff, 0);
        mPopupwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                view_backgroud.setVisibility(View.GONE);
                if(fab.getVisibility()==View.GONE){
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected MainImp initPresenter() {
        return new MainImp(this);
    }


    @Override
    public void onBackPressed() {
        if(drawerlayout.isDrawerOpen(Gravity.START)) {
            drawerlayout.closeDrawer(Gravity.START);
        }else{
            if ((System.currentTimeMillis() - mExitTime) > 1500) {
                showToastS("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }
    //逐个选择-->全选
    @Subscribe(tags = @Tag(RxTag.dataSelectAll))
    public void dataSelectAll(Integer index){
        cb_data_checkall.setChecked(true);
    }
    //取消某个选择-->取消全选
    @Subscribe(tags = @Tag(RxTag.dataNoSelectAll))
    public void dataNoSelectAll(Integer index){
        cb_data_checkall.setChecked(false);
    }
    //是否删除全部-->成功
    @Subscribe(tags = @Tag(RxTag.dataDeleteAllSuccess))
    public void dataDeleteAllSuccess(Boolean isDeleteAllSuccess){
        if(isDeleteAllSuccess){
            endDataBatchSelect();
        }
    }
}
