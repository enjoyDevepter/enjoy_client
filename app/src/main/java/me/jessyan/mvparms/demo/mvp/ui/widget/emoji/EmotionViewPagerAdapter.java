package me.jessyan.mvparms.demo.mvp.ui.widget.emoji;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

import static me.jessyan.mvparms.demo.mvp.ui.widget.emoji.EmotionLayout.EMOJI_PER_PAGE;

/**
 * CSDN_LQR
 * 表情控件的ViewPager适配器(emoji + 贴图)
 */

public class EmotionViewPagerAdapter extends PagerAdapter {

    int mPageCount = 0;
    int mTabPosi = 0;
    EditText mMessageEditText;
    private int mEmotionLayoutWidth;
    private int mEmotionLayoutHeight;
    private IEmotionSelectedListener listener;
    public AdapterView.OnItemClickListener emojiListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            int index = position + (Integer) parent.getTag() * EMOJI_PER_PAGE;
            int count = EmojiManager.getAll().size();
            if (position == EMOJI_PER_PAGE || index >= count) {
                if (listener != null) {
                    listener.onEmojiSelected("/DEL");
                }
                onEmojiSelected("/DEL");
            } else {
                String text = ((Emoji) EmojiManager.getAll().toArray()[index]).getUnicode();
                if (!TextUtils.isEmpty(text)) {
                    if (listener != null) {
                        listener.onEmojiSelected(text);
                    }
                    onEmojiSelected(text);
                }
            }
        }
    };

    public EmotionViewPagerAdapter(int emotionLayoutWidth, int emotionLayoutHeight, int tabPosi, IEmotionSelectedListener listener) {
        mEmotionLayoutWidth = emotionLayoutWidth;
        mEmotionLayoutHeight = emotionLayoutHeight;
        mTabPosi = tabPosi;

        mPageCount = 4;
        this.listener = listener;
    }

    public void attachEditText(EditText messageEditText) {
        mMessageEditText = messageEditText;
    }

    @Override
    public int getCount() {
        return mPageCount == 0 ? 1 : mPageCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        Context context = container.getContext();
        RelativeLayout rl = new RelativeLayout(context);
        rl.setGravity(Gravity.CENTER);

        GridView gridView = new GridView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        gridView.setLayoutParams(params);
        gridView.setGravity(Gravity.CENTER);

        gridView.setTag(position);//标记自己是第几页
        if (mTabPosi == 0) {
            gridView.setOnItemClickListener(emojiListener);
            gridView.setAdapter(new EmojiAdapter(context, mEmotionLayoutWidth, mEmotionLayoutHeight, position * EMOJI_PER_PAGE));
            gridView.setNumColumns(EmotionLayout.EMOJI_COLUMNS);
        }
        rl.addView(gridView);
        container.addView(rl);
        return rl;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private void onEmojiSelected(String key) {
        if (mMessageEditText == null)
            return;
        Editable editable = mMessageEditText.getText();
        if (key.equals("/DEL")) {
            mMessageEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        } else {
            int start = mMessageEditText.getSelectionStart();
            int end = mMessageEditText.getSelectionEnd();
            start = (start < 0 ? 0 : start);
            end = (start < 0 ? 0 : end);
            editable.replace(start, end, key);

            int editEnd = mMessageEditText.getSelectionEnd();
//            MoonUtils.replaceEmoticons(LQREmotionKit.getContext(), editable, 0, editable.toString().length());
            mMessageEditText.setSelection(editEnd);
        }
    }
}
