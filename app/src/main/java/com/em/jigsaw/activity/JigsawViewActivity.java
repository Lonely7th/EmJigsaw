package com.em.jigsaw.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.em.jigsaw.R;
import com.em.jigsaw.base.ContentKey;
import com.em.jigsaw.bean.JigsawImgBean;
import com.em.jigsaw.callback.OnJigsawChangedListener;
import com.em.jigsaw.utils.ImgUtil;
import com.em.jigsaw.utils.ToastUtil;
import com.em.jigsaw.view.JigsawView;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JigsawViewActivity extends AppCompatActivity {
    //private static final String TAG = "JigsawViewActivity";

    @BindView(R.id.view_jigsaw)
    JigsawView viewJigsaw;
    @BindView(R.id.iv_content)
    ImageView ivContent;

    private ImgUtil imgUtil;
    private ArrayList<JigsawImgBean> list = new ArrayList<>();

    private int[] ImgFormat = ContentKey.ImgFormat_9_16;
    private int[] CropFormat = ContentKey.Format_6_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jigsaw_view);
        ButterKnife.bind(this);
        imgUtil = new ImgUtil(JigsawViewActivity.this);
        initUI();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        viewJigsaw.setOnChangedListener(new OnJigsawChangedListener() {
            @Override
            public void onChanged(ArrayList<JigsawImgBean> arrayList) {
                if(imgUtil.jigsawSuccess(arrayList)){
                    ToastUtil.show(JigsawViewActivity.this,"成功");
                    ivContent.setVisibility(View.VISIBLE);
                    viewJigsaw.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 刷新jigsawAdapter
     */
    private void updateJigsawList(Bitmap bitmap) {
        list.clear();
        list.addAll(imgUtil.sortImgArray(imgUtil.getImgArray(bitmap, CropFormat, ImgFormat)));
        viewJigsaw.setLabels(list);
    }
}
