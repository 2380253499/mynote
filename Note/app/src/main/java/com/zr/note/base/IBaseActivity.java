package com.zr.note.base;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/8/4.
 */
public class IBaseActivity extends AppCompatActivity {
    protected void showSToast(String toast){
        Toast.makeText(this,toast,Toast.LENGTH_SHORT).show();
    }
    protected void showLToast(String toast){
        Toast.makeText(this,toast,Toast.LENGTH_LONG).show();
    }
}
