package com.zr.note.ui.main.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/13.
 */
public class Bean implements Serializable {
    private int _id;
    private Date updateTime;
    private Date creatTime;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }
}
