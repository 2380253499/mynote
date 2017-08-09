package com.newnote.module.gesture.activity.contract.imp;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.androidtools.SPUtils;
import com.github.baseclass.IPresenter;
import com.newnote.R;
import com.newnote.module.gesture.Constant;
import com.newnote.module.gesture.activity.contract.GestureCon;
import com.newnote.module.home.activity.MainActivity;
import com.newnote.tools.gesture.widget.GestureContentView;
import com.newnote.tools.gesture.widget.GestureDrawline;
import com.newnote.tools.gesture.widget.LockIndicator;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/10/26.
 */
public class GestureImp extends IPresenter<GestureCon.View> implements GestureCon.Presenter {

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
    public void setLockIndicator(LockIndicator lockIndicator) {
        mLockIndicator=lockIndicator;
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
    public GestureContentView initEditGestureContentView(final TextView text_reset ,final TextView tv_verify_tip, FrameLayout fl_gesture_noClick) {
            return new GestureContentView(mContext, false,null, new GestureDrawline.GestureCallBack() {
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
                            mView.showMsg("设置成功");
                            gestureContentView.clearDrawlineState(0L);
                            SPUtils.setPrefString(mContext, Constant.SP.gesture_pwd,inputCode);
                            mView.STActivity(MainActivity.class);
                            mView.actFinish();
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
    }

    @Override
    public GestureContentView initUpdateGestureContentView(final TextView text_reset,final  TextView tv_verify_tip, FrameLayout fl_gesture_noClick) {
        return new GestureContentView(mContext,false,null, new GestureDrawline.GestureCallBack() {
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
                        mView.showMsg("设置成功");
                        gestureContentView.clearDrawlineState(0L);
                        SPUtils.setPrefString(mContext, Constant.SP.gesture_pwd,inputCode);
                        mView.actFinish();
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
    }
    @Override
    public GestureContentView initVerifyOldGestureContentView(final String pwd,final TextView text_reset,final  TextView tv_verify_tip, FrameLayout fl_gesture_noClick) {
        return new GestureContentView(mContext,true,pwd, new GestureDrawline.GestureCallBack() {
            @Override
            public void onGestureCodeInput(String inputCode) {

            }
            @Override
            public void checkedSuccess() {
                pwdValidationSuccess=true;
                mLockIndicator.setPath("");
                gestureContentView.clearDrawlineState(0);
                mView.pwdValidationSuccess();
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
    }


    @Override
    public void resetPwd() {
        mIsFirstInput = true;
        updateCodeList("");
    }

    private boolean isInputPassValidate(String inputPassword) {
        if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
            return false;
        }
        return true;
    }
    @Override
    public GestureContentView initVerifyGestureContentView(final String gesturePWD, final TextView tv_verify_tip, final FrameLayout fl_gesture_noClick) {
        return new GestureContentView(mContext, true, gesturePWD,
                new GestureDrawline.GestureCallBack() {
                    @Override
                    public void onGestureCodeInput(String inputCode) {

                    }
                    @Override
                    public void checkedSuccess() {
                        gestureContentView.clearDrawlineState(0L);
//                        mView.showMsg("密码正确");
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
                    /*if(mHandler!=null){
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
                    }*/
                }
            };
        }
    }
}
