package com.em.jigsaw.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.base.YBaseAdapter;
import com.em.jigsaw.base.YBaseHolder;
import com.em.jigsaw.bean.PayEventBean;

import java.util.List;

/**
 * Time ： 2019/5/14 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class ShopDialogAdapter extends YBaseAdapter<PayEventBean> {

    public ShopDialogAdapter(List<PayEventBean> list, Context mContext) {
        super(list, mContext);
    }

    @Override
    public YBaseHolder initHolder() {
        return new MyHolder(mContext, mList);
    }

    private class MyHolder extends YBaseHolder<PayEventBean> {

        TextView tvContent,tvPrice;
        Button btnPay;

        public MyHolder(Context mContext, List<PayEventBean> mLists) {
            super(mContext, mLists);
        }

        @Override
        public View getInflateView(Context mContext) {
            View view = View.inflate(mContext, R.layout.item_dialog_shop, null);
            tvContent = view.findViewById(R.id.tv_content);
            tvPrice = view.findViewById(R.id.tv_original_price);
            btnPay = view.findViewById(R.id.btn_pay);
            tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            return view;
        }

        @Override
        public void bindData(final int position) {
            tvContent.setText(mLists.get(position).getContent());
            tvPrice.setText(mLists.get(position).getOriginalPrice());
            btnPay.setText(mLists.get(position).getPresentPrice());
        }
    }
}
