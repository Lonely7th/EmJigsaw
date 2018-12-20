package com.em.jigsaw.utils;

import android.content.SharedPreferences;

import com.em.jigsaw.BaseApplication;
import com.em.jigsaw.base.ContentKey;

/**
 * Time ： 2018/12/20 0020 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class LoginUtil {
    /**
     * 登录成功
     */
    public static void loginSuccess(String userInfo){
        //保存用户信息
        SharedPreferences.Editor editor = BaseApplication.sf.edit();
        //添加登录状态
        editor.putBoolean(ContentKey.LOGIN_STATUS, true);
        editor.putString(ContentKey.LOGIN_JSONSTR, userInfo);
        editor.apply();
    }

    /**
     * 注销登录
     */
    public static void exitLogin(){
        //清空登录状态
        SharedPreferences.Editor editor = BaseApplication.sf.edit();
        editor.putBoolean(ContentKey.LOGIN_STATUS, false);
        editor.apply();
    }

    /**
     * 判断登录状态
     */
    public static boolean isLogin() {
        return BaseApplication.sf.getBoolean(ContentKey.LOGIN_STATUS, false);
    }

    /**
     * 获取用户信息
     */
    public static String getUserInfo(){
        return "";
    }
}
