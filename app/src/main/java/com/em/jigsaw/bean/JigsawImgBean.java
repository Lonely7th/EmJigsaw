package com.em.jigsaw.bean;

/**
 * Time ： 2018/12/6 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class JigsawImgBean {
    // 图片地址
    private String ImgPath = "";
    // 当前索引
    private int CurIndex;
    // 正确索引
    private int RealIndex;
    // 图片展示位置
    private int[] indexArray;
    // 图片尺寸
    private int[] ImgFormat;
    // 图片在父控件中的位置
    private int[] ImgPosition;// {startX,startY,endX,endY}

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

    public int[] getIndexArray() {
        return indexArray;
    }

    public void setIndexArray(int[] indexArray) {
        this.indexArray = indexArray;
    }

    public int[] getImgFormat() {
        return ImgFormat;
    }

    public void setImgFormat(int[] imgFormat) {
        ImgFormat = imgFormat;
    }

    public int[] getImgPosition() {
        return ImgPosition;
    }

    public void setImgPosition(int[] imgPosition) {
        ImgPosition = imgPosition;
    }
}
