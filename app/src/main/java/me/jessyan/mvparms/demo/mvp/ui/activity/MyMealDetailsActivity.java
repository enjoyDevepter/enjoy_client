package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerMyMealDetailsComponent;
import me.jessyan.mvparms.demo.di.module.MyMealDetailsModule;
import me.jessyan.mvparms.demo.mvp.contract.MyMealDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.appointment.Appointment;
import me.jessyan.mvparms.demo.mvp.presenter.MyMealDetailsPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyMealDetailListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MyMealDetailsActivity extends BaseActivity<MyMealDetailsPresenter> implements MyMealDetailsContract.View, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, MyMealDetailListAdapter.OnChildItemClickLinstener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.content)
    RecyclerView mRecyclerView;
    @BindView(R.id.no_date)
    View onDate;
    @BindView(R.id.name)
    TextView nameTV;
    @BindView(R.id.orderId)
    TextView orderIdTV;
    @BindView(R.id.desc)
    TextView descTV;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    MyMealDetailListAdapter mAdapter;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMyMealDetailsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .myMealDetailsModule(new MyMealDetailsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_my_meal_details; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        titleTV.setText("我的套餐");
        swipeRefreshLayout.setOnRefreshListener(this);
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnChildItemClickLinstener(this);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0, ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.address_list_item_space)));
        initPaginate();

        nameTV.setText(getIntent().getStringExtra("mealName"));
        orderIdTV.setText(getIntent().getStringExtra("orderId"));
        String desc = getIntent().getStringExtra("desc");
        if (!ArmsUtils.isEmpty(desc)) {
            descTV.setText(desc);
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
    public Activity getActivity() {
        return this;
    }

    @Override
    public Cache getCache() {
        return provideCache();
    }

    @Override
    public void showError(boolean hasDate) {
        onDate.setVisibility(hasDate ? INVISIBLE : VISIBLE);
        swipeRefreshLayout.setVisibility(hasDate ? VISIBLE : INVISIBLE);
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
                    mPresenter.getAppointment(false);
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
    public void onRefresh() {
        mPresenter.getAppointment(true);
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
    public void onChildItemClick(View v, MyMealDetailListAdapter.ViewName viewname, int position) {
        Appointment appointment = mAdapter.getInfos().get(position);
        switch (viewname) {
            case LEFT:
                if (appointment.getStatus().equals("1")) {
                    // 复制
                    //获取剪贴板管理器：
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", appointment.getProjectId());
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    showMessage("已复制到剪贴板");
                } else if (appointment.getStatus().equals("2")) {
                    // 取消预约
                    provideCache().put("reservationId", appointment.getReservationId());
                    mPresenter.cancelAppointment();
                } else if (appointment.getStatus().equals("4")) {
                    // 申请奖励
                    provideCache().put("merchId", appointment.getGoods().getMerchId());
                    provideCache().put("goodsId", appointment.getGoods().getGoodsId());
                    mPresenter.apply();
                }
                break;
            case RIGHT:
                if (appointment.getStatus().equals("1")) {
                    // 预约
                    Intent addappointmentsIntent = new Intent(this, ChoiceTimeActivity.class);
                    addappointmentsIntent.putExtra("isMeal", true);
                    addappointmentsIntent.putExtra("projectId", appointment.getProjectId());
                    addappointmentsIntent.putExtra("type", "add_appointment_time");
                    ArmsUtils.startActivity(addappointmentsIntent);
                } else if (appointment.getStatus().equals("2")) {
                    // 改约
                    Intent addappointmentsIntent = new Intent(this, ChoiceTimeActivity.class);
                    addappointmentsIntent.putExtra("isMeal", true);
                    addappointmentsIntent.putExtra("reservationId", appointment.getReservationId());
                    addappointmentsIntent.putExtra("type", "modify_appointment_time");
                    ArmsUtils.startActivity(addappointmentsIntent);
                } else if (appointment.getStatus().equals("3")) {
                    provideCache().put("reservationId", appointment.getReservationId());
                    // 取消预约
                    mPresenter.cancelAppointment();
                } else if (appointment.getStatus().equals("4")) {
                    // 写日记
                    Intent intent = new Intent(getActivity(), ReleaseDiaryActivity.class);
                    intent.putExtra("orderId", appointment.getProjectId());
                    ArmsUtils.startActivity(intent);
                    break;
                }
            case ITEM:
                break;
        }
    }
}
