package com.em.jigsaw.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.adapter.FormatSelectAdapter;
import com.em.jigsaw.base.ContentKey;
import com.em.jigsaw.bean.FormatSelectBean;
import com.em.jigsaw.bean.JigsawImgBean;
import com.em.jigsaw.bean.event.ReleaseEvent;
import com.em.jigsaw.utils.ImgUtil;
import com.em.jigsaw.view.JigsawView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddJigsawActivity extends AppCompatActivity {

    @BindView(R.id.back_btn)
    RelativeLayout backBtn;
    @BindView(R.id.tv_bar_center)
    TextView tvBarCenter;
    @BindView(R.id.tv_bar_right)
    TextView tvBarRight;
    @BindView(R.id.right_btn)
    RelativeLayout rightBtn;
    @BindView(R.id.view_jigsaw)
    JigsawView viewJigsaw;
    @BindView(R.id.gv_format_select)
    GridView gvFormatSelect;

    private ImgUtil imgUtil;
    private Uri imageUri;
    private int[] ImgFormat;
    private boolean fristLoad = true;

    private ArrayList<JigsawImgBean> list = new ArrayList<>();

    private int currentFormat = 0;
    private FormatSelectAdapter formatSelectAdapter;
    private List<FormatSelectBean> formatSelectBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jigsaw_view);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        imgUtil = new ImgUtil(AddJigsawActivity.this);

        imageUri = Uri.parse(getIntent().getStringExtra("ImageUri"));
        ImgFormat = getIntent().getIntArrayExtra("ImgFormat");

        initData();
        initUI();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(fristLoad){
            fristLoad = false;
            updateJigsawList(imgUtil.getBitmap(imageUri));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        //初始化裁剪格式数据
        for(int i = 0;i < ContentKey.Format_Array.length;i++){
            FormatSelectBean formatSelectBean = new FormatSelectBean();
            formatSelectBean.setID(""+i);
            formatSelectBean.setTitle(ContentKey.Format_Array[i]);
            if(i == currentFormat){
                formatSelectBean.setSelect(true);
            }
            formatSelectBeans.add(formatSelectBean);
        }
    }

    private void initUI() {
        tvBarCenter.setText("选择裁剪格式");
        tvBarRight.setText("下一步");
        tvBarRight.setVisibility(View.VISIBLE);

        formatSelectAdapter = new FormatSelectAdapter(formatSelectBeans,AddJigsawActivity.this);
        gvFormatSelect.setAdapter(formatSelectAdapter);
        gvFormatSelect.setNumColumns(ContentKey.Format_Array.length);
        gvFormatSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentFormat = position;
                for(int i = 0;i < formatSelectBeans.size();i++){
                    if(i == currentFormat){
                        formatSelectBeans.get(i).setSelect(true);
                    }else{
                        formatSelectBeans.get(i).setSelect(false);
                    }
                }
                formatSelectAdapter.notifyDataSetChanged();
                updateJigsawList(imgUtil.getBitmap(imageUri));
            }
        });

        viewJigsaw.setViewTouched(false);
    }

    /**
     * 刷新jigsawAdapter
     */
    private void updateJigsawList(Bitmap bitmap) {
        list.clear();
        list.addAll(imgUtil.sortImgArray(imgUtil.getImgArray(bitmap, formatSelectBeans.get(currentFormat).getFormat(), ImgFormat)));
        viewJigsaw.setLabels(list);
    }

    @OnClick({R.id.back_btn, R.id.right_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.right_btn:
                startActivity(new Intent(AddJigsawActivity.this,SelectJStatusActivity.class)
                        .putExtra("ImageUri",imageUri.toString()).putExtra("CropFormat",formatSelectBeans.get(currentFormat).getTitle()));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReleaseEvent event) {
        if(event.getEvent() == 0){
            finish();
        }
    }
}
