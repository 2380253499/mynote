package com.haitaoit.peihuotong;

import android.os.SystemClock;
import android.util.Log;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void bb() throws Exception {
        String logString="asdfasfafsd";
        if (logString.length() > 2000) {
//            i.i("LogUtils", "logString.length = " + logString.length());
            int chunkCount = logString.length() / 2000;
            for (int i = 0; i <= chunkCount; i++) {
                int max = 2000 * (i + 1);
                if (max >= logString.length()) {
                    Log.i("log", logString.substring(2000 * i));
                } else {
                    Log.i("log", logString.substring(2000 * i, max));
                }
            }
        } else {
            Log.i("log",logString.toString());
        }
    }
    @Test
    public void a() throws Exception {
        String string = "此规格库存不足#￥@#hgfd";
        String str=string.trim();
        String numStr="";
        if(str != null && !"".equals(str)){
            for(int i=0;i<str.length();i++){
                if(str.charAt(i)>=48 && str.charAt(i)<=57){
                    numStr+=str.charAt(i);
                }
            }
        }
        System.out.println(numStr.length());
    }
    @Test
    public void b() throws Exception {
        String a="#asfdsf";
        String[] split = a.split("#");
        System.out.println(split.length);
        for (int i = 0; i < split.length; i++) {
            System.out.println(split[i]);
        }
    }
    @Test
    public void addition_isCorrect() throws Exception {
        final long time1=System.currentTimeMillis();
        System.out.println(time1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(5000);
                long time2=System.currentTimeMillis();
                System.out.println(time2);
                System.out.println(time2-time1);
            }
        }).start();
    }
}