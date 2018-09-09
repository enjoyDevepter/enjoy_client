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
import me.jessyan.mvparms.demo.di.component.DaggerRechargeFinishedComponent;
import me.jessyan.mvparms.demo.di.module.RechargeFinishedModule;
import me.jessyan.mvparms.demo.mvp.contract.RechargeFinishedContract;
import me.jessyan.mvparms.demo.mvp.presenter.RechargeFinishedPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class RechargeFinishedActivity extends BaseActivity<RechargeFinishedPresenter> implements RechargeFinishedContract.View, View.OnClickListener {

    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.success)
    View successV;
    @BindView(R.id.wait)
    View waitV;
    @BindView(R.id.finish)
    View finishV;
    @BindView(R.id.money)
    TextView moneyTV;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerRechargeFinishedComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .rechargeFinishedModule(new RechargeFinishedModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_recharge_finished; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        titleTV.setText("充值");
        finishV.setOnClickListener(this);
        boolean success = getIntent().getBooleanExtra("success", false);
        successV.setVisibility(success ? View.VISIBLE : View.INVISIBLE);
        waitV.setVisibility(success ? View.INVISIBLE : View.VISIBLE);
        moneyTV.setText("您的现金币充值后的余额为" + getIntent().getStringExtra("money") + "元");
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
            case R.id.finish:
                break;
        }
    }
}
