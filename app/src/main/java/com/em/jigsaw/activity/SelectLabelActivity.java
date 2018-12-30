package com.em.jigsaw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.em.jigsaw.R;
import com.em.jigsaw.adapter.LabelSelectAdapter;
import com.em.jigsaw.base.ContentKey;
import com.em.jigsaw.bean.LabelBean;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectLabelActivity extends AppCompatActivity {

    @BindView(R.id.edt_content)
    EditText edtContent;
    @BindView(R.id.label_listview)
    ListView labelListview;

    String tabIds;
    LabelSelectAdapter labelSelectAdapter;
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
        for(int i = 0;i < 16;i++){
            if(!tabIds.contains(""+i)){
                LabelBean bean = new LabelBean();
                bean.setId(""+i);
                bean.setTitle("标签"+i);
                labelBeanList.add(bean);
            }
        }
        labelSelectAdapter.notifyDataSetChanged();
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
                finish();
            }
        });

        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
