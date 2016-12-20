package com.zr.note.ui.main.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zr.note.R;
import com.zr.note.tools.ClickUtils;
import com.zr.note.tools.MyToast;

public class Main3Activity extends AppCompatActivity implements View.OnClickListener {
    View baseView;
    TextView tv,tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseView=getLayoutInflater().inflate(R.layout.activity_main3,null);
        setContentView(baseView);
        initView();
    }

    private void initView() {
        tv= (TextView) findViewById(R.id.tv);
        tv2= (TextView) findViewById(R.id.tv2);
        tv.setOnClickListener(this);
        tv2.setOnClickListener(this);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
            }
        });*/
    }

    int i,j;
    @Override
    public void onClick(View v) {
        if(ClickUtils.isFastClick(v)){
            return;
        }
        switch (v.getId()){
            case R.id.tv:
                MyToast.showToast(this, "tv" + i++);
                Log.i("=1=========","=1========="+i);
            break;
            case R.id.tv2:
                MyToast.showToast(this,"tv22"+j++);
                Log.i("=2=========", "=2=========" + j);
            break;
        }
    }

}
