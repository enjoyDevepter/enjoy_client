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
import com.jess.arms.utils.ArmsUtils;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerCartComponent;
import me.jessyan.mvparms.demo.di.module.CartModule;
import me.jessyan.mvparms.demo.mvp.contract.CartContract;
import me.jessyan.mvparms.demo.mvp.presenter.CartPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class CartActivity extends BaseActivity<CartPresenter> implements CartContract.View, View.OnClickListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.edit)
    TextView editTV;
    @BindView(R.id.cartList)
    RecyclerView cartRV;
    @Inject
    RecyclerView.Adapter mAdapter;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerCartComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .cartModule(new CartModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_cart;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        titleTV.setText("购物车");
        editTV.setOnClickListener(this);
        ArmsUtils.configRecyclerView(cartRV, mLayoutManager);
        cartRV.setAdapter(mAdapter);
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
            case R.id.edit:
                break;
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
