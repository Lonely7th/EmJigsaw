package com.em.jigsaw.activity.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.activity.JigsawViewActivity;
import com.em.jigsaw.activity.OthersInfoActivity;
import com.em.jigsaw.activity.SearchActivity;
import com.em.jigsaw.adapter.JigsawListAdapter;
import com.em.jigsaw.adapter.TopBarAdapter;
import com.em.jigsaw.base.ServiceAPI;
import com.em.jigsaw.bean.JNoteBean;
import com.em.jigsaw.bean.MainTopBarBean;
import com.em.jigsaw.callback.OnJListHeadClickListener;
import com.em.jigsaw.utils.SignUtil;
import com.em.jigsaw.utils.ToastUtil;
import com.em.jigsaw.view.TouchListView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    TouchListView mainListview;
    @BindView(R.id.topbar_view)
    RecyclerView topbarView;
    @BindView(R.id.iv_right_icon)
    ImageView ivRightIcon;
    @BindView(R.id.tv_bar_right)
    TextView tvBarRight;
    @BindView(R.id.right_btn)
    RelativeLayout rightBtn;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private View listFootView;
    private TextView tvLoadMore;
    private ImageView ivLoadMore;
    private List<JNoteBean> list = new ArrayList<>();
    private JigsawListAdapter jigsawListAdapter;
    private ArrayList<MainTopBarBean> topBarBeanList = new ArrayList<>();
    private TopBarAdapter topBarAdapter;

    private int currentTopBar = 0; // 当前分类
    private int currentPager = 1; // 当前加载页
    private boolean isLoading = false; // 正在加载
    private boolean hasMoreData = true; // 可以加载更多数据

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        listFootView = inflater.inflate(R.layout.view_main_list_foot, container, false);
        tvLoadMore = listFootView.findViewById(R.id.tv_content);
        ivLoadMore = listFootView.findViewById(R.id.iv_loading);
        ButterKnife.bind(this, rootView);

        initUI();
        initData();
        loadBarData();
        return rootView;
    }

    private void initData() {
        jigsawListAdapter = new JigsawListAdapter(list, getActivity(), new OnJListHeadClickListener() {
            @Override
            public void onClick(int position) {
                startActivity(new Intent(getActivity(), OthersInfoActivity.class).putExtra("user_id",list.get(position).getUserNo()));
            }
        });
        mainListview.setAdapter(jigsawListAdapter);
        mainListview.addFooterView(listFootView);
        mainListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == list.size()){
                    startLoadMore();
                }else{
                    startActivity(new Intent(getActivity(), JigsawViewActivity.class).putExtra("id", list.get(i).getNoteId()));
                }
            }
        });
    }

    /**
     * 初始化页面
     */
    private void initUI() {
        backBtn.setVisibility(View.GONE);
        ivRightIcon.setVisibility(View.VISIBLE);
        ivRightIcon.setImageDrawable(getResources().getDrawable(R.mipmap.icon_search_b));

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorBlue));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItemData(true);
            }
        });

        topbarView.setHasFixedSize(true);//设置固定大小
        topbarView.setItemAnimator(new DefaultItemAnimator());//设置默认动画
        LinearLayoutManager mLayoutManage = new LinearLayoutManager(getActivity());
        mLayoutManage.setOrientation(OrientationHelper.HORIZONTAL);//设置滚动方向，横向滚动
        topbarView.setLayoutManager(mLayoutManage);
        topBarAdapter = new TopBarAdapter(getActivity(), topBarBeanList);
        topbarView.setAdapter(topBarAdapter);

        topBarAdapter.setOnItemClickListener(new TopBarAdapter.OnRecycleViewItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                for (int i = 0; i < topBarBeanList.size(); i++) {
                    currentTopBar = position;
                    if (i == position) {
                        topBarBeanList.get(i).setSelect(true);
                    } else {
                        topBarBeanList.get(i).setSelect(false);
                    }
                }
                topBarAdapter.notifyDataSetChanged();

                loadItemData(true);
            }
        });

        mainListview.setListTouchListener(new TouchListView.MyListTouchListener() {
            @Override
            public void touchLeft() {
                if(currentTopBar < topBarBeanList.size()-1){
                    currentTopBar++;
                }else{
                    return;
                }
                for (int i = 0; i < topBarBeanList.size(); i++) {
                    if (i == currentTopBar) {
                        topBarBeanList.get(i).setSelect(true);
                    } else {
                        topBarBeanList.get(i).setSelect(false);
                    }
                }
                topbarView.scrollToPosition(currentTopBar);
                topBarAdapter.notifyDataSetChanged();

                loadItemData(true);
            }

            @Override
            public void touchRight() {
                if(currentTopBar > 0){
                    currentTopBar--;
                }else{
                    return;
                }
                for (int i = 0; i < topBarBeanList.size(); i++) {
                    if (i == currentTopBar) {
                        topBarBeanList.get(i).setSelect(true);
                    } else {
                        topBarBeanList.get(i).setSelect(false);
                    }
                }
                topbarView.scrollToPosition(currentTopBar);
                topBarAdapter.notifyDataSetChanged();

                loadItemData(true);
            }
        });

        mainListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (i == 0) {
                    //滚动到顶部
                } else if ((i + i1) == i2) {
                    //滚动到底部
                    if(!isLoading && hasMoreData){
                        startLoadMore();
                    }
                }
            }
        });
    }

    /**
     * 加载顶部导航栏
     */
    private void loadBarData() {
        OkGo.<String>get(ServiceAPI.GetCategroy).tag(this)
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
                                    MainTopBarBean barBean = new MainTopBarBean();
                                    barBean.setTitle(obj.getString("Title"));
                                    barBean.setID(obj.getString("Cid"));
                                    if (i == 0) {
                                        barBean.setSelect(true);
                                    }
                                    topBarBeanList.add(barBean);
                                }
                                topBarAdapter.notifyDataSetChanged();

                                loadItemData(true);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 加载Data
     * @param isRefresh 是否刷新
     */
    private void loadItemData(final boolean isRefresh) {
        if(isLoading){ // 避免重复加载
           return ;
        }
        if(topBarBeanList.size() == 0){ // 避免数组越界
            return ;
        }
        isLoading = true; // 开始加载
        if(isRefresh){
            currentPager = 1;
        }else{
            currentPager++; // 当前页+1
        }
        tvBarCenter.setText("加载中...");

        OkGo.<String>get(ServiceAPI.GetJList).tag(this)
                .params("categroy", topBarBeanList.get(currentTopBar).getID())
                .params("page", "" + currentPager)
                .params(SignUtil.getParams(false))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            if (body.getInt("ResultCode") == ServiceAPI.HttpSuccess) {
                                if(isRefresh){
                                    list.clear();
                                }

                                if(!body.getString("ResultData").equals("null")){
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
                                    jigsawListAdapter.notifyDataSetChanged();

                                    if(array.length() == 0){
                                        hasMoreData = false;
                                        tvBarCenter.setText("发现");
                                        ToastUtil.show(getActivity(),"暂无更多内容");
                                    }else{
                                        hasMoreData = true;
                                        tvBarCenter.setText("发现 " + array.length() + " 条新内容");
                                    }
                                }else{
                                    hasMoreData = false;
                                    tvBarCenter.setText("发现");
                                    jigsawListAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        hasMoreData = false;
                        loadFinish(isRefresh);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        loadFinish(isRefresh);
                    }
                });
    }

    /**
     * 开始加载更多
     */
    private void startLoadMore(){
        tvLoadMore.setText("加载更多...");
        loadItemData(false);
        ivLoadMore.setVisibility(View.VISIBLE);

        Animation rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_anim);
        if (rotate != null) {
            ivLoadMore.startAnimation(rotate);
        }
    }

    /**
     * 结束加载更多
     */
    private void endLoadMore(){
        tvLoadMore.setText("点击加载更多");
        ivLoadMore.setVisibility(View.GONE);
        ivLoadMore.clearAnimation();
    }

    /**
     * 加载结束
     */
    private void loadFinish(boolean isRefresh){
        if(isRefresh){
            mSwipeRefreshLayout.setRefreshing(false);
        }else{
            endLoadMore();
        }
        isLoading = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(getActivity());
    }

    @OnClick(R.id.right_btn)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), SearchActivity.class));
    }
}
