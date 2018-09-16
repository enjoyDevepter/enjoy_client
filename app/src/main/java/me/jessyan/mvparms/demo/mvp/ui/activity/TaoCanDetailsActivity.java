package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.cchao.MoneyView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
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
    @BindView(R.id.share)
    View shareV;
    @BindView(R.id.goods_iamges)
    Banner imagesB;
    @BindView(R.id.image_count)
    TextView imageCount;
    @BindView(R.id.salesPrice)
    MoneyView salesPriceTV;
    @BindView(R.id.totalPrice)
    MoneyView totalPriceTV;
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
    @BindView(R.id.detail_layout)
    TabLayout detailTab;
    @BindView(R.id.detailWV)
    WebView detailWV;
    @BindView(R.id.deposit)
    MoneyView depositTV;
    @BindView(R.id.tailMoney)
    MoneyView tailMoneyTV;
    @BindView(R.id.tel)
    View telV;
    @Inject
    RecyclerView.Adapter mAdapter;
    @Inject
    RecyclerView.LayoutManager layoutManager;
    @Inject
    RxPermissions mRxPermissions;

    MealDetailsResponse response;


    private Mobile mobile = new Mobile();
    private WebViewClient mClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            mobile.onGetWebContentHeight();
        }
    };

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
        }
    };

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
        collectV.setOnClickListener(this);
        shareV.setOnClickListener(this);
        imagesB.setImageLoader(new GlideImageLoader());
        imagesB.setIndicatorGravity(BannerConfig.CENTER);
        tabLayout.addTab(tabLayout.newTab().setText("套餐项目"));
        ArmsUtils.configRecyclerView(detailRV, layoutManager);
        detailRV.addItemDecoration(new SpacesItemDecoration(ArmsUtils.getDimens(this, R.dimen.divice_width), 0));
        detailRV.setAdapter(mAdapter);
        telV.setOnClickListener(this);

        detailWV.getSettings().setUseWideViewPort(true);
        detailWV.getSettings().setLoadWithOverviewMode(true);
        detailWV.addJavascriptInterface(mobile, "mobile");
        detailWV.setWebViewClient(mClient);
        detailTab.addTab(detailTab.newTab().setText("套餐详情"));
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
        salesPriceTV.setMoneyText(String.valueOf(response.getSetMealGoods().getSalePrice()));
        totalPriceTV.setMoneyText(String.valueOf(response.getSetMealGoods().getTotalPrice()));
        totalPriceTV.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        salesTV.setText(String.valueOf(response.getSetMealGoods().getSales()));
        collectV.setSelected("1".equals(response.getSetMealGoods().getFavorite()) ? true : false);
        depositTV.setMoneyText(String.valueOf(response.getSetMealGoods().getSalePrice()));
        tailMoneyTV.setMoneyText(String.valueOf(response.getSetMealGoods().getTotalPrice()));
        detailWV.loadUrl(response.getSetMealGoods().getContent());
        if ("0".equals(response.getSetMealGoods().getCanSale())) {
            buyV.setBackgroundColor(Color.parseColor("#ffc6c6c6"));
        } else {
            buyV.setBackgroundColor(Color.parseColor("#FFFF5656"));
            buyV.setOnClickListener(this);
        }

    }

    @Override
    public void updateCollect(boolean collect) {
        collectV.setSelected(collect);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
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
                mPresenter.tel(response.getTellphone());
                // 打电话
                break;
            case R.id.share:
                share();
                break;
        }
    }

    private void share() {
//        MealGoods goods = response.getSetMealGoods();
//        UMWeb web = new UMWeb(goods.());
//        web.setTitle(goods.getName());//标题
//        web.setDescription(goods.getTitle());
//        web.setThumb(new UMImage(this, goods.getImage()));
//        new ShareAction(this)
//                .withMedia(web)
//                .setCallback(shareListener)
//                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
//                .open();
    }


    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(detailRV);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
    }

    private class Mobile {
        @JavascriptInterface
        public void onGetWebContentHeight() {
            //重新调整webview高度
            detailWV.post(() -> {
                detailWV.measure(0, 0);
                int measuredHeight = detailWV.getMeasuredHeight();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) detailWV.getLayoutParams();
                layoutParams.height = measuredHeight;
                detailWV.setLayoutParams(layoutParams);
            });
        }
    }
}
