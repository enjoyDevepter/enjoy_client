package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.AppManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import org.simple.eventbus.EventBus;

import java.text.SimpleDateFormat;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerOrderDeatilsComponent;
import me.jessyan.mvparms.demo.di.module.OrderDeatilsModule;
import me.jessyan.mvparms.demo.mvp.contract.OrderDeatilsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Address;
import me.jessyan.mvparms.demo.mvp.model.entity.order.OrderDetails;
import me.jessyan.mvparms.demo.mvp.model.entity.response.OrderDetailsResponse;
import me.jessyan.mvparms.demo.mvp.presenter.OrderDeatilsPresenter;
import me.jessyan.mvparms.demo.mvp.ui.widget.CustomDialog;
import me.jessyan.mvparms.demo.mvp.ui.widget.MoneyView;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static me.jessyan.mvparms.demo.mvp.ui.activity.HospitalInfoActivity.KEY_FOR_HOSPITAL_ID;
import static me.jessyan.mvparms.demo.mvp.ui.activity.HospitalInfoActivity.KEY_FOR_HOSPITAL_NAME;


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
    @BindView(R.id.hospital_tag)
    TextView hospitalTagTV;
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
    @BindView(R.id.hPayTypeDesc)
    TextView hPayTypeDescTV;
    @BindView(R.id.deposit)
    TextView depositTV;
    @BindView(R.id.tailMoney)
    TextView tailMoneyTV;
    @BindView(R.id.horder_coupon)
    TextView horderCouponV;
    @BindView(R.id.horder_money)
    TextView horderMoneyV;
    @BindView(R.id.address_info)
    View addressInfoV;
    @BindView(R.id.address_realName)
    TextView addressRealNameTV;
    @BindView(R.id.address_mobile)
    TextView addressMobileTV;
    @BindView(R.id.address)
    TextView addressTV;
    @BindView(R.id.remark)
    TextView remarkTV;
    @BindView(R.id.price)
    TextView priceTV;
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
    @Inject
    AppManager appManager;
    CustomDialog dialog = null;
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
        hospitalAddressTV.setOnClickListener(this);
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
        String orderType = getIntent().getStringExtra("orderType");
        provideCache().put("orderId", order.getOrderId());
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.hospital_address:
                if ("2".equals(orderType) || "5".equals(orderType) || "12".equals(orderType) || "13".equals(orderType)) {
                    Intent storeIntent = new Intent(this, StoreInfoActivity.class);
                    storeIntent.putExtra("store_name", order.getStore().getName());
                    storeIntent.putExtra("store_id", order.getStore().getStoreId());
                    ArmsUtils.startActivity(storeIntent);
                } else if ("3".equals(orderType) || "6".equals(orderType) || "7".equals(orderType) || "10".equals(orderType) || "11".equals(orderType)) {
                    Intent hospitalIntent = new Intent(this, HospitalInfoActivity.class);
                    hospitalIntent.putExtra(KEY_FOR_HOSPITAL_NAME, order.getHospital().getName());
                    hospitalIntent.putExtra(KEY_FOR_HOSPITAL_ID, order.getHospital().getHospitalId());
                    ArmsUtils.startActivity(hospitalIntent);
                }
                break;
            case R.id.left:
                if ("1".equals(orderType) || "4".equals(orderType) || "8".equals(orderType) || "9".equals(orderType)) {
                    if ("1".equals(order.getOrderStatus())) {
                        // 取消订单
                        showCancelDailog();
                    } else if ("4".equals(order.getOrderStatus())) {
                        // 查看物流
                        Intent intent = new Intent(getActivity(), LogisticsActivity.class);
                        intent.putExtra("orderId", order.getOrderId());
                        ArmsUtils.startActivity(intent);
                    }
                } else if ("2".equals(orderType) || "5".equals(orderType) || "12".equals(orderType) || "13".equals(orderType)) {
                    if ("1".equals(order.getOrderStatus())) {
                        // 取消订单
                        showCancelDailog();
                    }
                } else if ("3".equals(orderType) || "6".equals(orderType) || "7".equals(orderType) || "10".equals(orderType) || "11".equals(orderType)) {
                    if ("1".equals(order.getOrderStatus())) {
                        // 取消订单
                        showCancelDailog();
                    }
                }
                break;
            case R.id.right:

                if ("1".equals(orderType) || "4".equals(orderType) || "8".equals(orderType) || "9".equals(orderType)) {
                    if ("1".equals(order.getOrderStatus())) {
                        // 去支付
                        Intent intent = new Intent(this, PayActivity.class);
                        intent.putExtra("orderId", order.getOrderId());
                        ArmsUtils.startActivity(intent);
                    } else if ("3".equals(order.getOrderStatus())) {
                        // 提醒发货
                        mPresenter.reminding();
                    } else if ("4".equals(order.getOrderStatus())) {
                        // 确认收货
                        mPresenter.confirmReceipt();
                    } else if ("5".equals(order.getOrderStatus())) {
                        // 写日记
                        Intent intent = new Intent(getActivity(), ReleaseDiaryActivity.class);
                        intent.putExtra("orderId", order.getOrderId());
                        ArmsUtils.startActivity(intent);
                    }
                } else if ("2".equals(orderType) || "5".equals(orderType) || "12".equals(orderType) || "13".equals(orderType)) {
                    if ("1".equals(order.getOrderStatus())) {
                        // 去支付
                        Intent intent = new Intent(this, PayActivity.class);
                        intent.putExtra("orderId", order.getOrderId());
                        ArmsUtils.startActivity(intent);
                    } else if ("31".equals(order.getOrderStatus())) {
                        // 预约
                        EventBus.getDefault().post(0, EventBusTags.CHANGE_APPOINTMENT_TYPE);
                        EventBus.getDefault().post(3, EventBusTags.CHANGE_MAIN_ITEM);
                        appManager.killAllBeforeClass(MainActivity.class);
                    } else if ("5".equals(order.getOrderStatus())) {
                        // 写日记
                        Intent intent = new Intent(getActivity(), ReleaseDiaryActivity.class);
                        intent.putExtra("orderId", order.getOrderId());
                        ArmsUtils.startActivity(intent);
                    }
                } else if ("3".equals(orderType) || "6".equals(orderType) || "7".equals(orderType) || "10".equals(orderType) || "11".equals(orderType)) {
                    if ("1".equals(order.getOrderStatus())) {
                        // 去支付
                        Intent intent = new Intent(this, PayActivity.class);
                        intent.putExtra("orderId", order.getOrderId());
                        ArmsUtils.startActivity(intent);
                    } else if ("2".equals(order.getOrderStatus())) {
                        // 付尾款
                        Intent intent = new Intent(this, PayActivity.class);
                        intent.putExtra("orderId", order.getOrderId());
                        ArmsUtils.startActivity(intent);
                    } else if ("31".equals(order.getOrderStatus())) {
                        // 预约
                        EventBus.getDefault().post(1, EventBusTags.CHANGE_APPOINTMENT_TYPE);
                        EventBus.getDefault().post(3, EventBusTags.CHANGE_MAIN_ITEM);
                        appManager.killAllBeforeClass(MainActivity.class);
                    } else if ("5".equals(order.getOrderStatus())) {
                        // 写日记
                        Intent intent = new Intent(getActivity(), ReleaseDiaryActivity.class);
                        intent.putExtra("orderId", order.getOrderId());
                        ArmsUtils.startActivity(intent);
                    }
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

        String orderType = getIntent().getStringExtra("orderType");
        String status = response.getOrder().getOrderStatus();

        if ("1".equals(orderType) || "4".equals(orderType) || "8".equals(orderType) || "9".equals(orderType)) {
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
                rightTV.setVisibility(View.GONE);
                leftTV.setVisibility(View.GONE);
            }
            orderV.setVisibility(View.VISIBLE);
            hOrderV.setVisibility(View.GONE);
            orderPayV.setVisibility(View.VISIBLE);
            hOrderPayV.setVisibility(View.GONE);
            mealOrderV.setVisibility(View.GONE);
            payTypeDescTV.setText(orderDetails.getPayTypeDesc());
            deliveryMethodDescTV.setText(orderDetails.getDeliveryMethodDesc());
        } else if ("2".equals(orderType) || "5".equals(orderType) || "12".equals(orderType) || "13".equals(orderType)) {
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
            hOrderV.setVisibility(View.VISIBLE);
            orderPayV.setVisibility(View.GONE);
            hospitalTagTV.setText("选择店铺");
            if (orderDetails.getStore() != null) {
                hospitalAddressTV.setText(orderDetails.getStore().getName());
            }
            appointmentsTimeTV.setText(orderDetails.getReservationDate() + " " + orderDetails.getReservationTime());
            hPayTypeDescTV.setText(orderDetails.getPayTypeDesc());
            orderV.setVisibility(View.GONE);
            hOrderPayV.setVisibility(View.GONE);
            mealOrderV.setVisibility(View.VISIBLE);
            mealPriceTV.setText(ArmsUtils.formatLong(response.getOrder().getTotalPrice()));
            mealMoneyTV.setText(ArmsUtils.formatLong(orderDetails.getMoney()));
        } else if ("3".equals(orderType) || "6".equals(orderType) || "7".equals(orderType) || "10".equals(orderType) || "11".equals(orderType)) {
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
            hOrderV.setVisibility(View.VISIBLE);
            orderPayV.setVisibility(View.GONE);
            hospitalTagTV.setText("选择医院");
            if (orderDetails.getHospital() != null) {
                hospitalAddressTV.setText(orderDetails.getHospital().getName());
            }
            appointmentsTimeTV.setText(orderDetails.getReservationDate() + " " + orderDetails.getReservationTime());
            hPayTypeDescTV.setText(orderDetails.getPayTypeDesc());
            orderV.setVisibility(View.GONE);
            if ("7".equals(response.getOrder().getOrderType())) {
                hOrderPayV.setVisibility(View.VISIBLE);
                mealOrderV.setVisibility(View.GONE);
                tailMoneyTV.setText(String.valueOf(response.getOrder().getGoodsList().get(0).getTailMoney()));
                depositTV.setText(String.valueOf(response.getOrder().getGoodsList().get(0).getDeposit()));
            } else {
                hOrderPayV.setVisibility(View.GONE);
                mealOrderV.setVisibility(View.VISIBLE);
                mealPriceTV.setText(ArmsUtils.formatLong(response.getOrder().getTotalPrice()));
                mealMoneyTV.setText(ArmsUtils.formatLong(orderDetails.getMoney()));
            }
        }
        payTimeTV.setText(sdf.format(orderDetails.getOrderTime()));
        orderStatusDescTV.setText(orderDetails.getOrderStatusDesc());
        orderIdTV.setText(orderDetails.getOrderId());
        orderTimeDetailTV.setText(sdf.format(orderDetails.getOrderTime()));


        Address address = orderDetails.getOrderRecipientInfo();
        if (null == address) {
            addressInfoV.setVisibility(View.GONE);
        } else {
            addressInfoV.setVisibility(View.VISIBLE);
            addressRealNameTV.setText(address.getRealName());
            addressMobileTV.setText(address.getMobile());
            addressTV.setText(address.getProvinceName() + address.getCityName() + address.getCountyName() + address.getAddress());
        }

        remarkTV.setText(orderDetails.getRemark());
        priceTV.setText(ArmsUtils.formatLong(orderDetails.getPrice()));
        moneyTV.setText(ArmsUtils.formatLong(orderDetails.getMoney()));
        horderMoneyV.setText(ArmsUtils.formatLong(orderDetails.getMoney()));
        deductionMoneyTV.setText(ArmsUtils.formatLong(orderDetails.getDeductionMoney()));
        couponTV.setText(ArmsUtils.formatLong(orderDetails.getCoupon()));
        horderCouponV.setText(ArmsUtils.formatLong(orderDetails.getCoupon()));
        freightTV.setText(ArmsUtils.formatLong(orderDetails.getFreight()));
        payMoneyTV.setMoneyText(ArmsUtils.formatLong(orderDetails.getPayMoney()));
    }

    private void showCancelDailog() {
        dialog = CustomDialog.create(getSupportFragmentManager())
                .setViewListener(new CustomDialog.ViewListener() {
                    @Override
                    public void bindView(View view) {
                        ((TextView) view.findViewById(R.id.content)).setText("确认取消该订单吗？");
                        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismissAllowingStateLoss();
                            }
                        });
                        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPresenter.cancelOrder();
                            }
                        });
                    }
                })
                .setLayoutRes(R.layout.dialog_remove_good_for_cart)
                .setDimAmount(0.5f)
                .isCenter(true)
                .setWidth(ArmsUtils.getDimens(this, R.dimen.dialog_width))
                .setHeight(ArmsUtils.getDimens(this, R.dimen.dialog_height))
                .show();
    }
}
