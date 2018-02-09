package com.mynote.module.home.event;

/**
 * Created by Administrator on 2018/2/9.
 */

public class OptionEvent {
    //创建时间排序
    public final static int flag_0=0;
    //修改时间排序
    public final static int flag_1=1;
    //批量删除
    public final static int flag_prepare_delete=2;
    //取消批量删除
    public final static int flag_cancel_delete=3;
    //退出批量删除
    public final static int flag_exit_delete=4;
    //进行批量删除
    public final static int flag_start_delete=5;
    public int flag;
    public int index;
    public OptionEvent(int flag,int index) {
        this.flag = flag;
        this.index = index;
    }
}