package me.jessyan.mvparms.demo.mvp.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.ActivityInfo;

/**
 * Created by guomin on 2018/10/13.
 */

public class CarouselView extends ViewSwitcher {
    public OnClickItemListener onClickItemListener;
    private int mCutItem;
    private int loopTime;//循环时间
    private MyHandler myHandler;
    private ArrayList<ActivityInfo> listString;

    public CarouselView(Context context) {
        this(context, null);
    }

    public CarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
        initAnimation();
    }

    /**
     * 初始化一些变量
     */
    private void initData() {
        listString = new ArrayList<>();
        myHandler = new MyHandler(this);
    }

    /**
     * 给viewSwitch添加显示的view，可以自由设置，外部调用
     *
     * @param layoutId 自定义view的布局id
     */
    public void addView(final int layoutId) {
        setFactory(new ViewFactory() {
            @Override
            public View makeView() {
                return LayoutInflater.from(getContext()).inflate(layoutId, null);
            }
        });
    }

    /**
     * 初始化进入和出去动画
     */
    private void initAnimation() {
        setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.translate_in));
        setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.translate_out));
    }

    /**
     * 设置数据源并展示view，外部调用
     *
     * @param mList
     * @param time
     */
    public void upDataListAndView(List<ActivityInfo> mList, int time) {
        mCutItem = 0;
        loopTime = time;
        if (null == mList) {
            return;
        }
        listString.clear();
        listString.addAll(mList);
        updataView(mList.get(0), getCurrentView(), mCutItem);
    }

    /**
     * 展示下一条广告
     */
    public void showNextView() {
        if (null == listString || listString.size() < 2) {
            return;
        }
        mCutItem = mCutItem == listString.size() - 1 ? 0 : mCutItem + 1;
        updataView(listString.get(mCutItem), getNextView(), mCutItem);
        showNext();
    }

    /**
     * 启动轮播
     */
    public void startLooping() {
        if (null == listString || listString.size() < 2) {
            return;
        }
        myHandler.removeMessages(0);
        myHandler.sendEmptyMessageDelayed(0, loopTime);
    }

    /**
     * 停止轮播
     */
    public void stopLooping() {
        myHandler.removeMessages(0);
    }

    /**
     * 在当前view上设置数据
     *
     * @param activityInfo
     * @param view
     */
    private void updataView(ActivityInfo activityInfo, View view, final int mCutItem) {
        TextView textView = view.findViewById(R.id.tv_carouse_text);
        textView.setText(activityInfo.getTitle());
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClickItemListener) {
                    onClickItemListener.onClick(mCutItem);
                }
                //Toast.makeText(getContext(), "你点击了第" + position + "条广告", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setOnClickListener(OnClickItemListener onClickListener) {
        this.onClickItemListener = onClickListener;
    }

    /**
     * 定义一个接口回调，响应广告点击
     */
    public interface OnClickItemListener {
        void onClick(int position);
    }

    /**
     * @description 主线程Handler
     * @note 因为存在定时任务，并且TextSwitcherView持有Activity的引用
     * 所以这里采用弱引用，主要针对内存回收的时候Activity泄露
     **/
    private static class MyHandler extends Handler {

        private WeakReference<CarouselView> mRef;

        public MyHandler(CarouselView view) {
            mRef = new WeakReference<CarouselView>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CarouselView mView = this.mRef.get();
            mView.showNextView();//展示下一条广告，会调用shownext方法展示下一条广告
            mView.startLooping();//启动轮播，间隔后展示下一条
        }
    }
}
