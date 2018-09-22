package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerGrowthComponent;
import me.jessyan.mvparms.demo.di.module.GrowthModule;
import me.jessyan.mvparms.demo.mvp.contract.GrowthContract;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.GrowthInfoResponse;
import me.jessyan.mvparms.demo.mvp.presenter.GrowthPresenter;
import me.jessyan.mvparms.demo.mvp.ui.widget.GrowthView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class GrowthActivity extends BaseActivity<GrowthPresenter> implements GrowthContract.View, View.OnClickListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.growth)
    TextView growthTV;
    @BindView(R.id.info)
    TextView infoTV;
    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.tabTwo)
    TabLayout tabTwoLayout;
    @BindView(R.id.how)
    View howV;
    @BindView(R.id.growth_level)
    GrowthView growthView;
    @BindView(R.id.contentList)
    RecyclerView mRecyclerView;
    @BindView(R.id.no_date)
    View noDataV;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.Adapter mAdapter;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;

    private GrowthInfoResponse response;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerGrowthComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .growthModule(new GrowthModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_growth; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        howV.setOnClickListener(this);
        tabLayout.addTab(tabLayout.newTab().setText("成长等级"));
        tabTwoLayout.addTab(tabTwoLayout.newTab().setText("成长记录"));
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        swipeRefreshLayout.setEnabled(false);
        initPaginate();
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
    public void showConent(boolean hasData) {
        mRecyclerView.setVisibility(hasData ? View.VISIBLE : View.GONE);
        noDataV.setVisibility(hasData ? View.GONE : View.VISIBLE);
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

    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.getGrowthList(false);
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
            mPaginate = Paginate.with(mRecyclerView, callbacks)
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.how:
                if (null != response) {
                    Intent articleIntent = new Intent(this, PlatformActivity.class);
                    articleIntent.putExtra("url", response.getUrl());
                    ArmsUtils.startActivity(articleIntent);
                }
                break;
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void updateUI(GrowthInfoResponse response) {
        this.response = response;
        growthTV.setText(String.valueOf(response.getGrowth()));
        infoTV.setText(response.getDesc());
        growthView.setGrowthInfoList(response.getGrowthInfoList());
    }
}
