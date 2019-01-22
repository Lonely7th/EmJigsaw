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
import com.em.jigsaw.bean.UserBean;

import java.util.List;

/**
 * Time ： 2019/1/22 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class FollowListAdapter extends YBaseAdapter<UserBean> {

    public FollowListAdapter(List<UserBean> list, Context mContext) {
        super(list, mContext);
    }

    @Override
    public YBaseHolder initHolder() {
        return new MyHolder(mContext, mList);
    }

    private class MyHolder extends YBaseHolder<UserBean> {

        ImageView ivHead;
        TextView tvUserName;

        public MyHolder(Context mContext, List<UserBean> mLists) {
            super(mContext, mLists);
        }

        @Override
        public View getInflateView(Context mContext) {
            View view = View.inflate(mContext, R.layout.item_follow_list, null);
            ivHead = view.findViewById(R.id.iv_head);
            tvUserName = view.findViewById(R.id.tv_user_name);
            return view;
        }

        @Override
        public void bindData(final int position) {
            UserBean bean = mLists.get(position);
            tvUserName.setText(bean.getUserName());
            Glide.with(mContext).load(bean.getNameHead().startsWith("http")?bean.getNameHead(): ServiceAPI.IMAGE_URL + bean.getNameHead()).into(ivHead);

        }
    }
}
