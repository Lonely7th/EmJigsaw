package com.em.jigsaw.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.base.ContentKey;
import com.em.jigsaw.base.ServiceAPI;
import com.em.jigsaw.bean.event.ReleaseEvent;
import com.em.jigsaw.utils.LoginUtil;
import com.em.jigsaw.utils.ToastUtil;
import com.em.jigsaw.view.LoadingDialog;
import com.em.jigsaw.view.SelectDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
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
    @BindView(R.id.tv_label_1)
    TextView tvLabel1;
    @BindView(R.id.tv_label_2)
    TextView tvLabel2;
    @BindView(R.id.tv_label_3)
    TextView tvLabel3;
    @BindView(R.id.edt_content)
    EditText edtContent;

    Uri imageUri;
    File ResFile;
    String cropFormat;
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

    private void release() {
        String content = TextUtils.isEmpty(edtContent.getText().toString())?"我发布了一条新状态，快来点击看看吧~":edtContent.getText().toString();
        try {
            ResFile = new File(new URI(imageUri.toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        OkGo.<String>post(ServiceAPI.AddJDetails).tag(this)
                .params("content", content)
                .params("releaser", LoginUtil.getUserInfo().getUserNo())
                .params("res", ResFile)
                .params("jtype", ""+curSelectType)
                .params("limitNum", ""+curSelectCount)
                .params("hideUser", isHideName)
                .params("cropFormat", cropFormat)
                .params("label1", "8")
                .params("labelTitle1", "非主流")
                .params("label2", "9")
                .params("labelTitle2", "搞笑")
                .params("label3", "")
                .params("labelTitle3", "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            if (body.getInt("ResultCode") == ServiceAPI.HttpSuccess) {
                                JSONObject ResultData = body.getJSONObject("ResultData");
                                if(loadingDialog != null){
                                    loadingDialog.dismiss();
                                }
                                ToastUtil.show(SelectJStatusActivity.this, "发布成功");
                                startActivity(new Intent(SelectJStatusActivity.this, JigsawViewActivity.class)
                                        .putExtra("id",ResultData.getString("NoteId")));
                                EventBus.getDefault().post(new ReleaseEvent(0));
                                finish();
                            } else {
                                ToastUtil.show(SelectJStatusActivity.this, "网络异常");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtil.show(SelectJStatusActivity.this, "网络异常");
                    }
                });
    }

    @OnClick({R.id.back_btn, R.id.right_btn, R.id.btn_crop_format, R.id.btn_hide_name, R.id.btn_limit_type, R.id.btn_limit_count, R.id.tv_label_1,
            R.id.tv_label_2, R.id.tv_label_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.right_btn:
                release();
                loadingDialog = new LoadingDialog(SelectJStatusActivity.this);
                loadingDialog.show();
                break;
            case R.id.btn_crop_format:
                finish();
                break;
            case R.id.btn_hide_name:
                isHideName = !isHideName;
                if (isHideName) {
                    ivHideName.setBackgroundResource(R.mipmap.icon_visibility_off_s);
                    tvHideName.setText("匿名");
                } else {
                    ivHideName.setBackgroundResource(R.mipmap.icon_visibility_off_n);
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
                if (curSelectType == 2) {
                    selectCountList.addAll(Arrays.asList(ContentKey.Limit_Count_Array));
                } else if (curSelectType == 1) {
                    selectCountList.addAll(Arrays.asList(ContentKey.Limit_Time_Array));
                } else {
                    break;
                }
                selectDialog = new SelectDialog(SelectJStatusActivity.this, selectCountList, new SelectDialog.OnSelectListener() {
                    @Override
                    public void onItemSelect(View view, int position, long id) {
                        curSelectCount = Integer.parseInt(selectCountList.get(position));
                        if (curSelectType == 2) {
                            tvLimitCount.setText(selectCountList.get(position) + "次");
                        } else if (curSelectType == 1) {
                            tvLimitCount.setText(selectCountList.get(position) + "秒");
                        }
                    }
                });
                selectDialog.show();
                break;
            case R.id.tv_label_1:
                startActivity(new Intent(SelectJStatusActivity.this, SelectLabelActivity.class));
                break;
            case R.id.tv_label_2:
                startActivity(new Intent(SelectJStatusActivity.this, SelectLabelActivity.class));
                break;
            case R.id.tv_label_3:
                startActivity(new Intent(SelectJStatusActivity.this, SelectLabelActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loadingDialog != null){
            loadingDialog.dismiss();
        }
    }
}
