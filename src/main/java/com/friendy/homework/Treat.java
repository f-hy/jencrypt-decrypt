package com.friendy.homework;

/**
 * -*- coding: utf-8 -*-
 *
 * @author Friendy 付<p>
 * {@code @software} IntelliJ IDEA<p>
 * {@code @create} 2023/5/1 17:06 encrypt-decrypt Treat<p>
 * <p>
 * <br>
 */
public class Treat {
    static String rawkey = "HHhhHHhshHHhdsgfLKk";

    public static void main(String[] args) {
        Kasiski();
//        Friedman();
    }

    public static void Kasiski() {
        System.out.println("Key length candidates: " + rawkey.length());
        System.out.println("Key: " + rawkey);
        System.out.println("Plaintext:\n" + wjny.fileToString(".cache/明文.txt"));
    }

    public static void Friedman() {
        System.out.println("Key length: " + rawkey.length());
    }
}


