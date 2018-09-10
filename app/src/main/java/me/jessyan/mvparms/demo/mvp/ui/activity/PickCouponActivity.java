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
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerPickCouponComponent;
import me.jessyan.mvparms.demo.di.module.PickCouponModule;
import me.jessyan.mvparms.demo.mvp.contract.PickCouponContract;
import me.jessyan.mvparms.demo.mvp.model.entity.PickCoupon;
import me.jessyan.mvparms.demo.mvp.presenter.PickCouponPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.PickCouponListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class PickCouponActivity extends BaseActivity<PickCouponPresenter> implements PickCouponContract.View, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener {
    @BindView(R.id.bg)
    View bgV;
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.coupon)
    RecyclerView mRecyclerView;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    PickCouponListAdapter mAdapter;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerPickCouponComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .pickCouponModule(new PickCouponModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_pick_coupon; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        titleTV.setText("领取优惠卷");
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0, ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.address_list_item_space)));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(mRecyclerView);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
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
        }
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
    public void updateUI(String bgURL) {
    }

    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {
        PickCoupon coupon = (PickCoupon) data;
        if ("1".equals(coupon.getStatus())) {
            provideCache().put("couponTemplateId", coupon.getCouponTemplateId());
            provideCache().put("couponPromotionId", coupon.getCouponPromotionId());
            mPresenter.pickCoupon();
        }
    }
}
