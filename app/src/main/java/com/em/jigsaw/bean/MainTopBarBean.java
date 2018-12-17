package com.em.jigsaw.bean;

/**
 * Time ： 2018/12/17 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class MainTopBarBean {
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
}
