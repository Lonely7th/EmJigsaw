package com.em.jigsaw.activity.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.activity.JigsawViewActivity;
import com.em.jigsaw.activity.LoginActivity;
import com.em.jigsaw.adapter.JigsawListAdapter;
import com.em.jigsaw.adapter.TopBarAdapter;
import com.em.jigsaw.base.ServiceAPI;
import com.em.jigsaw.bean.JigsawListBean;
import com.em.jigsaw.bean.MainTopBarBean;
import com.em.jigsaw.utils.LoginUtil;
import com.em.jigsaw.utils.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Time ： 2018/12/11 0011 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class MainFragment extends Fragment {
    private final static String TAG = "MainFragment";
    @BindView(R.id.back_btn)
    RelativeLayout backBtn;
    @BindView(R.id.tv_bar_center)
    TextView tvBarCenter;
    @BindView(R.id.main_listview)
    ListView mainListview;
    @BindView(R.id.topbar_view)
    android.support.v7.widget.RecyclerView topbarView;

    private List<JigsawListBean> list = new ArrayList<>();
    private JigsawListAdapter jigsawListAdapter;
    private ArrayList<MainTopBarBean> topBarBeanList = new ArrayList<>();
    private TopBarAdapter topBarAdapter;

    private int currentTopBar = 0;
    private int currentPager = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        initUI();
        initData();
        loadBarData();
        loadItemData();
        return rootView;
    }

    private void initData() {
        jigsawListAdapter = new JigsawListAdapter(list, getActivity());
        mainListview.setAdapter(jigsawListAdapter);
        mainListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), JigsawViewActivity.class));
            }
        });

    }

    private void initUI() {
        backBtn.setVisibility(View.GONE);
        tvBarCenter.setText("发现 8 条新内容");

        topbarView.setHasFixedSize(true);//设置固定大小
        topbarView.setItemAnimator(new DefaultItemAnimator());//设置默认动画
        LinearLayoutManager mLayoutManage=new LinearLayoutManager(getActivity());
        mLayoutManage.setOrientation(OrientationHelper.HORIZONTAL);//设置滚动方向，横向滚动
        topbarView.setLayoutManager(mLayoutManage);
        topBarAdapter = new TopBarAdapter(getActivity(),topBarBeanList);
        topbarView.setAdapter(topBarAdapter);

        topBarAdapter.setOnItemClickListener(new TopBarAdapter.OnRecycleViewItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                for(int i = 0;i < topBarBeanList.size();i++){
                    currentTopBar = position;
                    if(i == position){
                        topBarBeanList.get(i).setSelect(true);
                    }else{
                        topBarBeanList.get(i).setSelect(false);
                    }
                }
                topBarAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadBarData() {
        OkGo.<String>get(ServiceAPI.GetCategroy).tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            if(body.getInt("ResultCode") == ServiceAPI.HttpSuccess){
                                JSONArray array = body.getJSONArray("ResultData");
                                for(int i = 0;i < array.length();i++){
                                    JSONObject obj = array.getJSONObject(i);
                                    MainTopBarBean barBean = new MainTopBarBean();
                                    barBean.setTitle(obj.getString("Title"));
                                    barBean.setID(obj.getString("Id"));
                                    if(i == 0){
                                        barBean.setSelect(true);
                                    }
                                    topBarBeanList.add(barBean);
                                }
                                topBarAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void loadItemData(){
        OkGo.<String>get(ServiceAPI.GetJList).tag(this)
                .params("categroy",""+currentTopBar)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            if(body.getInt("ResultCode") == ServiceAPI.HttpSuccess){
                                JSONArray array = body.getJSONArray("ResultData");
                                for(int i = 0;i < array.length();i++){
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
                                jigsawListAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
