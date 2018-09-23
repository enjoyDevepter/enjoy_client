package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerConfirmOrderComponent;
import me.jessyan.mvparms.demo.di.module.ConfirmOrderModule;
import me.jessyan.mvparms.demo.mvp.contract.ConfirmOrderContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Address;
import me.jessyan.mvparms.demo.mvp.model.entity.Coupon;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.Store;
import me.jessyan.mvparms.demo.mvp.model.entity.response.OrderConfirmInfoResponse;
import me.jessyan.mvparms.demo.mvp.presenter.ConfirmOrderPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.OrderConfirmGoodsListAdapter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ConfirmOrderActivity extends BaseActivity<ConfirmOrderPresenter> implements ConfirmOrderContract.View, View.OnClickListener, OrderConfirmGoodsListAdapter.OnChildItemClickLinstener, View.OnFocusChangeListener {
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
    OrderConfirmInfoResponse response;
    private Address address;

    private SelfPickupAddrListActivity.ListType listType = SelfPickupAddrListActivity.ListType.STORE;
    private volatile boolean shouldSubmit;

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


    @Subscriber(tag = EventBusTags.STORE_CHANGE_EVENT)
    private void updateStore(Store store) {
        provideCache().put("storeId", store.getStoreId());
        selfAddressTV.setText(store.getAddress() + " " + store.getName());
    }

    @Subscriber(tag = EventBusTags.CHANGE_COUPON)
    private void updateCoupon(Coupon coupon) {
        provideCache().put("couponId", coupon.getCouponId());
        couponTextTV.setText(coupon.getName());
        mPresenter.getOrderConfirmInfo();
    }

    @Subscriber(tag = EventBusTags.ADDRESS_CHANGE_EVENT)
    private void updateAddress(Address address) {
        this.address = address;
        provideCache().put("addressId", address.getAddressId());
        addressTV.setText(address.getProvinceName() + " " + address.getCityName() + " " + address.getCountyName() + " " + address.getAddress());
        nameTV.setText(address.getReceiverName());
        phoneTV.setText(address.getPhone());
        noAddressV.setVisibility(View.GONE);
        addressV.setVisibility(View.VISIBLE);
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
        confirmV.setOnClickListener(this);
        moneyET.setOnFocusChangeListener(this);

        provideCache().put("deliveryMethodId", "1");
        List<Goods> goodsList = getIntent().getParcelableArrayListExtra("goodsList");
        if (goodsList != null) {
            provideCache().put("goodsList", goodsList);
        }
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        ((OrderConfirmGoodsListAdapter) mAdapter).setOnChildItemClickLinstener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
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
                Intent intent2 = new Intent(this, SelfPickupAddrListActivity.class);
                intent2.putExtra(SelfPickupAddrListActivity.KEY_FOR_ACTIVITY_LIST_TYPE, listType);
                ArmsUtils.startActivity(intent2);
                break;
            case R.id.dispatch:
                changtDispatch(true);
                break;
            case R.id.self:
                changtDispatch(false);
                break;
            case R.id.coupon_layout:
                Intent intent = new Intent(getActivity().getApplication(), CouponActivity.class);
                intent.putExtra("type", "优惠券");
                intent.putParcelableArrayListExtra("coupons", (ArrayList<? extends Parcelable>) response.getCouponList());
                ArmsUtils.startActivity(intent);
                break;
            case R.id.confirm:
                provideCache().put("remark", remarkET.getText().toString());
                mPresenter.placeOrder();
                break;
        }
    }

    private void changtDispatch(boolean self) {
        provideCache().put("deliveryMethodId", self ? "1" : "0");
        selfV.setSelected(!self);
        dispatchV.setSelected(self);
        selfV.setTextColor(!self ? seletcedColor : unseletcedColor);
        dispatchV.setTextColor(self ? seletcedColor : unseletcedColor);
        selfInfoV.setVisibility(!self ? View.VISIBLE : View.GONE);
        if (provideCache().get("addressId") != null) {
            addressV.setVisibility(self ? View.VISIBLE : View.GONE);
        } else {
            noAddressV.setVisibility(self ? View.VISIBLE : View.GONE);
        }

    }

    @Override
    public void updateUI(OrderConfirmInfoResponse response) {
        this.response = response;

        List<Coupon> couponList = response.getCouponList();
        if (null == couponList || null != couponList && couponList.size() <= 0) {
            String where = getActivity().getIntent().getStringExtra("where");
            if ("timelimitdetail".equals(where)) {
                couponLayoutV.setVisibility(View.GONE);
            } else if ("newpeople".equals(where)) {
                couponLayoutV.setVisibility(View.GONE);
            } else {
                couponTextTV.setText("暂无优惠卷");
            }
        } else {
            couponLayoutV.setOnClickListener(this);
            for (Coupon coupon : couponList) {
                if (coupon.getCouponId().equals(response.getCouponId())) {
                    couponTextTV.setText(coupon.getName());
                    break;
                }
            }
        }

        // 配送方式
        OrderConfirmInfoResponse.Delivery delivery = response.getDeliveryMethod();
        boolean supportDelivery = "1".equals(delivery.getIsDeliveryStaff());
        boolean supportStore = "1".equals(delivery.getIsStoreOneSelf());
        if (supportDelivery && supportStore) { // 都支持
            provideCache().put("deliveryMethodId", "1");
            dispatchV.setVisibility(View.VISIBLE);
            selfV.setVisibility(View.VISIBLE);
            noAddressV.setVisibility(View.VISIBLE);
            addressV.setVisibility(View.GONE);
            selfInfoV.setVisibility(View.GONE);
        } else if (supportDelivery && !supportStore) { //只支持快递
            provideCache().put("deliveryMethodId", "1");
            dispatchV.setVisibility(View.VISIBLE);
            selfV.setVisibility(View.GONE);
            noAddressV.setVisibility(View.VISIBLE);
            addressV.setVisibility(View.GONE);
            selfInfoV.setVisibility(View.GONE);
        } else if (!supportDelivery && supportStore) { //只支持自取
            provideCache().put("deliveryMethodId", "0");
            selfV.setSelected(true);
            selfV.setVisibility(View.VISIBLE);
            selfInfoV.setVisibility(View.VISIBLE);
            dispatchV.setVisibility(View.GONE);
            noAddressV.setVisibility(View.GONE);
            addressV.setVisibility(View.GONE);
        }

        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(this).extras();
        if (address != null) {
            addressTV.setText(address.getProvinceName() + " " + address.getCityName() + " " + address.getCountyName() + " " + address.getAddress());
            nameTV.setText(address.getReceiverName());
            phoneTV.setText(address.getPhone());
            noAddressV.setVisibility(View.GONE);
            addressV.setVisibility(View.VISIBLE);
        } else {
            noAddressV.setVisibility(View.VISIBLE);
            addressV.setVisibility(View.GONE);
        }
        if (cache.get(listType.getDataKey()) != null) {
            Store store = (Store) cache.get(listType.getDataKey());
            selfAddressTV.setText(store.getName());
        }

        moneyET.setHint(ArmsUtils.formatLong(response.getMoney()));
        balanceTV.setText(ArmsUtils.formatLong(response.getBalance()));
        totalPrice.setText(ArmsUtils.formatLong(response.getPrice()));
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
        List<Goods> goodsList = ((OrderConfirmGoodsListAdapter) mAdapter).getInfos();
        Goods goods = goodsList.get(position);
        String where = getIntent().getStringExtra("where");
        if (!ArmsUtils.isEmpty(where)) {
            return;
        }
        switch (viewname) {
            case ADD:
                goods.setNums(goods.getNums() + 1);
                break;
            case MINUS:
                if (goods.getNums() == 1) {
                    return;
                }
                goods.setNums(goods.getNums() - 1);
                break;
        }
        provideCache().put("goodsList", goodsList);
        mPresenter.getOrderConfirmInfo();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                //使EditText触发一次失去焦点事件
                v.setFocusable(false);
//                v.setFocusable(true); //这里不需要是因为下面一句代码会同时实现这个功能
                v.setFocusableInTouchMode(true);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(mRecyclerView);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            shouldSubmit = hasFocus;
        } else {
            if (shouldSubmit) {
                String m = moneyET.getText().toString();
                if (!ArmsUtils.isEmpty(m)) {
                    moneyET.setText("");
                    provideCache().put("money", Long.valueOf(m) * 100);
                }
                shouldSubmit = hasFocus;
                mPresenter.getOrderConfirmInfo();
            }
        }
    }
}
