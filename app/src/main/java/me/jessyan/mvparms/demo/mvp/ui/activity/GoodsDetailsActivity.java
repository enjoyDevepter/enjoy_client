package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
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
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerGoodsDetailsComponent;
import me.jessyan.mvparms.demo.di.module.GoodsDetailsModule;
import me.jessyan.mvparms.demo.mvp.contract.GoodsDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.GoodsSpecValue;
import me.jessyan.mvparms.demo.mvp.model.entity.Promotion;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsDetailsResponse;
import me.jessyan.mvparms.demo.mvp.presenter.GoodsDetailsPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsPromotionAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.GlideImageLoader;

import static com.jess.arms.utils.ArmsUtils.getContext;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class GoodsDetailsActivity extends BaseActivity<GoodsDetailsPresenter> implements GoodsDetailsContract.View, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener, TagFlowLayout.OnSelectListener {

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
    @BindView(R.id.collect)
    View collectV;
    @BindView(R.id.promotion_close)
    View promotionCloseV;
    @BindView(R.id.spec_close)
    View spceCloseV;
    @BindView(R.id.specs)
    TagFlowLayout speceflowLayout;
    @BindView(R.id.promotionCV)
    RecyclerView promotionCV;
    @BindView(R.id.price_info)
    View priceInfoV;
    @BindView(R.id.priceTag)
    TextView priceTagTV;
    @BindView(R.id.time_limit_layout)
    View timeLimitLayoutV;
    @BindView(R.id.count_down_view)
    CountdownView countdownView;
    @BindView(R.id.salePrice)
    TextView salePriceTV;
    @BindView(R.id.salePrice_top)
    TextView salePriceTopTV;
    @BindView(R.id.newly)
    View newlyV;
    @BindView(R.id.secKillPrice)
    TextView secKillPriceTV;
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
        collectV.setOnClickListener(this);
        spceCloseV.setOnClickListener(this);
        promotionCloseV.setOnClickListener(this);
        tabLayout.addTab(tabLayout.newTab().setText("商品详情"));
        tabLayout.addTab(tabLayout.newTab().setText("相关日志"));

        detailWV.addJavascriptInterface(mobile, "mobile");
        detailWV.setWebViewClient(mClient);

        ArmsUtils.configRecyclerView(promotionCV, mLayoutManager);
        promotionCV.setAdapter(promotionAdapter);
        promotionAdapter.setOnItemClickListener(this);
        speceflowLayout.setAdapter(adapter);
        adapter.setSelectedList(0);
        String where = getIntent().getStringExtra("where");

        if ("timelimitdetail".equals(where)) {
            priceInfoV.setVisibility(View.VISIBLE);
            timeLimitLayoutV.setVisibility(View.VISIBLE);
            cartV.setVisibility(View.GONE);
            priceTagTV.setText("限时秒杀价");
            newlyV.setVisibility(View.GONE);
            salePriceTopTV.setVisibility(View.GONE);
            promotionInfosV.setVisibility(View.GONE);
            speceflowLayout.setMaxSelectCount(0);

        } else if ("newpeople".equals(where)) {
            priceInfoV.setVisibility(View.VISIBLE);
            cartV.setVisibility(View.GONE);
            priceTagTV.setText("新人专享价");
            promotionInfosV.setVisibility(View.GONE);
            timeLimitLayoutV.setVisibility(View.GONE);
            newlyV.setVisibility(View.VISIBLE);
            salePriceTopTV.setVisibility(View.VISIBLE);
            speceflowLayout.setMaxSelectCount(0);
        } else {
            priceInfoV.setVisibility(View.GONE);
            timeLimitLayoutV.setVisibility(View.GONE);
            cartV.setVisibility(View.VISIBLE);
            promotionInfosV.setVisibility(View.VISIBLE);
            newlyV.setVisibility(View.GONE);
            salePriceTopTV.setVisibility(View.GONE);
            speceflowLayout.setOnSelectListener(this);
        }

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
    public void updateCollect(boolean collect) {
        collectV.setSelected(collect);
    }

    @Override
    public void updateUI(GoodsDetailsResponse response) {
        imagesB.setImages(response.getImages());
        //banner设置方法全部调用完毕时最后调用
        imagesB.start();
        imagesB.isAutoPlay(false);

        imageCount.setText("1/" + response.getImages().size());
        nameTV.setText(response.getGoods().getName());
        saleCountTV.setText(String.valueOf(response.getGoods().getSales()));
        goodSpecTV.setText(response.getGoods().getGoodsSpecValue().getSpecValueName());
        detailWV.loadData(response.getGoods().getMobileDetail(), "text/html", null);

        if (response.getPromotionList() == null || response.getPromotionList().size() <= 0) {
            promotionInfosV.setVisibility(View.GONE);
        }

        if (null == response.getGoods().getGoodsSpecValue()) {
            specV.setVisibility(View.GONE);
        }

        collectV.setSelected("1".equals(response.getGoods().getIsFavorite()) ? true : false);

        String specValueId = (String) provideCache().get("specValueId");
        if (!ArmsUtils.isEmpty(specValueId)) {
            for (int i = 0; i < response.getGoodsSpecValueList().size(); i++) {
                if (response.getGoodsSpecValueList().get(i).getSpecValueId().equals(specValueId)) {
                    adapter.setSelectedList(i);
                    goodSpecTV.setText(response.getGoodsSpecValueList().get(i).getSpecValueName());
                    return;
                }
            }
        }

        String where = getIntent().getStringExtra("where");
        if ("timelimitdetail".equals(where)) {
            long count = response.getGoods().getEndDate() - response.getGoods().getSysDate();
            if (count > 86400) {
                DynamicConfig.Builder builder = new DynamicConfig.Builder();
                builder.setShowHour(false)
                        .setShowSecond(false)
                        .setShowMinute(false)
                        .setShowMillisecond(false)
                        .setShowDay(true)
                        .setSuffix("天");
                countdownView.dynamicShow(builder.build());
                countdownView.start(count);
            } else {
                countdownView.start(count);
            }
            priceTV.setText(String.valueOf(response.getGoods().getSecKillPrice()));
            salePriceTV.setText(String.valueOf(response.getGoods().getSalePrice()));
            salePriceTV.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            secKillPriceTV.setText(String.valueOf(response.getGoods().getSecKillPrice()));
            spcePriceTV.setText(String.valueOf(response.getGoods().getSecKillPrice()));
        } else if ("newpeople".equals(where)) {
            salePriceTopTV.setText("￥" + String.valueOf(response.getGoods().getSalePrice()));
            salePriceTopTV.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            salePriceTV.setText(String.valueOf(response.getGoods().getSalePrice()));
            salePriceTV.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            priceTV.setText(String.valueOf(response.getGoods().getVipPrice()));
            secKillPriceTV.setText(String.valueOf(response.getGoods().getVipPrice()));
            spcePriceTV.setText(String.valueOf(response.getGoods().getVipPrice()));
        } else {
            priceTV.setText(String.valueOf(response.getGoods().getSalePrice()));
            spcePriceTV.setText(String.valueOf(response.getGoods().getSalePrice()));
        }

    }

    @Override
    public Cache getCache() {
        return provideCache();
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
                mPresenter.addGoodsToCart();
                break;
            case R.id.buy:
                mPresenter.goOrderConfirm();
                break;
            case R.id.promotion:
                showPro(true);
                break;
            case R.id.mask_pro:
                showPro(false);
                break;
            case R.id.spec:
                showSpec(true);
                break;
            case R.id.mask_spec:
                showSpec(false);
                break;
            case R.id.collect:
                mPresenter.collectGoods(!collectV.isSelected());
                break;
            case R.id.promotion_close:
                showPro(false);
                break;
            case R.id.spec_close:
                showSpec(false);
                break;
        }
    }

    private void showSpec(boolean show) {
        if (adapter.getCount() <= 0) {
            return;
        }
        if (show) {
            maskSpecV.setVisibility(View.VISIBLE);
            maskSpecV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.mask_in));
            specLayoutV.setVisibility(View.VISIBLE);
            specLayoutV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.for_butom_in));
            Goods goods = ((Goods) provideCache().get("goods"));
            spceIDTV.setText(goods.getGoodsId());
            spceNameTV.setText(goods.getName());
            adapter.setSelectedList(0);
            //itemView 的 Context 就是 Activity, Glide 会自动处理并和该 Activity 的生命周期绑定
            mImageLoader.loadImage(spceImageIV.getContext(),
                    ImageConfigImpl
                            .builder()
                            .url(goods.getImage())
                            .imageView(spceImageIV)
                            .build());

        } else {
            maskSpecV.setVisibility(View.GONE);
            maskSpecV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.mask_out));
            specLayoutV.setVisibility(View.GONE);
            specLayoutV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.for_buttom_out));
        }
    }

    private void showPro(boolean show) {
        if (promotionAdapter.getInfos().size() <= 0) {
            return;
        }
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

    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {
        switch (viewType) {
            case R.layout.goods_promotion_item:
                List<Promotion> promotionList = promotionAdapter.getInfos();
                for (int i = 0; i < promotionList.size(); i++) {
                    Promotion p = promotionList.get(i);
                    if (i == position) {
                        p.setCheck(!p.isCheck());
                        promotionContentV.setVisibility(p.isCheck() ? View.VISIBLE : View.INVISIBLE);
                        promotionTV.setText(p.isCheck() ? promotionList.get(position).getTitle() : "");
                        provideCache().put("promotionId", p.isCheck() ? p.getPromotionId() : "");
                    } else {
                        p.setCheck(false);
                    }
                }
                promotionAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onSelected(Set<Integer> selectPosSet) {
        if (selectPosSet.size() > 0) {
            GoodsSpecValue goodsSpecValue = (GoodsSpecValue) adapter.getItem((int) selectPosSet.toArray()[0]);
            provideCache().put("specValueId", goodsSpecValue.getSpecValueId());
            goodSpecTV.setText(goodsSpecValue.getSpecValueName());
            mPresenter.getCoodsDetailsForSpecValueId();
        } else {
            goodSpecTV.setText("");
            provideCache().put("specValueId", "");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DefaultAdapter.releaseAllHolder(promotionCV);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
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
