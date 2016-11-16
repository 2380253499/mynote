package com.zr.note.ui.gesture.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zr.note.R;
import com.zr.note.base.BaseActivity;
import com.zr.note.tools.gesture.widget.GestureContentView;
import com.zr.note.tools.gesture.widget.LockIndicator;
import com.zr.note.ui.constant.IntentParam;
import com.zr.note.ui.gesture.activity.contract.GestureCon;
import com.zr.note.ui.gesture.activity.contract.imp.GestureImp;

import butterknife.BindView;


/**
 *
 * 修改手势密码界面
 *
 */
public class GestureUpdateActivity extends BaseActivity<GestureCon.View,GestureCon.Presenter> implements GestureCon.View{

	@BindView(R.id.lock_indicator)
	LockIndicator mLockIndicator;
	@BindView(R.id.tv_tip)
	TextView tv_tip;
	@BindView(R.id.gesture_container)
	FrameLayout gesture_container;
	private GestureContentView mGestureContentView;
	@BindView(R.id.text_reset)
	TextView text_reset;
	@Override
	protected GestureImp initPresenter() {
		return new GestureImp(this);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected int setContentView() {
		return R.layout.activity_gesture_update;
	}

	@Override
	protected void setToolbarStyle() {
		setTitle("修改密码");
		boolean isUpdatePwd = getIntent().getBooleanExtra(IntentParam.Gesture.isUpdatePwd, false);
		if(!isUpdatePwd){
			setHideNavigationIcon();
		}
	}
	@Override
	protected int setOptionsMenu() {
		return 0;
	}
	@Override
	protected void initView() {
		text_reset.setClickable(false);
		text_reset.setOnClickListener(this);
		// 初始化一个显示各个点的viewGroup
//		mGestureContentView = initeGestureView();
		mGestureContentView = mPresenter.initEditGestureContentView(text_reset,tv_tip, gesture_container);
		mPresenter.setGestureContentView(mGestureContentView);
		mPresenter.setLockIndicator(mLockIndicator);
		// 设置手势解锁显示到哪个布局里面
		mGestureContentView.setParentView(gesture_container);
		updateCodeList("");
	}

	@Override
	protected void initData() {

	}
	@Override
	protected void viewOnClick(View v) {
		switch (v.getId()) {
			case R.id.text_reset:
				mPresenter.resetPwd();
				tv_tip.setText(getString(R.string.set_gesture_pattern));
				break;
			default:
				break;
		}
	}

	@Override
	protected void menuOnClick(int itemId) {

	}




	private void updateCodeList(String inputCode) {
		// 更新选择的图案
		mLockIndicator.setPath(inputCode);
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
