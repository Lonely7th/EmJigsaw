package com.em.jigsaw.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.adapter.SelectDialogAdapter;

/**
 * Time ： 2018/12/18 .
 * Author ： JN Zhang .
 * Description ：等待弹窗 .
 */
public class LoadingDialog extends Dialog {

    private Context context;

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.LoadingDialogStyle);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);

        setCanceledOnTouchOutside(false);
        getWindow().setGravity(Gravity.CENTER);
    }
}
