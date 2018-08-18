package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.chaychan.library.BottomBarItem;
import com.chaychan.library.BottomBarLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
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


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.bbl)
    BottomBarLayout bottomBarLayout;

    private List<Fragment> mFragmentList = new ArrayList<>();


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
    public void initData(Bundle savedInstanceState) {

        initFragment();
        bottomBarLayout.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(BottomBarItem bottomBarItem, int previousPosition, int currentPosition) {
                if (previousPosition == currentPosition) {
                    return;
                }
                if (currentPosition == 3 || currentPosition == 4) {
                    Cache<String, Object> appCache = ArmsUtils.obtainAppComponentFromContext(getApplication()).extras();
                    if (ArmsUtils.isEmpty((String) appCache.get(KEY_KEEP + "token"))) {
                        bottomBarLayout.setCurrentItem(previousPosition);
                        ArmsUtils.startActivity(LoginActivity.class);
                        return;
                    }
                }
                changeFragment(currentPosition);
            }
        });
        changeFragment(0);
    }


    private void initFragment() {
        mFragmentList.add(HomeFragment.newInstance());
        mFragmentList.add(MallFragment.newInstance());
        mFragmentList.add(DiscoverFragment.newInstance());
        mFragmentList.add(AppointmentFragment.newInstance());
        mFragmentList.add(MyFragment.newInstance());
    }

    private void changeFragment(int currentPosition) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_content, mFragmentList.get(currentPosition));
        transaction.commit();
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

}
