package com.friendy.homework;

import com.friendy.Utils.CommonUtil;

import java.util.Arrays;

/**
 * -*- coding: utf-8 -*-
 *
 * @author Friendy 付<p>
 * {@code @software} IntelliJ IDEA<p>
 * {@code @create} 2022/12/11 19:42 encrypt-decrypt Lab9<p>
 * <p>
 * <br>
 */
public class Lab9 {
    private static final int[] E = {11, 1, 6};//q,a,b
    private static final int[] G = {2, 7};
    private static final int nB = 7;

    public static void main(String[] args) {
        int[] pB = multiple(G, nB);
        System.out.println("（a）B的公钥PB是：" + Arrays.toString(pB));
        int[] Pm = {10, 9};
        int k = 3;
        int[][] cm = encrypt(Pm, pB, k);
        System.out.println("（b）A要加密消息Pm=(10,9)，其选择的随机值k=3，则密文Cm为：{" + Arrays.toString(cm[0]) + ", " + Arrays.toString(cm[1]) + "}");
        int[] decrypt = minus(cm[1], multiple(cm[0], nB));
        System.out.println("（c）B由Cm恢复Pm的计算结果为：" + Arrays.toString(decrypt));
    }

    private static int count_lambda(int[] p, int[] q) {
        int lambda;
        int denominator;//分母
        int molecular;//分子
        if (Arrays.equals(p, q)) {
            denominator = 2 * p[1];
            molecular = 3 * p[0] * p[0] + E[1];
        } else {
            denominator = q[0] - p[0];
            molecular = q[1] - p[1];
        }
        if (denominator < 0) {
            denominator = -denominator;
            molecular = -molecular;
        }
        lambda = (molecular * CommonUtil.getInverse(denominator, E[0])) % E[0];
        while (lambda < 0) {
            lambda += E[0];
        }
        return lambda;
    }

    private static int[] add(int[] p, int[] q) {
        int lambda = count_lambda(p, q);
//        System.out.println(lambda);
        int xR = (lambda * lambda - p[0] - q[0]) % E[0];
        while (xR < 0) {
            xR += E[0];
        }
        int yR = (lambda * (p[0] - xR) - p[1]) % E[0];
        while (yR < 0) {
            yR += E[0];
        }
//        System.out.println("{" + p[0] + ", " + p[1] + "} + {" + q[0] + ", " + q[1] + "} = {" + xR + ", " + yR + "}");
        return new int[]{xR, yR};
    }

    private static int[] multiple(int[] p, int n) {
        if (n == 1) return p;
        if (n % 2 != 0) return add(p, multiple(p, n - 1));
        return multiple(add(p, p), n / 2);
    }

    private static int[][] encrypt(int[] pm, int[] pB, int k) {
        return new int[][]{multiple(G, k), add(pm, multiple(pB, k))};
    }
    private static int[] minus(int[] p, int[] q){
        return add(p, new int[]{q[0], -q[1]});
    }
}
