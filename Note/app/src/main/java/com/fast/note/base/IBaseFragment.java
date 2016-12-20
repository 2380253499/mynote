package com.fast.note.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.hwangjr.rxbus.RxBus;
import com.fast.note.adapter.CommonAdapter;
import com.fast.note.tools.MyDialog;
import com.fast.note.view.Loading;
import com.fast.note.view.MyPopupwindow;

import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

public class IBaseFragment extends Fragment {
    protected Intent mIntent;
    protected Unbinder mUnBind;
    protected CommonAdapter mAdapter;
    protected MyPopupwindow mPopupwindow;
    protected MyDialog.Builder mDialog;
    private Handler mHandler;
    /****************************RxJava********************************/
    protected Observable mObservable;
    protected Subscriber mSubscriber,cSubscriber;
    protected Action1 mAction1;
    protected Action0 mAction0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.get().register(this);
        mIntent =new Intent();
    }
    protected Handler getmHandler(){
        if(mHandler==null){
            mHandler=new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }
    protected void showToastS(String toast){
        Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
    }
    protected void showToastL(String toast){
        Toast.makeText(getActivity(),toast,Toast.LENGTH_LONG).show();
    }
    protected void STActivityForResult(Class clazz,int requestCode){
        startActivityForResult(new Intent(getActivity(), clazz), requestCode);
    }
    protected void STActivityForResult(Intent intent,Class clazz,int requestCode){
        intent.setClass(getActivity(), clazz);
        startActivityForResult(intent, requestCode);
    }
    protected void STActivity(Class clazz){
        startActivity(new Intent(getActivity(), clazz));
    }
    protected void STActivity(Intent intent,Class clazz){
        intent.setClass(getActivity(), clazz);
        startActivity(intent);
    }
    protected void showLoading(boolean isExit){
        Loading.showForExit(getActivity(), isExit);
    }
    protected void showLoading(){
        Loading.show(getActivity());
    }
    protected void dismissLoading(){
        Loading.dismissLoading();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBind.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIntent =null;
        RxBus.get().unregister(this);
        if(mSubscriber!=null&&!mSubscriber.isUnsubscribed()){
            mSubscriber.unsubscribe();
        }
        if(mHandler!=null){
            mHandler=null;
        }
    }
}
