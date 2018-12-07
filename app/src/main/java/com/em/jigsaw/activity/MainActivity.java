package com.em.jigsaw.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.em.jigsaw.R;
import com.em.jigsaw.adapter.JigsawAdapter;
import com.em.jigsaw.bean.JigsawImgBean;
import com.em.jigsaw.view.JigsawView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.view_jigsaw)
    JigsawView viewJigsaw;

    private JigsawAdapter jigsawAdapter;
    private List<JigsawImgBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initUI();
        initData();
    }

    private void initData() {
        for(int i = 0;i < 9;i++){
            JigsawImgBean jigsawImgBean = new JigsawImgBean();
            list.add(jigsawImgBean);
        }
        jigsawAdapter = new JigsawAdapter(list,MainActivity.this);
        viewJigsaw.setNumColumns(3);
        viewJigsaw.setAdapter(jigsawAdapter);
    }

    private void initUI() {
    }
}
