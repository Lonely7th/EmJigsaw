package com.em.jigsaw.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.bean.MainTopBarBean;

import java.util.ArrayList;

/**
 * Time ： 2018/12/17 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class TopBarAdapter extends RecyclerView.Adapter<TopBarAdapter.MyViewHoler> implements View.OnClickListener {
    private Context mContext;
    private ArrayList<MainTopBarBean> list;
    private OnRecycleViewItemClickListener mOnItemClickListener;

    public TopBarAdapter(Context context, ArrayList<MainTopBarBean> data) {
        mContext = context;
        list = data;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoler holder, int position) {

        holder.tvTitle.setText(list.get(position).getTitle());
        if(list.get(position).isSelect()){
            holder.tvLine.setVisibility(View.VISIBLE);
            holder.tvTitle.setTextColor(Color.parseColor("#1296db"));
        }else{
            holder.tvLine.setVisibility(View.GONE);
            holder.tvTitle.setTextColor(Color.parseColor("#888888"));
        }
        holder.itemView.setTag(position);
    }

    @Override
    public MyViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        //负责创建视图
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_top_bar,null);
        view.setOnClickListener(this);
        return new MyViewHoler(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener!=null) {
            mOnItemClickListener.OnItemClick(v,(int)v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecycleViewItemClickListener listener) {
        this.mOnItemClickListener=listener;
    }
    public interface OnRecycleViewItemClickListener{
        void OnItemClick(View view,int position);
    }
    class MyViewHoler extends RecyclerView.ViewHolder {
        private final TextView tvTitle,tvLine;

        public MyViewHoler(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvLine = itemView.findViewById(R.id.tv_lines);
        }
    }
}
