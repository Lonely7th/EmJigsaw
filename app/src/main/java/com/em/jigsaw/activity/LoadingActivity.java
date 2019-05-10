package com.em.jigsaw.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.em.jigsaw.R;
import com.em.jigsaw.callback.OnLoadLabelListener;
import com.em.jigsaw.utils.LabelUtil;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //初始化标签列表
        LabelUtil.initLabel(LoadingActivity.this, new OnLoadLabelListener() {
            @Override
            public void onComplete() {
                startActivity(new Intent(LoadingActivity.this,HomeActivity.class));
            }
        });
    }
}
