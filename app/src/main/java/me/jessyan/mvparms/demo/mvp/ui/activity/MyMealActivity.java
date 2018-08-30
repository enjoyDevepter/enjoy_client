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

import org.simple.eventbus.Subscriber;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerMyMealComponent;
import me.jessyan.mvparms.demo.di.module.MyMealModule;
import me.jessyan.mvparms.demo.mvp.contract.MyMealContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Coupon;
import me.jessyan.mvparms.demo.mvp.model.entity.order.Order;
import me.jessyan.mvparms.demo.mvp.presenter.MyMealPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyMealListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MyMealActivity extends BaseActivity<MyMealPresenter> implements MyMealContract.View, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, MyMealListAdapter.OnChildItemClickLinstener, TabLayout.OnTabSelectedListener {

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
    MyMealListAdapter mAdapter;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMyMealComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .myMealModule(new MyMealModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_my_meal; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("我的套餐");
        backV.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        tabLayout.addTab(tabLayout.newTab().setTag("status").setText("全部"));
        tabLayout.addTab(tabLayout.newTab().setTag("status").setText("待付款"));
        tabLayout.addTab(tabLayout.newTab().setTag("status").setText("待预约"));
        tabLayout.addTab(tabLayout.newTab().setTag("status").setText("已完成"));
        tabLayout.addOnTabSelectedListener(this);
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0, ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.address_list_item_space)));
        mAdapter.setOnChildItemClickLinstener(this);
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

    @Override
    public void showConent(boolean hasData) {
        swipeRefreshLayout.setVisibility(hasData ? View.VISIBLE : View.GONE);
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
                    mPresenter.getMyMealAppointment(false);
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


    @Subscriber(tag = EventBusTags.MEAL_PAY_SUCCESS)
    private void updateCoupon(Coupon coupon) {
        mPresenter.getMyMealAppointment(true);
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
    public void onRefresh() {
        mPresenter.getMyMealAppointment(true);
    }

    @Override
    public void onChildItemClick(View v, MyMealListAdapter.ViewName viewname, int position) {
        Order appointment = mAdapter.getInfos().get(position);
        switch (viewname) {
            case LEFT:
                if ("1".equals(appointment.getOrderStatus())) {
                    // 取消订单
                }
                break;
            case RIGHT:
                if ("1".equals(appointment.getOrderStatus())) {
                    // 支付
                    Intent intent = new Intent(getActivity(), MealOrderConfirmActivity.class);
                    intent.putExtra("setMealId", appointment.getSetMealGoodsList().get(0).getSetMealId());
                    intent.putExtra("nums", appointment.getSetMealGoodsList().get(0).getNums());
                    intent.putExtra("totalPrice", appointment.getSetMealGoodsList().get(0).getTotalPrice());
                    intent.putExtra("salePrice", appointment.getSetMealGoodsList().get(0).getSalePrice());
                    ArmsUtils.startActivity(intent);
                } else if ("31".equals(appointment.getOrderStatus())) {
                    // 预约
                    Intent makeIntent = new Intent(this, MyMealDetailsActivity.class);
                    makeIntent.putExtra("orderId", appointment.getOrderId());
                    makeIntent.putExtra("mealName", appointment.getSetMealGoodsList().get(0).getName());
                    ArmsUtils.startActivity(makeIntent);
                } else if ("5".equals(appointment.getOrderStatus())) {
                    // 写日记
                    Intent intent = new Intent(getActivity(), ReleaseDiaryActivity.class);
                    intent.putExtra("orderId", appointment.getOrderId());
                    ArmsUtils.startActivity(intent);
                }
                break;
            case ITEM:
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        provideCache().put("type", tab.getPosition());
        mPresenter.getMyMealAppointment(true);
    }


    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(mRecyclerView);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
    }
}
