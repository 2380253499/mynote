package com.zr.note.ui.gesture.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zr.note.R;
import com.zr.note.base.BaseActivity;
import com.zr.note.base.BasePresenter;
import com.zr.note.tools.SPUtils;
import com.zr.note.tools.gesture.widget.GestureContentView;
import com.zr.note.tools.gesture.widget.GestureDrawline;
import com.zr.note.ui.constant.IntentParam;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;


/**
 *
 * 手势绘制/校验界面
 *
 */
public class GestureVerifyActivity2 extends BaseActivity {
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
	//可输出次数
	private int errorNum;
	//倒计时
	private int countDown;
	private final int countErrorNum=2;
	private final int countDownLength=3;
	private Timer timer;
	private TimerTask tt_task;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		errorNum=countErrorNum;
		countDown=countDownLength;
		gesturePWD = SPUtils.getGesturePWD(this);
		if(gesturePWD!=null){
			mIntent.putExtra(IntentParam.Gesture.isFirstIntoApp,true);
			STActivity(mIntent,GestureEdit2Activity.class);
			finish();
		}
	}

	@Override
	protected BasePresenter initPresenter() {
		return null;
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

	}
	@Override
	protected void menuOnClick(int itemId) {

	}
	private void setUpViews() {
		// 初始化一个显示各个点的viewGroup
//		mGestureContentView = new GestureContentView(this, true, gesturePWD,
		mGestureContentView = new GestureContentView(this, true, "1235789",
				new GestureDrawline.GestureCallBack() {
					@Override
					public void onGestureCodeInput(String inputCode) {

					}
					@Override
					public void checkedSuccess() {
						mGestureContentView.clearDrawlineState(0L);
						Toast.makeText(GestureVerifyActivity2.this, "密码正确", 1000).show();
						GestureVerifyActivity2.this.finish();
					}
					@Override
					public void checkedFail() {
						errorNum--;
						mGestureContentView.clearDrawlineState(1000L);
						tv_verify_tip.setVisibility(View.VISIBLE);
						tv_verify_tip.setText(Html
								.fromHtml("<font color='#990814'>密码错误,您还可以输入"+errorNum+"次</font>"));
						if(errorNum==0){
							fl_gesture_noClick.setVisibility(View.VISIBLE);
							initTimer();
							timer.schedule(tt_task,100,1000);
						}
						// 左右移动动画
						Animation shakeAnimation = AnimationUtils.loadAnimation(GestureVerifyActivity2.this, R.anim.shake);
						tv_verify_tip.startAnimation(shakeAnimation);
					}
				});
		// 设置手势解锁显示到哪个布局里面
		mGestureContentView.setParentView(fl_gesture_container);
	}

	private void initTimer() {
		if(timer==null){
            timer=new Timer();
        }
		if(tt_task==null){
            tt_task=new TimerTask() {
                @Override
                public void run() {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							tv_verify_tip.setText(--countDown+"秒");
							if(countDown==0){
								countDown=countDownLength;
								errorNum=countErrorNum;
								fl_gesture_noClick.setVisibility(View.GONE);

								tt_task.cancel();
								tt_task=null;
								timer.cancel();
								timer=null;
							}
						}
					});
                }
            };
        }
	}

}
