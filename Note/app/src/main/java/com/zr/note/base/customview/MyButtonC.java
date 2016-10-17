package com.zr.note.base.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

import com.zr.note.R;

/**
 * Created by Administrator on 2016/8/29.
 */
public class MyButtonC extends Button{
    private float radius=0;
    private float topLeftRadius=0;
    private float topRightRadius=0;
    private float bottomLeftRadius=0;
    private float bottomRightRadius=0;
    private StateListDrawable stateListDrawable;
    private int normalColor;
    private int pressColor;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyButtonC(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
            init(attrs);
    }
    public void setRadius(float radius){
        this.radius=radius;
    }

    public void setTopRightRadius(float topRightRadius) {
        this.topRightRadius = topRightRadius;
    }

    public void setBottomLeftRadius(float bottomLeftRadius) {
        this.bottomLeftRadius = bottomLeftRadius;
    }
    public void setBottomRightRadius(float bottomRightRadius) {
        this.bottomRightRadius = bottomRightRadius;
    }

    public void setTopLeftRadius(float topLeftRadius) {
        this.topLeftRadius = topLeftRadius;
    }

    public MyButtonC(Context context) {
        super(context);
            init(null);
    }

    public MyButtonC(Context context, AttributeSet attrs) {
        super(context, attrs);
            init(attrs);
    }
    public MyButtonC(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
            init(attrs);
    }

    private void init(AttributeSet attrs){
        this.setClickable(true);
        if(attrs==null){
            return;
        }
        TypedArray viewNormal = this.getContext().obtainStyledAttributes(attrs, R.styleable.MyButtonC);
        normalColor = viewNormal.getColor(R.styleable.MyButtonC_my_btc_normal, Color.parseColor("#00000000"));
        pressColor = viewNormal.getColor(R.styleable.MyButtonC_my_btc_press, Color.parseColor("#00000000"));
        radius = viewNormal.getDimension(R.styleable.MyButtonC_my_btc_corner_radius, 0);
        topLeftRadius = viewNormal.getDimension(R.styleable.MyButtonC_my_btc_corner_topLeftRadius, 0);
        topRightRadius = viewNormal.getDimension(R.styleable.MyButtonC_my_btc_corner_topRightRadius, 0);
        bottomLeftRadius = viewNormal.getDimension(R.styleable.MyButtonC_my_btc_corner_bottomLeftRadius, 0);
        bottomRightRadius = viewNormal.getDimension(R.styleable.MyButtonC_my_btc_corner_bottomRightRadius, 0);
        viewNormal.recycle();
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        GradientDrawable gradientDrawable=new GradientDrawable();
        GradientDrawable gradientDrawable2=new GradientDrawable();
        gradientDrawable.setColor(normalColor);
        gradientDrawable2.setColor(pressColor);
        if(radius>0){
            gradientDrawable.setCornerRadius(radius);
            gradientDrawable2.setCornerRadius(radius);
        }else{
            float[] fourRadius=new float[]{topLeftRadius,topLeftRadius,topRightRadius,topRightRadius,bottomRightRadius,bottomRightRadius,bottomLeftRadius,bottomLeftRadius};
            gradientDrawable.setCornerRadii(fourRadius);
            gradientDrawable2.setCornerRadii(fourRadius);
        }
        stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, gradientDrawable2);
        stateListDrawable.addState(new int[]{}, gradientDrawable);
        this.setBackground(stateListDrawable);
    }
}
