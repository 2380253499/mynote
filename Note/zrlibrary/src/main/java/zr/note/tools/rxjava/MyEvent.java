package zr.note.tools.rxjava;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Administrator on 2016/11/18.
 */
public class MyEvent extends Events {
    //所有事件的CODE
    public static final int TAP = 1; //点击事件
    public static final int OTHER = 21; //其他事件

    @Retention(RetentionPolicy.SOURCE)
    public @interface EventCode {}
}
