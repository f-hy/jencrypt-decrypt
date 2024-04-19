package com.friendy.homework;

import com.friendy.Utils.CommonUtil;

/**
 * -*- coding: utf-8 -*-
 *
 * @author Friendy 付<p>
 * {@code @software} IntelliJ IDEA<p>
 * {@code @create} 2022/12/23 10:30 encrypt-decrypt Lab11<p>
 * <p>
 * <br>
 */
public class Lab11 {
    private static final int p = 67;
    private static final int q = 11;
    private static final int h = 3;
    public static void main(String[] args) {
        int g = CommonUtil.pow_modulus(h, (p-1)* CommonUtil.getInverse(q, p), p);
        System.out.println("（1）求得参数g为" + g);
        int x = 5;
        int y = CommonUtil.pow_modulus(g, x, p);
        System.out.println("（2）取私钥x=5，求得公钥y为" + y);
        int M=4,k=3;
        int r = CommonUtil.pow_modulus(g, k, p)%q;
        int s = (CommonUtil.getInverse(k, q)*(H(M)+x*r))%q;
        System.out.println("（3）设消息M=4，取随机数k=3，求得M的签名（为了简化，用M替代H(M)）为(" + r + ", " + s + ")");
        System.out.println("（4）对M=4的上述签名进行验证。");
        int w = CommonUtil.getInverse(s, p);
        int u1 = (H(M)*w)%q;
        int u2 = (r*w)%q;
        int v =  ((CommonUtil.pow_modulus(g, u1, q)*CommonUtil.pow_modulus(y,u2,q))%p)%q;
        System.out.println("验证时计算得到的v为"+v);
        System.out.println("验证的结果 " + (v == r));
    }
    private static int H(int M){
        return M;
    }
}
