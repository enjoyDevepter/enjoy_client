package me.jessyan.mvparms.demo.mvp.ui.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jess.arms.utils.ArmsUtils;

import me.jessyan.mvparms.demo.R;

/**
 * Created by guomin on 2018/7/29.
 */

public class PlusSpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int leftRight;
    private int topBottom;

    private int divice_width = ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.divice_width);
    private int plusHeight = ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.plus_width);
    private Drawable plusDrawable = ArmsUtils.getDrawablebyResource(ArmsUtils.getContext(), R.mipmap.plus);

    public PlusSpacesItemDecoration(int leftRight, int topBottom) {
        this.leftRight = leftRight;
        this.topBottom = topBottom;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        canvas.save();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int height = child.getBottom();
            int width = child.getRight();

            //第一个ItemView不需要绘制
            if (i == 0) {
                continue;
            }
//            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
//            final int top = bottom - mDivider.getIntrinsicHeight();
            plusDrawable.setBounds(width - divice_width + (divice_width - plusHeight) / 2, (height - plusHeight) / 2, width - divice_width + (divice_width - plusHeight) / 2 + plusHeight / 2, (height - plusHeight) / 2 + plusHeight / 2);
            plusDrawable.draw(canvas);
        }
        canvas.restore();

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        //竖直方向的
        if (layoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
            //最后一项需要 bottom
            if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {
                outRect.bottom = topBottom;
            }
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = 0;
            } else {
                outRect.top = topBottom;
            }
            outRect.left = leftRight;
            outRect.right = leftRight;
        } else {
            //最后一项需要right
            if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {
                outRect.right = leftRight;
            }
            outRect.top = topBottom;
            outRect.left = leftRight;
            outRect.bottom = topBottom;
        }

    }
}
