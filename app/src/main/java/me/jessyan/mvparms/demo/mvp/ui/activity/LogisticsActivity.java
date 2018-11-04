package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
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
import me.jessyan.mvparms.demo.di.component.DaggerLogisticsComponent;
import me.jessyan.mvparms.demo.di.module.LogisticsModule;
import me.jessyan.mvparms.demo.mvp.contract.LogisticsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.order.response.LogisticsResponse;
import me.jessyan.mvparms.demo.mvp.presenter.LogisticsPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class LogisticsActivity extends BaseActivity<LogisticsPresenter> implements LogisticsContract.View, View.OnClickListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.confirm)
    View confirmV;
    @BindView(R.id.id)
    TextView idTV;
    @BindView(R.id.name)
    TextView nameTV;

    LogisticsResponse logisticsResponse;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerLogisticsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .logisticsModule(new LogisticsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_logistics; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        titleTV.setText("查看物流");
        confirmV.setOnClickListener(this);
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
            case R.id.confirm:
                Intent articleIntent = new Intent(this, PlatformActivity.class);
                articleIntent.putExtra("url", logisticsResponse.getUrl());
                ArmsUtils.startActivity(articleIntent);
                break;
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void updateUI(LogisticsResponse response) {
        this.logisticsResponse = response;
        idTV.setText(response.getLogisticsCode());
        nameTV.setText(response.getCompany());
    }
}
