package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerForgetComponent;
import me.jessyan.mvparms.demo.di.module.ForgetModule;
import me.jessyan.mvparms.demo.mvp.contract.ForgetContract;
import me.jessyan.mvparms.demo.mvp.presenter.ForgetPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ForgetActivity extends BaseActivity<ForgetPresenter> implements ForgetContract.View, View.OnClickListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.mobile)
    EditText mobileET;
    @BindView(R.id.vertify)
    EditText veritfyET;
    @BindView(R.id.get_vertify)
    TextView getVertifyV;
    @BindView(R.id.forget)
    View forgetV;

    private int time = 60;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerForgetComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .forgetModule(new ForgetModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_forget; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        getVertifyV.setOnClickListener(this);
        forgetV.setOnClickListener(this);
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
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.forget:
                mPresenter.find(mobileET.getText().toString(), veritfyET.getText().toString());
                break;
            case R.id.get_vertify:
                getVerify();
                break;
        }
    }

    private void getVerify() {
        if (ArmsUtils.isEmpty(mobileET.getText().toString())) {
            showMessage("请输入手机号码");
            return;
        }

        if (!ArmsUtils.isPhoneNum(mobileET.getText().toString())) {
            showMessage("手机号码格式不正确");
            return;
        }

        if (time == 60 || time <= 0) {
            time = 59;
            initTimer();
            timer.schedule(timerTask, 0, 1000);
            mPresenter.getVerifyForFind(mobileET.getText().toString());
        }
    }

    private void initTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                getVertifyV.post(new Runnable() {
                    @Override
                    public void run() {
                        if (time <= 0 && timer != null) {
                            timer.cancel();
                            timer = null;
                            timerTask.cancel();
                            timerTask = null;
                            getVertifyV.setText("重新获取");
                        } else {
                            getVertifyV.setText(time + "s");
                        }
                        time--;
                    }
                });
            }
        };

        timer = new Timer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    @Override
    public void showVerity() {
        time = 0;
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
