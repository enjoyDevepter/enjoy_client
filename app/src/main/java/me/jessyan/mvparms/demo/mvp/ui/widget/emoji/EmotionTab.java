package me.jessyan.mvparms.demo.mvp.ui.widget.emoji;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import me.jessyan.mvparms.demo.R;

/**
 * CSDN_LQR
 * 表情底部tab
 */
public class EmotionTab extends RelativeLayout {

    private ImageView mIvIcon;

    public EmotionTab(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.emotion_tab, this);

        mIvIcon = (ImageView) findViewById(R.id.ivIcon);
    }

}
