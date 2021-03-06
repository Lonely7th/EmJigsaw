package com.em.jigsaw.base;

/**
 * Time ： 2018/12/7 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class ContentKey {
    /**
     * 页面返回状态
     */
    public static final int Pager_Complete = 200;

    /**
     * 图片尺寸
     */
    public static final int[] ImgFormat_9_16 = {9,16,540,960};// 宽9高16

    /**
     * 图片裁剪格式
     */
    public static final int[] Format_3_3 = {3,3};
    public static final int[] Format_4_3 = {4,3};// 4行3列
    public static final int[] Format_4_4 = {4,4};
    public static final int[] Format_6_4 = {6,4};
    public static final int[] Format_6_6 = {6,6};
    public static final int[] Format_8_6 = {8,6};

    public static final String[] Format_Array = {"4-4","6-4","6-6","8-6"};

    /**
     * 选择图片方式
     */
    public static final int SelectPic_Gallery = 0;// 相册
    public static final int SelectPic_Camera = 1;// 相机

    /**
     * 高级选项相关
     */
    public static final String[] Limit_Type_Array = {"时间限制","次数限制"};
    public static final String[] Limit_Time_Array = {"60","90","120","180","300","360"};
    public static final String[] Limit_Count_Array = {"60","100","120","150","200","300"};

    /**
     * 页面限制模式
     */
    public static final int Limit_Type_None = 0;
    public static final int Limit_Type_Count = 2;
    public static final int Limit_Type_Timer = 1;
    /**
     * 分享相关
     */
    public static final String SHARE_URL = "https://www.baidu.com/";
    public static final String SHARE_TITLE = "SHARE_TITLE";
    public static final String SHARE_CONTENT = "SHARE_CONTENT";
    /**
     * 用户信息相关
     */
    public static final String LOGIN_JSONSTR = "EQCHA_LOGIN_JSONSTR";
    public static final String LOGIN_STATUS = "EQCHA_LOGIN_STATUS";
}
