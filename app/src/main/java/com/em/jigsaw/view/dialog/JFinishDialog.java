package com.em.jigsaw.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.bean.JNoteBean;

/**
 * Time ： 2019/1/4 .
 * Author ： JN Zhang .
 * Description ：结果展示弹窗 .
 */
public class JFinishDialog extends Dialog {
    private Context context;
    private OnFinishDialogListener onFinishDialogListener = null;

    private TextView tvScore,tvLabel,tvContent,tvBest,tvStatus;
    private ImageView ivScore;
    private JNoteBean JNoteBean;
    private int currentScore;

    public JFinishDialog(Context context) {
        super(context);
        this.context = context;
    }

    public JFinishDialog(Context context, JNoteBean JNoteBean,int currentScore, @NonNull final OnFinishDialogListener onFinishDialogListener) {
        super(context, R.style.AlertDialogStyle);
        this.context = context;
        this.JNoteBean = JNoteBean;
        this.currentScore = currentScore;
        this.onFinishDialogListener = onFinishDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_finish);
        tvScore = findViewById(R.id.tv_score);
        tvStatus = findViewById(R.id.tv_status);
        tvBest = findViewById(R.id.tv_best);
        tvLabel = findViewById(R.id.tv_label);
        tvContent = findViewById(R.id.tv_content);
        ivScore = findViewById(R.id.iv_score);

        setCanceledOnTouchOutside(false);
        getWindow().setGravity(Gravity.CENTER);

        initUI();
    }

    private void initUI(){
        int numScore = Integer.parseInt(JNoteBean.getLimitNum());
        int score = numScore - currentScore;
        if(score <= 0){ // C =0%
            tvScore.setTextColor(context.getResources().getColor(R.color.scoreC));
            tvStatus.setTextColor(context.getResources().getColor(R.color.scoreC));
            tvStatus.setText("(未完成)");
            ivScore.setImageDrawable(context.getResources().getDrawable(R.mipmap.score_c));
        }else if(score < numScore*0.2){ // B <20%
            tvScore.setTextColor(context.getResources().getColor(R.color.scoreB));
            tvStatus.setTextColor(context.getResources().getColor(R.color.scoreB));
            ivScore.setImageDrawable(context.getResources().getDrawable(R.mipmap.score_b));
        }else if(score < numScore*0.4){ // A 40%
            tvScore.setTextColor(context.getResources().getColor(R.color.scoreA));
            tvStatus.setTextColor(context.getResources().getColor(R.color.scoreA));
            ivScore.setImageDrawable(context.getResources().getDrawable(R.mipmap.score_a));
        }else if(score < numScore*0.6){ // S 60%
            tvScore.setTextColor(context.getResources().getColor(R.color.scoreS));
            tvStatus.setTextColor(context.getResources().getColor(R.color.scoreS));
            ivScore.setImageDrawable(context.getResources().getDrawable(R.mipmap.score_s));
        }else if(score < numScore*0.8){ // SS 80%
            tvScore.setTextColor(context.getResources().getColor(R.color.scoreS));
            tvStatus.setTextColor(context.getResources().getColor(R.color.scoreS));
            ivScore.setImageDrawable(context.getResources().getDrawable(R.mipmap.score_ss));
        }else{ // SSS >80%
            tvScore.setTextColor(context.getResources().getColor(R.color.scoreS));
            tvStatus.setTextColor(context.getResources().getColor(R.color.scoreS));
            ivScore.setImageDrawable(context.getResources().getDrawable(R.mipmap.score_sss));
        }

        tvBest.setText("·  本次成绩：" + currentScore + "  当前最佳：" + JNoteBean.getBestResults());
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
        tvLabel.setText(stringBuilderLabel.toString());
        tvContent.setText("简介：" + JNoteBean.getContent());
        findViewById(R.id.btn_pos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                onFinishDialogListener.onCloseDialog();
            }
        });
    }

    public interface OnFinishDialogListener{
        void onCloseDialog();
        void onClosePager();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
