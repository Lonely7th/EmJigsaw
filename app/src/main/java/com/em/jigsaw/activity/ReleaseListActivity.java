package com.em.jigsaw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.adapter.ReleaseListAdapter;
import com.em.jigsaw.base.ServiceAPI;
import com.em.jigsaw.bean.JigsawListBean;
import com.em.jigsaw.utils.LoginUtil;
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

public class ReleaseListActivity extends AppCompatActivity {

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

    private ReleaseListAdapter releaseListAdapter;
    private List<JigsawListBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_list);
        ButterKnife.bind(this);
        
        initUI();
        initData();
    }

    private void initData() {
        OkGo.<String>get(ServiceAPI.GetReleaseList).tag(this)
                .params("user_no", LoginUtil.getUserInfo().getUserNo())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            if (body.getInt("ResultCode") == ServiceAPI.HttpSuccess) {
                                JSONArray array = body.getJSONArray("ResultData");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject obj = array.getJSONObject(i);
                                    JigsawListBean jigsawListBean = new JigsawListBean();

                                    jigsawListBean.setBestResults(obj.getString("BestResults"));
                                    jigsawListBean.setCompleteNum(obj.getString("CompleteNum"));
                                    jigsawListBean.setContent(obj.getString("Content"));
                                    jigsawListBean.setCreatTime(obj.getLong("CreatTime"));
                                    jigsawListBean.setCropFormat(obj.getString("CropFormat"));
                                    jigsawListBean.setDisplayNum(obj.getString("DisplayNum"));
                                    jigsawListBean.setHideUser(obj.getBoolean("HideUser"));
                                    jigsawListBean.setJType(obj.getString("JType"));
                                    jigsawListBean.setLabel1(obj.getString("Label1"));
                                    jigsawListBean.setLabel2(obj.getString("Label2"));
                                    jigsawListBean.setLabel3(obj.getString("Label3"));
                                    jigsawListBean.setLabelTitle1(obj.getString("LabelTitle1"));
                                    jigsawListBean.setLabelTitle2(obj.getString("LabelTitle2"));
                                    jigsawListBean.setLabelTitle3(obj.getString("LabelTitle3"));
                                    jigsawListBean.setLimitNum(obj.getString("LimitNum"));
                                    jigsawListBean.setNoteId(obj.getString("NoteId"));
                                    jigsawListBean.setResPath(obj.getString("ResPath"));

                                    JSONObject userObj = obj.getJSONObject("Releaser");
                                    jigsawListBean.setUserHead(userObj.getString("NameHead"));
                                    jigsawListBean.setUserName(userObj.getString("UserName"));
                                    jigsawListBean.setUserNo(userObj.getString("UserNo"));
                                    list.add(jigsawListBean);
                                }
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

    private void initUI() {
        releaseListAdapter = new ReleaseListAdapter(list,ReleaseListActivity.this);
        listview.setAdapter(releaseListAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(ReleaseListActivity.this, JigsawViewActivity.class).putExtra("id", list.get(i).getNoteId()));
            }
        });
    }

    @OnClick(R.id.back_btn)
    public void onViewClicked() {
        finish();
    }
}
