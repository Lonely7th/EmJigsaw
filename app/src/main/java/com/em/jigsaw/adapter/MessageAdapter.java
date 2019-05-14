package com.em.jigsaw.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.base.YBaseAdapter;
import com.em.jigsaw.base.YBaseHolder;
import com.em.jigsaw.bean.MessageBean;

import java.util.List;

/**
 * Time ： 2019/5/13 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class MessageAdapter extends YBaseAdapter<MessageBean> {

    public MessageAdapter(List<MessageBean> list, Context mContext) {
        super(list, mContext);
    }

    @Override
    public YBaseHolder initHolder() {
        return new MyHolder(mContext, mList);
    }

    private class MyHolder extends YBaseHolder<MessageBean> {

        TextView tvTitle,tvContent,tvTime;

        public MyHolder(Context mContext, List<MessageBean> mLists) {
            super(mContext, mLists);
        }

        @Override
        public View getInflateView(Context mContext) {
            View view = View.inflate(mContext, R.layout.item_message, null);
            tvTitle = view.findViewById(R.id.tv_title);
            tvContent = view.findViewById(R.id.tv_content);
            tvTime = view.findViewById(R.id.tv_time);
            return view;
        }

        @Override
        public void bindData(final int position) {
            MessageBean bean = mLists.get(position);
            tvTitle.setText(bean.getTitle());
            tvContent.setText(bean.getContent());
            tvTime.setText(bean.getTime());
        }
    }
}
