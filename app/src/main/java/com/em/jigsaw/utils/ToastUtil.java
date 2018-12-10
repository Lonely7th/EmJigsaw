package com.em.jigsaw.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Time ： 2018/12/6 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class ToastUtil {
    /**
     * Toast显示
     */
    public static void show(final Context context,final String string) {
        MainThreadDelivery.getInstance().deliver(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 在主线程执行Runnable
     */
    static class MainThreadDelivery {
        private final Handler handler = new Handler(Looper.getMainLooper());
        private static MainThreadDelivery mDelivery = new MainThreadDelivery();

        static MainThreadDelivery getInstance() {
            return mDelivery;
        }

        /**
         * 主线程执行Runnable
         */
        void deliver(Runnable r) {
            handler.post(r);
        }
    }
}
