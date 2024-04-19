package com.friendy;

import com.friendy.Utils.EDUtil;

import java.io.File;
import java.util.*;

/**
 * -*- coding: utf-8 -*-
 *
 * @author Friendy 付<p>
 * {@code @software} IntelliJ IDEA<p>
 * {@code @create} 2022/9/4 17:59 encrypt-decrypt FrequencyAnalysis<p>
 * <p>
 * <br>
 */
public class FrequencyAnalysis {
    //英文里出现次数最多的字符
    private static final char MAGIC_CHAR = 'e';
    //破解生成的最大文件数
    private static final int DE_MAX_FILE = 4;

    public static void main(String[] args) {
        String srcFile = "article.txt";
        String enFile = "article_en.txt";
        String dePath = "article_de";
        //测试1，统计字符个数
        printCharCount(srcFile);
        int shift = 12;
        encryptKaiserFile(srcFile, enFile, shift);
        printCharCount(enFile);
        decryptKaiserCode(EDUtil.fileToString(enFile), dePath);
    }

    private static void printCharCount(String path) {
        String data = EDUtil.fileToString(path);
        for (Map.Entry<Character, Integer> entry : getDescendingCountChar(data)) {
            System.out.println("字符'" + entry.getKey() + "'出现" + entry.getValue() + "次");
        }
    }

    private static void encryptKaiserFile(String srcFile, String destFile, int shift) {
        String clear = EDUtil.fileToString(srcFile);
        String encryptKaiser = EDUtil.encryptKaiser(clear, shift);//加密文件
        EDUtil.stringToFile(destFile, encryptKaiser);
    }

    /**
     * @param cipher
     * @param destPath Should be directory
     */
    private static void decryptKaiserCode(String cipher, String destPath) {
        int deCount = 0;//当前解密生成的备选文件数
        //获取出现频率最高的字符信息（出现次数越多越靠前）
        File directory = new File(destPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        for (Map.Entry<Character, Integer> entry : getDescendingCountChar(cipher)) {
            //限制解密文件备选数
            if (deCount >= DE_MAX_FILE) {
                break;
            }
            System.out.println("字符'" + entry.getKey() + "'出现" + entry.getValue() + "次");
            ++deCount;
            //出现次数最高的字符跟MAGIC_CHAR的偏移量即为秘钥
            int shift = entry.getKey() - MAGIC_CHAR;
            System.out.println("猜测shift = " + shift + "， 解密生成第" + deCount + "个备选文件" + "\n");
            String clear = EDUtil.decryptKaiser(cipher, shift);
            String fileName = "de_" + deCount;
            EDUtil.stringToFile(new File(directory, fileName), clear);
        }
    }

    private static List<Map.Entry<Character, Integer>> getDescendingCountChar(String data) {
        HashMap<Character, Integer> map = new HashMap<>();
        for (char c : data.toCharArray()) {
            if (!map.containsKey(c)) {
                map.put(c, 1);
            } else {
                map.put(c, map.get(c) + 1);
            }
        }
        /*int maxCount = 0;
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            //不统计空格
            if(*//*entry.getKey() != ' ' && *//*entry.getValue()>maxCount){
                maxCount = entry.getValue();
            }
        }*/
        ArrayList<Map.Entry<Character, Integer>> mapList = new ArrayList<>(map.entrySet());//map转换成list便于排序
        Collections.sort(mapList, new Comparator<Map.Entry<Character, Integer>>() {
            @Override
            public int compare(Map.Entry<Character, Integer> o1, Map.Entry<Character, Integer> o2) {
                return -o1.getValue().compareTo(o2.getValue());
            }
        });
        return mapList;
    }
}
