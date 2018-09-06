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
    @BindView(R.id.order_center)
    TextView orderCenterTV;
    @BindView(R.id.order_detail)
    TextView orderDetailTV;
    @BindView(R.id.pay_img)
    View payImg;
    @BindView(R.id.pay_result_info)
    View payResultInfoV;
    @BindView(R.id.success)
    View successV;
    @BindView(R.id.fail)
    View failV;
    @BindView(R.id.retry)
    View retryV;
    @BindView(R.id.fail_order_detail)
    View failOrderV;

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
        orderCenterTV.setOnClickListener(this);
        orderDetailTV.setOnClickListener(this);
        retryV.setOnClickListener(this);
        failOrderV.setOnClickListener(this);

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
                ArmsUtils.startActivity(MyOrderActivity.class);
                killMyself();
                break;
            case R.id.fail_order_detail:
            case R.id.order_detail:
                Intent detailIntent = new Intent(this, OrderDeatilsActivity.class);
                detailIntent.putExtra("orderId", getIntent().getStringExtra("orderId"));
                detailIntent.putExtra("orderType", getIntent().getStringExtra("orderType"));
                ArmsUtils.startActivity(detailIntent);
                killMyself();
                break;
            case R.id.retry:
                break;
        }
    }
}
