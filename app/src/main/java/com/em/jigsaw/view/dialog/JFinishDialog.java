package com.em.jigsaw.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

    private TextView tvScore,tvLabel,tvContent;
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
        tvLabel = findViewById(R.id.tv_label);
        tvContent = findViewById(R.id.tv_content);
        ivScore = findViewById(R.id.iv_score);

        setCanceledOnTouchOutside(false);
        getWindow().setGravity(Gravity.CENTER);

        findViewById(R.id.btn_neg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                onFinishDialogListener.onClosePager();
            }
        });
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
