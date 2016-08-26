package com.zr.note.main.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.TextView;

import com.zr.note.R;
import com.zr.note.base.BaseActivity;
import com.zr.note.inter.MyOnClickListener;
import com.zr.note.main.biz.imp.MainImp;
import com.zr.note.main.view.MainView;

public class MainActivity extends BaseActivity<MainView,MainImp>implements MainView{
    private DrawerLayout drawerLayout;
    private TextView tv_a1,tv_a2;
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
                showToastS("onNoDoubleClick");
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
            }
        });*/
    }
    @Override
    protected void setToolbarStyle() {
        setToolbarTitle("Note");
        setToolbarSubTitle("SubNote");
        setNavigationIcon(R.drawable.ic_launcher);
    }
    @Override
    protected void initData() {

    }
    @Override
    protected void viewOnClick(View v) {
        showToastS("onNoDoubleClick"+v.getId());
        switch (v.getId()){
            case R.id.fab:
                startActivity(new Intent(MainActivity.this,SActivity.class));
            break;
        }
    }


    @Override
    protected MainImp initImp() {
        return new MainImp();
    }

    @Override
    public void show() {

    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this);//改变状态栏核心代码
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        Window window = getWindow();
//        window.setNavigationBarColor(getColor(R.color.colorPrimaryDark1));
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_main);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawerlayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setSubtitle("Subtitle");
        toolbar.setTitle("setTitle11");
        setSupportActionBar(toolbar);
        toolbar.getNavigationIcon();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
                *//*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*//*
            }
        });
        tv_a1= (TextView) findViewById(R.id.tv_a1);
        tv_a2= (TextView) findViewById(R.id.tv_a2);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            MyToast.showToast(this,"item");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }*/
}
