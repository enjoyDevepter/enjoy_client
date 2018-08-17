package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
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
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerHGoodsDetailsComponent;
import me.jessyan.mvparms.demo.di.module.HGoodsDetailsModule;
import me.jessyan.mvparms.demo.mvp.contract.HGoodsDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.GoodsSpecValue;
import me.jessyan.mvparms.demo.mvp.model.entity.Promotion;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HGoodsDetailsResponse;
import me.jessyan.mvparms.demo.mvp.presenter.HGoodsDetailsPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsPromotionAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.GlideImageLoader;

import static com.jess.arms.utils.ArmsUtils.getContext;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class HGoodsDetailsActivity extends BaseActivity<HGoodsDetailsPresenter> implements HGoodsDetailsContract.View, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener, TagFlowLayout.OnSelectListener {

    @BindView(R.id.back)
    View backV;
    @BindView(R.id.share)
    View cartV;
    @BindView(R.id.buy)
    View buyV;
    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.detail)
    WebView detailWV;
    @BindView(R.id.goods_iamges)
    Banner imagesB;
    @BindView(R.id.image_count)
    TextView imageCountTV;
    @BindView(R.id.tailMoney_buttom)
    TextView tailMoneyButtomTV;
    @BindView(R.id.deposit_buttom)
    TextView depositButtomTV;
    @BindView(R.id.deposit)
    TextView depositTV;
    @BindView(R.id.tailMoney)
    TextView tailMoneyTV;
    @BindView(R.id.isFavorite)
    View isFavoriteV;
    @BindView(R.id.saleCount)
    TextView saleCountTV;
    @BindView(R.id.name)
    TextView nameTV;
    @BindView(R.id.mask_pro)
    View maskProV;
    @BindView(R.id.promotion_layout)
    View proLayoutV;
    @BindView(R.id.promotion)
    View promotionV;
    @BindView(R.id.promotion_info)
    View promotionInfoV;
    @BindView(R.id.promotion_name)
    TextView promotionNameTV;
    @BindView(R.id.mask_spec)
    View maskSpecV;
    @BindView(R.id.spec_layout)
    View specLayoutV;
    @BindView(R.id.spec)
    View specV;
    @BindView(R.id.spec_image)
    ImageView spceImageIV;
    @BindView(R.id.spec_name)
    TextView spceNameTV;
    @BindView(R.id.spec_price)
    TextView spcePriceTV;
    @BindView(R.id.spec_goods_id)
    TextView spceIDTV;
    @BindView(R.id.goods_spec)
    TextView goodsSpecTV;
    @BindView(R.id.promotion_close)
    View promotionCloseV;
    @BindView(R.id.spec_close)
    View spceCloseV;
    @BindView(R.id.promotionCV)
    RecyclerView promotionCV;
    @BindView(R.id.specs)
    TagFlowLayout speceflowLayout;
    @Inject
    TagAdapter adapter;
    @Inject
    GoodsPromotionAdapter promotionAdapter;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;

    HGoodsDetailsResponse response;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerHGoodsDetailsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .hGoodsDetailsModule(new HGoodsDetailsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_hgoods_details; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        promotionV.setOnClickListener(this);
        specV.setOnClickListener(this);
        imagesB.setImageLoader(new GlideImageLoader());
        imagesB.setIndicatorGravity(BannerConfig.CENTER);
        tabLayout.addTab(tabLayout.newTab().setText("商品详情"));
        tabLayout.addTab(tabLayout.newTab().setText("相关日志"));

        maskProV.setOnClickListener(this);
        maskSpecV.setOnClickListener(this);
        promotionCloseV.setOnClickListener(this);
        spceCloseV.setOnClickListener(this);
        isFavoriteV.setOnClickListener(this);
        buyV.setOnClickListener(this);

        ArmsUtils.configRecyclerView(promotionCV, mLayoutManager);
        promotionCV.setAdapter(promotionAdapter);
        promotionAdapter.setOnItemClickListener(this);
        speceflowLayout.setAdapter(adapter);
        speceflowLayout.setOnSelectListener(this);
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
    public Cache getCache() {
        return provideCache();
    }

    @Override
    public void updateUI(HGoodsDetailsResponse response) {
        this.response = response;
        imagesB.setImages(response.getImages());
        //banner设置方法全部调用完毕时最后调用
        imagesB.start();
        imagesB.isAutoPlay(false);

        imageCountTV.setText("1/" + response.getImages().size());
        nameTV.setText(response.getGoods().getName());

        depositTV.setText(String.valueOf(response.getGoods().getDeposit()));
        tailMoneyTV.setText(String.valueOf(response.getGoods().getTailMoney()));
        isFavoriteV.setSelected("1".equals(response.getGoods().getIsFavorite()) ? true : false);
        saleCountTV.setText(String.valueOf(response.getGoods().getSales()));
        detailWV.loadData(response.getGoods().getMobileDetail(), "text/html", null);
        tailMoneyButtomTV.setText(String.valueOf(response.getGoods().getTailMoney()));
        depositButtomTV.setText(String.valueOf(response.getGoods().getDeposit()));

        String specValueId = (String) provideCache().get("specValueId");
        if (!ArmsUtils.isEmpty(specValueId)) {
            for (int i = 0; i < response.getGoodsSpecValueList().size(); i++) {
                if (response.getGoodsSpecValueList().get(i).getSpecValueId().equals(specValueId)) {
                    adapter.setSelectedList(i);
                    goodsSpecTV.setText(response.getGoodsSpecValueList().get(i).getSpecValueName());
                    return;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.promotion_close:
            case R.id.promotion:
            case R.id.mask_pro:
                showPro();
                break;
            case R.id.mask_spec:
            case R.id.spec:
            case R.id.spec_close:
                showSpec();
                break;
            case R.id.isFavorite: // 缺接口
                break;
            case R.id.buy:
                mPresenter.goOrderConfirm();
                break;
        }
    }

    private void showPro() {
        if (promotionAdapter.getInfos().size() <= 0) {
            return;
        }
        if (!maskProV.isShown()) {
            maskProV.setVisibility(View.VISIBLE);
            maskProV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.mask_in));
            proLayoutV.setVisibility(View.VISIBLE);
            proLayoutV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.for_butom_in));
        } else {
            maskProV.setVisibility(View.GONE);
            maskProV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.mask_out));
            proLayoutV.setVisibility(View.GONE);
            proLayoutV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.for_buttom_out));
        }
    }


    private void showSpec() {
        if (adapter.getCount() <= 0) {
            return;
        }
        if (!maskSpecV.isShown()) {
            maskSpecV.setVisibility(View.VISIBLE);
            maskSpecV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.mask_in));
            specLayoutV.setVisibility(View.VISIBLE);
            specLayoutV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.for_butom_in));
            spceIDTV.setText(response.getGoods().getAdvanceDepositId());
            spcePriceTV.setText(String.valueOf(response.getGoods().getMarketPrice()));
            spceNameTV.setText(response.getGoods().getName());

            //itemView 的 Context 就是 Activity, Glide 会自动处理并和该 Activity 的生命周期绑定
            mImageLoader.loadImage(spceImageIV.getContext(),
                    ImageConfigImpl
                            .builder()
                            .url(response.getGoods().getImage())
                            .imageView(spceImageIV)
                            .build());

        } else {
            maskSpecV.setVisibility(View.GONE);
            maskSpecV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.mask_out));
            specLayoutV.setVisibility(View.GONE);
            specLayoutV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.for_buttom_out));
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
                        promotionInfoV.setVisibility(p.isCheck() ? View.VISIBLE : View.INVISIBLE);
                        promotionNameTV.setText(p.isCheck() ? promotionList.get(position).getTitle() : "");
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
            goodsSpecTV.setText(goodsSpecValue.getSpecValueName());
            mPresenter.getHCoodsDetailsForSpecValueId();
        } else {
            goodsSpecTV.setText("");
            provideCache().put("specValueId", "");
        }
    }
}