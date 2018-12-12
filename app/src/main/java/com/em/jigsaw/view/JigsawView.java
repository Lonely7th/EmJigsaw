package com.em.jigsaw.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
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
public class JigsawView extends ViewGroup {
    private static final String TAG = "JigsawView";

    private Context mContext;
    private int[] format = null;
    private int[] location = new int[2];// 记录JigsawView在父控件的位置

    private int contentHeight = 0;// 记录内容的高度
    private int lineWidth = 0;// 记录行的宽度
    private int maxLineWidth = 0;// 记录最宽的行宽
    private int maxItemHeight = 0;// 记录一行中item高度最大的高度

    private int initialLeft,initialTop;// 初始偏移量
    private int itemHeight,itemWidth;// 每个View的尺寸

    private ArrayList<JigsawImgBean> mLabels = new ArrayList<>();

    private View dragView = null;
    private ImageView dragImageView = null;

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
        int result;
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
        int result;
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
        int count = getChildCount() - 1;
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);

            int x = mLabels.get(i).getIndexArray()[1] * view.getMeasuredWidth();
            int y = mLabels.get(i).getIndexArray()[0] * view.getMeasuredHeight();

            view.layout(initialLeft + x, initialTop + y, initialLeft + x + view.getMeasuredWidth(), initialTop + y + view.getMeasuredHeight());
        }
    }

    /**
     * 设置标签内容
     */
    public void setLabels(ArrayList<JigsawImgBean> labels,int[] format) {
        this.format = format;
        if(format == null)
            throw new NullPointerException("");

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

        //初始化拖动控件
        initDragView();
    }

    /**
     * 初始化拖动控件
     */
    private void initDragView(){
        getLocationOnScreen(location);
        final int groupViewLeft = location[0];
        final int groupViewTop = location[1];
        Log.d(TAG,"groupViewLeft = " + groupViewLeft);
        Log.d(TAG,"groupViewTop = " + groupViewTop);

        dragView = View.inflate(mContext, R.layout.item_jigsaw_view, null);
        dragImageView = dragView.findViewById(R.id.iv_content);
        RelativeLayout.LayoutParams linearParams =(RelativeLayout.LayoutParams) dragImageView.getLayoutParams();
        linearParams.height = itemHeight;
        linearParams.width = itemWidth;
        dragImageView.setLayoutParams(linearParams);
        dragView.setTag(-1);
        addView(dragView);

        setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int startY,startX,endX,endY;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "ACTION_DOWN");
                        //获取当前按下的坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();

                        Glide.with(mContext).load(mLabels.get(0).getImgPath()).into(dragImageView);
                        Log.d(TAG,"startX = " + startX);
                        Log.d(TAG,"startY = " + startY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //获取移动后的坐标
                        int left = (int) event.getRawX() - groupViewLeft - (itemWidth / 2);
                        int top = (int) event.getRawY() - groupViewTop - (itemHeight / 2);
                        int right = left + dragImageView.getMeasuredWidth();
                        int bottom = top + dragImageView.getMeasuredHeight();
                        dragView.layout(left, top, right, bottom);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "ACTION_UP");
                        endX = (int) event.getRawX();
                        endY = (int) event.getRawY();
                        Log.d(TAG,"endX = " + endX);
                        Log.d(TAG,"endY = " + endY);
                        break;
                }
                return true;
            }
        });
    }

    private void addLabel(final JigsawImgBean bean,final int position){
        final View view = View.inflate(mContext, R.layout.item_jigsaw_view, null);
        final ImageView ivContent = view.findViewById(R.id.iv_content);

        double screenRate = (double) getMeasuredWidth() / (double) getMeasuredHeight();// 屏幕比例
        double picRate = (double)bean.getImgFormat()[0] / (double)bean.getImgFormat()[1];// 图片比例

        //计算每个View的尺寸
        RelativeLayout.LayoutParams linearParams =(RelativeLayout.LayoutParams) ivContent.getLayoutParams();
        if(screenRate > picRate){
            itemHeight = getMeasuredHeight() / format[0];
            itemWidth = itemHeight * (bean.getImgFormat()[0] / format[1]) / (bean.getImgFormat()[1] / format[0]);
        }else{
            itemWidth = getMeasuredWidth() / format[1];
            itemHeight = itemWidth * (bean.getImgFormat()[1] / format[0]) / (bean.getImgFormat()[0] / format[1]);
        }
        linearParams.height = itemHeight;
        linearParams.width = itemWidth;
        ivContent.setLayoutParams(linearParams);

        //计算初始的偏移量，用于设置内容居中显示
        initialLeft = (getMeasuredWidth() - itemWidth*format[1]) / 2;
        initialTop = (getMeasuredHeight() - itemHeight*format[0]) / 2;

        Glide.with(mContext).load(bean.getImgPath()).into(ivContent);

        //label通过tag保存自己的位置(position)
        view.setTag(position);
        addView(view);
    }
}
