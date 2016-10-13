package com.zr.note.ui.main.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/13.
 */
public class JokeBean  extends BaseBean implements Serializable{
    private String dataContent;

    public String getDataContent() {
        return dataContent;
    }

    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }
}
