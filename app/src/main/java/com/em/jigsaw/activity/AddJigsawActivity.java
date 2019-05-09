package com.em.jigsaw.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.adapter.FormatSelectAdapter;
import com.em.jigsaw.base.ContentKey;
import com.em.jigsaw.base.ServiceAPI;
import com.em.jigsaw.bean.FormatSelectBean;
import com.em.jigsaw.bean.JNoteBean;
import com.em.jigsaw.bean.JigsawImgBean;
import com.em.jigsaw.bean.event.ReleaseEvent;
import com.em.jigsaw.utils.ImgUtil;
import com.em.jigsaw.utils.LoginUtil;
import com.em.jigsaw.utils.SignUtil;
import com.em.jigsaw.utils.ToastUtil;
import com.em.jigsaw.view.JigsawView;
import com.em.jigsaw.view.dialog.LoadingDialog;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddJigsawActivity extends AppCompatActivity {

    @BindView(R.id.back_btn)
    RelativeLayout backBtn;
    @BindView(R.id.tv_bar_center)
    TextView tvBarCenter;
    @BindView(R.id.tv_bar_right)
    TextView tvBarRight;
    @BindView(R.id.right_btn)
    RelativeLayout rightBtn;
    @BindView(R.id.view_jigsaw)
    JigsawView viewJigsaw;
    @BindView(R.id.gv_format_select)
    GridView gvFormatSelect;

    private File ResFile;
    private ImgUtil imgUtil;
    private JNoteBean jNoteBean;
    private boolean fristLoad = true;

    private ArrayList<JigsawImgBean> list = new ArrayList<>();

    private int currentFormat = 0;
    private FormatSelectAdapter formatSelectAdapter;
    private List<FormatSelectBean> formatSelectBeans = new ArrayList<>();

    private LoadingDialog loadingDialog;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jigsaw_view);
        ButterKnife.bind(this);
        imgUtil = new ImgUtil(AddJigsawActivity.this);
        jNoteBean = gson.fromJson(getIntent().getStringExtra("jNoteBean"),JNoteBean.class);

        initData();
        initUI();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(fristLoad){
            fristLoad = false;
            updateJigsawList(imgUtil.getBitmap(jNoteBean.getResPath()));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    private void initData() {
        //初始化裁剪格式数据
        for(int i = 0;i < ContentKey.Format_Array.length;i++){
            FormatSelectBean formatSelectBean = new FormatSelectBean();
            formatSelectBean.setID(""+i);
            formatSelectBean.setTitle(ContentKey.Format_Array[i]);
            if(i == currentFormat){
                formatSelectBean.setSelect(true);
            }
            formatSelectBeans.add(formatSelectBean);
        }
    }

    private void initUI() {
        tvBarCenter.setText("预览");
        tvBarRight.setText("发布");
        tvBarRight.setVisibility(View.VISIBLE);

        formatSelectAdapter = new FormatSelectAdapter(formatSelectBeans,AddJigsawActivity.this);
        gvFormatSelect.setAdapter(formatSelectAdapter);
        gvFormatSelect.setNumColumns(ContentKey.Format_Array.length);
        gvFormatSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentFormat = position;
                for(int i = 0;i < formatSelectBeans.size();i++){
                    if(i == currentFormat){
                        formatSelectBeans.get(i).setSelect(true);
                    }else{
                        formatSelectBeans.get(i).setSelect(false);
                    }
                }
                formatSelectAdapter.notifyDataSetChanged();
                updateJigsawList(imgUtil.getBitmap(jNoteBean.getResPath()));
            }
        });

        viewJigsaw.setViewTouched(false);
    }

    /**
     * 发布
     */
    private void release() {
        loadingDialog = new LoadingDialog(AddJigsawActivity.this);
        loadingDialog.show();
        try {
            ResFile = new File(jNoteBean.getResPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkGo.<String>post(ServiceAPI.AddJDetails).tag(this)
                .params("content", jNoteBean.getContent())
                .params("releaser", LoginUtil.getUserInfo().getUserNo())
                .params("res", ResFile)
                .params("jtype", jNoteBean.getJType())
                .params("limitNum", "" + jNoteBean.getLimitNum())
                .params("hideUser", jNoteBean.getUserHead())
                .params("cropFormat", jNoteBean.getCropFormat())
                .params("label1", jNoteBean.getLabel1())
                .params("labelTitle1", jNoteBean.getLabelTitle1())
                .params("label2", jNoteBean.getLabel2())
                .params("labelTitle2", jNoteBean.getLabelTitle2())
                .params("label3", jNoteBean.getLabel3())
                .params("labelTitle3", jNoteBean.getLabelTitle3())
                .params(SignUtil.getParams(true))
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
                                ToastUtil.show(AddJigsawActivity.this, "发布成功");
                                startActivity(new Intent(AddJigsawActivity.this, JigsawViewActivity.class)
                                        .putExtra("id", ResultData.getString("NoteId")));
                                EventBus.getDefault().post(new ReleaseEvent(0));
                                finish();
                            } else {
                                ToastUtil.show(AddJigsawActivity.this, "网络异常");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtil.show(AddJigsawActivity.this, "网络异常");
                    }
                });
    }

    /**
     * 刷新jigsawAdapter
     */
    private void updateJigsawList(Bitmap bitmap) {
        list.clear();
        list.addAll(imgUtil.sortImgArray(imgUtil.getImgArray(bitmap, imgUtil.getCropFormatByFlag(jNoteBean.getCropFormat()), ContentKey.ImgFormat_9_16)));
        viewJigsaw.setLabels(list);
    }

    @OnClick({R.id.back_btn, R.id.right_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.right_btn:
                release();
                break;
        }
    }
}
