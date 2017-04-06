package com.zr.note.base.customview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;

import com.github.customview.MyEditText;

/**
 * Created by Administrator on 2017/4/6.
 */
public class CustomScrollView extends ScrollView{
    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
//        return super.requestChildRectangleOnScreen(child, rectangle, immediate);
        if(child instanceof MyEditText||child instanceof EditText){
            return true;
        }
        return false;
    }
}
