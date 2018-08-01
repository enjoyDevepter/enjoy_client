package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

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
    View getVertifyV;
    @BindView(R.id.forget)
    View forgetV;

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
                mPresenter.getVerifyForFind(mobileET.getText().toString());
                break;
        }
    }
}
