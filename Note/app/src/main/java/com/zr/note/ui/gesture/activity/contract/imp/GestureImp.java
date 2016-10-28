package com.zr.note.ui.gesture.activity.contract.imp;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zr.note.R;
import com.zr.note.base.IPresenter;
import com.zr.note.tools.gesture.widget.GestureContentView;
import com.zr.note.tools.gesture.widget.GestureDrawline;
import com.zr.note.ui.gesture.activity.contract.GestureCon;
import com.zr.note.ui.main.activity.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/10/26.
 */
public class GestureImp extends IPresenter<GestureCon.View> implements GestureCon.Presenter {
    //可输出次数
    private int errorNum;
    //倒计时
    private int countDown;
    private final int countErrorNum = 2;
    private final int countDownLength = 3;
    private Timer timer;
    private TimerTask tt_task;
    private GestureContentView gestureContentView;
    public GestureImp(Context context) {
        super(context);
        errorNum=countErrorNum;
        countDown=countDownLength;
    }


    @Override
    public void setGestureContentView(GestureContentView mGestureContentView) {
        gestureContentView=mGestureContentView;
    }

    @Override
    public GestureContentView initGestureContentView(final String gesturePWD,final TextView tv_verify_tip, final FrameLayout fl_gesture_noClick) {
        return new GestureContentView(mContext, true, gesturePWD,
                new GestureDrawline.GestureCallBack() {
                    @Override
                    public void onGestureCodeInput(String inputCode) {

                    }
                    @Override
                    public void checkedSuccess() {
                        gestureContentView.clearDrawlineState(0L);
                        mView.showMsg("密码正确");
                        mView.STActivity(MainActivity.class);
                        mView.actFinish();
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
    }

    private void initTimer(final TextView tv_verify_tip, final FrameLayout fl_gesture_noClick) {
        if (timer == null) {
            timer = new Timer();
        }
        if (tt_task == null) {
            tt_task = new TimerTask() {
                @Override
                public void run() {
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
            };
        }
    }
}
