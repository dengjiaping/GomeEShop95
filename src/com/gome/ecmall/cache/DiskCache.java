package com.gome.ecmall.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;

import com.gome.ecmall.cache.BytesArrayFactory.BytesArray;
import com.gome.ecmall.util.VersionUpdateUtils;

public class DiskCache {

    private static File cacheFolder;
    public static final String TAG = "DiskCache";

    @SuppressLint("NewApi")
    public static void initDiskCache(Context context) {
        int androidVerson = android.os.Build.VERSION.SDK_INT;
        if (androidVerson > 7 && context.getExternalCacheDir() != null) {
            cacheFolder = context.getExternalCacheDir();
        } else {
            cacheFolder = new File(VersionUpdateUtils.cacheDir);
        }
    }

    public static boolean clearCache() {
        if (cacheFolder == null) {
            return false;
        }
        boolean isSuccess = false;
        try {
            File[] files = cacheFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            isSuccess = true;
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    public static boolean writeDiskCache(String key, BytesArray bytesArray) {
        if (cacheFolder == null && key == null || bytesArray == null) {
            return false;
        }
        boolean isWrited = false;
        FileOutputStream fos = null;
        File cacheFile = null;
        try {
            cacheFile = new File(cacheFolder, key);
            if (cacheFile.exists()) {
                cacheFile.delete();
            }
            fos = new FileOutputStream(cacheFile);
            fos.write(bytesArray.getData(), bytesArray.offset(), bytesArray.size());
            isWrited = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }// end if
             // BDebug.d(TAG, "writeDiskCache:" + cacheFile.getAbsolutePath()
             // + "  isWrited:" + isWrited);
        }// end finally
        return isWrited;
    }

    public static BytesArray readCache(String name) {
        if (cacheFolder == null || name == null) {
            return null;
        }
        File cacheFile = new File(cacheFolder, name);
        if (!cacheFile.exists()) {
            return null;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(cacheFile);
            BytesArrayFactory factory = BytesArrayFactory.getDefaultInstance();
            int length = (int) cacheFile.length();
            BytesArray bytesArray = factory.requestBytesArray(length);
            if (bytesArray.readInputStream(fis)) {
                return bytesArray;
            } else {
                factory.releaseBytesArray(bytesArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // BDebug.d(TAG, "readCache:" + cacheFile.getAbsolutePath() +
            // "   hasRead:" + hasData);
        }
        return null;
    }

    public File getAbsFile() {
        return cacheFolder;
    }
}
