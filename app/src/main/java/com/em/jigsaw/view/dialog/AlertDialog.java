package com.em.jigsaw.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.adapter.SelectDialogAdapter;
import com.em.jigsaw.callback.OnAlterDialogListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Time ： 2018/12/19 .
 * Author ： JN Zhang .
 * Description ：提示弹窗 .
 */
public class AlertDialog extends Dialog {
    private Context context;
    private TextView tvContent;
    private Button btnRight,btnLeft;
    private OnAlterDialogListener onAlterDialogListener = null;

    private String strContent,strButton;

    public AlertDialog(Context context) {
        super(context);
        this.context = context;
    }

    public AlertDialog(Context context, String strContent,String strButton,@NonNull final OnAlterDialogListener onAlterDialogListener) {
        super(context, R.style.AlertDialogStyle);
        this.context = context;
        this.strContent = strContent;
        this.strButton = strButton;
        this.onAlterDialogListener = onAlterDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_alert);
        tvContent = findViewById(R.id.tv_content);
        btnRight = findViewById(R.id.btn_pos);
        btnLeft = findViewById(R.id.btn_neg);

        tvContent.setText(strContent);
        btnRight.setText(strButton);
        btnLeft.setText("取消");

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAlterDialogListener.onRightClick();
                dismiss();
            }
        });
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAlterDialogListener.onLeftClick();
                dismiss();
            }
        });

        setCanceledOnTouchOutside(false);
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
