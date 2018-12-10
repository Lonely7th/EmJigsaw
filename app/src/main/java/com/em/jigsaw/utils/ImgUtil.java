package com.em.jigsaw.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.em.jigsaw.bean.JigsawImgBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
        }
    }

    /**
     * 拆分图片
     */
    public ArrayList<JigsawImgBean> getImgArray(int format){
        ArrayList<JigsawImgBean> list = new ArrayList<>();

        AssetManager manager = mContext.getResources().getAssets();
        try {
            InputStream inputStream = manager.open("test.png");
            Bitmap bm = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            int mHeight = bm.getHeight() / format;
            int mWidth = bm.getWidth() / format;

            int x,y = 0,index = 0;
            for(int i = 0;i < format;i++){
                x = 0;
                for(int k = 0;k < format;k++){
                    Bitmap bm1 = ImgSplit(bm,x,y,mWidth,mHeight);
                    String filePath = ImgSave(bm1,"pic" + i + k + ".png");

                    JigsawImgBean jigsawImgBean = new JigsawImgBean();
                    jigsawImgBean.setImgPath(filePath);
                    jigsawImgBean.setCurIndex(index);
                    jigsawImgBean.setRealIndex(index);
                    list.add(jigsawImgBean);

                    x += mWidth;
                    index++;
                }
                y+=mHeight;
            }
        } catch (IOException e) {
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
