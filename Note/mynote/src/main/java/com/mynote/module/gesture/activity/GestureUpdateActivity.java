package com.mynote.module.gesture.activity;

import android.content.Intent;
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
import com.mynote.Constant;
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
 * 修改手势密码界面
 *
 */
public class GestureUpdateActivity extends BaseActivity {

	@BindView(R.id.lock_indicator)
	LockIndicator mLockIndicator;
	@BindView(R.id.tv_tip)
	TextView tv_verify_tip;
	@BindView(R.id.gesture_container)
	FrameLayout gesture_container;
	private GestureContentView gestureContentView;
	@BindView(R.id.text_reset)
	TextView text_reset;
	@BindView(R.id.tv_forget_oldpwd)
	TextView tv_forget_oldpwd;

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
		gestureContentView =new GestureContentView(mContext,true,SPUtils.getString(mContext, AppXml.gesturePWD,null), new GestureDrawline.GestureCallBack() {
			@Override
			public void onGestureCodeInput(String inputCode) {

			}
			@Override
			public void checkedSuccess() {
				pwdValidationSuccess=true;
				mLockIndicator.setPath("");
				gestureContentView.clearDrawlineState(0);
				pwdValidationSuccess();
			}
			@Override
			public void checkedFail() {
				tv_verify_tip.setText(Html.fromHtml("<font color='#E7E7E6'>原手势密码错误,请重新绘制</font>"));
				Animation shakeAnimation = AnimationUtils.loadAnimation(mContext, R.anim.shake);
				tv_verify_tip.startAnimation(shakeAnimation);
				tv_verify_tip.postDelayed(new Runnable() {
					@Override
					public void run() {
						tv_verify_tip.setText(Html.fromHtml("<font color='#58c2f7'>请绘制原手势密码</font>"));
					}
				},1000);
//                mView.showMsg("原手势密码验证失败,请重新绘制");
				mLockIndicator.setPath("");
				gestureContentView.clearDrawlineState(900L);
			}
		});
		// 设置手势解锁显示到哪个布局里面
		gestureContentView.setParentView(gesture_container);
		mLockIndicator.setPath("");
	}

	@Override
	protected void initData() {

	}
	public void pwdValidationSuccess() {
		tv_verify_tip.setText(getString(R.string.set_gesture_pattern));
		tv_forget_oldpwd.setVisibility(View.GONE);
		initUpdateGesturePWD();
	}
	@Override
	public void onViewClick(View v) {
		switch (v.getId()) {
			case R.id.tv_forget_oldpwd:
				STActivityForResult(SuperPassWordActivity.class, Constant.IParam.update_superPWD);
				break;
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
	private boolean isInputPassValidate(String inputPassword) {
		if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
			return false;
		}
		return true;
	}
	private void initUpdateGesturePWD() {
		gestureContentView=new GestureContentView(mContext,false,null, new GestureDrawline.GestureCallBack() {
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
						SPUtils.setPrefString(mContext,AppXml.gesturePWD, inputCode);
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
		});
		gestureContentView.setParentView(gesture_container);
		mLockIndicator.setPath("");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode== Constant.IParam.update_superPWD&&resultCode==RESULT_OK){
			pwdValidationSuccess();
		}
	}
}
