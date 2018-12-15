package com.em.jigsaw.bean;

/**
 * Time ： 2018/12/15 0015 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class JigsawListBean {
    private String UserName;
    private String Content;
    private String CreatTime;
    private String HeadPath;
    private String JigsawPath;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getCreatTime() {
        return CreatTime;
    }

    public void setCreatTime(String creatTime) {
        CreatTime = creatTime;
    }

    public String getHeadPath() {
        return HeadPath;
    }

    public void setHeadPath(String headPath) {
        HeadPath = headPath;
    }

    public String getJigsawPath() {
        return JigsawPath;
    }

    public void setJigsawPath(String jigsawPath) {
        JigsawPath = jigsawPath;
    }
}
