package com.zr.note.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.fivehundredpx.android.blur.BlurringView;
import com.zr.note.R;
import com.zr.note.tools.FastBlur;

public class Main3Activity extends AppCompatActivity {
//    BlurLayout sample;
    View baseView;
    BlurringView blurringView;
    ImageView image;
    TextView text2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseView=getLayoutInflater().inflate(R.layout.activity_main3,null);
        setContentView(baseView);
        View view = findViewById(R.id.ll);
//        sample = (BlurLayout) findViewById(R.id.sample);
//        sample.setHoverView(baseView);

//        blurringView= (BlurringView) findViewById(R.id.blurring_view);
//        blurringView.setBlurredView(view);
//        blurringView.invalidate();
Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.aa);
//        blur( bitmap,view);
        image= (ImageView) findViewById(R.id.image);
        text2= (TextView) findViewById(R.id.text2);
        image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                image.getViewTreeObserver().removeOnPreDrawListener(this);
                image.buildDrawingCache();

                Bitmap bmp = image.getDrawingCache();
                blur(bmp, text2);
                return true;
            }
        });

    }

    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float radius = 80;

        Bitmap overlay = Bitmap.createBitmap((int)(view.getMeasuredWidth()), (int)(view.getMeasuredHeight()), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft(), -view.getTop());
        canvas.drawBitmap(bkg, 0, 0, null);
        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        view.setBackground(new BitmapDrawable(getResources(), overlay));
//        statusText.setText("cost " + (System.currentTimeMillis() - startMs) + "ms");
    }

}
