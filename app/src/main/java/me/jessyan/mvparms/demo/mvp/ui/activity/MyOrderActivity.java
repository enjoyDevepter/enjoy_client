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
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerMyOrderComponent;
import me.jessyan.mvparms.demo.di.module.MyOrderModule;
import me.jessyan.mvparms.demo.mvp.contract.MyOrderContract;
import me.jessyan.mvparms.demo.mvp.model.entity.order.Order;
import me.jessyan.mvparms.demo.mvp.presenter.MyOrderPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyOrderAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MyOrderActivity extends BaseActivity<MyOrderPresenter> implements MyOrderContract.View, View.OnClickListener, TabLayout.OnTabSelectedListener, MyOrderAdapter.OnChildItemClickLinstener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.type)
    TabLayout typeTabLayout;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_data)
    View noData;
    @BindView(R.id.status)
    TabLayout statusTabLayout;
    @BindView(R.id.orders)
    RecyclerView ordersRV;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    MyOrderAdapter mAdapter;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMyOrderComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .myOrderModule(new MyOrderModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_my_order; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        titleTV.setText("我的订单");
        ArmsUtils.configRecyclerView(ordersRV, mLayoutManager);
        ordersRV.setAdapter(mAdapter);
        ordersRV.addItemDecoration(new SpacesItemDecoration(0, ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.address_list_item_space)));
        mAdapter.setOnChildItemClickLinstener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        TabLayout.Tab tab1 = typeTabLayout.newTab().setText("商美");
        TabLayout.Tab tab2 = typeTabLayout.newTab().setText("生美/科美");
        TabLayout.Tab tab3 = typeTabLayout.newTab().setText("医美");
        typeTabLayout.addTab(tab1);
        typeTabLayout.addTab(tab2);
        typeTabLayout.addTab(tab3);
        typeTabLayout.addOnTabSelectedListener(this);
        int type = getIntent().getIntExtra("type", 0);
        provideCache().put("type", type);
        switch (type) {
            case 0:
                tab1.select();
                break;
            case 1:
                tab2.select();
                break;
            case 2:
                tab3.select();
                break;
        }
        statusTabLayout.addTab(statusTabLayout.newTab().setTag("status").setText("全部"));
        statusTabLayout.addTab(statusTabLayout.newTab().setTag("status").setText("待付款"));
        statusTabLayout.addTab(statusTabLayout.newTab().setTag("status").setText("待发货"));
        statusTabLayout.addTab(statusTabLayout.newTab().setTag("status").setText("待收货"));
        statusTabLayout.addTab(statusTabLayout.newTab().setTag("status").setText("已完成"));
        statusTabLayout.addOnTabSelectedListener(this);
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
        if (!"status".equals(tab.getTag())) {
            provideCache().put("type", tab.getPosition());
        } else {
            provideCache().put("status", tab.getPosition());
        }
        mPresenter.getOrder(true);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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
        swipeRefreshLayout.setVisibility(hasData ? View.VISIBLE : View.GONE);
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


    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.getOrder(false);
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

            mPaginate = Paginate.with(ordersRV, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
            mPaginate.setHasMoreDataToLoad(false);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mPaginate = null;
        DefaultAdapter.releaseAllHolder(ordersRV);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用

    }

    @Override
    public void onChildItemClick(View v, MyOrderAdapter.ViewName viewname, int position) {
        Order order = mAdapter.getInfos().get(position);
        switch (viewname) {
            case PAY:
                Intent payIntent = new Intent(this, PayActivity.class);
                payIntent.putExtra("orderId", order.getOrderId());
                payIntent.putExtra("payMoney", order.getPayMoney());
                payIntent.putExtra("orderTime", order.getOrderTime());
                ArmsUtils.startActivity(payIntent);
                break;
            case ITEM:
                Intent detailIntent = new Intent(this, OrderDeatilsActivity.class);
                detailIntent.putExtra("orderId", order.getOrderId());
                detailIntent.putExtra("type", (int) provideCache().get("type"));
                detailIntent.putExtra("isMeal", "6".equals(order.getOrderType()) ? true : false);
                ArmsUtils.startActivity(detailIntent);
                break;
            case LOGISTICS:
                break;
            case TAKE_OVER:
                break;
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.getOrder(true);
    }
}
