package com.zr.note.tools;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by Administrator on 2016/10/12.
 */
public class EditTextUtils {
    private static final int DECIMAL_DIGITS = 1;

    public static InputFilter getInputFilter(){
        return getInputFilter(DECIMAL_DIGITS);
    }
    public static InputFilter getInputFilter(final int length){
        return new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,Spanned dest, int dstart, int dend) {
                // 删除等特殊字符，直接返回
                if ("".equals(source.toString().trim())) {
                    return null;
                }
                int index=dest.toString().indexOf(".");
                String dValue = dest+"".toString();
                String[] splitStr = dValue.split("\\.");
                if (splitStr.length > 1&&dend>index) {
                    String dotValue = splitStr[1];
                    int diff = dotValue.length()  - length;
                    if (diff >= 0) {
                        return "";
                    }
                }
                return null;
            }
        };
    }
}
