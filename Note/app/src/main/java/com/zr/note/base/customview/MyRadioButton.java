package com.zr.note.base.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.zr.note.R;

/**
 * Created by Administrator on 2016/8/30.
 */
public class MyRadioButton extends RadioButton {
    public MyRadioButton(Context context) {
        super(context);
        init(null);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if(checked){

        }else{

        }
    }
    private void init(AttributeSet attrs){
        if(attrs==null){
            return;
        }
        TypedArray typedArray = this.getContext().obtainStyledAttributes(attrs, R.styleable.MyRadioButton);

       /* int normalId = typedArray.getResourceId(typedArray.getResourceId(MyRadioButton_normal, -1),-1);
        int normalId = typedArray.getResourceId(typedArray.getResourceId(MyRadioButton_normal, -1),-1);
        int normalId = typedArray.getResourceId(typedArray.getResourceId(MyRadioButton_normal, -1),-1);
        sd.addState(new int[]{}, this.getContext().getResources().getDrawable(normalId));
        viewNormal.recycle();
        viewChecked.recycle();
        viewPress.recycle();
        viewFocus.recycle();
*/
        this.setClickable(true);//若view不能点击，则使其可以点击，selector才会生效
    }
}
