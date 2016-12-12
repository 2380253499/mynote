package com.zr.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/12/12.
 */
public class FileUtils {
    public static void copyFileForAssets(Context context,String fileName,File filePath) {
        AssetManager assets = context.getAssets();
        InputStream is = null;
        try {
            is = assets.open(fileName);
            int len = 0;
            byte buffer[] = new byte[1024];
            FileOutputStream fos = new FileOutputStream(filePath);
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
