package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerAuthenticationComponent;
import me.jessyan.mvparms.demo.di.module.AuthenticationModule;
import me.jessyan.mvparms.demo.mvp.contract.AuthenticationContract;
import me.jessyan.mvparms.demo.mvp.presenter.AuthenticationPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class AuthenticationActivity extends BaseActivity<AuthenticationPresenter> implements AuthenticationContract.View, View.OnClickListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.name)
    EditText nameET;
    @BindView(R.id.card)
    EditText cardET;
    @BindView(R.id.submit)
    View submitV;
    @BindView(R.id.opposite)
    View oppositeV;
    @BindView(R.id.front)
    View frontV;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerAuthenticationComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .authenticationModule(new AuthenticationModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_authentication; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("实名认证");
        backV.setOnClickListener(this);
        submitV.setOnClickListener(this);
        oppositeV.setOnClickListener(this);
        frontV.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.submit:
                break;
            case R.id.front:
                break;
            case R.id.opposite:
                break;
        }
    }
}
