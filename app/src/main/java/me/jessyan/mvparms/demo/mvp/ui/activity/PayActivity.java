package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import cn.ehanmy.pay.PayManager;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerPayComponent;
import me.jessyan.mvparms.demo.di.module.PayModule;
import me.jessyan.mvparms.demo.mvp.contract.PayContract;
import me.jessyan.mvparms.demo.mvp.model.entity.pay.response.PayInfoResponse;
import me.jessyan.mvparms.demo.mvp.presenter.PayPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class PayActivity extends BaseActivity<PayPresenter> implements PayContract.View, View.OnClickListener, PayManager.PayListener {

    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.order_id)
    TextView orderIdTV;
    @BindView(R.id.price)
    TextView priceTV;
    @BindView(R.id.pay_one)
    View payOneV;
    @BindView(R.id.pay_one_img)
    ImageView payOneIV;
    @BindView(R.id.pay_one_text)
    TextView payOneTV;
    @BindView(R.id.pay_one_status)
    View payOneStatus;
    @BindView(R.id.pay_two)
    View payTwoV;
    @BindView(R.id.pay_two_img)
    ImageView payTwoIV;
    @BindView(R.id.pay_two_text)
    TextView payTwoTV;
    @BindView(R.id.pay_two_status)
    View payTwoStatus;
    @BindView(R.id.confirm)
    View confirmV;

    @Inject
    ImageLoader mImageLoader;

    PayInfoResponse response;

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
        payOneV.setOnClickListener(this);
        payTwoV.setOnClickListener(this);
        orderIdTV.setText(getIntent().getStringExtra("orderId"));
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
            case R.id.pay_one:
                if (!payOneStatus.isSelected()) {
                    changeToPayOne(true);
                }
                break;
            case R.id.pay_two:
                if (!payTwoStatus.isSelected()) {
                    changeToPayOne(false);
                }
                break;
            case R.id.confirm:
                mPresenter.pay();
                break;
        }
    }

    private void changeToPayOne(boolean toOne) {
        provideCache().put("payId", toOne ? response.getPayEntryList().get(0).getPayId() : response.getPayEntryList().get(1).getPayId());
        payOneStatus.setSelected(toOne);
        payTwoStatus.setSelected(!toOne);
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
    public void updateUI(PayInfoResponse response) {
        this.response = response;
        priceTV.setText(ArmsUtils.formatLong(response.getPayMoney()));

        payOneTV.setText(response.getPayEntryList().get(0).getName());

        provideCache().put("payMoney", response.getPayMoney());

        provideCache().put("payId", response.getPayEntryList().get(0).getPayId());

        payOneStatus.setSelected(true);
        //itemView 的 Context 就是 Activity, Glide 会自动处理并和该 Activity 的生命周期绑定
        mImageLoader.loadImage(this,
                ImageConfigImpl
                        .builder()
                        .isCenterCrop(true)
                        .placeholder(R.drawable.place_holder_img)
                        .url(response.getPayEntryList().get(0).getImage())
                        .imageView(payOneIV)
                        .build());

        payTwoTV.setText(response.getPayEntryList().get(1).getName());

        mImageLoader.loadImage(this,
                ImageConfigImpl
                        .builder()
                        .isCenterCrop(true)
                        .placeholder(R.drawable.place_holder_img)
                        .url(response.getPayEntryList().get(1).getImage())
                        .imageView(payTwoIV)
                        .build());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPaySuccess(Map<String, String> rawResult) {
    }

    @Override
    public void onPayError(int error_code, String message) {
    }

    @Override
    public void onPayCancel() {
    }
}
