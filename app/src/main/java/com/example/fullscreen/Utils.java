package com.example.fullscreen;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Pair;

import java.io.File;
import java.io.FileOutputStream;

public class Utils {

    private final static int SCREEN_SHOT_OFFET_TIME_S = 60*60;

    public static Bitmap newBitmap(Bitmap mapgolden, Bitmap mapqr) {
        Bitmap retBmp;
        retBmp = Bitmap.createBitmap(mapgolden.getWidth(), mapgolden.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(retBmp);
        mapqr = resizeBitmap(mapqr,695, 695);
        canvas.drawBitmap(mapgolden, 0, 0, null);
        canvas.drawBitmap(mapqr, 43, 40, null);
        return retBmp;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        float scaleWidth = ((float) newWidth) / bitmap.getWidth();
        float scaleHeight = ((float) newHeight) / bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bmpScale = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bmpScale;
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return null;
    }

    public static Pair<Long, String> getLatestMediaPhoto(Context context) {
        //查询路径和修改时间
        String[] projection = {MediaStore.Images.Media.DATA,MediaStore.Images.Media.DATE_MODIFIED};
        long currentTime = System.currentTimeMillis()/ 1000 - SCREEN_SHOT_OFFET_TIME_S;
        //检查camera文件夹，查询并排序
        Pair<Long, String> mediaPair = null;

        Cursor cursor = MediaStore.Images.Media.query(context.getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                MediaStore.Images.Media.DATE_ADDED + " >= ?",
                new String[]{currentTime + ""},
                MediaStore.Images.Media.DATE_ADDED + " DESC");
        if (cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            String path= cursor.getString(index);
            File file = new File(path);
            if (path != null && file.exists()){
                index = cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED);
                mediaPair = new Pair(cursor.getLong(index),path);
            }
        }
        //对比
        if (mediaPair != null ) {
            return mediaPair;
        }
        return null;
    }

    public static Bitmap cropPic(Bitmap bitmap, Rect src){
        Bitmap retBmp = Bitmap.createBitmap(src.width(), src.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(retBmp);
        Rect dist = new Rect(0,0, src.width(), src.height());
        canvas.drawBitmap(bitmap, src, dist, null);
        retBmp = resizeBitmap(retBmp,695, 695);
        return retBmp;
    }

    public static void saveBitmap(int num, Bitmap bitmap){
        try {
            File sdCardRoot = Environment.getExternalStorageDirectory();
            File mydir = new File(sdCardRoot, "myqr");
            if(!mydir.exists()) mydir.mkdir();
            String name = "qr" + num + ".jpg";
            File file = new File(mydir, name);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
