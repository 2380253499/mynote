package com.zr.note.test;

import com.zr.note.tools.DateUtils;

import java.util.Date;

/**
 * Created by Administrator on 2017/6/12.
 */

public class Test {
    public static void main(String[] args) {

        int y = Integer.parseInt(DateUtils.dateToString(new Date(), "yyyy"));
        int m = Integer.parseInt(DateUtils.dateToString(new Date(), "MM"));
        int d = Integer.parseInt(DateUtils.dateToString(new Date(), "dd"));
        System.out.println(y);
        System.out.println(m);
        System.out.println(d);
    }
}
