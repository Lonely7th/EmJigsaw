package com.em.jigsaw.bean;

/**
 * Time ： 2018/12/15 0015 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class JNoteBean {
    private String NoteId;
    private String Content;
    private String UserNo;
    private String UserName;
    private String UserHead;
    private long CreatTime;
    private String ResPath = "";
    private String GsResPath = "";
    private String DisplayNum;
    private String CompleteNum;
    private String JType;
    private String LimitNum;
    private String BestResults;
    private String Label1;
    private String LabelTitle1;
    private String Label2;
    private String LabelTitle2;
    private String Label3;
    private String LabelTitle3;
    private boolean HideUser;
    private String CropFormat;

    private int FavoriteId; // 0.未收藏 1.已收藏

    public int getFavoriteId() {
        return FavoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        FavoriteId = favoriteId;
    }

    public String getNoteId() {
        return NoteId;
    }

    public void setNoteId(String noteId) {
        NoteId = noteId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getUserNo() {
        return UserNo;
    }

    public void setUserNo(String userNo) {
        UserNo = userNo;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserHead() {
        return UserHead;
    }

    public void setUserHead(String userHead) {
        UserHead = userHead;
    }

    public long getCreatTime() {
        return CreatTime;
    }

    public void setCreatTime(long creatTime) {
        CreatTime = creatTime;
    }

    public String getResPath() {
        return ResPath;
    }

    public void setResPath(String resPath) {
        ResPath = resPath;
    }

    public String getDisplayNum() {
        return DisplayNum;
    }

    public void setDisplayNum(String displayNum) {
        DisplayNum = displayNum;
    }

    public String getCompleteNum() {
        return CompleteNum;
    }

    public void setCompleteNum(String completeNum) {
        CompleteNum = completeNum;
    }

    public String getJType() {
        return JType;
    }

    public void setJType(String JType) {
        this.JType = JType;
    }

    public String getLimitNum() {
        return LimitNum;
    }

    public void setLimitNum(String limitNum) {
        LimitNum = limitNum;
    }

    public String getBestResults() {
        return BestResults;
    }

    public void setBestResults(String bestResults) {
        BestResults = bestResults;
    }

    public String getLabel1() {
        return Label1;
    }

    public void setLabel1(String label1) {
        Label1 = label1;
    }

    public String getLabelTitle1() {
        return LabelTitle1;
    }

    public void setLabelTitle1(String labelTitle1) {
        LabelTitle1 = labelTitle1;
    }

    public String getLabel2() {
        return Label2;
    }

    public void setLabel2(String label2) {
        Label2 = label2;
    }

    public String getLabelTitle2() {
        return LabelTitle2;
    }

    public void setLabelTitle2(String labelTitle2) {
        LabelTitle2 = labelTitle2;
    }

    public String getLabel3() {
        return Label3;
    }

    public void setLabel3(String label3) {
        Label3 = label3;
    }

    public String getLabelTitle3() {
        return LabelTitle3;
    }

    public void setLabelTitle3(String labelTitle3) {
        LabelTitle3 = labelTitle3;
    }

    public boolean isHideUser() {
        return HideUser;
    }

    public void setHideUser(boolean hideUser) {
        HideUser = hideUser;
    }

    public String getCropFormat() {
        return CropFormat;
    }

    public void setCropFormat(String cropFormat) {
        CropFormat = cropFormat;
    }

    public String getGsResPath() {
        return GsResPath;
    }

    public void setGsResPath(String gsResPath) {
        GsResPath = gsResPath;
    }
}
