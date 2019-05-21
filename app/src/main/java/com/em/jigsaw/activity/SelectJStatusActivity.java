package com.em.jigsaw.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.adapter.SelectCropFormatAdapter;
import com.em.jigsaw.base.ContentKey;
import com.em.jigsaw.bean.JNoteBean;
import com.em.jigsaw.bean.OddsBean;
import com.em.jigsaw.bean.SelectCropFormatBean;
import com.em.jigsaw.bean.event.ReleaseEvent;
import com.em.jigsaw.utils.OddsUtil;
import com.em.jigsaw.utils.ToastUtil;
import com.em.jigsaw.view.dialog.SelectDialog;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @BindView(R.id.gv_crop_format)
    GridView gvCropFormat;
    @BindView(R.id.tv_difficulty)
    TextView tvDifficulty;
    @BindView(R.id.tv_difficulty_content)
    TextView tvDifficultyContent;

    String cropFormat = ContentKey.Format_Array[2];
    boolean isHideName = false;

    String FilePath;
    String tabId1, tabId2, tabId3;
    String tabTitle1, tabTitle2, tabTitle3;

    SelectDialog selectDialog;
    int curSelectType = 0;//默认时间限制
    ArrayList<String> selectTypeList = new ArrayList<>();
    int curSelectCount = 120;//默认120秒
    ArrayList<String> selectCountList = new ArrayList<>();

    SelectCropFormatAdapter selectCropFormatAdapter;
    ArrayList<SelectCropFormatBean> selectCropFormatList = new ArrayList<>();

    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_jstatus);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        FilePath = getIntent().getStringExtra("ImageUri");
        initUI();
        initData();
    }

    private void initUI() {
        tvBarCenter.setText("基本设置");
        tvBarRight.setText("下一步");
        tvBarRight.setVisibility(View.VISIBLE);
        tvLimitType.setText(ContentKey.Limit_Type_Array[curSelectType]);

        for (int i = 0; i < ContentKey.Format_Array.length; i++) {
            SelectCropFormatBean bean = new SelectCropFormatBean();
            bean.setContent(ContentKey.Format_Array[i]);
            if (i == 2) {
                bean.setSelect(true);
            }
            selectCropFormatList.add(bean);
        }
        selectCropFormatAdapter = new SelectCropFormatAdapter(selectCropFormatList, SelectJStatusActivity.this);
        gvCropFormat.setAdapter(selectCropFormatAdapter);

        gvCropFormat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                for (int i = 0; i < selectCropFormatList.size(); i++) {
                    cropFormat = selectCropFormatList.get(position).getContent();
                    if (i == position) {
                        selectCropFormatList.get(i).setSelect(true);
                    } else {
                        selectCropFormatList.get(i).setSelect(false);
                    }
                }
                selectCropFormatAdapter.notifyDataSetChanged();
                updateOddsDegree();
            }
        });
        updateLimitCount();
        updateOddsDegree();
    }

    private void initData() {
        selectTypeList.addAll(Arrays.asList(ContentKey.Limit_Type_Array));
    }

    /**
     * 刷新难度系数
     */
    @SuppressLint("SetTextI18n")
    private void updateOddsDegree() {
        OddsBean oddsBean = OddsUtil.getCurOdds(cropFormat, (curSelectType + 1), curSelectCount);
        if(oddsBean != null){
            tvDifficulty.setText(oddsBean.getDegree() + " *");
            tvDifficultyContent.setText("当其他用户挑战失败时你将得到" + oddsBean.getReward() + "个贝壳");
        }else{
            tvDifficulty.setText("*");
            tvDifficultyContent.setText("");
        }
    }

    /**
     * 刷新限制数值
     */
    @SuppressLint("SetTextI18n")
    private void updateLimitCount() {
        if (curSelectType == 1) {
            tvLimitCount.setText(curSelectCount + "次");
        } else if (curSelectType == 0) {
            tvLimitCount.setText(curSelectCount + "秒");
        }
    }

    private void release() {
        String content = edtContent.getText().toString();

        //将标签前移，确保已经填写的标签处于最前端
        if (TextUtils.isEmpty(tabId1)) {
            if (!TextUtils.isEmpty(tabId2)) {
                tabId1 = tabId2;
                tabTitle1 = tabTitle2;
                tabId2 = "";
                tabTitle2 = "";
            } else {
                tabId1 = tabId3;
                tabTitle1 = tabTitle3;
                tabId3 = "";
                tabTitle3 = "";
            }
        }
        if (TextUtils.isEmpty(tabId2)) {
            if (!TextUtils.isEmpty(tabId3)) {
                tabId2 = tabId3;
                tabTitle2 = tabTitle3;
                tabId3 = "";
                tabTitle3 = "";
            }
        }

        JNoteBean jNoteBean = new JNoteBean();
        jNoteBean.setContent(content);
        jNoteBean.setCropFormat(cropFormat);
        jNoteBean.setJType("" + (curSelectType + 1));//这里+1是为了匹配服务端对于Note类型的定义，服务端type=0表示无限制
        jNoteBean.setHideUser(isHideName);
        jNoteBean.setLimitNum("" + curSelectCount);
        jNoteBean.setResPath(FilePath);
        jNoteBean.setLabel1(tabId1);
        jNoteBean.setLabelTitle1(tabTitle1);
        jNoteBean.setLabel2(tabId2);
        jNoteBean.setLabelTitle2(tabTitle2);
        jNoteBean.setLabel3(tabId3);
        jNoteBean.setLabelTitle3(tabTitle3);

        startActivity(new Intent(SelectJStatusActivity.this, AddJigsawActivity.class)
                .putExtra("jNoteBean", gson.toJson(jNoteBean)));
    }

    @OnClick({R.id.back_btn, R.id.right_btn, R.id.btn_hide_name, R.id.btn_limit_type, R.id.btn_limit_count, R.id.tv_label_1,
            R.id.tv_label_2, R.id.tv_label_3, R.id.iv_tab_close_1, R.id.iv_tab_close_2, R.id.iv_tab_close_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.right_btn:
                if (curSelectCount == 0) {
                    ToastUtil.show(SelectJStatusActivity.this, "请选择限制数值");
                    return;
                }
                if (TextUtils.isEmpty(tabId1) && TextUtils.isEmpty(tabId2) && TextUtils.isEmpty(tabId3)) {
                    ToastUtil.show(SelectJStatusActivity.this, "请添加至少一个标签");
                    return;
                }
                release();
                break;
            case R.id.btn_hide_name:
                isHideName = !isHideName;
                if (isHideName) {
                    ivHideName.setBackgroundResource(R.mipmap.icon_visibility_off_n);
                    tvHideName.setText("匿名");
                } else {
                    ivHideName.setBackgroundResource(R.mipmap.icon_visibility_no);
                    tvHideName.setText("不匿名");
                }
                break;
            case R.id.btn_limit_type:
                selectDialog = new SelectDialog(SelectJStatusActivity.this, selectTypeList, new SelectDialog.OnSelectListener() {
                    @Override
                    public void onItemSelect(View view, int position, long id) {
                        curSelectType = position;
                        curSelectCount = 0;
                        tvLimitType.setText(selectTypeList.get(position));
                        tvLimitCount.setText("选择数值");
                        updateOddsDegree();
                    }
                });
                selectDialog.show();
                break;
            case R.id.btn_limit_count:
                selectCountList.clear();
                if (curSelectType == 1) {
                    selectCountList.addAll(Arrays.asList(ContentKey.Limit_Count_Array));
                } else if (curSelectType == 0) {
                    selectCountList.addAll(Arrays.asList(ContentKey.Limit_Time_Array));
                }
                selectDialog = new SelectDialog(SelectJStatusActivity.this, selectCountList, new SelectDialog.OnSelectListener() {
                    @Override
                    public void onItemSelect(View view, int position, long id) {
                        curSelectCount = Integer.parseInt(selectCountList.get(position));
                        updateLimitCount();
                        updateOddsDegree();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReleaseEvent event) {
        if (event.getEvent() == 0) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
