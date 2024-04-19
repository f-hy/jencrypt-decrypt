package com.friendy.homework;

import java.util.*;

/**
 * -*- coding: utf-8 -*-
 *
 * @author Friendy 付<p>
 * {@code @software} IntelliJ IDEA<p>
 * {@code @create} 2023/5/1 17:14 encrypt-decrypt Friedman<p>
 * <p>
 * <br>
 */
public class Friedman {
    public static void main(String[] args) {
        String ciphertext = wjny.fileToString(".cache/res密文.txt");
        int keyLength = friedmanTest(ciphertext);
        System.out.println("Key length: " + keyLength);
    }

    public static int friedmanTest(String ciphertext) {
        int n = ciphertext.length();
        Map<Character, Integer> freqMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            char c = ciphertext.charAt(i);
            if (freqMap.containsKey(c)) {
                freqMap.put(c, freqMap.get(c) + 1);
            } else {
                freqMap.put(c, 1);
            }
        }

        double sum = 0;
        for (char c : freqMap.keySet()) {
            int freq = freqMap.get(c);
            sum += freq * (freq - 1);
        }

        double ic = sum / (n * (n - 1));
        int keyLength = (int) Math.round(0.027 * n / ((n - 1) * ic + 0.065 - 0.038 * n));
        return keyLength;
    }
}