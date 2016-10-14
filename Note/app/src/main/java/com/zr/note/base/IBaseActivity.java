package com.zr.note.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.zr.note.adapter.CommonAdapter;

/**
 * Created by Administrator on 2016/8/4.
 */
public class IBaseActivity extends AppCompatActivity {
    protected Intent mIntent;
    protected CommonAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIntent=new Intent();
    }

    protected void showToastS(String toast){
        Toast.makeText(this,toast,Toast.LENGTH_SHORT).show();
    }
    protected void showToastL(String toast){
        Toast.makeText(this,toast,Toast.LENGTH_LONG).show();
    }
    protected void STActivity(Class clazz){
        startActivity(new Intent(this, clazz));
    }
    protected void STActivity(Intent intent,Class clazz){
        intent.setClass(this, clazz);
        startActivity(intent);
    }
    protected  void addFragment(int resId,Fragment fragment){
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().add(resId, fragment).commit();
        }
    }
    protected  void replaceFragment(int resId,Fragment fragment){
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(resId, fragment).commit();
        }
    }
    protected  void hideFragment(Fragment fragment){
        if (fragment != null&&!fragment.isHidden()) {
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
        }
    }
    protected  void showFragment(Fragment fragment){
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().show(fragment).commit();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIntent=null;
    }
}
