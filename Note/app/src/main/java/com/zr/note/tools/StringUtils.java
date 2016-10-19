package com.zr.note.tools;

/**
 * Created by Administrator on 2016/10/19.
 */
public class StringUtils {
    public static String getStringLength(int dataCount,int position){
        int countLength = String.valueOf(dataCount).length();
        StringBuffer stringBuffer=new StringBuffer();
        for (int i = 0; i < countLength-String.valueOf(position+1).length(); i++) {
            stringBuffer.append("0");
        }
        return stringBuffer.toString();
    }
}
