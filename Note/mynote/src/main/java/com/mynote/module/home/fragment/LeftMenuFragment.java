package com.mynote.module.home.fragment;

import android.content.res.ColorStateList;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.androidtools.AES;
import com.github.androidtools.DateUtils;
import com.github.androidtools.PhoneUtils;
import com.mynote.R;
import com.mynote.base.BaseFragment;
import com.mynote.module.secret.activity.SecretActivity;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;

public class LeftMenuFragment extends BaseFragment {
    @BindView(R.id.nav_container)
    NavigationView nav_container;

    View v_click_view;
    TextView tv_super_pwd;

    private TextView tv_leftmenu_qq;
    private ImageView civ_head;
    private View headerView;
    private int clickNum;
    private int secretNum;
    @Override
    protected int getContentView() {
        return R.layout.fragment_left_menu;
    }
    @Override
    protected void initView() {
        headerView = nav_container.getHeaderView(0);
        civ_head= (ImageView) headerView.findViewById(R.id.civ_head);
        civ_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNum++;
            }
        });
        tv_super_pwd= (TextView) headerView.findViewById(R.id.tv_super_pwd);
        tv_super_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(secretNum==2){
                    tv_super_pwd.setVisibility(View.GONE);

                    STActivity(SecretActivity.class);
                }else if(secretNum<2){
                    secretNum++;
                }
            }
        });
        v_click_view=headerView.findViewById(R.id.v_click_view);
        v_click_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickNum>=5){
                    clickNum=0;
                    tv_super_pwd.setText(getSuperPWD());
                    tv_super_pwd.setVisibility(View.VISIBLE);
                }else{
                    secretNum=0;
                    tv_super_pwd.setVisibility(View.GONE);
                }
            }
        });
        Glide.with(getActivity()).load(R.drawable.bird).into(civ_head);
        tv_leftmenu_qq= (TextView) headerView.findViewById(R.id.tv_leftmenu_qq);
        tv_leftmenu_qq.setOnClickListener(this);
        nav_container.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
//                mPresenter.itemClick(item.getItemId());
                return false;
            }
        });
        nav_container.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorText)));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.tv_leftmenu_qq:
                PhoneUtils.copyText(getActivity(), "271910854");
                showToastS("复制成功");
                break;
        }
    }

    public String getSuperPWD() {
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
        String encode = AES.encode(strMinute);
        return encode.substring(0,10);
    }
}
