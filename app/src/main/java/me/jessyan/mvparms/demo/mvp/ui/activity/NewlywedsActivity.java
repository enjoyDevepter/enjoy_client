package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerNewlywedsComponent;
import me.jessyan.mvparms.demo.di.module.NewlywedsModule;
import me.jessyan.mvparms.demo.mvp.contract.NewlywedsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Ad;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.presenter.NewlywedsPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsNewlyListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.GlideImageLoader;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class NewlywedsActivity extends BaseActivity<NewlywedsPresenter> implements NewlywedsContract.View, View.OnClickListener, GoodsNewlyListAdapter.OnChildItemClickLinstener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.content)
    RecyclerView mRecyclerView;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    GoodsNewlyListAdapter mAdapter;
    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerNewlywedsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .newlywedsModule(new NewlywedsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_newlyweds; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        titleTV.setText("新人专享");
        banner.setImageLoader(new GlideImageLoader());
        banner.setIndicatorGravity(BannerConfig.CENTER);
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0, ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.address_list_item_space)));
        mAdapter.setOnChildItemClickLinstener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        initPaginate();
    }


    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
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
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
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

    @Override
    public void updateUI(List<Ad> adList) {
        // 广告
        List<String> urls = new ArrayList<>();
        for (Ad ad : adList) {
            urls.add(ad.getImage());
        }
        banner.setImages(urls);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        banner.isAutoPlay(false);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Ad ad = adList.get(position);
            }
        });
    }

    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.getTimeLimitGoodsList(false);
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

            mPaginate = Paginate.with(mRecyclerView, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
            mPaginate.setHasMoreDataToLoad(false);
        }
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
    public void onChildItemClick(View v, GoodsNewlyListAdapter.ViewName viewname, int position) {
        switch (viewname) {
            case BUY:
                break;
            case ITEM:
                Goods goods = mAdapter.getInfos().get(position);
                if ("1".equals(goods.getType())) {
                    Intent intent = new Intent(getActivity().getApplication(), GoodsDetailsActivity.class);
                    intent.putExtra("where", "newpeople");
                    intent.putExtra("goodsId", goods.getGoodsId());
                    intent.putExtra("merchId", goods.getMerchId());
                    intent.putExtra("promotionId", goods.getPromotionId());
                    ArmsUtils.startActivity(intent);
                } else if ("2".equals(goods.getType())) {
                    Intent intent = new Intent(getActivity().getApplication(), KGoodsDetailsActivityActivity.class);
                    intent.putExtra("where", "newpeople");
                    intent.putExtra("goodsId", goods.getGoodsId());
                    intent.putExtra("merchId", goods.getMerchId());
                    intent.putExtra("promotionId", goods.getPromotionId());
                    ArmsUtils.startActivity(intent);

                } else if ("3".equals(goods.getType())) {
                    Intent intent = new Intent(getActivity().getApplication(), HGoodsDetailsActivity.class);
                    intent.putExtra("where", "newpeople");
                    intent.putExtra("goodsId", goods.getGoodsId());
                    intent.putExtra("merchId", goods.getMerchId());
                    intent.putExtra("promotionId", goods.getPromotionId());
                    ArmsUtils.startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onRefresh() {
        mPresenter.getTimeLimitGoodsList(true);
    }

}
