package com.newnote.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.base.activity.IBaseActivity;
import com.github.androidtools.ClickUtils;
import com.github.androidtools.StatusBarUtils;
import com.newnote.R;
import com.newnote.module.home.activity.MainActivity;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/4.
 */
public abstract class BaseActivity<P extends BasePresenter> extends IBaseActivity implements BaseView {
    /****************************Toolbar*************************/
    private Toolbar toolbar;
    private boolean showNavigationIcon =true;
    private int navigationIcon =-1;
    private Menu mMenu;
    protected int pageNum=1;
    protected int pageSize=25;
    /************************************************************/
    protected P mPresenter;
    protected abstract P initPresenter();
    protected abstract int[] getContentView();
    protected abstract void initView();
    protected abstract void initData();
    protected abstract int setOptionsMenu();
    protected abstract void menuOnClick(int itemId);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mContext=this;
        setContentView(getContentView()[0]);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getContentView()[1]==0?R.string.default_title:getContentView()[1]);
        setSupportActionBar(toolbar);
        onInitToolbar();
        setToolBarStyle();
        mPresenter= initPresenter();
        if(mPresenter!=null){
            mPresenter.attach(this);
        }
        initView();
        initData();
    }

    protected void setShowNavigationIcon(boolean showNavigationIcon) {
        this.showNavigationIcon = showNavigationIcon;
    }
    protected void setNavigationIcon(int backIcon){
        this.navigationIcon =backIcon;
    }
    protected void onInitToolbar() {

    }
    private void setToolBarStyle() {
        if(navigationIcon==-1){
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));
        }else{
            getSupportActionBar().setHomeAsUpIndicator(navigationIcon);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(showNavigationIcon);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if(mContext instanceof MainActivity){
        }else{
            StatusBarUtils.setColor(this, getResources().getColor(R.color.colorPrimary), 0);
        }
    }
    protected Toolbar getToolbar(){
        return toolbar;
    }

    public void onViewClick(View v) {
        /*if(ClickUtils.isFastClick(v, 850)){
            return;
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(setOptionsMenu()!=0){
            mMenu=menu;
            getMenuInflater().inflate(setOptionsMenu(), menu);
        }
        return true;
    }

    /**
     * 隐藏溢出菜单
     * @param itemIndex
     */
    protected void setMenuVisible(int itemIndex){
        setMenuVisible(itemIndex,true);
    }
    protected void setMenuVisible(int itemIndex,boolean visible){
        if(mMenu!=null){
            mMenu.getItem(itemIndex).setVisible(visible);
        }
    }
    private void onMenuClick(int itemId) {
        if(!ClickUtils.isFastClickById(itemId,900)){
            menuOnClick(itemId);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId=item.getItemId();
        if(itemId==android.R.id.home){
            finish();
            return true;
        }else{
            onMenuClick(itemId);
            return true;
        }
//        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        dismissLoading();
    }

    @Override
    public void actFinish() {
        finish();
    }

    @Override
    public void showMsg(String msg) {
        showToastS(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null){
            mPresenter.detach();
        }
        if(mMenu!=null){
            mMenu=null;
        }
        ClickUtils.clearLastClickTime();
    }

}
