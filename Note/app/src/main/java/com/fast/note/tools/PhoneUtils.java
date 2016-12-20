package com.fast.note.tools;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;

/**
 * Created by Administrator on 2016/9/6.
 */
public class PhoneUtils {
    public static int getPhoneWidth(Context context) {
        Point point = new Point();
        ((Activity)context).getWindowManager().getDefaultDisplay().getSize(point);
        return point.x;
    }
    public static int getPhoneHeight(Context context) {
        Point point = new Point();
        ((Activity)context).getWindowManager().getDefaultDisplay().getSize(point);
        return point.y;
    }
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    public static void copyText(Context context,String text){
        ClipboardManager cm = (ClipboardManager)(context).getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setPrimaryClip(ClipData.newPlainText(null,text));
    }
    public static String pasteText(Context context){
        ClipboardManager cm = (ClipboardManager)(context).getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm.hasPrimaryClip()){
            return cm.getPrimaryClip().getItemAt(0).getText().toString();
        }
        return null;
    }

    /**
     * 跳转到手机系统权限管理界面
     * @param context
     */
    public static void PermissionsSetting(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }
}
