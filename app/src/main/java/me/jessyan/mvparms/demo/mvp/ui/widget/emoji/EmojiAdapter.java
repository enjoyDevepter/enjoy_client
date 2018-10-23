package me.jessyan.mvparms.demo.mvp.ui.widget.emoji;


import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

import me.jessyan.mvparms.demo.R;

public class EmojiAdapter extends BaseAdapter {

    private final float mPerWidth;
    private final float mPerHeight;
    private final float mIvSize;
    private Context mContext;
    private int mStartIndex;
    private int mEmotionLayoutWidth;
    private int mEmotionLayoutHeight;
    private Emoji[] emojis;

    public EmojiAdapter(Context context, int emotionLayoutWidth, int emotionLayoutHeight, int startIndex) {
        mContext = context;
        mStartIndex = startIndex;
        mEmotionLayoutWidth = emotionLayoutWidth;
        mEmotionLayoutHeight = emotionLayoutHeight - dip2px(context, 35 + 26 + 50);//减去底部的tab高度、小圆点的高度才是viewpager的高度，再减少30dp是让表情整体的顶部和底部有个外间距

        mPerWidth = mEmotionLayoutWidth * 1f / EmotionLayout.EMOJI_COLUMNS;
        mPerHeight = mEmotionLayoutHeight * 1f / EmotionLayout.EMOJI_ROWS;
        float ivWidth = mPerWidth * .6f;
        float ivHeight = mPerHeight * .6f;
        mIvSize = Math.min(ivWidth, ivHeight);
        emojis = new Emoji[EmojiManager.getAll().size()];
        EmojiManager.getAll().toArray(emojis);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public int getCount() {
        int count = EmojiManager.getAll().size() - mStartIndex + 1;
        count = Math.min(count, EmotionLayout.EMOJI_PER_PAGE + 1);
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return mStartIndex + position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout rl = new RelativeLayout(mContext);
        rl.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, (int) mPerHeight));

        View childV = new View(mContext);
        int count = EmojiManager.getAll().size();
        int index = mStartIndex + position;
        if (position == EmotionLayout.EMOJI_PER_PAGE || index == count) {
            childV.setBackgroundResource(R.mipmap.ic_emoji_del);
        } else if (index < count) {
            childV = new TextView(mContext);
            TextView textView = ((TextView) childV);
            textView.setGravity(Gravity.CENTER);
            textView.setText(emojis[index].getUnicode());
            textView.setTextSize(23);
        }

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.width = (int) mIvSize;
        layoutParams.height = (int) mIvSize;
        childV.setLayoutParams(layoutParams);

        rl.setGravity(Gravity.CENTER);
        rl.addView(childV);

        return rl;
    }
}
