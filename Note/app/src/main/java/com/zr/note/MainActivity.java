package com.zr.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zr.note.activity.Main3Activity;
import com.zr.note.tools.MyToast;
import com.zr.note.tools.StatusBarCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this);//改变状态栏核心代码
        setContentView(R.layout.activity_main);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawerlayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setSubtitle("Subtitle");
        toolbar.setTitle("setTitle");
        setSupportActionBar(toolbar);
        toolbar.getNavigationIcon();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Main3Activity.class));
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
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

    }
}
