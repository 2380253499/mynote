package com.zr.note.ui.gesture.activity.contract.imp;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.zr.note.R;
import com.zr.note.base.IPresenter;
import com.zr.note.tools.AES;
import com.zr.note.tools.SPUtils;
import com.zr.note.tools.gesture.widget.GestureContentView;
import com.zr.note.tools.gesture.widget.GestureDrawline;
import com.zr.note.ui.gesture.activity.GestureEdit2Activity;
import com.zr.note.ui.gesture.activity.contract.GestureCon;
import com.zr.note.ui.main.activity.MainActivity;

/**
 * Created by Administrator on 2016/10/26.
 */
public class GestureImp extends IPresenter<GestureCon.View> implements GestureCon.Presenter{
    public GestureImp(Context context) {
        super(context);
    }


}
