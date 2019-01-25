package com.em.jigsaw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.em.jigsaw.R;
import com.em.jigsaw.adapter.ReleaseListAdapter;
import com.em.jigsaw.base.ServiceAPI;
import com.em.jigsaw.bean.JNoteBean;
import com.em.jigsaw.bean.UserBean;
import com.em.jigsaw.utils.LoginUtil;
import com.em.jigsaw.utils.SignUtil;
import com.em.jigsaw.utils.ToastUtil;
import com.em.jigsaw.view.RoundImageView;
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

public class OthersInfoActivity extends AppCompatActivity {

    @BindView(R.id.btn_close)
    ImageView btnClose;
    @BindView(R.id.btn_star)
    TextView btnStar;
    @BindView(R.id.iv_head)
    RoundImageView ivHead;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.tv_user_id)
    TextView tvUserId;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_release_num)
    TextView tvReleaseNum;
    @BindView(R.id.lv_release)
    ListView lvRelease;

    private String userNo,nickName,headPath;
    private boolean isFollower = false;// 是否已经关注该用户

    private ReleaseListAdapter releaseListAdapter;
    private List<JNoteBean> list = new ArrayList<>();

    private boolean isFollowing = false;// 正在处理关注相关的事务
    private LoadingDialog loadingDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_info);
        ButterKnife.bind(this);
        userNo = getIntent().getStringExtra("user_id");

        initUI();
        initUserInfo();
        initReleaseList();
    }

    private void initUI() {
        releaseListAdapter = new ReleaseListAdapter(list,OthersInfoActivity.this);
        lvRelease.setAdapter(releaseListAdapter);
        lvRelease.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(OthersInfoActivity.this, JigsawViewActivity.class).putExtra("id", list.get(i).getNoteId()));
            }
        });

        loadingDialog = new LoadingDialog(OthersInfoActivity.this);
    }

    private void initReleaseList() {
        OkGo.<String>get(ServiceAPI.GetReleaseList).tag(this)
                .params("user_no", userNo)
                .params(SignUtil.getParams(true))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            if (body.getInt("ResultCode") == ServiceAPI.HttpSuccess) {
                                JSONArray array = body.getJSONArray("ResultData");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject obj = array.getJSONObject(i);
                                    JNoteBean JNoteBean = new JNoteBean();

                                    JNoteBean.setBestResults(obj.getString("BestResults"));
                                    JNoteBean.setCompleteNum(obj.getString("CompleteNum"));
                                    JNoteBean.setContent(obj.getString("Content"));
                                    JNoteBean.setCreatTime(obj.getLong("CreatTime"));
                                    JNoteBean.setCropFormat(obj.getString("CropFormat"));
                                    JNoteBean.setDisplayNum(obj.getString("DisplayNum"));
                                    JNoteBean.setHideUser(obj.getBoolean("HideUser"));
                                    JNoteBean.setJType(obj.getString("JType"));
                                    JNoteBean.setLabel1(obj.getString("Label1"));
                                    JNoteBean.setLabel2(obj.getString("Label2"));
                                    JNoteBean.setLabel3(obj.getString("Label3"));
                                    JNoteBean.setLabelTitle1(obj.getString("LabelTitle1"));
                                    JNoteBean.setLabelTitle2(obj.getString("LabelTitle2"));
                                    JNoteBean.setLabelTitle3(obj.getString("LabelTitle3"));
                                    JNoteBean.setLimitNum(obj.getString("LimitNum"));
                                    JNoteBean.setNoteId(obj.getString("NoteId"));
                                    JNoteBean.setResPath(obj.getString("ResPath"));
                                    JNoteBean.setGsResPath(obj.getString("GsResPath"));
                                    JNoteBean.setSuccessRate(obj.getString("SuccessRate"));

                                    JSONObject userObj = obj.getJSONObject("Releaser");
                                    JNoteBean.setUserHead(userObj.getString("NameHead"));
                                    JNoteBean.setUserName(userObj.getString("UserName"));
                                    JNoteBean.setUserNo(userObj.getString("UserNo"));
                                    list.add(JNoteBean);
                                }
                                tvReleaseNum.setText(nickName + " 的发布" + " (" + list.size() + ")");
                                releaseListAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }

    private void initUserInfo() {
        OkGo.<String>get(ServiceAPI.GetUserInfo).tag(this)
                .params("user_no", userNo)
                .params("follow_no", LoginUtil.getUserInfo().getUserNo())
                .params(SignUtil.getParams(true))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            if(body.getInt("ResultCode") == ServiceAPI.HttpSuccess){
                                JSONObject data = body.getJSONObject("ResultData");
                                nickName = data.getString("UserName");
                                tvNickName.setText("昵称：" + nickName);
                                tvUserId.setText("用户Id：" + data.getString("UserNo"));
                                tvAddress.setText("地址：" + (TextUtils.isEmpty(data.getString("NameCity"))?"中国":data.getString("NameCity")));
                                headPath = data.getString("NameHead").startsWith("http")?data.getString("NameHead"): ServiceAPI.IMAGE_URL + data.getString("NameHead");
                                Glide.with(OthersInfoActivity.this).load(headPath).into(ivHead);

                                tvReleaseNum.setText(nickName + " 的发布" + " (" + list.size() + ")");
                                isFollower = data.getBoolean("IsFollow");

                                updateFavoriteStatus();
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

    /**
     * 添加关注
     */
    private void addFollow(){
        if(isFollowing){
            return ;
        }
        if(!LoginUtil.isLogin()){
            ToastUtil.show(OthersInfoActivity.this,"请登录后关注");
            return ;
        }
        isFollowing = true;

        OkGo.<String>post(ServiceAPI.AddFollow).tag(this)
                .params("user_no", LoginUtil.getUserInfo().getUserNo())
                .params("follow_id", userNo)
                .params("type", isFollower?"1":"0")
                .params(SignUtil.getParams(true))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            if(body.getInt("ResultCode") == ServiceAPI.HttpSuccess){
                                if(isFollower){
                                    ToastUtil.show(OthersInfoActivity.this,"关注取消");
                                    isFollower = false;
                                }else{
                                    ToastUtil.show(OthersInfoActivity.this,"关注成功");
                                    isFollower = true;
                                }
                                updateFavoriteStatus();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<String> response) {
                        super.onError(response);
                        ToastUtil.show(OthersInfoActivity.this,"网络异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        isFollowing = false;
                    }
                });
    }

    /**
     * 刷新关注状态
     */
    private void updateFavoriteStatus() {
        if(isFollower){
            btnStar.setText("已关注");
            btnStar.setTextColor(getResources().getColor(R.color.colorWhite));
            btnStar.setBackgroundResource(R.drawable.bg_shape_blue);
        }else{
            btnStar.setText("+ 关注");
            btnStar.setTextColor(getResources().getColor(R.color.colorBlue));
            btnStar.setBackgroundResource(R.drawable.bg_border_blue);
        }
    }

    @OnClick({R.id.btn_close, R.id.btn_star, R.id.iv_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                finish();
                break;
            case R.id.btn_star:
                addFollow();
                break;
            case R.id.iv_head:
                startActivity(new Intent(OthersInfoActivity.this, ShowPicActivity.class).putExtra("uri", headPath));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loadingDialog != null){
            loadingDialog.dismiss();
        }
    }
}
