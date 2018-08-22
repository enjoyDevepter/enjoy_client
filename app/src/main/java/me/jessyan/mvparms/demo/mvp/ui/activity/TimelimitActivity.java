package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerTimelimitComponent;
import me.jessyan.mvparms.demo.di.module.TimelimitModule;
import me.jessyan.mvparms.demo.mvp.contract.TimelimitContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.presenter.TimelimitPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsLimitListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyMealListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class TimelimitActivity extends BaseActivity<TimelimitPresenter> implements TimelimitContract.View, MyMealListAdapter.OnChildItemClickLinstener, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.content)
    RecyclerView mRecyclerView;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    GoodsLimitListAdapter mAdapter;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerTimelimitComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .timelimitModule(new TimelimitModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_timelimit; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        titleTV.setText("限时秒杀");
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0, ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.address_list_item_space)));
        mAdapter.setOnItemClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
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
    public Activity getActivity() {
        return this;
    }

    @Override
    public Cache getCache() {
        return provideCache();
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
                    mPresenter.getTimeLimitGoodsList(false);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
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
    public void onChildItemClick(View v, MyMealListAdapter.ViewName viewname, int position) {

    }

    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {

        Goods goods = mAdapter.getInfos().get(position);
        if ("1".equals(goods.getType())) {
            Intent intent = new Intent(getActivity().getApplication(), GoodsDetailsActivity.class);
            intent.putExtra("where", "timelimitdetail");
            intent.putExtra("goodsId", goods.getGoodsId());
            intent.putExtra("merchId", goods.getMerchId());
            intent.putExtra("promotionId", goods.getPromotionId());
            ArmsUtils.startActivity(intent);

        } else if ("2".equals(goods.getType())) {

        } else if ("3".equals(goods.getType())) {
            Intent intent = new Intent(getActivity().getApplication(), HGoodsDetailsActivity.class);
            intent.putExtra("where", "timelimitdetail");
            intent.putExtra("goodsId", goods.getGoodsId());
            intent.putExtra("merchId", goods.getMerchId());
            intent.putExtra("promotionId", goods.getPromotionId());
            ArmsUtils.startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.getTimeLimitGoodsList(true);
    }
}
