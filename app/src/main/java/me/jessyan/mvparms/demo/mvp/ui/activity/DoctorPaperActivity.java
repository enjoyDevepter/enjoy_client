package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.di.component.DaggerDoctorPaperComponent;
import me.jessyan.mvparms.demo.di.module.DoctorPaperModule;
import me.jessyan.mvparms.demo.mvp.contract.DoctorPaperContract;
import me.jessyan.mvparms.demo.mvp.presenter.DoctorPaperPresenter;

import me.jessyan.mvparms.demo.R;


import static com.jess.arms.utils.Preconditions.checkNotNull;


public class DoctorPaperActivity extends BaseActivity<DoctorPaperPresenter> implements DoctorPaperContract.View {

    public static final String KEY_FOR_DOCTOR_ID = "key_for_doctor_id";

    @BindView(R.id.list)
    RecyclerView list;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.Adapter mAdapter;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerDoctorPaperComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .doctorPaperModule(new DoctorPaperModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_doctor_paper; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        ArmsUtils.configRecyclerView(list, mLayoutManager);
        list.setAdapter(mAdapter);
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

    public Activity getActivity(){
        return this;
    }
}
