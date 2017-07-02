package com.newnote.base;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.base.activity.IBaseActivity;
import com.github.tools.ClickUtils;
import com.github.tools.StatusBarUtils;
import com.newnote.MainActivity;
import com.newnote.R;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/4.
 */
public abstract class BaseActivity<P extends BasePresenter> extends IBaseActivity implements BaseView {
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
    protected int pageNum=1;
    protected int pageSize=25;
    /************************************************************/
    protected P mPresenter;
    protected abstract P initPresenter();
    protected abstract int getContentView();
    protected abstract void setToolbarStyle();
    protected abstract int setOptionsMenu();
    protected abstract void initView();
    protected abstract void initData();
    protected abstract void menuOnClick(int itemId);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(getContentView());
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarStyle();
        setNavigationColor(R.color.white);
        setSupportActionBar(toolbar);
        setToolBar();
        mPresenter= initPresenter();
        if(mPresenter!=null){
            mPresenter.attach( this);
        }
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

    /**
     *返回键颜色
     */
    protected void setNavigationColor(int backColor){
//        this.backColor = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        this.backColor = getResources().getDrawable(R.drawable.ic_back);
//        this.backColor.setColorFilter(getResources().getColor(backColor), PorterDuff.Mode.SRC_ATOP);
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

    public void onViewClick(View v) {
        if(ClickUtils.isFastClick(v, 850)){
            return;
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
