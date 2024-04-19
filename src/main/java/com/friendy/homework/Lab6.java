package com.friendy.homework;

import com.friendy.Utils.CommonUtil;

import java.util.Arrays;
import java.util.List;

/**
 * -*- coding: utf-8 -*-
 *
 * @author Friendy 付<p>
 * {@code @software} IntelliJ IDEA<p>
 * {@code @create} 2022/11/20 15:14 encrypt-decrypt Lab6<p>
 * <p>
 * <br>
 */
public class Lab6 {
    public static void main(String[] args) {
        int[][] datas = {
                {3, 11, 7, 5},
                {5, 11, 3, 9},
                {7, 11, 17, 8},
                {11, 13, 11, 7},
                {17, 31, 7, 2}
        };// p,q,e,M
        for (int[] data : datas) {
            print(data);
            List<int[]> pupr = getPUPR(data[0], data[1], data[2]);
            int C = CommonUtil.pow_modulus(data[3], pupr.get(0)[0], pupr.get(0)[1]);
            System.out.print(";\tgetC = " + C);
            int myM = CommonUtil.pow_modulus(C, pupr.get(1)[0], pupr.get(1)[1]);
            System.out.println("\tgetM = " + myM);
        }
    }

    public static List<int[]> getPUPR(int p, int q, int e) {
        int n = p * q;
        int phi = (p - 1) * (q - 1);
        int d = CommonUtil.getInverse(e, phi);
        int[] PU = new int[]{e, n};
        int[] PR = new int[]{d, n};
        return Arrays.asList(new int[][]{PU, PR});
    }

    private static void print(int[] data) {
        System.out.print("p=" + data[0] +
                ", q=" + data[1] +
                ", e=" + data[2] +
                ", M=" + data[3]);
    }
}
