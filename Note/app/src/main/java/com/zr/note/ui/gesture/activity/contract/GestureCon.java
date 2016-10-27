package com.zr.note.ui.gesture.activity.contract;

import android.widget.FrameLayout;
import android.widget.TextView;

import com.zr.note.base.BasePresenter;
import com.zr.note.base.BaseView;
import com.zr.note.tools.gesture.widget.GestureContentView;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface GestureCon {
   interface View extends BaseView{

   }

   interface Presenter extends BasePresenter<View>{
      void setGestureContentView(GestureContentView mGestureContentView);
      GestureContentView initGestureContentView(String gesturePWD,TextView tv_verify_tip, FrameLayout fl_gesture_noClick);
   }

}
