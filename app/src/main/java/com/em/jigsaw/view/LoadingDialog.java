package com.em.jigsaw.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.em.jigsaw.R;

/**
 * Time ： 2018/12/18 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class LoadingDialog extends Dialog {

    ImageView spaceshipImage;
    Context context;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout layout = v.findViewById(R.id.dialog_view);// 加载布局
        spaceshipImage = v.findViewById(R.id.img_loading);
        TextView tipTextView = v.findViewById(R.id.tv_loading);// 提示文字
        tipTextView.setText("加载中...");// 设置加载信息

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){      //解决7.0dialog的问题
            getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        }

        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
    }

    @Override
    public void show() {
        super.show();
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_animation); // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
    }
}
