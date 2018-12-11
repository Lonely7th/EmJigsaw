package com.em.jigsaw.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.em.jigsaw.R;
import com.em.jigsaw.bean.JigsawImgBean;

import java.util.ArrayList;

/**
 * Time ： 2018/12/6 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class JigsawView extends ViewGroup{
    private static final String TAG = "JigsawView";

    private Context mContext;
    private int[] format = null;

    private ArrayList<JigsawImgBean> mLabels = new ArrayList<>();

    public JigsawView(Context context) {
        super(context);
        this.mContext = context;
    }

    public JigsawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public JigsawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int count = getChildCount();
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();

        int contentHeight = 0; //记录内容的高度
        int lineWidth = 0; //记录行的宽度
        int maxLineWidth = 0; //记录最宽的行宽
        int maxItemHeight = 0; //记录一行中item高度最大的高度

        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);

            if (maxWidth < lineWidth + view.getMeasuredWidth()) {
                contentHeight += maxItemHeight;
                maxItemHeight = 0;
                maxLineWidth = Math.max(maxLineWidth, lineWidth);
                lineWidth = 0;
            }
            maxItemHeight = Math.max(maxItemHeight, view.getMeasuredHeight());
            lineWidth += view.getMeasuredWidth();
        }

        contentHeight += maxItemHeight;
        maxLineWidth = Math.max(maxLineWidth, lineWidth);

        setMeasuredDimension(measureWidth(widthMeasureSpec, maxLineWidth),
                measureHeight(heightMeasureSpec, contentHeight));
    }

    private int measureWidth(int measureSpec, int contentWidth) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = contentWidth + getPaddingLeft() + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        result = Math.max(result, getSuggestedMinimumWidth());
        return result;
    }

    private int measureHeight(int measureSpec, int contentHeight) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = contentHeight + getPaddingTop() + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        result = Math.max(result, getSuggestedMinimumHeight());
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);

            int x = mLabels.get(i).getIndexArray()[1] * view.getMeasuredWidth();
            int y = mLabels.get(i).getIndexArray()[0] * view.getMeasuredHeight();

            view.layout(x, y, x + view.getMeasuredWidth(), y + view.getMeasuredHeight());
        }
    }

    /**
     * 设置标签内容
     */
    public void setLabels(ArrayList<JigsawImgBean> labels,int[] format) {
        this.format = format;
        if(format == null){
            throw new NullPointerException("");
        }

        // 清空Labels
        removeAllViews();
        mLabels.clear();

        if (labels != null) {
            mLabels.addAll(labels);
            int size = labels.size();
            for (int i = 0; i < size; i++) {
                addLabel(labels.get(i), i);
            }
        }
    }

    public void updateLabels(ArrayList<JigsawImgBean> labels){

    }

    private void addLabel(final JigsawImgBean bean,final int position){
        final View view = View.inflate(mContext, R.layout.item_jigsaw_view, null);
        final ImageView ivContent = view.findViewById(R.id.iv_content);

        Log.d(TAG,"getMeasuredWidth() = " + getMeasuredWidth());
        Log.d(TAG,"getMeasuredHeight() = " + getMeasuredHeight());
        RelativeLayout.LayoutParams linearParams =(RelativeLayout.LayoutParams) ivContent.getLayoutParams();
        linearParams.height = getMeasuredHeight() / 4;
        linearParams.width = getMeasuredWidth() / 3;
        ivContent.setLayoutParams(linearParams);

        Glide.with(mContext).load(bean.getImgPath()).into(ivContent);

        //label通过tag保存自己的位置(position)
        view.setTag(position);
        addView(view);
    }
}
