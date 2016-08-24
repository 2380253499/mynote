package com.zr.note.base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/8/4.
 */
public class IBaseActivity extends AppCompatActivity implements View.OnClickListener{
    protected void showSToast(String toast){
        Toast.makeText(this,toast,Toast.LENGTH_SHORT).show();
    }
    protected void showLToast(String toast){
        Toast.makeText(this,toast,Toast.LENGTH_LONG).show();
    }
    protected void SActivity(Class clazz){
        startActivity(new Intent(this,clazz));
    }

    @Override
    public void onClick(View v) {

    }
}
