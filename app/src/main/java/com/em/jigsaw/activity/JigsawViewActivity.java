package com.em.jigsaw.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.em.jigsaw.R;
import com.em.jigsaw.base.ContentKey;
import com.em.jigsaw.base.ServiceAPI;
import com.em.jigsaw.bean.JigsawImgBean;
import com.em.jigsaw.bean.JigsawListBean;
import com.em.jigsaw.callback.OnAlterDialogListener;
import com.em.jigsaw.callback.OnJigsawChangedListener;
import com.em.jigsaw.utils.ImgUtil;
import com.em.jigsaw.utils.ToastUtil;
import com.em.jigsaw.view.AlertDialog;
import com.em.jigsaw.view.JigsawView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JigsawViewActivity extends AppCompatActivity {
    @BindView(R.id.btn_replay)
    ImageView btnReplay;
    @BindView(R.id.tv_limit_start)
    TextView tvLimitStart;
    @BindView(R.id.tv_limit_count)
    TextView tvLimitCount;
    @BindView(R.id.tv_limit_unit)
    TextView tvLimitUnit;
    @BindView(R.id.btn_close)
    ImageView btnClose;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.view_jigsaw)
    JigsawView viewJigsaw;
    @BindView(R.id.iv_content)
    ImageView ivContent;
    @BindView(R.id.view_cneter_bar)
    LinearLayout viewCneterBar;
    @BindView(R.id.tv_top)
    TextView tvTop;

    private ImgUtil imgUtil;
    private Bitmap resBitmap;
    private JigsawListBean jigsawListBean;
    private ArrayList<JigsawImgBean> list = new ArrayList<>();

    private String NoteId;
    private int[] ImgFormat = new int[2];// 宽/高
    private int[] CropFormat = new int[2];// 宽/高

    private int limitType = ContentKey.Limit_Type_None;
    private int baseLimit;
    private int currentLimit;

    private Timer timer = new Timer();
    private boolean fristTouch = true;//是否首次滑动
    private boolean JigsawSuccess = false;//是否拼图成功

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jigsaw_view);
        ButterKnife.bind(this);
        imgUtil = new ImgUtil(JigsawViewActivity.this);

        NoteId = getIntent().getStringExtra("id");

        loadData();
        initUI();
    }

    /**
     * 初始化数据
     */
    private void loadData() {
        OkGo.<String>get(ServiceAPI.GetJDetails).tag(this)
                .params("note_id", NoteId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            if (body.getInt("ResultCode") == ServiceAPI.HttpSuccess) {
                                JSONObject obj = body.getJSONObject("ResultData");

                                jigsawListBean = new JigsawListBean();

                                jigsawListBean.setBestResults(obj.getString("BestResults"));
                                jigsawListBean.setCompleteNum(obj.getString("CompleteNum"));
                                jigsawListBean.setContent(obj.getString("Content"));
                                jigsawListBean.setCreatTime(obj.getLong("CreatTime"));
                                jigsawListBean.setCropFormat(obj.getString("CropFormat"));
                                jigsawListBean.setDisplayNum(obj.getString("DisplayNum"));
                                jigsawListBean.setHideUser(obj.getBoolean("HideUser"));
                                jigsawListBean.setJType(obj.getString("JType"));
                                jigsawListBean.setLabel1(obj.getString("Label1"));
                                jigsawListBean.setLabel2(obj.getString("Label2"));
                                jigsawListBean.setLabel3(obj.getString("Label3"));
                                jigsawListBean.setLabelTitle1(obj.getString("LabelTitle1"));
                                jigsawListBean.setLabelTitle2(obj.getString("LabelTitle2"));
                                jigsawListBean.setLabelTitle3(obj.getString("LabelTitle3"));
                                jigsawListBean.setLimitNum(obj.getString("LimitNum"));
                                jigsawListBean.setNoteId(obj.getString("NoteId"));
                                jigsawListBean.setResPath(obj.getString("ResPath"));

                                JSONObject userObj = obj.getJSONObject("Releaser");
                                jigsawListBean.setUserHead(userObj.getString("NameHead"));
                                jigsawListBean.setUserName(userObj.getString("UserName"));
                                jigsawListBean.setUserNo(userObj.getString("UserNo"));

                                String[] cropFormatArray = jigsawListBean.getCropFormat().split("-");
                                for(int i = 0;i < cropFormatArray.length;i++){
                                    CropFormat[i] = Integer.parseInt(cropFormatArray[i]);
                                }

                                limitType = Integer.parseInt(jigsawListBean.getJType());
                                baseLimit = Integer.parseInt(jigsawListBean.getLimitNum());

                                Glide.get(JigsawViewActivity.this).clearMemory();
                                Glide.with(JigsawViewActivity.this).load(jigsawListBean.getResPath()).into(new SimpleTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        resBitmap = ((BitmapDrawable) resource).getBitmap();
                                        ImgFormat[0] = resBitmap.getWidth();
                                        ImgFormat[1] = resBitmap.getHeight();

                                        ivContent.setImageBitmap(resBitmap);
                                        tvContent.setText(jigsawListBean.getContent());
                                        if(!jigsawListBean.getBestResults().equals("-1")){
                                            tvTop.setText("当前最佳：" + jigsawListBean.getContent());
                                        }
                                        updateJigsawList();
                                        updateLimitStatus();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        viewJigsaw.setOnChangedListener(new OnJigsawChangedListener() {
            @Override
            public void onChanged(ArrayList<JigsawImgBean> arrayList) {
                if (imgUtil.jigsawSuccess(arrayList)) {
                    JigsawSuccess = true;
                    viewJigsaw.setViewTouched(false);

                    ivContent.setVisibility(View.VISIBLE);
                    viewJigsaw.setVisibility(View.GONE);
                    ToastUtil.show(JigsawViewActivity.this, "恭喜你~");
                }

                if (limitType == ContentKey.Limit_Type_Count) {
                    currentLimit--;
                    tvLimitCount.setText("" + (currentLimit < 0 ? 0 : currentLimit));

                    if (currentLimit <= 0) {
                        viewJigsaw.setViewTouched(false);
                    }
                } else if (limitType == ContentKey.Limit_Type_Timer) {
                    if (fristTouch) {
                        fristTouch = false;
                        timer.schedule(timerTask, 0, 1000);
                    }
                }
            }
        });
    }

    /**
     * 刷新UI
     */
    private void updateJigsawList() {
        list.clear();
        list.addAll(imgUtil.sortImgArray(imgUtil.getImgArray(resBitmap, CropFormat, ImgFormat)));
        viewJigsaw.setLabels(list);
    }

    /**
     * 刷新限制状态
     */
    private void updateLimitStatus() {
        JigsawSuccess = false;
        viewJigsaw.setViewTouched(true);
        viewJigsaw.setVisibility(View.VISIBLE);
        ivContent.setVisibility(View.GONE);

        switch (limitType) {
            case ContentKey.Limit_Type_None:
                tvLimitStart.setText("无限制");
                break;
            case ContentKey.Limit_Type_Count:
                tvLimitStart.setText("剩余");
                tvLimitUnit.setText("次");
                currentLimit = baseLimit;
                tvLimitCount.setText("" + currentLimit);
                break;
            case ContentKey.Limit_Type_Timer:
                tvLimitStart.setText("剩余");
                tvLimitUnit.setText("秒");
                currentLimit = baseLimit;
                tvLimitCount.setText("" + currentLimit);
                break;
        }
    }

    /**
     * 倒计时相关
     */
    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    tvLimitCount.setText("" + (currentLimit < 0 ? 0 : currentLimit));
                    if (currentLimit == 0) {
                        viewJigsaw.setViewTouched(false);
                    }
            }
            super.handleMessage(msg);
        }
    };
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (currentLimit > 0 && !JigsawSuccess) {
                currentLimit--;
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }
    };

    @OnClick({R.id.btn_replay, R.id.btn_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_replay:
                showDialog("重新开始？", 1);
                break;
            case R.id.btn_close:
                showDialog("退出当前页面？", 0);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            showDialog("退出当前页面？", 0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showDialog(String content, final int type) {
        alertDialog = new AlertDialog(JigsawViewActivity.this, content,
                "确定", new OnAlterDialogListener() {
            @Override
            public void onRightClick() {
                switch (type) {
                    case 0:
                        finish();
                        break;
                    case 1:
                        updateLimitStatus();
                        updateJigsawList();
                        break;
                }
            }

            @Override
            public void onLeftClick() {
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }
    }
}
