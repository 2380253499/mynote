package com.mynote.module.leftmenu.bean;

import com.library.base.BaseObj;

/**
 * Created by Administrator on 2018/3/8.
 */

public class BirthdayBean extends BaseObj {
    private String name;
    private long birthday;
    private boolean isTiXing;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public boolean isTiXing() {
        return isTiXing;
    }

    public void setTiXing(boolean tiXing) {
        isTiXing = tiXing;
    }
}
