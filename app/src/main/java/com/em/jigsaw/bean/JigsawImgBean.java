package com.em.jigsaw.bean;

/**
 * Time ： 2018/12/6 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class JigsawImgBean {
    //图片地址
    private String ImgPath = "";
    //当前索引
    private int CurIndex;
    //正确索引
    private int RealIndex;

    public String getImgPath() {
        return ImgPath;
    }

    public void setImgPath(String imgPath) {
        ImgPath = imgPath;
    }

    public int getCurIndex() {
        return CurIndex;
    }

    public void setCurIndex(int curIndex) {
        CurIndex = curIndex;
    }

    public int getRealIndex() {
        return RealIndex;
    }

    public void setRealIndex(int realIndex) {
        RealIndex = realIndex;
    }
}