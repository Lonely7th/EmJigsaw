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
import android.widget.ListView;

import com.em.jigsaw.R;
import com.em.jigsaw.adapter.LabelSelectAdapter;
import com.em.jigsaw.base.ContentKey;
import com.em.jigsaw.base.ServiceAPI;
import com.em.jigsaw.bean.LabelBean;
import com.em.jigsaw.utils.KeyBoardUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectLabelActivity extends AppCompatActivity {

    @BindView(R.id.edt_content)
    EditText edtContent;
    @BindView(R.id.label_listview)
    ListView labelListview;

    String tabIds,curKey;
    LabelSelectAdapter labelSelectAdapter;
    List<LabelBean> baseList = new ArrayList<>();
    List<LabelBean> labelBeanList = new ArrayList<>();

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
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            if(body.getInt("ResultCode") == ServiceAPI.HttpSuccess){
                                JSONArray array = body.getJSONArray("ResultData");
                                for(int i = 0;i < array.length();i++){
                                    JSONObject obj = array.getJSONObject(i);
                                    LabelBean bean = new LabelBean();
                                    bean.setId(obj.getString("Lid"));
                                    bean.setTitle(obj.getString("Title"));
                                    baseList.add(bean);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initUI() {
        labelSelectAdapter = new LabelSelectAdapter(labelBeanList, SelectLabelActivity.this);
        labelListview.setAdapter(labelSelectAdapter);
        labelListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setResult(ContentKey.Pager_Complete, new Intent()
                        .putExtra("id",labelBeanList.get(i).getId())
                        .putExtra("title",labelBeanList.get(i).getTitle()));
                KeyBoardUtils.closeKeybord(edtContent,SelectLabelActivity.this);
                finish();
            }
        });

        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence)){
                    labelBeanList.clear();
                    for(LabelBean bean : baseList){
                        if(bean.getTitle().contains(charSequence)){
                            labelBeanList.add(bean);
                        }
                    }
                    labelSelectAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
