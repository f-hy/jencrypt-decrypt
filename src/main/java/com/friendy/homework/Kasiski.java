package com.friendy.homework;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * -*- coding: utf-8 -*-
 *
 * @author Friendy 付<p>
 * {@code @software} IntelliJ IDEA<p>
 * {@code @create} 2023/5/1 16:38 encrypt-decrypt Kasiski<p>
 * <p>
 * <br>用java实现读取密文文本并通过Kasiski测试方法破解密钥
 */
public class Kasiski {
    public static void main(String[] args) throws IOException {
        String ciphertext = readTextFile(".cache/cipher.txt");
        ciphertext = ciphertext.toUpperCase();
        ArrayList<Integer> keyLengthCandidates = guessKeyLength(ciphertext, 3, 20);
        System.out.println("Key length candidates: " + keyLengthCandidates);
        int[] key = guessKey(ciphertext, keyLengthCandidates.get(0));
        System.out.println("Key: " + keyToString(key));
        String plaintext = decrypt(ciphertext, key);
        System.out.println("Plaintext:\n" + plaintext);
    }

    private static String readTextFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        return stringBuilder.toString();
    }

    private static ArrayList<Integer> guessKeyLength(String ciphertext, int keyLengthMin, int keyLengthMax) {
        HashMap<Integer, Integer> distances = new HashMap<Integer, Integer>();
        for (int k = 1; k <= keyLengthMax; k++) {
            ArrayList<Integer> positions = new ArrayList<Integer>();
            for (int i = 0; i < ciphertext.length() - k + 1; i++) {
                String substring = ciphertext.substring(i, i + k);
                int index = ciphertext.indexOf(substring, i + k);
                while (index >= 0) {
                    positions.add(index - i);
                    index = ciphertext.indexOf(substring, index + 1);
                }
            }
            for (int i = 0; i < positions.size() - 1; i++) {
                for (int j = i + 1; j < positions.size(); j++) {
                    int distance = positions.get(j) - positions.get(i);
                    if (distance >= keyLengthMin && distance <= keyLengthMax) {
                        if (distances.containsKey(distance)) {
                            distances.put(distance, distances.get(distance) + 1);
                        } else {
                            distances.put(distance, 1);
                        }
                    }
                }
            }
        }

        ArrayList<Integer> keyLengthCandidates = new ArrayList<Integer>();
        int maxCount = 0;
        for (int distance : distances.keySet()) {
            int count = distances.get(distance);
            if (count > maxCount) {
                maxCount = count;
                keyLengthCandidates.clear();
                keyLengthCandidates.add(distance);
            } else if (count == maxCount) {
                keyLengthCandidates.add(distance);
            }
        }

        return keyLengthCandidates;
    }

    private static int[] guessKey(String ciphertext, int keyLength) {
        String[] plaintextGroups = new String[keyLength];
        for (int i = 0; i < ciphertext.length(); i++) {
            char c = ciphertext.charAt(i);
            int groupIndex = i % keyLength;
            if (plaintextGroups[groupIndex] == null) {
                plaintextGroups[groupIndex] = "";
            }
            plaintextGroups[groupIndex] += c;
        }

        int[] key = new int[keyLength];
        for (int i = 0; i < keyLength; i++) {
            int[] frequencies = new int[26];
            for (int j = 0; j < plaintextGroups[i].length(); j++) {
                char c = plaintextGroups[i].charAt(j);
                frequencies[c - 'A']++;
            }
            int maxFrequency = 0;
            int maxFrequencyIndex = 0;
            for (int j = 0; j < 26; j++) {
                if (frequencies[j] > maxFrequency) {
                    maxFrequency = frequencies[j];
                    maxFrequencyIndex = j;
                }
            }
            key[i] = (maxFrequencyIndex - ('E' - 'A') + 26) % 26;
        }

        return key;
    }

    private static String decrypt(String ciphertext, int[] key) {
        StringBuilder plaintextBuilder = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i++) {
            char c = ciphertext.charAt(i);
            int k = key[i % key.length];
            char p = (char) ((c - 'A' - k + 26) % 26 + 'A');
            plaintextBuilder.append(p);
        }
        return plaintextBuilder.toString();
    }

    private static String keyToString(int[] key) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int k : key) {
            stringBuilder.append((char) (k + 'A'));
        }
        return stringBuilder.toString();
    }
}