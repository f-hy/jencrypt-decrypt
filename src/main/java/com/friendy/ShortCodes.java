package com.friendy;

import com.friendy.Utils.EDUtil;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * -*- coding: utf-8 -*-
 *
 * @author Friendy 付<p>
 * {@code @software} IntelliJ IDEA<p>
 * {@code @create} 2022/9/4 12:26 encrypt-decrypt ShortCodes<p>
 * <p>
 * <br>
 */
public class ShortCodes {
    @Test
    private static void baseTest(){
        System.out.println("Hello 友谊!\n-Dfile.encoding=GB2312");
    }
    @Test
    private static void byteTest(){
        String a = "a";
        for (byte aByte : a.getBytes()) {
            System.out.println((int) aByte);
        }
    }
    @Test
    private static void bitTest(){
        String a = "a";
        for (byte aByte : a.getBytes()) {
            System.out.println(Integer.toBinaryString(aByte));
        }
    }
    @Test
    private static void HanziTest() throws UnsupportedEncodingException {
        String a = "啊";
        System.out.println(a.getBytes().length);
        System.out.println(a.getBytes(StandardCharsets.UTF_8).length);
        System.out.println(a.getBytes("GBK").length);
        for (byte aByte : a.getBytes()) {
            System.out.println(aByte);
            System.out.println(Integer.toBinaryString(aByte));
        }
    }
    @Test
    private static void DESTest(){
        String msg = "Hello 友谊!";
        String shift = "12345678";
        String transformation = "DES";
        String algorithm = "DES";
        byte[] bytes = EDUtil.encryptDES(msg, shift);
        byte[] bytes1 = EDUtil.encryptDES(msg.getBytes(), shift, transformation, algorithm);
        System.out.println(Arrays.toString(bytes));
        System.out.println(Arrays.toString(bytes1));
        System.out.println(new String(bytes));
        System.out.println(new String(bytes1));
        byte[] de_bytes = EDUtil.decryptDES(bytes, shift, transformation, algorithm);
        System.out.println(Arrays.toString(de_bytes));
        System.out.println(new String(de_bytes));
    }
    @Test
    private static void DigestTest(){
        String clear = "aa";// 算法
        String algorithm = "MD5";// 算法
        MessageDigest messageDigest = null; // 获取数字摘要对象
        try {
            messageDigest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] digest = messageDigest.digest(clear.getBytes());// 获取消息数字摘要的字节数组
        System.out.println(new String(digest));//乱码
        System.out.println(Arrays.toString(Base64.getEncoder().encode(digest)));
        System.out.println(Base64.getEncoder().encodeToString(digest));
        System.out.println(Arrays.toString(digest));
        System.out.println(new BigInteger(1, digest).toString(16));
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : digest) {
            String s = Integer.toHexString(b & 0xff);// 转成 16进制
            if(s.length()==1){
                s+="0";// 如果生成的字符只有一个，前面补0
            }
            stringBuilder.append(s);
        }
        System.out.println(stringBuilder.toString());
    }
    private static String getDigest(String clear, String algorithm){
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] digest = messageDigest.digest(clear.getBytes());
        return new BigInteger(1, digest).toString(16);
    }
    private static void getDigestTest(){
        String clear = "aa";
        ArrayList<String> algorithms = new ArrayList<>(Arrays.asList("MD5", "SHA-1", "SHA-256", "SHA-512"));
        for (String algorithm : algorithms) {
            System.out.println(getDigest(clear, algorithm));
        }
    }
    private static void RSATest(){
        String algorithm = "RSA";
        KeyPairGenerator keyPairGenerator = null;//  创建密钥对生成器对象
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        KeyPair keyPair = keyPairGenerator.generateKeyPair();// 生成密钥对
        // 对公私钥进行base64编码，只是为了显示，并不用这个 生成私钥 获取私钥字节数组
        String privateKeyString = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        String publicKeyString = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        System.out.println(privateKeyString.length());
        System.out.println(publicKeyString.length());

        String clear = "aa";
        byte[] de_bytes = new byte[0];
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());//私钥加密；若公钥加密和公钥解密，一样报错
            byte[] pri_bytes = cipher.doFinal(clear.getBytes());
            System.out.println(Base64.getEncoder().encodeToString(pri_bytes));
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPublic());//私钥解密会报错
            de_bytes = cipher.doFinal(pri_bytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Base64.getEncoder().encodeToString(de_bytes));
        System.out.println(new String(de_bytes));
    }
    /**
     * 解密数据
     * @param algorithm      : 算法
     * @param encrypted      : 密文
     * @param key            : 密钥
     * @return : 原文
     */
    public static String decryptRSA(String algorithm, Key key, String encrypted){
        byte[] bytes = new byte[0];// 对密文进行解密，不需要使用base64，因为原文不会乱码
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decode = Base64.getDecoder().decode(encrypted);// 由于密文进行了Base64编码, 在这里需要进行解码
            bytes = cipher.doFinal(decode);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return new String(bytes);
    }
    /**
     * 使用密钥加密数据
     * @param algorithm      : 算法
     * @param clear          : 原文
     * @param key            : 密钥
     * @return : 密文
     */
    public static String encryptRSA(String algorithm, Key key, String clear){
        byte[] bytes = new byte[0];
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            bytes = cipher.doFinal(clear.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return Base64.getEncoder().encodeToString(bytes);
    }
    /**
     * 生成密钥对并保存在本地文件中
     * 1. 获取密钥对生成器
     * 2. 获取密钥对
     * 3. 获取公/私钥 获取byte数组 进行Base64编码
     * 4. 保存文件
     * @param algorithm : 算法
     * @param pubPath   : 公钥保存路径
     * @param priPath   : 私钥保存路径
     */
    public static void generateKeyToFile(String algorithm, String pubPath, String priPath){
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            String publicKeyString = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            String privateKeyString = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
            FileUtils.writeStringToFile(new File(pubPath), publicKeyString, Charset.forName("UTF-8"));
            FileUtils.writeStringToFile(new File(priPath), privateKeyString, Charset.forName("UTF-8"));
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取私钥
     * @param priPath 私钥的路径
     * @param algorithm 算法
     * @return 返回私钥的key对象
     */
    public static PrivateKey getPrivateKey(String priPath, String algorithm){
        try {
            String privateKeyString = FileUtils.readFileToString(new File(priPath), Charset.defaultCharset());// 将文件内容转为字符串
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);// 获取密钥工厂
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));// 构建密钥规范 进行Base64解码
            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);// 生成私钥
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取公钥
     * @param pubPath 公钥路径
     * @param algorithm 算法
     * @return 返回公钥的key对象
     */
    public static PublicKey getPublicKey(String pubPath, String algorithm){
        try {
            String publicKeyString = FileUtils.readFileToString(new File(pubPath), Charset.defaultCharset());
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    private static void RSATest1(){
        String clear = "aa";
        String algorithm = "RSA";
        generateKeyToFile(algorithm, "aa.pub", "aa.pri");
        PrivateKey privateKey = getPrivateKey("aa.pri", algorithm);
        PublicKey publicKey = getPublicKey("aa.pub", algorithm);
        String en = encryptRSA(algorithm, privateKey, clear);
        String de = decryptRSA(algorithm, publicKey, en);
        System.out.println("en = " + en);
        System.out.println("de = " + de);
    }

    /**
     * 生成签名
     * @param clear      : 原文
     * @param algorithm  : 算法
     * @param privateKey : 私钥
     * @return : 签名
     */
    public static String getSignature(String clear, String algorithm, PrivateKey privateKey){
        byte[] sign = new byte[0];
        try {
            Signature signature = Signature.getInstance(algorithm);
            signature.initSign(privateKey);
            signature.update(clear.getBytes());
            sign = signature.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
        return Base64.getEncoder().encodeToString(sign);
    }
    public static void main(String[] args) {
//        DigestTest();
//        getDigestTest();
//        RSATest1();
//        System.out.println('t' - 'a');
        System.out.println(EDUtil.getInverse(7, 26));
    }
}
