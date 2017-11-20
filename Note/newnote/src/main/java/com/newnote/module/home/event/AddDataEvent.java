package com.newnote.module.home.event;

/**
 * Created by Administrator on 2017/11/20.
 */

public class AddDataEvent {
    public int index;
    public boolean isAddData;
    public AddDataEvent(int index,boolean isAddData) {
        this.index = index;
        this.isAddData = isAddData;
    }

}
