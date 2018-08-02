package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerTaoCanDetailsComponent;
import me.jessyan.mvparms.demo.di.module.TaoCanDetailsModule;
import me.jessyan.mvparms.demo.mvp.contract.TaoCanDetailsContract;
import me.jessyan.mvparms.demo.mvp.presenter.TaoCanDetailsPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class TaoCanDetailsActivity extends BaseActivity<TaoCanDetailsPresenter> implements TaoCanDetailsContract.View {

    @BindView(R.id.tab)
    TabLayout tabLayout;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerTaoCanDetailsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .taoCanDetailsModule(new TaoCanDetailsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_tao_can_details; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        tabLayout.addTab(tabLayout.newTab().setText("套餐详情"));
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
}
