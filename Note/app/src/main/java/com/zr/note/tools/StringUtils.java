package com.zr.note.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/10/19.
 */
public class StringUtils {
    /**
     * 列表有100条数据，第一条数据就显示 001
     * @param dataCount 数据总数
     * @param position  当前数据下标
     * @return
     */
    public static String getStringLength(int dataCount, int position) {
        int countLength = String.valueOf(dataCount).length();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < countLength - String.valueOf(position + 1).length(); i++) {
            stringBuffer.append("0");
        }
        return stringBuffer.toString();
    }
    public static String string2Unicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }
    public static String unicode2String(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            string.append((char) data);
        }
        return string.toString();
    }
    public static void main(String[] a) {
        System.out.println(string2Unicode("123"));
        System.out.println(unicode2String("\\u31\\u32\\u33"));
        System.out.println(stringToUnicode("123"));
        System.out.println(unicodeToString("\\u0031\\u0032\\u0033"));

    }




    private static final Pattern REG_UNICODE = Pattern.compile("[0-9A-Fa-f]{4}");
    private static final Pattern EN_CODE = Pattern.compile("[A-Za-z]{4}");
    public static String unicodeToString(String str) {
        StringBuilder sb = new StringBuilder();
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char c1 = str.charAt(i);
            if (c1 == '\\' && i < len - 1) {
                char c2 = str.charAt(++i);
                if (c2 == 'u' && i <= len - 5) {
                    String tmp = str.substring(i + 1, i + 5);
                    Matcher matcher = REG_UNICODE.matcher(tmp);
                    if (matcher.find()) {
                        sb.append((char) Integer.parseInt(tmp, 16));
                        i = i + 4;
                    } else {
                        sb.append(c1).append(c2);
                    }
                } else {
                    sb.append(c1).append(c2);
                }
            } else {
                sb.append(c1);
            }
        }
        return sb.toString();
    }

    /**
     * Convert the whole String object.
     *
     * @param str
     * @return
     */
    public static String stringToUnicode(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            String tmpStr = Integer.toHexString(str.charAt(i));
            if (tmpStr.length() < 4) {
                sb.append("\\u00");
            } else {
                sb.append("\\u");
            }
            sb.append(tmpStr);
        }
        return sb.toString();
    }

    /**
     * Just convert Chinese String
     *
     * @param str
     * @return
     */
    public static String chinese2Unicode(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            String tmpStr = Integer.toHexString(str.charAt(i));

            if (tmpStr.length() < 4) {
                sb.append(str.charAt(i));
            } else {
                sb.append("\\u");
                sb.append(tmpStr);
            }
        }
        return sb.toString();
    }

}
