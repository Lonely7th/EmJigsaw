package com.em.jigsaw.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.adapter.SearchAdapter;
import com.em.jigsaw.base.ServiceAPI;
import com.em.jigsaw.bean.JNoteBean;
import com.em.jigsaw.callback.OnJListHeadClickListener;
import com.em.jigsaw.utils.KeyBoardUtils;
import com.em.jigsaw.utils.SignUtil;
import com.em.jigsaw.utils.ToastUtil;
import com.em.jigsaw.view.dialog.LoadingDialog;
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

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.edt_content)
    EditText edtContent;
    @BindView(R.id.btn_search)
    TextView btnSearch;
    @BindView(R.id.btn_close)
    ImageView btnClose;
    @BindView(R.id.listview)
    ListView listview;

    private View listFootView;
    private TextView tvLoadMore;
    private ImageView ivLoadMore;
    private SearchAdapter searchAdapter;
    private List<JNoteBean> list = new ArrayList<>();

    private String key = "";
    private LoadingDialog loadingDialog;
    private int currentPager = 1; // 当前加载页
    private boolean isLoading = false; // 正在加载
    private boolean hasMoreData = true; // 可以加载更多数据

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        LayoutInflater inflater = getLayoutInflater();
        listFootView = inflater.inflate(R.layout.view_main_list_foot, null);
        tvLoadMore = listFootView.findViewById(R.id.tv_content);
        ivLoadMore = listFootView.findViewById(R.id.iv_loading);

        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {
        searchAdapter = new SearchAdapter(list, this, new OnJListHeadClickListener() {
            @Override
            public void onClick(int position) {
                startActivity(new Intent(SearchActivity.this, OthersInfoActivity.class).putExtra("user_id",list.get(position).getUserNo()));
            }
        });
        listview.addFooterView(listFootView);
        listview.setAdapter(searchAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == list.size()) {
                    startLoadMore();
                } else {
                    startActivity(new Intent(SearchActivity.this, JigsawViewActivity.class).putExtra("id", list.get(i).getNoteId()));
                }
            }
        });

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (i == 0) {
                    //滚动到顶部
                } else if ((i + i1) == i2) {
                    //滚动到底部
                    if (!isLoading && hasMoreData) {
                        startLoadMore();
                    }
                }
            }
        });

        edtContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“搜索”键*/
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    key = edtContent.getText().toString().trim();
                    if (TextUtils.isEmpty(key)) {
                        ToastUtil.show(SearchActivity.this, "关键字不能为空");
                    } else {
                        loadingDialog = new LoadingDialog(SearchActivity.this);
                        loadingDialog.show();
                        loadItemData(true);
                    }
                    KeyBoardUtils.closeKeybord(edtContent, SearchActivity.this);
                    return true;
                }
                return false;
            }
        });

        KeyBoardUtils.openKeybord(edtContent, SearchActivity.this);
    }

    /**
     * 加载Data
     */
    private void loadItemData(final boolean isRefresh) {
        if (isLoading || TextUtils.isEmpty(key)) { // 避免重复加载
            return;
        }
        isLoading = true;
        if (isRefresh) {
            currentPager = 1;
        } else {
            currentPager++; // 当前页+1
        }

        OkGo.<String>get(ServiceAPI.SearchJNote).tag(this)
                .params("key", key)
                .params("page", "" + currentPager)
                .params(SignUtil.getParams(false))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            if (body.getInt("ResultCode") == ServiceAPI.HttpSuccess) {
                                if (isRefresh) {
                                    list.clear();
                                }

                                if (!body.getString("ResultData").equals("null")) {
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
                                    searchAdapter.notifyDataSetChanged();

                                    if (array.length() == 0) {
                                        hasMoreData = false;
                                        ToastUtil.show(SearchActivity.this, "暂无更多内容");
                                    } else {
                                        hasMoreData = true;
                                    }
                                    KeyBoardUtils.closeKeybord(edtContent, SearchActivity.this);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        endLoadMore();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        endLoadMore();
                    }
                });
    }

    /**
     * 开始加载更多
     */
    private void startLoadMore() {
        tvLoadMore.setText("加载更多...");
        loadItemData(false);
        ivLoadMore.setVisibility(View.VISIBLE);

        Animation rotate = AnimationUtils.loadAnimation(SearchActivity.this, R.anim.rotate_anim);
        if (rotate != null) {
            ivLoadMore.startAnimation(rotate);
        }
    }

    /**
     * 结束加载更多
     */
    private void endLoadMore() {
        tvLoadMore.setText("点击加载更多");
        ivLoadMore.setVisibility(View.GONE);
        ivLoadMore.clearAnimation();
        isLoading = false;
        if (list.size() > 0) {
            listview.setVisibility(View.VISIBLE);
        } else {
            listview.setVisibility(View.GONE);
        }
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @OnClick({R.id.btn_close, R.id.btn_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                KeyBoardUtils.closeKeybord(edtContent, SearchActivity.this);
                finish();
                break;
            case R.id.btn_search:
                key = edtContent.getText().toString().trim();
                if (TextUtils.isEmpty(key)) {
                    ToastUtil.show(SearchActivity.this, "关键字不能为空");
                } else {
                    loadingDialog = new LoadingDialog(SearchActivity.this);
                    loadingDialog.show();
                    loadItemData(true);
                }
                break;
        }
    }
}
