package com.zr.note.base;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zr.note.R;
import com.zr.note.tools.ClickUtils;
import com.zr.note.tools.StatusBarUtils;
import com.zr.note.ui.main.activity.MainActivity;

import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

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
    private Menu mMenu;
    /****************************RxJava********************************/
    protected Observable mObservable;
    protected Subscriber mSubscriber;
    protected Action1 mAction1;
    protected Action0 mAction0;
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
        mContext=this;
        setContentView(setContentView());
        setColorPrimaryDark();//兼容4.4
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

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if(mContext instanceof MainActivity){
        }else{
            StatusBarUtils.setColor(this, getResources().getColor(R.color.colorPrimary), 0);
        }
    }

    private void setToolBar() {
        if(!showNavigationIcon){
            getSupportActionBar().setDisplayHomeAsUpEnabled(showNavigationIcon);
        }else if(navigationIcon !=-1){//设置icon
            getSupportActionBar().setHomeAsUpIndicator(navigationIcon);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }else if(backColor!=null){//设置箭头颜色
            getSupportActionBar().setHomeAsUpIndicator(backColor);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
//        StatusBarCompat.compat(this, getResources().getColor(R.color.colorPrimaryDark));
    }
    /**
     *返回键颜色
     */
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
            mMenu=menu;
            getMenuInflater().inflate(setOptionsMenu(), menu);
        }
        return true;
    }
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

    }

    @Override
    public void hideLoading() {

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
    public void STActivityForResult(Class clazz,int requestCode){
        super.STActivityForResult(clazz, requestCode);
    }
    @Override
    public void STActivityForResult(Intent intent,Class clazz,int requestCode){
        super.STActivityForResult(intent,clazz,requestCode);
    }
    @Override
    public void STActivity(Class clazz){
        super.STActivity(clazz);
    }
    @Override
    public void STActivity(Intent intent,Class clazz){
        super.STActivity(intent,clazz);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(mPresenter!=null){
            mPresenter.attach((V) this);
        }
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
        if(mSubscriber!=null&&!mSubscriber.isUnsubscribed()){
            mSubscriber.unsubscribe();
        }
        ClickUtils.clearLastClickTime();
    }

}
