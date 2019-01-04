package com.em.jigsaw.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.em.jigsaw.R;
import com.em.jigsaw.base.ServiceAPI;
import com.em.jigsaw.base.YBaseAdapter;
import com.em.jigsaw.base.YBaseHolder;
import com.em.jigsaw.bean.JNoteBean;
import com.em.jigsaw.utils.TimerUtil;

import java.util.List;

/**
 * Time ： 2018/12/6 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class JigsawListAdapter extends YBaseAdapter<JNoteBean> {

    public JigsawListAdapter(List<JNoteBean> list, Context mContext) {
        super(list, mContext);
    }

    @Override
    public YBaseHolder initHolder() {
        return new MyHolder(mContext, mList);
    }

    private class MyHolder extends YBaseHolder<JNoteBean> {

        ImageView ivJigsaw,ivHead;
        TextView tvUserName,tvCreatTime,tvContent,tvCropFormat,tvResult;
        TextView tvLabel1,tvLabel2,tvLabel3;

        public MyHolder(Context mContext, List<JNoteBean> mLists) {
            super(mContext, mLists);
        }

        @Override
        public View getInflateView(Context mContext) {
            View view = View.inflate(mContext, R.layout.item_jigsaw_list, null);
            ivJigsaw = view.findViewById(R.id.iv_jigsaw);
            ivHead = view.findViewById(R.id.iv_head);
            tvUserName = view.findViewById(R.id.tv_user_name);
            tvCreatTime = view.findViewById(R.id.tv_creat_time);
            tvContent = view.findViewById(R.id.tv_content);
            tvCropFormat = view.findViewById(R.id.tv_crop_format);
            tvResult = view.findViewById(R.id.tv_zan_status);

            tvLabel1 = view.findViewById(R.id.tv_label_1);
            tvLabel2 = view.findViewById(R.id.tv_label_2);
            tvLabel3 = view.findViewById(R.id.tv_label_3);
            return view;
        }

        @Override
        public void bindData(final int position) {
            JNoteBean baen = mLists.get(position);
            if(baen.isHideUser()){
                tvUserName.setText("匿名用户");
                Glide.with(mContext).load(R.mipmap.icon_account_circle).into(ivHead);
            }else{
                tvUserName.setText(baen.getUserName());
                Glide.with(mContext).load(baen.getUserHead()).into(ivHead);
            }

            tvCreatTime.setText(TimerUtil.timeStamp2Date(baen.getCreatTime()));
            tvContent.setText(baen.getContent());

            StringBuilder sbLimit = new StringBuilder();
            switch (baen.getJType()){
                case "1":
                    sbLimit.append("时间限制：").append(baen.getLimitNum()).append("秒");
                    break;
                case "2":
                    sbLimit.append("次数限制：").append(baen.getLimitNum()).append("次");
                    break;
            }
            tvCropFormat.setText("格式：" + baen.getCropFormat() + "    " + sbLimit.toString());

            Glide.with(mContext).load(baen.getResPath().startsWith("http")?baen.getResPath(): ServiceAPI.IMAGE_URL + baen.getResPath()).into(ivJigsaw);

            if(TextUtils.isEmpty(baen.getLabelTitle1())){
                tvLabel1.setVisibility(View.INVISIBLE);
            }else{
                tvLabel1.setVisibility(View.VISIBLE);
                tvLabel1.setText(baen.getLabelTitle1());
            }
            if(TextUtils.isEmpty(baen.getLabelTitle2())){
                tvLabel2.setVisibility(View.INVISIBLE);
            }else{
                tvLabel2.setVisibility(View.VISIBLE);
                tvLabel2.setText(baen.getLabelTitle2());
            }
            if(TextUtils.isEmpty(baen.getLabelTitle3())){
                tvLabel3.setVisibility(View.INVISIBLE);
            }else{
                tvLabel3.setVisibility(View.VISIBLE);
                tvLabel3.setText(baen.getLabelTitle3());
            }

            //展示次数
            tvResult.setText(baen.getDisplayNum() + "次挑战/" + baen.getCompleteNum() + "成功");
        }
    }
}
