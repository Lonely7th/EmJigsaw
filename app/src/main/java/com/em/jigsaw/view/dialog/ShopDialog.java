package com.em.jigsaw.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.em.jigsaw.R;
import com.em.jigsaw.adapter.SelectDialogAdapter;
import com.em.jigsaw.adapter.ShopDialogAdapter;
import com.em.jigsaw.base.ServiceAPI;
import com.em.jigsaw.bean.MessageBean;
import com.em.jigsaw.bean.PayEventBean;
import com.em.jigsaw.utils.LoginUtil;
import com.em.jigsaw.utils.SignUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Time ： 2019/5/13 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class ShopDialog extends Dialog {
    private Context context;
    private ListView listView;

    private ShopDialogAdapter shopDialogAdapter;
    private List<PayEventBean> mlist = new ArrayList<>();

    public ShopDialog(Context context) {
        super(context, R.style.AlertDialogStyle);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shop);
        listView = findViewById(R.id.list_view);
        shopDialogAdapter = new ShopDialogAdapter(mlist, context);
        listView.setAdapter(shopDialogAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();
            }
        });
        findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        setCanceledOnTouchOutside(false);

        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);

        loadData();
    }

    private void loadData(){
        OkGo.<String>get(ServiceAPI.GetFollowList).tag(this)
                .params("user_no", LoginUtil.getUserInfo().getUserNo())
                .params(SignUtil.getParams(true))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            if (body.getInt("ResultCode") == ServiceAPI.HttpSuccess) {
                                JSONArray array = body.getJSONArray("ResultData");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject obj = array.getJSONObject(i).getJSONObject("Follower");
                                    PayEventBean payEventBean = new PayEventBean();
                                    payEventBean.setContent(obj.getString("NameCity"));
                                    payEventBean.setId(obj.getString("NameCity"));
                                    payEventBean.setOriginalPrice(obj.getString("NameCity"));
                                    payEventBean.setPresentPrice(obj.getString("NameCity"));
                                    mlist.add(payEventBean);
                                }
                                shopDialogAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dismiss();
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
