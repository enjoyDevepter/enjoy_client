package me.jessyan.mvparms.demo.mvp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.jess.arms.utils.ArmsUtils;

import java.util.List;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.GrowthInfo;

/**
 * Created by guomin on 2018/9/22.
 */

public class GrowthView extends View {

    float lastX = 0;
    private Scroller mScroller;
    private List<GrowthInfo> growthInfoList;
    private int padding = ArmsUtils.getDimens(getContext(), R.dimen.growth_padding);
    private int top_divider = ArmsUtils.getDimens(getContext(), R.dimen.growth_top_divider);
    private int buttom_divider = ArmsUtils.getDimens(getContext(), R.dimen.growth_buttom_divider);
    private int textSize = ArmsUtils.getDimens(getContext(), R.dimen.growth_level_text_size);
    private int item_height = ArmsUtils.getDimens(getContext(), R.dimen.growth_height);
    private int item_width = ArmsUtils.getDimens(getContext(), R.dimen.growth_width);
    private int item_split = ArmsUtils.getDimens(getContext(), R.dimen.growth_height);
    private int level_color = ArmsUtils.getColor(getContext(), R.color.growth_level_color);
    private int growth_color = ArmsUtils.getColor(getContext(), R.color.growth_growth_color);
    private int current_progress = ArmsUtils.getColor(getContext(), R.color.growth_progress_color);
    private int progress = ArmsUtils.getColor(getContext(), R.color.growth_progress_nor_color);
    private Paint paint;
    private int contentWidth;

    public GrowthView(Context context) {
        this(context, null);
    }

    public GrowthView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GrowthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        mScroller = new Scroller(getContext());

    }

    public void setGrowthInfoList(List<GrowthInfo> growthInfoList) {
        this.growthInfoList = growthInfoList;
        contentWidth = 4 * padding + growthInfoList.size() * item_width + (growthInfoList.size() - 1) * item_split;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        if (null != growthInfoList) {
            int start = 0;
            if (contentWidth > getWidth()) {
                start = 2 * padding;
            } else {
                start = (getWidth() - contentWidth) / 2;
            }
            for (int i = 0; i < growthInfoList.size(); i++) {
                // 绘制进度
                GrowthInfo growthInfo = growthInfoList.get(i);
                paint.setColor(progress);
                int begin = start + i * item_split + i * item_width;
                canvas.drawRect(begin, (height - item_height) / 2, begin + item_width, (height - item_height) / 2 + item_height, paint);
                paint.setColor(current_progress);
                canvas.drawRect(begin, (height - item_height) / 2, begin + item_width * (float) growthInfo.getPercent() / 100, (height - item_height) / 2 + item_height, paint);

                // 绘制level
                paint.setColor(level_color);
                String name = growthInfo.getName();
                Rect rect = new Rect();
                paint.getTextBounds(name, 0, name.length(), rect);
                canvas.drawText(growthInfo.getName(), begin - item_split - rect.width() / 2, height / 2 - top_divider - ArmsUtils.getBaseline(paint), paint);

                // 绘制growth
                paint.setColor(growth_color);
                String growth = growthInfo.getGrowth() + "";
                paint.getTextBounds(growth, 0, growth.length(), rect);
                canvas.drawText(growth, begin - item_split - rect.width() / 2, height / 2 + item_height / 2 + buttom_divider + rect.height() - ArmsUtils.getBaseline(paint), paint);

            }
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {//判断Scroller是否执行完毕
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                //计算偏移量
                float offsetX = x - lastX;
                System.out.println("getScrollX " + getScrollX() + "    offsetX   " + offsetX + "    getWidth()  " + getWidth() + "   contentWidth() " + contentWidth);
                if (contentWidth > getWidth() && getScrollX() >= 0 && getScrollX() <= contentWidth - getWidth()) {
                    if (getScrollX() <= contentWidth - getWidth() && getScrollX() >= getWidth() - contentWidth) {
                        scrollTo((int) -offsetX, 0);
                    } else {
                        if (getScrollX() > 0) {
                            scrollTo(contentWidth - getWidth(), 0);
                        } else {
                            scrollTo(getWidth() - contentWidth, 0);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (getScrollX() < 0) {
                    setScrollX(0);
                }
                if (contentWidth > getWidth() && getScrollY() >= contentWidth - getWidth()) {
                    setScrollX(contentWidth - getWidth());
                }
                System.out.println("mScroller.getCurrX() " + mScroller.getCurrX());

//                scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
//                postInvalidate();
                break;
        }
        return true;
    }
}
