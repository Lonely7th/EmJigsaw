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

    private ImageView spaceshipImage;
    private Context context;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);

        spaceshipImage = findViewById(R.id.img_loading);

        setCanceledOnTouchOutside(false);
        getWindow().setGravity(Gravity.CENTER);

//        WindowManager windowManager = (WindowManager) context
//                .getSystemService(Context.WINDOW_SERVICE);
//        display = windowManager.getDefaultDisplay();
//        findViewById(R.id.bg_dialog_select).setLayoutParams(new FrameLayout.LayoutParams((int) (display
//                .getWidth() * 1.0), LinearLayout.LayoutParams.WRAP_CONTENT));

    }

    @Override
    public void show() {
        super.show();
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_animation); // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
    }
}
