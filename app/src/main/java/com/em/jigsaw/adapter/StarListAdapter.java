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
import com.em.jigsaw.bean.NoteStarBean;
import com.em.jigsaw.utils.TimerUtil;

import java.util.List;

/**
 * Time ： 2019/1/3 0003 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class StarListAdapter extends YBaseAdapter<NoteStarBean> {

    public StarListAdapter(List<NoteStarBean> list, Context mContext) {
        super(list, mContext);
    }

    @Override
    public YBaseHolder initHolder() {
        return new MyHolder(mContext, mList);
    }

    private class MyHolder extends YBaseHolder<NoteStarBean> {

        ImageView ivJigsaw,ivHead;
        TextView tvUserName,tvCreatTime,tvContent,tvCropFormat;
        TextView tvLabel1,tvLabel2,tvLabel3;

        public MyHolder(Context mContext, List<NoteStarBean> mLists) {
            super(mContext, mLists);
        }

        @Override
        public View getInflateView(Context mContext) {
            View view = View.inflate(mContext, R.layout.item_star_list, null);
            ivJigsaw = view.findViewById(R.id.iv_jigsaw);
            ivHead = view.findViewById(R.id.iv_head);
            tvUserName = view.findViewById(R.id.tv_user_name);
            tvCreatTime = view.findViewById(R.id.tv_creat_time);
            tvContent = view.findViewById(R.id.tv_content);
            tvCropFormat = view.findViewById(R.id.tv_crop_format);

            tvLabel1 = view.findViewById(R.id.tv_label_1);
            tvLabel2 = view.findViewById(R.id.tv_label_2);
            tvLabel3 = view.findViewById(R.id.tv_label_3);
            return view;
        }

        @Override
        public void bindData(final int position) {
            JNoteBean baen = mLists.get(position).getjNoteBean();
            if(baen.isHideUser()){
                tvUserName.setText("匿名用户");
                Glide.with(mContext).load(R.mipmap.icon_account_circle).into(ivHead);
            }else{
                tvUserName.setText(baen.getUserName());
                Glide.with(mContext).load(baen.getUserHead().startsWith("http")?baen.getUserHead(): ServiceAPI.IMAGE_URL + baen.getUserHead()).into(ivHead);
            }

            tvCreatTime.setText(TimerUtil.timeStamp2Date(baen.getCreatTime()));
            tvContent.setText(baen.getContent());

            StringBuilder sbLimit = new StringBuilder();
            switch (baen.getJType()){
                case "0":
                    tvContent.setText(baen.getContent());
//                    tvContent.setTextColor(mContext.getResources().getColor(R.color.colorGary_S));
                    break;
                case "1":
                    tvContent.setText("当前最佳：" + baen.getBestResults() + " 秒");
//                    tvContent.setTextColor(mContext.getResources().getColor(R.color.scoreS));
                    sbLimit.append("时间限制：").append(baen.getLimitNum()).append("秒");
                    break;
                case "2":
                    tvContent.setText("当前最佳 " + baen.getBestResults() + " 次");
//                    tvContent.setTextColor(mContext.getResources().getColor(R.color.scoreS));
                    sbLimit.append("次数限制：").append(baen.getLimitNum()).append("次");
                    break;
            }
            tvCropFormat.setText("格式：" + baen.getCropFormat() + "    " + sbLimit.toString());

            Glide.with(mContext).load(baen.getGsResPath().startsWith("http")?baen.getGsResPath(): ServiceAPI.IMAGE_URL + baen.getGsResPath()).into(ivJigsaw);

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
        }
    }
}
