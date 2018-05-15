package com.mynote.module.leftmenu.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.github.androidtools.DateUtils;
import com.github.androidtools.PhoneUtils;
import com.github.customview.MyButton;
import com.github.customview.MyCheckBox;
import com.github.customview.MyEditText;
import com.mynote.R;
import com.mynote.base.BaseActivity;

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

    @OnClick({R.id.bt_addBirthday_save})
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
        TimePickerView timePickerView=new TimePickerView(mContext,TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setRange(1950,new Date().getYear());
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                year = date.getYear();
                month = date.getMonth()+1;
                day = date.getDay();
                Log("==="+year+"="+month+"="+day);
                String format = DateUtils.dateToString(date);
                tv_addBirthday_date.setText(format);
            }
        });
        timePickerView.show();
    }

    private void addBirthday(String name, boolean checked) {

    }
}
