package com.friendy.homework;

import com.friendy.Utils.CommonUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * -*- coding: utf-8 -*-
 *
 * @author Friendy 付<p>
 * {@code @software} IntelliJ IDEA<p>
 * {@code @create} 2022/11/25 9:02 encrypt-decrypt Lab7<p>
 * <p>
 * <br>
 */
public class Lab7 {
    public static void main(String[] args) {
        int q = 71;//prime number
        int alpha = 7;//root of origin
        int X_A = 5;
        int Y_A = CommonUtil.pow_modulus(alpha, X_A, q);
        int X_B = 12;
        int Y_B = CommonUtil.pow_modulus(alpha, X_B, q);
        int K = CommonUtil.pow_modulus(Y_B, X_A, q);
        System.out.println("（1）设公用素数q=71，本原根α= 7。");
        System.out.println("\t(a)若用户A的私钥XA=5，则A的公钥YA为 " + Y_A);
        System.out.println("\t(b)若用户B的私钥XB=12，则B的公钥YB为 " + Y_B);
        System.out.println("\t(c)共享的密钥为 " + K);
        q = 11;
        alpha = 2;
        Y_A = 9;
        Y_B = 3;
        ArrayList<Integer> dict = CommonUtil.pow_modulus(alpha, q);
        X_A = dict.indexOf(Y_A);
        X_B = dict.indexOf(Y_B);
        K = CommonUtil.pow_modulus(Y_A, X_B, q);
        System.out.println("（2）设公用素数q=11，本原根α=2。");
        System.out.println("\t(a)若用户A的公钥YA=9，则A的私钥XA为 " + X_A);
        System.out.println("\t(b)若用户B的公钥YB=3，则共享的密钥K为 " + K);
    }
}
