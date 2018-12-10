package com.em.jigsaw.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.em.jigsaw.R;
import com.em.jigsaw.base.YBaseAdapter;
import com.em.jigsaw.base.YBaseHolder;
import com.em.jigsaw.bean.JigsawImgBean;

import java.util.List;

/**
 * Time ： 2018/12/6 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class JigsawAdapter extends YBaseAdapter<JigsawImgBean> {

    public JigsawAdapter(List<JigsawImgBean> list, Context mContext) {
        super(list, mContext);
    }

    @Override
    public YBaseHolder initHolder() {
        return new ArticleHolder(mContext, mList);
    }

    private class ArticleHolder extends YBaseHolder<JigsawImgBean> {

        ImageView ivContent;

        public ArticleHolder(Context mContext, List<JigsawImgBean> mLists) {
            super(mContext, mLists);
        }

        @Override
        public View getInflateView(Context mContext) {
            View view = View.inflate(mContext, R.layout.item_jigsaw, null);
            ivContent = view.findViewById(R.id.iv_content);
            return view;
        }

        @Override
        public void bindData(final int position) {
            Glide.with(mContext)
                    .load(mLists.get(position).getImgPath())
                    .into(ivContent);
        }
    }
}
