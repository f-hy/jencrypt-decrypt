package com.friendy.Utils;

/**
 * -*- coding: utf-8 -*-
 *
 * @author Friendy 付<p>
 * {@code @software} IntelliJ IDEA<p>
 * {@code @create} 2023/2/8 16:06 encrypt-decrypt Summary<p>
 * <p>
 * <br>
 */
public class Summary {
    public static void main(String[] args) {
        int i = CommonUtil.pow_modulus(435, 765, 2579);
        System.out.println(CommonUtil.getInverse(i, 2579));
    }
    //1KaiserEncryption

    /**
     * 仿射加密
     * @param input
     * @param key
     * @return
     */
    public static StringBuilder AffineEncryption(String input, int[] key){
        StringBuilder ciphertext = new StringBuilder();
        for (char c : input.toCharArray()) {
            char out = c;
            if (c >= 'a' && c <= 'z') {
                out = (char) ((((int) c - (int) 'a')*key[0]+key[1]) % 26 + (int) 'a');
            } else if (c >= 'A' && c <= 'Z') {
                out = (char) ((((int) c - (int) 'A')*key[0]+key[1]) % 26 + (int) 'A');
            }
            ciphertext.append(out);
        }
        return ciphertext;
    }
    public static StringBuilder AffineDecryption(String ciphertext, int[] key){
        StringBuilder plaintext = new StringBuilder();
        for (char c : ciphertext.toCharArray()) {
            char out = c;
            if (c >= 'a' && c <= 'z') {
                int tmp = ((c - (int) 'a' - key[1]) * CommonUtil.getInverse(key[0],26)) % 26;
                tmp = tmp >= 0 ? tmp : (tmp + 26);
                out = (char) (tmp + (int) 'a');
            } else if (c >= 'A' && c <= 'Z') {
                int tmp = ((c - (int) 'A' - key[1]) * CommonUtil.getInverse(key[0],26)) % 26;
                tmp = tmp >= 0 ? tmp : (tmp + 26);
                out = (char) (tmp + (int) 'A');
            }
            plaintext.append(out);
        }
        return plaintext;
    }
    //3DES
    //4GF(24)
    //5AES
    //6
    //7
    //8
    //9
    //10
    //11
}
