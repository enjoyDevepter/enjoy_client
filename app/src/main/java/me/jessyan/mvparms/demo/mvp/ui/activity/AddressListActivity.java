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
import org.simple.eventbus.Subscriber;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerAddressListComponent;
import me.jessyan.mvparms.demo.di.module.AddressListModule;
import me.jessyan.mvparms.demo.mvp.contract.AddressListContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Address;
import me.jessyan.mvparms.demo.mvp.presenter.AddressListPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.AddressEditListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.AddressListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class AddressListActivity extends BaseActivity<AddressListPresenter> implements AddressListContract.View, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener, AddressEditListAdapter.OnChildItemClickLinstener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.back)
    View backV;
    @BindView(R.id.edit)
    TextView editTV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.add_address)
    View addV;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_data)
    View noData;
    @BindView(R.id.content)
    RecyclerView contentRV;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    AddressListAdapter addressListAdapter;
    @Inject
    AddressEditListAdapter addressEditListAdapter;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerAddressListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .addressListModule(new AddressListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_address_list; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        editTV.setOnClickListener(this);
        addV.setOnClickListener(this);
        titleTV.setText("收货地址");
        ArmsUtils.configRecyclerView(contentRV, mLayoutManager);
        contentRV.setAdapter(addressListAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        addressEditListAdapter.setOnChildItemClickLinstener(this);
        addressListAdapter.setOnItemClickListener(this);
    }

    @Subscriber(tag = EventBusTags.ADD_ADDRESS_SUCCESS)
    public void updateAddressInfo(Address address) {
        mPresenter.getAddressList(true);
        swtichToEditMode(true);
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
        contentRV.setVisibility(hasData ? View.VISIBLE : View.GONE);
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
                    if (!editTV.isSelected()) {
                        mPresenter.getAddressList(false);
                    }
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
            mPaginate = Paginate.with(contentRV, callbacks)
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
            case R.id.edit:
                swtichToEditMode(!editTV.isSelected());
                break;
            case R.id.add_address:
                ArmsUtils.startActivity(AddAddressActivity.class);
                break;
        }
    }

    private void swtichToEditMode(boolean edit) {
        editTV.setText(edit ? "确定" : "编辑");
        editTV.setSelected(edit);
        addV.setVisibility(edit ? View.VISIBLE : View.GONE);
        if (edit) {
            contentRV.addItemDecoration(new SpacesItemDecoration(0, ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.address_list_item_space)));
            contentRV.setAdapter(addressEditListAdapter);
        } else {
            contentRV.setAdapter(addressListAdapter);
            initPaginate();
        }
    }

    @Override
    public void onChildItemClick(View v, AddressEditListAdapter.ViewName viewname, int position) {
        switch (viewname) {
            case EDIT:
                Intent intent = new Intent(this, AddAddressActivity.class);
                intent.putExtra("address", addressEditListAdapter.getInfos().get(position));
                ArmsUtils.startActivity(intent);
                break;
            case DELETE:
                mPresenter.delAddress(addressEditListAdapter.getInfos().get(position).getAddressId(), position);
                break;
            case CHECK:
                mPresenter.refreshList(position);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(contentRV);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();

    }

    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {
        switch (viewType) {
            case R.layout.addrsss_list_item:
                EventBus.getDefault().post(addressListAdapter.getInfos().get(position), EventBusTags.ADDRESS_CHANGE_EVENT);
                // 回传地址信息
                killMyself();
                break;
        }
    }

    @Override
    public void onRefresh() {
        if (!editTV.isSelected()) {
            mPresenter.getAddressList(true);
        }
    }
}
