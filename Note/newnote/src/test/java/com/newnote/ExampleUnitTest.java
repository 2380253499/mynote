package com.newnote;

import com.github.androidtools.AES;
import com.github.androidtools.DateUtils;

import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void getPWD() throws Exception {
        A a = new A();
        A a1 = a.getA();
        System.out.println(a1.b);
        superPWD();
    }

    public abstract class  A<T extends A >{
        public String b;
        public A a;

        public A( ) {
        }

        public   T getA(){
/*//            ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
            Class<T> clazz = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//            Class<T> clazz =      (Class<T>) pt.getActualTypeArguments()[0];
            T  mPresenter= null;
            try {
                mPresenter = clazz.newInstance();
                mPresenter.b="aa";
            } catch ( Exception e) {
                e.printStackTrace();
            }*/
            return GenericsUtils.getSuperClassGenricType(this, 0);;
        }
    }
    public void superPWD(){
        String time = DateUtils.dateToString(new Date(), "yyyyMMdd");
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String strHour=hour+"";
        if(hour<10){
            strHour="0"+hour;
        }
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        String strMinute="00";
        if(minute>55){
            strMinute="55";
        }else if(minute>50){
            strMinute="50";
        }else if(minute>45){
            strMinute="45";
        }else if(minute>40){
            strMinute="40";
        }else if(minute>35){
            strMinute="35";
        }else if(minute>30){
            strMinute="30";
        }else if(minute>25){
            strMinute="25";
        }else if(minute>20){
            strMinute="20";
        }else if(minute>15){
            strMinute="15";
        }else if(minute>10){
            strMinute="10";
        }else if(minute>5){
            strMinute="05";
        }else if(minute>=0){
            strMinute="00";
        }
        strMinute=time+""+strHour+""+strMinute+"note";
        System.out.println(strMinute);
        String encode = AES.encode(strMinute);
//					et_super_pwd.setText(encode.substring(0,10));
        System.out.println(encode.substring(0,10));
    }
}