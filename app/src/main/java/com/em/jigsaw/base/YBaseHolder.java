package com.em.jigsaw.base;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * Time ： 2018/5/7 .
 * Author ： JN Zhang .
 * Description ： Holder基类.
 */

public abstract class YBaseHolder<T> {
    public View holderView;
    public List<T> mLists;
    public Context mContext;

    public YBaseHolder(Context mContext, List<T> mLists) {
        this.mLists = mLists;
        this.mContext = mContext;
        holderView = getInflateView(mContext);
    }

    public abstract View getInflateView(Context mContext);

    public abstract void bindData(int position);
}
