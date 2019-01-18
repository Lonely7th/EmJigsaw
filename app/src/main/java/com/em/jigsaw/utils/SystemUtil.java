package com.em.jigsaw.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

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
}
