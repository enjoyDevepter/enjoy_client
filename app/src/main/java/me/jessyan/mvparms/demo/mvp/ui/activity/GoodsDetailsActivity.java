package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.utils.ArmsUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.utils.AnimationUtil;
import me.jessyan.mvparms.demo.di.component.DaggerGoodsDetailsComponent;
import me.jessyan.mvparms.demo.di.module.GoodsDetailsModule;
import me.jessyan.mvparms.demo.mvp.contract.GoodsDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsDetailsResponse;
import me.jessyan.mvparms.demo.mvp.presenter.GoodsDetailsPresenter;
import me.jessyan.mvparms.demo.mvp.ui.widget.GlideImageLoader;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class GoodsDetailsActivity extends BaseActivity<GoodsDetailsPresenter> implements GoodsDetailsContract.View, View.OnClickListener {

    @BindView(R.id.back)
    View backV;
    @BindView(R.id.share)
    View shareV;
    @BindView(R.id.add_cart)
    View cartV;
    @BindView(R.id.buy)
    View buyV;
    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.detail)
    WebView detailWV;
    @BindView(R.id.goods_iamges)
    Banner imagesB;
    @BindView(R.id.name)
    TextView nameTV;
    @BindView(R.id.price)
    TextView priceTV;
    @BindView(R.id.sale)
    TextView saleTV;
    @BindView(R.id.promotion_name)
    TextView promotionTV;
    @BindView(R.id.sales_promotion)
    View promotionInfosV;
    @BindView(R.id.spec)
    View specV;
    @BindView(R.id.goods_spec)
    TextView goodSpecTV;
    @BindView(R.id.image_count)
    TextView imageCount;
    @BindView(R.id.spec_layout)
    View specLayoutV;
    @BindView(R.id.promotion_layout)
    View promLayoutV;
    @BindView(R.id.spec_image)
    ImageView spceImageIV;
    @BindView(R.id.spec_name)
    TextView spceNameTV;
    @BindView(R.id.spec_price)
    TextView spcePriceTV;
    @BindView(R.id.spec_goods_id)
    TextView spceIDTV;
    @BindView(R.id.specs)
    TagFlowLayout speceflowLayout;

    @Inject
    TagAdapter adapter;

    @Inject
    ImageLoader mImageLoader;

    private Mobile mobile = new Mobile();
    private WebViewClient mClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            mobile.onGetWebContentHeight();
        }
    };

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerGoodsDetailsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .goodsDetailsModule(new GoodsDetailsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_goods_details; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        shareV.setOnClickListener(this);
        cartV.setOnClickListener(this);
        buyV.setOnClickListener(this);
        specV.setOnClickListener(this);
        specLayoutV.setOnClickListener(this);
        promLayoutV.setOnClickListener(this);
        promotionInfosV.setOnClickListener(this);
        imagesB.setImageLoader(new GlideImageLoader());
        imagesB.setIndicatorGravity(BannerConfig.CENTER);
        tabLayout.addTab(tabLayout.newTab().setText("商品详情"));
        tabLayout.addTab(tabLayout.newTab().setText("相关日志"));

        detailWV.addJavascriptInterface(mobile, "mobile");
        detailWV.setWebViewClient(mClient);

//        mPresenter.getGoodsDetails(savedInstanceState.getString("goodsId"), savedInstanceState.getString("merchId"));
        mPresenter.getGoodsDetails("180719173530010072", "180719173530010074");
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
    public void updateUI(GoodsDetailsResponse response) {
        imagesB.setImages(response.getImages());
        //banner设置方法全部调用完毕时最后调用
        imagesB.start();
        imagesB.isAutoPlay(false);

        imageCount.setText("1/" + response.getImages().size());

        nameTV.setText(response.getGoods().getName());
        priceTV.setText(String.valueOf(response.getGoods().getSalePrice()));
        priceTV.setText(String.valueOf(response.getGoods().getSales()));
        promotionTV.setText(String.valueOf(response.getPromotionList().get(0).getTitle()));
        goodSpecTV.setText(response.getGoods().getGoodsSpecValues());
        detailWV.loadData(response.getGoods().getMobileDetail(), "text/html", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.share:
                break;
            case R.id.add_cart:
                break;
            case R.id.buy:
                break;
            case R.id.sales_promotion:
                promLayoutV.startAnimation(AnimationUtil.show());
                promLayoutV.setVisibility(View.VISIBLE);
                break;
            case R.id.spec:
                showSpec();
                break;
            case R.id.spec_layout:
                specLayoutV.startAnimation(AnimationUtil.dismiss());
                specLayoutV.setVisibility(View.GONE);
                break;
            case R.id.promotion_layout:
                promLayoutV.startAnimation(AnimationUtil.dismiss());
                promLayoutV.setVisibility(View.GONE);
                break;
        }
    }

    private void showSpec() {
//        specLayoutV.startAnimation(AnimationUtil.show());
//        specLayoutV.setVisibility(View.VISIBLE);
//
//
//        mImageLoader.loadImage(this,
//                ImageConfigImpl
//                        .builder()
//                        .url(response.getGoods().getImage())
//                        .imageView(spceImageIV)
//                        .build());
//
//        spceNameTV.setText(response.getGoods().getName());
//        spcePriceTV.setText(String.valueOf(response.getGoods().getSalePrice()));
//        spceIDTV.setText(response.getGoods().getMerchId());
//        specList.add("500ML");
//        specList.add("1000ML");
//        specList.add("1500ML");
//        speceflowLayout.setAdapter(adapter);

    }

    private class Mobile {
        @JavascriptInterface
        public void onGetWebContentHeight() {
            //重新调整webview高度
            detailWV.post(() -> {
                detailWV.measure(0, 0);
                int measuredHeight = detailWV.getMeasuredHeight();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) detailWV.getLayoutParams();
                layoutParams.weight = LinearLayout.LayoutParams.MATCH_PARENT;
                layoutParams.height = measuredHeight;
                detailWV.setLayoutParams(layoutParams);
            });


        }
    }
}
