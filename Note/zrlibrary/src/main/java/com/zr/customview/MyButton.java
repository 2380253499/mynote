package com.zr.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

import com.zr.R;


/**
 * Created by Administrator on 2016/8/29.
 */
public class MyButton extends Button{
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
            init(attrs);
    }
    public MyButton(Context context) {
        super(context);
            init(null);
    }
    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
            init(attrs);
    }
    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
            init(attrs);
    }
    private void init(AttributeSet attrs){
        this.setClickable(true);
        if(attrs==null){
            return;
        }
        TypedArray viewNormal = this.getContext().obtainStyledAttributes(attrs, R.styleable.MyButton);
        Drawable my_normal = viewNormal.getDrawable(R.styleable.MyButton_my_bt_normal);
        Drawable my_press = viewNormal.getDrawable(R.styleable.MyButton_my_bt_press);
        viewNormal.recycle();
        if(my_normal!=null&&my_press!=null){
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, my_press);
            stateListDrawable.addState(new int[]{}, my_normal);
            this.setBackground(stateListDrawable);
        }
    }
}
