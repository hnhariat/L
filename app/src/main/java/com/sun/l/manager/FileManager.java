package com.sun.l.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.sun.l.LConst;
import com.sun.l.utils.LBitmapCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by sunje on 2016-03-04.
 */
public class FileManager {
    public static final String ROOT = "/L";
    public static String FILE_BACKGROUND = "background.l";

    public static void initialize() {
        File file = new File(Environment.getExternalStorageDirectory() + ROOT);
        if (!file.exists()) {
            boolean result = file.mkdir();
            Log.d("L.file", "@make " + file.getAbsolutePath() + " : " + result);
        }
    }

    public static String getRootPath() {
        return Environment.getExternalStorageDirectory() + ROOT;
    }

    public static boolean copyTo(String filePath, String targetPath) {
        boolean result;
        File file = new File(filePath);
        if (file != null && file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                FileOutputStream newfos = new FileOutputStream(targetPath);
                int readcount = 0;
                byte[] buffer = new byte[1024];
                while ((readcount = fis.read(buffer, 0, 1024)) != -1) {
                    newfos.write(buffer, 0, readcount);
                }
                newfos.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            result = true;
        } else {
            result = false;
        }
        Log.d("L.file" , "@copy from : " + filePath + " to : " + targetPath + " " + result);
        return result;
    }

    public static void saveBitmapCache(Context context, String filePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        toCache(context, bitmap);
    }

    public static void toCache(Context context, Bitmap bitmap) {
        LBitmapCache.getInstance(context).put(LConst.Key.background, bitmap);
    }
}
