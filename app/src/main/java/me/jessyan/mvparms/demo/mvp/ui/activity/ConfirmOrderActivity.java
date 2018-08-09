package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerConfirmOrderComponent;
import me.jessyan.mvparms.demo.di.module.ConfirmOrderModule;
import me.jessyan.mvparms.demo.mvp.contract.ConfirmOrderContract;
import me.jessyan.mvparms.demo.mvp.model.entity.response.OrderConfirmInfoResponse;
import me.jessyan.mvparms.demo.mvp.presenter.ConfirmOrderPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.OrderConfirmGoodsListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.CustomProgressDailog;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ConfirmOrderActivity extends BaseActivity<ConfirmOrderPresenter> implements ConfirmOrderContract.View, View.OnClickListener, OrderConfirmGoodsListAdapter.OnChildItemClickLinstener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.balance)
    TextView balanceTV;
    @BindView(R.id.payMoney)
    TextView payMoneyTV;
    @BindView(R.id.freight)
    TextView freightTV;
    @BindView(R.id.coupon)
    TextView couponTV;
    @BindView(R.id.money)
    TextView moneyTV;
    @BindView(R.id.deductionMoney)
    TextView deductionMoneyTV;
    @BindView(R.id.no_address)
    View noAddressV;
    @BindView(R.id.addres_info_layout)
    View addressV;
    @BindView(R.id.self_layout)
    View selfInfoV;
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
        }
    }

    private void changtDispatch(boolean self) {
        selfV.setSelected(!self);
        dispatchV.setSelected(self);
        selfV.setTextColor(!self ? seletcedColor : unseletcedColor);
        dispatchV.setTextColor(self ? seletcedColor : unseletcedColor);
        selfInfoV.setVisibility(!self ? View.VISIBLE : View.INVISIBLE);
        noAddressV.setVisibility(self ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void updateUI(OrderConfirmInfoResponse response) {
        this.response = response;
        balanceTV.setText(String.valueOf(response.getBalance()));
        payMoneyTV.setText(String.valueOf(response.getPayMoney()));
        freightTV.setText(String.valueOf(response.getFreight()));
        deductionMoneyTV.setText(String.valueOf(response.getDeductionMoney()));
        moneyTV.setText(String.valueOf(response.getMoney()));
        couponTV.setText(String.valueOf(response.getCoupon()));
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onChildItemClick(View v, OrderConfirmGoodsListAdapter.ViewName viewname, int position) {
        switch (viewname) {
            case ADD:
                break;
            case MINUS:
                break;
        }
    }
}
