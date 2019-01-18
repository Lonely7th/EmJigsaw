package com.em.jigsaw.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Time ： 2018/12/21 0021 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class SignUtil {

    /**
     * 获取网络请求时的签名
     */
    public static Map<String, String> getParams(boolean need_token){
        Map<String, String> params = new HashMap<>();
        //添加时间戳
        String timeStamp = System.currentTimeMillis()+"";
        params.put("timeStamp",timeStamp);
        params.put("token",(need_token?LoginUtil.getUserInfo().getUserToken():""));
        //添加签名
        String sign = sign(timeStamp, need_token);
        params.put("sign",sign);
        return params;
    }

    private static String sign(String timeStamp, boolean need_token) {
        try {
            String secretKey = "9b063dfaef3f9deaf4413ffb8f26d247";
            StringBuilder sb = new StringBuilder();
            sb.append(timeStamp);
            sb.append(secretKey);
            if(need_token){
                sb.append(LoginUtil.getUserInfo().getUserToken());
            }
            return getAppM5(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getAppM5(String plainText) {
        String re_md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;

            StringBuilder buf = new StringBuilder("");
            for (byte aB : b) {
                i = aB;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            re_md5 = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }

}
