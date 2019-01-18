package com.em.jigsaw.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.base.YBaseAdapter;
import com.em.jigsaw.base.YBaseHolder;
import com.em.jigsaw.bean.SelectCropFormatBean;

import java.util.List;

/**
 * Time ： 2019/1/18 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class SelectCropFormatAdapter extends YBaseAdapter<SelectCropFormatBean> {

    public SelectCropFormatAdapter(List<SelectCropFormatBean> list, Context mContext) {
        super(list, mContext);
    }

    @Override
    public YBaseHolder initHolder() {
        return new MyHolder(mContext, mList);
    }

    private class MyHolder extends YBaseHolder<SelectCropFormatBean> {

        TextView tvTitle;

        public MyHolder(Context mContext, List<SelectCropFormatBean> mLists) {
            super(mContext, mLists);
        }

        @Override
        public View getInflateView(Context mContext) {
            View view = View.inflate(mContext, R.layout.item_crop_format_select, null);
            tvTitle = view.findViewById(R.id.tv_title);
            return view;
        }

        @Override
        public void bindData(final int position) {
            tvTitle.setText(mLists.get(position).getContent());
            if(mLists.get(position).isSelect()){
                tvTitle.setTextColor(Color.parseColor("#1296db"));
                tvTitle.setBackgroundResource(R.drawable.bg_border_blue);
            }else{
                tvTitle.setTextColor(Color.parseColor("#888888"));
                tvTitle.setBackgroundResource(R.drawable.bg_border_gray);
            }
        }
    }
}
