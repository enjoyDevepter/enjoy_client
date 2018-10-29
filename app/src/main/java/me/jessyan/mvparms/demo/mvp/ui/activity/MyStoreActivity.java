package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
import me.jessyan.mvparms.demo.di.component.DaggerMyStoreComponent;
import me.jessyan.mvparms.demo.di.module.MyStoreModule;
import me.jessyan.mvparms.demo.mvp.contract.MyStoreContract;
import me.jessyan.mvparms.demo.mvp.presenter.MyStorePresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyStoresListAdapter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MyStoreActivity extends BaseActivity<MyStorePresenter> implements MyStoreContract.View, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.edit)
    View editV;
    @BindView(R.id.type)
    TabLayout tabLayout;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.store)
    RecyclerView storeRV;
    @BindView(R.id.no_data)
    View noData;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    MyStoresListAdapter mAdapter;

    private SelfPickupAddrListActivity.ListType listType = SelfPickupAddrListActivity.ListType.RELATEDSTORE;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems = true;


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMyStoreComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .myStoreModule(new MyStoreModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_my_store; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        editV.setOnClickListener(this);
        tabLayout.addTab(tabLayout.newTab().setText("已关联店铺"));
        swipeRefreshLayout.setOnRefreshListener(this);
        storeRV.setAdapter(mAdapter);
        ArmsUtils.configRecyclerView(storeRV, mLayoutManager);
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
        storeRV.setVisibility(hasData ? View.VISIBLE : View.GONE);
        noData.setVisibility(hasData ? View.GONE : View.VISIBLE);
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

    @Subscriber(tag = EventBusTags.STORE_CHANGE_EVENT)
    private void updateStore(int index) {
        mPresenter.getRelateStore(true);
    }

    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.getRelateStore(false);
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

            mPaginate = Paginate.with(storeRV, callbacks)
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
    public void killMyself() {
        finish();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.edit:
                Intent intent2 = new Intent(this, SelfPickupAddrListActivity.class);
                intent2.putExtra(SelfPickupAddrListActivity.KEY_FOR_ACTIVITY_LIST_TYPE, listType);
                ArmsUtils.startActivity(intent2);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(storeRV);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
        this.mPaginate = null;

    }

    @Override
    public void onRefresh() {
        mPresenter.getRelateStore(true);
    }
}
