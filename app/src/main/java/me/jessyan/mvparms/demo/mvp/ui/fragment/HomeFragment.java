package me.jessyan.mvparms.demo.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
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
import me.jessyan.mvparms.demo.app.utils.SPUtils;
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
import me.jessyan.mvparms.demo.mvp.ui.activity.CityActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.DiaryDetailsActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.DiaryForGoodsActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.DoctorActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.GoodsDetailsActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.HGoodsDetailsActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.HospitalActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.HospitalInfoActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.LoginActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.MessageActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.NewlywedsActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.PlatformActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.RecommendActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.SearchActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.SearchResultActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.TaoCanActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.TaoCanDetailsActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.TimelimitActivity;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DiaryListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HomeArticleAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HomeGoodsAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.GlideImageLoader;
import me.jessyan.mvparms.demo.mvp.ui.widget.HiNestedScrollView;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static android.support.v7.widget.RecyclerView.LayoutManager;
import static android.support.v7.widget.RecyclerView.OnClickListener;
import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;
import static com.jess.arms.utils.Preconditions.checkNotNull;
import static me.jessyan.mvparms.demo.mvp.ui.activity.HospitalInfoActivity.KEY_FOR_HOSPITAL_ID;
import static me.jessyan.mvparms.demo.mvp.ui.activity.HospitalInfoActivity.KEY_FOR_HOSPITAL_NAME;


public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View, OnClickListener, SwipeRefreshLayout.OnRefreshListener, DiaryListAdapter.OnChildItemClickLinstener, DefaultAdapter.OnRecyclerViewItemClickListener, TabLayout.OnTabSelectedListener, NestedScrollView.OnScrollChangeListener {
    @BindView(R.id.title_layout)
    View titleV;
    @BindView(R.id.message)
    View messageV;
    @BindView(R.id.city)
    TextView cityV;
    @BindView(R.id.search)
    View serachV;
    @BindView(R.id.tabOne)
    TabLayout tabLayout;
    @BindView(R.id.tabOneFloat)
    TabLayout tabOneFloat;
    @BindView(R.id.tabTwo)
    TabLayout tabTwoLayout;
    @BindView(R.id.tabTwoFloat)
    TabLayout tabTwoFloat;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.module_layout)
    LinearLayout moduleLayout;
    @BindView(R.id.recommend)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.nestedScrollView)
    HiNestedScrollView nestedScrollView;
    @Inject
    RxPermissions mRxPermissions;
    @Inject
    LayoutManager mLayoutManager;
    @Inject
    DiaryListAdapter mAdapter;
    private List<NaviInfo> firstNavList;
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
        if (ArmsUtils.isEmpty(SPUtils.get("countyName", ""))) {
            cityV.setText("北京");
        } else {
            cityV.setText(SPUtils.get("countyName", ""));
        }

        banner.setImageLoader(new GlideImageLoader());
        banner.setIndicatorGravity(BannerConfig.CENTER);
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0, ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.address_list_item_space)));
        mAdapter.setOnChildItemClickLinstener(this);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (firstCompletelyVisibleItemPosition == 0) {
                    nestedScrollView.setNeedScroll(true);
                }
            }
        });
        mRecyclerView.setNestedScrollingEnabled(false);
        nestedScrollView.setOnScrollChangeListener(this);
        mPresenter.updateHomeInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != tabLayout.getTabAt(0)) {
            tabLayout.getTabAt(0).select();
        }
        if (null != tabOneFloat.getTabAt(0)) {
            tabOneFloat.getTabAt(0).select();
        }
        if (null != tabTwoLayout.getTabAt(0)) {
            tabTwoLayout.getTabAt(0).select();
        }
        if (null != tabTwoFloat.getTabAt(0)) {
            tabTwoFloat.getTabAt(0).select();
        }
    }

    @Override
    public void setLoadedAllItems(boolean has) {
        this.hasLoadedAllItems = has;
    }

    @Override
    public Cache getCache() {
        return provideCache();
    }


    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void refreshUI(List<NaviInfo> firstNavList, List<Ad> ads, List<Module> moduleList, List<NaviInfo> secondNavList) {

        // 一级导航
        this.firstNavList = firstNavList;
        tabLayout.removeAllTabs();
        tabOneFloat.removeAllTabs();
        tabLayout.addOnTabSelectedListener(this);
        tabOneFloat.addOnTabSelectedListener(this);
        for (NaviInfo naviInfo : firstNavList) {
            tabLayout.addTab(tabLayout.newTab().setTag(naviInfo.getRedirectType()).setText(naviInfo.getTitle()));
        }
        for (NaviInfo naviInfo : firstNavList) {
            tabOneFloat.addTab(tabOneFloat.newTab().setTag(naviInfo.getRedirectType()).setText(naviInfo.getTitle()));
        }

        // 广告
        List<String> urls = new ArrayList<>();
        for (Ad ad : ads) {
            urls.add(ad.getImage());
        }
        banner.setImages(urls);
        //banner设置方法全部调用完毕时最后调用
        banner.isAutoPlay(true);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Ad ad = ads.get(position);
                String redirectType = ads.get(position).getRedirectType();
                try {
                    JSONObject object = new JSONObject(ads.get(position).getExtendParam());
                    if ("project".equals(redirectType)) {
                        if ("1".equals(object.optString("type"))) {
                            Intent intent = new Intent(getActivity().getApplication(), GoodsDetailsActivity.class);
                            intent.putExtra("type", object.optString("type"));
                            intent.putExtra("goodsId", object.optString("goodsId"));
                            intent.putExtra("merchId", object.optString("merchId"));
                            ArmsUtils.startActivity(intent);
                        } else if ("2".equals(object.optString("type"))) {
                            // fixme 暂无功能

                        } else if ("3".equals(object.optString("type"))) {
                            Intent hGoodsintent = new Intent(getActivity().getApplication(), HGoodsDetailsActivity.class);
                            hGoodsintent.putExtra("goodsId", object.optString("goodsId"));
                            hGoodsintent.putExtra("merchId", object.optString("merchId"));
                            hGoodsintent.putExtra("advanceDepositId", object.optString("advanceDepositId"));
                            ArmsUtils.startActivity(hGoodsintent);
                        }
                    } else if ("hospital".equals(redirectType)) {
                        Intent hospitalIntent = new Intent(getActivity(), HospitalInfoActivity.class);
                        hospitalIntent.putExtra(KEY_FOR_HOSPITAL_NAME, ad.getTitle());
                        hospitalIntent.putExtra(KEY_FOR_HOSPITAL_ID, object.optString("hospitalId"));
                        ArmsUtils.startActivity(hospitalIntent);

                    } else if ("article".equals(redirectType)) {
                        Intent articleIntent = new Intent(getContext(), PlatformActivity.class);
                        articleIntent.putExtra("url", object.optString("url"));
                        ArmsUtils.startActivity(articleIntent);
                    } else if ("store".equals(redirectType)) {
                        //fixme 暂无功能
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        banner.start();

        moduleLayout.removeAllViews();
        // 模块
        for (Module module : moduleList) {
            View moduleV = LayoutInflater.from(getActivity()).inflate(R.layout.home_module, new FrameLayout(getActivity()));
            RecyclerView moduleRV = moduleV.findViewById(R.id.moduleRV);
            ((TextView) moduleV.findViewById(R.id.module_name)).setText(module.getName());
            if ("平台介绍".equals(module.getName())) {
                moduleV.findViewById(R.id.title).setVisibility(View.GONE);
            }
            if ("新人专区".equals(module.getName())) {
                moduleV.findViewById(R.id.icon).setBackgroundResource(R.mipmap.main_new_peiple_icon);
                try {
                    if (!ArmsUtils.isEmpty(module.getExtendParam())) {
                        ((TextView) moduleV.findViewById(R.id.moduleinfo)).setText("距离活动结束还有" + new JSONObject(module.getExtendParam()).optString("remainingDays") + "天");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if ("限时秒杀".equals(module.getName())) {
                moduleV.findViewById(R.id.icon).setBackgroundResource(R.mipmap.main_miaosha_icon);
            }
            moduleV.findViewById(R.id.module_info).setTag(module.getRedirectType());
            moduleV.findViewById(R.id.module_info).setOnClickListener(this);

            if ("goods".equals(module.getType())) {
                List<GoodSummary> goods = new Gson().fromJson(module.getBody(), new TypeToken<List<GoodSummary>>() {
                }.getType());
                if (goods.size() <= 0) {
                    continue;
                }
                moduleRV.addItemDecoration(new SpacesItemDecoration(ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.home_module_style_margin_left), 0));
                moduleRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                HomeGoodsAdapter homeGoodsAdapter = new HomeGoodsAdapter(goods);
                homeGoodsAdapter.setOnItemClickListener(this);
                moduleRV.setAdapter(homeGoodsAdapter);
            } else if ("article".equals(module.getType())) {
                List<Article> articles = new Gson().fromJson(module.getBody(), new TypeToken<List<Article>>() {
                }.getType());
                if (articles.size() <= 0) {
                    continue;
                }
                RecyclerView articleRV = new RecyclerView(getActivity());
                articleRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                articleRV.addItemDecoration(new SpacesItemDecoration(ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.home_module_article_margin_left), 0));
                HomeArticleAdapter homeArticleAdapter = new HomeArticleAdapter(articles);
                homeArticleAdapter.setOnItemClickListener(this);
                articleRV.setAdapter(homeArticleAdapter);
                moduleLayout.addView(articleRV);
            }
            moduleLayout.addView(moduleV);
        }

        // 二级导航
        tabTwoLayout.removeAllTabs();
        tabTwoFloat.removeAllTabs();
        tabTwoLayout.addOnTabSelectedListener(this);
        tabTwoFloat.addOnTabSelectedListener(this);
        for (NaviInfo naviInfo : secondNavList) {
            tabTwoLayout.addTab(tabTwoLayout.newTab().setTag(naviInfo.getRedirectType()).setText(naviInfo.getTitle()));
            tabTwoFloat.addTab(tabTwoFloat.newTab().setTag(naviInfo.getRedirectType()).setText(naviInfo.getTitle()));
        }
    }

    @Override
    public void updateDiaryUI(int count) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mRecyclerView.getLayoutParams();
        int[] location = new int[2];
        titleV.getLocationInWindow(location);
        layoutParams.height = ArmsUtils.getDimens(getContext(), R.dimen.home_diary_item_height) * count + ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.address_list_item_space) * count + 1;
        mRecyclerView.setLayoutParams(layoutParams);
    }

    @Override
    public void setData(@Nullable Object data) {
    }

    @Subscriber(tag = EventBusTags.CITY_CHANGE_EVENT)
    private void updateUserWithMode(Area area) {
        cityV.setText(area.getName());
        mPresenter.updateHomeInfo();
    }


    @Subscriber(tag = EventBusTags.DIARY_COMMENT_SUCCESS)
    private void updateDirayComment(boolean success) {
        mPresenter.getRecommenDiaryList(true);
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (null != tabLayout && null != tabLayout.getTabAt(0)) {
                tabLayout.getTabAt(0).select();
            }
            if (null != tabOneFloat && null != tabOneFloat.getTabAt(0)) {
                tabOneFloat.getTabAt(0).select();
            }
            if (null != tabTwoLayout && null != tabTwoLayout.getTabAt(0)) {
                tabTwoLayout.getTabAt(0).select();
            }
            if (null != tabTwoFloat && null != tabTwoFloat.getTabAt(0)) {
                tabTwoFloat.getTabAt(0).select();
            }
        }
    }


    @Override
    public void killMyself() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message:
                Cache<String, Object> appCache = ArmsUtils.obtainAppComponentFromContext(getActivity()).extras();
                if (ArmsUtils.isEmpty((String) appCache.get(KEY_KEEP + "token"))) {
                    ArmsUtils.startActivity(LoginActivity.class);
                    return;
                }
                ArmsUtils.startActivity(MessageActivity.class);
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
            case VOTE:
                provideCache().put("diaryId", diary.getDiaryId());
                mPresenter.vote("1".equals(diary.getIsPraise()) ? false : true, position);
                break;
            case LEFT_IMAGE:
            case RIGHT_IMAGE:
            case ITEM:
                Intent intent = new Intent(getActivity().getApplication(), DiaryForGoodsActivity.class);
                intent.putExtra("diaryId", diary.getDiaryId());
                intent.putExtra("goodsId", diary.getGoods().getGoodsId());
                intent.putExtra("merchId", diary.getGoods().getMerchId());
                intent.putExtra("memberId", diary.getMember().getMemberId());
                ArmsUtils.startActivity(intent);
                break;
            case COMMENT:
                Intent diaryIntent = new Intent(getActivity().getApplication(), DiaryDetailsActivity.class);
                diaryIntent.putExtra("diaryId", diary.getDiaryId());
                ArmsUtils.startActivity(diaryIntent);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {
        switch (viewType) {
            case R.layout.home_article_item: // 文章
                Article article = (Article) data;
                Intent articleIntent = new Intent(getContext(), PlatformActivity.class);
                articleIntent.putExtra("url", article.getUrl());
                ArmsUtils.startActivity(articleIntent);
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
    public void onTabSelected(TabLayout.Tab tab) {
        String tag = (String) tab.getTag();
        if ("".equals(tab.getTag())) {
        } else if ("combo".equals(tag)) {
            ArmsUtils.startActivity(TaoCanActivity.class);
        } else if ("medicalcosmetology".equals(tag)) {
            Intent result = new Intent(getActivity(), SearchResultActivity.class);
            result.putExtra("busType", "3");
            result.putExtra("title", firstNavList.get(tab.getPosition()).getTitle());
            ArmsUtils.startActivity(result);
        } else if ("shop".equals(tag)) {
            Intent result = new Intent(getActivity(), SearchResultActivity.class);
            result.putExtra("busType", "1");
            result.putExtra("title", firstNavList.get(tab.getPosition()).getTitle());
            ArmsUtils.startActivity(result);
        } else if ("recom_project".equals(tag)) {
            Intent result = new Intent(getActivity(), RecommendActivity.class);
            result.putExtra("isGoods", false);
            ArmsUtils.startActivity(result);
        } else if ("recom_good".equals(tag)) {
            Intent result = new Intent(getActivity(), RecommendActivity.class);
            result.putExtra("isGoods", true);
            ArmsUtils.startActivity(result);
        } else if ("hospital".equals(tag)) {
            ArmsUtils.startActivity(getActivity(), HospitalActivity.class);
        } else if ("doctor".equals(tag)) {
            ArmsUtils.startActivity(getActivity(), DoctorActivity.class);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

        int[] location = new int[2];
        tabLayout.getLocationOnScreen(location);
        int yPosition = location[1];
        int[] titleLocation = new int[2];
        titleV.getLocationInWindow(titleLocation);
        if (yPosition < (titleV.getHeight() + titleLocation[1])) {
            tabOneFloat.setVisibility(View.VISIBLE);
        } else {
            tabOneFloat.setVisibility(View.GONE);
        }
        tabTwoLayout.getLocationOnScreen(location);
        yPosition = location[1];
        if (yPosition < (titleV.getHeight() + titleLocation[1])) {
            tabOneFloat.setVisibility(View.GONE);
            tabTwoFloat.setVisibility(View.VISIBLE);
        } else {
            tabTwoFloat.setVisibility(View.GONE);
        }
        if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
            if (!hasLoadedAllItems) {
                mPresenter.getRecommenDiaryList(false);
            }
        }
    }
}
