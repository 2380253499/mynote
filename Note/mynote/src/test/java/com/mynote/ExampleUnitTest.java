package com.mynote;

import org.junit.Test;

import java.sql.Timestamp;
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
//        System.out.println(SystemClock.currentThreadTimeMillis());
        System.out.println(System.nanoTime());
        System.out.println(System.currentTimeMillis());
        System.out.println(new Timestamp(new Date().getTime())+"");
        System.out.println(new Date().getTime());
    }
    public class a{
        private long a;
        private String b;

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

        public long getA() {
            return a;
        }

        public void setA(long a) {
            this.a = a;
        }
    }
}