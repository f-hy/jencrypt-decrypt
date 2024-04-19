package com.friendy.Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * -*- coding: utf-8 -*-
 *
 * @author Friendy 付<p>
 * {@code @software} IntelliJ IDEA<p>
 * {@code @create} 2022/9/4 13:23 encrypt-decrypt EDUtil<p>
 * <p>
 * <br>
 */
public class EDUtil {
    //https://baike.baidu.com/item/%E5%AD%97%E6%AF%8D%E9%A2%91%E7%8E%87/9669044
    final static char[] FREQUENT_CHAR_LIST = {
            'e', 't', 'a', 'o', 'n',
            'r', 'i', 's', 'h', 'd',
            'l', 'f', 'c', 'm', 'u',
            'g', 'y', 'p', 'w', 'b',
            'v', 'k', 'j', 'x', 'q',
            'z'};
    final static String[] FREQUENT_CHAR_PAIR_LIST = {
            "th", "he", "an", "re", "er",
            "in", "on", "at", "nd", "st",
            "es", "en", "of", "te", "ed",
            "or", "ti", "hi", "as", "to"};
    final static String[] FREQ_REPEATED_CHAR_PAIR_LIST = {
            "ll", "ee", "ss", "oo", "tt",
            "ff", "rr", "nn", "pp", "cc"};

    /**
     * @param path
     * @return String
     */
    public static String fileToString(String path) {
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer;
        try {
            bufferedReader = new BufferedReader(new FileReader(new File(path)));
            int buffer = 1024;
            char[] buf = new char[buffer];
            int len;
            stringBuffer = new StringBuffer();
            while ((len = bufferedReader.read(buf)) != -1) {
                stringBuffer.append(buf, 0, len);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return stringBuffer.toString();
    }

    public static void stringToFile(String path, String data) {
        stringToFile(new File(path), data);
    }

    /**
     * @param path
     * @param data
     */
    public static void stringToFile(File path, String data) {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(path));
            bufferedWriter.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * @param inputStream
     * @return String
     */
    public static String inputStreamToString(InputStream inputStream) {
        BufferedInputStream bufferedInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(inputStream);
            int len;
            int buffer = 1024;
            byte[] buf = new byte[buffer];
            byteArrayOutputStream = new ByteArrayOutputStream();
            while ((len = bufferedInputStream.read(buf)) != -1) {
                byteArrayOutputStream.write(buf, 0, len);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            return byteArrayOutputStream.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 凯撒解密
     *
     * @param cipher 密文
     * @param shift  移位
     * @return clear 明文
     */
    public static String decryptKaiser(String cipher, int shift) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : cipher.toCharArray()) {
            stringBuilder.append((char) ((int) c - shift));
        }
        return stringBuilder.toString();
    }

    /**
     * 凯撒加密
     *
     * @param clear 明文
     * @return cipher 密文
     */
    public static String encryptKaiser(String clear) {
        return encryptKaiser(clear, 3);
    }

    /**
     * 凯撒加密
     *
     * @param clear 明文
     * @param shift 移位
     * @return cipher 密文
     */
    public static String encryptKaiser(String clear, int shift) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : clear.toCharArray()) {
            stringBuilder.append((char) ((int) c + shift));
        }
        return stringBuilder.toString();
    }

    /**
     * transformation: DES
     * algorithm: DES
     *
     * @param clear
     * @param shift des加密必须是8位
     * @return
     */
    public static byte[] encryptDES(String clear, String shift) {
        String transformation = "DES";// transformation:参数表示使用什么类型加密
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            String algorithm = "DES";
            // 指定秘钥规则
            // 第一个参数表示：密钥，key的字节数组;
            // 第二个参数表示：算法
            SecretKeySpec secretKeySpec = new SecretKeySpec(shift.getBytes(), algorithm);
            // 第一个参数：表示模式，有加密模式和解密模式
            // 第二个参数：表示秘钥规则
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return cipher.doFinal(clear.getBytes());// 进行加密
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    transformation: DES/CBC/NoPadding
    在CBC中还要在加密解密中都写好IV向量IvParameterSpec iv = new IvParameterSpec(key.getBytes());//必须是8个字节
     */

    /**
     * DES解密
     *
     * @param encryptDES     密文
     * @param shift          密钥
     * @param transformation 加密算法
     * @param algorithm      加密类型
     * @return
     */
    public static byte[] decryptDES(byte[] encryptDES, String shift, String transformation, String algorithm) {
        // String transformation = "DES/CBC/PKCS5Padding";
        //String transformation = "DES/CBC/NoPadding";//NoPadding 这种填充模式 原文必须是8个字节的整倍数
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            SecretKeySpec secretKeySpec = new SecretKeySpec(shift.getBytes(), algorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return cipher.doFinal(encryptDES);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param clear          原文
     * @param shift          密钥(DES,密钥的长度必须是8个字节)
     * @param transformation 获取Cipher对象的算法
     * @param algorithm      获取密钥的算法
     * @return
     */
    public static byte[] encryptDES(byte[] clear, String shift, String transformation, String algorithm) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            SecretKeySpec secretKeySpec = new SecretKeySpec(shift.getBytes(), algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return cipher.doFinal(clear);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 欧几里得算法求最大公约数
     * getGreatestCommonDivisor
     *
     * @param a
     * @param b
     * @return
     */
    public static int gcd(int a, int b) {
        int c = a % b;
        while (c != 0) {
            a = b;
            b = c;
            c = a % b;
        }
        return b;
    }

    /**
     * 求逆元
     * @param verse
     * @param modulus
     * @return
     */
    public static int getInverse(int verse, int modulus) {
        if (gcd(verse, modulus) != 1) {
            return -1;
        }
        int inverse = 2;
        while ((inverse * verse) % modulus != 1) {
            ++inverse;
        }
        return inverse;
    }
}
