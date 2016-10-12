package com.zr.note.base.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
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
        Drawable background = getBackground();
        if (background instanceof ColorDrawable &&background!=null) {
            return;
        }
        TypedArray viewNormal = this.getContext().obtainStyledAttributes(attrs, R.styleable.MyLinearLayout);
        boolean leftLine=viewNormal.getBoolean(R.styleable.MyLinearLayout_my_ll_left_line, false);
        boolean topLine=viewNormal.getBoolean(R.styleable.MyLinearLayout_my_ll_top_line,false);
        boolean rightLine=viewNormal.getBoolean(R.styleable.MyLinearLayout_my_ll_right_line,false);
        boolean bottomLine=viewNormal.getBoolean(R.styleable.MyLinearLayout_my_ll_bottom_line, false);
        float borderWidth = viewNormal.getDimension(R.styleable.MyLinearLayout_my_ll_border_width, 0);
        int solidColor = viewNormal.getColor(R.styleable.MyLinearLayout_my_ll_solid, Color.parseColor("#00000000"));
        int borderColor = viewNormal.getColor(R.styleable.MyLinearLayout_my_ll_border_color,Color.parseColor("#00000000"));
        float dashWidth = viewNormal.getDimension(R.styleable.MyLinearLayout_my_ll_border_dashWidth, 0);
        float dashGap = viewNormal.getDimension(R.styleable.MyLinearLayout_my_ll_border_dashGap, 0);
        float radius = viewNormal.getDimension(R.styleable.MyLinearLayout_my_ll_corner_radius, 0);
        int[]lineType=new int[4];
        boolean lineFlag=false;
        if(leftLine){
            lineFlag=true;
            lineType[0]=(int)borderWidth;
        }
        if(topLine){
            lineFlag=true;
            lineType[1]=(int)borderWidth;
        }
        if(rightLine){
            lineFlag=true;
            lineType[2]=(int)borderWidth;
        }
        if(bottomLine){
            lineFlag=true;
            lineType[3]=(int)borderWidth;
        }
        if(lineFlag){//layer-list
            GradientDrawable layerDrawable0=new GradientDrawable();
            layerDrawable0.setColor(borderColor);
            layerDrawable0.setCornerRadius(radius);
            GradientDrawable layerDrawable1=new GradientDrawable();
            layerDrawable1.setColor(solidColor);
            layerDrawable1.setCornerRadius(radius);
            Drawable[] layers = new Drawable[2];
            layers[0] = layerDrawable0;
            layers[1] = layerDrawable1;
            LayerDrawable layerDrawable = new LayerDrawable(layers);
            layerDrawable.setLayerInset(1, lineType[0], lineType[1], lineType[2], lineType[3]);
            viewNormal.recycle();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                this.setBackground(layerDrawable);
            }else{
                this.setBackgroundDrawable(layerDrawable);
            }
        }else{//shape
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
}
