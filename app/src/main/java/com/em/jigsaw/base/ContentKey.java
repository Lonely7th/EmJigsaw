package com.em.jigsaw.base;

/**
 * Time ： 2018/12/7 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class ContentKey {
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

    /**
     * 选择图片的方式
     */
    public static final int SelectPic_Gallery = 0;// 相册
    public static final int SelectPic_Camera = 1;// 相机
}
