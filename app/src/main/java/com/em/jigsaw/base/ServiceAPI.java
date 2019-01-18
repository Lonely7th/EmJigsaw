package com.em.jigsaw.base;

/**
 * Time ： 2018/12/26 0026 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class ServiceAPI {
    public final static String BASE_URL = "http://172.31.71.35:8080/";
    public final static String IMAGE_URL = "http://172.31.71.35:8089/";

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

    /**
     * 发布相关
     */
    public static final String GetCategroy = BASE_URL + "api/v2/getCategroy";
    public static final String GetJList = BASE_URL + "api/v3/getJNoteList";
    public static final String GetJDetails = BASE_URL + "api/v3/getJNoteDetails";
    public static final String AddJDetails = BASE_URL + "api/v3/addJNote";
    public static final String GetLabelList = BASE_URL + "api/v3/getLabelList";
    public static final String GetReleaseList = BASE_URL + "api/v3/getReleaseList";
    public static final String GetStarList = BASE_URL + "api/v3/getStarNoteList";
    public static final String StarJNote = BASE_URL + "api/v3/starJNote";
    public static final String SearchJNote = BASE_URL + "api/v3/searchJNoteList";
    public static final String PostJResult = BASE_URL + "api/v3/postJNoteResult";

    /**
     * 系统相关
     */
    public static final String AddFeedBack = BASE_URL + "api/v4/addFeedBack";
    public static final String GetVerisonCode = BASE_URL + "api/v4/getVersionCode";
}
