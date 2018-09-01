package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.chaychan.library.BottomBarLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerMainComponent;
import me.jessyan.mvparms.demo.di.module.MainModule;
import me.jessyan.mvparms.demo.mvp.contract.MainContract;
import me.jessyan.mvparms.demo.mvp.presenter.MainPresenter;
import me.jessyan.mvparms.demo.mvp.ui.fragment.AppointmentFragment;
import me.jessyan.mvparms.demo.mvp.ui.fragment.DiscoverFragment;
import me.jessyan.mvparms.demo.mvp.ui.fragment.HomeFragment;
import me.jessyan.mvparms.demo.mvp.ui.fragment.MallFragment;
import me.jessyan.mvparms.demo.mvp.ui.fragment.MyFragment;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View, ViewPager.OnPageChangeListener, LocationListener, View.OnClickListener {

    @BindView(R.id.ll_home)
    View homeV;
    @BindView(R.id.ll_mall)
    View mallV;
    @BindView(R.id.ll_found)
    View foundV;
    @BindView(R.id.ll_appointment)
    View appointmentV;
    @BindView(R.id.ll_my)
    View myV;
    @BindView(R.id.bbl)
    BottomBarLayout bottomBarLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @Inject
    RxPermissions mRxPermissions;

    private List<Fragment> mFragmentList = new ArrayList<>();
    private boolean hasChange;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_main; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initFragment();
        viewPager.setAdapter(new FragmentStatePagerAdapter((getSupportFragmentManager())) {
            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return "";
            }

            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

        });
        viewPager.addOnPageChangeListener(this);
        bottomBarLayout.setViewPager(viewPager);
        homeV.setOnClickListener(this);
        mallV.setOnClickListener(this);
        foundV.setOnClickListener(this);
        appointmentV.setOnClickListener(this);
        myV.setOnClickListener(this);
        viewPager.setCurrentItem(0);
    }

    private void initFragment() {
        mFragmentList.add(HomeFragment.newInstance());
        mFragmentList.add(MallFragment.newInstance());
        mFragmentList.add(DiscoverFragment.newInstance());
        mFragmentList.add(AppointmentFragment.newInstance());
        mFragmentList.add(MyFragment.newInstance());
    }

    @Subscriber(tag = EventBusTags.CHANGE_MAIN_INDEX)
    public void updateIndex(int index) {
        viewPager.setCurrentItem(1);
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Cache<String, Object> appCache = ArmsUtils.obtainAppComponentFromContext(getApplication()).extras();
        if (position == 2 && positionOffset > 0 && ArmsUtils.isEmpty((String) appCache.get(KEY_KEEP + "token"))) {
            viewPager.setCurrentItem(2);
        }
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
                viewPager.setCurrentItem(0);
                break;
            case R.id.ll_mall:
                viewPager.setCurrentItem(1);
                break;
            case R.id.ll_found:
                viewPager.setCurrentItem(2);
                break;
            case R.id.ll_appointment:
                Cache<String, Object> appCache = ArmsUtils.obtainAppComponentFromContext(getApplication()).extras();
                if (ArmsUtils.isEmpty((String) appCache.get(KEY_KEEP + "token"))) {
                    ArmsUtils.startActivity(LoginActivity.class);
                    return;
                }
                viewPager.setCurrentItem(3);
                break;
            case R.id.ll_my:
                Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(getApplication()).extras();
                if (ArmsUtils.isEmpty((String) cache.get(KEY_KEEP + "token"))) {
                    ArmsUtils.startActivity(LoginActivity.class);
                    return;
                }
                viewPager.setCurrentItem(4);
                break;
        }
    }
}
