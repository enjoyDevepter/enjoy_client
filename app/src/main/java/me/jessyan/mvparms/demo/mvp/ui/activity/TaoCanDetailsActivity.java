package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerTaoCanDetailsComponent;
import me.jessyan.mvparms.demo.di.module.TaoCanDetailsModule;
import me.jessyan.mvparms.demo.mvp.contract.TaoCanDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.response.MealDetailsResponse;
import me.jessyan.mvparms.demo.mvp.presenter.TaoCanDetailsPresenter;
import me.jessyan.mvparms.demo.mvp.ui.widget.GlideImageLoader;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class TaoCanDetailsActivity extends BaseActivity<TaoCanDetailsPresenter> implements TaoCanDetailsContract.View, View.OnClickListener {

    @BindView(R.id.back)
    View backV;
    @BindView(R.id.buy)
    View buyV;
    @BindView(R.id.goods_iamges)
    Banner imagesB;
    @BindView(R.id.image_count)
    TextView imageCount;
    @BindView(R.id.salesPrice)
    TextView salesPriceTV;
    @BindView(R.id.totalPrice)
    TextView totalPriceTV;
    @BindView(R.id.collect)
    View collectV;
    @BindView(R.id.sales)
    TextView salesTV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.detail)
    RecyclerView detailRV;
    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.deposit)
    TextView depositTV;
    @BindView(R.id.tailMoney)
    TextView tailMoneyTV;
    @BindView(R.id.tel)
    View telV;
    @Inject
    RecyclerView.Adapter mAdapter;
    @Inject
    RecyclerView.LayoutManager layoutManager;

    MealDetailsResponse response;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerTaoCanDetailsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .taoCanDetailsModule(new TaoCanDetailsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_tao_can_details; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        buyV.setOnClickListener(this);
        collectV.setOnClickListener(this);
        imagesB.setImageLoader(new GlideImageLoader());
        imagesB.setIndicatorGravity(BannerConfig.CENTER);
        tabLayout.addTab(tabLayout.newTab().setText("套餐详情"));
        ArmsUtils.configRecyclerView(detailRV, layoutManager);
        detailRV.addItemDecoration(new SpacesItemDecoration(ArmsUtils.getDimens(this, R.dimen.divice_width), 0));
        detailRV.setAdapter(mAdapter);
        telV.setOnClickListener(this);
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
    public void updateUI(MealDetailsResponse response) {
        this.response = response;
        imagesB.setImages(response.getSetMealGoods().getImages());
        imagesB.start();
        imagesB.isAutoPlay(false);
        imageCount.setText("1/" + response.getSetMealGoods().getImages().size());
        titleTV.setText(String.valueOf(response.getSetMealGoods().getName()));
        salesPriceTV.setText(String.valueOf(response.getSetMealGoods().getSalePrice()));
        totalPriceTV.setText("￥" + String.valueOf(response.getSetMealGoods().getTotalPrice()));
        salesTV.setText(String.valueOf(response.getSetMealGoods().getSales()));
        collectV.setSelected("1".equals(response.getSetMealGoods().getFavorite()) ? true : false);
        depositTV.setText(String.valueOf(response.getSetMealGoods().getSalePrice()));
        tailMoneyTV.setText(String.valueOf(response.getSetMealGoods().getTotalPrice()));
    }

    @Override
    public void updateCollect(boolean collect) {
        collectV.setSelected(collect);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.buy:
                mPresenter.buy();
                break;
            case R.id.collect:
                mPresenter.collectGoods(!collectV.isSelected());
                break;
            case R.id.tel:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(detailRV);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
    }
}
