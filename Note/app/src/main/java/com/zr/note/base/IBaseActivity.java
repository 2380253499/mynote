package com.zr.note.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/8/4.
 */
public class IBaseActivity extends AppCompatActivity {
    protected Intent mIntent;
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
        intent.setClass(this,clazz);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIntent=null;
    }
}
