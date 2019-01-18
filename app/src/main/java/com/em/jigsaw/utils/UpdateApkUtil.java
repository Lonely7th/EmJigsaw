package com.em.jigsaw.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.base.Request;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Time ： 2018/12/20 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class UpdateApkUtil {
    private static ProgressDialog proDialog = null;    //下载进度条
    private static String ApkPath,ApkName = "emjigsw.apk";

    public static void downLoadFile(final Activity context, String fileUrl) {
        // 创建文件夹
        ApkPath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/apk/";
        //创建文件夹
        File dir = new File(ApkPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        proDialog = new ProgressDialog(context);      //创建Dialog
        // 设置进度条风格，风格为长形
        proDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // 设置ProgressDialog 标题
        proDialog.setTitle("正在下载");
        // 设置ProgressDialog 提示信息
        proDialog.setMessage("正在下载，请勿关闭...");
        // 设置ProgressDialog 标题图标
        //proDialog.setIcon(R.drawable.ic_launcher);
        // 设置ProgressDialog 进度条进度
        proDialog.setProgress(0);
        // 设置ProgressDialog 的进度条是否不明确
        proDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        proDialog.setCancelable(false);
        proDialog.setMax(100);

        OkGo.<File>get(fileUrl)
                .tag(context)
                .execute(new FileCallback(ApkPath, ApkName) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<File> response) {
                        //打开apk文件安装
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(response.body()), "application/vnd.android.package-archive");
                        context.startActivity(intent);
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        proDialog.setProgress((int) (progress.fraction * 100));
                    }

                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        super.onStart(request);
                        proDialog.show();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        proDialog.dismiss();
                    }
                });
    }

}
