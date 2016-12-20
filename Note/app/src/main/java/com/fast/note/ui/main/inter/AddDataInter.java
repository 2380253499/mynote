package com.fast.note.ui.main.inter;

/**
 * Created by Administrator on 2016/10/10.
 */
public interface AddDataInter {
    boolean saveData();
    void clearData();
    interface AddDataFinish{
        void addDataFinish();
    }
}
