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
import com.em.jigsaw.view.RoundImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

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

    private ReleaseListAdapter releaseListAdapter;
    private List<JNoteBean> list = new ArrayList<>();

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
        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OthersInfoActivity.this, ShowPicActivity.class).putExtra("uri", headPath));
            }
        });
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
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @OnClick({R.id.btn_close, R.id.btn_star, R.id.iv_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                finish();
                break;
            case R.id.btn_star:
                break;
            case R.id.iv_head:
                break;
        }
    }
}
