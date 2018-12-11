package com.em.jigsaw.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.em.jigsaw.bean.JigsawImgBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Time ： 2018/12/7 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class ImgUtil {
    private static final String TAG = "ImgUtil";
    private Context mContext;
    private String ImgDirPath;

    public ImgUtil(Context mContext){
        this.mContext = mContext;
        ImgDirPath = mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/pic0/";
        //创建文件夹
        File dir = new File(ImgDirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }else {
            File[] files = dir.listFiles();
            for(File file : files){
                file.delete();
            }
        }
    }

    public Bitmap getBitmap(Uri uri){
        try {
            // 读取uri所在的图片
            return MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 拆分图片
     * @param bm 图片内容
     * @param cropFormat 裁剪图片的格式
     * @param ImgFormat 原图的格式
     * @return 裁剪获得图片的列表
     */
    public ArrayList<JigsawImgBean> getImgArray(Bitmap bm, int[] cropFormat, int[] ImgFormat){
        ArrayList<JigsawImgBean> list = new ArrayList<>();

        try {
            int x,y = 0,index = 0;
            int mHeight = bm.getHeight() / cropFormat[0];
            int mWidth = bm.getWidth() / cropFormat[1];

            for(int i = 0;i < cropFormat[0];i++){
                x = 0;
                for(int k = 0;k < cropFormat[1];k++){
                    Bitmap bm1 = ImgSplit(bm,x,y,mWidth,mHeight);
                    String filePath = ImgSave(bm1,"" + System.currentTimeMillis() + i + k + ".png");

                    JigsawImgBean jigsawImgBean = new JigsawImgBean();
                    jigsawImgBean.setImgPath(filePath);
                    jigsawImgBean.setCurIndex(index);
                    jigsawImgBean.setRealIndex(index);
                    jigsawImgBean.setImgFormat(ImgFormat);
                    int[] index_array = {i,k};
                    jigsawImgBean.setIndexArray(index_array);
                    list.add(jigsawImgBean);

                    x += mWidth;
                    index++;
                }
                y+=mHeight;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 打乱图片顺序
     */
    public ArrayList<JigsawImgBean> sortImgArray(ArrayList<JigsawImgBean> data){
        ArrayList<JigsawImgBean> list = new ArrayList<>(data);
        Collections.shuffle(list);
        for(int i = 0;i < list.size();i++){
            list.get(i).setCurIndex(i);
        }
        return list;
    }

    /**
     * 交换图片位置
     */
    public void swapImgArray(ArrayList<JigsawImgBean> list,int i1,int i2){
        Collections.swap(list,i1,i2);
        for(int i = 0;i < list.size();i++){
            list.get(i).setCurIndex(i);
        }
    }

    /**
     * 判断图片排序是否正确
     */
    public boolean jigsawSuccess(ArrayList<JigsawImgBean> list){
        for(JigsawImgBean bean : list){
            if(bean.getCurIndex() != bean.getRealIndex()){
                return false;
            }
        }
        return true;
    }

    /**
     * 图片压缩
     */
    private void ImgCompress(){

    }

    /**
     * 截取图片的部分
     */
    private Bitmap ImgSplit(Bitmap bm,int x,int y,int width,int height){
        return Bitmap.createBitmap(bm,x,y,width,height);
    }

    /**
     * 保存图片到本地
     */
    private String ImgSave(Bitmap bm, String picName){
        try {
            File f = new File(ImgDirPath, picName);
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            return f.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
