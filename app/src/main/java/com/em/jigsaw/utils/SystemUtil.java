package com.em.jigsaw.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.HashSet;

/**
 * Time ： 2019/1/17 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class SystemUtil {

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }
    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    //获取App版本信息
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 随机指定范围内N个不重复的数
     * 利用HashSet的特征，只能存放不同的值
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n 随机数个数
     */
    public static void randomSet(int min, int max, int n, HashSet<Integer> set) {
        if (n > (max - min + 1) || max < min) {
            return;
        }
        for (int i = 0; i < n; i++) {
            // 调用Math.random()方法
            int num = (int) (Math.random() * (max - min)) + min;
            set.add(num);// 将不同的数存入HashSet中
        }
        int setSize = set.size();
        // 如果存入的数小于指定生成的个数，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小
        if (setSize < n) {
            randomSet(min, max, n - setSize, set);// 递归
        }
    }
}
