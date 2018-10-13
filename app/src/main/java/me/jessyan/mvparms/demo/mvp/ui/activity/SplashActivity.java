package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.PermissionUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerSplashComponent;
import me.jessyan.mvparms.demo.di.module.SplashModule;
import me.jessyan.mvparms.demo.mvp.contract.SplashContract;
import me.jessyan.mvparms.demo.mvp.presenter.SplashPresenter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashContract.View, View.OnClickListener {
    @Inject
    RxPermissions mRxPermissions;
    @Inject
    RxErrorHandler mErrorHandler;
    @BindView(R.id.time)
    TextView timeTV;
    private Timer timer = new Timer();
    private int time = 3;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerSplashComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .splashModule(new SplashModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_splash; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        timeTV.setOnClickListener(this);
        PermissionUtil.requestPermissionForInit(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (time == 0) {
                                    timer.cancel();
                                    ArmsUtils.startActivity(MainActivity.class);
                                    killMyself();
                                } else {
                                    timeTV.setText("跳过 " + time);
                                    time--;
                                }

                            }
                        });
                    }
                }, 0, 1000);
            }

            @Override
            public void onRequestPermissionFailure(List<String> permissions) {
                killMyself();
            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                killMyself();
            }
        }, mRxPermissions, mErrorHandler);
    }

    @Override
    public boolean useImmersive() {
        return false;
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

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time:
                if (null != timer) {
                    timer.cancel();
                    timer = null;
                }
                ArmsUtils.startActivity(MainActivity.class);
                killMyself();
                break;
        }
    }
}
