package com.mynote.module.leftmenu.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.github.androidtools.DateUtils;
import com.github.androidtools.PhoneUtils;
import com.github.customview.MyButton;
import com.github.customview.MyCheckBox;
import com.github.customview.MyEditText;
import com.mynote.R;
import com.mynote.base.BaseActivity;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/8.
 */

public class AddBirthdayActivity extends BaseActivity {

    @BindView(R.id.et_addBirthday_name)
    MyEditText et_addBirthday_name;
    @BindView(R.id.tv_addBirthday_date)
    TextView tv_addBirthday_date;
    @BindView(R.id.cb_addBirthday_tx)
    MyCheckBox cb_addBirthday_tx;
    @BindView(R.id.bt_addBirthday_save)
    MyButton bt_addBirthday_save;
    int year,month,day;
    @Override
    protected int getContentView() {
        setAppTitle("增加生日");
        return R.layout.act_add_birthday;
    }

    @Override
    protected void initView() {


    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.bt_addBirthday_save,R.id.tv_addBirthday_date})
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.tv_addBirthday_date:
                PhoneUtils.hiddenKeyBoard(mContext);
                showPicker();
                break;
            case R.id.bt_addBirthday_save:
                String name = getSStr(et_addBirthday_name);
                String date = getSStr(tv_addBirthday_date);
                boolean checked = cb_addBirthday_tx.isChecked();
                if(TextUtils.isEmpty(name)){
                    showMsg("请输入姓名");
                    return;
                }else if(TextUtils.isEmpty(date)){
                    showMsg("请选择生日");
                    return;
                }
                addBirthday(name,checked);
                break;
        }
    }

    private void showPicker() {
       /* Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        Log("==="+calendar.get(Calendar.YEAR));
        TimePickerView timePickerView=new TimePickerView(mContext,TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setRange(1950,calendar.get(Calendar.YEAR));
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                Calendar instance = Calendar.getInstance();
                instance.setTime(date);
                year = instance.get(Calendar.YEAR);
                month = instance.get(Calendar.MONTH)+1;
                day = instance.get(Calendar.DAY_OF_MONTH);;
                Log("==="+year+"="+month+"="+day);
                String format = DateUtils.dateToString(date);
                tv_addBirthday_date.setText(format);
            }
        });
        timePickerView.show();*/
        Calendar startTime= Calendar.getInstance();
        startTime.set(Calendar.YEAR,1950);

        Calendar endTime= Calendar.getInstance();
        endTime.setTime(new Date());
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Calendar instance = Calendar.getInstance();
                instance.setTime(date);
                year = instance.get(Calendar.YEAR);
                month = instance.get(Calendar.MONTH)+1;
                day = instance.get(Calendar.DAY_OF_MONTH);;
                Log("==="+year+"="+month+"="+day);
                String format = DateUtils.dateToString(date);
                tv_addBirthday_date.setText(format);
            }
        }).setRangDate(startTime,endTime).setType(new boolean[]{true,true,true,false,false,false}).build();
        pvTime.show();
    }

    private void addBirthday(String name, boolean checked) {

    }
}
