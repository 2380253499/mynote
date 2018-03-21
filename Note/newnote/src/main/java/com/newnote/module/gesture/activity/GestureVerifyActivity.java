package com.newnote.module.gesture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.androidtools.SPUtils;
import com.newnote.R;
import com.newnote.base.BaseActivity;
import com.newnote.module.gesture.Constant;
import com.newnote.module.gesture.activity.contract.GestureCon;
import com.newnote.module.gesture.activity.contract.imp.GestureImp;
import com.newnote.module.home.activity.MainActivity;
import com.newnote.tools.gesture.widget.GestureContentView;

import butterknife.BindView;


/**
 *
 * 手势绘制/校验界面
 *
 */
public class GestureVerifyActivity extends BaseActivity<GestureImp> implements GestureCon.View{
	@BindView(R.id.iv_logo)
	ImageView iv_logo;
	@BindView(R.id.tv_phone_number)
	TextView tv_phone_number;
	@BindView(R.id.tv_verify_tip)
	TextView tv_verify_tip;
	@BindView(R.id.fl_gesture_container)
	FrameLayout fl_gesture_container;
	@BindView(R.id.fl_gesture_noClick)
	FrameLayout fl_gesture_noClick;
	private GestureContentView mGestureContentView;
	@BindView(R.id.tv_forget_gesture)
	TextView tv_forget_gesture;

	private String gesturePWD;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		gesturePWD = SPUtils.getString(this, Constant.SP.gesture_pwd,null);
		super.onCreate(savedInstanceState);
		if(gesturePWD==null){
			Intent mIntent=new Intent();
			mIntent.putExtra(Constant.Gesture.isFirstIntoApp,true);
			STActivity(mIntent,GestureEditActivity.class);
			finish();
		}
	}
	@Override
	protected GestureImp initPresenter() {
		return new GestureImp( );
	}

	@Override
	protected int getContentView() {
		setAppTitle("输入手势密码");
		return R.layout.activity_gesture_verify;
	}

	@Override
	protected void initView() {
		hiddenBackIcon();
//		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		setUpViews();
		tv_forget_gesture.setOnClickListener(this);
	}

	@Override
	protected void initData() {

	}

	@Override
	public void onViewClick(View v) {
		switch (v.getId()){
			case R.id.tv_forget_gesture:
//				showToastL("请加QQ群271910854联系群主");
				STActivityForResult(SuperPassWordActivity.class,Constant.Gesture.request_superPWD);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==Constant.Gesture.request_superPWD&&resultCode==RESULT_OK){
			STActivity(MainActivity.class);
			finish();
		}
	}
	private void setUpViews() {
		// 初始化一个显示各个点的viewGroup
//		mGestureContentView = new GestureContentView(this, true, gesturePWD,
		mGestureContentView = mPresenter.initVerifyGestureContentView(gesturePWD, tv_verify_tip, fl_gesture_noClick);
		mPresenter.setGestureContentView(mGestureContentView);
		// 设置手势解锁显示到哪个布局里面
		mGestureContentView.setParentView(fl_gesture_container);
	}
	@Override
	public void onBackPressed() {
		if ((System.currentTimeMillis() - mExitTime) > 1500) {
			showToastS("再按一次退出程序");
			mExitTime = System.currentTimeMillis();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void pwdValidationSuccess() {

	}
}
