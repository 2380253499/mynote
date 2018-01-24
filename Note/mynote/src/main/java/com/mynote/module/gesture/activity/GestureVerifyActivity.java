package com.mynote.module.gesture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
 * 手势绘制/校验界面
 *
 */
public class GestureVerifyActivity extends BaseActivity {
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
	private GestureContentView gestureContentView;
	@BindView(R.id.tv_forget_gesture)
	TextView tv_forget_gesture;

	/**
	 * 手势密码
	 */
	private boolean pwdValidationSuccess=false;
	private LockIndicator mLockIndicator;
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

	private String gesturePWD;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		gesturePWD = SPUtils.getString(this, AppXml.gesture_pwd,null);
		super.onCreate(savedInstanceState);
		if(gesturePWD==null){
			Intent mIntent=new Intent();
			mIntent.putExtra(AppXml.isFirstIntoApp,true);
			STActivity(mIntent,GestureEditActivity.class);
			finish();
		}
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
				STActivityForResult(SuperPassWordActivity.class,AppXml.request_superPWD);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==AppXml.request_superPWD&&resultCode==RESULT_OK){
			STActivity(MainActivity.class);
			finish();
		}
	}
	private void setUpViews() {
		gestureContentView = new GestureContentView(mContext, true, gesturePWD,
				new GestureDrawline.GestureCallBack() {
					@Override
					public void onGestureCodeInput(String inputCode) {
					}
					@Override
					public void checkedSuccess() {
						gestureContentView.clearDrawlineState(0L);
//                        mView.showMsg("密码正确");
						STActivity(MainActivity.class);
						actFinish();
					}
					@Override
					public void checkedFail() {
						errorNum--;
						gestureContentView.clearDrawlineState(800L);
						if (errorNum == 0) {
							tv_verify_tip.setVisibility(View.INVISIBLE);
							fl_gesture_noClick.setVisibility(View.VISIBLE);
							initTimer(tv_verify_tip, fl_gesture_noClick);
							timer.schedule(tt_task, 100, 1000);
						}else{
							tv_verify_tip.setVisibility(View.VISIBLE);
							tv_verify_tip.setText(Html.fromHtml("<font color='#E7E7E6'>密码错误,您还可以输入" + errorNum + "次</font>"));
							// 左右移动动画
							Animation shakeAnimation = AnimationUtils.loadAnimation(mContext, R.anim.shake);
							tv_verify_tip.startAnimation(shakeAnimation);
						}
					}
				});
		// 设置手势解锁显示到哪个布局里面
		gestureContentView.setParentView(fl_gesture_container);
	}
	private void initTimer(final TextView tv_verify_tip, final FrameLayout fl_gesture_noClick) {
		if (timer == null) {
			timer = new Timer();
		}
		if (tt_task == null) {
			tt_task = new TimerTask() {
				@Override
				public void run() {
					if(mHandler!=null){
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								countDown--;
								if (countDown == 0) {
									tv_verify_tip.setVisibility(View.INVISIBLE);
									countDown = countDownLength;
									errorNum = countErrorNum;
									fl_gesture_noClick.setVisibility(View.GONE);

									tt_task.cancel();
									tt_task = null;
									timer.cancel();
									timer = null;
								} else {
									tv_verify_tip.setVisibility(View.VISIBLE);
									tv_verify_tip.setText(Html.fromHtml("<font color='#E7E7E6'>" + countDown  + "秒之后才能输入密码</font>"));
								}
							}
						});
					}
				}
			};
		}
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
