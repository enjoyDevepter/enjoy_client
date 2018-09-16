package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerPayResultComponent;
import me.jessyan.mvparms.demo.di.module.PayResultModule;
import me.jessyan.mvparms.demo.mvp.contract.PayResultContract;
import me.jessyan.mvparms.demo.mvp.presenter.PayResultPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class PayResultActivity extends BaseActivity<PayResultPresenter> implements PayResultContract.View, View.OnClickListener {

    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.order_detail)
    View orderDetailV;
    @BindView(R.id.charge_order_detail)
    View chargeOrderTetailV;
    @BindView(R.id.pay_success)
    View paySuccessV;
    @BindView(R.id.charge_success)
    View chargeSuccessV;
    @BindView(R.id.order_center)
    View orderCenterV;

    @BindView(R.id.order_id)
    TextView orderIdTV;
    @BindView(R.id.pay_type)
    TextView payTypeTV;
    @BindView(R.id.money)
    TextView moneyTV;
    @BindView(R.id.time)
    TextView timeTV;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerPayResultComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .payResultModule(new PayResultModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_pay_result; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("支付成功");
        backV.setOnClickListener(this);
        orderDetailV.setOnClickListener(this);
        chargeOrderTetailV.setOnClickListener(this);
        orderCenterV.setOnClickListener(this);

        boolean wait = getIntent().getBooleanExtra("wait", false);
        paySuccessV.setVisibility(wait ? View.INVISIBLE : View.VISIBLE);
        chargeSuccessV.setVisibility(wait ? View.VISIBLE : View.INVISIBLE);
        orderIdTV.setText(getIntent().getStringExtra("orderId"));
        payTypeTV.setText(getIntent().getStringExtra("payTypeDesc"));
        moneyTV.setText(ArmsUtils.formatLong(getIntent().getLongExtra("payMoney", 0)));
        timeTV.setText(sdf.format(getIntent().getLongExtra("orderTime", 0)));
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
            case R.id.order_center:
                Intent intent = new Intent(this, MyOrderActivity.class);
                String orderType = getIntent().getStringExtra("orderType");
                if ("1".equals(orderType) || "4".equals(orderType) || "8".equals(orderType) || "9".equals(orderType)) { // 普通订单
                    intent.putExtra("type", 0);
                } else if ("2".equals(orderType) || "5".equals(orderType)) { // 生美订单
                    intent.putExtra("type", 1);
                } else if ("3".equals(orderType) || "6".equals(orderType) || "7".equals(orderType) || "10".equals(orderType) || "11".equals(orderType)) { // 医美订单
                    intent.putExtra("type", 2);
                }
                ArmsUtils.startActivity(intent);
                break;
            case R.id.order_detail:
            case R.id.charge_order_detail:
                Intent detailIntent = new Intent(this, OrderDeatilsActivity.class);
                detailIntent.putExtra("orderId", getIntent().getStringExtra("orderId"));
                detailIntent.putExtra("orderType", getIntent().getStringExtra("orderType"));
                ArmsUtils.startActivity(detailIntent);
                killMyself();
                break;
        }
    }
}
