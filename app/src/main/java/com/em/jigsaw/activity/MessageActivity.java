package com.em.jigsaw.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.adapter.FollowListAdapter;
import com.em.jigsaw.base.ServiceAPI;
import com.em.jigsaw.bean.UserBean;
import com.em.jigsaw.utils.LoginUtil;
import com.em.jigsaw.utils.SignUtil;
import com.em.jigsaw.view.dialog.LoadingDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageActivity extends AppCompatActivity {

    @BindView(R.id.back_btn)
    RelativeLayout backBtn;
    @BindView(R.id.tv_bar_center)
    TextView tvBarCenter;
    @BindView(R.id.iv_right_icon)
    ImageView ivRightIcon;
    @BindView(R.id.tv_bar_right)
    TextView tvBarRight;
    @BindView(R.id.right_btn)
    RelativeLayout rightBtn;
    @BindView(R.id.listview)
    ListView listview;

    private FollowListAdapter followListAdapter;
    private List<UserBean> list = new ArrayList<>();

    private LoadingDialog loadingDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        ButterKnife.bind(this);

        initUI();
        initData();
    }

    private void initData() {
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
                                    UserBean userBean = new UserBean();
                                    userBean.setNameHead(obj.getString("NameHead"));
                                    userBean.setUserName(obj.getString("UserName"));
                                    userBean.setNameCity(obj.getString("NameCity"));
                                    userBean.setUserNo(obj.getString("UserNo"));
                                    userBean.setUserPhone(obj.getString("UserPhone"));
                                    list.add(userBean);
                                }
                                tvBarCenter.setText("我的关注" + "(" + array.length() + ")");
                                followListAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        loadingDialog.show();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        loadingDialog.dismiss();
                    }
                });
    }

    private void initUI() {
        followListAdapter = new FollowListAdapter(list,MessageActivity.this);
        listview.setAdapter(followListAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(MessageActivity.this, OthersInfoActivity.class).putExtra("user_id", list.get(i).getUserNo()));
            }
        });

        loadingDialog = new LoadingDialog(MessageActivity.this);
    }

    @OnClick(R.id.back_btn)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loadingDialog != null){
            loadingDialog.dismiss();
        }
    }
}
