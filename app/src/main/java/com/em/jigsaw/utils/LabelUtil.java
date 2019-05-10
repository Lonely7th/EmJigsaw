package com.em.jigsaw.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.em.jigsaw.base.ServiceAPI;
import com.em.jigsaw.bean.LabelBean;
import com.em.jigsaw.callback.OnLoadLabelListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Time ： 2019/5/10 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class LabelUtil {

    private static List<LabelBean> labelBeanList = new ArrayList<>();

    public static void initLabel(Context context, @NonNull OnLoadLabelListener onLoadLabelListener){
        OkGo.<String>get(ServiceAPI.GetLabelList).tag(context)
                .params(SignUtil.getParams(false))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            if (body.getInt("ResultCode") == ServiceAPI.HttpSuccess) {
                                JSONArray array = body.getJSONArray("ResultData");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject obj = array.getJSONObject(i);
                                    LabelBean bean = new LabelBean();
                                    bean.setId(obj.getString("Id"));
                                    bean.setTitle(obj.getString("Title"));
                                    labelBeanList.add(bean);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        onLoadLabelListener.onComplete();
                    }
                });
    }

    /**
     * 获取全部Label
     */
    public static List<LabelBean> getAllLabel(){
        return labelBeanList;
    }

    /**
     * 随机获取Label
     * @param num Label的数量
     */
    public static List<LabelBean> getRanLabel(int num){
        List<LabelBean> result = new ArrayList<>();
        HashSet<Integer> hotIndexArray = new HashSet<>();
        SystemUtil.randomSet(0,labelBeanList.size()-1,num,hotIndexArray);
        for(int i = 0;i < labelBeanList.size();i++){
            if(hotIndexArray.contains(i)){
                result.add(labelBeanList.get(i));
            }
        }
        return result;
    }

}
