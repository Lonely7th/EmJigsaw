package com.em.jigsaw.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.base.YBaseAdapter;
import com.em.jigsaw.base.YBaseHolder;
import com.em.jigsaw.bean.FormatSelectBean;
import com.em.jigsaw.bean.LabelBean;

import java.util.List;

/**
 * Time ： 2018/12/30 0030 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class LabelSelectAdapter extends YBaseAdapter<LabelBean> {

    public LabelSelectAdapter(List<LabelBean> list, Context mContext) {
        super(list, mContext);
    }

    @Override
    public YBaseHolder initHolder() {
        return new MyHolder(mContext, mList);
    }

    private class MyHolder extends YBaseHolder<LabelBean> {

        TextView tvTitle;

        public MyHolder(Context mContext, List<LabelBean> mLists) {
            super(mContext, mLists);
        }

        @Override
        public View getInflateView(Context mContext) {
            View view = View.inflate(mContext, R.layout.item_label_select, null);
            tvTitle = view.findViewById(R.id.tv_title);
            return view;
        }

        @Override
        public void bindData(final int position) {
            tvTitle.setText(mLists.get(position).getTitle());
        }
    }
}
