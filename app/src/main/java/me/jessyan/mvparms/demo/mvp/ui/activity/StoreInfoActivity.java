package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.paginate.Paginate;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerStoreInfoComponent;
import me.jessyan.mvparms.demo.di.module.StoreInfoModule;
import me.jessyan.mvparms.demo.mvp.contract.StoreInfoContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.Store;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.ActivityInfo;
import me.jessyan.mvparms.demo.mvp.presenter.StoreInfoPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HospitalEnvImageAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.CarouselView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class StoreInfoActivity extends BaseActivity<StoreInfoPresenter> implements StoreInfoContract.View, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.hot_img)
    ImageView hot_img;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.follow_layout)
    View followLayout;
    @BindView(R.id.follow)
    View followV;
    @BindView(R.id.ad)
    View adV;
    @BindView(R.id.infos)
    CarouselView carouselView;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    GoodsListAdapter storeGoodsListAdapter;
    @Inject
    HospitalEnvImageAdapter hospitalEnvImageAdapter;

    private View[] views = new View[3];
    private String[] titles = new String[]{
            "店铺介绍",
            "店铺项目",
            "店铺环境"
    };
    // 第一个页面
    private WebView stroeInfoWV;
    // 第二个页面
    private RecyclerView goodsList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;
    // 第三个页面
    private RecyclerView envList;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerStoreInfoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .storeInfoModule(new StoreInfoModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_store_info; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        title.setText(getIntent().getStringExtra("store_name"));
        back.setOnClickListener(this);
        followLayout.setOnClickListener(this);
        initViewPager();
        tab.setupWithViewPager(viewpager);
        provideCache().put("store_id", getIntent().getStringExtra("store_id"));
        provideCache().put("store_name", getIntent().getStringExtra("store_name"));
    }

    private void initViewPager() {
        // 初始化第一个页面
        stroeInfoWV = new WebView(this);
        stroeInfoWV.getSettings().setUseWideViewPort(true);
        stroeInfoWV.getSettings().setLoadWithOverviewMode(true);
        views[0] = stroeInfoWV;

        // 初始化第二个页面
        swipeRefreshLayout = (SwipeRefreshLayout) LayoutInflater.from(this).inflate(R.layout.swipe_recyclerview, null);
        goodsList = swipeRefreshLayout.findViewById(R.id.list);
        goodsList.setAdapter(storeGoodsListAdapter);
        storeGoodsListAdapter.setOnItemClickListener(this);
        LinearLayoutManager goodsListLayoutManager = new LinearLayoutManager(this);
        ArmsUtils.configRecyclerView(goodsList, goodsListLayoutManager);
        views[1] = swipeRefreshLayout;
        swipeRefreshLayout.setOnRefreshListener(this);
        initPaginate();

        // 初始化第三个页面
        envList = new RecyclerView(this);
        GridLayoutManager envLayoutManager = new GridLayoutManager(this, 2);
        ArmsUtils.configRecyclerView(envList, envLayoutManager);
        envList.setAdapter(hospitalEnvImageAdapter);
        envList.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int margin = ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.space_10);
                if (parent.getChildLayoutPosition(view) % 2 == 0) {
                    outRect.set(0, 0, margin, margin);
                } else {
                    outRect.set(0, 0, 0, margin);
                }
            }
        });
        views[2] = envList;


        // 初始化viewPager
        viewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.length;
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
                View view = views[position];
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        });
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
    public void updateStoreInfo(Store store) {
        WebView webView = (WebView) views[0];
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl(store.getIntro());
        mImageLoader.loadImage(this,
                ImageConfigImpl
                        .builder()
                        .placeholder(R.drawable.place_holder_img)
                        .url(store.getImage())
                        .isCenterCrop(true)
                        .imageView(hot_img)
                        .build());
        followV.setSelected("1".equals(store.getIsFollow()) ? true : false);
    }

    @Override
    public void updateActivityInfo(List<ActivityInfo> activityInfoList) {
        if (null == activityInfoList || (activityInfoList != null && activityInfoList.size() <= 0)) {
            adV.setVisibility(View.GONE);
        } else {
            carouselView.removeAllViews();
            carouselView.addView(R.layout.carouse_view);
            carouselView.upDataListAndView(activityInfoList, 2000);
            carouselView.startLooping();
            carouselView.setOnClickListener(new CarouselView.OnClickItemListener() {
                @Override
                public void onClick(int position) {
                    Intent intent = new Intent(getApplication(), ActivityInfoActivity.class);
                    intent.putExtra("activityId", activityInfoList.get(position).getActivityId());
                    intent.putExtra("isFromStore", true);
                    intent.putExtra("title", activityInfoList.get(position).getTitle());
                    intent.putExtra("store_id", getIntent().getStringExtra("store_id"));
                    ArmsUtils.startActivity(intent);
                }
            });
        }
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
    public void updatefollowStatus(boolean follow) {
        followV.setSelected(follow);
    }

    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.getGoodsList(false);
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

            mPaginate = Paginate.with(goodsList, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
            mPaginate.setHasMoreDataToLoad(false);
        }
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
            case R.id.follow_layout:
                mPresenter.follow(!followV.isSelected());
                break;
        }
    }

    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {
        Goods goods = storeGoodsListAdapter.getInfos().get(position);
        Intent intent = new Intent(getActivity().getApplication(), KGoodsDetailsActivityActivity.class);
        intent.putExtra("type", goods.getType());
        intent.putExtra("goodsId", goods.getGoodsId());
        intent.putExtra("merchId", goods.getMerchId());
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void onRefresh() {
        mPresenter.getGoodsList(true);
    }
}
