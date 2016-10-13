package com.zr.note.ui.main.entity;

import com.zr.note.base.BaseBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/13.
 */
public class SpendBean  extends BaseBean implements Serializable{
    /*liveSpend
dataRemark*/
    private String dataRemark;
    private double liveSpend;

    public String getDataRemark() {
        return dataRemark;
    }

    public void setDataRemark(String dataRemark) {
        this.dataRemark = dataRemark;
    }

    public double getLiveSpend() {
        return liveSpend;
    }

    public void setLiveSpend(double liveSpend) {
        this.liveSpend = liveSpend;
    }
}
