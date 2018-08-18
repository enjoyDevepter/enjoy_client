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
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerCouponComponent;
import me.jessyan.mvparms.demo.di.module.CouponModule;
import me.jessyan.mvparms.demo.mvp.contract.CouponContract;
import me.jessyan.mvparms.demo.mvp.presenter.CouponPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.CouponListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class CouponActivity extends BaseActivity<CouponPresenter> implements CouponContract.View, DefaultAdapter.OnRecyclerViewItemClickListener, View.OnClickListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.coupon)
    RecyclerView mRecyclerView;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    CouponListAdapter mAdapter;


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerCouponComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .couponModule(new CouponModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_coupon; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        titleTV.setText("优惠券");
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0, ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.address_list_item_space)));
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
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(this).extras();
        cache.put("coupon", mAdapter.getInfos().get(position));
        killMyself();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DefaultAdapter.releaseAllHolder(mRecyclerView);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
    }
}
