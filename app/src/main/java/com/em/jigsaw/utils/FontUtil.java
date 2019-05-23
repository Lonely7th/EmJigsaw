package com.em.jigsaw.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * Time ： 2019/5/22 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class FontUtil {

    /**
     * 设置文字高亮显示
     */
    public static void setHighlightTextView(String content, String key, TextView view, int color){
        int start = content.indexOf(key);
        int end = start + key.length();
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        style.setSpan(new ForegroundColorSpan(color),start,end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        view.setText(style);
    }

}
