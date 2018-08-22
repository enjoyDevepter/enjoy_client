package me.jessyan.mvparms.demo.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerHomeComponent;
import me.jessyan.mvparms.demo.di.module.HomeModule;
import me.jessyan.mvparms.demo.mvp.contract.HomeContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Ad;
import me.jessyan.mvparms.demo.mvp.model.entity.Area;
import me.jessyan.mvparms.demo.mvp.model.entity.Article;
import me.jessyan.mvparms.demo.mvp.model.entity.Diary;
import me.jessyan.mvparms.demo.mvp.model.entity.GoodSummary;
import me.jessyan.mvparms.demo.mvp.model.entity.Module;
import me.jessyan.mvparms.demo.mvp.model.entity.NaviInfo;
import me.jessyan.mvparms.demo.mvp.presenter.HomePresenter;
import me.jessyan.mvparms.demo.mvp.ui.activity.AddressListActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.CityActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.DiaryForGoodsActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.GoodsDetailsActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.HGoodsDetailsActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.NewlywedsActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.SearchActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.TaoCanActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.TaoCanDetailsActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.TimelimitActivity;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DiaryListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HomeArticleAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HomeGoodsAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.GlideImageLoader;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, DiaryListAdapter.OnChildItemClickLinstener, DefaultAdapter.OnRecyclerViewItemClickListener, OnBannerListener {

    @BindView(R.id.message)
    View messageV;
    @BindView(R.id.city)
    TextView cityV;
    @BindView(R.id.search)
    View serachV;
    @BindView(R.id.tabOne)
    TabLayout tabLayout;
    @BindView(R.id.tabTwo)
    TabLayout tabLayoutTwo;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.module_layout)
    LinearLayout moduleLayout;
    @BindView(R.id.recommend)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Inject
    RxPermissions mRxPermissions;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    DiaryListAdapter mAdapter;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerHomeComponent
                .builder()
                .appComponent(appComponent)
                .homeModule(new HomeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        cityV.setOnClickListener(this);
        messageV.setOnClickListener(this);
        serachV.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        banner.setImageLoader(new GlideImageLoader());
        banner.setIndicatorGravity(BannerConfig.CENTER);
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0, ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.address_list_item_space)));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnChildItemClickLinstener(this);
        initPaginate();
        mPresenter.updateHomeInfo();
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
    public Cache getCache() {
        return provideCache();
    }

    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.getRecommenDiaryList();
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
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void refreshUI(List<NaviInfo> firstNavList, List<Ad> ads, List<Module> moduleList, List<NaviInfo> secondNavList) {

        // 一级导航
        tabLayout.removeAllTabs();
        for (NaviInfo naviInfo : firstNavList) {
            tabLayout.addTab(tabLayout.newTab().setText(naviInfo.getTitle()));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        break;
                    case 1:
                        ArmsUtils.startActivity(TaoCanActivity.class);
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // 广告
        List<String> urls = new ArrayList<>();
        for (Ad ad : ads) {
            urls.add(ad.getImage());
        }
        banner.setImages(urls);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        banner.isAutoPlay(false);
        banner.setOnBannerListener(this);

        moduleLayout.removeAllViews();
        // 模块
        for (Module module : moduleList) {
            View moduleV = LayoutInflater.from(getActivity()).inflate(R.layout.home_module, new FrameLayout(getActivity()));
            RecyclerView moduleRV = moduleV.findViewById(R.id.moduleRV);
            ((TextView) moduleV.findViewById(R.id.module_name)).setText(module.getName());
            if ("平台介绍".equals(module.getName())) {
                moduleV.findViewById(R.id.module_info).setVisibility(View.GONE);
            }
            if ("新人专区".equals(module.getName())) {
                try {
                    if (!ArmsUtils.isEmpty(module.getExtendParam())) {
                        ((TextView) moduleV.findViewById(R.id.moduleinfo)).setText("距离活动结束还有" + new JSONObject(module.getExtendParam()).optString("remainingDays") + "天");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            moduleV.findViewById(R.id.module_info).setTag(module.getRedirectType());
            moduleV.findViewById(R.id.module_info).setOnClickListener(this);

            if ("goods".equals(module.getType())) {
                List<GoodSummary> goods = new Gson().fromJson(module.getBody(), new TypeToken<List<GoodSummary>>() {
                }.getType());
                moduleRV.addItemDecoration(new SpacesItemDecoration(ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.home_module_style_margin_left), 0));
                moduleRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                HomeGoodsAdapter homeGoodsAdapter = new HomeGoodsAdapter(goods);
                homeGoodsAdapter.setOnItemClickListener(this);
                moduleRV.setAdapter(homeGoodsAdapter);
            } else if ("article".equals(module.getType())) {
                List<Article> articles = new Gson().fromJson(module.getBody(), new TypeToken<List<Article>>() {
                }.getType());
                RecyclerView articleRV = new RecyclerView(getActivity());
                moduleRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                HomeArticleAdapter homeArticleAdapter = new HomeArticleAdapter(articles);
                homeArticleAdapter.setOnItemClickListener(this);
                moduleRV.setAdapter(homeArticleAdapter);
                moduleLayout.addView(articleRV);
            }
            moduleLayout.addView(moduleV);
        }

        // 二级导航
        tabLayoutTwo.removeAllTabs();
        for (NaviInfo naviInfo : secondNavList) {
            tabLayoutTwo.addTab(tabLayoutTwo.newTab().setText(naviInfo.getTitle()));
        }
        tabLayoutTwo.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 1:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Subscriber(tag = EventBusTags.CITY_CHANGE_EVENT)
    private void updateUserWithMode(Area area) {
        cityV.setText(area.getName());
        mPresenter.updateHomeInfo();
    }

    @Subscriber(tag = EventBusTags.LOGIN_STATUS_CHANGE_EVENT)
    private void refreshHomeInfo() {
        mPresenter.updateHomeInfo();
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message:
                ArmsUtils.startActivity(AddressListActivity.class);
                break;
            case R.id.city:
                ArmsUtils.startActivity(CityActivity.class);
                break;
            case R.id.search:
                ArmsUtils.startActivity(SearchActivity.class);
                break;
            case R.id.module_info:
                if ("combodetail".equals(v.getTag())) {
                    ArmsUtils.startActivity(getActivity(), TaoCanActivity.class);
                } else if ("timelimitdetail".equals(v.getTag())) {
                    ArmsUtils.startActivity(getActivity(), TimelimitActivity.class);
                } else if ("newpeople".equals(v.getTag())) {
                    ArmsUtils.startActivity(getActivity(), NewlywedsActivity.class);
                }
                break;
        }
    }


    @Override
    public void onRefresh() {
        mPresenter.updateHomeInfo();
    }

    @Override
    public void onChildItemClick(View v, DiaryListAdapter.ViewName viewname, int position) {
        Diary diary = mAdapter.getInfos().get(position);
        switch (viewname) {
            case FLLOW:
                provideCache().put("memberId", diary.getMember().getMemberId());
                mPresenter.follow("1".equals(diary.getMember().getIsFollow()) ? false : true, position);
                break;
            case LEFT_IMAGE:
                break;
            case RIGHT_IMAGE:
                break;
            case VOTE:
                provideCache().put("diaryId", diary.getDiaryId());
                mPresenter.vote("1".equals(diary.getIsPraise()) ? false : true, position);
                break;
            case ITEM:
                Intent intent = new Intent(getActivity().getApplication(), DiaryForGoodsActivity.class);
                intent.putExtra("diaryId", diary.getDiaryId());
                intent.putExtra("goodsId", diary.getGoods().getGoodsId());
                intent.putExtra("merchId", diary.getGoods().getMerchId());
                intent.putExtra("memberId", diary.getMember().getMemberId());
                ArmsUtils.startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {
        switch (viewType) {
            case R.layout.home_article_item: // 文章
                Article article = (Article) data;
                showMessage(article.getTitle());
                break;
            default:
                GoodSummary good = (GoodSummary) data;
                if ("newpeople".equals(good.getRedirectType())) {
                    if ("1".equals(good.getType())) {
                        Intent intent = new Intent(getActivity().getApplication(), GoodsDetailsActivity.class);
                        intent.putExtra("where", "newpeople");
                        intent.putExtra("goodsId", good.getGoodsId());
                        intent.putExtra("merchId", good.getMerchId());
                        intent.putExtra("promotionId", good.getPromotionId());
                        ArmsUtils.startActivity(intent);
                    } else if ("2".equals(good.getType())) {

                    } else if ("3".equals(good.getType())) {
                        Intent intent = new Intent(getActivity().getApplication(), HGoodsDetailsActivity.class);
                        intent.putExtra("where", "newpeople");
                        intent.putExtra("goodsId", good.getGoodsId());
                        intent.putExtra("merchId", good.getMerchId());
                        intent.putExtra("promotionId", good.getPromotionId());
                        ArmsUtils.startActivity(intent);
                    }
                } else if ("combodetail".equals(good.getRedirectType())) {
                    // 套餐详情界面
                    Intent intent = new Intent(getActivity(), TaoCanDetailsActivity.class);
                    intent.putExtra("setMealId", good.getGoodsId());
                    ArmsUtils.startActivity(intent);

                } else if ("timelimitdetail".equals(good.getRedirectType())) {
                    if ("1".equals(good.getType())) {
                        Intent intent = new Intent(getActivity().getApplication(), GoodsDetailsActivity.class);
                        intent.putExtra("where", "timelimitdetail");
                        intent.putExtra("goodsId", good.getGoodsId());
                        intent.putExtra("merchId", good.getMerchId());
                        intent.putExtra("promotionId", good.getPromotionId());
                        ArmsUtils.startActivity(intent);

                    } else if ("2".equals(good.getType())) {

                    } else if ("3".equals(good.getType())) {
                        Intent intent = new Intent(getActivity().getApplication(), HGoodsDetailsActivity.class);
                        intent.putExtra("where", "timelimitdetail");
                        intent.putExtra("goodsId", good.getGoodsId());
                        intent.putExtra("merchId", good.getMerchId());
                        intent.putExtra("promotionId", good.getPromotionId());
                        ArmsUtils.startActivity(intent);
                    }
                }
                break;
        }
    }

    @Override
    public void OnBannerClick(int position) {

    }
}
