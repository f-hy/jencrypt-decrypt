package com.friendy.homework;

import java.io.*;
import java.util.ArrayList;

/**
 * -*- coding: utf-8 -*-
 *
 * @author Friendy 付<p>
 * {@code @software} IntelliJ IDEA<p>
 * {@code @create} 2023/4/26 20:10 encrypt-decrypt wjny<p>
 * <p>
 * <br>
 */
public class wjny {
    private static final int MODULUS = 26;

    public static void main(String[] args) {
//        1）实现维吉尼亚密码算法，可以利用任意长度密钥对任意长度句子进行加解密；
//        e1();
//        2）通过Kasiski和Friedman测试方法实现密钥破解。
    }

    public static void e1() {
        String rawkey = "HHhhHHhshHHhdsgfLKk";
        String rawPath = ".cache/明文.txt";
        String resenPath = ".cache/res密文.txt";
        String resdePath = ".cache/res明文.txt";
        int[] key = parseKey(rawkey);
        String rawText = fileToString(rawPath);
        String cipher = cryptText(rawText, key, true);
        stringToFile(resenPath, cipher);
        String plain = cryptText(cipher, key, false);
        stringToFile(resdePath, plain);
    }

    public static void e2() {
        //
    }

    public static int[] parseKey(String key) {
        String lowerCase = key.toLowerCase();
        int[] integers = new int[key.length()];
        for (int i = 0; i < key.length(); ++i) {
            integers[i] = (int) lowerCase.charAt(i) - 'a';
        }
        return integers;
    }

    public static String cryptText(String plainText, int[] key, boolean en) {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for (char c : plainText.toCharArray()) {
            char out = c;
            if (c >= 'a' && c <= 'z') {
                int tmp;
                if (en) tmp = (c - (int) 'a' + key[i++]) % MODULUS;
                else {
                    tmp = (c - (int) 'a' - key[i++]) % MODULUS;
                    tmp = tmp >= 0 ? tmp : (tmp + MODULUS);
                }
                if (i == key.length) i = 0;
                out = (char) (tmp + (int) 'a');
            } else if (c >= 'A' && c <= 'Z') {
                int tmp;
                if (en) tmp = (c - (int) 'A' + key[i++]) % MODULUS;
                else {
                    tmp = (c - (int) 'A' - key[i++]) % MODULUS;
                    tmp = tmp >= 0 ? tmp : (tmp + MODULUS);
                }
                if (i == key.length) i = 0;
                out = (char) (tmp + (int) 'A');
            }
            stringBuilder.append(out);

        }
        return stringBuilder.toString();
    }

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
}
