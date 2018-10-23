package me.jessyan.mvparms.demo.mvp.ui.widget.emoji;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import me.jessyan.mvparms.demo.R;

/**
 * CSDN_LQR
 * 表情布局
 */
public class EmotionLayout extends LinearLayout implements View.OnClickListener {

    public static final int EMOJI_COLUMNS = 7;
    public static final int EMOJI_ROWS = 3;
    public static final int EMOJI_PER_PAGE = EMOJI_COLUMNS * EMOJI_ROWS - 1;//最后一个是删除键

    public static final int STICKER_COLUMNS = 4;
    public static final int STICKER_ROWS = 2;
    public static final int STICKER_PER_PAGE = STICKER_COLUMNS * STICKER_ROWS;
    EditText mMessageEditText;
    private int mMeasuredWidth;
    private int mMeasuredHeight;
    private int mTabPosi = 0;
    private Context mContext;
    private ViewPager mVpEmotioin;
    private LinearLayout mLlPageNumber;
    private LinearLayout mLlTabContainer;
    private View sendV;
    private int mTabCount;
    private SparseArray<View> mTabViewArray = new SparseArray<>();
    private IEmotionSelectedListener mEmotionSelectedListener;

    public EmotionLayout(Context context) {
        this(context, null);
    }

    public EmotionLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmotionLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setEmotionSelectedListener(IEmotionSelectedListener emotionSelectedListener) {
        if (emotionSelectedListener != null) {
            this.mEmotionSelectedListener = emotionSelectedListener;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init();
        initListener();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasuredWidth = measureWidth(widthMeasureSpec);
        mMeasuredHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(mMeasuredWidth, mMeasuredHeight);
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = dip2px(this.getContext(), 200);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = dip2px(this.getContext(), 200);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.emotion_layout, this);
        mVpEmotioin = (ViewPager) findViewById(R.id.vpEmotioin);
        mLlPageNumber = (LinearLayout) findViewById(R.id.llPageNumber);
        mLlTabContainer = (LinearLayout) findViewById(R.id.llTabContainer);
        sendV = findViewById(R.id.send);
        initTabs();

    }

    private void initTabs() {
        //默认添加一个表情tab
        EmotionTab emojiTab = new EmotionTab(mContext);
        mLlTabContainer.addView(emojiTab);
        mTabViewArray.put(0, emojiTab);
        selectTab(0);
    }

    private void initListener() {
        if (mLlTabContainer != null) {
            mTabCount = mLlTabContainer.getChildCount() - 1;//不包含最后的设置按钮
            for (int position = 0; position < mTabCount; position++) {
                View tab = mLlTabContainer.getChildAt(position);
                tab.setTag(position);
                tab.setOnClickListener(this);
            }
        }
        sendV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mEmotionSelectedListener) {
                    mEmotionSelectedListener.onSend();
                }
            }
        });

        mVpEmotioin.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setCurPageCommon(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void setCurPageCommon(int position) {
        setCurPage(position, 4);
    }

    private void setCurPage(int page, int pageCount) {
        int hasCount = mLlPageNumber.getChildCount();
        int forMax = Math.max(hasCount, pageCount);

        ImageView ivCur = null;
        for (int i = 0; i < forMax; i++) {
            if (pageCount <= hasCount) {
                if (i >= pageCount) {
                    mLlPageNumber.getChildAt(i).setVisibility(View.GONE);
                    continue;
                } else {
                    ivCur = (ImageView) mLlPageNumber.getChildAt(i);
                }
            } else {
                if (i < hasCount) {
                    ivCur = (ImageView) mLlPageNumber.getChildAt(i);
                } else {
                    ivCur = new ImageView(mContext);
                    ivCur.setBackgroundResource(R.drawable.selector_view_pager_indicator);
                    LayoutParams params = new LayoutParams(dip2px(mContext, 8), dip2px(mContext, 8));
                    ivCur.setLayoutParams(params);
                    params.leftMargin = dip2px(mContext, 3);
                    params.rightMargin = dip2px(mContext, 3);
                    mLlPageNumber.addView(ivCur);
                }
            }

            ivCur.setId(i);
            ivCur.setSelected(i == page);
            ivCur.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        mTabPosi = (int) v.getTag();
        selectTab(mTabPosi);
    }

    private void selectTab(int tabPosi) {
        mTabViewArray.get(tabPosi).setBackgroundResource(R.drawable.shape_tab_press);

        //显示表情内容
        fillVpEmotioin(tabPosi);
    }

    private void fillVpEmotioin(int tabPosi) {
        EmotionViewPagerAdapter adapter = new EmotionViewPagerAdapter(mMeasuredWidth, mMeasuredHeight, tabPosi, mEmotionSelectedListener);
        mVpEmotioin.setAdapter(adapter);
        mLlPageNumber.removeAllViews();
        setCurPageCommon(0);
        if (tabPosi == 0) {
            adapter.attachEditText(mMessageEditText);
        }
    }

    public void attachEditText(EditText messageEditText) {
        mMessageEditText = messageEditText;
    }
}

