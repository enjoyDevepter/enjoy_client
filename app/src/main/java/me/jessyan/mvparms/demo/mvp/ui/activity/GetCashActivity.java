package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import me.jessyan.mvparms.demo.di.component.DaggerGetCashComponent;
import me.jessyan.mvparms.demo.di.module.GetCashModule;
import me.jessyan.mvparms.demo.mvp.contract.GetCashContract;
import me.jessyan.mvparms.demo.mvp.presenter.GetCashPresenter;

import me.jessyan.mvparms.demo.R;


import static com.jess.arms.utils.Preconditions.checkNotNull;


public class GetCashActivity extends BaseActivity<GetCashPresenter> implements GetCashContract.View {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerGetCashComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .getCashModule(new GetCashModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_get_cash; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

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
}
