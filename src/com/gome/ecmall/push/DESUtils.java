package com.gome.ecmall.push;

import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DESUtils {
    /**
     * 密钥
     */
    public static final String DEFAULT_KEY = "sjz.hbbill.com";

    /**
     * 解密
     * 
     * @param message
     *            加密后的内容
     * @param key
     *            密钥
     * @return
     * @throws Exception
     */
    public static String decrypt(String message, String key) throws Exception {
        if (key.length() > 8) {
            key = key.substring(0, 8);
        }
        byte[] bytesrc = convertHexString(message);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));

        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

        byte[] retByte = cipher.doFinal(bytesrc);
        return new String(retByte);
    }

    /**
     * 加密
     * 
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String message, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

        return cipher.doFinal(message.getBytes("UTF-8"));
    }

    public static byte[] convertHexString(String ss) {
        byte digest[] = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }

        return digest;
    }

    /**
     * 加密，返回的是加密后的字符串
     * 
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt2(String message, String key) throws Exception {
        String jiami = java.net.URLEncoder.encode(message, "utf-8").toLowerCase(Locale.CHINA);
        String a = toHexString(encrypt(jiami, key)).toUpperCase(Locale.CHINA);
        return a;
    }

    /**
     * 解密，返回的是解密后的字符串
     * 
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt2(String message, String key) throws Exception {
        String result = java.net.URLDecoder.decode(decrypt(message, key), "utf-8");
        return result;
    }

    // public static void main(String[] args) throws Exception {
    // // String key = "QPZMG150";
    // // String jiami = "{\"你好呀#*_<>?:&^@%;aaa'aaaaaa----}";
    // //
    // // System.out.println("加密数据:" + jiami);
    // // String a = toHexString(encrypt(jiami, key)).toUpperCase();
    // // System.out.println("加密后的数据为:" + a);
    //
    // String b = decrypt("3E90DE32A0E05E95395B0250B878B9ECE629107D32BE9345C032A36874F203F8", "gomeshop");
    // System.out.println("解密后的数据:" + b);
    //
    // }

    public static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }

        return hexString.toString();
    }
}