package com.friendy.homework;

import java.io.*;
import java.util.*;

import com.friendy.Utils.CommonUtil;

/**
 * -*- coding: utf-8 -*-
 *
 * @author Friendy 付<p>
 * {@code @software} IntelliJ IDEA<p>
 * {@code @create} 2022/9/16 8:06 encrypt-decrypt Lab2<p>
 * <p>
 * <br>
 */
public class Lab2 {
    static final List<Integer> alphaList = Arrays.asList(1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25);
    private static final int MODULUS = 26;
    private static final char[] FREQUENT_CHAR_LIST = {'e', 't', 'a', 'o', 'n'};
    //破解生成的最大文件数
    private static final int DE_MAX_FILE = 4;

    public static void main(String[] args) {
        String plain_path = ".cache/明文.txt";
        String plainText = fileToString(plain_path);
        String enCode = encryptText(plainText, 7, 19);
        System.out.println("加密后的结果：\n" + enCode);
//        String path = "密文.txt";
//        String enCode = fileToString(path);

        System.out.println("1）使用暴力破解的方式对仿射加密进行破解，还原明文：");
        long mid, end;
        long start = System.currentTimeMillis();
        for (Integer alpha : alphaList) {
            for (int beta = 0; beta < MODULUS; ++beta) {
                System.out.println("Alpha=" + alpha + ", Beta=" + beta + ": " + decryptAffine(enCode, alpha, beta).substring(0, 30));
            }
        }
        mid = System.currentTimeMillis();
        System.out.println("暴力破解时间：" + (mid - start));
        Scanner scanner = new Scanner(System.in);
        System.out.print("所看到的合适的Alpha Beta值是：");
        int alpha = scanner.nextInt();
        int beta = scanner.nextInt();
        System.out.println("明文为：\n" + decryptAffine(enCode, alpha, beta));
        end = System.currentTimeMillis();
        System.out.println("暴力破解总时间：" + (end - start));

        System.out.println("2）使用词频统计的方式对仿射加密进行破解，还原明文：");
        start = System.currentTimeMillis();
        ArrayList<int[]> abPairs = affineFrequencyAnalysis(enCode, "affine_de");
        for (int i = 0; i < DE_MAX_FILE; ++i) {
            System.out.println("第" + i + "组：alpha=" + abPairs.get(i)[0] + ", beta=" + abPairs.get(i)[1] + ": " + decryptAffine(enCode, abPairs.get(i)[0], abPairs.get(i)[1]).substring(0, 30));
        }
        mid = System.currentTimeMillis();
        System.out.println("频率分析法时间：" + (mid - start));
        System.out.print("所看到的合适的Alpha Beta值是第几组：");
        int resg = scanner.nextInt();
        String da = decryptAffine(enCode, abPairs.get(resg)[0], abPairs.get(resg)[1]);
        System.out.println("明文为：\n" + da);
        stringToFile(".cache/明文.txt", da);
        end = System.currentTimeMillis();
        System.out.println("频率分析法总时间：" + (end - start));
    }

    /**
     * @param path
     * @return String
     */
    public static String fileToString(String path) {
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer;
        try {
            bufferedReader = new BufferedReader(new FileReader(new File(path)));
            int buffer = 1024;
            char[] buf = new char[buffer];
            int len;
            stringBuffer = new StringBuffer();
            while ((len = bufferedReader.read(buf)) != -1) {
                stringBuffer.append(buf, 0, len);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return stringBuffer.toString();
    }

    public static void stringToFile(String path, String data) {
        stringToFile(new File(path), data);
    }

    /**
     * @param path
     * @param data
     */
    public static void stringToFile(File path, String data) {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(path));
            bufferedWriter.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static String encryptText(String plainText, int alpha, int beta) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : plainText.toCharArray()) {
            char out = c;
            if (c >= 'a' && c <= 'z') {
                int tmp = ((c - (int) 'a') * alpha + beta) % MODULUS;
                tmp = tmp >= 0 ? tmp : (tmp + MODULUS);
                out = (char) (tmp + (int) 'a');
            } else if (c >= 'A' && c <= 'Z') {
                int tmp = ((c - (int) 'A') * alpha + beta) % MODULUS;
                tmp = tmp >= 0 ? tmp : (tmp + MODULUS);
                out = (char) (tmp + (int) 'A');
            }
            stringBuilder.append(out);
        }
        return stringBuilder.toString();
    }

    public static String decryptAffine(String enCode, int alpha, int beta) {
        int alphaInverse = CommonUtil.getInverse(alpha, MODULUS);
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : enCode.toCharArray()) {
            char out = c;
            if (c >= 'a' && c <= 'z') {
                int tmp = ((c - (int) 'a' - beta) * alphaInverse) % MODULUS;
                tmp = tmp >= 0 ? tmp : (tmp + MODULUS);
                out = (char) (tmp + (int) 'a');
            } else if (c >= 'A' && c <= 'Z') {
                int tmp = ((c - (int) 'A' - beta) * alphaInverse) % MODULUS;
                tmp = tmp >= 0 ? tmp : (tmp + MODULUS);
                out = (char) (tmp + (int) 'A');
            }
            stringBuilder.append(out);
        }
        return stringBuilder.toString();
    }

    public static List<Map.Entry<Character, Integer>> getDescendingCountChar(String data) {
        HashMap<Character, Integer> map = new HashMap<>();
        for (char c : data.toCharArray()) {
            if (!map.containsKey(c)) {
                map.put(c, 1);
            } else {
                map.put(c, map.get(c) + 1);
            }
        }
        ArrayList<Map.Entry<Character, Integer>> mapList = new ArrayList<>(map.entrySet());//map转换成list便于排序
        Collections.sort(mapList, new Comparator<Map.Entry<Character, Integer>>() {
            @Override
            public int compare(Map.Entry<Character, Integer> o1, Map.Entry<Character, Integer> o2) {
                return -o1.getValue().compareTo(o2.getValue());
            }
        });
        return mapList;
    }

    private static ArrayList<int[]> affineFrequencyAnalysis(String enCode, String destPath) {
//        int deCount = 0;
        /*File directory = new File(destPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }*/
        ArrayList<int[]> res = new ArrayList<>();
        List<Map.Entry<Character, Integer>> entryList = getDescendingCountChar(enCode);
//        System.out.println(entryList);
        int x1 = FREQUENT_CHAR_LIST[0] - 'a';
        int x2 = FREQUENT_CHAR_LIST[1] - 'a';
        for (int i = 0; i < DE_MAX_FILE; ++i) {
            int y1 = entryList.get(i).getKey() - 'a';
            int y2 = entryList.get(i + 1).getKey() - 'a';
            int dx = x1 - x2;
            int dy = y1 - y2;
            if (dx < 0) {
                dx = -dx;
                dy = -dy;
            }
            int dxInverse = CommonUtil.getInverse(dx, MODULUS);
            int alpha = (dy * dxInverse) % MODULUS;
            alpha = alpha >= 0 ? alpha : (alpha + MODULUS);
            int beta = (y1 - alpha * x1) % MODULUS;
            beta = beta >= 0 ? beta : (beta + MODULUS);
            res.add(new int[]{alpha, beta});
        }
        return res;
    }
}
