package com.fast.note;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/15.
 */
public class testa {
    public static void main(String[]a){
        System.out.println(new Timestamp(new Date().getTime()));
        DecimalFormat df=new DecimalFormat(".##");
        df.setRoundingMode(RoundingMode.HALF_EVEN);
        System.out.println(df.format(23.335));

        DecimalFormat formater = new DecimalFormat("#0");
        formater.setRoundingMode(RoundingMode.FLOOR);
        System.out.println(formater.format(16.7897456));
    }
}
