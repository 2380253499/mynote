package com.zr.note.ui.main.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zr.note.ui.main.inter.AddDataInter;

/**
 * Created by Administrator on 2016/10/31.
 */
public class AddSpendDataBro extends BroadcastReceiver {
    private AddDataInter.AddDataFinish addDataInter;

    public AddSpendDataBro(AddDataInter.AddDataFinish addDataInter) {
        this.addDataInter= addDataInter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isAddData = intent.getBooleanExtra(BroFilter.isAddData, false);
        if(isAddData){
            addDataInter.addDataFinish();
        }

    }
}
