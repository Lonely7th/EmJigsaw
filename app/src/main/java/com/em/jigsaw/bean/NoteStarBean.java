package com.em.jigsaw.bean;

/**
 * Time ： 2019/1/4 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class NoteStarBean {
    private String StarNo;
    private String UserNo;
    private String CreatTime;
    private JNoteBean jNoteBean;

    public String getStarNo() {
        return StarNo;
    }

    public void setStarNo(String starNo) {
        StarNo = starNo;
    }

    public String getUserNo() {
        return UserNo;
    }

    public void setUserNo(String userNo) {
        UserNo = userNo;
    }

    public String getCreatTime() {
        return CreatTime;
    }

    public void setCreatTime(String creatTime) {
        CreatTime = creatTime;
    }

    public JNoteBean getjNoteBean() {
        return jNoteBean;
    }

    public void setjNoteBean(JNoteBean jNoteBean) {
        this.jNoteBean = jNoteBean;
    }
}
