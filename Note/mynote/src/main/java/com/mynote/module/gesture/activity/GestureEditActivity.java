package com.mynote.module.gesture.activity;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.androidtools.SPUtils;
import com.mynote.AppXml;
import com.mynote.module.home.activity.MainActivity;
import com.mynote.R;
import com.mynote.base.BaseActivity;
import com.mynote.tools.gesture.widget.GestureContentView;
import com.mynote.tools.gesture.widget.GestureDrawline;
import com.mynote.tools.gesture.widget.LockIndicator;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;


/**
 *
 * 手势密码设置界面
 *
 */
public class GestureEditActivity extends BaseActivity {

	@BindView(R.id.lock_indicator)
	LockIndicator mLockIndicator;
	@BindView(R.id.tv_tip)
	TextView tv_verify_tip;
	@BindView(R.id.gesture_container)
	FrameLayout gesture_container;

	/**
	 * 手势密码
	 */
	private boolean pwdValidationSuccess=false;
	/**
	 * 第一次输入
	 */
	private boolean mIsFirstInput = true;
	/**
	 * 第一次设置的密码
	 */
	private String mFirstPassword = null;
	//可输出次数
	private int errorNum;
	//倒计时
	private int countDown;
	private final int countErrorNum = 4;
	private final int countDownLength = 59;
	private Timer timer;
	private TimerTask tt_task;
	private GestureContentView gestureContentView;
	@BindView(R.id.text_reset)
	TextView text_reset;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected int getContentView() {
		setAppTitle("设置密码");
		return R.layout.activity_gesture_edit;
	}

	@Override
	protected void initView() {
		boolean isUpdatePwd = getIntent().getBooleanExtra(AppXml.isUpdatePwd, false);
		if(!isUpdatePwd){
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		}
		text_reset.setClickable(false);
		text_reset.setOnClickListener(this);
		// 初始化一个显示各个点的viewGroup
//		mGestureContentView = initeGestureView();
		gestureContentView =new GestureContentView(mContext, false,null, new GestureDrawline.GestureCallBack() {
			@Override
			public void onGestureCodeInput(String inputCode) {
				if (!isInputPassValidate(inputCode)) {
					tv_verify_tip.setText(Html.fromHtml("<font color='#E7E7E6'>最少链接4个点,请重新输入</font>"));
					gestureContentView.clearDrawlineState(0L);
					return;
				}
				if (mIsFirstInput) {
					mFirstPassword = inputCode;
					updateCodeList(inputCode);
					gestureContentView.clearDrawlineState(0L);
					text_reset.setClickable(true);
					text_reset.setText(mContext.getString(R.string.reset_gesture_code));
				} else {
					if (inputCode.equals(mFirstPassword)) {
						showMsg("设置成功");
						gestureContentView.clearDrawlineState(0L);
						SPUtils.setPrefString(mContext,AppXml.gesturePWD,inputCode);
						STActivity(MainActivity.class);
						actFinish();
					} else {
						tv_verify_tip.setText(Html.fromHtml("<font color='#E7E7E6'>与上一次绘制不一致，请重新绘制</font>"));
						// 左右移动动画
						Animation shakeAnimation = AnimationUtils.loadAnimation(mContext, R.anim.shake);
						tv_verify_tip.startAnimation(shakeAnimation);
						// 保持绘制的线，1.5秒后清除
						gestureContentView.clearDrawlineState(1300L);
					}
				}
				mIsFirstInput = false;
			}
			@Override
			public void checkedSuccess() {
			}
			@Override
			public void checkedFail() {
			}
		});;
		// 设置手势解锁显示到哪个布局里面
		gestureContentView.setParentView(gesture_container);
		mLockIndicator.setPath("");
	}

	@Override
	protected void initData() {

	}
	private boolean isInputPassValidate(String inputPassword) {
		if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
			return false;
		}
		return true;
	}
	@Override
	public void onViewClick(View v) {
		switch (v.getId()) {
			case R.id.text_reset:
				mIsFirstInput = true;
				updateCodeList("");
				tv_verify_tip.setText(getString(R.string.set_gesture_pattern));
				break;
			default:
				break;
		}
	}
	/**
	 * 设置小图案提示
	 * @param inputCode
	 */
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
