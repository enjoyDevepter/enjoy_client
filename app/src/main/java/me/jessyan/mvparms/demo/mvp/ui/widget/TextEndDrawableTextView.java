package me.jessyan.mvparms.demo.mvp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;

import java.lang.ref.WeakReference;

import static android.text.style.DynamicDrawableSpan.ALIGN_BASELINE;

public class TextEndDrawableTextView extends android.support.v7.widget.AppCompatTextView {
    private static final String dot = "...  ";
    private static final String IMAGE = "/img";
    private SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
    private Drawable drawable;
    private String str;
    private int viewWidth = 0;

    public TextEndDrawableTextView(Context context) {
        super(context);
    }

    public TextEndDrawableTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextEndDrawableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
        setContentInner();
    }

    public void setContentStr(String str) {
        this.str = str;
        setContentInner();
    }


    private void setContentInner() {
        if (spannableStringBuilder == null || viewWidth == 0) {
            return;
        }
        spannableStringBuilder.clear();
        if (TextUtils.isEmpty(str)) {
            str = "";
        }

        TextPaint paint = getPaint();
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        boolean hasdot = false;
        int drawableWidth = drawable == null ? 0 : drawable.getIntrinsicWidth();
        while (rect.width() + drawableWidth + (hasdot ? paint.measureText(dot) : 0) + 20 > viewWidth * 2) {
            str = str.substring(0, str.length() - 1);
            paint.getTextBounds(str, 0, str.length(), rect);
            hasdot = true;
        }

        if (TextUtils.isEmpty(str)) {
            super.setText("");
            return;
        }

        spannableStringBuilder.append(str);

        if (hasdot) {
            spannableStringBuilder.append(dot);
        }

        if (drawable != null) {
            Spannable content = new SpannableStringBuilder("占位");
            CenterImageSpan span = new CenterImageSpan(drawable, ALIGN_BASELINE);
// 用ImageSpan替换文本
            content.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.append(content);
        }

        super.setText(spannableStringBuilder);
    }

    public class CenterImageSpan extends ImageSpan {

        private WeakReference<Drawable> mDrawableRef;

        public CenterImageSpan(Context context, int resourceId, int verticalAlignment) {
            super(context, resourceId, verticalAlignment);
        }

        public CenterImageSpan(Drawable drawable, int verticalAlignment) {
            super(drawable, verticalAlignment);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
        }

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end,
                           Paint.FontMetricsInt fontMetricsInt) {
            Drawable drawable = getDrawable();
            Rect rect = drawable.getBounds();
            if (fontMetricsInt != null) {
                Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
                int fontHeight = fmPaint.descent - fmPaint.ascent;
                int drHeight = rect.bottom - rect.top;
                int centerY = fmPaint.ascent + fontHeight / 2;

                fontMetricsInt.ascent = centerY - drHeight / 2;
                fontMetricsInt.top = fontMetricsInt.ascent;
                fontMetricsInt.bottom = centerY + drHeight / 2;
                fontMetricsInt.descent = fontMetricsInt.bottom;
            }
            return rect.right;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y,
                         int bottom, Paint paint) {
            Drawable drawable = getCachedDrawable();
            canvas.save();
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.descent - fmPaint.ascent;
            int centerY = y + fmPaint.descent - fontHeight / 2;
            int transY = centerY - (drawable.getBounds().bottom - drawable.getBounds().top) / 2;
            canvas.translate(x, transY);
            drawable.draw(canvas);
            canvas.restore();
        }

        private Drawable getCachedDrawable() {
            WeakReference<Drawable> wr = mDrawableRef;
            Drawable d = null;
            if (wr != null) {
                d = wr.get();
            }

            if (d == null) {
                d = getDrawable();
                mDrawableRef = new WeakReference<>(d);
            }

            return d;
        }
    }

}
