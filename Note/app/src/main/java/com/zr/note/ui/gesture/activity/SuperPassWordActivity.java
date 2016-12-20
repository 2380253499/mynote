package com.zr.note.ui.gesture.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zr.note.R;
import com.zr.note.base.BaseActivity;
import com.zr.note.base.BasePresenter;
import com.zr.note.tools.AES;
import com.zr.note.tools.DateUtils;
import com.zr.note.tools.PhoneUtils;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;


/**
 *
 * 超级密码
 *
 */
public class SuperPassWordActivity extends BaseActivity{

	@BindView(R.id.et_super_pwd)
	EditText et_super_pwd;
	@BindView(R.id.tv_submit_pwd)
	TextView tv_submit_pwd;
	@BindView(R.id.tv_copy_qq)
	TextView tv_copy_qq;

	@Override
	protected BasePresenter initPresenter() {
		return null;
	}

	@Override
	protected int setContentView() {
		return R.layout.activity_super_passwork;
	}

	@Override
	protected void setToolbarStyle() {
		setTitle("超级密码");
	}
	@Override
	protected int setOptionsMenu() {
		return 0;
	}
	@Override
	protected void initView() {

		tv_copy_qq.setOnClickListener(this);
		tv_submit_pwd.setOnClickListener(this);
	}

	@Override
	protected void initData() {

	}
	@Override
	protected void viewOnClick(View v) {
		switch (v.getId()) {
			case R.id.tv_copy_qq:
				PhoneUtils.copyText(this, "2380253499");
				showToastS("复制QQ号码成功");
				break;
			case R.id.tv_submit_pwd:
				String superPWD = et_super_pwd.getText().toString().trim();
				if(TextUtils.isEmpty(superPWD)){
					showToastS("请输入超级密码");
				}else{
					String time = DateUtils.dateToString(new Date(), "yyyyMMdd");
					int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
					String strHour=hour+"";
					if(hour<10){
						strHour="0"+hour;
					}
					int minute = Calendar.getInstance().get(Calendar.MINUTE);
					String strMinute="00";
					if(minute>55){
						strMinute="55";
					}else if(minute>50){
						strMinute="50";
					}else if(minute>45){
						strMinute="45";
					}else if(minute>40){
						strMinute="40";
					}else if(minute>35){
						strMinute="35";
					}else if(minute>30){
						strMinute="30";
					}else if(minute>25){
						strMinute="25";
					}else if(minute>20){
						strMinute="20";
					}else if(minute>15){
						strMinute="15";
					}else if(minute>10){
						strMinute="10";
					}else if(minute>5){
						strMinute="05";
					}else if(minute>=0){
						strMinute="00";
					}
					strMinute=time+""+strHour+""+strMinute+"note";
					Log.i("====","===="+strMinute);
					String encode = AES.encode(strMinute);
//					et_super_pwd.setText(encode.substring(0,10));
					if(superPWD.equalsIgnoreCase(encode.substring(0,10))){
//						showToastS("密码正确");
						setResult(RESULT_OK);
						finish();
					}else{
						showToastS("超级密码不正确,请重新输入");
					}
				}
				break;
			default:
				break;
		}
	}

	public static void main(String[] args) {
		System.out.println(AES.encode("201611280935note"));
		System.out.println(AES.encode("201611280935note").substring(0,10));
		if("C42B3BFE89".equalsIgnoreCase(AES.encode("201611251703note").substring(0,10))){
			System.out.println("true");
		}
	}
	@Override
	protected void menuOnClick(int itemId) {

	}
}
