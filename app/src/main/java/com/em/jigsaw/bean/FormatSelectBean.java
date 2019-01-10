package com.em.jigsaw.bean;

import com.em.jigsaw.base.ContentKey;

/**
 * Time ： 2018/12/18 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class FormatSelectBean {
    private String ID;
    private String Title;
    private boolean isSelect = false;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int[] getFormat() {
        int[] formatArray = null;
        switch (getTitle()){
            case "3-3":
                formatArray = ContentKey.Format_3_3;
                break;
            case "4-3":
                formatArray = ContentKey.Format_4_3;
                break;
            case "4-4":
                formatArray = ContentKey.Format_4_4;
                break;
            case "6-4":
                formatArray = ContentKey.Format_6_4;
                break;
            case "6-6":
                formatArray = ContentKey.Format_6_6;
                break;
        }
        return formatArray;
    }
}
