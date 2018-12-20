package com.em.jigsaw.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.base.ContentKey;
import com.em.jigsaw.bean.JigsawImgBean;
import com.em.jigsaw.callback.OnAlterDialogListener;
import com.em.jigsaw.callback.OnJigsawChangedListener;
import com.em.jigsaw.utils.ImgUtil;
import com.em.jigsaw.utils.ToastUtil;
import com.em.jigsaw.view.AlertDialog;
import com.em.jigsaw.view.JigsawView;

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

    private boolean initWindow = true;
    private ImgUtil imgUtil;
    private Uri imageUri;
    private ArrayList<JigsawImgBean> list = new ArrayList<>();

    private int[] ImgFormat;
    private int[] CropFormat = ContentKey.Format_6_4;

    private int limitType = ContentKey.Limit_Type_Timer;
    private int limitCount = 90;//次数限制
    private int limitTimer = 300;//时间限制
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

        imageUri = Uri.parse(getIntent().getStringExtra("ImageUri"));
        ImgFormat = getIntent().getIntArrayExtra("ImgFormat");

        initData();
        initUI();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(initWindow){
            initWindow = false;
            updateJigsawList(imgUtil.getBitmap(imageUri));
        }
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
        ivContent.setImageURI(imageUri);
        updateLimitStatus();

        viewJigsaw.setOnChangedListener(new OnJigsawChangedListener() {
            @Override
            public void onChanged(ArrayList<JigsawImgBean> arrayList) {
                if (imgUtil.jigsawSuccess(arrayList)) {
                    JigsawSuccess = true;
                    viewJigsaw.setViewTouched(false);

//                    ivContent.setVisibility(View.VISIBLE);
//                    viewJigsaw.setVisibility(View.GONE);
                    ToastUtil.show(JigsawViewActivity.this,"恭喜你~");
                }

                if(limitType == ContentKey.Limit_Type_Count){
                    currentLimit--;
                    tvLimitCount.setText(""+(currentLimit<0?0:currentLimit));

                    if(currentLimit <= 0){
                        viewJigsaw.setViewTouched(false);
                    }
                }else if(limitType == ContentKey.Limit_Type_Timer){
                    if(fristTouch){
                        fristTouch = false;
                        timer.schedule(timerTask, 0, 1000);
                    }
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

    /**
     * 刷新限制状态
     */
    private void updateLimitStatus(){
        JigsawSuccess = false;
        viewJigsaw.setViewTouched(true);

        switch (limitType){
            case ContentKey.Limit_Type_None:
                tvLimitStart.setVisibility(View.GONE);
                tvLimitCount.setVisibility(View.GONE);
                tvLimitUnit.setVisibility(View.GONE);
                break;
            case ContentKey.Limit_Type_Count:
                tvLimitStart.setText("剩余");
                tvLimitUnit.setText("次");
                currentLimit = limitCount;
                break;
            case ContentKey.Limit_Type_Timer:
                tvLimitStart.setText("剩余");
                tvLimitUnit.setText("秒");
                currentLimit = limitTimer;
                break;
        }
        tvLimitCount.setText(""+currentLimit);
    }

    /**
     * 倒计时相关
     */
    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    tvLimitCount.setText(""+(currentLimit<0?0:currentLimit));
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
                showDialog("重新开始？");
                break;
            case R.id.btn_close:
                showDialog("退出当前页面？");
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            showDialog("退出当前页面？");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showDialog(String content){
        alertDialog = new AlertDialog(JigsawViewActivity.this, content,
                "确定", new OnAlterDialogListener() {
            @Override
            public void onRightClick() {
                finish();
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
        if(timerTask != null){
            timerTask.cancel();
        }
    }
}
