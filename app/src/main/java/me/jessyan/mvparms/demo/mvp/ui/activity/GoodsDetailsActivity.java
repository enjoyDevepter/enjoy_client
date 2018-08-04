package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.utils.ArmsUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerGoodsDetailsComponent;
import me.jessyan.mvparms.demo.di.module.GoodsDetailsModule;
import me.jessyan.mvparms.demo.mvp.contract.GoodsDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.GoodsDetails;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsDetailsResponse;
import me.jessyan.mvparms.demo.mvp.presenter.GoodsDetailsPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsPromotionAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.GlideImageLoader;

import static com.jess.arms.utils.ArmsUtils.getContext;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class GoodsDetailsActivity extends BaseActivity<GoodsDetailsPresenter> implements GoodsDetailsContract.View, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener, TagFlowLayout.OnTagClickListener {

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
    @BindView(R.id.saleCount)
    TextView saleCountTV;
    @BindView(R.id.promotion_name)
    TextView promotionTV;
    @BindView(R.id.promotion)
    View promotionInfosV;
    @BindView(R.id.spec)
    View specV;
    @BindView(R.id.mask_pro)
    View maskProV;
    @BindView(R.id.mask_spec)
    View maskSpecV;
    @BindView(R.id.goods_spec)
    TextView goodSpecTV;
    @BindView(R.id.image_count)
    TextView imageCount;
    @BindView(R.id.spec_layout)
    View specLayoutV;
    @BindView(R.id.promotion_layout)
    View promLayoutV;
    @BindView(R.id.promotion_content)
    View promotionContentV;
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
    @BindView(R.id.promotionCV)
    RecyclerView promotionCV;
    @Inject
    TagAdapter adapter;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    GoodsPromotionAdapter promotionAdapter;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;


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
        maskProV.setOnClickListener(this);
        maskSpecV.setOnClickListener(this);
        imagesB.setImageLoader(new GlideImageLoader());
        imagesB.setIndicatorGravity(BannerConfig.CENTER);
        tabLayout.addTab(tabLayout.newTab().setText("商品详情"));
        tabLayout.addTab(tabLayout.newTab().setText("相关日志"));

        detailWV.addJavascriptInterface(mobile, "mobile");
        detailWV.setWebViewClient(mClient);

        ArmsUtils.configRecyclerView(promotionCV, mLayoutManager);
        promotionCV.setAdapter(promotionAdapter);
        promotionAdapter.setOnItemClickListener(this);
        speceflowLayout.setAdapter(adapter);

        speceflowLayout.setOnTagClickListener(this);
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
        saleCountTV.setText(String.valueOf(response.getGoods().getSales()));
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
            case R.id.promotion:
                showPro(true);
                break;
            case R.id.spec:
                showSpec(true);
                break;
            case R.id.mask_pro:
                showPro(false);
                break;
            case R.id.mask_spec:
                showSpec(false);
                break;
        }
    }

    private void showSpec(boolean show) {
        if (show) {
            maskSpecV.setVisibility(View.VISIBLE);
            maskSpecV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.mask_in));
            specLayoutV.setVisibility(View.VISIBLE);
            specLayoutV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.for_butom_in));
        } else {
            maskSpecV.setVisibility(View.GONE);
            maskSpecV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.mask_out));
            specLayoutV.setVisibility(View.GONE);
            specLayoutV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.for_buttom_out));
        }
    }

    private void showPro(boolean show) {
        if (show) {
            maskProV.setVisibility(View.VISIBLE);
            maskProV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.mask_in));
            promLayoutV.setVisibility(View.VISIBLE);
            promLayoutV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.for_butom_in));
        } else {
            maskProV.setVisibility(View.GONE);
            maskProV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.mask_out));
            promLayoutV.setVisibility(View.GONE);
            promLayoutV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.for_buttom_out));
        }
    }

    private void showSpec() {
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

    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {
        switch (viewType) {
            case R.layout.goods_promotion_item:
                List<GoodsDetails.Promotion> promotionList = promotionAdapter.getInfos();
                for (int i = 0; i < promotionList.size(); i++) {
                    GoodsDetails.Promotion p = promotionList.get(i);
                    p.setCheck(i == position ? p.isCheck() ? false : true : false);
                }
                promotionContentV.setVisibility(View.VISIBLE);
                promotionTV.setText(promotionList.get(position).getTitle());
                promotionAdapter.notifyDataSetChanged();
                showPro(false);
                break;
        }
    }


    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        view.setSelected(true);
        return false;
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
