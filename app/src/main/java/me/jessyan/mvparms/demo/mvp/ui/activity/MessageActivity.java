package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerMessageComponent;
import me.jessyan.mvparms.demo.di.module.MessageModule;
import me.jessyan.mvparms.demo.mvp.contract.MessageContract;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.CommentMessage;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.DynamicMessage;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.NoticeMessage;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.PraiseMessage;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.PrivateMessage;
import me.jessyan.mvparms.demo.mvp.presenter.MessagePresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.CommentMessageAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DynamicMessageAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.NoticeMessageAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.PraiseMessageAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.PrivateMessageAdapter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MessageActivity extends BaseActivity<MessagePresenter> implements MessageContract.View, View.OnClickListener, TabLayout.OnTabSelectedListener, SwipeRefreshLayout.OnRefreshListener, DefaultAdapter.OnRecyclerViewItemClickListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.type)
    TabLayout tabLayout;
    @BindView(R.id.noticeInfo)
    WebView noticeWV;
    @BindView(R.id.privateInfo)
    View privateV;
    @BindView(R.id.headImage)
    ImageView headIV;
    @BindView(R.id.name)
    TextView nameTV;
    @BindView(R.id.sendTime)
    TextView sendTimeTV;
    @BindView(R.id.content)
    TextView contentTV;
    @BindView(R.id.reply)
    View replyV;
    @BindView(R.id.reply_info)
    TextView replyInfoTV;
    @BindView(R.id.reply_time)
    TextView replyTimeTV;
    @BindView(R.id.reply_content)
    EditText replyContentET;
    @BindView(R.id.replyBtn)
    View replyBtn;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.message)
    RecyclerView messageRV;
    @BindView(R.id.no_data)
    View noData;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    PrivateMessageAdapter pMAdapter;
    @Inject
    NoticeMessageAdapter nAdapter;
    @Inject
    DynamicMessageAdapter dAdapter;
    @Inject
    CommentMessageAdapter cAdapter;
    @Inject
    PraiseMessageAdapter pAdapter;
    @Inject
    ImageLoader mImageLoader;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMessageComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .messageModule(new MessageModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_message; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        replyBtn.setOnClickListener(this);
        titleTV.setText("私信");
        provideCache().put("type", 0);
        tabLayout.addTab(tabLayout.newTab().setText("私信"));
        tabLayout.addTab(tabLayout.newTab().setText("通知"));
        tabLayout.addTab(tabLayout.newTab().setText("动态"));
//        tabLayout.addTab(tabLayout.newTab().setText("回复"));
        tabLayout.addTab(tabLayout.newTab().setText("评论"));
        tabLayout.addTab(tabLayout.newTab().setText("点赞"));
        tabLayout.addOnTabSelectedListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        ArmsUtils.configRecyclerView(messageRV, mLayoutManager);
        messageRV.setAdapter(pMAdapter);
        pMAdapter.setOnItemClickListener(this);
        nAdapter.setOnItemClickListener(this);
        dAdapter.setOnItemClickListener(this);
        cAdapter.setOnItemClickListener(this);
        pAdapter.setOnItemClickListener(this);
        initPaginate();
    }


    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Cache getCache() {
        return provideCache();
    }

    @Override
    public void showConent(boolean hasData) {
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        messageRV.setVisibility(hasData ? View.VISIBLE : View.GONE);
        noData.setVisibility(hasData ? View.GONE : View.VISIBLE);
        noticeWV.setVisibility(View.GONE);
        privateV.setVisibility(View.GONE);
    }

    /**
     * 开始加载更多
     */
    @Override
    public void startLoadMore() {
        isLoadingMore = true;
    }

    /**
     * 结束加载更多
     */
    @Override
    public void endLoadMore() {
        isLoadingMore = false;
    }

    @Override
    public void setLoadedAllItems(boolean has) {
        this.hasLoadedAllItems = has;
    }

    @Override
    public void showPrivateDetail(PrivateMessage privateMessage) {
        if (ArmsUtils.isEmpty(privateMessage.getReplyContent())) {
            replyV.setVisibility(View.INVISIBLE);
            replyContentET.setVisibility(View.VISIBLE);
            replyBtn.setVisibility(View.VISIBLE);
        } else {
            replyV.setVisibility(View.VISIBLE);
            replyContentET.setVisibility(View.GONE);
            replyBtn.setVisibility(View.GONE);
            replyInfoTV.setText(privateMessage.getReplyContent());
            replyTimeTV.setText(privateMessage.getReplyTime());
        }
        privateV.setVisibility(View.VISIBLE);
        noticeWV.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.GONE);

        //itemView 的 Context 就是 Activity, Glide 会自动处理并和该 Activity 的生命周期绑定
        mImageLoader.loadImage(headIV.getContext(),
                ImageConfigImpl
                        .builder()
                        .placeholder(R.drawable.place_holder_img)
                        .url(privateMessage.getHeadImage())
                        .isCenterCrop(true)
                        .imageView(headIV)
                        .build());
        nameTV.setText(privateMessage.getTitle());
        contentTV.setText(privateMessage.getContent());
        sendTimeTV.setText(privateMessage.getSendTime());

    }

    @Override
    public void showNotice(NoticeMessage noticeMessage) {
        noticeWV.loadUrl(noticeMessage.getUrl());
        noticeWV.setVisibility(View.VISIBLE);
        privateV.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.GONE);
    }

    @Override
    public void showDynamic(DynamicMessage dynamicMessage) {
        noticeWV.loadUrl(dynamicMessage.getUrl());
        noticeWV.setVisibility(View.VISIBLE);
        privateV.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.GONE);
    }

    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.getMessage(false);
                }

                @Override
                public boolean isLoading() {
                    return isLoadingMore;
                }

                @Override
                public boolean hasLoadedAllItems() {
                    return hasLoadedAllItems;
                }
            };

            mPaginate = Paginate.with(messageRV, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
            mPaginate.setHasMoreDataToLoad(false);
        }
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.replyBtn:
                if (ArmsUtils.isEmpty(replyContentET.getText().toString())) {
                    showMessage("请输入回复内容!");
                    return;
                }
                getCache().put("reply", replyContentET.getText().toString());
                mPresenter.reply();
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        provideCache().put("type", tab.getPosition());
        switch (tab.getPosition()) {
            case 0:
                messageRV.setAdapter(pMAdapter);
                break;
            case 1:
                messageRV.setAdapter(nAdapter);
                break;
            case 2:
                messageRV.setAdapter(dAdapter);
                break;
            case 3:
                messageRV.setAdapter(cAdapter);
                break;
            case 4:
                messageRV.setAdapter(pAdapter);
                break;

        }
        initPaginate();
        mPresenter.getMessage(true);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(messageRV);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
        this.mPaginate = null;

    }

    @Override
    public void onRefresh() {
        mPresenter.getMessage(true);
    }

    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {
        switch (viewType) {
            case R.layout.private_letter_item:
                if (data instanceof PrivateMessage) {
                    getCache().put("privateMessageId", ((PrivateMessage) data).getPrivateMessageId());
                    mPresenter.getNoticeDetail();
                } else if (data instanceof CommentMessage) {
                    // 跳日志详情
                    Intent diaryIntent = new Intent(getActivity().getApplication(), DiaryDetailsActivity.class);
                    diaryIntent.putExtra("diaryId", ((CommentMessage) data).getDiaryId());
                    ArmsUtils.startActivity(diaryIntent);
                } else if (data instanceof PraiseMessage) {
                    Intent diaryIntent = new Intent(getActivity().getApplication(), DiaryDetailsActivity.class);
                    diaryIntent.putExtra("diaryId", ((PraiseMessage) data).getDiaryId());
                    ArmsUtils.startActivity(diaryIntent);
                }
                break;
            case R.layout.notify_item:
                if (data instanceof NoticeMessage) {
                    getCache().put("noticeId", ((NoticeMessage) data).getNoticeId());
                } else if (data instanceof DynamicMessage) {
                    getCache().put("dynamicId", ((DynamicMessage) data).getDynamicId());
                }
                mPresenter.getNoticeDetail();
                break;
        }
    }
}
