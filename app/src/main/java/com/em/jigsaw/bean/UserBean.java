package com.em.jigsaw.bean;

/**
 * Time ： 2018/12/26 0026 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class UserBean {
    private String Id;
    private String UserNo;
    private String UserName;
    private String UserToken = "";
    private String UserPhone;
    private String NameHead = "";
    private String NameCity;
    private String CreatTime;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    public String getUserToken() {
        return UserToken;
    }

    public void setUserToken(String userToken) {
        UserToken = userToken;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getNameHead() {
        return NameHead;
    }

    public void setNameHead(String nameHead) {
        NameHead = nameHead;
    }

    public String getNameCity() {
        return NameCity;
    }

    public void setNameCity(String nameCity) {
        NameCity = nameCity;
    }

    public String getCreatTime() {
        return CreatTime;
    }

    public void setCreatTime(String creatTime) {
        CreatTime = creatTime;
    }
}
