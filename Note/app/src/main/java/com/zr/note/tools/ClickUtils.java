package com.zr.note.tools;

import android.util.SparseLongArray;
import android.view.View;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/8/10.
 */
public class ClickUtils {
    private static SparseLongArray sLastClickTime;

    public synchronized static boolean isFastClick(View view){
        return isFastClick(view,1500);
    }
    public synchronized static boolean isFastClick(View view,int time){
        if(sLastClickTime==null){
            sLastClickTime=new SparseLongArray();
        }
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if(currentTime-sLastClickTime.get(view.getId())>time){
            sLastClickTime.put(view.getId(),currentTime);
            return false;
        }
        return true;
    }
    public static void clearLastClickTime(){
        sLastClickTime=null;
    }
}
