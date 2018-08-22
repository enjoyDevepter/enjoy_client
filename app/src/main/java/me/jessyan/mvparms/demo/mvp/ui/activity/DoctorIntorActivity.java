package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.di.component.DaggerDoctorIntorComponent;
import me.jessyan.mvparms.demo.di.module.DoctorIntorModule;
import me.jessyan.mvparms.demo.mvp.contract.DoctorIntorContract;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorIntorBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.HospitalBean;
import me.jessyan.mvparms.demo.mvp.presenter.DoctorIntorPresenter;

import me.jessyan.mvparms.demo.R;


import static com.jess.arms.utils.Preconditions.checkNotNull;


public class DoctorIntorActivity extends BaseActivity<DoctorIntorPresenter> implements DoctorIntorContract.View {

    public static final String KEY_FOR_DOCTOR_BEAN = "key_for_doctor_bean";

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.doctor_info)
    TextView doctor_info;
    @BindView(R.id.head_image)
    ImageView head_image;
    @BindView(R.id.doctor_name)
    TextView doctor_name;
    @BindView(R.id.hosp_info)
    TextView hosp_info;

    private DoctorBean doctorBean;
    @Inject
    ImageLoader mImageLoader;



    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerDoctorIntorComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .doctorIntorModule(new DoctorIntorModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_doctor_intor; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        title.setText("医生介绍");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMyself();
            }
        });
        doctorBean = (DoctorBean) getIntent().getSerializableExtra(KEY_FOR_DOCTOR_BEAN);
        if(doctorBean == null){
            throw new NullPointerException("doctor bean can't null");
        }
        mPresenter.getDoctorInfo(doctorBean.getDoctorId());
        HospitalBean hospitalBean = doctorBean.getHospitalBean();
        if(hospitalBean != null){
            hosp_info.setText(hospitalBean.getName());
        }else{
            hosp_info.setText("");
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    public void update(DoctorIntorBean doctorIntorBean){
        mImageLoader.loadImage(this,
                ImageConfigImpl
                        .builder()
                        .url(doctorIntorBean.getHeadImage())
                        .imageView(head_image)
                        .build());
        doctor_info.setText(doctorIntorBean.getIntroduce());
        doctor_name.setText(doctorIntorBean.getName());
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
