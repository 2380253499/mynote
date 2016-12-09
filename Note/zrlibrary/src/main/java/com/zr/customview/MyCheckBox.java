package com.zr.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.CheckBox;

import zr.note.tools.R;

/**
 * Created by Administrator on 2016/8/29.
 */
public class MyCheckBox extends CheckBox{
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyCheckBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
            init(attrs);
    }
    public MyCheckBox(Context context) {
        super(context);
            init(null);
    }
    public MyCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
            init(attrs);
    }
    public MyCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
            init(attrs);
    }
    private void init(AttributeSet attrs){
        this.setClickable(true);
        if(attrs==null){
            return;
        }
        TypedArray viewNormal = this.getContext().obtainStyledAttributes(attrs, R.styleable.MyCheckBox);
        Drawable my_normal = viewNormal.getDrawable(R.styleable.MyCheckBox_my_checkbox_normal);
        Drawable my_checked = viewNormal.getDrawable(R.styleable.MyCheckBox_my_checkbox_checked);
        int my_normal_color = viewNormal.getColor(R.styleable.MyCheckBox_my_checkbox_normal_color, this.getTextColors().getDefaultColor());
        int my_checked_color = viewNormal.getColor(R.styleable.MyCheckBox_my_checkbox_checked_color,-1);
        int my_checked_drawable = viewNormal.getInteger(R.styleable.MyCheckBox_my_checkbox_checked_drawable, 0);
        viewNormal.recycle();
        if(my_normal!=null&&my_checked!=null){
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_checked}, my_checked);
            stateListDrawable.addState(new int[]{}, my_normal);
            switch (my_checked_drawable){
                case 0:
                    this.setButtonDrawable(stateListDrawable);
                break;
                case 1:
                    this.setCompoundDrawablesWithIntrinsicBounds(stateListDrawable,null,null,null);
                break;
                case 2:
                    this.setCompoundDrawablesWithIntrinsicBounds(null,stateListDrawable,null,null);
                break;
                case 3:
                    this.setCompoundDrawablesWithIntrinsicBounds(null,null,stateListDrawable,null);
                break;
                case 4:
                    this.setCompoundDrawablesWithIntrinsicBounds(null,null,null,stateListDrawable);
                break;
            }

        }

        if(my_checked_color!=-1){
            int [][]colorState=new int[2][];
            int []myColor=new int[]{my_checked_color,my_normal_color};
            colorState[0]=new int[]{android.R.attr.state_checked};
            colorState[1]=new int[]{};
            ColorStateList colorStateList=new ColorStateList(colorState,myColor);
            this.setTextColor(colorStateList);
        }
    }
}
