package com.friendy.Utils;

import java.util.ArrayList;

/**
 * -*- coding: utf-8 -*-
 *
 * @author Friendy 付<p>
 * {@code @software} IntelliJ IDEA<p>
 * {@code @create} 2022/9/24 17:24 encrypt-decrypt CommonUtil<p>
 * <p>
 * <br>
 */
public class CommonUtil {
    public static int diff(String[] a, String[] b) {
        int sum = 0;
        for (int i = 0; i < a.length; ++i) {
            if (!a[i].equals(b[i])) {
                ++sum;
                System.out.println(i + "(" + a[i] + ", " + b[i] + ")");
            }
        }
        return sum;
    }

    public static int diff(int[] a, int[] b) {
        int sum = 0;
        for (int i = 0; i < a.length; ++i) {
            if (a[i] != b[i]) {
                ++sum;
                System.out.println(i + "(" + a[i] + ", " + b[i] + ")");
            }
        }
        return sum;
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
     *
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

    /**
     * quick compute (a**b)%modulus
     *
     * @param a
     * @param b
     * @param modulus
     * @return (a * * b)%modulus
     */

    /**
     * take negative number into account
     * @param a number to be modulated
     * @param modulus modulus
     * @return
     */
    public static int modulus(int a, int modulus) {
        int i = 1;
        while (a < 0) {
            a += (modulus * i);
            i += i;
        }
        return a % modulus;
    }

    public static int pow_modulus(int a, int b, int modulus) {
        if (b == 0) return 1;
        if (b % 2 != 0) return modulus(a * pow_modulus(a, b - 1, modulus), modulus);
        return pow_modulus(modulus(a * a, modulus), b / 2, modulus);
    }
    /*public static int pow_modulus(int a, int b, int modulus){
        if(b == 0) return 1;
        if(b%2 != 0) return (a*pow_modulus(a,b-1,modulus))%modulus;
        return pow_modulus((a*a)%modulus,b/2,modulus);
    }*/

    /**
     * get a dict of compute result of (a**i)%modulus
     *
     * @param a
     * @param modulus
     * @return [(a * * i)%modulus]
     */
    public static ArrayList<Integer> pow_modulus(int a, int modulus) {
        ArrayList<Integer> res = new ArrayList<>();
        res.add(0);
        for (int i = 1; i < modulus; i++) {
            res.add(pow_modulus(a, i, modulus));
        }
        return res;
    }
}
