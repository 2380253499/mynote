package com.zr.note.ui.gesture.activity.contract;

import android.widget.FrameLayout;
import android.widget.TextView;

import com.zr.note.base.BasePresenter;
import com.zr.note.base.BaseView;
import com.zr.note.tools.gesture.widget.GestureContentView;
import com.zr.note.tools.gesture.widget.LockIndicator;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface GestureCon {
   interface View extends BaseView {
      void pwdValidationSuccess();
   }
   interface Presenter extends BasePresenter<View> {
      void setGestureContentView(GestureContentView mGestureContentView);
      /**
       * 重置手势view
       * @param lockIndicator
       */
      void setLockIndicator(LockIndicator lockIndicator);
      /**
       *首次编辑手势密码
       * @return
       */
      GestureContentView initEditGestureContentView(TextView text_reset,TextView tv_verify_tip, FrameLayout fl_gesture_noClick);

      /**
       *修改手势密码
       * @return
       */
      GestureContentView initUpdateGestureContentView(TextView text_reset,TextView tv_verify_tip, FrameLayout fl_gesture_noClick);
      /**
       *验证原手势密码
       * @return
       */
      GestureContentView initVerifyOldGestureContentView(String pwd,TextView text_reset,TextView tv_verify_tip, FrameLayout fl_gesture_noClick);

      /**
       * 重置手势view清除小图案
       */
      void resetPwd();
      /**
       *验证手势密码
       * @return
       */
      GestureContentView initVerifyGestureContentView(String gesturePWD, TextView tv_verify_tip, FrameLayout fl_gesture_noClick);
   }

}
