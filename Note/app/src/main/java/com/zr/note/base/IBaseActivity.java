package com.zr.note.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hwangjr.rxbus.RxBus;
import com.zr.note.adapter.CommonAdapter;
import com.zr.note.tools.MyDialog;
import com.zr.note.view.Loading;
import com.zr.note.view.MyPopupwindow;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;


/**
 * Created by Administrator on 2016/8/4.
 */
public class IBaseActivity extends AppCompatActivity {
    protected Activity mContext;
    protected Intent mIntent;
    protected CommonAdapter mAdapter;
    protected Handler mHandler;
    protected long mExitTime;
    protected MyPopupwindow mPopupwindow;
    protected MyDialog.Builder mDialog;
    /****************************RxJava********************************/
    protected Observable mObservable;
    protected Subscriber mSubscriber;
    protected Action1 mAction1;
    protected Action0 mAction0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.get().register(this);
        mIntent=new Intent();
        mContext=this;
    }
    protected Intent getmIntent(){
        return new Intent();
    }
    protected View inflateView(int resource,ViewGroup viewGroup){
        return LayoutInflater.from(this).inflate(resource, viewGroup);
    }
    protected View inflateView(int resource){
        return inflateView(resource, null);
    }
    protected void showToastS(String toast){
        Toast.makeText(this,toast,Toast.LENGTH_SHORT).show();
    }
    protected void showToastL(String toast){
        Toast.makeText(this,toast,Toast.LENGTH_LONG).show();
    }
    protected void STActivityForResult(Class clazz,int requestCode){
        startActivityForResult(new Intent(this, clazz), requestCode);
    }
    protected void STActivityForResult(Intent intent,Class clazz,int requestCode){
        intent.setClass(this, clazz);
        startActivityForResult(intent,requestCode);
    }
    protected void STActivity(Class clazz){
        startActivity(new Intent(this, clazz));
    }
    protected void STActivity(Intent intent,Class clazz){
        intent.setClass(this, clazz);
        startActivity(intent);//ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

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
    protected String getSStr(View view){
        if(view instanceof TextView){
            return ((TextView)view).getText().toString();
        } else if (view instanceof EditText) {
            return ((EditText)view).getText().toString();
        }else{
            return null;
        }
    }
    protected void showLoading(boolean isExit){
        Loading.showForExit(this,isExit);
    }
    protected void showLoading(){
        Loading.show(this);
    }
    protected void dismissLoading(){
        Loading.dismissLoading();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIntent=null;
        mHandler=null;
        mContext=null;
        RxBus.get().unregister(this);
        if(mSubscriber!=null&&!mSubscriber.isUnsubscribed()){
            mSubscriber.unsubscribe();
        }
    }
}
