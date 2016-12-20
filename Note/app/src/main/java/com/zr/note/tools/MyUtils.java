package com.zr.note.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/4/13.
 */
public  class MyUtils {
    public static List<Activity> actList;
    public static List<Activity> allActList;
    public static final String INTO_FROM_LIST="intoForList";
    public static final String ROB_GONG_FANG="gongFang";
    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }
    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }
    public static void addActivityToList(Activity activity){
        /*if(actList==null){
            actList=new ArrayList<Activity>();
        }
        actList.add(activity);*/
    }
    public static void removeActivityFromList(){
       /* if(actList!=null){
            Iterator it = actList.iterator();
            while (it.hasNext()) {
                ((Activity)it.next()).finish();
            }
            *//*for (int i = 0; i <actList.size() ; i++) {
                actList.get(i).finish();
            }*//*
            actList.clear();
        }*/
    }

    public static void addActivityToAllList(Activity activity){
        /*if(allActList==null){
            allActList=new ArrayList<Activity>();
        }
        allActList.add(activity);*/
    }
    public static void removeActivityFromAllList(){
       /* if(allActList!=null){
            Iterator it = allActList.iterator();
            while (it.hasNext()) {
                ((Activity)it.next()).finish();
            }
            allActList.clear();
            if(actList!=null){
                actList.clear();
            }
        }*/
    }

    public static int compareDate(Date date,Date date2){
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(date);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(date2);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return Math.abs(day2 - day1);
    }
    public static int compareNowDate(Date date){
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(date);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(new Date());
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return Math.abs(day2 - day1);
    }
    public static int compareNowDate(String date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar aCalendar = Calendar.getInstance();
        try {
            aCalendar.setTime(sdf.parse(date));
            int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
            aCalendar.setTime(new Date());
            int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
            return Math.abs(day2 - day1);
        } catch (ParseException e) {
            e.printStackTrace();
            return 99;
        }
    }
    public static String dateFormat(String date,String formatString){
        SimpleDateFormat sdf1=new SimpleDateFormat("yyy-MM-dd");
        SimpleDateFormat sdf2=new SimpleDateFormat(formatString);
        try {
            String format=sdf2.format(sdf1.parse(date));
            return format;
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }
    public static Date stringToDate(String strDate)
    {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }

    }
    public static String dateFormat(String date){
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        try {
            String format=sdf.format(sdf.parse(date));
            return format;
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }
    public static String getFormatDate(Date date){
        return getFormatDate(date,"yyyy-MM-dd");
    }
    public static String getFormatDate(Date date,String formatStr){
        SimpleDateFormat sdf=new SimpleDateFormat(formatStr);
        try {
            String format=sdf.format(date);
            return format;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String getFormatDate(long lDate){
        return getFormatDate(lDate,"yyyy-MM-dd");
    }
    public static String getFormatDate(long lDate,String formatStr){
        SimpleDateFormat sdf=new SimpleDateFormat(formatStr);
        try {
            String format=sdf.format(new Date(lDate));
            return format;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static int getHeight(Context context){
        WindowManager wm = ((Activity)context).getWindowManager();
        return wm.getDefaultDisplay().getHeight();
    }
    public static int getWidth(Context context){
        WindowManager wm = ((Activity)context).getWindowManager();
        return wm.getDefaultDisplay().getWidth();
    }
    public static double division(double d1, double d2,int len) {// 进行除法运算
        if (d2 != 0){
            BigDecimal b1 = new BigDecimal(d1);
            BigDecimal b2 = new BigDecimal(d2);
            return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
        }else{
            return 0;
        }

    }
    public static double division(String d1,String d2,int len) {// 进行除法运算
        double dl1= Double.parseDouble(d1);
        double dl2= Double.parseDouble(d2);
        return division(dl1, dl2, 2);
    }
    public static double division(String d1,String d2) {// 进行除法运算
        return division(d1, d2, 2);
    }
    public static void LogI(String d1,String d2) {
        Log.i("request=====log-key[" + d1, "]########[" + d2 + "]");
    }
    public static void LogI(String d1) {
        Log.i("request", "json=====" + d1 + "");
    }
    public static void showToast(Context context,String msg){
        if(context!=null){
            if(msg!=null&&msg.length()>0){
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }
    };
    /************************** 压缩图片到300*400 ****************************/
    public static String replaceImgUrl(String imgUrl){
        return replaceImgUrl(imgUrl,300,400);
    };
    public static String replaceImgUrl(String imgUrl,int width,int height){
        String newUrl;
        if(imgUrl!=null&&imgUrl.trim().length()>0){
            if(imgUrl.indexOf(".jpg")>=0){
                imgUrl=imgUrl.replace(".jpg","/"+width+"_"+height+"_f.jpg");
                return imgUrl;
            }else{
                return imgUrl;
            }
        }
        return imgUrl;
    };
    public static void setListHeight(Adapter mSearch, ListView mListView) {
        int totalHeight = 0;
        for (int i = 0, len = mSearch.getCount(); i < len; i++) {
            View listItem = mSearch.getView(i, null, mListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = totalHeight + (mListView.getDividerHeight() * (mSearch.getCount() - 1));
        mListView.setLayoutParams(params);
    }


    /**
     * 获取图片的旋转角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
            return degree;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 旋转角度
     * @param img
     * @param degree
     * @return
     */
    public static Bitmap toTurn(Bitmap img, int degree){
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        int width = img.getWidth();
        int height =img.getHeight();
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        return img;
    }
    /**
     * 判断文件大小
     */
    public static long getFileSize(String path){
        File file=new File(path);
        long length = file.length();
        return length;
    }
    public static boolean imageIsDamage(String imgPath){//检查图片大小，是否正常
        long fileSize = MyUtils.getFileSize(imgPath);
        if(fileSize<(2*1024)){
            return true;
        }
        return false;
    }
    public static String getSaveImgPath(){
        return Environment.getExternalStorageDirectory().getPath() + "/vocinno/compression";
    }

    public static void deleteCompression(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                recursionDeleteFile(new File(getSaveImgPath()));
            }
        }).start();
    }
    /**
     * 删除文件
     * @param file
     */
    public static void recursionDeleteFile(File file){
        if(file.isFile()){
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                recursionDeleteFile(f);
            }
            file.delete();
        }
    }


    /**
     * 判断文件是否存在
     * @param path
     * @return
     */
    public static boolean isExist(String path){
        try {
            File file=new File(path);
            return file.exists();
        }catch (Exception e){
            return false;
        }
    }
    /**
     * 时间秒格式化  01:10
     * @param length
     * @return
     */
    public static String getRecordLength(int length){
        String divisionStr="";
        String modulusStr="";
        int division=length/60;
        int modulus=length%60;
        if(division<=9){
            divisionStr="0"+division;
        }else{
            divisionStr=division+"";
        }
        if(modulus<=9){
            modulusStr="0"+modulus;
        }else{
            modulusStr=""+modulus;
        }
        return divisionStr+":"+modulusStr;
    }
}
