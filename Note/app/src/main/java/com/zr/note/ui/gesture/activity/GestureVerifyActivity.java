package com.zr.note.ui.gesture.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zr.note.R;
import com.zr.note.base.BaseActivity;
import com.zr.note.tools.AES;
import com.zr.note.tools.SPUtils;
import com.zr.note.tools.gesture.widget.GestureContentView;
import com.zr.note.ui.constant.IntentParam;
import com.zr.note.ui.gesture.activity.contract.GestureCon;
import com.zr.note.ui.gesture.activity.contract.imp.GestureImp;

import butterknife.BindView;


/**
 *
 * 手势绘制/校验界面
 *
 */
public class GestureVerifyActivity extends BaseActivity<GestureCon.View,GestureCon.Presenter> implements GestureCon.View{
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
		gesturePWD =SPUtils.getGesturePWD(this);
		if(gesturePWD!=null){
			gesturePWD=AES.decode(gesturePWD);
		}
		super.onCreate(savedInstanceState);
		if(gesturePWD==null){
			mIntent.putExtra(IntentParam.Gesture.isFirstIntoApp,true);
			STActivity(mIntent,GestureEditActivity.class);
			finish();
		}
	}
	@Override
	protected GestureImp initPresenter() {
		return new GestureImp(this);
	}

	@Override
	protected int setContentView() {
		return R.layout.activity_gesture_verify;
	}

	@Override
	protected void setToolbarStyle() {
		setTitle("输入手势密码");
		setHideNavigationIcon();
	}

	@Override
	protected int setOptionsMenu() {
		return 0;
	}

	@Override
	protected void initView() {
		setUpViews();
		tv_forget_gesture.setOnClickListener(this);
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void viewOnClick(View v) {
		switch (v.getId()){
			case R.id.tv_forget_gesture:
				showToastL("请加QQ群271910854联系群主");
			break;
		}
	}
	@Override
	protected void menuOnClick(int itemId) {

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
}
