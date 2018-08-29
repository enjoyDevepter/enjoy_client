package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.cchao.MoneyView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import java.text.SimpleDateFormat;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerOrderDeatilsComponent;
import me.jessyan.mvparms.demo.di.module.OrderDeatilsModule;
import me.jessyan.mvparms.demo.mvp.contract.OrderDeatilsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Address;
import me.jessyan.mvparms.demo.mvp.model.entity.order.OrderDetails;
import me.jessyan.mvparms.demo.mvp.model.entity.response.OrderDetailsResponse;
import me.jessyan.mvparms.demo.mvp.presenter.OrderDeatilsPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class OrderDeatilsActivity extends BaseActivity<OrderDeatilsPresenter> implements OrderDeatilsContract.View, View.OnClickListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.payTime)
    TextView payTimeTV;
    @BindView(R.id.orderStatusDesc)
    TextView orderStatusDescTV;
    @BindView(R.id.orders)
    RecyclerView ordersRV;
    @BindView(R.id.orderId)
    TextView orderIdTV;
    @BindView(R.id.orderTime_detail)
    TextView orderTimeDetailTV;
    @BindView(R.id.payTypeDesc)
    TextView payTypeDescTV;
    @BindView(R.id.deliveryMethodDesc)
    TextView deliveryMethodDescTV;
    @BindView(R.id.hospital_address)
    TextView hospitalAddressTV;
    @BindView(R.id.appointments_time)
    TextView appointmentsTimeTV;
    @BindView(R.id.sOrder_layout)
    View orderV;
    @BindView(R.id.hOrder_layout)
    View hOrderV;
    @BindView(R.id.order_pay_layout)
    View orderPayV;
    @BindView(R.id.horder_pay_layout)
    View hOrderPayV;
    @BindView(R.id.deposit)
    TextView depositTV;
    @BindView(R.id.tailMoney)
    TextView tailMoneyTV;
    @BindView(R.id.horder_coupon)
    TextView horderCouponV;
    @BindView(R.id.horder_money)
    TextView horderMoneyV;
    @BindView(R.id.address_realName)
    TextView addressRealNameTV;
    @BindView(R.id.address_mobile)
    TextView addressMobileTV;
    @BindView(R.id.address)
    TextView addressTV;
    @BindView(R.id.remark)
    TextView remarkTV;
    @BindView(R.id.price)
    MoneyView priceTV;
    @BindView(R.id.money)
    TextView moneyTV;
    @BindView(R.id.deductionMoney)
    TextView deductionMoneyTV;
    @BindView(R.id.coupon)
    TextView couponTV;
    @BindView(R.id.freight)
    TextView freightTV;
    @BindView(R.id.payMoney)
    MoneyView payMoneyTV;
    @BindView(R.id.right)
    TextView rightTV;
    @BindView(R.id.left)
    TextView leftTV;
    @BindView(R.id.operation)
    View operationV;
    @BindView(R.id.meal_order_pay_layout)
    View mealOrderV;
    @BindView(R.id.meal_price)
    TextView mealPriceTV;
    @BindView(R.id.meal_money)
    TextView mealMoneyTV;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.Adapter mAdapter;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private OrderDetailsResponse response;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerOrderDeatilsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .orderDeatilsModule(new OrderDeatilsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_order_deatils; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("订单详情");
        backV.setOnClickListener(this);
        leftTV.setOnClickListener(this);
        rightTV.setOnClickListener(this);

        ArmsUtils.configRecyclerView(ordersRV, mLayoutManager);
        ordersRV.setAdapter(mAdapter);
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
        OrderDetails order = response.getOrder();
        int orderType = getIntent().getIntExtra("type", 0);
        provideCache().put("orderId", order.getOrderId());
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.left:
                switch (orderType) {
                    case 0:
                        if ("1".equals(order.getOrderStatus())) {
                            // 取消订单
                            mPresenter.cancelOrder();
                        } else if ("4".equals(order.getOrderStatus())) {
                            // 查看物流
                        }
                        break;
                    case 1:
                        break;
                    case 2:
                        if ("1".equals(order.getOrderStatus())) {
                            // 取消订单
                            mPresenter.cancelOrder();
                        }
                        break;
                }
                break;
            case R.id.right:
                switch (orderType) {
                    case 0:
                        if ("1".equals(order.getOrderStatus())) {
                            // 去支付

                        } else if ("3".equals(order.getOrderStatus())) {
                            // 提醒发货
                            mPresenter.reminding();
                        } else if ("4".equals(order.getOrderStatus())) {
                            // 确认收货
                            mPresenter.confirmReceipt();
                        } else if ("5".equals(order.getOrderStatus())) {
                            // 写日记
                            Intent intent = new Intent(getActivity(), ReleaseDiaryActivity.class);
                            intent.putExtra("imageURl", order.getGoodsList().get(0).getImage());
                            intent.putExtra("name", order.getGoodsList().get(0).getName());
                            intent.putExtra("price", order.getGoodsList().get(0).getSalePrice());
                            intent.putExtra("goodsId", order.getGoodsList().get(0).getGoodsId());
                            intent.putExtra("merchId", order.getGoodsList().get(0).getMerchId());
                            intent.putExtra("orderId", order.getOrderId());
                            ArmsUtils.startActivity(intent);
                        }
                        break;
                    case 1:
                        break;
                    case 2:
                        if ("1".equals(order.getOrderStatus())) {
                            // 去支付

                        } else if ("2".equals(order.getOrderStatus())) {
                            // 付尾款

                        } else if ("31".equals(order.getOrderStatus())) {
                            // 预约
                            Intent makeIntent = new Intent(this, MyMealDetailsActivity.class);
                            makeIntent.putExtra("orderId", order.getOrderId());
                            makeIntent.putExtra("mealName", order.getGoodsList().get(0).getName());
                            ArmsUtils.startActivity(makeIntent);
                        } else if ("5".equals(order.getOrderStatus())) {
                            // 写日记
                            Intent intent = new Intent(getActivity(), ReleaseDiaryActivity.class);
                            intent.putExtra("imageURl", order.getSetMealGoodsList().get(0).getImage());
                            intent.putExtra("name", order.getSetMealGoodsList().get(0).getName());
                            intent.putExtra("price", order.getSetMealGoodsList().get(0).getSalePrice());
                            intent.putExtra("goodsId", order.getSetMealGoodsList().get(0).getSetMealId());
                            intent.putExtra("merchId", order.getSetMealGoodsList().get(0).getSetMealId());
                            intent.putExtra("orderId", order.getOrderId());
                            ArmsUtils.startActivity(intent);
                        }
                        break;
                }
                break;
        }
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
    public void updateUI(OrderDetailsResponse response) {
        this.response = response;
        OrderDetails orderDetails = response.getOrder();

        int orderType = getIntent().getIntExtra("type", 0);
        String status = response.getOrder().getOrderStatus();
        switch (orderType) {
            case 0:
                if ("1".equals(status)) {
                    leftTV.setVisibility(View.VISIBLE);
                    leftTV.setText("取消订单");
                    rightTV.setVisibility(View.VISIBLE);
                    rightTV.setText("去支付");
                } else if ("3".equals(status)) {
                    rightTV.setVisibility(View.VISIBLE);
                    rightTV.setText("提醒发货");
                    leftTV.setVisibility(View.GONE);
                } else if ("4".equals(status)) {
                    leftTV.setVisibility(View.VISIBLE);
                    leftTV.setText("查看物流");
                    rightTV.setVisibility(View.VISIBLE);
                    rightTV.setText("确认收货");
                } else if ("5".equals(status)) {
                    rightTV.setText("写日记");
                    rightTV.setVisibility(View.VISIBLE);
                    leftTV.setVisibility(View.GONE);
                }
                orderV.setVisibility(View.VISIBLE);
                hOrderV.setVisibility(View.GONE);
                orderPayV.setVisibility(View.VISIBLE);
                hOrderPayV.setVisibility(View.GONE);
                mealOrderV.setVisibility(View.GONE);
                payTypeDescTV.setText(orderDetails.getPayTypeDesc());
                deliveryMethodDescTV.setText(orderDetails.getDeliveryMethodDesc());
                break;
            case 1:
                break;
            case 2:
                if (status.equals("1")) {
                    leftTV.setVisibility(View.VISIBLE);
                    leftTV.setText("取消订单");
                    rightTV.setVisibility(View.VISIBLE);
                    rightTV.setText("去支付");
                } else if (status.equals("2")) {
                    rightTV.setVisibility(View.VISIBLE);
                    rightTV.setText("付尾款");
                    leftTV.setVisibility(View.GONE);
                } else if (status.equals("31")) {
                    leftTV.setVisibility(View.GONE);
                    rightTV.setVisibility(View.VISIBLE);
                    rightTV.setText("预约");
                } else if (status.equals("5")) {
                    rightTV.setText("写日记");
                    rightTV.setVisibility(View.VISIBLE);
                    leftTV.setVisibility(View.GONE);
                }
                if (response.getOrder().equals("7")) {
                    orderV.setVisibility(View.GONE);
                    hOrderV.setVisibility(View.VISIBLE);
                    orderPayV.setVisibility(View.GONE);
                    hOrderPayV.setVisibility(View.VISIBLE);
                    mealOrderV.setVisibility(View.GONE);
                    hospitalAddressTV.setText(orderDetails.getHospital().getAddress());
                    appointmentsTimeTV.setText(orderDetails.getAppointmentsDate() + " " + orderDetails.getAppointmentsTime());
                } else {
                    orderV.setVisibility(View.GONE);
                    hOrderV.setVisibility(View.GONE);
                    orderPayV.setVisibility(View.GONE);
                    hOrderPayV.setVisibility(View.GONE);
                    mealOrderV.setVisibility(View.VISIBLE);
                    mealPriceTV.setText(ArmsUtils.formatLong(response.getOrder().getPayMoney()));
                    mealMoneyTV.setText(ArmsUtils.formatLong(orderDetails.getMoney()));
                }
                break;
        }


        payTimeTV.setText(sdf.format(orderDetails.getOrderTime()));
        orderStatusDescTV.setText(orderDetails.getOrderStatusDesc());
        orderIdTV.setText(orderDetails.getOrderId());
        orderTimeDetailTV.setText(sdf.format(orderDetails.getOrderTime()));


        Address address = orderDetails.getOrderRecipientInfo();
        addressRealNameTV.setText(address.getRealName());
        addressMobileTV.setText(address.getMobile());
        addressTV.setText(address.getAddress());

        remarkTV.setText(orderDetails.getRemark());
        priceTV.setMoneyText(ArmsUtils.formatLong(orderDetails.getPrice()));
        moneyTV.setText(ArmsUtils.formatLong(orderDetails.getMoney()));
        horderMoneyV.setText(ArmsUtils.formatLong(orderDetails.getMoney()));
        deductionMoneyTV.setText(ArmsUtils.formatLong(orderDetails.getDeductionMoney()));
        couponTV.setText(ArmsUtils.formatLong(orderDetails.getCoupon()));
        horderCouponV.setText(ArmsUtils.formatLong(orderDetails.getCoupon()));
        freightTV.setText(ArmsUtils.formatLong(orderDetails.getFreight()));
        payMoneyTV.setMoneyText(ArmsUtils.formatLong(orderDetails.getPayMoney()));
    }
}
