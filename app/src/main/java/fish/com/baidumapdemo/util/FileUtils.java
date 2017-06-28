package fish.com.baidumapdemo.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Created by neil on 2017/5/28 0028.
 */

public class FileUtils {


    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    public static File createTmpFile(Context context) throws IOException {
        File dir = null;
        if(TextUtils.equals(Environment.getExternalStorageState(), MEDIA_MOUNTED)) {
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            if (!dir.exists()) {
                dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/Camera");
                if (!dir.exists()) {
                    dir = getCacheDirectory(context, true);
                }
            }
        }else{
            dir = getCacheDirectory(context, true);
        }
        return File.createTempFile(JPEG_FILE_PREFIX, JPEG_FILE_SUFFIX, dir);
    }


    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";


    public static File getCacheDirectory(Context context) {
        return getCacheDirectory(context, true);
    }


    public static File getCacheDirectory(Context context, boolean preferExternal) {
        File appCacheDir = null;
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) { // (sh)it happens (Issue #660)
            externalStorageState = "";
        } catch (IncompatibleClassChangeError e) { // (sh)it happens too (Issue #989)
            externalStorageState = "";
        }
        if (preferExternal && MEDIA_MOUNTED.equals(externalStorageState) && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }


    public static File getIndividualCacheDirectory(Context context, String cacheDir) {
        File appCacheDir = getCacheDirectory(context);
        File individualCacheDir = new File(appCacheDir, cacheDir);
        if (!individualCacheDir.exists()) {
            if (!individualCacheDir.mkdir()) {
                individualCacheDir = appCacheDir;
            }
        }
        return individualCacheDir;
    }

    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(
                Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null;
            }
            try {
                new File(appCacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
            }
        }
        return appCacheDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }




    /**
     *    ----
     * @param fromPath 被复制的文件路径
     * @param toPath 复制的目录文件路径
     * @param rewrite 是否重新创建文件
     *
     * <p>文件的复制操作方法
     */
    public static void copyfile(String fromPath, String toPath, Boolean rewrite ){

        File fromFile = new File(fromPath);
        File toFile = new File(toPath);

        if(!fromFile.exists()){
            return;
        }
        if(!fromFile.isFile()){
            return;
        }
        if(!fromFile.canRead()){
            return;
        }
        if(!toFile.getParentFile().exists()){
            toFile.getParentFile().mkdirs();
        }
        if(toFile.exists() && rewrite){
            toFile.delete();
        }

        try {
            FileInputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile);

            byte[] bt = new byte[1024];
            int c;
            while((c=fosfrom.read(bt)) > 0){
                fosto.write(bt,0,c);
            }
            //关闭输入、输出流
            fosfrom.close();
            fosto.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
