package com.friendy.homework;

import com.friendy.Utils.MD5Constants;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * -*- coding: utf-8 -*-
 *
 * @author Friendy 付<p>
 * {@code @software} IntelliJ IDEA<p>
 * {@code @create} 2022/12/21 10:54 encrypt-decrypt Lab10<p>
 * <p>
 * <br>
 */
public class Lab10 {
    public static void main(String[] args) {
        String encrypt = "HelloWorld";
        MD5_CTX md5_ctx = new MD5_CTX();
        md5_ctx = MD5Update(md5_ctx, encrypt, encrypt.length());
        Integer[] decrypt = MD5Final(md5_ctx);
        for (Integer i : decrypt) {
            System.out.printf("%02X", i & 0xff);
            System.out.printf("%02X", unsigned_rotate_right(i, 8) & 0xff);
            System.out.printf("%02X", unsigned_rotate_right(i, 16) & 0xff);
            System.out.printf("%02X", unsigned_rotate_right(i, 24) & 0xff);
        }
    }

    private static int F(int x, int y, int z) {
        return (((x) & (y)) | ((~x) & (z)));
    }

    private static int G(int x, int y, int z) {
        return (((x) & (z)) | ((y) & (~z)));
    }

    private static int H(int x, int y, int z) {
        return ((x) ^ (y) ^ (z));
    }

    private static int I(int x, int y, int z) {
        return ((y) ^ ((x) | (~z)));
    }

    private static int unsigned_rotate_right(int x, int n) {
        if (x > 0 || n == 0) {
            return x >> n;
        } else {
            return ((x >> n) & (((1 << (32 - n)) - 1)));
        }
    }

    private static int ROTATE_LEFT(int x, int n) {
        return (((x) << (n)) | unsigned_rotate_right(x, 32 - n));
    }

    private static int FF(int a, int b, int c, int d, int x, int s, int ac) {
        (a) += F((b), (c), (d)) + (x) + (ac);
        (a) = ROTATE_LEFT((a), (s));
        (a) += (b);
        return a;
    }

    private static int GG(int a, int b, int c, int d, int x, int s, int ac) {
        (a) += G((b), (c), (d)) + (x) + (ac);
        (a) = ROTATE_LEFT((a), (s));
        (a) += (b);
        return a;
    }

    private static int HH(int a, int b, int c, int d, int x, int s, int ac) {
        (a) += H((b), (c), (d)) + (x) + (ac);
        (a) = ROTATE_LEFT((a), (s));
        (a) += (b);
        return a;
    }

    private static int II(int a, int b, int c, int d, int x, int s, int ac) {
        (a) += I((b), (c), (d)) + (x) + (ac);
        (a) = ROTATE_LEFT((a), (s));
        (a) += (b);
        return a;
    }

    private static MD5_CTX MD5Update(MD5_CTX md5_ctx, String input, int inputLen) {
        int i;
        int index = (md5_ctx.count[0] >> 3) & 0x3f;
        if ((md5_ctx.count[0] += (inputLen << 3)) < (inputLen << 3))
            ++md5_ctx.count[1];
        md5_ctx.count[1] += (inputLen >> 29);
        int partLen = 64 - index;
        if (inputLen >= partLen) {
            md5_ctx.buffer.replace(index, index + partLen, input.substring(0, partLen));
            md5_ctx.state = MD5Transform(md5_ctx).state;
            for (i = partLen; i + 63 < inputLen; i += 64) {
                md5_ctx.buffer = new StringBuilder(input.substring(i, i + 64));
                md5_ctx.state = MD5Transform(md5_ctx).state;
            }
            index = 0;
        } else {
            i = 0;
        }
        md5_ctx.buffer.replace(index, index + inputLen - i, input.substring(i));
        return md5_ctx;
    }

    private static Integer[] MD5Final(MD5_CTX md5_ctx) {
        StringBuilder bits = Encode(new ArrayList<>(Arrays.asList(md5_ctx.count)));
        int index = (md5_ctx.count[0] >> 3) & 0x3f;
        int padLen = (index < 56) ? (56 - index) : (120 - index);
        md5_ctx = MD5Update(md5_ctx, String.valueOf(MD5Constants.PADDING), padLen);
        md5_ctx = MD5Update(md5_ctx, String.valueOf(bits), 8);
        return md5_ctx.state;
    }

    private static MD5_CTX MD5Transform(MD5_CTX md5_ctx) {
        Integer a = md5_ctx.state[0], b = md5_ctx.state[1], c = md5_ctx.state[2], d = md5_ctx.state[3];
        ArrayList<Integer> x = Decode(new StringBuilder(String.valueOf(md5_ctx.buffer)), 16);
        a = FF(a, b, c, d, x.get(0), MD5Constants.S[0][0], 0xd76aa478);
        d = FF(d, a, b, c, x.get(1), MD5Constants.S[0][1], 0xe8c7b756);
        c = FF(c, d, a, b, x.get(2), MD5Constants.S[0][2], 0x242070db);
//        b = FFt(b, c, d, a, x.get(3), MD5Constants.S[0][3], 0xc1bdceee);
        b = FF(b, c, d, a, x.get(3), MD5Constants.S[0][3], 0xc1bdceee);
        a = FF(a, b, c, d, x.get(4), MD5Constants.S[0][0], 0xf57c0faf);
        d = FF(d, a, b, c, x.get(5), MD5Constants.S[0][1], 0x4787c62a);
        c = FF(c, d, a, b, x.get(6), MD5Constants.S[0][2], 0xa8304613);
        b = FF(b, c, d, a, x.get(7), MD5Constants.S[0][3], 0xfd469501);
        a = FF(a, b, c, d, x.get(8), MD5Constants.S[0][0], 0x698098d8);
        d = FF(d, a, b, c, x.get(9), MD5Constants.S[0][1], 0x8b44f7af);
        c = FF(c, d, a, b, x.get(10), MD5Constants.S[0][2], 0xffff5bb1);
        b = FF(b, c, d, a, x.get(11), MD5Constants.S[0][3], 0x895cd7be);
        a = FF(a, b, c, d, x.get(12), MD5Constants.S[0][0], 0x6b901122);
        d = FF(d, a, b, c, x.get(13), MD5Constants.S[0][1], 0xfd987193);
        c = FF(c, d, a, b, x.get(14), MD5Constants.S[0][2], 0xa679438e);
        b = FF(b, c, d, a, x.get(15), MD5Constants.S[0][3], 0x49b40821);
        a = GG(a, b, c, d, x.get(1), MD5Constants.S[1][0], 0xf61e2562);
        d = GG(d, a, b, c, x.get(6), MD5Constants.S[1][1], 0xc040b340);
        c = GG(c, d, a, b, x.get(11), MD5Constants.S[1][2], 0x265e5a51);
        b = GG(b, c, d, a, x.get(0), MD5Constants.S[1][3], 0xe9b6c7aa);
        a = GG(a, b, c, d, x.get(5), MD5Constants.S[1][0], 0xd62f105d);
        d = GG(d, a, b, c, x.get(10), MD5Constants.S[1][1], 0x2441453);
        c = GG(c, d, a, b, x.get(15), MD5Constants.S[1][2], 0xd8a1e681);
        b = GG(b, c, d, a, x.get(4), MD5Constants.S[1][3], 0xe7d3fbc8);
        a = GG(a, b, c, d, x.get(9), MD5Constants.S[1][0], 0x21e1cde6);
        d = GG(d, a, b, c, x.get(14), MD5Constants.S[1][1], 0xc33707d6);
        c = GG(c, d, a, b, x.get(3), MD5Constants.S[1][2], 0xf4d50d87);
        b = GG(b, c, d, a, x.get(8), MD5Constants.S[1][3], 0x455a14ed);
        a = GG(a, b, c, d, x.get(13), MD5Constants.S[1][0], 0xa9e3e905);
        d = GG(d, a, b, c, x.get(2), MD5Constants.S[1][1], 0xfcefa3f8);
        c = GG(c, d, a, b, x.get(7), MD5Constants.S[1][2], 0x676f02d9);
        b = GG(b, c, d, a, x.get(12), MD5Constants.S[1][3], 0x8d2a4c8a);
        a = HH(a, b, c, d, x.get(5), MD5Constants.S[2][0], 0xfffa3942);
        d = HH(d, a, b, c, x.get(8), MD5Constants.S[2][1], 0x8771f681);
        c = HH(c, d, a, b, x.get(11), MD5Constants.S[2][2], 0x6d9d6122);
        b = HH(b, c, d, a, x.get(14), MD5Constants.S[2][3], 0xfde5380c);
        a = HH(a, b, c, d, x.get(1), MD5Constants.S[2][0], 0xa4beea44);
        d = HH(d, a, b, c, x.get(4), MD5Constants.S[2][1], 0x4bdecfa9);
        c = HH(c, d, a, b, x.get(7), MD5Constants.S[2][2], 0xf6bb4b60);
        b = HH(b, c, d, a, x.get(10), MD5Constants.S[2][3], 0xbebfbc70);
        a = HH(a, b, c, d, x.get(13), MD5Constants.S[2][0], 0x289b7ec6);
        d = HH(d, a, b, c, x.get(0), MD5Constants.S[2][1], 0xeaa127fa);
        c = HH(c, d, a, b, x.get(3), MD5Constants.S[2][2], 0xd4ef3085);
        b = HH(b, c, d, a, x.get(6), MD5Constants.S[2][3], 0x4881d05);
        a = HH(a, b, c, d, x.get(9), MD5Constants.S[2][0], 0xd9d4d039);
        d = HH(d, a, b, c, x.get(12), MD5Constants.S[2][1], 0xe6db99e5);
        c = HH(c, d, a, b, x.get(15), MD5Constants.S[2][2], 0x1fa27cf8);
        b = HH(b, c, d, a, x.get(2), MD5Constants.S[2][3], 0xc4ac5665);
        a = II(a, b, c, d, x.get(0), MD5Constants.S[3][0], 0xf4292244);
        d = II(d, a, b, c, x.get(7), MD5Constants.S[3][1], 0x432aff97);
        c = II(c, d, a, b, x.get(14), MD5Constants.S[3][2], 0xab9423a7);
        b = II(b, c, d, a, x.get(5), MD5Constants.S[3][3], 0xfc93a039);
        a = II(a, b, c, d, x.get(12), MD5Constants.S[3][0], 0x655b59c3);
        d = II(d, a, b, c, x.get(3), MD5Constants.S[3][1], 0x8f0ccc92);
        c = II(c, d, a, b, x.get(10), MD5Constants.S[3][2], 0xffeff47d);
        b = II(b, c, d, a, x.get(1), MD5Constants.S[3][3], 0x85845dd1);
        a = II(a, b, c, d, x.get(8), MD5Constants.S[3][0], 0x6fa87e4f);
        d = II(d, a, b, c, x.get(15), MD5Constants.S[3][1], 0xfe2ce6e0);
        c = II(c, d, a, b, x.get(6), MD5Constants.S[3][2], 0xa3014314);
        b = II(b, c, d, a, x.get(13), MD5Constants.S[3][3], 0x4e0811a1);
        a = II(a, b, c, d, x.get(4), MD5Constants.S[3][0], 0xf7537e82);
        d = II(d, a, b, c, x.get(11), MD5Constants.S[3][1], 0xbd3af235);
        c = II(c, d, a, b, x.get(2), MD5Constants.S[3][2], 0x2ad7d2bb);
        b = II(b, c, d, a, x.get(9), MD5Constants.S[3][3], 0xeb86d391);
        md5_ctx.state[0] += a;
        md5_ctx.state[1] += b;
        md5_ctx.state[2] += c;
        md5_ctx.state[3] += d;
        return md5_ctx;
    }


    public static StringBuilder Encode(ArrayList<Integer> input) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < input.size(); ++i) {
            stringBuilder.append((char) (input.get(i) & 0xff));
            stringBuilder.append((char) (unsigned_rotate_right(input.get(i), 8) & 0xff));
            stringBuilder.append((char) (unsigned_rotate_right(input.get(i), 16) & 0xff));
            stringBuilder.append((char) (unsigned_rotate_right(input.get(i), 24) & 0xff));
        }
        return stringBuilder;
    }

    /**
     * @param input
     * @param len   length of output result
     * @return
     */
    public static ArrayList<Integer> Decode(StringBuilder input, int len) {
        ArrayList<Integer> res = new ArrayList<>();
        int length = input.length() / 4;
        length *= 4;
        for (int i = 0; i < length; i += 4) {
            res.add((input.charAt(i)) | (input.charAt(i + 1) << 8) | (input.charAt(i + 2) << 16) | (input.charAt(i + 3) << 24));
        }
        length = input.length() - length;
        int tmp = 0;
        if (length > 0) {
            tmp = input.charAt(length);
            if (length > 1) {
                tmp |= (input.charAt(length + 1) << 8);
                if (length > 2) {
                    tmp |= (input.charAt(length + 2) << 16);
                }
            }
        }
        res.add(tmp);
        while (res.size() < len) {
            res.add(0x00000000);
        }
        return res;
    }

    static class MD5_CTX {
        Integer[] state = new Integer[4];
        Integer[] count = new Integer[2];
        StringBuilder buffer;

        public MD5_CTX() {
            state = new Integer[]{0x67452301, 0xefcdab89, 0x98badcfe, 0x10325476};
            count = new Integer[]{0, 0};
            buffer = new StringBuilder(64);
        }
    }
}
