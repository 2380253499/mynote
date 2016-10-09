package com.zr.note.ui.main.activity;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zr.note.R;
import com.zr.note.base.BaseActivity;
import com.zr.note.inter.MyOnClickListener;
import com.zr.note.network.OKHttpUtils;
import com.zr.note.network.ResultCallback;
import com.zr.note.tools.LogUtils;
import com.zr.note.tools.PhoneUtils;
import com.zr.note.ui.main.contract.MainContract;
import com.zr.note.ui.main.contract.imp.MainImp;
import com.zr.note.view.MyPopupwindow;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class MainActivity extends BaseActivity<MainContract.View, MainContract.Presenter> implements MainContract.View {
    @BindView(R.id.tv_a4)
    TextView tv_a4;
    private DrawerLayout drawerLayout;
    private CollapsingToolbarLayout ctl_layout;
    private TextView tv_a1, tv_a2;
    private FloatingActionButton fab;

    @Override
    protected int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        getToolbar().setNavigationOnClickListener(new MyOnClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        ctl_layout = (CollapsingToolbarLayout) findViewById(R.id.ctl_layout);
        ctl_layout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        ctl_layout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));

        findViewById(R.id.tv_a1).setOnClickListener(this);
        findViewById(R.id.tv_a2).setOnClickListener(this);
        findViewById(R.id.tv_a3).setOnClickListener(this);
    }

    @Override
    protected void setToolbarStyle() {
        setToolbarTitle("Note");
        setNavigationIcon(R.drawable.drawer_menu);
    }

    @Override
    protected int setOptionsMenu() {
        return R.menu.menu_main;
    }

    @Override
    protected void initData() {

    }

    String URL;
    private void bb() {
        URL="http://61.152.255.241:8082/sales-web/mobile/cust/addCustomerdelMobile";
        Map<String,String> map=new HashMap<String,String>();
        map.put("price","0-900");
        map.put("other","噶哈哈哈你啊好11");
        map.put("area","");
        map.put("fromToRoom","1-2");
        map.put("token","72b092d47d1d45eba99fdbb6246825a6");
        map.put("custCode","C98040000041");
        map.put("reqType","rent");
        map.put("distCode","");
        map.put("acreage","0-50");
        map.put("randomTime","1475982728");
        OKHttpUtils.postAsyn(URL, map, new ResultCallback<String>() {
            @Override
            public void onError(Call call, Exception e) {
                Log.i("===", "===" + e);
            }

            @Override
            public void onSuccess(String response) {
                LogUtils.Log(response);
            }
        });
    }
    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_a3:
                bb();
                break;
            case R.id.tv_a4:
                break;
            case R.id.tv_a2:
                break;
            case R.id.tv_a1:
                break;
            case R.id.fab:
                break;
        }
    }


    @Override
    protected void menuOnClick(int itemId) {
        switch (itemId) {
            case R.id.action_settings:
//                startActivity(new Intent(MainActivity.this, SActivity.class));
                MyPopupwindow popupwindow = new MyPopupwindow(this, R.layout.layout_options);
                int xoff = PhoneUtils.getPhoneWidth(this) - PhoneUtils.dip2px(this, 115);
                popupwindow.showAsDropDown(getToolbar(), xoff, 0);
                break;
        }
    }


    @Override
    protected MainImp initPresenter() {
        return new MainImp();
    }

    @OnClick(R.id.tv_a4)
    public void onClick(TextView View) {
        LogUtils.Log("onClick");
        View.setText("tv_a");
        showToastS("tv_a4");
    }

}
