package com.mynote;

import com.github.androidtools.DateUtils;

import org.junit.Test;

import java.util.Date;

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
    public void asdf() throws Exception {
        String s="2018-02-07 17:48:51.";
        Date date = DateUtils.stringToDate(s,"yyy-MM-dd HH:mm:ss");
        System.out.println(date.getTime());
    }

}