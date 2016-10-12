package com.zr.note.base.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

import com.zr.note.R;

/**
 * Created by Administrator on 2016/9/6.
 */
public class MyEditText extends EditText {
    public MyEditText(Context context) {
        super(context);
        init(null);
    }
    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }
    private void init(AttributeSet attrs){
        if(attrs==null){
            return;
        }
        Drawable background = getBackground();
        if (background instanceof ColorDrawable&&background!=null) {
            return;
        }
        TypedArray viewNormal = this.getContext().obtainStyledAttributes(attrs, R.styleable.MyEditText);
        float borderWidth = viewNormal.getDimension(R.styleable.MyEditText_my_et_border_width, 0);
        int solidColor = viewNormal.getColor(R.styleable.MyEditText_my_et_solid, Color.parseColor("#00000000"));
        int borderColor = viewNormal.getColor(R.styleable.MyEditText_my_et_border_color, -1);
        float dashWidth = viewNormal.getDimension(R.styleable.MyEditText_my_et_border_dashWidth, 0);
        float dashGap = viewNormal.getDimension(R.styleable.MyEditText_my_et_border_dashGap, 0);

        float radius = viewNormal.getDimension(R.styleable.MyEditText_my_et_corner_radius, 0);
        GradientDrawable gradientDrawable=new GradientDrawable();
        gradientDrawable.setStroke((int) borderWidth, borderColor, dashWidth, dashGap);
        gradientDrawable.setColor(solidColor);
        if(radius>0){
            gradientDrawable.setCornerRadius(radius);
        }else{
            float topLeftRadius = viewNormal.getDimension(R.styleable.MyEditText_my_et_corner_topLeftRadius, 0);
            float topRightRadius = viewNormal.getDimension(R.styleable.MyEditText_my_et_corner_topRightRadius, 0);
            float bottomLeftRadius = viewNormal.getDimension(R.styleable.MyEditText_my_et_corner_bottomLeftRadius, 0);
            float bottomRightRadius = viewNormal.getDimension(R.styleable.MyEditText_my_et_corner_bottomRightRadius, 0);
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
