package com.em.jigsaw.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.bean.JigsawImgBean;
import com.em.jigsaw.utils.ImgUtil;
import com.em.jigsaw.view.JigsawView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddJigsawActivity extends AppCompatActivity {

    @BindView(R.id.back_btn)
    RelativeLayout backBtn;
    @BindView(R.id.tv_bar_center)
    TextView tvBarCenter;
    @BindView(R.id.iv_right_icon)
    ImageView ivRightIcon;
    @BindView(R.id.tv_bar_right)
    TextView tvBarRight;
    @BindView(R.id.right_btn)
    RelativeLayout rightBtn;
    @BindView(R.id.view_jigsaw)
    JigsawView viewJigsaw;
    @BindView(R.id.iv_content)
    ImageView ivContent;

    private ImgUtil imgUtil;
    private ArrayList<JigsawImgBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jigsaw_view);
        ButterKnife.bind(this);
        imgUtil = new ImgUtil(AddJigsawActivity.this);

        Uri imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
        ivContent.setImageURI(imageUri);
        updateJigsawList(imgUtil.getBitmap(imageUri));
    }

    /**
     * 刷新jigsawAdapter
     */
    private void updateJigsawList(Bitmap bitmap) {
        list.clear();
//        list.addAll(imgUtil.sortImgArray(imgUtil.getImgArray(bitmap, CropFormat, ImgFormat)));
        viewJigsaw.setLabels(list);
    }

    @OnClick({R.id.back_btn, R.id.right_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.right_btn:
                break;
        }
    }
}
