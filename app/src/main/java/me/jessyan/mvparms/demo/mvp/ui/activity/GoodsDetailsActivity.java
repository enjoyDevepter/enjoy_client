package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.cchao.MoneyView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

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
import me.jessyan.mvparms.demo.mvp.ui.adapter.DiaryListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsPromotionAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.SpecLabelTextProvider;
import me.jessyan.mvparms.demo.mvp.ui.widget.GlideImageLoader;
import me.jessyan.mvparms.demo.mvp.ui.widget.LabelsView;

import static com.jess.arms.utils.ArmsUtils.getContext;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class GoodsDetailsActivity extends BaseActivity<GoodsDetailsPresenter> implements GoodsDetailsContract.View, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener, LabelsView.OnLabelSelectChangeListener {

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
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.goods_iamges)
    Banner imagesB;
    @BindView(R.id.name)
    TextView nameTV;
    @BindView(R.id.price)
    MoneyView priceTV;
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
    MoneyView spcePriceTV;
    @BindView(R.id.spec_goods_id)
    TextView spceIDTV;
    @BindView(R.id.collect)
    View collectV;
    @BindView(R.id.promotion_close)
    View promotionCloseV;
    @BindView(R.id.spec_close)
    View spceCloseV;
    @BindView(R.id.specs)
    LabelsView speceLabelsView;
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
    MoneyView salePriceTV;
    @BindView(R.id.salePrice_top)
    TextView salePriceTopTV;
    @BindView(R.id.newly)
    View newlyV;
    @BindView(R.id.secKillPrice)
    MoneyView secKillPriceTV;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    GoodsPromotionAdapter promotionAdapter;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    DiaryListAdapter mAdapter;

    GoodsDetailsResponse response;


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
    private List<View> views = new ArrayList<>();
    private String[] titles = new String[]{"商品详情", "相关日志"};

    // 第一个页面
    private WebView detailWV;

    // 第二个页面
    private RecyclerView dirayRV;
    private SwipeRefreshLayout diraySRL;

    private Mobile mobile = new Mobile();
    private WebViewClient mClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            mobile.onGetWebContentHeight();
        }
    };

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;

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

        ArmsUtils.configRecyclerView(promotionCV, mLayoutManager);
        promotionCV.setAdapter(promotionAdapter);
        promotionAdapter.setOnItemClickListener(this);
        String where = getIntent().getStringExtra("where");

        if ("timelimitdetail".equals(where)) {
            priceInfoV.setVisibility(View.VISIBLE);
            timeLimitLayoutV.setVisibility(View.VISIBLE);
            cartV.setVisibility(View.GONE);
            priceTagTV.setText("限时秒杀价");
            speceLabelsView.setSelectType(LabelsView.SelectType.NONE);
            newlyV.setVisibility(View.GONE);
            salePriceTopTV.setVisibility(View.GONE);
            promotionInfosV.setVisibility(View.GONE);

        } else if ("newpeople".equals(where)) {
            priceInfoV.setVisibility(View.VISIBLE);
            cartV.setVisibility(View.GONE);
            priceTagTV.setText("新人专享价");
            speceLabelsView.setSelectType(LabelsView.SelectType.NONE);
            promotionInfosV.setVisibility(View.GONE);
            timeLimitLayoutV.setVisibility(View.GONE);
            newlyV.setVisibility(View.VISIBLE);
            salePriceTopTV.setVisibility(View.VISIBLE);
        } else {
            priceInfoV.setVisibility(View.GONE);
            timeLimitLayoutV.setVisibility(View.GONE);
            cartV.setVisibility(View.VISIBLE);
            promotionInfosV.setVisibility(View.VISIBLE);
            newlyV.setVisibility(View.GONE);
            salePriceTopTV.setVisibility(View.GONE);
        }
    }


    private void initViewPage() {

        viewpager.removeAllViews();
        views.clear();
        // 初始化商品详情
        detailWV = new WebView(this);
        views.add(detailWV);
        detailWV.getSettings().setUseWideViewPort(true);
        detailWV.getSettings().setLoadWithOverviewMode(true);
        detailWV.addJavascriptInterface(mobile, "mobile");
        detailWV.setWebViewClient(mClient);
        tabLayout.addTab(tabLayout.newTab().setText(titles[0]));


        // 初始化viewPager
        viewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View view = views.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        });

        tabLayout.setupWithViewPager(viewpager);

    }

    /**
     * 开始加载更多
     */
    @Override
    public void startLoadMore() {
        isLoadingMore = true;
    }

    /**
     * 结束加载更多
     */
    @Override
    public void endLoadMore() {
        isLoadingMore = false;
    }

    @Override
    public void setLoadedAllItems(boolean has) {
        this.hasLoadedAllItems = has;
    }


    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.getGoodsForDiary();
                }

                @Override
                public boolean isLoading() {
                    return isLoadingMore;
                }

                @Override
                public boolean hasLoadedAllItems() {
                    return hasLoadedAllItems;
                }
            };

            mPaginate = Paginate.with(dirayRV, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
            mPaginate.setHasMoreDataToLoad(false);
        }
    }

    @Override
    public void showLoading() {
        diraySRL.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        diraySRL.setRefreshing(false);
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
        if (null == this.response ||
                !this.response.getGoodsSpecValueList().equals(response.getGoodsSpecValueList())) {
            speceLabelsView.setLabels(response.getGoodsSpecValueList(), new SpecLabelTextProvider());
            provideCache().put("specValueId", response.getGoods().getGoodsSpecValue().getSpecValueId());
            String specValueId = (String) provideCache().get("specValueId");
            if (!ArmsUtils.isEmpty(specValueId)) {
                for (int i = 0; i < response.getGoodsSpecValueList().size(); i++) {
                    if (response.getGoodsSpecValueList().get(i).getSpecValueId().equals(specValueId)) {
                        if (speceLabelsView.getSelectType() == LabelsView.SelectType.NONE) {
                            speceLabelsView.setSelectOne(i);
                        } else {
                            speceLabelsView.setSelects(i);
                        }
                        goodSpecTV.setText(response.getGoodsSpecValueList().get(i).getSpecValueName());
                        break;
                    }
                }
            }
        }
        this.response = response;

        imagesB.setImages(response.getImages());
        //banner设置方法全部调用完毕时最后调用
        imagesB.start();
        imagesB.isAutoPlay(false);

        shareV.setVisibility(ArmsUtils.isEmpty(response.getGoods().getShareUrl()) ? View.INVISIBLE : View.VISIBLE);

        imageCount.setText("1/" + response.getImages().size());
        nameTV.setText(response.getGoods().getName());
        saleCountTV.setText(String.valueOf(response.getGoods().getSales()));
        goodSpecTV.setText(response.getGoods().getGoodsSpecValue().getSpecValueName());

        initViewPage();

        detailWV.loadUrl(response.getGoods().getMobileDetail());


        List<Promotion> promotions = response.getPromotionList();
        if (promotions == null || promotions.size() <= 0) {
            promotionInfosV.setVisibility(View.GONE);
        } else {
            promotionInfosV.setVisibility(View.VISIBLE);
            promotionContentV.setVisibility(View.VISIBLE);
            promotionTV.setText(promotions.get(0).getTitle());
            provideCache().put("promotionId", promotions.get(0).getPromotionId());
        }

        if (null == response.getGoods().getGoodsSpecValue()) {
            specV.setVisibility(View.GONE);
        }

        collectV.setSelected("1".equals(response.getGoods().getIsFavorite()) ? true : false);

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
            priceTV.setMoneyText(String.valueOf(response.getGoods().getSecKillPrice()));
            salePriceTV.setMoneyText(String.valueOf(response.getGoods().getSalePrice()));
            salePriceTV.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            secKillPriceTV.setMoneyText(String.valueOf(response.getGoods().getSecKillPrice()));
            spcePriceTV.setMoneyText(String.valueOf(response.getGoods().getSecKillPrice()));
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) specV.getLayoutParams();
            layoutParams.topMargin = 0;
            specV.setLayoutParams(layoutParams);
        } else if ("newpeople".equals(where)) {
            salePriceTopTV.setText("￥" + String.valueOf(response.getGoods().getSalePrice()));
            salePriceTopTV.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            salePriceTV.setMoneyText(String.valueOf(response.getGoods().getSalePrice()));
            salePriceTV.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            priceTV.setMoneyText(String.valueOf(response.getGoods().getVipPrice()));
            secKillPriceTV.setMoneyText(String.valueOf(response.getGoods().getVipPrice()));
            spcePriceTV.setMoneyText(String.valueOf(response.getGoods().getVipPrice()));
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) specV.getLayoutParams();
            layoutParams.topMargin = 0;
            specV.setLayoutParams(layoutParams);
        } else {
            priceTV.setMoneyText(String.valueOf(response.getGoods().getSalePrice()));
            spcePriceTV.setMoneyText(String.valueOf(response.getGoods().getSalePrice()));
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) specV.getLayoutParams();
            layoutParams.topMargin = ArmsUtils.getDimens(this, R.dimen.space_5);
            specV.setLayoutParams(layoutParams);
        }

    }

    @Override
    public void updateDiaryUI(boolean hasDate) {
        if (hasDate && views.size() < 2) {
            tabLayout.addTab(tabLayout.newTab().setText(titles[1]));
            diraySRL = (SwipeRefreshLayout) LayoutInflater.from(this).inflate(R.layout.swipe_recyclerview, null);
            dirayRV = diraySRL.findViewById(R.id.list);
            dirayRV.setAdapter(mAdapter);
            ArmsUtils.configRecyclerView(dirayRV, new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            initPaginate();
            views.add(diraySRL);
            viewpager.getAdapter().notifyDataSetChanged();
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
                share();
                break;
            case R.id.add_cart:
                mPresenter.addGoodsToCart();
                break;
            case R.id.buy:
                mPresenter.goOrderConfirm();
                break;
            case R.id.mask_pro:
            case R.id.promotion:
            case R.id.promotion_close:
                showPro();
                break;
            case R.id.spec:
            case R.id.mask_spec:
            case R.id.spec_close:
                showSpec();
                break;
            case R.id.collect:
                mPresenter.collectGoods(!collectV.isSelected());
                break;
        }
    }

    private void share() {
        Goods goods = response.getGoods();
        UMWeb web = new UMWeb(goods.getShareUrl());
        web.setTitle(goods.getName());//标题
        web.setDescription(goods.getTitle());
        web.setThumb(new UMImage(this, goods.getImage()));
        new ShareAction(this)
                .withMedia(web)
                .setCallback(shareListener)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .open();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void showSpec() {
        if (null == speceLabelsView.getLabels()
                || (null != speceLabelsView.getLabels() && speceLabelsView.getLabels().size() <= 0)
                || !ArmsUtils.isEmpty(getIntent().getStringExtra("where"))) {
            return;
        }
        if (!maskSpecV.isShown()) {
            speceLabelsView.setOnLabelSelectChangeListener(this);
            maskSpecV.setVisibility(View.VISIBLE);
            maskSpecV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.mask_in));
            specLayoutV.setVisibility(View.VISIBLE);
            specLayoutV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.for_butom_in));
            Goods goods = ((Goods) provideCache().get("goods"));
            spceIDTV.setText(goods.getMerchId());
            spceNameTV.setText(goods.getName());
            //itemView 的 Context 就是 Activity, Glide 会自动处理并和该 Activity 的生命周期绑定
            mImageLoader.loadImage(spceImageIV.getContext(),
                    ImageConfigImpl
                            .builder()
                            .placeholder(R.mipmap.place_holder_img)
                            .url(goods.getImage())
                            .imageView(spceImageIV)
                            .build());

        } else {
            speceLabelsView.setOnLabelSelectChangeListener(null);
            maskSpecV.setVisibility(View.GONE);
            maskSpecV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.mask_out));
            specLayoutV.setVisibility(View.GONE);
            specLayoutV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.for_buttom_out));
        }
    }

    private void showPro() {
        if (promotionAdapter.getInfos().size() <= 0
                || !ArmsUtils.isEmpty(getIntent().getStringExtra("where"))) {
            return;
        }
        if (!maskProV.isShown()) {
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
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(promotionCV);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
    }

    @Override
    public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
        if (isSelect) {
            GoodsSpecValue goodsSpecValue = (GoodsSpecValue) data;
            provideCache().put("specValueId", goodsSpecValue.getSpecValueId());
            provideCache().put("merchId", response.getGoods().getMerchId());
            goodSpecTV.setText(goodsSpecValue.getSpecValueName());
            mPresenter.getCoodsDetailsForSpecValueId();
        }
    }

    @Override
    public void onBackPressed() {
        if (maskProV.isShown() || maskSpecV.isShown()) {
            showSpec();
            showPro();
            return;
        }
        super.onBackPressed();
    }

    private class Mobile {
        @JavascriptInterface
        public void onGetWebContentHeight() {
            //重新调整webview高度
            detailWV.post(() -> {
                detailWV.measure(0, 0);
                int measuredHeight = detailWV.getMeasuredHeight();
                ViewPager.LayoutParams layoutParams = (ViewPager.LayoutParams) detailWV.getLayoutParams();
                layoutParams.height = measuredHeight;
                detailWV.setLayoutParams(layoutParams);

                LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) viewpager.getLayoutParams();
                layoutParams1.height = measuredHeight;
                viewpager.setLayoutParams(layoutParams1);
            });
        }
    }
}
