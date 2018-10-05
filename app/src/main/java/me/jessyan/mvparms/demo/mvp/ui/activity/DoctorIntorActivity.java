package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerDoctorIntorComponent;
import me.jessyan.mvparms.demo.di.module.DoctorIntorModule;
import me.jessyan.mvparms.demo.mvp.contract.DoctorIntorContract;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorIntorBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorSkill;
import me.jessyan.mvparms.demo.mvp.presenter.DoctorIntorPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class DoctorIntorActivity extends BaseActivity<DoctorIntorPresenter> implements DoctorIntorContract.View, View.OnClickListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.intro)
    WebView doctorInfoWV;
    @BindView(R.id.head_image)
    ImageView head_image;
    @BindView(R.id.doctor_name)
    TextView doctor_name;
    @BindView(R.id.hosp_info)
    TextView dutyTV;
    @BindView(R.id.addr_info)
    TextView skillTV;
    @Inject
    ImageLoader mImageLoader;

    private Mobile mobile = new Mobile();
    private WebViewClient mClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            mobile.onGetWebContentHeight();
        }
    };

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
        backV.setOnClickListener(this);
        doctorInfoWV.getSettings().setUseWideViewPort(true);
        doctorInfoWV.getSettings().setLoadWithOverviewMode(true);
        doctorInfoWV.addJavascriptInterface(mobile, "mobile");
        doctorInfoWV.setWebViewClient(mClient);
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    public void update(DoctorIntorBean doctorIntorBean) {
        mImageLoader.loadImage(this,
                ImageConfigImpl
                        .builder()
                        .isCenterCrop(true)
                        .placeholder(R.mipmap.place_holder_user)
                        .url(doctorIntorBean.getHeadImage())
                        .imageView(head_image)
                        .build());
        doctorInfoWV.loadUrl(doctorIntorBean.getIntroduce());
        doctor_name.setText(doctorIntorBean.getName());
        StringBuilder dutySB = new StringBuilder();
        if (null == doctorIntorBean.getDutyList() || doctorIntorBean.getDutyList().size() == 0) {
            dutyTV.setText("暂无");
        } else {
            for (String duty : doctorIntorBean.getDutyList()) {
                dutySB.append(duty).append(" ");
            }
        }
        dutyTV.setText(dutySB.toString());
        dutySB.delete(0, dutySB.length());
        if (null == doctorIntorBean.getDoctorSkillList() || doctorIntorBean.getDoctorSkillList().size() == 0) {
            skillTV.setText("暂无");
        } else {
            for (DoctorSkill skill : doctorIntorBean.getDoctorSkillList()) {
                dutySB.append(skill.getProjectName()).append(" ");
            }
            skillTV.setText(dutySB.toString());
        }
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

    public Activity getActivity() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
        }
    }

    private class Mobile {
        @JavascriptInterface
        public void onGetWebContentHeight() {
            //重新调整webview高度
            doctorInfoWV.post(() -> {
                if (null != doctorInfoWV) {
                    doctorInfoWV.measure(0, 0);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) doctorInfoWV.getLayoutParams();
                    layoutParams.height = doctorInfoWV.getMeasuredHeight();
                    doctorInfoWV.setLayoutParams(layoutParams);
                }
            });
        }
    }
}
