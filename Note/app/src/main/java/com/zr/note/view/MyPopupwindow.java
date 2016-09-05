package com.zr.note.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by Administrator on 2016/8/2.
 */
public class MyPopupwindow extends PopupWindow{
    private Context context;
    public MyPopupwindow(Context ctx,int contentView) {
        context=ctx;
        View view = LayoutInflater.from(ctx).inflate(contentView, null);
        setPopupwindow(view);
    }
    public MyPopupwindow(Context ctx,View contentView) {
        context=ctx;
        setPopupwindow(contentView);
    }
    private void setPopupwindow(View contentView) {
        setBackground();
        setOutsideTouchable(true);
        setFocusable(true);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setOnDismissListener(getOnDismissListener());
    }


    private void setBackground(){
        ColorDrawable cd = new ColorDrawable(0x000000);
        this.setBackgroundDrawable(cd);
        WindowManager.LayoutParams lp=((Activity)context).getWindow().getAttributes();
        lp.alpha = 0.9f;
        ((Activity)context).getWindow().setAttributes(lp);
    }
    private void onDismissBackground(){
        WindowManager.LayoutParams lp= ((Activity)context).getWindow().getAttributes();
        lp.alpha = 1f;
        ((Activity)context).getWindow().setAttributes(lp);
    }
    @NonNull
    private OnDismissListener getOnDismissListener() {
        return new OnDismissListener() {
            @Override
            public void onDismiss() {
                onDismissBackground();
            }
        };
    }


}
