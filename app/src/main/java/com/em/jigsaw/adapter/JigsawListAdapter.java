package com.em.jigsaw.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.em.jigsaw.R;
import com.em.jigsaw.base.YBaseAdapter;
import com.em.jigsaw.base.YBaseHolder;
import com.em.jigsaw.bean.JigsawImgBean;
import com.em.jigsaw.bean.JigsawListBean;

import java.util.List;

/**
 * Time ： 2018/12/6 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class JigsawListAdapter extends YBaseAdapter<JigsawListBean> {

    public JigsawListAdapter(List<JigsawListBean> list, Context mContext) {
        super(list, mContext);
    }

    @Override
    public YBaseHolder initHolder() {
        return new ArticleHolder(mContext, mList);
    }

    private class ArticleHolder extends YBaseHolder<JigsawListBean> {

        ImageView ivJigsaw,ivHead;
        TextView tvUserName,tvCreatTime,tvContent;

        public ArticleHolder(Context mContext, List<JigsawListBean> mLists) {
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
            return view;
        }

        @Override
        public void bindData(final int position) {

        }
    }
}
