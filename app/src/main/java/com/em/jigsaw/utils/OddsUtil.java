package com.em.jigsaw.utils;

import android.text.TextUtils;

import com.em.jigsaw.bean.OddsBean;

/**
 * Time ： 2019/5/17 .
 * Author ： JN Zhang .
 * Description ：.
 */
public class OddsUtil {

    public static OddsBean getCurOdds(String cropFormat, int nType, int nCount){
        if(TextUtils.isEmpty(cropFormat) || nType == 0 || nCount == 0){
            return null;
        }
        OddsBean oddsBean = new OddsBean();
        oddsBean.setDegree("1.4");
        oddsBean.setReward("3");
        return oddsBean;
    }

}
