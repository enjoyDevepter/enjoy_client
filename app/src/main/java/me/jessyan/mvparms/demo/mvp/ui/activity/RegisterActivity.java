package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerRegisterComponent;
import me.jessyan.mvparms.demo.di.module.RegisterModule;
import me.jessyan.mvparms.demo.mvp.contract.RegisterContract;
import me.jessyan.mvparms.demo.mvp.presenter.RegisterPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.CodeAdapter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.View, View.OnClickListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.info)
    TextView infoTV;
    @BindView(R.id.register)
    TextView registerTV;
    @BindView(R.id.choice)
    TextView choiceV;
    @BindView(R.id.get_validate)
    TextView getValidateV;
    @BindView(R.id.validate)
    EditText validateET;
    @BindView(R.id.mobile)
    EditText mobileET;
    @BindView(R.id.password)
    EditText passwordET;
    @BindView(R.id.content)
    EditText contentET;
    @Inject
    RecyclerView.Adapter mAdapter;
    @Inject
    RxPermissions mRxPermissions;

    private int time = 60;
    private Timer timer;
    private TimerTask timerTask;

    private PopupWindow popupWindow;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerRegisterComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .registerModule(new RegisterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_register; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        infoTV.setText(Html.fromHtml("<font color='#666666'>已注册过，</font> <font color='#5FBFE3'>登录</font>"));
        backV.setOnClickListener(this);
        infoTV.setOnClickListener(this);
        choiceV.setOnClickListener(this);
        getValidateV.setOnClickListener(this);
        registerTV.setOnClickListener(this);
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
            case R.id.get_validate:
                getVerify();
                break;
            case R.id.register:
                register();
                break;
            case R.id.choice:
                showType();
                break;
            case R.id.info:
                killMyself();
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
        if (time == 30 || time <= 0) {
            time = 29;
            initTimer();
            timer.schedule(timerTask, 0, 1000);
            mPresenter.getVerify(mobileET.getText().toString());
        }
    }

    private void initTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                getValidateV.post(new Runnable() {
                    @Override
                    public void run() {
                        if (time <= 0 && timer != null) {
                            timer.cancel();
                            timer = null;
                            timerTask.cancel();
                            timerTask = null;
                            getValidateV.setText("重新获取");
                        } else {
                            getValidateV.setText(time + "s");
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

    private void register() {
        mPresenter.register(mobileET.getText().toString(), passwordET.getText().toString(), validateET.getText().toString(), String.valueOf(1), contentET.getText().toString());
    }


    private void showType() {

        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }

        RecyclerView recyclerView = new RecyclerView(this);
        ArmsUtils.configRecyclerView(recyclerView, new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        ((CodeAdapter) mAdapter).setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                choiceV.setText(String.valueOf(data));
                if (popupWindow != null) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            }
        });
        popupWindow = new PopupWindow(recyclerView, ArmsUtils.getDimens(this, R.dimen.code_width), LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置背景
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(choiceV, 0, -ArmsUtils.getDimens(this, R.dimen.code_height));
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showVerity() {
        time = 0;
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }
}
