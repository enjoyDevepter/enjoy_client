package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerConfirmOrderComponent;
import me.jessyan.mvparms.demo.di.module.ConfirmOrderModule;
import me.jessyan.mvparms.demo.mvp.contract.ConfirmOrderContract;
import me.jessyan.mvparms.demo.mvp.presenter.ConfirmOrderPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ConfirmOrderActivity extends BaseActivity<ConfirmOrderPresenter> implements ConfirmOrderContract.View, View.OnClickListener {
    @BindView(R.id.no_address)
    View noAddressV;
    @BindView(R.id.addres_info_layout)
    View addressV;
    @BindView(R.id.self_layout)
    View selfV;
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.dispatch)
    View dispatchV;

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
        noAddressV.setOnClickListener(this);
        addressV.setOnClickListener(this);
        selfV.setOnClickListener(this);
        backV.setOnClickListener(this);
        dispatchV.setOnClickListener(this);

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
            case R.id.no_address:
            case R.id.addres_info_layout:
                ArmsUtils.startActivity(AddressListActivity.class);
                break;
            case R.id.self_layout:
                break;
        }
    }
}