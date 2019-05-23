package com.em.jigsaw.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.em.jigsaw.R;
import com.em.jigsaw.base.ServiceAPI;
import com.em.jigsaw.base.YBaseAdapter;
import com.em.jigsaw.base.YBaseHolder;
import com.em.jigsaw.bean.JNoteBean;
import com.em.jigsaw.callback.OnJListHeadClickListener;
import com.em.jigsaw.utils.FontUtil;
import com.em.jigsaw.utils.ImgUtil;
import com.em.jigsaw.utils.TimerUtil;

import java.util.List;

/**
 * Time ： 2018/12/6 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class JigsawListAdapter extends YBaseAdapter<JNoteBean> {

    private OnJListHeadClickListener onJListHeadClickListener;

    public JigsawListAdapter(List<JNoteBean> list, Context mContext, @NonNull OnJListHeadClickListener onJListHeadClickListener) {
        super(list, mContext);
        this.onJListHeadClickListener = onJListHeadClickListener;
    }

    @Override
    public YBaseHolder initHolder() {
        return new MyHolder(mContext, mList);
    }

    private class MyHolder extends YBaseHolder<JNoteBean> {

        ImageView ivJigsaw,ivHead;
        TextView tvUserName,tvCreatTime,tvContent,tvCropFormat,tvContent2,tvContent3;
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
            tvContent2 = view.findViewById(R.id.tv_content_2);
            tvContent3 = view.findViewById(R.id.tv_content_3);
            tvCropFormat = view.findViewById(R.id.tv_crop_format);

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
                ImgUtil.loadImg2Account(mContext,baen.getUserHead().startsWith("http")?baen.getUserHead(): ServiceAPI.IMAGE_URL + baen.getUserHead(),ivHead);
            }

            tvCreatTime.setText(TimerUtil.timeStamp2Date2(baen.getCreatTime()));

            StringBuilder sbLimit = new StringBuilder();
            switch (baen.getJType()){
                case "0":
                    tvContent.setText(baen.getContent());
                    break;
                case "1":
                    FontUtil.setHighlightTextView("使用10个贝壳开始挑战", "10", tvContent, mContext.getResources().getColor(R.color.scoreA));
                    FontUtil.setHighlightTextView("挑战成功可获得20个贝壳", "20", tvContent2, mContext.getResources().getColor(R.color.scoreA));
                    sbLimit.append("时间限制：").append(baen.getLimitNum()).append("秒");
                    break;
                case "2":
                    FontUtil.setHighlightTextView("使用10个贝壳开始挑战", "10", tvContent, mContext.getResources().getColor(R.color.scoreA));
                    FontUtil.setHighlightTextView("挑战成功可获得20个贝壳", "20", tvContent2, mContext.getResources().getColor(R.color.scoreA));
                    sbLimit.append("次数限制：").append(baen.getLimitNum()).append("次");
                    break;
            }
            tvCropFormat.setText("格式：" + baen.getCropFormat() + "    " + sbLimit.toString());
            tvContent3.setText(baen.getContent());

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

            ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onJListHeadClickListener.onClick(position);
                }
            });

        }
    }
}
