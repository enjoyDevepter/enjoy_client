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
import me.jessyan.mvparms.demo.di.component.DaggerSelfPickupAddrListComponent;
import me.jessyan.mvparms.demo.di.module.SelfPickupAddrListModule;
import me.jessyan.mvparms.demo.mvp.contract.SelfPickupAddrListContract;
import me.jessyan.mvparms.demo.mvp.presenter.SelfPickupAddrListPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class SelfPickupAddrListActivity extends BaseActivity<SelfPickupAddrListPresenter> implements SelfPickupAddrListContract.View, View.OnClickListener {

    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.district)
    TextView districtV;
    @BindView(R.id.district_layout)
    View districtLayoutV;
    @BindView(R.id.store)
    TextView storeV;
    @BindView(R.id.store_layout)
    View storeLayoutV;
    @BindView(R.id.confirm)
    View confirmV;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerSelfPickupAddrListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .selfPickupAddrListModule(new SelfPickupAddrListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_self_pickup_addr_list; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("自提地址");
        backV.setOnClickListener(this);
        districtV.setOnClickListener(this);
        storeV.setOnClickListener(this);
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
            case R.id.district_layout:
                break;
            case R.id.store_layout:
                break;
            case R.id.confirm:
                break;
        }
    }
}
