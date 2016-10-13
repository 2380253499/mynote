package com.zr.note.base;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zr.note.R;
import com.zr.note.tools.ClickUtils;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/4.
 */
public abstract class BaseActivity<V extends BaseView, P extends BasePresenter<V>> extends IBaseActivity implements BaseView,View.OnClickListener{
    /****************************Toolbar*************************/
    private Toolbar toolbar;
    private boolean showNavigationIcon =true;
    private int navigationIcon =-1;
    private Drawable backColor;
    private int logIcon=-1;
    private Drawable logIconDrawble;
    private int titleId=-1;
    private String titleString;
    private int subTitleId=-1;
    private String subTitleString;
    /************************************************************/
    protected P mPresenter;
    protected abstract P initPresenter();
    protected abstract int setContentView();
    protected abstract void setToolbarStyle();
    protected abstract int setOptionsMenu();
    protected abstract void initView();
    protected abstract void initData();
    protected abstract void viewOnClick(View v);
    protected abstract void menuOnClick(int itemId);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setColorPrimaryDark();//兼容4.4
        setContentView(setContentView());
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarStyle();
        setNavigationColor(R.color.white);
        setSupportActionBar(toolbar);
        setToolBar();
        mPresenter= initPresenter();
        initView();
        initData();
    }
    private void setToolBar() {
        if(navigationIcon !=-1){
            getSupportActionBar().setHomeAsUpIndicator(navigationIcon);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }else if(backColor!=null){
            getSupportActionBar().setHomeAsUpIndicator(backColor);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }else{
            getSupportActionBar().setDisplayHomeAsUpEnabled(showNavigationIcon);
        }
        if(logIcon!=-1){
            getSupportActionBar().setLogo(logIcon);
        }else if(logIconDrawble!=null){
            getSupportActionBar().setLogo(logIconDrawble);
        }
        if(titleId!=-1){
            getSupportActionBar().setTitle(titleId);
        }else if(titleString!=null){
            getSupportActionBar().setTitle(titleString);
        }
        if (subTitleId != -1) {
            getSupportActionBar().setSubtitle(subTitleId);
        }else if(subTitleString!=null){
            getSupportActionBar().setSubtitle(subTitleString);
        }
    }

    private void setColorPrimaryDark() {
//        StatusBarCompatForKitKat.compat(this, getResources().getColor(R.color.colorPrimaryDark));
    }
    protected void setNavigationColor(int backColor){
        this.backColor = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        this.backColor.setColorFilter(getResources().getColor(backColor), PorterDuff.Mode.SRC_ATOP);
    }
    protected void setNavigationIcon(int backIcon){
        this.navigationIcon =backIcon;
    }
    protected void setHideNavigationIcon(){
        showNavigationIcon =false;
    }
    protected void setLogIcon(int logIcon){
        this.logIcon=logIcon;
    }
    protected void setLogIcon(Drawable logIconDrawble){
        this.logIconDrawble=logIconDrawble;
    }
    protected void setToolbarTitle(int titleId){
        this.titleId=titleId;
    }
    protected void setToolbarTitle(String titleString){
        this.titleString=titleString;
    }
    protected void setToolbarSubTitle(int subTitleId){
        this.subTitleId=subTitleId;
    }
    protected void setToolbarSubTitle(String subTitleString){
        this.subTitleString=subTitleString;
    }
    protected Toolbar getToolbar(){
        return toolbar;
    }

    @Override
    public void onClick(View v) {
        if(!ClickUtils.isFastClick(v,960)){
            viewOnClick(v);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(setOptionsMenu()!=0){
            getMenuInflater().inflate(setOptionsMenu(), menu);
        }
        return true;
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

    }

    @Override
    public void hideLoading() {

    }
    @Override
    public void showMsg(String msg) {
        showToastS(msg);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.attach((V) this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null){
            mPresenter.detach();
        }
        ClickUtils.clearLastClickTime();
    }

}
