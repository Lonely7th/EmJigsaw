package com.em.jigsaw.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.base.YBaseAdapter;
import com.em.jigsaw.base.YBaseHolder;

import java.util.List;

/**
 * Time ： 2018/12/17 0017 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class SelectDialogAdapter extends YBaseAdapter<String> {

    public SelectDialogAdapter(List<String> list, Context mContext) {
        super(list, mContext);
    }

    @Override
    public YBaseHolder initHolder() {
        return new MyHolder(mContext, mList);
    }

    private class MyHolder extends YBaseHolder<String> {

        TextView tvTitle;

        public MyHolder(Context mContext, List<String> mLists) {
            super(mContext, mLists);
        }

        @Override
        public View getInflateView(Context mContext) {
            View view = View.inflate(mContext, R.layout.item_dialog_select, null);
            tvTitle = view.findViewById(R.id.tv_title);
            return view;
        }

        @Override
        public void bindData(final int position) {
            tvTitle.setText(mLists.get(position));

        }
    }
}
