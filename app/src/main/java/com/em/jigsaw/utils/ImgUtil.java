package com.em.jigsaw.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.em.jigsaw.activity.JigsawViewActivity;
import com.em.jigsaw.base.ContentKey;
import com.em.jigsaw.bean.JigsawImgBean;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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

    /**
     * 根据文件路径获取Bitmap
     * @param pathName 文件路径
     */
    public Bitmap getBitmap(String pathName){
        try {
            return BitmapFactory.decodeFile(pathName);
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
                    jigsawImgBean.setCropFormat(cropFormat);
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
    public ArrayList<JigsawImgBean> swapImgArray(ArrayList<JigsawImgBean> list,int i1,int i2){
        Collections.swap(list,i1,i2);
        for(int i = 0;i < list.size();i++){
            list.get(i).setCurIndex(i);
        }
        return new ArrayList<>(list);
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
    public String ImgSave(Bitmap bm, String picName){
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

    /**
     * 保存图片到相册
     */
    public void saveImageToGallery(String path) {
        Glide.get(mContext).clearMemory();
        Glide.with(mContext).load(path).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                // 首先保存图片
                File appDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(), "pic");
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                String fileName = System.currentTimeMillis() + ".jpg";
                File file = new File(appDir, fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 其次把文件插入到系统图库
                try {
                    MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                            file.getAbsolutePath(), fileName, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // 最后通知图库更新
                mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
            }
        });
    }

    public void saveImageToGallery(Bitmap bitmap) {
        Glide.get(mContext).clearMemory();
        Glide.with(mContext).load(bitmap).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                // 首先保存图片
                File appDir = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(), "pic");
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                String fileName = System.currentTimeMillis() + ".jpg";
                File file = new File(appDir, fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 其次把文件插入到系统图库
                try {
                    MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                            file.getAbsolutePath(), fileName, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // 最后通知图库更新
                mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
            }
        });
    }

    /**
     * 根据标识获取裁剪尺寸
     */
    public int[] getCropFormatByFlag(String flag){
        int[] result = new int[2];
        switch (flag){
            case "4-3":
                result = ContentKey.Format_4_3;
                break;
            case "4-4":
                result = ContentKey.Format_4_4;
                break;
            case "6-4":
                result = ContentKey.Format_6_4;
                break;
            case "6-6":
                result = ContentKey.Format_6_6;
                break;
        }
        return result;
    }

}
