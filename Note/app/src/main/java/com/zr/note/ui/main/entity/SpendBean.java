package com.zr.note.ui.main.entity;

import com.zr.note.base.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/10/13.
 */
public class SpendBean  extends BaseEntity implements Serializable{
    /*liveSpend
dataRemark*/
    private String dataRemark;
    private double liveSpend;
    private String localYear;
    private String localMonth;
    private String localDay;
    private List<SpendBean> list;
    private double totalSpend;

    public List<SpendBean> getList() {
        return list;
    }

    public void setList(List<SpendBean> list) {
        this.list = list;
    }

    public double getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(double totalSpend) {
        this.totalSpend = totalSpend;
    }

    public String getDataRemark() {
        return dataRemark;
    }

    public void setDataRemark(String dataRemark) {
        this.dataRemark = dataRemark;
    }

    public double getLiveSpend() {
        return liveSpend;
    }

    public String getLocalYear() {
        return localYear;
    }

    public void setLocalYear(String localYear) {
        this.localYear = localYear;
    }

    public String getLocalMonth() {
        return localMonth;
    }

    public void setLocalMonth(String localMonth) {
        this.localMonth = localMonth;
    }

    public String getLocalDay() {
        return localDay;
    }

    public void setLocalDay(String localDay) {
        this.localDay = localDay;
    }

    public void setLiveSpend(double liveSpend) {
        this.liveSpend = liveSpend;
    }
}
