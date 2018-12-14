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
    // 图片尺寸
    private int[] ImgFormat;
    // 图片在父控件中的位置
    private double[] ImgPosition;// {startX,startY,endX,endY}
    //裁剪格式
    private int[] cropFormat;

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

    //获取图片索引
    public int[] getIndexArray() {
        int index = 0;
        int[] indexArray = new int[2];
        for(int x = 0;x < cropFormat[0];x++){
            for(int y = 0;y < cropFormat[1];y++){
                if(CurIndex == index){
                    indexArray[0] = x;
                    indexArray[1] = y;
                }
                index++;
            }
        }
        return indexArray;
    }

    public int[] getImgFormat() {
        return ImgFormat;
    }

    public void setImgFormat(int[] imgFormat) {
        ImgFormat = imgFormat;
    }

    public double[] getImgPosition() {
        return ImgPosition;
    }

    public void setImgPosition(double[] imgPosition) {
        ImgPosition = imgPosition;
    }

    public int[] getCropFormat() {
        return cropFormat;
    }

    public void setCropFormat(int[] cropFormat) {
        this.cropFormat = cropFormat;
    }
}
