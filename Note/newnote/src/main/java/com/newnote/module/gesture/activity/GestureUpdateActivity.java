package com.newnote.module.gesture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.androidtools.SPUtils;
import com.newnote.R;
import com.newnote.base.BaseActivity;
import com.newnote.module.gesture.Constant;
import com.newnote.module.gesture.activity.contract.GestureCon;
import com.newnote.module.gesture.activity.contract.imp.GestureImp;
import com.newnote.tools.gesture.widget.GestureContentView;
import com.newnote.tools.gesture.widget.LockIndicator;

import butterknife.BindView;


/**
 *
 * 修改手势密码界面
 *
 */
public class GestureUpdateActivity extends BaseActivity<GestureImp> implements GestureCon.View{

	@BindView(R.id.lock_indicator)
	LockIndicator mLockIndicator;
	@BindView(R.id.tv_tip)
	TextView tv_tip;
	@BindView(R.id.gesture_container)
	FrameLayout gesture_container;
	private GestureContentView mGestureContentView;
	@BindView(R.id.text_reset)
	TextView text_reset;
	@BindView(R.id.tv_forget_oldpwd)
	TextView tv_forget_oldpwd;

	@Override
	protected GestureImp initPresenter() {
		return new GestureImp( );
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected int getContentView() {
		setAppTitle("修改密码");
		return R.layout.activity_gesture_update;
	}

	@Override
	protected void initView() {
		tv_forget_oldpwd.setOnClickListener(this);
		//@string/set_gesture_pattern
		text_reset.setClickable(false);
		text_reset.setOnClickListener(this);
		// 初始化一个显示各个点的viewGroup
		mGestureContentView = mPresenter.initVerifyOldGestureContentView(SPUtils.getPrefString(this, Constant.SP.gesture_pwd,null),text_reset, tv_tip, gesture_container);
		mPresenter.setGestureContentView(mGestureContentView);
		mPresenter.setLockIndicator(mLockIndicator);
		// 设置手势解锁显示到哪个布局里面
		mGestureContentView.setParentView(gesture_container);
		mLockIndicator.setPath("");
	}

	@Override
	protected void initData() {

	}
	@Override
	public void onViewClick(View v) {
		switch (v.getId()) {
			case R.id.tv_forget_oldpwd:
				STActivityForResult(SuperPassWordActivity.class,Constant.Gesture.update_superPWD);
				break;
			case R.id.text_reset:
				mPresenter.resetPwd();
				tv_tip.setText(getString(R.string.set_gesture_pattern));
				break;
			default:
				break;
		}
	}


	@Override
	public void pwdValidationSuccess() {
		tv_tip.setText(getString(R.string.set_gesture_pattern));
		tv_forget_oldpwd.setVisibility(View.GONE);
		initUpdateGesturePWD();
	}

	private void initUpdateGesturePWD() {
		mGestureContentView = mPresenter.initUpdateGestureContentView(text_reset, tv_tip, gesture_container);
		mPresenter.setGestureContentView(mGestureContentView);
		mPresenter.setLockIndicator(mLockIndicator);
		mGestureContentView.setParentView(gesture_container);
		mLockIndicator.setPath("");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode== Constant.Gesture.update_superPWD&&resultCode==RESULT_OK){
			pwdValidationSuccess();
		}
	}
}
