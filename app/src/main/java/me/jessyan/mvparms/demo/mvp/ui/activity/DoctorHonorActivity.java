package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.di.component.DaggerDoctorHonorComponent;
import me.jessyan.mvparms.demo.di.module.DoctorHonorModule;
import me.jessyan.mvparms.demo.mvp.contract.DoctorHonorContract;
import me.jessyan.mvparms.demo.mvp.presenter.DoctorHonorPresenter;

import me.jessyan.mvparms.demo.R;


import static com.jess.arms.utils.Preconditions.checkNotNull;


public class DoctorHonorActivity extends BaseActivity<DoctorHonorPresenter> implements DoctorHonorContract.View {

    public static final String KEY_FOR_DOCTOR_ID = "key_for_doctor_id";

    @BindView(R.id.list)
    RecyclerView list;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.Adapter mAdapter;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    View back;

    @BindView(R.id.no_date)
    View no_date;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerDoctorHonorComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .doctorHonorModule(new DoctorHonorModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_doctor_honor; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        title.setText("医生荣誉");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMyself();
            }
        });
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

    public void setEmpty(boolean isEmpty){
        list.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        no_date.setVisibility(!isEmpty ? View.GONE : View.VISIBLE);
    }
}
