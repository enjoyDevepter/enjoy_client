package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import me.jessyan.mvparms.demo.di.component.DaggerCashPasswordComponent;
import me.jessyan.mvparms.demo.di.module.CashPasswordModule;
import me.jessyan.mvparms.demo.mvp.contract.CashPasswordContract;
import me.jessyan.mvparms.demo.mvp.presenter.CashPasswordPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 设置提现密码
 */
public class CashPasswordActivity extends BaseActivity<CashPasswordPresenter> implements CashPasswordContract.View, View.OnClickListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    View back;

    @BindView(R.id.old)
    EditText old;
    @BindView(R.id.newly)
    EditText newly;
    @BindView(R.id.confirm)
    EditText confirm;

    @BindView(R.id.get_code)
    TextView get_code;
    @BindView(R.id.commit)
    View commit;
    private int time = 60;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCashPasswordComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .cashPasswordModule(new CashPasswordModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_cash_password; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        title.setText("设置提现密码");
        back.setOnClickListener(this);
        get_code.setOnClickListener(this);
        commit.setOnClickListener(this);
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

    public void showOk() {
        ArmsUtils.makeText(this, "设置成功");
        newly.setText("");
        old.setText("");
        confirm.setText("");
    }

    private void getVerify() {

        if (time == 60 || time <= 0) {
            time = 59;
            initTimer();
            timer.schedule(timerTask, 0, 1000);
            mPresenter.getVerifyForFind();
        }
    }

    private void initTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                get_code.post(new Runnable() {
                    @Override
                    public void run() {
                        if (time <= 0 && timer != null) {
                            timer.cancel();
                            timer = null;
                            timerTask.cancel();
                            timerTask = null;
                            get_code.setText("重新获取");
                        } else {
                            get_code.setText(time + "s");
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.get_code:
                getVerify();
                break;
            case R.id.confirm:
                if (old.getText() == null || TextUtils.isEmpty(old.getText().toString())) {
                    ArmsUtils.makeText(ArmsUtils.getContext(), "请输入密码");
                    return;
                }
                if (newly.getText() == null || TextUtils.isEmpty(newly.getText().toString())) {
                    ArmsUtils.makeText(ArmsUtils.getContext(), "请重复密码");
                    return;
                }
                if (confirm.getText() == null || TextUtils.isEmpty(confirm.getText().toString())) {
                    ArmsUtils.makeText(ArmsUtils.getContext(), "请输入验证码");
                    return;
                }
                mPresenter.setCashPassword(newly.getText().toString(), old.getText().toString(), confirm.getText().toString());
                break;
        }
    }
}
