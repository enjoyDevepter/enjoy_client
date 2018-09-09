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

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerChoiceStoreComponent;
import me.jessyan.mvparms.demo.di.module.ChoiceStoreModule;
import me.jessyan.mvparms.demo.mvp.contract.ChoiceStoreContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Store;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.HospitalBaseInfoBean;
import me.jessyan.mvparms.demo.mvp.presenter.ChoiceStorePresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.StoresListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static me.jessyan.mvparms.demo.mvp.ui.activity.SelfPickupAddrListActivity.KEY_FOR_ACTIVITY_LIST_TYPE;


public class ChoiceStoreActivity extends BaseActivity<ChoiceStorePresenter> implements ChoiceStoreContract.View, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleV;
    @BindView(R.id.confirm)
    View confirmV;
    @BindView(R.id.stores)
    RecyclerView storesRV;
    @BindView(R.id.info_text)
    TextView info_text;
    @BindView(R.id.no_date)
    View noDataV;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    StoresListAdapter mAdapter;

    private SelfPickupAddrListActivity.ListType listType;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerChoiceStoreComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .choiceStoreModule(new ChoiceStoreModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_choice_store; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        listType = (SelfPickupAddrListActivity.ListType) getIntent().getSerializableExtra(SelfPickupAddrListActivity.KEY_FOR_ACTIVITY_LIST_TYPE);
        if (listType == null) {
            throw new RuntimeException("listType is not null.you need send listType use key  \"KEY_FOR_ACTIVITY_LIST_TYPE\"");
        }
        backV.setOnClickListener(this);
        titleV.setText(listType.getSecendListTitle());
        info_text.setText(listType.getInfoText());
        confirmV.setOnClickListener(this);
        ArmsUtils.configRecyclerView(storesRV, mLayoutManager);
        storesRV.setAdapter(mAdapter);
        storesRV.addItemDecoration(new SpacesItemDecoration(0, ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.address_list_item_space)));
        mAdapter.setOnItemClickListener(this);
        initPaginate();
        swipeRefreshLayout.setOnRefreshListener(this);
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
            case R.id.confirm:
                SelfPickupAddrListActivity.ListType listType = (SelfPickupAddrListActivity.ListType) this.getIntent().getSerializableExtra(KEY_FOR_ACTIVITY_LIST_TYPE);
                if (null != provideCache().get("choiceItem")) {
                    int index = (int) provideCache().get("choiceItem");
                    switch (listType) {
                        case HOP:
                            EventBus.getDefault().post((HospitalBaseInfoBean) mAdapter.getInfos().get(index), EventBusTags.HOSPITAL_CHANGE_EVENT);
                            break;
                        case STORE:
                            EventBus.getDefault().post((Store) mAdapter.getInfos().get(index), EventBusTags.STORE_CHANGE_EVENT);
                            break;
                        case ADDR:
                            break;
                    }
                } else {
                    switch (listType) {
                        case HOP:
                            showMessage("请选择医院！");
                            return;
                        case STORE:
                            showMessage("请选择店铺信息！");
                            return;
                        case ADDR:
                            return;
                    }
                }
                killMyself();
                break;
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
    public void showConent(boolean hasData) {
        storesRV.setVisibility(hasData ? View.VISIBLE : View.GONE);
        noDataV.setVisibility(hasData ? View.GONE : View.VISIBLE);
    }

    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.getData(false);
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

            mPaginate = Paginate.with(storesRV, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
            mPaginate.setHasMoreDataToLoad(false);
        }
    }


    @Override
    public void onRefresh() {
        mPresenter.getData(true);
    }


    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {
        for (int i = 0; i < mAdapter.getInfos().size(); i++) {
            mAdapter.getInfos().get(i).setCheck(i == position ? true : false);
        }
        mAdapter.notifyDataSetChanged();
        provideCache().put("choiceItem", position);
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
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(storesRV);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
    }
}
