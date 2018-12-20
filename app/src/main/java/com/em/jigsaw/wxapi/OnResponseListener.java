package com.em.jigsaw.wxapi;

/**
 * Time ： 2018/12/20 .
 * Author ： JN Zhang .
 * Description ： .
 */
public interface OnResponseListener {
    void onSuccess();
    void onCancel();
    void onFail(String message);
}
