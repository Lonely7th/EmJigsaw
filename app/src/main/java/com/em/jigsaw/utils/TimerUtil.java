package com.em.jigsaw.utils;


import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Time ： 2018/12/28 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class TimerUtil {

    public static String timeStamp2Date(long time) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        res = simpleDateFormat.format(date);
        return res;
    }
}
