package com.em.jigsaw.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.em.jigsaw.bean.JNoteBean;
import com.em.jigsaw.bean.JigsawImgBean;
import com.em.jigsaw.callback.OnAlterDialogListener;
import com.em.jigsaw.callback.OnJigsawChangedListener;
import com.em.jigsaw.utils.ImgUtil;
import com.em.jigsaw.utils.LoginUtil;
import com.em.jigsaw.utils.SignUtil;
import com.em.jigsaw.utils.ToastUtil;
import com.em.jigsaw.view.JigsawView;
import com.em.jigsaw.view.dialog.AlertDialog;
import com.em.jigsaw.view.dialog.JFinishDialog;
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
    @BindView(R.id.btn_star)
    ImageView btnStar;
    @BindView(R.id.tv_more)
    TextView tvMore;

    private ImgUtil imgUtil;
    private Bitmap resBitmap;
    private JNoteBean JNoteBean;
    private ArrayList<JigsawImgBean> list = new ArrayList<>();

    private String NoteId;
    private int[] ImgFormat = new int[2];// 宽/高
    private int[] CropFormat = new int[2];// 宽/高

    private int limitType = ContentKey.Limit_Type_None;
    private int baseLimit;
    private int currentLimit;

    private Timer timer = new Timer();
    private boolean fristTouch = true;// 是否首次滑动
    private boolean JigsawSuccess = false;// 是否拼图成功

    private AlertDialog alertDialog;
    private JFinishDialog jFinishDialog;

    private boolean isFavorite = false;// 是否正在处理收藏相关的操作

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
                .params("user_id", LoginUtil.getUserInfo().getUserNo())
                .params(SignUtil.getParams(false))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            if (body.getInt("ResultCode") == ServiceAPI.HttpSuccess) {
                                JSONObject obj = body.getJSONObject("ResultData");

                                JNoteBean = new JNoteBean();

                                JNoteBean.setBestResults(obj.getString("BestResults"));
                                JNoteBean.setCompleteNum(obj.getString("CompleteNum"));
                                JNoteBean.setContent(obj.getString("Content"));
                                JNoteBean.setCreatTime(obj.getLong("CreatTime"));
                                JNoteBean.setCropFormat(obj.getString("CropFormat"));
                                JNoteBean.setDisplayNum(obj.getString("DisplayNum"));
                                JNoteBean.setHideUser(obj.getBoolean("HideUser"));
                                JNoteBean.setJType(obj.getString("JType"));
                                JNoteBean.setLabel1(obj.getString("Label1"));
                                JNoteBean.setLabel2(obj.getString("Label2"));
                                JNoteBean.setLabel3(obj.getString("Label3"));
                                JNoteBean.setLabelTitle1(obj.getString("LabelTitle1"));
                                JNoteBean.setLabelTitle2(obj.getString("LabelTitle2"));
                                JNoteBean.setLabelTitle3(obj.getString("LabelTitle3"));
                                JNoteBean.setLimitNum(obj.getString("LimitNum"));
                                JNoteBean.setNoteId(obj.getString("NoteId"));
                                JNoteBean.setResPath(obj.getString("ResPath"));
                                JNoteBean.setFavoriteId(obj.getInt("FavoriteId"));

                                JSONObject userObj = obj.getJSONObject("Releaser");
                                JNoteBean.setUserHead(userObj.getString("NameHead"));
                                JNoteBean.setUserName(userObj.getString("UserName"));
                                JNoteBean.setUserNo(userObj.getString("UserNo"));

                                String[] cropFormatArray = JNoteBean.getCropFormat().split("-");
                                for (int i = 0; i < cropFormatArray.length; i++) {
                                    CropFormat[i] = Integer.parseInt(cropFormatArray[i]);
                                }

                                StringBuilder stringBuilderLabel = new StringBuilder("标签：");
                                if(!TextUtils.isEmpty(JNoteBean.getLabelTitle1())){
                                    stringBuilderLabel.append("#").append(JNoteBean.getLabelTitle1());
                                }
                                if(!TextUtils.isEmpty(JNoteBean.getLabelTitle2())){
                                    stringBuilderLabel.append("#").append(JNoteBean.getLabelTitle2());
                                }
                                if(!TextUtils.isEmpty(JNoteBean.getLabelTitle3())){
                                    stringBuilderLabel.append("#").append(JNoteBean.getLabelTitle3());
                                }
                                tvMore.setText(stringBuilderLabel.toString());

                                limitType = Integer.parseInt(JNoteBean.getJType());
                                baseLimit = Integer.parseInt(JNoteBean.getLimitNum());

                                Glide.get(JigsawViewActivity.this).clearMemory();
                                Glide.with(JigsawViewActivity.this).load(JNoteBean.getResPath()).into(new SimpleTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        resBitmap = ((BitmapDrawable) resource).getBitmap();
                                        ImgFormat[0] = resBitmap.getWidth();
                                        ImgFormat[1] = resBitmap.getHeight();

                                        ivContent.setImageBitmap(resBitmap);
                                        tvContent.setText(JNoteBean.getContent());
                                        StringBuilder sb = new StringBuilder();
                                        if (!JNoteBean.getJType().equals("0")) {
                                            sb.append("当前最佳：").append(JNoteBean.getBestResults()).append(JNoteBean.getJType().equals("1")?"秒":"次");
                                        } else {
                                            sb.append("当前最佳：-");
                                        }
                                        tvTop.setText(sb.toString());
                                        updateJigsawList();
                                        updateLimitStatus();
                                    }
                                });

                                updateFavoriteStatus();
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
                if (imgUtil.jigsawSuccess(arrayList)) { // 完成拼图
                    JigsawSuccess = true;
                    viewJigsaw.setViewTouched(false);
                    int currentScore = baseLimit - currentLimit;

                    jFinishDialog = new JFinishDialog(JigsawViewActivity.this, JNoteBean,
                            JNoteBean.getJType().equals("0")?0:currentScore, new JFinishDialog.OnFinishDialogListener() {
                        @Override
                        public void onCloseDialog() {

                        }

                        @Override
                        public void onClosePager() {
                            finish();
                        }
                    });
                    jFinishDialog.show();

                    ivContent.setVisibility(View.VISIBLE);
                    viewJigsaw.setVisibility(View.GONE);

                    if(!JNoteBean.getJType().equals("0")){ // 如果有时间/次数限制则提交结果
                        postJResult(currentScore<Integer.parseInt(JNoteBean.getBestResults())?2:1,currentScore);
                    }
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
     * 刷新收藏状态
     */
    private void updateFavoriteStatus() {
        if(JNoteBean.getFavoriteId() == 1){
            btnStar.setImageDrawable(getResources().getDrawable(R.mipmap.icon_favorite));
        }else{
            btnStar.setImageDrawable(getResources().getDrawable(R.mipmap.icon_favorite_border));
        }
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
                tvLimitCount.setText("--");
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

    /**
     * 收藏相关
     */
    private void starNote(){
        if(isFavorite){
            return ;
        }
        if(!LoginUtil.isLogin()){
            ToastUtil.show(JigsawViewActivity.this,"未登录");
            return ;
        }
        isFavorite = true;

        OkGo.<String>post(ServiceAPI.StarJNote).tag(this)
                .params("user_no", LoginUtil.getUserInfo().getUserNo())
                .params("note_id", JNoteBean.getNoteId())
                .params("type", JNoteBean.getFavoriteId()==1?"1":"0")
                .params(SignUtil.getParams(true))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            if(body.getInt("ResultCode") == ServiceAPI.HttpSuccess){
                                if(JNoteBean.getFavoriteId()==1){
                                    ToastUtil.show(JigsawViewActivity.this,"收藏取消");
                                    JNoteBean.setFavoriteId(0);
                                }else{
                                    ToastUtil.show(JigsawViewActivity.this,"收藏成功");
                                    JNoteBean.setFavoriteId(1);
                                }
                                updateFavoriteStatus();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<String> response) {
                        super.onError(response);
                        ToastUtil.show(JigsawViewActivity.this,"网络异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        isFavorite = false;
                    }
                });
    }

    /**
     * 提交结果
     */
    private void postJResult(final int status,final int score){
        OkGo.<String>post(ServiceAPI.PostJResult).tag(this)
                .params("user_no", LoginUtil.getUserInfo().getUserNo())
                .params("note_id", JNoteBean.getNoteId())
                .params("status", status)
                .params("score", score)
                .params(SignUtil.getParams(true))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            if(body.getInt("ResultCode") == ServiceAPI.HttpSuccess){ }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<String> response) {
                        super.onError(response);
                        ToastUtil.show(JigsawViewActivity.this,"网络异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @OnClick({R.id.btn_replay, R.id.btn_close, R.id.btn_star, R.id.tv_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_replay:
                showDialog("重新开始？", 1);
                break;
            case R.id.btn_close:
                if(JigsawSuccess){
                    finish();
                }else{
                    showDialog("退出当前页面？", 0);
                }
                break;
            case R.id.btn_star:
                starNote();
                break;
            case R.id.tv_more:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(JigsawSuccess){
            finish();
        }else{
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                showDialog("退出当前页？", 0);
                return true;
            }
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
        if(jFinishDialog != null){
            jFinishDialog.dismiss();
        }
        if(alertDialog != null){
            alertDialog.dismiss();
        }
    }
}
