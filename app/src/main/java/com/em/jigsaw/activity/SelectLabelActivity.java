package com.em.jigsaw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import com.em.jigsaw.R;
import com.em.jigsaw.adapter.HotLabelAdapter;
import com.em.jigsaw.adapter.LabelSelectAdapter;
import com.em.jigsaw.base.ContentKey;
import com.em.jigsaw.base.ServiceAPI;
import com.em.jigsaw.bean.LabelBean;
import com.em.jigsaw.utils.KeyBoardUtils;
import com.em.jigsaw.utils.SignUtil;
import com.em.jigsaw.utils.SystemUtil;
import com.em.jigsaw.view.dialog.LoadingDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectLabelActivity extends AppCompatActivity {

    @BindView(R.id.edt_content)
    EditText edtContent;
    @BindView(R.id.label_listview)
    ListView labelListview;
    @BindView(R.id.label_gridview)
    GridView labelGridview;

    String tabIds, curKey;
    int hotNum = 9; // 热门搜索数量
    LabelSelectAdapter labelSelectAdapter;
    HotLabelAdapter hotLabelAdapter;
    List<LabelBean> baseList = new ArrayList<>();
    List<LabelBean> labelBeanList = new ArrayList<>();
    List<LabelBean> labelHotsList = new ArrayList<>();

    LoadingDialog loadingDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_label);
        ButterKnife.bind(this);
        tabIds = getIntent().getStringExtra("tabId");

        initUI();
        loadData();
    }

    private void loadData() {
        labelBeanList.clear();
        OkGo.<String>get(ServiceAPI.GetLabelList).tag(this)
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
                                    if(!tabIds.contains(bean.getTitle())){
                                        baseList.add(bean);
                                    }
                                }
                                initHotLabels();
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

    private void initHotLabels(){
        HashSet<Integer> hotIndexArray = new HashSet<>();
        SystemUtil.randomSet(0,baseList.size()-1,hotNum,hotIndexArray);
        for(int i = 0;i < baseList.size();i++){
            if(hotIndexArray.contains(i)){
                labelHotsList.add(baseList.get(i));
            }
        }
        hotLabelAdapter.notifyDataSetChanged();
    }

    private void initUI() {
        labelSelectAdapter = new LabelSelectAdapter(labelBeanList, SelectLabelActivity.this);
        labelListview.setAdapter(labelSelectAdapter);
        labelListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setResult(ContentKey.Pager_Complete, new Intent()
                        .putExtra("id", labelBeanList.get(i).getId())
                        .putExtra("title", labelBeanList.get(i).getTitle()));
                KeyBoardUtils.closeKeybord(edtContent, SelectLabelActivity.this);
                finish();
            }
        });

        hotLabelAdapter = new HotLabelAdapter(labelHotsList,SelectLabelActivity.this);
        labelGridview.setAdapter(hotLabelAdapter);
        labelGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setResult(ContentKey.Pager_Complete, new Intent()
                        .putExtra("id", labelHotsList.get(i).getId())
                        .putExtra("title", labelHotsList.get(i).getTitle()));
                KeyBoardUtils.closeKeybord(edtContent, SelectLabelActivity.this);
                finish();
            }
        });

        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                labelBeanList.clear();
                for (LabelBean bean : baseList) {
                    if (!TextUtils.isEmpty(charSequence) && bean.getTitle().contains(charSequence)) {
                        labelBeanList.add(bean);
                    }
                }
                labelSelectAdapter.notifyDataSetChanged();

                if(labelBeanList.size() > 0){
                    labelListview.setVisibility(View.VISIBLE);
                    labelGridview.setVisibility(View.GONE);
                }else{
                    labelListview.setVisibility(View.GONE);
                    labelGridview.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loadingDialog = new LoadingDialog(SelectLabelActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loadingDialog != null){
            loadingDialog.dismiss();
        }
    }
}
