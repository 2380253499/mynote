package com.fast.note.ui.main.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.fast.note.R;
import com.fast.note.tools.PhoneUtils;
import com.fast.note.view.MyPopupwindow;

public class ScrollingActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  MyPopupwindow popupwindow = new MyPopupwindow(ScrollingActivity.this, R.layout.popu_options);
                  int xoff = PhoneUtils.getPhoneWidth(ScrollingActivity.this) - PhoneUtils.dip2px(ScrollingActivity.this, 115);
                        popupwindow.showAsDropDown(toolbar, xoff, 0);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 1;
        float radius = 20;
//        if (downScale.isChecked()) {
//            scaleFactor = 8;
//            radius = 2;
//        }

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / scaleFactor),
                (int) (view.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

//        overlay = FastBlur.doBlur(overlay, (int) radius, true);
//        view.setBackground(new BitmapDrawable(getResources(), overlay));
//        statusText.setText(System.currentTimeMillis() - startMs + "ms");
    }
}
