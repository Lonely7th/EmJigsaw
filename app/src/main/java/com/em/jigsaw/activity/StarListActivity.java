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
import com.em.jigsaw.adapter.StarListAdapter;
import com.em.jigsaw.base.ServiceAPI;
import com.em.jigsaw.bean.JNoteBean;
import com.em.jigsaw.bean.NoteStarBean;
import com.em.jigsaw.utils.LoginUtil;
import com.em.jigsaw.utils.SignUtil;
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

public class StarListActivity extends AppCompatActivity {

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

    private StarListAdapter starListAdapter;
    private List<NoteStarBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_list);
        ButterKnife.bind(this);

        initUI();
        initData();
    }

    private void initData() {
        OkGo.<String>get(ServiceAPI.GetStarList).tag(this)
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
                                    JSONObject data = array.getJSONObject(i);
                                    NoteStarBean noteStarBean = new NoteStarBean();
                                    noteStarBean.setCreatTime(data.getString("CreatTime"));
                                    noteStarBean.setUserNo(data.getString("UserNo"));

                                    JSONObject obj = data.getJSONObject("Note");
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

                                    noteStarBean.setjNoteBean(JNoteBean);
                                    list.add(noteStarBean);
                                }
                                tvBarCenter.setText("我的收藏" + "(" + array.length() + ")");
                                starListAdapter.notifyDataSetChanged();
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
        starListAdapter = new StarListAdapter(list,StarListActivity.this);
        listview.setAdapter(starListAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(StarListActivity.this, JigsawViewActivity.class).putExtra("id", list.get(i).getjNoteBean().getNoteId()));
            }
        });
    }

    @OnClick(R.id.back_btn)
    public void onViewClicked() {
        finish();
    }
}
