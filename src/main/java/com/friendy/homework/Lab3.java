package com.friendy.homework;


import com.friendy.Utils.DESConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * -*- coding: utf-8 -*-
 *
 * @author Friendy 付<p>
 * {@code @software} IntelliJ IDEA<p>
 * {@code @create} 2022/9/23 8:02 encrypt-decrypt Lab3<p>
 * <p>
 * <br>
 * https://blog.csdn.net/hxx290/article/details/121659221
 */
public class Lab3 {
    private static final int LEFT_MOVE_COUNT = 16;//循环左移的轮数
    private static final int LOOPS = 16;//加密的轮数

    /**
     * 置换函数
     *
     * @param data             被置换的数据
     * @param permutationTable 置换表
     * @return 置换的数据
     */
    public static ArrayList<Integer> permutation(ArrayList<Integer> data, int[] permutationTable) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int pti : permutationTable) {
            result.add(data.get(pti - 1));
        }
        return result;
    }

    /**
     * S盒置换
     *
     * @param data 被置换的数据
     * @param sBox S盒
     * @return 置换的数据
     */
    public static ArrayList<Integer> spermutation(ArrayList<Integer> data, int[][][] sBox) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < 8; ++i) {
            int j = data.get(6 * i) * 2 + data.get(6 * i + 5);
            int k = data.get(6 * i + 1) * 8 + data.get(6 * i + 2) * 4 + data.get(6 * i + 3) * 2 + data.get(6 * i + 4);
            for (int l = 3; l >= 0; --l) {
                result.add((sBox[i][j][k] & (1 << l)) == 0 ? 0 : 1);
            }
        }
        return result;
    }

    /**
     * 将字符串转换为二进制数组
     *
     * @param data
     * @return
     */
    public static ArrayList<Integer> toBinary(String data) {
        ArrayList<Integer> result = new ArrayList<>();
        for (char c : data.toCharArray()) {
            int i = c;
            for (int j = 7; j >= 0; --j) {
                result.add((i & (1 << j)) == 0 ? 0 : 1);
            }
        }
        return result;
    }

    /**
     * 将二进制数组8个一组转换成字符串
     *
     * @param data
     * @return
     */
    public static String toString(List<Integer> data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.size() / 8; ++i) {
            int ci = 0;
            for (int j = 0; j < 8; ++j) {
                ci = ci * 2 + data.get(i * 8 + j);
            }
            stringBuilder.append((char) ci);
        }
        return stringBuilder.toString();
    }

    /**
     * 将二进制数组转换成二进制字符串
     *
     * @param data
     * @return
     */
    public static String toBinaryString(ArrayList<Integer> data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer datum : data) {
            stringBuilder.append(datum);
        }
        return stringBuilder.toString();
    }

    /**
     * 将二进制数组转换成十六进制字符串
     *
     * @param data
     * @return
     */
    public static String toHexString(ArrayList<Integer> data) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.size() / 4; ++i) {
            int sum = 0;
            for (int j = 0; j < 4; ++j) {
                sum = sum * 2 + data.get(4 * i + j);
            }
            stringBuilder.append(chars[sum]);
        }
        return stringBuilder.toString();
    }

    /**
     * DES将64位明文/密文转换成64位密文/明文
     *
     * @param data      明文/密文
     * @param subKeys   子密钥组
     * @param isEncrypt 1加密，0解密
     * @return 密文/明文
     */
    public static ArrayList<Integer> DES(ArrayList<Integer> data, ArrayList<ArrayList<Integer>> subKeys, boolean isEncrypt) {
        ArrayList<Integer> IP = permutation(data, DESConstants.IP);
        ArrayList<Integer> M = loopF(IP, subKeys, isEncrypt);
        ArrayList<Integer> MIP_1 = permutation(M, DESConstants.IP_1);
        return MIP_1;
    }

    /**
     * DES加密
     *
     * @param data 明文
     * @param key  密钥
     * @return 密文
     */
    public static ArrayList<Integer> DESencrypt(String data, String key) {
        String pdata = padding(data);
        String pkey = key.length() == 8 ? key : padding(key).substring(0, 8);
        ArrayList<ArrayList<Integer>> subKeys = genSubKeys(toBinary(pkey));
        ArrayList<Integer> en = new ArrayList<>();
        for (int i = 0; i < pdata.length() / 8; ++i) {
            en.addAll(DES(toBinary(pdata.substring(8 * i, 8 * i + 8)), subKeys, true));
        }
        return en;
    }

    /**
     * DES解密
     *
     * @param en  密文
     * @param key 密钥
     * @return 明文
     */
    public static ArrayList<Integer> DESdecrypt(ArrayList<Integer> en, String key) {
        if (en.size() % 64 != 0) {
            return new ArrayList<>();
        }
        String pkey = key.length() == 8 ? key : padding(key).substring(0, 8);
        ArrayList<ArrayList<Integer>> subKeys = genSubKeys(toBinary(pkey));
        ArrayList<Integer> de = new ArrayList<>();
        for (int i = 0; i < en.size() / 64; ++i) {
            ArrayList<Integer> des = DES(new ArrayList<>(en.subList(64 * i, 64 * i + 64)), subKeys, false);
            de.addAll(des);
//            de.addAll(DES(new ArrayList<>(en.subList(64 * i, 64 * i + 64)), subKeys, false));
        }
        int size = de.size();
        int padding = Integer.parseInt(toString(de.subList(size - 8, size)));
        return new ArrayList<>(de.subList(0, size - padding * 8));
    }

    /**
     * F函数循环
     *
     * @param data      循环数据
     * @param subKeys   子密钥组
     * @param isEncrypt 1加密，0解密
     * @return 循环结果
     */
    public static ArrayList<Integer> loopF(ArrayList<Integer> data, ArrayList<ArrayList<Integer>> subKeys, boolean isEncrypt) {
        int halfLen = data.size() / 2;
        ArrayList<Integer> L = new ArrayList<Integer>(data.subList(0, halfLen));
        ArrayList<Integer> R = new ArrayList<>(data.subList(halfLen, data.size()));
        if (isEncrypt) {
            for (int i = 0; i < LOOPS; ++i) {
                ArrayList<Integer> t = new ArrayList<>(F(L, R, subKeys.get(i)));
                L = new ArrayList<>(R);
                R = new ArrayList<>(t);
            }
        } else {
            for (int i = LOOPS - 1; i >= 0; --i) {
                ArrayList<Integer> t = new ArrayList<>(F(L, R, subKeys.get(i)));
                L = new ArrayList<>(R);
                R = new ArrayList<>(t);
            }
        }
        R.addAll(L);
        return R;
    }

    /**
     * 生成子密钥组
     *
     * @param key 密钥
     * @return 子密钥组
     */
    public static ArrayList<ArrayList<Integer>> genSubKeys(ArrayList<Integer> key) {
        ArrayList<ArrayList<Integer>> subKeys = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> k1 = permutation(key, DESConstants.PC_1);
        int halfLen = k1.size() / 2;
        ArrayList<Integer> C = new ArrayList<Integer>(k1.subList(0, halfLen));
        ArrayList<Integer> D = new ArrayList<Integer>(k1.subList(halfLen, k1.size()));
        for (int i = 0; i < LEFT_MOVE_COUNT; ++i) {
            C = rotateLeft(C, DESConstants.LM[i]);
            D = rotateLeft(D, DESConstants.LM[i]);
            ArrayList<Integer> t = new ArrayList<Integer>(C);
            t.addAll(D);
            subKeys.add(new ArrayList<>(permutation(t, DESConstants.PC_2)));
        }
        return subKeys;
    }

    /**
     * 循环左移
     *
     * @param data   要左移的数组
     * @param offset 左移位数
     * @return 左移结果
     */
    public static ArrayList<Integer> rotateLeft(ArrayList<Integer> data, int offset) {
        ArrayList<Integer> result = new ArrayList<>(data.subList(offset, data.size()));
        for (int i = 0; i < offset; ++i) {
            result.add(data.get(i));
        }
        return result;
    }

    /**
     * F函数
     *
     * @param Li
     * @param Ri
     * @param subKeyi
     * @return
     */
    public static ArrayList<Integer> F(ArrayList<Integer> Li, ArrayList<Integer> Ri, ArrayList<Integer> subKeyi) {
        //扩展置换E
        ArrayList<Integer> RE = permutation(Ri, DESConstants.E);
        //由子秘钥Ki加密
        for (int i = 0; i < RE.size(); ++i) {
            RE.set(i, RE.get(i) ^ subKeyi.get(i));
        }
        //S盒
        ArrayList<Integer> S32 = spermutation(RE, DESConstants.S);
        //置换P
        ArrayList<Integer> RP = permutation(S32, DESConstants.P);
        for (int i = 0; i < RP.size(); ++i) {
            RP.set(i, RP.get(i) ^ Li.get(i));
        }
        return RP;
    }

    /**
     * 填充函数，若字符串不是8的倍速，则填充至8的倍数，并记录填充字符个数
     *
     * @param data 要被填充的字符串
     * @return 填充结果
     */
    public static String padding(String data) {
        StringBuilder stringBuilder = new StringBuilder(data);
        int paddNum = 8 - data.length() % 8;
        for (int i = 0; i < paddNum; ++i) {
            stringBuilder.append(paddNum);
        }
        return stringBuilder.toString();
    }

    /**
     * 打印结果（二进制数组，二进制字符串，十六进制字符串，强转字符串)
     *
     * @param data
     */
    public static void print(ArrayList<Integer> data) {
        System.out.println("Binary array: size: " + data.size() + ": " + data);
        String tbs = toBinaryString(data);
        System.out.println("Binary stream: size: " + tbs.length() + ": " + tbs);
        String ths = toHexString(data);
        System.out.println("Hexadecimal stream: size: " + ths.length() + ": " + ths);
        String ts = toString(data);
        System.out.println("String: size: " + ts.length() + ": " + ts);
    }

    /**
     * 将以空格分割的二进制字符串强转为字符串
     *
     * @param b8s
     * @return
     */
    public static String b8stoString(String b8s) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String b8 : b8s.split(" ")) {
            stringBuilder.append((char) Integer.parseInt(b8, 2));
        }
        return stringBuilder.toString();
    }

    /**
     * 统计数组不同数据位的数量
     *
     * @param a
     * @param b
     * @return
     */
    public static int diffCount(ArrayList<Integer> a, ArrayList<Integer> b) {
        int sum = 0;
        for (int i = 0; i < a.size(); ++i) {
            if (a.get(i) != b.get(i)) {
                ++sum;
            }
        }
        return sum;
    }

    public static ArrayList<Integer> tDESencrypt(String data, String[] keys) {
        //EDE
        ArrayList<Integer> e = DESencrypt(data, keys[0]);
        ArrayList<Integer> d = DESdecrypt(e, keys[1]);
        return DESencrypt(toString(d), keys[2]);
    }

    public static ArrayList<Integer> tDESdecrypt(ArrayList<Integer> en, String[] keys) {
        //DED
        ArrayList<Integer> d = DESdecrypt(en, keys[0]);
        ArrayList<Integer> e = DESencrypt(toString(d), keys[1]);
        return DESdecrypt(e, keys[2]);
    }

    public static void main(String[] args) {
        String key = "abcdABCD";
        String[] keys = {"abcdABCD", "abcdefgh", "81726354"};
        String data = "Big Data Security";
        ArrayList<Integer> ten = tDESencrypt(data, keys);
        print(ten);
        ArrayList<Integer> tde = tDESdecrypt(ten, keys);
        print(tde);
//        System.out.println("1）实现DES加密算法，已知密钥abcdABCD，对明文“Big Data Security”进行加密");
//        ArrayList<Integer> en = DESencrypt(data, key);
//        print(en);
//        System.out.println("2）实现DES解密算法，对1）得到的密文进行解密");
//        ArrayList<Integer> de = DESdecrypt(en, key);
//        print(de);
//        String s1 = "00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000";
//        String s2 = "10000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000";
//        System.out.println("3）已知密钥abcdABCD，明文块1，明文块2，使用同一密钥对两组明文分别进行加密，输出加密后两个密文块的二进制流，统计两个密文块间不同数据位的数量");
//        String block1 = b8stoString(s1);
//        String block2 = b8stoString(s2);
//        ArrayList<Integer> b1 = DESencrypt(block1, key);
//        ArrayList<Integer> b2 = DESencrypt(block2, key);
//        System.out.println("加密后的明文块1的二进制：" + toBinaryString(b1));
//        System.out.println("加密后的明文块2的二进制：" + toBinaryString(b2));
//        System.out.println(toHexString(b1));
//        System.out.println(toHexString(b2));
//        System.out.println("两个密文块间不同数据位的数量：" + diffCount(b1, b2));
    }
}