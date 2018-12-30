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
    @BindView(R.id.iv_tab_close_1)
    ImageView ivTabClose1;
    @BindView(R.id.iv_tab_close_2)
    ImageView ivTabClose2;
    @BindView(R.id.iv_tab_close_3)
    ImageView ivTabClose3;

    Uri imageUri;
    File ResFile;
    String cropFormat;
    boolean isHideName = false;

    String tabId1, tabId2, tabId3;
    String tabTitle1, tabTitle2, tabTitle3;

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
        String content = TextUtils.isEmpty(edtContent.getText().toString()) ? "我发布了一条新状态，快来点击看看吧~" : edtContent.getText().toString();
        try {
            ResFile = new File(new URI(imageUri.toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        OkGo.<String>post(ServiceAPI.AddJDetails).tag(this)
                .params("content", content)
                .params("releaser", LoginUtil.getUserInfo().getUserNo())
                .params("res", ResFile)
                .params("jtype", "" + curSelectType)
                .params("limitNum", "" + curSelectCount)
                .params("hideUser", isHideName)
                .params("cropFormat", cropFormat)
                .params("label1", tabId1)
                .params("labelTitle1", tabTitle1)
                .params("label2", tabId2)
                .params("labelTitle2", tabTitle2)
                .params("label3", tabId3)
                .params("labelTitle3", tabTitle3)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            if (body.getInt("ResultCode") == ServiceAPI.HttpSuccess) {
                                JSONObject ResultData = body.getJSONObject("ResultData");
                                if (loadingDialog != null) {
                                    loadingDialog.dismiss();
                                }
                                ToastUtil.show(SelectJStatusActivity.this, "发布成功");
                                startActivity(new Intent(SelectJStatusActivity.this, JigsawViewActivity.class)
                                        .putExtra("id", ResultData.getString("NoteId")));
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
            R.id.tv_label_2, R.id.tv_label_3,R.id.iv_tab_close_1, R.id.iv_tab_close_2, R.id.iv_tab_close_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.right_btn:
                if(TextUtils.isEmpty(tabId1) && TextUtils.isEmpty(tabId1) && TextUtils.isEmpty(tabId1)){
                    ToastUtil.show(SelectJStatusActivity.this,"请添加至少一个标签");
                    return ;
                }
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
                startActivityForResult(new Intent(SelectJStatusActivity.this,
                        SelectLabelActivity.class).putExtra("tabId", tabTitle1 + "/" + tabTitle2 + "/" + tabTitle3), 0);
                break;
            case R.id.tv_label_2:
                startActivityForResult(new Intent(SelectJStatusActivity.this,
                        SelectLabelActivity.class).putExtra("tabId", tabTitle1 + "/" + tabTitle2 + "/" + tabTitle3), 1);
                break;
            case R.id.tv_label_3:
                startActivityForResult(new Intent(SelectJStatusActivity.this,
                        SelectLabelActivity.class).putExtra("tabId", tabTitle1 + "/" + tabTitle2 + "/" + tabTitle3), 2);
                break;
            case R.id.iv_tab_close_1:
                tabId1 = "";
                tabTitle1 = "";
                tvLabel1.setText("未添加");
                tvLabel1.setTextColor(getResources().getColor(R.color.colorGary));
                tvLabel1.setBackgroundResource(R.drawable.bg_border_gray);
                ivTabClose1.setVisibility(View.GONE);
                break;
            case R.id.iv_tab_close_2:
                tabId2 = "";
                tabTitle2 = "";
                tvLabel2.setText("未添加");
                tvLabel2.setTextColor(getResources().getColor(R.color.colorGary));
                tvLabel2.setBackgroundResource(R.drawable.bg_border_gray);
                ivTabClose2.setVisibility(View.GONE);
                break;
            case R.id.iv_tab_close_3:
                tabId3 = "";
                tabTitle3 = "";
                tvLabel3.setText("未添加");
                tvLabel3.setTextColor(getResources().getColor(R.color.colorGary));
                tvLabel3.setBackgroundResource(R.drawable.bg_border_gray);
                ivTabClose3.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ContentKey.Pager_Complete) {
            switch (requestCode) {
                case 0:
                    tabId1 = data.getStringExtra("id");
                    tabTitle1 = data.getStringExtra("title");
                    tvLabel1.setText(tabTitle1);
                    tvLabel1.setTextColor(getResources().getColor(R.color.colorBlue));
                    tvLabel1.setBackgroundResource(R.drawable.bg_border_blue);
                    ivTabClose1.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    tabId2 = data.getStringExtra("id");
                    tabTitle2 = data.getStringExtra("title");
                    tvLabel2.setText(tabTitle2);
                    tvLabel2.setTextColor(getResources().getColor(R.color.colorBlue));
                    tvLabel2.setBackgroundResource(R.drawable.bg_border_blue);
                    ivTabClose2.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    tabId3 = data.getStringExtra("id");
                    tabTitle3 = data.getStringExtra("title");
                    tvLabel3.setText(tabTitle3);
                    tvLabel3.setTextColor(getResources().getColor(R.color.colorBlue));
                    tvLabel3.setBackgroundResource(R.drawable.bg_border_blue);
                    ivTabClose3.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}
