package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.di.component.DaggerDoctorMainComponent;
import me.jessyan.mvparms.demo.di.module.DoctorMainModule;
import me.jessyan.mvparms.demo.mvp.contract.DoctorMainContract;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorSkill;
import me.jessyan.mvparms.demo.mvp.presenter.DoctorMainPresenter;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.ui.widget.ShapeImageView;


import static com.jess.arms.utils.Preconditions.checkNotNull;


public class DoctorMainActivity extends BaseActivity<DoctorMainPresenter> implements DoctorMainContract.View {

    public static final String KEY_FOR_DOCTOR_ID = "key_for_doctor_id";

    public static final String LIKE = "1";
    public static final String UNLIKE = "0";

    private String doctorId;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    View back;

    @BindView(R.id.head_image)
    ShapeImageView head_image;
    @BindView(R.id.doctor_name)
    TextView doctor_name;
    @BindView(R.id.comment_count)
    TextView comment_count;
    @BindView(R.id.rating)
    RatingBar rating;
    @BindView(R.id.hit_good)
    View hit_good;
    boolean isLike;
    @BindView(R.id.hosp_info)
    TextView hosp_info;
    @BindView(R.id.addr_info)
    TextView addr_info;
    @BindView(R.id.skill_list)
    TagFlowLayout skill_list;

    @BindView(R.id.doctor_intro)
    View doctor_intro;

    @Inject
    ImageLoader mImageLoader;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerDoctorMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .doctorMainModule(new DoctorMainModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_doctor_main; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        doctorId = getIntent().getStringExtra(KEY_FOR_DOCTOR_ID);
        title.setText("医生主页");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMyself();
            }
        });
        hit_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLike){
                    mPresenter.unlikeDoctor(doctorId);
                }else{
                    mPresenter.likeDoctor(doctorId);
                }
            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    public void updateDoctorInfo(DoctorBean doctorBean){
        mImageLoader.loadImage(this,
                ImageConfigImpl
                        .builder()
                        .url(doctorBean.getHeadImage())
                        .imageView(head_image)
                        .build());
        doctor_name.setText(doctorBean.getName());
        comment_count.setText(""+doctorBean.getComment());
        rating.setRating(doctorBean.getStar());
        updateLikeImage(LIKE.equals(doctorBean.getIsPraise()));
        hosp_info.setText(doctorBean.getHospitalBean().getName());
        List<DoctorSkill> doctorSkillList = doctorBean.getDoctorSkillList();
        TagAdapter<DoctorSkill> adapter = new TagAdapter<DoctorSkill>(new ArrayList<>(doctorSkillList)) {
            @Override
            public View getView(FlowLayout parent, int position, DoctorSkill s) {
                TextView tv = (TextView) LayoutInflater.from(ArmsUtils.getContext()).inflate(R.layout.doctor_main_skill_item, null, false);
                tv.setText(s.getProjectName());
                tv.setTextColor(Color.BLACK);
                return tv;
            }
        };
        skill_list.setAdapter(adapter);
        skill_list.setMaxSelectCount(0);

        doctor_intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorMainActivity.this,DoctorIntorActivity.class);
                intent.putExtra(DoctorIntorActivity.KEY_FOR_DOCTOR_BEAN,doctorBean);
                ArmsUtils.startActivity(intent);
            }
        });
    }

    @Override
    public void updateLikeImage(boolean isLike) {
        this.isLike = isLike;
        hit_good.setBackground(getResources().getDrawable(isLike ? R.mipmap.hit_good_yes : R.mipmap.hit_good_no));
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
