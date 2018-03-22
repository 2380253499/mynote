package com.mynote;

import com.github.androidtools.DateUtils;
import com.library.base.tools.has.Lunar;
import com.library.base.tools.has.LunarSolar;
import com.library.base.tools.has.Solar;

import org.junit.Test;

import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void asdfsa() throws Exception {
        //2018-02-07 17:48:51.
        Date date = DateUtils.stringToDate("2018-02-07 17:48:51.","yyy-MM-dd HH:mm:ss");
        System.out.println(date.getTime());
    }
    @Test
    public void yy() throws Exception {
        LunarSolar lunarSolar=new LunarSolar();
        Calendar a = Calendar.getInstance();
        Calendar b = Calendar.getInstance();
        a.set(2015, 3, 31);
        b.set(2015, 3, 29);
        long diffDays = (b.getTimeInMillis() - a.getTimeInMillis()) / (1000 * 60 * 60 * 24);
        Calendar aa = Calendar.getInstance();
        Calendar bb = Calendar.getInstance();
        a.set(2015, 2, 31);
        b.set(2015, 3, 1);
        System.out.println(distanceDay(aa,bb));
    }
    public long distanceDay(Calendar before,Calendar after){
        long diffDays = (after.getTimeInMillis() - before.getTimeInMillis()) / (1000 * 60 * 60 * 24);
        return diffDays;
    }
    @Test
    public void sadf() throws Exception {
/*        Solar solar = new Solar();
        solar.solarYear = 2001;
        solar.solarMonth = 5;
        solar.solarDay = 23;
        System.out.println("solar:"+solar.toString());


        LunarSolar lunarSolar = new LunarSolar();
        Lunar lunar = lunarSolar.SolarToLunar(solar);
        System.out.println(lunar.isleap+"lunar:"+lunar.toString());


        lunar.lunarYear = lunar.lunarYear+1;*/
//        lunar.isleap = false;
        Lunar lunar2=new Lunar();
        lunar2.lunarYear=2001;
        lunar2.lunarMonth=4;
        lunar2.lunarDay=1;
        LunarSolar lunarSolar = new LunarSolar();
        Solar new_solar = lunarSolar.LunarToSolar(lunar2);
        System.out.println("new_lunar:"+lunar2.toString());
        System.out.println("new_solar:"+new_solar.toString());
    }
    @Test
    public void asdf() throws Exception {
        String s="2018-02-07 17:48:51.";
        Date date = DateUtils.stringToDate(s,"yyy-MM-dd HH:mm:ss");
        System.out.println(date.getTime());

        System.out.println(genNonceStr());
    }
    private String genNonceStr() {
        Random random = new Random();
        return getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    public static final String getMessageDigest(byte[] buffer) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest e = MessageDigest.getInstance("MD5");
            e.update(buffer);
            byte[] md = e.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;

            for(int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }
            return new String(str);
        } catch (Exception var9) {
            return null;
        }
    }
}