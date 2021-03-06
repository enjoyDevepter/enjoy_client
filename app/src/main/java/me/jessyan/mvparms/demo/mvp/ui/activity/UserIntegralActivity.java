package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;

import org.simple.eventbus.Subscriber;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerUserIntegralComponent;
import me.jessyan.mvparms.demo.di.module.UserIntegralModule;
import me.jessyan.mvparms.demo.mvp.contract.UserIntegralContract;
import me.jessyan.mvparms.demo.mvp.model.MyModel;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.MemberAccount;
import me.jessyan.mvparms.demo.mvp.presenter.UserIntegralPresenter;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;
import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * 用户积分
 */
public class UserIntegralActivity extends BaseActivity<UserIntegralPresenter> implements UserIntegralContract.View {

    @BindView(R.id.score)
    TextView score;

    @BindView(R.id.back)
    View back;

    @BindView(R.id.no_date)
    View onDateV;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.Adapter mAdapter;

    @BindView(R.id.contentList)
    RecyclerView contentList;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.qianming)
    View qianming;
    @BindView(R.id.how_to_icon)
    View how_to_icon;
    @BindView(R.id.how_to_icon_title)
    TextView how_to_icon_title;
    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerUserIntegralComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .userIntegralModule(new UserIntegralModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_user_integral; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(ArmsUtils.getContext()).extras();
        updateUserAccount((MemberAccount) cache.get(KEY_KEEP + MyModel.KEY_FOR_USER_ACCOUNT));

        ArmsUtils.configRecyclerView(contentList, mLayoutManager);
        contentList.setAdapter(mAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.requestOrderList(true);
            }
        });
        initPaginate();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMyself();
            }
        });
        qianming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.qiandao();
            }
        });
    }

    @Override
    public void showConent(boolean hasData) {
        swipeRefreshLayout.setVisibility(hasData ? View.VISIBLE : View.GONE);
        onDateV.setVisibility(hasData ? View.GONE : View.VISIBLE);
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
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }


    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.requestOrderList(false);
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

            mPaginate = Paginate.with(contentList, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
            mPaginate.setHasMoreDataToLoad(false);
        }
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
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(contentList);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
        this.mPaginate = null;
    }


    public Activity getActivity() {
        return this;
    }

    @Subscriber(tag = EventBusTags.USER_ACCOUNT_CHANGE)
    public void updateUserAccount(MemberAccount account) {
        if (null != account) {
            score.setText(account.getPoint() + "");
        }
    }

    public void updateQiandaoInfo(boolean isSignin, long point, String url) {
        View.OnClickListener howGetIntegral = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserIntegralActivity.this, WebActivity.class);
                intent.putExtra(WebActivity.KEY_FOR_WEB_URL, url);
                intent.putExtra(WebActivity.KEY_FOR_WEB_TITLE, "如何获取积分");
                ArmsUtils.startActivity(intent);
            }
        };
        how_to_icon.setOnClickListener(howGetIntegral);
        how_to_icon_title.setOnClickListener(howGetIntegral);
        qianming.setEnabled(!isSignin);
        score.setText(point + "");
    }
}
