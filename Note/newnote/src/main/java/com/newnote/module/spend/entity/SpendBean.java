package com.newnote.module.spend.entity;


import com.newnote.base.BaseEntity;

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
    private int localYear;
    private int localMonth;
    private int localDay;
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

    public int getLocalYear() {
        return localYear;
    }

    public void setLocalYear(int localYear) {
        this.localYear = localYear;
    }

    public int getLocalMonth() {
        return localMonth;
    }

    public void setLocalMonth(int localMonth) {
        this.localMonth = localMonth;
    }

    public int getLocalDay() {
        return localDay;
    }

    public void setLocalDay(int localDay) {
        this.localDay = localDay;
    }

    public void setLiveSpend(double liveSpend) {
        this.liveSpend = liveSpend;
    }
}
