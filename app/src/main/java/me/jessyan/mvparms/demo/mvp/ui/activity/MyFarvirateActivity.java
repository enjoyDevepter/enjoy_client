package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
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
import me.jessyan.mvparms.demo.di.component.DaggerMyFarvirateComponent;
import me.jessyan.mvparms.demo.di.module.MyFarvirateModule;
import me.jessyan.mvparms.demo.mvp.contract.MyFarvirateContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.MealGoods;
import me.jessyan.mvparms.demo.mvp.presenter.MyFarviratePresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.FarvirateTaoCanListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyFarvirateGoodsListAdapter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MyFarvirateActivity extends BaseActivity<MyFarviratePresenter> implements MyFarvirateContract.View, View.OnClickListener, TabLayout.OnTabSelectedListener, SwipeRefreshLayout.OnRefreshListener, DefaultAdapter.OnRecyclerViewItemClickListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_date)
    View noDataV;
    @BindView(R.id.meals)
    RecyclerView mRecyclerView;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    MyFarvirateGoodsListAdapter mAdapter;
    @Inject
    FarvirateTaoCanListAdapter tMAdapter;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMyFarvirateComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .myFarvirateModule(new MyFarvirateModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_my_farvirate; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("我的收藏");
        backV.setOnClickListener(this);
        backV.setOnClickListener(this);
        provideCache().put("status", "3");
        swipeRefreshLayout.setOnRefreshListener(this);
        tabLayout.addTab(tabLayout.newTab().setText("医美"));
        tabLayout.addTab(tabLayout.newTab().setText("美肤"));
        tabLayout.addTab(tabLayout.newTab().setText("商城"));
        tabLayout.addTab(tabLayout.newTab().setText("套餐"));
        tabLayout.addOnTabSelectedListener(this);
        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(getActivity(),
                R.drawable.tablayout_divider_vertical));
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        tMAdapter.setOnItemClickListener(this);
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

    @Override
    public void showConent(boolean hasData) {
        swipeRefreshLayout.setVisibility(hasData ? View.VISIBLE : View.INVISIBLE);
        noDataV.setVisibility(hasData ? View.INVISIBLE : View.VISIBLE);
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
                    mPresenter.getMyFarvirate(false);
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
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        String type = "3";
        switch (tab.getPosition()) {
            case 0:
                mRecyclerView.setAdapter(mAdapter);
                type = "3";
                break;
            case 1:
                mRecyclerView.setAdapter(mAdapter);
                type = "2";
                break;
            case 2:
                mRecyclerView.setAdapter(mAdapter);
                type = "1";
                break;
            case 3:
                mRecyclerView.setAdapter(tMAdapter);
                type = "4";
                break;
        }
        initPaginate();
        provideCache().put("status", type);
        mPresenter.getMyFarvirate(true);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onRefresh() {
        mPresenter.getMyFarvirate(true);
    }

    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {
        if ("1".equals(provideCache().get("status"))) {
            Goods goods = mAdapter.getInfos().get(position);
            Intent intent = new Intent(getActivity().getApplication(), GoodsDetailsActivity.class);
            intent.putExtra("type", "1");
            intent.putExtra("goodsId", goods.getGoodsId());
            intent.putExtra("merchId", goods.getMerchId());
            ArmsUtils.startActivity(intent);
        } else if ("2".equals(provideCache().get("status"))) {

        } else if ("3".equals(provideCache().get("status"))) {
            Goods goods = mAdapter.getInfos().get(position);
            Intent hGoodsintent = new Intent(getActivity().getApplication(), HGoodsDetailsActivity.class);
            hGoodsintent.putExtra("type", "3");
            hGoodsintent.putExtra("goodsId", goods.getGoodsId());
            hGoodsintent.putExtra("merchId", goods.getMerchId());
            hGoodsintent.putExtra("advanceDepositId", goods.getAdvanceDepositId());
            ArmsUtils.startActivity(hGoodsintent);
        } else if ("4".equals(provideCache().get("status"))) {
            MealGoods mealGoods = tMAdapter.getInfos().get(position);
            Intent intent = new Intent(getActivity(), TaoCanDetailsActivity.class);
            intent.putExtra("setMealId", mealGoods.getSetMealId());
            ArmsUtils.startActivity(intent);
        }
    }
}
