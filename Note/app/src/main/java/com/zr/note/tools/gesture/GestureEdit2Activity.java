package com.zr.note.tools.gesture;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zr.note.R;
import com.zr.note.base.BaseActivity;
import com.zr.note.base.BasePresenter;
import com.zr.note.tools.AES;
import com.zr.note.tools.LogUtils;
import com.zr.note.tools.SPUtils;
import com.zr.note.tools.gesture.widget.GestureContentView;
import com.zr.note.tools.gesture.widget.GestureDrawline;
import com.zr.note.tools.gesture.widget.LockIndicator;
import com.zr.note.ui.main.activity.MainActivity;

import butterknife.BindView;


/**
 *
 * 手势密码设置界面
 *
 */
public class GestureEdit2Activity extends BaseActivity     {

	@BindView(R.id.lock_indicator)
	LockIndicator mLockIndicator;
	@BindView(R.id.text_tip)
	TextView text_tip;
	@BindView(R.id.gesture_container)
	FrameLayout gesture_container;
	private GestureContentView mGestureContentView;
	@BindView(R.id.text_reset)
	TextView text_reset;
	private boolean mIsFirstInput = true;
	private String mFirstPassword = null;

	@Override
	protected BasePresenter initPresenter() {
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LogUtils.Log("");
		super.onCreate(savedInstanceState);
		LogUtils.Log("");
	}

	@Override
	protected int setContentView() {
		return R.layout.activity_gesture_edit;
	}

	@Override
	protected void setToolbarStyle() {
		setTitle("设置密码");
		setHideNavigationIcon();
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
		mGestureContentView = new GestureContentView(this, false,null, new GestureDrawline.GestureCallBack() {
			@Override
			public void onGestureCodeInput(String inputCode) {
				if (!isInputPassValidate(inputCode)) {
					text_tip.setText(Html.fromHtml("<font color='#990814'>最少链接4个点,请重新输入</font>"));
					mGestureContentView.clearDrawlineState(0L);
					return;
				}
				if (mIsFirstInput) {
					mFirstPassword = inputCode;
					updateCodeList(inputCode);
					mGestureContentView.clearDrawlineState(0L);
					text_reset.setClickable(true);
					text_reset.setText(getString(R.string.reset_gesture_code));
				} else {
					if (inputCode.equals(mFirstPassword)) {
						showToastS("设置成功");
						mGestureContentView.clearDrawlineState(0L);
						SPUtils.setGesturePWD(GestureEdit2Activity.this, AES.encode(inputCode));
						STActivity(MainActivity.class);
						finish();
					} else {
						text_tip.setText(Html.fromHtml("<font color='#990814'>与上一次绘制不一致，请重新绘制</font>"));
						// 左右移动动画
						Animation shakeAnimation = AnimationUtils.loadAnimation(GestureEdit2Activity.this, R.anim.shake);
						text_tip.startAnimation(shakeAnimation);
						// 保持绘制的线，1.5秒后清除
						mGestureContentView.clearDrawlineState(1300L);
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
		});
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
			case R.id.text_cancel:
				this.finish();
				break;
			case R.id.text_reset:
				mIsFirstInput = true;
				updateCodeList("");
				text_tip.setText(getString(R.string.set_gesture_pattern));
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


	private boolean isInputPassValidate(String inputPassword) {
		if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
			return false;
		}
		return true;
	}

}
