package com.em.jigsaw.callback;

import com.em.jigsaw.bean.JigsawImgBean;

import java.util.ArrayList;

/**
 * Time ： 2018/12/13 .
 * Author ： JN Zhang .
 * Description ： .
 */
public interface OnJigsawChangedListener {
    void onChanged(ArrayList<JigsawImgBean> arrayList);
}
