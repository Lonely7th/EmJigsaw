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

import java.util.List;

/**
 * Time ： 2018/12/18 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class FormatSelectAdapter extends YBaseAdapter<FormatSelectBean> {

    public FormatSelectAdapter(List<FormatSelectBean> list, Context mContext) {
        super(list, mContext);
    }

    @Override
    public YBaseHolder initHolder() {
        return new MyHolder(mContext, mList);
    }

    private class MyHolder extends YBaseHolder<FormatSelectBean> {

        TextView tvTitle;
        ImageView ivFormat;

        public MyHolder(Context mContext, List<FormatSelectBean> mLists) {
            super(mContext, mLists);
        }

        @Override
        public View getInflateView(Context mContext) {
            View view = View.inflate(mContext, R.layout.item_format_select, null);
            tvTitle = view.findViewById(R.id.tv_title);
            ivFormat = view.findViewById(R.id.iv_format);
            return view;
        }

        @Override
        public void bindData(final int position) {
            tvTitle.setText(mLists.get(position).getTitle());
            if(mList.get(position).isSelect()){
                ivFormat.setBackgroundResource(R.mipmap.icon_grid_on_b);
                tvTitle.setTextColor(Color.parseColor("#1296db"));
            }else{
                ivFormat.setBackgroundResource(R.mipmap.icon_grid_on_w);
                tvTitle.setTextColor(Color.parseColor("#ffffff"));
            }
        }
    }
}
