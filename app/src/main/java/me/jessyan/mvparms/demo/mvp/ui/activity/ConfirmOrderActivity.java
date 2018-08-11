package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerConfirmOrderComponent;
import me.jessyan.mvparms.demo.di.module.ConfirmOrderModule;
import me.jessyan.mvparms.demo.mvp.contract.ConfirmOrderContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Address;
import me.jessyan.mvparms.demo.mvp.model.entity.Store;
import me.jessyan.mvparms.demo.mvp.model.entity.request.OrderConfirmInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.OrderConfirmInfoResponse;
import me.jessyan.mvparms.demo.mvp.presenter.ConfirmOrderPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.OrderConfirmGoodsListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.CustomProgressDailog;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ConfirmOrderActivity extends BaseActivity<ConfirmOrderPresenter> implements ConfirmOrderContract.View, View.OnClickListener, OrderConfirmGoodsListAdapter.OnChildItemClickLinstener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.totalPrice)
    TextView totalPrice;
    @BindView(R.id.balance)
    TextView balanceTV;
    @BindView(R.id.payMoney)
    TextView payMoneyTV;
    @BindView(R.id.freight)
    TextView freightTV;
    @BindView(R.id.coupon)
    TextView couponTV;
    @BindView(R.id.money)
    EditText moneyET;
    @BindView(R.id.selt_money)
    TextView moneyTV;
    @BindView(R.id.deductionMoney)
    TextView deductionMoneyTV;
    @BindView(R.id.no_address)
    View noAddressV;
    @BindView(R.id.addres_info_layout)
    View addressV;
    @BindView(R.id.address)
    TextView addressTV;
    @BindView(R.id.name)
    TextView nameTV;
    @BindView(R.id.phone)
    TextView phoneTV;
    @BindView(R.id.self_layout)
    View selfInfoV;
    @BindView(R.id.self_address)
    TextView selfAddressTV;
    @BindView(R.id.coupon_layout)
    View couponLayoutV;
    @BindView(R.id.coupon_text)
    TextView couponTextTV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.dispatch)
    TextView dispatchV;
    @BindView(R.id.self)
    TextView selfV;
    @BindView(R.id.confirm)
    View confirmV;
    @BindView(R.id.remark)
    EditText remarkET;
    @BindView(R.id.order_goods)
    RecyclerView mRecyclerView;
    @BindColor(R.color.order_confirm_type_seleted)
    int seletcedColor;
    @BindColor(R.color.order_confirm_type_unseleted)
    int unseletcedColor;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.Adapter mAdapter;
    CustomProgressDailog progressDailog;
    OrderConfirmInfoResponse response;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerConfirmOrderComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .confirmOrderModule(new ConfirmOrderModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_confirm_order; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    protected void onResume() {
        super.onResume();

        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(this).extras();

        if ("1".equals(provideCache().get("deliveryMethod"))) {
            if (cache.get("memberAddressInfo") != null) {
                Address address = (Address) cache.get("memberAddressInfo");
                addressTV.setText(address.getProvinceName() + " " + address.getCityName() + " " + address.getCountyName() + " " + address.getAddress());
                nameTV.setText(address.getReceiverName());
                phoneTV.setText(address.getPhone());
                noAddressV.setVisibility(View.GONE);
                addressV.setVisibility(View.VISIBLE);
            } else {
                noAddressV.setVisibility(View.VISIBLE);
                addressV.setVisibility(View.GONE);
            }
        } else {
            if (cache.get("storeInfo") != null) {
                Store store = (Store) cache.get("storeInfo");
                selfAddressTV.setText(store.getName());
            }
        }

    }


    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("确认订单");
        dispatchV.setSelected(true);
        noAddressV.setOnClickListener(this);
        addressV.setOnClickListener(this);
        selfInfoV.setOnClickListener(this);
        backV.setOnClickListener(this);
        selfV.setOnClickListener(this);
        dispatchV.setOnClickListener(this);
        couponLayoutV.setOnClickListener(this);
        confirmV.setOnClickListener(this);

        List<OrderConfirmInfoRequest.OrderGoods> goodsList = getIntent().getParcelableArrayListExtra("goodsList");
        if (goodsList != null) {
            provideCache().put("goodsList", goodsList);
        }
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        ((OrderConfirmGoodsListAdapter) mAdapter).setOnChildItemClickLinstener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void showLoading() {
        progressDailog = new CustomProgressDailog(this);
        progressDailog.show();
    }

    @Override
    public void hideLoading() {
        progressDailog.dismiss();
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
            case R.id.no_address:
            case R.id.addres_info_layout:
                ArmsUtils.startActivity(AddressListActivity.class);
                break;
            case R.id.self_layout:
                ArmsUtils.startActivity(SelfPickupAddrListActivity.class);
                break;
            case R.id.dispatch:
                changtDispatch(true);
                break;
            case R.id.self:
                changtDispatch(false);
                break;
            case R.id.coupon_layout:
                Intent intent = new Intent(getActivity().getApplication(), CouponActivity.class);
                intent.putParcelableArrayListExtra("coupons", (ArrayList<? extends Parcelable>) response.getCouponList());
                ArmsUtils.startActivity(intent);
                break;
            case R.id.confirm:
                String m = moneyET.getText().toString();
                if (!ArmsUtils.isEmpty(m)) {
                    provideCache().put("money", Long.valueOf(m));
                }
                provideCache().put("remark", remarkET.getText().toString());
                mPresenter.placeOrder();
                break;
        }
    }

    private void changtDispatch(boolean self) {
        provideCache().put("deliveryMethod", self ? "1" : "0");
        selfV.setSelected(!self);
        dispatchV.setSelected(self);
        selfV.setTextColor(!self ? seletcedColor : unseletcedColor);
        dispatchV.setTextColor(self ? seletcedColor : unseletcedColor);
        selfInfoV.setVisibility(!self ? View.VISIBLE : View.GONE);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(this).extras();
        if (cache.get("memberAddressInfo") != null) {
            addressV.setVisibility(self ? View.VISIBLE : View.GONE);
        } else {
            noAddressV.setVisibility(self ? View.VISIBLE : View.GONE);
        }

    }

    @Override
    public void updateUI(OrderConfirmInfoResponse response) {
        this.response = response;
        if (response.getCouponList() == null || (response.getCouponList() != null && response.getCouponList().size() <= 0)) {
            couponLayoutV.setVisibility(View.GONE);
        }

        // 配送方式
        OrderConfirmInfoResponse.Delivery delivery = response.getDeliveryMethod();
        boolean supportDelivery = "1".equals(delivery.getIsDeliveryStaff());
        boolean supportStore = "1".equals(delivery.getIsStoreOneSelf());
        if (supportDelivery && supportStore) { // 都支持
            provideCache().put("deliveryMethod", "1");
            dispatchV.setVisibility(View.VISIBLE);
            selfV.setVisibility(View.VISIBLE);
            noAddressV.setVisibility(View.VISIBLE);
            addressV.setVisibility(View.GONE);
            selfInfoV.setVisibility(View.GONE);
        } else if (supportDelivery && !supportStore) { //只支持快递
            provideCache().put("deliveryMethod", "1");
            dispatchV.setVisibility(View.VISIBLE);
            selfV.setVisibility(View.GONE);
            noAddressV.setVisibility(View.VISIBLE);
            addressV.setVisibility(View.GONE);
            selfInfoV.setVisibility(View.GONE);
        } else if (!supportDelivery && supportStore) { //只支持自取
            provideCache().put("deliveryMethod", "0");
            selfV.setSelected(true);
            selfV.setVisibility(View.VISIBLE);
            selfInfoV.setVisibility(View.VISIBLE);
            dispatchV.setVisibility(View.GONE);
            noAddressV.setVisibility(View.GONE);
            addressV.setVisibility(View.GONE);
        }

        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(this).extras();
        if (cache.get("memberAddressInfo") != null) {
            Address address = (Address) cache.get("memberAddressInfo");
            addressTV.setText(address.getProvinceName() + " " + address.getCityName() + " " + address.getCountyName() + " " + address.getAddress());
            nameTV.setText(address.getReceiverName());
            phoneTV.setText(address.getPhone());
            noAddressV.setVisibility(View.GONE);
            addressV.setVisibility(View.VISIBLE);
        } else {
            noAddressV.setVisibility(View.VISIBLE);
            addressV.setVisibility(View.GONE);
        }
        if (cache.get("storeInfo") != null) {
            Store store = (Store) cache.get("storeInfo");
            selfAddressTV.setText(store.getName());
        }

        balanceTV.setText(String.valueOf(response.getBalance()));
        totalPrice.setText(ArmsUtils.formatLong(response.getTotalPrice()));
        payMoneyTV.setText(ArmsUtils.formatLong(response.getPayMoney()));
        freightTV.setText(ArmsUtils.formatLong(response.getFreight()));
        deductionMoneyTV.setText(ArmsUtils.formatLong(response.getDeductionMoney()));
        moneyTV.setText(ArmsUtils.formatLong(response.getMoney()));
        couponTV.setText(ArmsUtils.formatLong(response.getCoupon()));
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
    public void onChildItemClick(View v, OrderConfirmGoodsListAdapter.ViewName viewname, int position) {
        List<OrderConfirmInfoResponse.GoodsBean> goodsList = ((OrderConfirmGoodsListAdapter) mAdapter).getInfos();
        switch (viewname) {
            case ADD:
                goodsList.get(position).setNums(goodsList.get(position).getNums() + 1);
                break;
            case MINUS:
                if (goodsList.get(position).getNums() == 1) {
                    return;
                }
                goodsList.get(position).setNums(goodsList.get(position).getNums() - 1);
                break;
        }
        provideCache().put("goodsList", goodsList);
        mPresenter.getOrderConfirmInfo();
    }
}
