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
import com.em.jigsaw.callback.OnJigsawChangedListener;
import com.em.jigsaw.utils.ImgUtil;

import java.util.ArrayList;

/**
 * Time ： 2018/12/6 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class JigsawView extends ViewGroup {
    private static final String TAG = "JigsawView";

    private Context mContext;
    private ImgUtil imgUtil;
    private int[] location = new int[2];// 记录JigsawView在父控件的位置

    private int contentHeight = 0;// 记录内容的高度
    private int lineWidth = 0;// 记录行的宽度
    private int maxLineWidth = 0;// 记录最宽的行宽
    private int maxItemHeight = 0;// 记录一行中item高度最大的高度

    private double initialLeft,initialTop;// 初始偏移量
    private double itemHeight,itemWidth;// 每个View的尺寸

    private ArrayList<JigsawImgBean> mLabels = new ArrayList<>();

    private View dragView = null;
    private ImageView dragImageView = null;
    private boolean validClick = false;// 有效点击
    private int startY,startX,endX,endY;
    private int startIndex = -1,endIndex = -1,curIndex = -1;

    private int groupViewLeft,groupViewTop;// 当前ViewGroup的左边距和上边距

    private OnJigsawChangedListener onJigsawChangedListener;

    public JigsawView(Context context) {
        super(context);
        init(context);
    }

    public JigsawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public JigsawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.mContext = context;
        imgUtil = new ImgUtil(context);
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

            // 记录当前View在父控件的位置
            double[] arrayImgPosition = {initialLeft + x, initialTop + y, initialLeft + x + view.getMeasuredWidth(), initialTop + y + view.getMeasuredHeight()};
            mLabels.get(i).setImgPosition(arrayImgPosition);

            view.layout((int)arrayImgPosition[0], (int)arrayImgPosition[1], (int)arrayImgPosition[2], (int)arrayImgPosition[3]);
        }
    }

    /**
     * 设置标签内容
     */
    public void setLabels(ArrayList<JigsawImgBean> labels) {
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

    public void updateLabels(){
        // 清空Labels
        removeAllViews();
        for (int i = 0; i < mLabels.size(); i++) {
            addLabel(mLabels.get(i), i);
        }
        addDragView();
    }

    /**
     * 刷新Label选中状态
     */
    public void updateLabelsTouchStatus(){
        int count = getChildCount() - 1;
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if(curIndex == i){
                view.setAlpha(0.7f);
            }else{
                view.setAlpha(1.0f);
            }
        }
    }

    /**
     * 设置View变化事件监听
     */
    public void setOnChangedListener(OnJigsawChangedListener onJigsawChangedListener){
        this.onJigsawChangedListener = onJigsawChangedListener;
    }

    /**
     * 初始化拖动控件
     */
    private void initDragView(){
        getLocationOnScreen(location);
        groupViewLeft = location[0];
        groupViewTop = location[1];

        addDragView();
        setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //获取当前按下的坐标
                        startX = (int) event.getRawX() - groupViewLeft;
                        startY = (int) event.getRawY() - groupViewTop;
                        Log.d(TAG,"startX = " + startX);
                        Log.d(TAG,"startY = " + startY);

                        startIndex = getTouchView(startX, startY);
                        Log.d(TAG,"startIndex = " + startIndex);

                        if(startIndex != -1){
                            validClick = true;
                            Glide.with(mContext).load(mLabels.get(startIndex).getImgPath()).into(dragImageView);
                        }else{
                            validClick = false;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //获取移动后的坐标
                        if(validClick){
                            int moveIndex = getTouchView((int) event.getRawX() - groupViewLeft, (int) event.getRawY() - groupViewTop);
                            if(moveIndex != curIndex){
                                curIndex = moveIndex;
                                updateLabelsTouchStatus();
                            }

                            double left = event.getRawX() - groupViewLeft - (itemWidth / 2);
                            double top = event.getRawY() - groupViewTop - (itemHeight / 2);
                            double right = left + dragImageView.getMeasuredWidth();
                            double bottom = top + dragImageView.getMeasuredHeight();
                            dragView.layout((int)left, (int)top, (int)right, (int)bottom);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = (int) event.getRawX() - groupViewLeft;
                        endY = (int) event.getRawY() - groupViewTop;
                        Log.d(TAG,"endX = " + endX);
                        Log.d(TAG,"endY = " + endY);

                        endIndex = getTouchView(endX, endY);
                        Log.d(TAG,"endIndex = " + endIndex);

                        validClick = false;
                        if(startIndex != -1 && endIndex != -1){
                            ArrayList<JigsawImgBean> newLabels = imgUtil.swapImgArray(mLabels,startIndex,endIndex);
                            mLabels.clear();
                            mLabels.addAll(newLabels);
                        }
                        updateLabels();

                        if(onJigsawChangedListener != null){
                            onJigsawChangedListener.onChanged(mLabels);
                        }
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 添加拖动控件
     */
    private void addDragView(){
        dragView = View.inflate(mContext, R.layout.item_jigsaw_view, null);
        dragImageView = dragView.findViewById(R.id.iv_content);
        RelativeLayout.LayoutParams linearParams =(RelativeLayout.LayoutParams) dragImageView.getLayoutParams();
        linearParams.height = (int) itemHeight;
        linearParams.width = (int) itemWidth;
        dragImageView.setLayoutParams(linearParams);
        dragView.setTag(-1);
        addView(dragView);
    }

    private void addLabel(JigsawImgBean bean,int position){
        final View view = View.inflate(mContext, R.layout.item_jigsaw_view, null);
        final ImageView ivContent = view.findViewById(R.id.iv_content);

        double screenRate = (double) getMeasuredWidth() / (double) getMeasuredHeight();// 屏幕比例
        double picRate = (double)bean.getImgFormat()[0] / (double)bean.getImgFormat()[1];// 图片比例

        //计算每个View的尺寸
        RelativeLayout.LayoutParams linearParams =(RelativeLayout.LayoutParams) ivContent.getLayoutParams();
        if(screenRate > picRate){
            itemHeight = (double) getMeasuredHeight() / (double) bean.getCropFormat()[0];
            itemWidth = itemHeight * ((double)bean.getImgFormat()[0] / (double)bean.getCropFormat()[1]) / ((double)bean.getImgFormat()[1] / (double)bean.getCropFormat()[0]);
        }else{
            itemWidth = (double) getMeasuredWidth() / (double) bean.getCropFormat()[1];
            itemHeight = itemWidth * ((double)bean.getImgFormat()[1] / (double)bean.getCropFormat()[0]) / ((double)bean.getImgFormat()[0] / (double)bean.getCropFormat()[1]);
        }
        linearParams.height = (int) itemHeight;
        linearParams.width = (int) itemWidth;
        ivContent.setLayoutParams(linearParams);

        //计算初始的偏移量，用于设置内容居中显示
        initialLeft = (getMeasuredWidth() - itemWidth*bean.getCropFormat()[1]) / 2;
        initialTop = (getMeasuredHeight() - itemHeight*bean.getCropFormat()[0]) / 2;

        Glide.with(mContext).load(bean.getImgPath()).into(ivContent);

        //label通过tag保存自己的位置(position)
        view.setTag(position);
        addView(view);
    }

    /**
     * 获取被选择的View
     */
    private int getTouchView(int x, int y){
        for(int i = 0;i < mLabels.size();i++){
            JigsawImgBean bean = mLabels.get(i);
            double[] array = bean.getImgPosition();
            if(array[0] < x && x < array[2] && array[1] < y && y < array[3]){
                return i;
            }
        }
        return -1;
    }
}
