package com.mkf_test.showtexts.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * dp、sp 转换为 px 的工具类
 * 
 * 
 *
 */ 
public class DisplayUtil { 
	/**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     * 
     * @param pxValue
     *            （DisplayMetrics类中属性density）
     * @return
     */ 
    public static int px2dip(Context context, float pxValue) { 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int) (pxValue / scale + 0.5f); 
    } 
   
    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     * 
     * @param dipValue
     *            （DisplayMetrics类中属性density）
     * @return
     */ 
    public static int dip2px(Context context, float dipValue) { 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int) (dipValue * scale + 0.5f); 
    } 
   
    /**
     * 将px值转换为sp值，保证文字大小不变
     * 
     * @param pxValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */ 
    public static int px2sp(Context context, float pxValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (pxValue / fontScale + 0.5f); 
    } 
   
    /**
     * 将sp值转换为px值，保证文字大小不变
     * 
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */ 
    public static int sp2px(Context context, float spValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale + 0.5f); 
    } 
/**
 * 获取屏幕的宽度
 * @param context
 * @return
 */
    public static int getScreenWidth(Context context) { 
    	return context.getResources().getDisplayMetrics().widthPixels; 
    } 
    
    /**
     * 获取屏幕的高度
     * @param context
     * @return
     */
    public static int getScreenHight(Context context) { 
    	return context.getResources().getDisplayMetrics().heightPixels; 
    } 
    /**
     * 判断外部存储卡是否可用
     * @return
     */
    public static boolean isExternalStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static final int getHeightInPx(Context context) {
        final int height = context.getResources().getDisplayMetrics().heightPixels;
        return height;
    }

    public static final int getWidthInPx(Context context) {
        final int width = context.getResources().getDisplayMetrics().widthPixels;
        return width;
    }

    public static final int getHeightInDp(Context context) {
        final float height = context.getResources().getDisplayMetrics().heightPixels;
        int heightInDp = px2dip(context, height);
        return heightInDp;
    }

    public static final int getWidthInDp(Context context) {
        final float width = context.getResources().getDisplayMetrics().widthPixels;
        int widthInDp = px2dip(context, width);
        return widthInDp;
    }


    /**
     * 根据string.xml资源格式化字符串
     * @param context
     * @param resource
     * @param args
     * @return
     */
    public static String formatResourceString(Context context, int resource, Object... args) {
        String str = context.getResources().getString(resource);
        if(TextUtils.isEmpty(str)) {
            return null;
        }
        return String.format(str, args);
    }

    /**
     * 获取拍照相片存储文件
     * @param context
     * @return
     */
    public static File createFile(Context context){
        File file;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            String timeStamp = String.valueOf(new Date().getTime());
            file = new File(Environment.getExternalStorageDirectory() +
                    File.separator +context.getPackageName()+      File.separator + timeStamp+".jpg");
        }else{
            File cacheDir = context.getCacheDir();
            String timeStamp = String.valueOf(new Date().getTime());
            file = new File(cacheDir+ File.separator +context.getPackageName(), timeStamp+".jpg");
        }
        return file;
    }

    /**
     * 获取磁盘缓存文件
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获取应用程序版本号
     * @param context
     * @return
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    
}
