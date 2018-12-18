package com.em.jigsaw.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.base.ContentKey;
import com.em.jigsaw.view.LoadingDialog;
import com.em.jigsaw.view.SelectDialog;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * Time ： 2018/12/11 0011 .
 * Author ： JN Zhang .
 * Description ：设置高级选项 页面 .
 */
public class SelectJStatusActivity extends AppCompatActivity {

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
    @BindView(R.id.tv_crop_format)
    TextView tvCropFormat;
    @BindView(R.id.btn_crop_format)
    RelativeLayout btnCropFormat;
    @BindView(R.id.btn_hide_name)
    RelativeLayout btnHideName;
    @BindView(R.id.tv_limit_type)
    TextView tvLimitType;
    @BindView(R.id.btn_limit_type)
    RelativeLayout btnLimitType;
    @BindView(R.id.tv_limit_count)
    TextView tvLimitCount;
    @BindView(R.id.btn_limit_count)
    RelativeLayout btnLimitCount;
    @BindView(R.id.iv_hide_name)
    ImageView ivHideName;
    @BindView(R.id.tv_hide_name)
    TextView tvHideName;

    String cropFormat;
    Uri imageUri;
    boolean isHideName = false;

    SelectDialog selectDialog;
    int curSelectType = 0;
    ArrayList<String> selectTypeList = new ArrayList<>();
    int curSelectCount = 0;
    ArrayList<String> selectCountList = new ArrayList<>();

    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_jstatus);
        ButterKnife.bind(this);
        cropFormat = getIntent().getStringExtra("CropFormat");
        imageUri = Uri.parse(getIntent().getStringExtra("ImageUri"));
        initUI();
        initData();
    }

    private void initUI() {
        tvBarCenter.setText("高级选项");
        tvBarRight.setText("发布");
        tvBarRight.setVisibility(View.VISIBLE);

        tvCropFormat.setText(cropFormat);
    }

    private void initData() {
        selectTypeList.addAll(Arrays.asList(ContentKey.Limit_Type_Array));
    }

    @OnClick({R.id.back_btn, R.id.right_btn, R.id.btn_crop_format, R.id.btn_hide_name, R.id.btn_limit_type, R.id.btn_limit_count})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.right_btn:
                loadingDialog = new LoadingDialog(SelectJStatusActivity.this);
                loadingDialog.show();
                break;
            case R.id.btn_crop_format:
                finish();
                break;
            case R.id.btn_hide_name:
                isHideName = !isHideName;
                if (isHideName) {
                    ivHideName.setBackgroundResource(R.mipmap.icon_radio_button_checked);
                    tvHideName.setText("匿名");
                } else {
                    ivHideName.setBackgroundResource(R.mipmap.icon_radio_button_uncheck);
                    tvHideName.setText("不匿名");
                }
                break;
            case R.id.btn_limit_type:
                selectDialog = new SelectDialog(SelectJStatusActivity.this, selectTypeList, new SelectDialog.OnSelectListener() {
                    @Override
                    public void onItemSelect(View view, int position, long id) {
                        curSelectType = position;
                        tvLimitType.setText(selectTypeList.get(position));
                    }
                });
                selectDialog.show();
                break;
            case R.id.btn_limit_count:
                selectCountList.clear();
                if (curSelectType == 1) {
                    selectCountList.addAll(Arrays.asList(ContentKey.Limit_Count_Array));
                } else if (curSelectType == 2) {
                    selectCountList.addAll(Arrays.asList(ContentKey.Limit_Time_Array));
                } else {
                    break;
                }
                selectDialog = new SelectDialog(SelectJStatusActivity.this, selectCountList, new SelectDialog.OnSelectListener() {
                    @Override
                    public void onItemSelect(View view, int position, long id) {
                        curSelectCount = position;
                        if (curSelectType == 1) {
                            tvLimitCount.setText(selectCountList.get(position) + "次");
                        } else if (curSelectType == 2) {
                            tvLimitCount.setText(selectCountList.get(position) + "秒");
                        }
                    }
                });
                selectDialog.show();
                break;
        }
    }
}
