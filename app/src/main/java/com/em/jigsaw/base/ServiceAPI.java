package com.em.jigsaw.base;

/**
 * Time ： 2018/12/26 0026 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class ServiceAPI {
    public final static String BASE_URL = "http://172.31.71.35:8080/";

    /**
     * 状态码
     */
    public static final int HttpSuccess = 200;

    /**
     * 用户相关
     */
    public static final String Login = BASE_URL + "api/v1/login";
    public static final String ChangeInfo = BASE_URL + "api/v1/changeInfo";
    public static final String GetUserInfo = BASE_URL + "api/v1/getUserInfo";
    public static final String GetUserHead = BASE_URL + "api/v1/changeHead";
}
