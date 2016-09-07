package com.zr.note.base.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.zr.note.R;

/**
 * Created by Administrator on 2016/9/6.
 */
public class MyLinearLayout extends LinearLayout{
    public MyLinearLayout(Context context) {
        super(context);
        init(null);
    }
    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }
    private void init(AttributeSet attrs){
        if(attrs==null){
            return;
        }
        TypedArray viewNormal = this.getContext().obtainStyledAttributes(attrs, R.styleable.MyLinearLayout);
        float borderWidth = viewNormal.getDimension(R.styleable.MyLinearLayout_my_ll_border_width, 0);
        int solidColor = viewNormal.getColor(R.styleable.MyLinearLayout_my_ll_solid, Color.parseColor("#00000000"));
        int borderColor = viewNormal.getColor(R.styleable.MyLinearLayout_my_ll_border_color, -1);
        float dashWidth = viewNormal.getDimension(R.styleable.MyLinearLayout_my_ll_border_dashWidth, 0);
        float dashGap = viewNormal.getDimension(R.styleable.MyLinearLayout_my_ll_border_dashGap, 0);

        float radius = viewNormal.getDimension(R.styleable.MyLinearLayout_my_ll_corner_radius, 0);
        GradientDrawable gradientDrawable=new GradientDrawable();
        gradientDrawable.setStroke((int) borderWidth, borderColor, dashWidth, dashGap);
        gradientDrawable.setColor(solidColor);
        if(radius>0){
            gradientDrawable.setCornerRadius(radius);
        }else{
            float topLeftRadius = viewNormal.getDimension(R.styleable.MyLinearLayout_my_ll_corner_topLeftRadius, 0);
            float topRightRadius = viewNormal.getDimension(R.styleable.MyLinearLayout_my_ll_corner_topRightRadius, 0);
            float bottomLeftRadius = viewNormal.getDimension(R.styleable.MyLinearLayout_my_ll_corner_bottomLeftRadius, 0);
            float bottomRightRadius = viewNormal.getDimension(R.styleable.MyLinearLayout_my_ll_corner_bottomRightRadius, 0);
            float[] fourRadius=new float[]{topLeftRadius,topLeftRadius,topRightRadius,topRightRadius,bottomRightRadius,bottomRightRadius,bottomLeftRadius,bottomLeftRadius};
            gradientDrawable.setCornerRadii(fourRadius);
        }
        viewNormal.recycle();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.setBackground(gradientDrawable);
        }else{
            this.setBackgroundDrawable(gradientDrawable);
        }
    }
}
