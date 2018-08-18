package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerPayComponent;
import me.jessyan.mvparms.demo.di.module.PayModule;
import me.jessyan.mvparms.demo.mvp.contract.PayContract;
import me.jessyan.mvparms.demo.mvp.presenter.PayPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class PayActivity extends BaseActivity<PayPresenter> implements PayContract.View, View.OnClickListener {

    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.pay_for_zfb)
    View zfbPay;
    @BindView(R.id.order_id)
    TextView orderIdTV;
    @BindView(R.id.price)
    TextView priceTV;
    @BindView(R.id.zfb)
    View zfbV;
    @BindView(R.id.pay_for_wx)
    View wxPay;
    @BindView(R.id.wx)
    View wxV;
    @BindView(R.id.confirm)
    View confirmV;
    @BindView(R.id.order_goods)
    RecyclerView mRecyclerView;
    @Inject
    RecyclerView.Adapter mAdapter;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerPayComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .payModule(new PayModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_pay; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("立即支付");
        backV.setOnClickListener(this);
        confirmV.setOnClickListener(this);
        zfbPay.setOnClickListener(this);
        wxPay.setOnClickListener(this);
        zfbV.setSelected(true);
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        orderIdTV.setText(getIntent().getStringExtra("orderId"));
        priceTV.setText(ArmsUtils.formatLong(getIntent().getLongExtra("payMoney", 0)));
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
            case R.id.pay_for_zfb:
                if (!zfbV.isSelected()) {
                    changeToWX(false);
                }
                break;
            case R.id.pay_for_wx:
                if (!wxV.isSelected()) {
                    changeToWX(true);
                }
                break;
            case R.id.confirm:
                if (zfbV.isSelected()) {

                } else {

                }
                break;
        }
    }

    private void changeToWX(boolean wxPay) {
        wxV.setSelected(wxPay);
        zfbV.setSelected(!wxPay);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DefaultAdapter.releaseAllHolder(mRecyclerView);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
    }
}
