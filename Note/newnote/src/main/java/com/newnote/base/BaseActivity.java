package com.newnote.base;

import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.androidtools.ClickUtils;
import com.github.androidtools.PhoneUtils;
import com.github.androidtools.StatusBarUtils;
import com.github.androidtools.inter.MyOnClickListener;
import com.github.baseclass.BasePresenter;
import com.github.baseclass.activity.IBaseActivity;
import com.newnote.R;
import com.newnote.database.DBManager;
import com.newnote.module.home.activity.MainActivity;

import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/4.
 */
public abstract class BaseActivity<P extends BasePresenter> extends IBaseActivity implements View.OnClickListener{
    /*************************************************/
    protected Toolbar toolbar;
    private boolean showNavigationIcon =true;
    private int navigationIcon =-1;
    protected int pageNum=2;
    protected int pageSize=  DBManager.pageSize;
    private String appTitle,appRightTitle;
    private int appTitleColor,appRightTitleColor;
    private int appRightImg;
    private int titleBackgroud= R.color.colorPrimary;
    private int statusBarBackgroud= R.color.colorPrimary;
    protected TextView app_title,app_right_tv;
    protected ImageView app_right_iv;
    private View status_bar;
    protected boolean hiddenBottomLine;
    /****************************************************/
    protected abstract int getContentView();
    protected abstract void initView();
    protected abstract void initData();
    protected abstract void onViewClick(View v);
    protected void initRxBus(){};
    protected void hiddenBottomLine(boolean flag){
        hiddenBottomLine=flag;
    }
    /****************************Toolbar*************************/
    protected long mExitTime;
    /************************************************************/
    protected P mPresenter;
    protected abstract P initPresenter();

    protected void setAppTitle(String title){
        appTitle=title;
        if(app_title!=null){
            app_title.setText(appTitle==null?"":appTitle);
        }
    }
    public void setAppRightTitle(String appRightTitle) {
        this.appRightTitle = appRightTitle;
    }
    public void setAppRightImg(int appRightImg) {
        this.appRightImg = appRightImg;
        if(app_right_iv!=null&&appRightImg!=0){
            app_right_iv.setImageResource(appRightImg);

            app_right_tv.setVisibility(View.GONE);
            app_right_iv.setVisibility(View.VISIBLE);
        }
    }

    public void setTitleBackgroud(int titleBackgroud) {
        this.titleBackgroud = titleBackgroud;
    }
    public void setStatusBarBackgroud(int statusBarBackgroud) {
        this.statusBarBackgroud = statusBarBackgroud;
        if(status_bar!=null){
            status_bar.setBackgroundColor(getResources().getColor(statusBarBackgroud));
        }
    }
    public void setAppTitleColor(int appTitleColor) {
        this.appTitleColor = appTitleColor;
    }

    public void setAppRightTitleColor(int appRightTitleColor) {
        this.appRightTitleColor = appRightTitleColor;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setTheme(R.style.AppTheme_NoActionBar);
        mContext=this;
        setContentView(getContentView());
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.LOLLIPOP){
//            StatusBarUtils.setTransparent(this);
            if(!(mContext instanceof MainActivity)){
                WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
                localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);//半透明
            }

        }

        ButterKnife.bind(this);
        if(null!=findViewById(R.id.status_bar)){
            status_bar = findViewById(R.id.status_bar);
            int statusBarHeight = StatusBarUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.height=statusBarHeight;
            status_bar.setLayoutParams(layoutParams);
            status_bar.setBackgroundColor(getResources().getColor(statusBarBackgroud));
        }
        if(null!=findViewById(R.id.toolbar)){
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new MyOnClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAfterTransition();
                    }else{
                        finish();
                    }
                }
            });
            toolbar.setBackgroundColor(getResources().getColor(titleBackgroud));
        }
        if(null!=findViewById(R.id.app_title)){
            app_title= (TextView) findViewById(R.id.app_title);
            app_title.setText(appTitle==null?"":appTitle);
            if(null!=findViewById(R.id.v_bottom_line)){
                if(TextUtils.isEmpty(appTitle)||hiddenBottomLine){
                    findViewById(R.id.v_bottom_line).setVisibility(View.GONE);
                }
            }

            if(appTitleColor!=0){
                app_title.setTextColor(appTitleColor);
            }
        }
        if(null!=findViewById(R.id.app_right_tv)){
            app_right_tv= (TextView) findViewById(R.id.app_right_tv);
        }
        if(null!=findViewById(R.id.app_right_iv)){
            app_right_iv= (ImageView) findViewById(R.id.app_right_iv);
        }
        if(appRightImg!=0){
            app_right_iv.setImageResource(appRightImg);

            app_right_tv.setVisibility(View.GONE);
            app_right_iv.setVisibility(View.VISIBLE);
        }
        if(appRightTitle!=null){
            app_right_tv.setText(appRightTitle);
            app_right_tv.setVisibility(View.VISIBLE);
            app_right_iv.setVisibility(View.GONE);
            if(appRightTitleColor!=0){
                app_right_tv.setTextColor(appRightTitleColor);
            }
        }
        /*if(null!=findViewById(R.id.pcfl_refresh)){
            pcfl = (PtrClassicFrameLayout) findViewById(R.id.pcfl_refresh);
            pcfl.setLastUpdateTimeRelateObject(this);
        }
        if(null!=findViewById(R.id.pl_load)){
            pl_load = (ProgressLayout) findViewById(R.id.pl_load);
            pl_load.setInter(this);
        }*/
        mPresenter= initPresenter();
        if(mPresenter!=null){
            mPresenter.attach( this);
        }
        setInput();
        initRxBus();
        initView();
        if(toolbar!=null){
            if(navigationIcon!=-1){
                getSupportActionBar().setHomeAsUpIndicator(navigationIcon);
            }else{
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
//                getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(showNavigationIcon);
        }
        initData();
    }

    protected void setBackIcon(int resId){
        navigationIcon=resId;
    }
    protected void hiddenBackIcon(){
        showNavigationIcon=false;
    }
    protected void hiddenBackIcon(boolean show){
        showNavigationIcon=show;
    }
    protected String getSStr(View view){
        if(view instanceof TextView){
            return ((TextView)view).getText().toString();
        } else if (view instanceof EditText) {
            return ((EditText)view).getText().toString();
        }else{
            return null;
        }
    }
    /*public void showProgress(){
        if (pl_load != null) {
            pl_load.showProgress();
        }
    }
    public void showContent(){
        if (pl_load != null) {
            pl_load.showContent();
        }
    }
    public void showErrorText(){
        if (pl_load != null) {
            pl_load.showErrorText();
        }
    }*/
    @Override
    public void onClick(View v) {
        if(!ClickUtils.isFastClick(v, 800)){
            onViewClick(v);
        }
    }
    private void setInput() {
        final View rootView = ((ViewGroup) this.findViewById(android.R.id.content))
                .getChildAt(0);
        final View decorView = getWindow().getDecorView();
        if(decorView==null){
            return;
        }
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Rect rect= new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int screenHeight = decorView.getRootView().getHeight();
                int heightDifferent = screenHeight - rect.bottom- PhoneUtils.getNavigationBarHeight(mContext);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) rootView.getLayoutParams();
                lp.setMargins(0, 0, 0, heightDifferent);
                rootView.requestLayout();
            }
        });
    }

    protected boolean isEmpty(List list){
        return list == null || list.size() == 0;
    }
    protected boolean notEmpty(List list){
        return !(list == null || list.size() == 0);
    }
    protected String getRnd(){
        Random random = new Random();
        int rnd = random.nextInt(9000) + 1000;
        return rnd+"";
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
