package com.gome.ecmall.cache;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.webkit.URLUtil;

import com.gome.ecmall.cache.BytesArrayFactory.BytesArray;

public abstract class ImageLoadTask implements Serializable {

    /**
     * 封装图片加载操作的抽象类，用来封装图片加载和处理的信息和操作，以及回调
     */
    private static final long serialVersionUID = -8964356235074738747L;
    public String filePath;
    public String key;
    public static final int STATUS_READY = 0;
    public static final int STATUS_STARTED = 1;
    public static final int STATUS_FINISHED = 2;
    private int currentState = STATUS_READY;

    public ImageLoadTask(String filePath) {
        if (filePath == null || filePath.length() == 0) {
            throw new NullPointerException("params error!");
        }
        this.filePath = filePath;
        key = getCacheKey(filePath);
    }

    /**
     * 计算缓存的索引key,此key用来確定Cache的唯一性
     * 
     * @param filePath
     * @return
     */
    public static String getCacheKey(String filePath) {
        String keyPath = filePath;
        try {
            if (URLUtil.isHttpUrl(filePath)) {
                URL url = new URL(filePath);
                keyPath = url.getPath();// path部分不包含地址重写的参数，可通过url.getQuery()获得
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return getDigestCode(keyPath);
    }

    /**
     * 子类实现此方法，定义具体的图片加载操作
     * 
     */
    protected abstract Bitmap doInBackground();

    /**
     * 子类重载此方法来执行图片加载完之后的回调
     * 
     * @param task
     * @param bitmap
     */
    public void onImageLoaded(ImageLoadTask task, Bitmap bitmap) {

    };

    /**
     * 子类实现此方法，定义具体的bitmap转字节数组方式(png或jpg,是否压缩等) 下面提供了默认实现
     * 
     * @param bitmap
     * @return
     */
    protected BytesArray transBitmapToBytesArray(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        BytesArrayFactory factory = BytesArrayFactory.getDefaultInstance();
        int length = bitmap.getRowBytes() * bitmap.getHeight();
        BytesArray bytesArray = factory.requestBytesArray(length);
        if (bitmap.compress(bitmap.hasAlpha() ? CompressFormat.PNG : CompressFormat.JPEG, 100, bytesArray)) {
            return bytesArray;
        }
        factory.releaseBytesArray(bytesArray);
        return null;
    };

    public void performCallback(Bitmap bitmap) {
        onImageLoaded(this, bitmap);
        currentState = STATUS_FINISHED;
    }

    public void setStatus(int status) {
        currentState = status;
    }

    public int getStatus() {
        return currentState;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    // equals相等 hashcode必须相等
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ImageLoadTask)) {
            return false;
        }
        ImageLoadTask task = (ImageLoadTask) o;
        return this.key.equals(task.key);
    }

    public static String getDigestCode(String msg) {
        String digest = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(msg.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            digest = buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return digest;
    }

}
