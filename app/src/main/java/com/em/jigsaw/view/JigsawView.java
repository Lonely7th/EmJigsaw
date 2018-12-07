package com.em.jigsaw.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Time ： 2018/12/6 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class JigsawView extends GridView{
    private static final String TAG = "JigsawView";

    private Context mContext;

    public JigsawView(Context context) {
        super(context);
        mContext = context;
    }

    public JigsawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public JigsawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }
}
