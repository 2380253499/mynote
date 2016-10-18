package com.zr.note.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.zr.note.adapter.CommonAdapter;
import com.zr.note.tools.MyDialog;
import com.zr.note.view.MyPopupwindow;

import butterknife.Unbinder;

public class IBaseFragment extends Fragment {
    protected Intent mIntent;
    protected Unbinder mUnBind;
    protected CommonAdapter mAdapter;
    protected MyPopupwindow mPopupwindow;
    protected MyDialog.Builder mDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIntent =new Intent();
    }
    protected void showToastS(String toast){
        Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
    }
    protected void showToastL(String toast){
        Toast.makeText(getActivity(),toast,Toast.LENGTH_LONG).show();
    }
    protected void STActivity(Class clazz){
        startActivity(new Intent(getActivity(), clazz));
    }
    protected void STActivity(Intent intent,Class clazz){
        intent.setClass(getActivity(),clazz);
        startActivity(intent);
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
    }
}
