package me.jessyan.mvparms.demo.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
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
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

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
import me.jessyan.mvparms.demo.mvp.model.entity.GoodSummary;
import me.jessyan.mvparms.demo.mvp.model.entity.Module;
import me.jessyan.mvparms.demo.mvp.model.entity.NaviInfo;
import me.jessyan.mvparms.demo.mvp.presenter.HomePresenter;
import me.jessyan.mvparms.demo.mvp.ui.activity.CityActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.SearchActivity;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HomeArticleAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HomeGoodsAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.GlideImageLoader;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View, View.OnClickListener {

    @BindView(R.id.message)
    View messageV;
    @BindView(R.id.city)
    View cityV;
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
    @Inject
    RxPermissions mRxPermissions;

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

        banner.setImageLoader(new GlideImageLoader());
        banner.setIndicatorGravity(BannerConfig.CENTER);

        mPresenter.updateHomeInfo();

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
                showMessage(tab.getText().toString());
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
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });

        // 模块
        for (Module module : moduleList) {
            View moduleV = LayoutInflater.from(getActivity()).inflate(R.layout.home_module, new FrameLayout(getActivity()));
            RecyclerView moduleRV = moduleV.findViewById(R.id.moduleRV);
            ((TextView) moduleV.findViewById(R.id.module_name)).setText(module.getName());
            if (!"限时秒杀".equals(module.getName())) {
                moduleV.findViewById(R.id.module_info).setVisibility(View.GONE);
            }
            if ("goods".equals(module.getType())) {
                List<GoodSummary> goods = new Gson().fromJson(module.getBody(), new TypeToken<List<GoodSummary>>() {
                }.getType());
                moduleRV.addItemDecoration(new SpacesItemDecoration(ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.home_module_style_margin_left), 0));
                moduleRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                moduleRV.setAdapter(new HomeGoodsAdapter(goods));
            } else if ("article".equals(module.getType())) {
                List<Article> articles = new Gson().fromJson(module.getBody(), new TypeToken<List<Article>>() {
                }.getType());
                RecyclerView articleRV = new RecyclerView(getActivity());
                moduleRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                moduleRV.setAdapter(new HomeArticleAdapter(articles));
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
                showMessage(tab.getText().toString());
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
        mPresenter.updateHomeInfo();
    }

    @Subscriber(tag = EventBusTags.LOGIN_STATUS_CHANGE_EVENT)
    private void refreshHomeInfo() {
        mPresenter.updateHomeInfo();
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message:
                break;
            case R.id.city:
                ArmsUtils.startActivity(CityActivity.class);
                break;
            case R.id.search:
                ArmsUtils.startActivity(SearchActivity.class);
                break;
        }
    }


}
