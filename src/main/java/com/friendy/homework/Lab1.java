package com.friendy.homework;

import java.util.Scanner;

/**
 * -*- coding: utf-8 -*-
 *
 * @author Friendy 付<p>
 * {@code @software} IntelliJ IDEA<p>
 * {@code @create} 2022/9/14 10:15 encrypt-decrypt Lab1<p>
 * <p>
 * <br>
 */
public class Lab1 {
    public static void main(String[] args) {
        String input = "Hello World!";
        String en = "Izqzm piyzmznodhvoz tjpm kjrzm oj xcvibz tjpmnzga!";
        int key = 10;
        System.out.println("加密原文：" + input);
        String enCode = encryptKaiser(input, key);
        System.out.println("1）输入任意明文和密钥K=10，对其进行凯撒加密，得到的密文：" + enCode);
        String deCode = decryptKaiser(enCode, key);
        System.out.println("2）输入密文和密钥K=10，对其进行凯撒解密，解密后：" + deCode);
        System.out.println("3）破译如下密文：Izqzm piyzmznodhvoz tjpm kjrzm oj xcvibz tjpmnzga! 过程：");
        for (int i = 1; i < 26; ++i) {
            System.out.println("key=" + i + ": " + decryptKaiser(en, i));
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("所看到的合适的key值是：");
        int resKey = scanner.nextInt();//此处要输入你所看到的合适的key值
        System.out.println("破译结果为：key=" + resKey + "，明文为：" + decryptKaiser(en, resKey));
        //result: key=21
    }

    private static String encryptKaiser(String input, int key) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : input.toCharArray()) {
            char out = c;
            if (c >= 'a' && c <= 'z') {
                out = (char) (((int) c - (int) 'a' + key) % 26 + (int) 'a');
            } else if (c >= 'A' && c <= 'Z') {
                out = (char) (((int) c - (int) 'A' + key) % 26 + (int) 'A');
            }
            stringBuilder.append(out);
        }
        return stringBuilder.toString();
    }

    private static String decryptKaiser(String enCode, int key) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : enCode.toCharArray()) {
            char out = c;
            if (c >= 'a' && c <= 'z') {
                out = (char) (((int) c - (int) 'a' - key + 26) % 26 + (int) 'a');
            } else if (c >= 'A' && c <= 'Z') {
                out = (char) (((int) c - (int) 'A' - key + 26) % 26 + (int) 'A');
            }
            stringBuilder.append(out);
        }
        return stringBuilder.toString();
    }

}
